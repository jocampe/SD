package pt.tecnico.sauron.silo;

import java.util.ArrayList;
import java.util.List;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import pt.tecnico.sauron.silo.Domain.Operations;
import pt.tecnico.sauron.silo.Domain.Exception.DuplicateCameraException;
import pt.tecnico.sauron.silo.Domain.Exception.InvalidCameraNameException;
import pt.tecnico.sauron.silo.Domain.Exception.NoSuchCameraException;
import pt.tecnico.sauron.silo.Domain.Exception.NoSuchObjectException;
import pt.tecnico.sauron.silo.Domain.Exception.WrongTypeException;
import pt.tecnico.sauron.silo.Domain.Camera;
import pt.tecnico.sauron.silo.Domain.Coordinates;
import pt.tecnico.sauron.silo.Domain.Observation;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;
import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.NOT_FOUND;

public class ServerImpl extends SiloServiceGrpc.SiloServiceImplBase {
	
	private Operations op = new Operations();

	
	//Transform - Transforma uma observacao local em observacao grpc

	public ObservationGrpc transform(Observation observation) { 
		return ObservationGrpc.newBuilder()
				.setType(observation.getType()) 
				.setId(observation.getId())
				.setTime(observation.getTime())
				.setCamera(observation.getCam())
				.build();
	}
	//TransformList2 - Transforma uma lista de observacao grpc em observacao local
	public Iterable<?extends Observation> transformList2(Iterable<?extends ObservationGrpc> lst) {
		List<Observation> obsLst = new ArrayList<>();
		for(ObservationGrpc element : lst)
		{
			Observation obs = new Observation(element.getId(), element.getType(), element.getTime(), element.getCamera());
			obsLst.add(obs);
		}
		return obsLst;
	}
	//TransformCoord - transforma coordenadas locais em coordenadas grpc
	public CoordinatesGrpc transformCoord(Coordinates coordinates) {
		return CoordinatesGrpc.newBuilder()
				.setLat(coordinates.getLat())
				.setLon(coordinates.getLon())
				.build();
	}

	public CoordinatesGrpc transformCoord(Double lat, Double lon) {
		return CoordinatesGrpc.newBuilder()
				.setLat(lat)
				.setLon(lon)
				.build();
	}
	

	//TransformList - Transforma uma lista de observacao local em uma lista de observacao grpc
	public Iterable<?extends ObservationGrpc> transformList(Iterable<?extends Observation> lst) {
		List<ObservationGrpc> grpcLst = new ArrayList<>();
		for(Observation element : lst)
		{
			grpcLst.add(ObservationGrpc.newBuilder()
					.setType(element.getType()) 
					.setId(element.getId())
					.setTime(element.getTime())
					.setCamera(element.getCam())
					.build());
		}
		return grpcLst;
	}
	public Iterable<?extends CameraGrpc> transformListCam(Iterable<?extends Camera> lst) {
		List<CameraGrpc> grpcLst = new ArrayList<>();
		for(Camera element : lst)
		{
			grpcLst.add(CameraGrpc.newBuilder()
					.setCamera(element.getName())
					.setCoordinates(this.transformCoord(element.getLatitude(), element.getLongitude()))
					.addAllObservation(this.transformList(element.getObservation()))
					.build());
		}
		return grpcLst;
	}
	@Override
	public void updateMessage(UpdateMessage request, StreamObserver<UpdateMessage> responseObserver) {
		UpdateMessage response = UpdateMessage.newBuilder()
				.addAllRepTS(this.op.getReplicaTimestamp())
				.addAllObservation(this.transformList(this.op.getUpdateLogObs()))
				.addAllCamera(this.transformListCam((this.op.getUpdateLogCam())))
				.build();
				
		responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
	
	@Override
	public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {

	    String input = request.getText();
	    if(input.equals(null)) {
	    	responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Input cannot be empty!").asRuntimeException());
	    }
	    String output = "Hello " + input + "!";
	    PingResponse response = PingResponse.newBuilder().setText(output).build();
	    responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
	
	@Override
	public void clear(ClearRequest request, StreamObserver<ClearResponse> responseObserver) {
		op.clear();
		ClearResponse response = ClearResponse.getDefaultInstance();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void init(InitRequest request, StreamObserver<InitResponse> responseObserver) {
		//TO_DO
		InitResponse response = InitResponse.getDefaultInstance();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void track(TrackRequest request, StreamObserver<TrackResponse> responseObserver) {
		String objType = request.getType();
		String objId = request.getId();
		if(objType.equals(null)) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid object type! Type must not be empty or null").asRuntimeException());
		}
		if(objId.equals(null)) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid object ID! ID must not be empty or null").asRuntimeException());
		}
		TrackResponse response;
		try {
			response = TrackResponse.newBuilder()
					.setObservation(transform(this.op.track(request.getType(), request.getId())))
					.addAllNew(this.op.valueTimestampUpdate(request.getPrevList()))
					.build();
			responseObserver.onNext(response);
		    responseObserver.onCompleted();		
		} catch (NoSuchObjectException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No object found with given ID!").asRuntimeException());

		} catch (WrongTypeException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No object found with given type!").asRuntimeException());
		}
	}
	
	@Override
	public void trackMatch(TrackMatchRequest request, StreamObserver<TrackMatchResponse> responseObserver) {
		try {
		TrackMatchResponse response = 
				TrackMatchResponse.newBuilder()
				.addAllObservation(this.transformList(this.op.trackMatch(request.getType(), request.getId())))
				.addAllNew(this.op.valueTimestampUpdate(request.getPrevList()))
				.build();
		responseObserver.onNext(response);
	    responseObserver.onCompleted();
		}
		catch(WrongTypeException e){
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No object found with given type!").asRuntimeException());
		}
		catch(NoSuchObjectException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No object found with given ID!").asRuntimeException());
		}
	}
	
	@Override
	public void trace(TraceRequest request, StreamObserver<TraceResponse> responseObserver) {
		try {
		TraceResponse response = 
				TraceResponse.newBuilder()
				.addAllObservation(this.transformList(this.op.trace(request.getType(), request.getId())))
				.addAllNew(this.op.valueTimestampUpdate(request.getPrevList()))
				.build();
		responseObserver.onNext(response);
	    responseObserver.onCompleted();
		}
		catch(WrongTypeException e){
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No object found with given type!").asRuntimeException());
		}
		catch(NoSuchObjectException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No object found with given ID!").asRuntimeException());
		}
	}
	
	@Override
	public void report(ReportRequest request, StreamObserver<ReportResponse> responseObserver) {
		ReportResponse response = ReportResponse.newBuilder().addAllNew(this.op.valueTimestampUpdate(request.getPrevList())).build();
		try {
			this.op.report(request.getName(), this.transformList2(request.getObservationList()));
			responseObserver.onNext(response);
		    responseObserver.onCompleted();			
		} catch (InvalidCameraNameException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid camera name!  Name cannot be empty and must be between 3 and 15 characters long").asRuntimeException());
			}
		  catch(NoSuchCameraException e) {
			  responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No camera found with given name!").asRuntimeException());
		  }
		}
	

	@Override
	public void camJoin(CamJoinRequest request, StreamObserver<CamJoinResponse> responseObserver) {
	  CamJoinResponse response = CamJoinResponse.newBuilder().addAllNew(this.op.valueTimestampUpdate(request.getPrevList())).build();
	  try {
		this.op.cam_join(request.getName(), request.getCoordinates().getLat(), request.getCoordinates().getLon());
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	} catch (InvalidCameraNameException e) {
		responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid camera name!  Name cannot be empty and must be between 3 and 15 characters long").asRuntimeException());
		
	} catch (DuplicateCameraException e) {
		responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Camera already exists!").asRuntimeException());
	}

	}

	
	@Override
	public void camInfo(CamInfoRequest request, StreamObserver<CamInfoResponse> responseObserver) {
		String camName = request.getName();
	  	CamInfoResponse response;
		try {
			response = CamInfoResponse.newBuilder()
					  .setCoordinates(this.transformCoord((op.cam_info(request.getName()))))
					  .addAllNew(this.op.valueTimestampUpdate(request.getPrevList()))
					  .build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} catch (NoSuchCameraException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("No camera found with given name!").asRuntimeException());
		}
	  }
	
	public Operations getOperations() {
		return op;
	}
}

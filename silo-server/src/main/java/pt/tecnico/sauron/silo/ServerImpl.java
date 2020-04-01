package pt.tecnico.sauron.silo;

import io.grpc.stub.StreamObserver;
import pt.tecnico.sauron.silo.Domain.Operations;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;

public class ServerImpl extends SiloServiceGrpc.SiloServiceImplBase {
	
	private Operations op = new Operations();

	@Override
	public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {

	    String input = request.getText();
	    String output = "Hello " + input + "!";
	    PingResponse response = PingResponse.newBuilder().setText(output).build();
	    responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
	
	@Override
	public void track(TrackRequest request, StreamObserver<TrackResponse> responseObserver) {
		TrackResponse response = 
				TrackResponse.newBuilder()
					.setObservation(op.track(request.getType(), request.getId())).build();
		responseObserver.onNext(response);
	    responseObserver.onCompleted();					
	}
	
	@Override
	public void trackMatch(TrackMatchRequest request, StreamObserver<TrackMatchResponse> responseObserver) {
		TrackMatchResponse response = 
				TrackMatchResponse.newBuilder()
				.addAllObservation(op.trackMatch(request.getType(), request.getId())).build();
		responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
	
	@Override
	public void trace(TraceRequest request, StreamObserver<TraceResponse> responseObserver) {
		TraceResponse response = 
				TraceResponse.newBuilder()
				.addAllObservation(op.trace(request.getType(), request.getId())).build();
		responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
}


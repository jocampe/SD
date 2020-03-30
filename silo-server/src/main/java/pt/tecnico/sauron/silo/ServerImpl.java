package pt.tecnico.sauron.silo;

import io.grpc.stub.StreamObserver;
import pt.tecnico.sauron.silo.grpc.Silo.PingRequest;
import pt.tecnico.sauron.silo.grpc.Silo.PingResponse;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;

public class ServerImpl extends SiloServiceGrpc.SiloServiceImplBase {

	@Override
	public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {

	    String input = request.getText();
	    String output = "Hello " + input + "!";
	    PingResponse response = PingResponse.newBuilder().setText(output).build();
	    responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
}


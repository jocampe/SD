package pt.tecnico.sauron.silo.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;


public class SiloFrontend implements AutoCloseable {
	
	private final ManagedChannel channel;
	private final SiloServiceGrpc.SiloServiceBlockingStub stub;
	
	public SiloFrontend(String host, int port) {
		// Channel is the abstraction to connect to a service endpoint.
		// Let us use plaintext communication because we do not have certificates.
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		// Create a blocking stub.
		stub = SiloServiceGrpc.newBlockingStub(channel);
	
	}
	
	public PingResponse setPing(PingRequest request) {
		return stub.ping(request);
	}
	
	public TrackResponse getTrack(TrackRequest request) {
		return stub.track(request);
	}
	
	
	public CamJoinResponse camJoin(CamJoinRequest request) {
		return stub.camJoin(request);
	}
	
	public CamInfoResponse camInfo(CamInfoRequest request) {
		return stub.camInfo(request);
	}

	
	@Override
	public final void close() {
		channel.shutdown();
	}

}

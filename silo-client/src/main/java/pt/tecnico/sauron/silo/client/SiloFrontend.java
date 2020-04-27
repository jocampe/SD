package pt.tecnico.sauron.silo.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.Status;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;


public class SiloFrontend implements AutoCloseable {
	
	private final ManagedChannel channel;
	private final SiloServiceGrpc.SiloServiceBlockingStub stub;
	
	public SiloFrontend(String zooHost, String zooPort, String path) {
		// Channel is the abstraction to connect to a service endpoint.
		// Let us use plaintext communication because we do not have certificates.
		
		// receive and print arguments
		/*
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		*/
		
		ZKNaming zkNaming = new ZKNaming(zooHost, zooPort);
		
		// lookup
		ZKRecord record = zkNaming.lookup(path);
		String target = record.getURI();
		
		this.channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

		// Create a blocking stub.
		stub = SiloServiceGrpc.newBlockingStub(channel);
	
	}
	
	public PingResponse setPing(PingRequest request) {
		return stub.ping(request);
	}
	
	public ClearResponse setClear(ClearRequest request) {
		return stub.clear(request);
	}
	
	public TrackResponse track(TrackRequest request) {
		return stub.track(request);
	}
	
	public TraceResponse trace(TraceRequest request) {
		return stub.trace(request);
	}
	
	public CamJoinResponse camJoin(CamJoinRequest request) {
		return stub.camJoin(request);
	}
	
	public CamInfoResponse camInfo(CamInfoRequest request) {
		return stub.camInfo(request);
	}
	
	public ReportResponse report(ReportRequest request) {
		return stub.report(request);
	}
	
	public TrackMatchResponse trackMatch(TrackMatchRequest request) {
		return stub.trackMatch(request);
	}


	@Override
	public final void close() {
		channel.shutdown();
	}

}

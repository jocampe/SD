package pt.tecnico.sauron.silo.client;

import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;


public class SiloFrontend implements AutoCloseable {
	
	private final ManagedChannel channel;
	private final SiloServiceGrpc.SiloServiceBlockingStub stub;
	ZKNaming zkNaming;
	private String _path = "/grpc/sauron/silo";
	private String target;
	private int repCount;
	
	public SiloFrontend(String zooHost, String zooPort, String replica) {
		try {
			zkNaming = new ZKNaming(zooHost,zooPort);
			this.setClientPath(replica);
			this.setRepCount();
			// lookup
			ZKRecord record = zkNaming.lookup(_path);
			target = record.getURI();

		} catch (ZKNamingException e) {
			e.printStackTrace();
		}
		channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		stub = SiloServiceGrpc.newBlockingStub(channel);
	}
	
	public void setClientPath(String replica) throws ZKNamingException {
		//se encontra 0 ->erro, 1->escolhe esse ou n->random
		//verificar se o num da replica e ou nao fornecido
		Collection<ZKRecord> repCollection;
		repCollection = getRecords();
		
		int size = repCollection.size();
		//se nao ha replicas -> erro
		if(size == 0) {
			//error
			System.out.println("ERROR SIZE: "+size);
		}
		//se o numero da replica nao esta definido
		if(replica.equals("0")) {
			System.out.println("it is 0");
			if(size == 1) {
				_path = repCollection.toArray(ZKRecord[]::new)[0].getPath();
			}
			else {
				Random rand = new Random();
				int randNumber = rand.nextInt(size-1);
				_path = repCollection.toArray(ZKRecord[]::new)[randNumber].getPath();
			}
		}
		else {
			_path += "/" + replica;
		}
		System.out.println("THIS IS THE CLIENT PATH:" + _path);	
	}
	
	public Collection<ZKRecord> getRecords() throws ZKNamingException {
		return this.zkNaming.listRecords(_path);
	}
	public void setRepCount() throws ZKNamingException {
		repCount = getRecords().size();
	}
	public int getRepCount() {
		return repCount;
	}
	
	public synchronized PingResponse setPing(PingRequest request) {
		return stub.ping(request);
	}
	public synchronized ClearResponse setClear(ClearRequest request) {
		return stub.clear(request);
	}
	
	public synchronized TrackResponse track(TrackRequest request) {
		return stub.track(request);
	}
	
	public synchronized TraceResponse trace(TraceRequest request) {
		return stub.trace(request);
	}
	
	public synchronized CamJoinResponse camJoin(CamJoinRequest request) {
		return stub.camJoin(request);
	}
	
	public synchronized CamInfoResponse camInfo(CamInfoRequest request) {
		return stub.camInfo(request);
	}
	
	public synchronized ReportResponse report(ReportRequest request) {
		return stub.report(request);
	}
	
	public synchronized TrackMatchResponse trackMatch(TrackMatchRequest request) {
		return stub.trackMatch(request);
	}


	@Override
	public final void close() {
		channel.shutdown();
	}

}

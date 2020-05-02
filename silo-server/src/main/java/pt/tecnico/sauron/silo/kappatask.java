package pt.tecnico.sauron.silo;

import java.util.Collection;
import java.util.TimerTask;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.sauron.silo.grpc.Silo.CamJoinRequest;
import pt.tecnico.sauron.silo.grpc.Silo.CamJoinResponse;
import pt.tecnico.sauron.silo.grpc.Silo.CoordinatesGrpc;
import pt.tecnico.sauron.silo.grpc.Silo.PingRequest;
import pt.tecnico.sauron.silo.grpc.Silo.PingResponse;
import pt.tecnico.sauron.silo.grpc.Silo.UpdateMessage;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;
import pt.tecnico.sauron.silo.ServerImpl;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

public  class kappatask extends TimerTask{ 
	public String _zooHost;
	public String _zooPort;
	public String _path;
	public ZKNaming _zkNaming;
	private ManagedChannel channel;
	private  SiloServiceGrpc.SiloServiceBlockingStub stub;
	private String target;
	public int _curr;
	
	
	public kappatask(String zooHost, String zooPort, String path, ZKNaming zkNaming, int curr) {
		this._zooHost = zooHost;
		this._zooPort = zooPort;
		this._path = path;
		this._zkNaming = zkNaming;
		this._curr = curr;
	}
    public void run() 
    { 
    	Collection<ZKRecord> repCollection;
		try {
			String cutPath = _path.substring(0, _path.length() - 2);
			repCollection = this._zkNaming.listRecords(cutPath);
			
		int size = repCollection.size();
		String[] repPaths = new String[2];
		
		if(size > 1) {
			if (size == 2) {
				
				if(this._curr == 1) {
					_path = repCollection.toArray(ZKRecord[]::new)[1].getPath();
					repPaths[0] = _path;
				}
				if(this._curr == 2)
					_path = repCollection.toArray(ZKRecord[]::new)[0].getPath();
					repPaths[0] = _path;
			}
			else {
				if(this._curr == 1) {
					_path = repCollection.toArray(ZKRecord[]::new)[1].getPath();
					repPaths[0] = _path;
				}
				if(this._curr == size) {
					_path = repCollection.toArray(ZKRecord[]::new)[size -1].getPath();
					repPaths[0] = _path;
				}
				else {
					for(int i =1; i < size-1; i++) {
						_path = repCollection.toArray(ZKRecord[]::new)[i-1].getPath();
						repPaths[0] = _path;
						_path = repCollection.toArray(ZKRecord[]::new)[i+1].getPath();
						repPaths[1] = _path;
				
					}
				}
			}
			for(int i =0; i < 2; i++) {
				ZKRecord record = _zkNaming.lookup(repPaths[i]);
				target = record.getURI();
				channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
				stub = SiloServiceGrpc.newBlockingStub(channel);
				
				//sendo op, o objeto Operations
				//recebe o update log da replica i e faz os updates
				//setUpdates(stub.updateMessage(getUpdates()));
				
				
				PingResponse response = stub.ping(PingRequest.newBuilder().setText("friend").build());
				System.out.println(response);
				
				
				channel.shutdown();
			}
		}
			
		} catch (ZKNamingException e) {}
		
    }
    /*public UpdateMessage getUpdates() {
    	UpdateMessage request = UpdateMessage.newBuilder()
    			.addAllRepTS(op.getReplicaTimestamp)
    			.addAllObservation(op.getUpdateLogObs)
    			.addAllCamera(op.getUpdateLogCam);
    			.build();
    	return request;
    }
    public void setUpdates(UpdateMessage response) {
    	
    }*/
}


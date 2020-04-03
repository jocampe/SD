package pt.tecnico.sauron.silo.Domain;

import com.google.protobuf.Timestamp;

public class Observation {
	
	private String id;
	private String type;
	public Timestamp timestamp; 

	
	public Observation (String _id, String _type, Timestamp _timestamp) {
		this.id = _id;
		this.type = _type;
		this.timestamp = _timestamp;
		
	}
	
	
	public String getId() {
		return this.id;
	}
	public void setId(String _id) {
		this.id = _id;
	}
	
	public String getType() {
		return this.type;
	}
	public void setType(String _type) {
		this.type = _type;
	}
	
	public Timestamp getTime() {
		return this.timestamp;
	}
	public void setTime(Timestamp _timestamp) {
		this.timestamp = _timestamp;
	}
	
}

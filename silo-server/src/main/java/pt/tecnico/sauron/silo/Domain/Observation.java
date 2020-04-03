package pt.tecnico.sauron.silo.Domain;

import com.google.protobuf.Timestamp;

public class Observation {
	
	private String id;
	private String type;
	public Timestamp timestamp;
	public String camera = null;
	private double latitude = 0;
	private double longitude = 0;

	
	public Observation (String _id, String _type, Timestamp _timestamp,String _camera, double _latitude, double _longitude) {
		this.id = _id;
		this.type = _type;
		this.timestamp = _timestamp;
		this.camera = _camera;
		this.latitude = _latitude;
		this.longitude = _longitude;
	}
	public Observation (String _id, String _type, Timestamp _timestamp, String _cam) {
		this.id = _id;
		this.type = _type;
		this.timestamp = _timestamp;
		this.camera = _cam;
	}
	
	
	public String getId() {
		return this.id;
	}
	public void setId(String _id) {
		this.id = _id;
	}
	public double getLat() {
		return this.latitude;
	}
	public void setLat(double _latitude) {
		this.latitude = _latitude;
	}
	public double getLon() {
		return this.longitude;
	}
	public void setLon(double _longitude) {
		this.latitude = _longitude;
	}
	
	public String getCam() {
		return this.camera;
	}
	public void setCam(String _camera) {
		this.camera = _camera;
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

package pt.tecnico.sauron.silo.Domain;

import java.util.ArrayList;
import java.util.List;
import pt.tecnico.sauron.silo.Domain.*;


public class Camera {
	
	private String _name;
	private double _latitude;
	private double _longitude;
	private List<Observation> _obsList = new ArrayList<>();
	
	public Camera(String name, double latitude, double longitude) {
		_name = name;
		_latitude = latitude;
		_longitude = longitude;
	}
	
	public String getName() {return _name;}
	public double getLatitude() {return _latitude;}
	public double getLongitude() {return _longitude;}
	public List getObservation() {return _obsList;}
	public void addObservation(Observation observation) { _obsList.add(observation);}
	
	
}


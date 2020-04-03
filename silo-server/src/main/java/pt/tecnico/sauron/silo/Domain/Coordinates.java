package pt.tecnico.sauron.silo.Domain;

public class Coordinates {
	private double _lat;
	private double _lon;
	
	public Coordinates(double lat, double lon) {
		_lat = lat;
		_lon = lon;
	}
	
	public double getLat() {return _lat;}
	public double getLon() {return _lon;}
	
}

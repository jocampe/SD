package pt.tecnico.sauron.silo.Domain;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;




public class Operations {
	
	private Map<String,Object> object = new TreeMap<>();
	private Map<String, Camera> _cameras = new TreeMap<>();
	
	public Operations() {}
		
	
	public Observation track(String type, String id) {
		return object.get(id).getLastObservation();
	}
	
	//observacao mais recente de cada id que der match
	public Iterable<?extends Observation> trackMatch(String type, String id) {
		
		List<Observation> lst = new ArrayList<>();

		int index = id.indexOf('*');
		if(index != -1) 
			id = id.substring(0, index) + "." + id.substring(index, id.length());
		
		for(Map.Entry<String,Object> entry : object.entrySet()) {
			if(Pattern.matches(id, entry.getKey()))
				lst.add(object.get(entry.getKey()).getLastObservation());
		}
		return lst;
	}
	
	//a ordenacao e so fazer print ao ultimo ao primeiro membro da lista
	public Iterable<?extends Observation> trace(String type, String id) {
		return object.get(id).getObservationList();
		
	}
	

	public void report (String name, Iterable<?extends Observation>observation){
			Camera cam = _cameras.get(name);
			if(cam != null) {
				Instant time = Instant.now();
				Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
				for(Observation element : observation ) {
					if (object.get(element.getId()) == null) {
					Object object2 = new Object(element.getId(), element.getType());
					element.setCam(name);
					element.setLat(cam.getLatitude());
					element.setLon(cam.getLongitude());
					element.setTime(timestamp);
					object2.addObservation(element);
					object.put(element.getId(), object2);
					}
					else {
						element.setCam(name);
						element.setLat(cam.getLatitude());
						element.setLon(cam.getLongitude());
						element.setTime(timestamp);
						object.get(element.getId()).addObservation(element);
					}
				}
			}
			
	}

	public synchronized void cam_join(String name, double latitude, double longitude) {
		  /*
		   * try{
		   * 	Camera eye = new Camera(name, latitude, longitude);
		   * 	_cameras.put(name, camera);
		   * }
		   * catch (InvalidCameraNameException icne) {System.out.println("Invalid Camera name.\n");}
		   * catch (DuplicateCameraException dce) {System.out.println("Duplicate Camera Name");}
		   * 
		   */
		   	Camera eye = new Camera(name, latitude, longitude);
		   	_cameras.put(name, eye);
		}

		public Coordinates cam_info(String name) { 
		  /*
		  try {
		    Camera camera = _cameras.get(name);
		    String camInfo = "Latitude: " + camera.getLatitude() + "Longitude: " + camera.getLongitude();
		    return camInfo;
		  }
		  catch(NoSuchCameraException nsce) {System.out.println("Camera doesn't exist");}
		  */
			Camera eye = _cameras.get(name);
			Coordinates coordinates = new Coordinates(eye.getLatitude(), eye.getLongitude());
			return coordinates;
		}
		/*
		public void removeCamera(String name) {
			_cameras.remove(name);
		} 
		*/
		
	

}

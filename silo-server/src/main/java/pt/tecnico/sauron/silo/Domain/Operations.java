package pt.tecnico.sauron.silo.Domain;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import pt.tecnico.sauron.silo.Domain.Exception.*;




public class Operations {
	
	private Map<String,Object> _objects = new TreeMap<>();
	private Map<String,Camera> _cameras = new TreeMap<>();
	private List<Integer> valueTimestamp = new ArrayList<>(Arrays.asList(new Integer[4]));
	
	private List<Observation> updateLogObs = new ArrayList<>();
	private List<Camera> updateLogCam = new ArrayList<>();
	private List<Integer> replicaTimestamp;
	
	public Operations() {
		//init both timestamp vecs at 0
		Collections.fill(valueTimestamp, 0);
		replicaTimestamp = valueTimestamp;
	}
		
	//verifica se pedido.prev <= value timestamp
	public synchronized List<Integer> valueTimestampUpdate(List<Integer> prev) {
		for (int i=0; i<prev.size(); i++) {
			if (prev.get(i) > valueTimestamp.get(i))
				;    //FICAR PENDENTE
		}
		return valueTimestamp;
	}
	//incrementa a entrada i
	public synchronized List<Integer> replicaTimestampUpdate(List<Integer> prev) {
		int index = 1; 	//este numero corresponde a replica em questao. tem de ser passado como arg
		Integer value = replicaTimestamp.get(index); // get value
		value = value + 1; // increment value
		replicaTimestamp.set(index, value);
		value = prev.get(index);
		value = value + 1;
		prev.set(index, value);
		return prev;
	}
	
	
	
//1 - rever que operacoes fazem realmente update. no update, o metodo acima nao e o chamado. criar outro para incrementar
//  o replicaTimestamp
//3 - timer e comunicacao replicas a funcionar entre si
//  No silo server criar metodo de enviar info e receber info usando as listas de update info

	public Observation track(String type, String id) throws NoSuchObjectException, WrongTypeException {
		Object object = _objects.get(id);
		if(object == null) {
			throw new NoSuchObjectException();
		}
		if(object.getType() != type) {
			throw new WrongTypeException();
		}
		return _objects.get(id).getLastObservation();
	}
	
	//observacao mais recente de cada id que der match
	public Iterable<?extends Observation> trackMatch(String type, String id) throws NoSuchObjectException, WrongTypeException {
		Object object = _objects.get(id);
		if(object == null) {
			throw new NoSuchObjectException();
		}
		
		if(object.getType() != type) {
			throw new WrongTypeException();
		}
		
		List<Observation> lst = new ArrayList<>();

		int index = id.indexOf('*');
		if(index != -1) 
			id = id.substring(0, index) + "." + id.substring(index, id.length());
		
		for(Map.Entry<String,Object> entry : _objects.entrySet()) {
			if(Pattern.matches(id, entry.getKey()))
				lst.add(_objects.get(entry.getKey()).getLastObservation());
		}
		return lst;
	}
	
	//a ordenacao e so fazer print ao ultimo ao primeiro membro da lista
	public Iterable<?extends Observation> trace(String type, String id) throws NoSuchObjectException, WrongTypeException {
		Object object = _objects.get(id);
		if(object == null) {
			throw new NoSuchObjectException();
		}
		if(object.getType() != type) {
			throw new WrongTypeException();
		}
		return object.getObservationList();
		
	}
	

	public void report (String name, Iterable<?extends Observation>observation) throws NoSuchCameraException, InvalidCameraNameException{
		
			
		   	if(name.length()<3 || name.length()>15) { //FIXME alphanumeric
		   		throw new InvalidCameraNameException();
		   	}
		   	Camera cam = _cameras.get(name);
			if(cam != null) {
				Instant time = Instant.now();
				Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
				for(Observation element : observation ) {
					updateLogObs.add(element);   //adiciona uma obs a lista de updates
					if (_objects.get(element.getId()) == null) {
					//System.out.println("Received :" + element.getCam() + element.getId()+ cam.getLatitude()+ element.getType());
					Object object2 = new Object(element.getId(), element.getType());
					element.setCam(name);
					element.setLat(cam.getLatitude());
					element.setLon(cam.getLongitude());
					element.setTime(timestamp);
					object2.addObservation(element);
					_objects.put(element.getId(), object2);
					}
					else {
						element.setCam(name);
						element.setLat(cam.getLatitude());
						element.setLon(cam.getLongitude());
						element.setTime(timestamp);
						_objects.get(element.getId()).addObservation(element);
					}
				}
			}
			else {
				throw new NoSuchCameraException();
			}
			
	}

	public synchronized void cam_join(String name, double latitude, double longitude) throws InvalidCameraNameException, DuplicateCameraException{
		   	if(name.length()<3 || name.length()>15) { //FIXME alphanumeric
		   		throw new InvalidCameraNameException();
		   		
		   	}
		   	if(_cameras.containsKey(name)) {
		   		throw new DuplicateCameraException();
		   		
		   	}
		   	Camera eye = new Camera(name, latitude, longitude);
		   	_cameras.put(name, eye);
		   	updateLogCam.add(eye);
		}

		public synchronized Coordinates cam_info(String name) throws NoSuchCameraException{
			if(_cameras.get(name).equals(null)) {
				throw new NoSuchCameraException();
			}
			Camera eye = _cameras.get(name);
			Coordinates coordinates = new Coordinates(eye.getLatitude(), eye.getLongitude());
			return coordinates;
		}
		/*
		public void removeCamera(String name) {
			_cameras.remove(name);
		} 
		*/
		
		public List<Observation> getUpdateLogObs() {
			return updateLogObs;
		}
		public List<Camera> getUpdateLogCam() {
			return updateLogCam;
		}
		public List<Integer> getReplicaTimestamp() {
			return replicaTimestamp;
		}
		public void clear() {
			_cameras.clear();
			_objects.clear();
		}
		
	

}

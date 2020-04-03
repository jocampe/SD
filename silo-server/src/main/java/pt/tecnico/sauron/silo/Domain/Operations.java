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
	
	public void report (String name, Iterable<?extends Observation>observation, List<String>id, List<String>type){
		//do verification with cam_info
			Instant time = Instant.now();
			Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
			int count = 0;
			for(Observation element : observation ) {
				Object object2 = new Object(id.get(count), type.get(count));
				element.setTime(timestamp);
				object2.addObservation(element);
				object.put(id.get(count), object2);
				count++;
			}
			
			
	}
}

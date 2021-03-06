package pt.tecnico.sauron.silo.Domain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import pt.tecnico.sauron.silo.Domain.*;

public class Object {
	
	private String id;
	private String type;
	private List<Observation> obsList = new ArrayList<>();
	
	public Object (String _id, String _type) {
		this.id = _id;
		this.type = _type;
	}
	
	public String getId() {
		return this.id;
	}
	public String getType() {
		return this.type;
	}
	//novas obs sao added no fim da lista, por isso basta inverter a ordem
	public List<Observation> getObsList(){
		return new ArrayList<>(Lists.reverse(this.obsList));
	}
	
	public void addObservation(Observation observation){
		obsList.add(observation);
	}
	
	//assumindo que a lista nunca e ordenada, basta ir buscar a ultima obs a ser inserida
	public Observation getLastObservation() {
		return obsList.get(obsList.size() - 1);
	}
	
	public List<Observation> getObservationList() {
		return obsList;
	}
	
}

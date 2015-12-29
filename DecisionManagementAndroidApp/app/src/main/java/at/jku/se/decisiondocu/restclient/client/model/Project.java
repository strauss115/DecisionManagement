package at.jku.se.decisiondocu.restclient.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Project extends Node {

	@JsonIgnore
	public int getNrOfDecisions() {
		return nrOfDecisions;
	}
	@JsonIgnore
	public void setNrOfDecisions(int nrOfDecisions) {
		this.nrOfDecisions = nrOfDecisions;
	}

	@JsonIgnore
	private int nrOfDecisions = -1;

	public Project(String name, User admin, String password) {
		super(name);
		this.addRelation(RelationString.PROJECTADMIN, admin, true);
		this.addDirectProperty(PropertyString.PASSWORD, password);
	}

	public Project() {
		super();
	}

	@JsonIgnore
	public String getPassword() {
		try{
		return super.getDirectProperties().get(PropertyString.PASSWORD);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
	}

	@Override
	public Map<String, String> getDirectProperties() {
		if (super.getDirectProperties() == null)
			return null;
		HashMap<String, String> result = new HashMap<String, String>(super.getDirectProperties());
		result.remove(PropertyString.PASSWORD);
		return result;
	}

}

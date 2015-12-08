package at.jku.se.decisiondocu.restclient.client.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Project extends Node {

	public int getNrOfDecisions() {
		return nrOfDecisions;
	}

	public void setNrOfDecisions(int nrOfDecisions) {
		this.nrOfDecisions = nrOfDecisions;
	}

	private int nrOfDecisions = -1;



	public Project(String name, User admin, String password) {
		super(name);
		this.addRelation(RelationString.PROJECTAMIN, admin, true);
		this.addDirectProperty(PropertyString.PASSWORD, password);
	}

	public Project() {
		super();
		// TODO Auto-generated constructor stub
	}

	@JsonIgnore
	public String getPassword() {
		return this.getDirectProperties().get(PropertyString.PASSWORD);
	}

	@JsonIgnore
	public void setPassword(String password) {
		this.addDirectProperty(PropertyString.PASSWORD, password);
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

package at.jku.se.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.strings.PropertyString;
import at.jku.se.database.strings.RelationString;

public class Project extends Node {
	
	// ------------------------------------------------------------------------

	public Project(String name, User admin, String password) {
		super(name);
		this.addRelation(RelationString.HAS_PROJECTADMIN, admin, true);
		this.addDirectProperty(PropertyString.PASSWORD, password);
	}

	public Project() {
		super();
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getPassword() {
		return getDirectProperty(PropertyString.PASSWORD);
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
	}
	
	// ------------------------------------------------------------------------
	
	
	
	// ------------------------------------------------------------------------

	@Override
	public Map<String, String> getDirectProperties() {
		if (super.getDirectProperties() == null)
			return null;
		HashMap<String, String> result = new HashMap<String, String>(super.getDirectProperties());
		result.remove(PropertyString.PASSWORD);
		return result;
	}

}

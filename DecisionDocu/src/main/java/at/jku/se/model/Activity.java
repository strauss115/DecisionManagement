package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;
import at.jku.se.dm.shared.NodeString;
import at.jku.se.dm.shared.PropertyString;

public class Activity extends Node {

	// ------------------------------------------------------------------------
	
	public Activity() {
		
	}
	
	public Activity(String name) {
		super (name);
	}
	
	public Activity(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}
	
	public Activity(String userEmail, String message) {
		super(message);
		this.setEMail(userEmail);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.ACTIVITY;
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public String getEmail() {
		return getDirectProperty(PropertyString.USER_EMAIL);
	}

	@JsonIgnore
	public void setEMail(String email) {
		super.addDirectProperty(PropertyString.USER_EMAIL, email);
		DBService.updateNode(this, 0);
	}
	
	
}

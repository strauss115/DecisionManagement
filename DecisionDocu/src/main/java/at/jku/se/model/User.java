package at.jku.se.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;
import at.jku.se.database.strings.NodeString;
import at.jku.se.database.strings.PropertyString;
import at.jku.se.database.strings.RelationString;

public class User extends Node {

	// private List<Team> teams; Teams are stored in Relationships
	// private String picture; Picture might be a Seperate Node

	public User(String email, String firstname, String lastname, boolean isAdmin) {
		super(firstname);
		if (email != null && firstname != null && lastname != null) {
			this.addDirectProperty("lastname", lastname);
			this.addDirectProperty("email", email);
			this.addDirectProperty("isAdmin", "" + isAdmin);
		}
	}

	public User(String email, String firstname, String lastname, String password, boolean isAdmin) {
		super(firstname);
		if (email != null && firstname != null && lastname != null) {
			this.addDirectProperty("firstname", firstname);
			this.addDirectProperty("lastname", lastname);
			this.addDirectProperty("email", email);
			this.addDirectProperty("password", password);
			this.addDirectProperty("isAdmin", "" + isAdmin);
		}
	}

	public User() {
		super();
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.USER;
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getLastname() {
		try {
			return super.getDirectProperties().get("lastname");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public void setLastname(String lastname) {
		super.addDirectProperty("lastname", lastname);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getEmail() {
		try {
			return super.getDirectProperties().get("email");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public void setEmail(String email) {
		super.addDirectProperty("email", email);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getPassword() {
		try {
			return super.getDirectProperties().get("password");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty("password", password);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public boolean isAdmin() {
		try {
			return Boolean.parseBoolean(super.getDirectProperties().get("isAdmin"));
		} catch (Exception e) {
			return false;
		}
	}

	@JsonIgnore
	public void setAdmin(boolean admin) {
		super.addDirectProperty("isAdmin", "" + admin);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public List<Project> getProjects() {
		return getNodesByRelationship(RelationString.HAS_PROJECT, Project.class);
	}
	
	@JsonIgnore
	public void addToProject(Project project) {
		this.addRelation(RelationString.HAS_PROJECT, project, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	@JsonIgnore
	public boolean deleteFromProject(Project project) {
		return deleteRelationByRelatedNode(RelationString.HAS_PROJECT, project);
	}
	
	// ------------------------------------------------------------------------

	@Override
	public Map<String, String> getDirectProperties() {
		if (super.getDirectProperties() == null)
			return null;
		HashMap<String, String> result = new HashMap<String, String>(super.getDirectProperties());
		result.remove(PropertyString.PASSWORD);
		return result;
	}

	public void addToTeam(Project team) {
		this.addRelation("hasTeam", team, true);
	}

}

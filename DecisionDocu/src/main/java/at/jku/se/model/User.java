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
			this.addDirectProperty(PropertyString.LASTNAME, lastname);
			this.addDirectProperty(PropertyString.EMAIL, email);
			this.addDirectProperty(PropertyString.IS_ADMIN, "" + isAdmin);
		}
	}

	public User(String email, String firstname, String lastname, String password, boolean isAdmin) {
		super(firstname);
		if (email != null && firstname != null && lastname != null) {
			this.addDirectProperty(PropertyString.FIRSTNAME, firstname);
			this.addDirectProperty(PropertyString.LASTNAME, lastname);
			this.addDirectProperty(PropertyString.EMAIL, email);
			this.addDirectProperty(PropertyString.PASSWORD, password);
			this.addDirectProperty(PropertyString.IS_ADMIN, "" + isAdmin);
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
		return super.getDirectProperty(PropertyString.LASTNAME);
	}

	@JsonIgnore
	public void setLastname(String lastName) {
		super.addDirectProperty(PropertyString.LASTNAME, lastName);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getFirstName() {
		return super.getDirectProperty(PropertyString.FIRSTNAME);
	}

	@JsonIgnore
	public void setFirstName(String firstName) {
		super.addDirectProperty(PropertyString.FIRSTNAME, firstName);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getEmail() {
		return super.getDirectProperty(PropertyString.EMAIL);
	}

	@JsonIgnore
	public void setEmail(String email) {
		super.addDirectProperty(PropertyString.EMAIL, email);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getPassword() {
		return super.getDirectProperty(PropertyString.PASSWORD);
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public boolean isAdmin() {
		try {
			return Boolean.parseBoolean(super.getDirectProperty(PropertyString.IS_ADMIN));
		} catch (Exception e) {
			return false;
		}
	}

	@JsonIgnore
	public void setAdmin(boolean admin) {
		super.addDirectProperty(PropertyString.IS_ADMIN, String.valueOf(admin));
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

}

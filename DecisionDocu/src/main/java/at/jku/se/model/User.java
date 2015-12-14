package at.jku.se.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.strings.PropertyString;

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

	@JsonIgnore
	public String getLastname() {
		try {
			return super.getDirectProperties().get("lastname");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public String getEmail() {
		try {
			return super.getDirectProperties().get("email");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public String getPassword() {
		try {
			return super.getDirectProperties().get("password");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public boolean isAdmin() {
		try {
			return Boolean.parseBoolean(super.getDirectProperties().get("isAdmin"));
		} catch (Exception e) {
			return false;
		}
	}

	@JsonIgnore
	public void setLastname(String lastname) {
		super.addDirectProperty("lastname", lastname);
	}

	@JsonIgnore
	public void setEmail(String email) {
		super.addDirectProperty("email", email);
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty("password", password);
	}

	@JsonIgnore
	public void setAdmin(boolean admin) {
		super.addDirectProperty("isAdmin", "" + admin);
	}

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

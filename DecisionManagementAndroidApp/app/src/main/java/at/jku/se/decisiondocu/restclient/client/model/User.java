package at.jku.se.decisiondocu.restclient.client.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
		// TODO Auto-generated constructor stub
	}

	@JsonIgnore
	public String getLastname() {
		try {
			return this.getDirectProperties().get("lastname");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public String getEmail() {
		try {
			return this.getDirectProperties().get("email");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public String getPassword() {
		try {
			return this.getDirectProperties().get("password");
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	public boolean isAdmin() {
		try {
			return Boolean.parseBoolean(this.getDirectProperties().get("isAdmin"));
		} catch (Exception e) {
			return false;
		}
	}

	@JsonIgnore
	public void setLastname(String lastname) {
		this.addDirectProperty("lastname", lastname);
	}

	@JsonIgnore
	public void setEmail(String email) {
		this.addDirectProperty("email", email);
	}

	@JsonIgnore
	public void setPassword(String password) {
		this.addDirectProperty("password", password);
	}

	@JsonIgnore
	public void setAdmin(boolean admin) {
		this.addDirectProperty("isAdmin", "" + admin);
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

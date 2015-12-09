package at.jku.se.dm.rest.pojos;

import java.util.LinkedList;

import at.jku.se.dm.rest.ResponseData;


/**
 * Data class for teams used by REST service
 */
public class Team extends ResponseData {
	
	public static final String ID_PREFIX = "T";

	// ------------------------------------------------------------------------
	
	private String name;
	private LinkedList<String> users;
	private User admin;
	
	// ------------------------------------------------------------------------
	
	public Team() {
		
	}
	
	public Team(String id, String name, User admin) {
		this.name = name;
		this.admin = admin;
		setId(ID_PREFIX + name);
		
		users = new LinkedList<String>();
	}
	
	public Team(String name, User admin) {
		this.name = name;
		this.admin = admin;
		setId(ID_PREFIX + name);
		
		users = new LinkedList<String>();
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public User getAdmin() {
		return admin;
	}
	
	public void setAdmin(User admin) {
		this.admin = admin;
	}
	
	public boolean addUser(User user) {
		if (!users.contains(user.getEMail())) {
			users.add((user.getEMail()));
			return true;
		}
		return false;
	}
	
	public boolean removeUser(User user) {
		if (users.contains((user.getEMail()))) {
			users.remove((user.getEMail()));
			return true;
		}
		return false;
	}
}

package at.jku.se.dm.rest.pojos;

import java.util.LinkedList;

import at.jku.se.dm.rest.ResponseData;


/**
 * Data class for teams used by REST service
 */
public class Team extends ResponseData {

	/**
	 * Unique team name 
	 */
	private String name;
	private LinkedList<String> users;
	
	// ------------------------------------------------------------------------
	
	public Team() {
		
	}
	
	public Team(String name) {
		this.name = name;
		
		users = new LinkedList<String>();
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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

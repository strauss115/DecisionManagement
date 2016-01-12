package at.jku.se.rest.web.pojos;

import java.util.LinkedList;
import java.util.List;

import at.jku.se.rest.response.ResponseData;


/**
 * Data class for teams used by REST service
 */
public class WebTeam extends ResponseData {
	
	// ------------------------------------------------------------------------
	
	private String name;
	private List<String> users;
	private String admin;
	
	// ------------------------------------------------------------------------
	
	/**
	 * Default constructor
	 */
	public WebTeam() {
		
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param admin
	 */
	public WebTeam(String id, String name, String admin) {
		this.name = name;
		this.admin = admin;
		
		users = new LinkedList<String>();
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param admin
	 */
	public WebTeam(String name, String admin) {
		this.name = name;
		this.admin = admin;
		
		users = new LinkedList<String>();
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAdmin() {
		return admin;
	}
	
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	public List<String> getUsers() {
		return users;
	}
	
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	public boolean addUser(WebUser user) {
		if (!users.contains(user.getEMail())) {
			users.add((user.getEMail()));
			return true;
		}
		return false;
	}
	
	public boolean removeUser(WebUser user) {
		if (users.contains((user.getEMail()))) {
			users.remove((user.getEMail()));
			return true;
		}
		return false;
	}
}

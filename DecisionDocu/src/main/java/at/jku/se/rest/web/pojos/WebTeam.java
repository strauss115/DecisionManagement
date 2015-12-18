package at.jku.se.rest.web.pojos;

import java.util.LinkedList;

import at.jku.se.rest.response.ResponseData;


/**
 * Data class for teams used by REST service
 */
public class WebTeam extends ResponseData {
	
	public static final String ID_PREFIX = "T";

	// ------------------------------------------------------------------------
	
	private String name;
	private LinkedList<String> users;
	private WebUser admin;
	
	// ------------------------------------------------------------------------
	
	public WebTeam() {
		
	}
	
	public WebTeam(String id, String name, WebUser admin) {
		this.name = name;
		this.admin = admin;
		setId(ID_PREFIX + name);
		
		users = new LinkedList<String>();
	}
	
	public WebTeam(String name, WebUser admin) {
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
	
	public WebUser getAdmin() {
		return admin;
	}
	
	public void setAdmin(WebUser admin) {
		this.admin = admin;
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

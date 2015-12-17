package at.jku.se.rest.web.pojos;

import java.util.LinkedList;

import at.jku.se.rest.response.ResponseData;

public class WebUser extends ResponseData {

	/**
	 * Unique e-mail address
	 */
	private String eMail;
	private String firstName, lastName;
	private String password; // TODO JsonIgnore is not working...
	private String urlProfilePicture;
	private boolean isAdmin;
	private LinkedList<String> teams;

	// ------------------------------------------------------------------------

	public WebUser() {

	}

	public WebUser(String eMail, String firstName, String lastName, String password) {
		this.eMail = eMail;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		
		this.teams = new LinkedList<String>();
	}

	// ------------------------------------------------------------------------

	public String getEMail() {
		return eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrlProfilePicture() {
		return urlProfilePicture;
	}

	public void setUrlProfilePicture(String urlProfilePicture) {
		this.urlProfilePicture = urlProfilePicture;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public LinkedList<String> getTeams() {
		return teams;
	}

	public boolean addToTeam(WebTeam team) {
		if (!teams.contains((team.getName()))) {
			teams.add((team.getName()));
			return true;
		}
		return false;
	}
	
	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return eMail;
	}
	

}

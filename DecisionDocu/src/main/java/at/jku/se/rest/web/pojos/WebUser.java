package at.jku.se.rest.web.pojos;

import java.util.LinkedList;
import java.util.List;

import at.jku.se.rest.response.ResponseData;

/**
 * Data container class to represent a user for web application
 */
public class WebUser extends ResponseData {

	/**
	 * Unique e-mail address
	 */
	private String eMail;
	private String firstName, lastName;
	private String profilePicture;
	private boolean isAdmin;
	private List<String> teams;

	// ------------------------------------------------------------------------

	/**
	 * Default constructor
	 */
	public WebUser() {

	}

	/**
	 * Constructor
	 * @param eMail
	 * @param firstName
	 * @param lastName
	 */
	public WebUser(String eMail, String firstName, String lastName) {
		this.eMail = eMail;
		this.firstName = firstName;
		this.lastName = lastName;
		
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

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<String> getTeams() {
		return teams;
	}
	
	public void setTeams(List<String> teams) {
		this.teams = teams;
	}

	public boolean addToTeam(WebTeam team) {
		if (!teams.contains((team.getName()))) {
			teams.add((team.getName()));
			return true;
		}
		return false;
	}
	
	// ------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return eMail;
	}
	

}

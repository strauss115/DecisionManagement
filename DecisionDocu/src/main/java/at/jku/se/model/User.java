package at.jku.se.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.dm.shared.NodeString;
import at.jku.se.dm.shared.PropertyString;

/**
 * Class for User Nodes
 * It extends Nodes
 * @author August
 *
 */
public class User extends Node {

	// private List<Team> teams; Teams are stored in Relationships
	// private String picture; Picture might be a Seperate Node
	
	private static final Logger log = LogManager.getLogger(User.class);
	
	// ------------------------------------------------------------------------

	/**
	 * Constructor
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @param isAdmin
	 */
	public User(String email, String firstname, String lastname, boolean isAdmin) {
		super(firstname);
		if (email != null && firstname != null && lastname != null) {
			this.addDirectProperty(PropertyString.LASTNAME, lastname);
			this.addDirectProperty(PropertyString.EMAIL, email);
			this.addDirectProperty(PropertyString.IS_ADMIN, "" + isAdmin);
		}
	}

	/**
	 * Constructor
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @param password
	 * @param isAdmin
	 */
	public User(String email, String firstname, String lastname, String password, boolean isAdmin) {
		super(firstname);
		if (email != null && firstname != null && lastname != null) {
			this.setName(firstname);
			// this.addDirectProperty(PropertyString.FIRSTNAME, firstname);
			this.addDirectProperty(PropertyString.LASTNAME, lastname);
			this.addDirectProperty(PropertyString.EMAIL, email);
			this.addDirectProperty(PropertyString.PASSWORD, password);
			this.addDirectProperty(PropertyString.IS_ADMIN, "" + isAdmin);
		}
	}

	/**
	 * Default constructor
	 */
	public User() {
		super();
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the type of the node
	 * @return The type of the node as string
	 */
	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.USER;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns last name of the user
	 * @return The last name of the user as String
	 */
	@JsonIgnore
	public String getLastname() {
		return super.getAllDirectProperties().get(PropertyString.LASTNAME);
	}

	/**
	 * Sets the last name of the user
	 * @param lastName
	 */
	@JsonIgnore
	public void setLastname(String lastName) {
		super.addDirectProperty(PropertyString.LASTNAME, lastName);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the first name of the user
	 * @return The first name of the user as String
	 */
	@JsonIgnore
	public String getFirstName() {
		return super.getName();
	}

	/**
	 * Sets the first name of the user
	 * @param firstName
	 */
	@JsonIgnore
	public void setFirstName(String firstName) {
		super.setName(firstName);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the email of the user
	 * @return The email of the user as String
	 */
	@JsonIgnore
	public String getEmail() {
		return super.getAllDirectProperties().get(PropertyString.EMAIL);
	}

	/**
	 * Sets the email of the user
	 * @param email
	 */
	@JsonIgnore
	public void setEmail(String email) {
		super.addDirectProperty(PropertyString.EMAIL, email);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the password of the user
	 * @return The password of the user as String
	 */
	@JsonIgnore
	public String getPassword() {
		// need to get password by direct access as getDirectProperties is overwritten
		try {
			return super.getDirectProperties().get("password");
		} catch (Exception e) {
			log.error("Unable to get password", e);
			return "";
		}
	}

	/**
	 * Sets the password of the user
	 * @param password
	 */
	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
		DBService.updateNode(this, 0);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public Document getProfilePicture() {
		List<Document> document = getNodesByRelationship(RelationString.HAS_PICTURE, Document.class);
		if (!document.isEmpty()) {
			return document.get(0);
		}
		return null;
	}
	
	@JsonIgnore
	public long getProfilePictureId() {
		Document d = getProfilePicture();
		if (d != null) {
			return d.getId();
		}
		return 0;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns true if user is admin, false if not admin
	 * @return true if user is admin, false if user is not admin
	 */
	@JsonIgnore
	public boolean isAdmin() {
		try {
			return Boolean.parseBoolean(super.getAllDirectProperties().get(PropertyString.IS_ADMIN));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Makes the user an admin
	 * @param admin
	 */
	@JsonIgnore
	public void setAdmin(boolean admin) {
		super.addDirectProperty(PropertyString.IS_ADMIN, String.valueOf(admin));
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns all projects of the user
	 * @return All projects of the user
	 */
	@JsonIgnore
	public List<Project> getProjects() {
		return getNodesByRelationship(RelationString.HAS_PROJECT, Project.class);
	}

	/**
	 * Adds a user to a project
	 * @param project
	 */
	@JsonIgnore
	public void addToProject(Project project) {
		this.addRelation(RelationString.HAS_PROJECT, project, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}

	/**
	 * Deletes a user from project
	 * @param project
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteFromProject(Project project) {
		return deleteRelationByRelatedNode(RelationString.HAS_PROJECT, project);
	}

	// ------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getDirectProperties() {
		if (super.getDirectProperties() == null)
			return null;
		HashMap<String, String> result = new HashMap<String, String>(super.getDirectProperties());
		result.remove(PropertyString.PASSWORD);
		return result;
	}

}

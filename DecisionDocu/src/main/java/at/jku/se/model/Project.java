package at.jku.se.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;
import at.jku.se.dm.shared.NodeString;
import at.jku.se.dm.shared.PropertyString;
import at.jku.se.dm.shared.RelationString;

/**
 * Class for Project Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Project extends Node {

	private static final Logger log = LogManager.getLogger(Project.class);
	
	// ------------------------------------------------------------------------

	/**
	 * Constructor
	 * @param name
	 * @param admin
	 * @param password
	 */
	public Project(String name, User admin, String password) {
		super(name);
		this.addRelation(RelationString.HAS_PROJECTADMIN, admin, true);
		this.addDirectProperty(PropertyString.PASSWORD, password);
	}

	/**
	 * Default constructor
	 */
	public Project() {
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
		return NodeString.PROJECT;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the password of the project
	 * @return The password of the project as String
	 */
	@JsonIgnore
	public String getPassword() {
		try {
			return super.getDirectProperties().get(PropertyString.PASSWORD);
		} catch (Exception e) {
			log.error("Unable to get password", e);
			return "";
		}
	}

	/**
	 * Sets the password of the project
	 * @param password
	 */
	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
		DBService.updateNode(this, 0);
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns the admin of the project
	 * @return The admin user node object of the project
	 */
	@JsonIgnore
	public User getAdmin() {
		return getSingleNodeByRelationship(RelationString.HAS_PROJECTADMIN, User.class);
	}
	
	/**
	 * Sets the admin user
	 * @param user The user node object of the admin
	 */
	@JsonIgnore
	public void setAdmin(User user) {
		setSingleNodeRelationship(RelationString.HAS_PROJECTADMIN, user);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns all decisions of the project
	 * @return A List of Decision node objects
	 */
	@JsonIgnore
	public List<Decision> getDecisions() {
		return getNodesByRelationship(RelationString.HAS_DECISION, Decision.class);
	}
	
	/**
	 * Adds a decision to the project
	 * @param decision
	 */
	@JsonIgnore
	public void addDecision(Decision decision) {
		this.addRelation(RelationString.HAS_DECISION, decision, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a decision of the project
	 * @param decision
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteDecisionRelation(Decision decision) {
		return deleteRelationByRelatedNode(RelationString.HAS_DECISION, decision);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Activity> getActivites() {
		return getNodesByRelationship(RelationString.HAS_ACTIVITY, Activity.class);
	}
	
	@JsonIgnore
	public void addActivity(Activity activity) {
		this.addRelation(RelationString.HAS_ACTIVITY, activity, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	// ------------------------------------------------------------------------

	/**
	 * Returns all direct properties of the project
	 * @return A Map<String, String> object of direct properties
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

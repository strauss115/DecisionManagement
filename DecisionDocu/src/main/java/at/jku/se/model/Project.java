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

public class Project extends Node {

	private static final Logger log = LogManager.getLogger(Project.class);
	
	// ------------------------------------------------------------------------

	public Project(String name, User admin, String password) {
		super(name);
		this.addRelation(RelationString.HAS_PROJECTADMIN, admin, true);
		this.addDirectProperty(PropertyString.PASSWORD, password);
	}

	public Project() {
		super();
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.PROJECT;
	}

	// ------------------------------------------------------------------------

	
	@JsonIgnore
	public String getPassword() {
		try {
			return super.getDirectProperties().get(PropertyString.PASSWORD);
		} catch (Exception e) {
			log.error("Unable to get password", e);
			return "";
		}
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
		DBService.updateNode(this, 0);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public User getAdmin() {
		return getSingleNodeByRelationship(RelationString.HAS_PROJECTADMIN, User.class);
	}
	
	@JsonIgnore
	public void setAdmin(User user) {
		setSingleNodeRelationship(RelationString.HAS_PROJECTADMIN, user);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Decision> getDecisions() {
		return getNodesByRelationship(RelationString.HAS_DECISION, Decision.class);
	}
	
	@JsonIgnore
	public void addDecision(Decision decision) {
		this.addRelation(RelationString.HAS_DECISION, decision, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	@JsonIgnore
	public boolean deleteDecisionRelation(Decision decision) {
		return deleteRelationByRelatedNode(RelationString.HAS_DECISION, decision);
	}
	
	// ------------------------------------------------------------------------

	@Override
	public Map<String, String> getDirectProperties() {
		if (super.getDirectProperties() == null)
			return null;
		HashMap<String, String> result = new HashMap<String, String>(super.getDirectProperties());
		result.remove(PropertyString.PASSWORD);
		return result;
	}

}

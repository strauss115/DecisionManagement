package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;
import at.jku.se.dm.shared.NodeString;
import at.jku.se.dm.shared.PropertyString;
import at.jku.se.dm.shared.RelationString;

/**
 * Class for Decision Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Decision extends Node {

	// private static final Logger log = LogManager.getLogger(Decision.class);

	// ------------------------------------------------------------------------

	/**
	 * Constructor
	 * @param name
	 */
	public Decision(String name) {
		super(name);
	}

	/** 
	 * Constructor
	 * @param name
	 * @param relations
	 */
	public Decision(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	/**
	 * Default constructor
	 */
	public Decision() {
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
		return NodeString.DECISION;
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns the description
	 * @return The description
	 */
	@JsonIgnore
	public String getDescription() {
		return getDirectProperty(PropertyString.DESCRIPTION);
	}

	/**
	 * Sets the description
	 * @param description
	 */
	@JsonIgnore
	public void setDescription(String description) {
		super.addDirectProperty(PropertyString.DESCRIPTION, description);
		DBService.updateNode(this, 0);
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns the last activity
	 * @return The last activity as String
	 */
	@JsonIgnore
	public String getLastActivity() {
		String value = getDirectProperty(PropertyString.LAST_ACTIVITY);
		if (value.equals(""))
			return "No last activity";
		return value;
	}
	
	/**
	 * Sets the last activity
	 * @param lastActivity
	 */
	@JsonIgnore
	public void setLastActivity(String lastActivity) {
		super.addDirectProperty(PropertyString.LAST_ACTIVITY, lastActivity);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the Id of the author user node
	 * @return The Id of the author user node
	 */
	@JsonIgnore
	public String getAuthorId() {
		Node author = getSingleNodeByRelationship(RelationString.HAS_CREATOR, User.class);
		if (author != null)
			return String.valueOf(author.getId());
		return "";
	}

	/**
	 * Sets the user for the decision
	 * @param user
	 */
	@JsonIgnore
	public String getAuthorEmail() {
		User author = getSingleNodeByRelationship(RelationString.HAS_CREATOR, User.class);
		if (author != null)
			return author.getEmail();
		return "";
	}
	
	@JsonIgnore
	public void setAuthor(User user) {
		setSingleNodeRelationship(RelationString.HAS_CREATOR, user);
		DBService.updateNode(this, 0);
	}

	// ------------------------------------------------------------------------
	
	/**
	 * Returns the influence factors of the decision
	 * @return A List of InfluenceFactor of the decision
	 */
	@JsonIgnore
	public List<InfluenceFactor> getInfluenceFactors() {
		return getNodesByRelationship(RelationString.HAS_INFLUENCE_FACTOR, InfluenceFactor.class);
	}
	
	/**
	 * Adds a influence factor
	 * @param influenceFactor
	 */
	@JsonIgnore
	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		this.addRelation(RelationString.HAS_INFLUENCE_FACTOR, influenceFactor, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a influence factor
	 * @param influenceFactor
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteInfluenceFactor(InfluenceFactor influenceFactor) {
		return DBService.deleteNode(influenceFactor.getId());
	}

	// ------------------------------------------------------------------------
	
	/**
	 * Returns the rationals of the decision
	 * @return A list of Rational of the decision
	 */
	@JsonIgnore
	public List<Rationale> getRationales() {
		return getNodesByRelationship(RelationString.HAS_RATIONALE, Rationale.class);
	}
	
	/**
	 * Adds rationale to decision
	 * @param rationale
	 */
	@JsonIgnore
	public void addRationale(Rationale rationale) {
		this.addRelation(RelationString.HAS_RATIONALE, rationale, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a relation of the decision
	 * @param rationale
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteRationale(Rationale rationale) {
		return DBService.deleteNode(rationale.getId());
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns alternatives of the decision
	 * @return A List of Alternatives of the decision
	 */
	@JsonIgnore
	public List<Alternative> getAlternatives() {
		return getNodesByRelationship(RelationString.HAS_ALTERNATIVE, Alternative.class);
	}
	
	/**
	 * Adds alternative to the decision
	 * @param alternative
	 */
	@JsonIgnore
	public void addAlterantive(Alternative alternative) {
		this.addRelation(RelationString.HAS_ALTERNATIVE, alternative, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a alternative of the decision
	 * @param alternative
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteAlternative(Alternative alternative) {
		return DBService.deleteNode(alternative.getId());
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns the decisions consequences
	 * @return A List of Consequence Objects of the decision
	 */
	@JsonIgnore
	public List<Consequence> getConsquences() {
		return getNodesByRelationship(RelationString.HAS_CONSEQUENCE, Consequence.class);
	}
	
	/**
	 * Adds a consequence to the decision
	 * @param consequence
	 */
	@JsonIgnore
	public void addConsequence(Consequence consequence) {
		this.addRelation(RelationString.HAS_CONSEQUENCE, consequence, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes the consequence of the decision
	 * @param consequence
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteConsequence(Consequence consequence) {
		return DBService.deleteNode(consequence.getId());
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns quality attributes of the decision
	 * @return A List of QualityAttribute objects
	 */
	@JsonIgnore
	public List<QualityAttribute> getQualityAttributes() {
		return getNodesByRelationship(RelationString.HAS_QUALITY_ATTRIBUTES, QualityAttribute.class);
	}
	
	/**
	 * Adds a quality attribute to the decision
	 * @param qualityAttribute
	 */
	@JsonIgnore
	public void addQualityAttribute(QualityAttribute qualityAttribute) {
		this.addRelation(RelationString.HAS_QUALITY_ATTRIBUTES, qualityAttribute, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a quality attribute of the decision
	 * @param qualityAttribute
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteQualityAttribute(QualityAttribute qualityAttribute) {
		return DBService.deleteNode(qualityAttribute.getId());
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns the related decisions of the decision
	 * @return A List of Decision objects
	 */
	@JsonIgnore
	public List<Decision> getRelatedDecisions() {
		return getNodesByRelationship(RelationString.HAS_RELATED_DECISION, Decision.class);
	}
	
	/**
	 * Adds a decision to the decision
	 * @param decision
	 */
	@JsonIgnore
	public void addRelatedDecision(Decision decision) {
		this.addRelation(RelationString.HAS_RELATED_DECISION, decision, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a decision from the decision
	 * @param decision
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteRelatedDecision(Decision decision) {
		return deleteRelationByRelatedNode(RelationString.HAS_RELATED_DECISION, decision);
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns responsibles of the decision
	 * @return A List of User object which are responsible for the decision
	 */
	@JsonIgnore
	public List<User> getResponsibles() {
		return getNodesByRelationship(RelationString.HAS_RESPONSIBLE, User.class);
	}
	
	/**
	 * Adds a responsible user
	 * @param user
	 */
	@JsonIgnore
	public void addResponsible(User user) {
		this.addRelation(RelationString.HAS_RESPONSIBLE, user, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}

	/**
	 * Deletes a responsible user
	 * @param user
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteResponsible(User user) {
		return deleteRelationByRelatedNode(RelationString.HAS_RESPONSIBLE, user);
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns all documents of the decision
	 * @return A List of Document objects
	 */
	@JsonIgnore
	public List<Document> getDocuments() {
		return getNodesByRelationship(RelationString.HAS_DOCUMENT, Document.class);
	}
	
	/**
	 * Adds a document to the decision
	 * @param document
	 */
	@JsonIgnore
	public void addDocument(Document document) {
		this.addRelation(RelationString.HAS_DOCUMENT, document, true);
		DBService.updateNodeWihtRelationships(this, 0);
	}
	
	/**
	 * Deletes a document
	 * @param document
	 * @return true if deleted, false if not deleted
	 */
	@JsonIgnore
	public boolean deleteDocument(Document document) {
		return deleteRelationByRelatedNode(RelationString.HAS_DOCUMENT, document);
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns the messages of the decision
	 * @return A List of Message objects
	 */
	@JsonIgnore
	public List<Message> getMessages() {
		return getNodesByRelationship(RelationString.HAS_MESSAGE, Message.class);
	}

}

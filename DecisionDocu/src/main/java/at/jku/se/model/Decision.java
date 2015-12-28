package at.jku.se.model;

import java.util.List;
import java.util.Map;

import javax.management.relation.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;
import at.jku.se.database.strings.RelationString;

public class Decision extends Node {

	// private static final Logger log = LogManager.getLogger(Decision.class);

	// ------------------------------------------------------------------------

	public Decision(String name) {
		super(name);
	}

	public Decision(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public Decision() {
		super();
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getDescription() {
		return getDirectProperty(RelationString.DESCRIPTION);
	}

	@JsonIgnore
	public void setDescription(String description) {
		super.addDirectProperty(RelationString.DESCRIPTION, description);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getAuthorId() {
		Node author = getSingleNodeByRelationship(RelationString.CREATOR, User.class);
		if (author != null)
			return String.valueOf(author.getId());
		return "";
	}

	@JsonIgnore
	public void setAuthor(User user) {
		setSingleNodeRelationship(RelationString.CREATOR, user);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public List<InfluenceFactor> getInfluenceFactors() {
		return getNodesByRelationship(RelationString.HAS_INFLUENCE_FACTOR, InfluenceFactor.class);
	}
	
	@JsonIgnore
	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		this.addRelation(RelationString.HAS_INFLUENCE_FACTOR, influenceFactor, true);
	}
	
	@JsonIgnore
	public boolean deleteInfluenceFactor(InfluenceFactor influenceFactor) {
		return DBService.deleteNode(influenceFactor.getId());
	}

	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Rationale> getRationales() {
		return getNodesByRelationship(RelationString.HAS_RATIONALE, Rationale.class);
	}
	
	@JsonIgnore
	public void addRationale(Rationale rationale) {
		this.addRelation(RelationString.HAS_RATIONALE, rationale, true);
	}
	
	@JsonIgnore
	public boolean deleteRationale(Rationale rationale) {
		return DBService.deleteNode(rationale.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Alternative> getAlternatives() {
		return getNodesByRelationship(RelationString.HAS_ALTERNATIVE, Alternative.class);
	}
	
	@JsonIgnore
	public void addAlterantive(Alternative alternative) {
		this.addRelation(RelationString.HAS_ALTERNATIVE, alternative, true);
	}
	
	@JsonIgnore
	public boolean deleteAlternative(Alternative alternative) {
		return DBService.deleteNode(alternative.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Consequence> getConsquences() {
		return getNodesByRelationship(RelationString.HAS_CONSEQUENCE, Consequence.class);
	}
	
	@JsonIgnore
	public void addConsequence(Consequence consequence) {
		this.addRelation(RelationString.HAS_CONSEQUENCE, consequence, true);
	}
	
	@JsonIgnore
	public boolean deleteConsequence(Consequence consequence) {
		return DBService.deleteNode(consequence.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<QualityAttribute> getQualityAttributes() {
		return getNodesByRelationship(RelationString.HAS_QUALITY_ATTRIBUTES, QualityAttribute.class);
	}
	
	@JsonIgnore
	public void addQualityAttribute(QualityAttribute qualityAttribute) {
		this.addRelation(RelationString.HAS_QUALITY_ATTRIBUTES, qualityAttribute, true);
	}
	
	@JsonIgnore
	public boolean deleteQualityAttribute(QualityAttribute qualityAttribute) {
		return DBService.deleteNode(qualityAttribute.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Decision> getRelatedDecisions() {
		return getNodesByRelationship(RelationString.HAS_RELATED_DECISION, Decision.class);
	}
	
	@JsonIgnore
	public void addRelatedDecision(Decision decision) {
		this.addRelation(RelationString.HAS_RELATED_DECISION, decision, true);
	}
	
	@JsonIgnore
	public boolean deleteRelatedDecision(Decision decision) {
		return deleteRelationByRelatedNode(RelationString.HAS_RELATED_DECISION, decision);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<User> getResponsibles() {
		return getNodesByRelationship(RelationString.HAS_RESPONSIBLE, User.class);
	}
	
	@JsonIgnore
	public void addResponsible(User user) {
		this.addRelation(RelationString.HAS_RESPONSIBLE, user, true);
	}

	@JsonIgnore
	public boolean deleteResponsible(User user) {
		return deleteRelationByRelatedNode(RelationString.HAS_RESPONSIBLE, user);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Document> getDocuments() {
		return getNodesByRelationship(RelationString.HAS_DOCUMENT, Document.class);
	}
	
	@JsonIgnore
	public void addDocument(Document document) {
		this.addRelation(RelationString.HAS_DOCUMENT, document, true);
	}
	
	@JsonIgnore
	public boolean deleteDocument(Document document) {
		return deleteRelationByRelatedNode(RelationString.HAS_DOCUMENT, document);
	}

}

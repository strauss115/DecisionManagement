package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;

public class Decision extends Node {

	// private static final Logger log = LogManager.getLogger(Decision.class);

	private static final String DESCRIPTION = "description";
	private static final String HAS_AUTHOR = "hasAuthor", HAS_INFLUENCE_FACTOR = "hasInfluenceFactor",
			HAS_RATIONALE = "hasRationale", HAS_ALTERNATIVE = "hasAlternative", HAS_CONSEQUENCE = "hasConsequence",
			HAS_QUALITY_ATTRIBUTES = "hasQualityAttribute", HAS_RELATED_DECISION = "hasRelatedDecisions",
			HAS_RESPONSIBLE = "hasResponsibles", HAS_DOCUMENT = "hasDocument";

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
		return getDirectProperty(DESCRIPTION);
	}

	@JsonIgnore
	public void setDescription(String description) {
		super.addDirectProperty(DESCRIPTION, description);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getAuthorId() {
		Node author = getSingleNodeByRelationship(HAS_AUTHOR, User.class);
		if (author != null)
			return String.valueOf(author.getId());
		return "";
	}

	@JsonIgnore
	public void setAuthor(User user) {
		setSingleNodeRelationship(HAS_AUTHOR, user);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public List<InfluenceFactor> getInfluenceFactors() {
		return getNodesByRelationship(HAS_INFLUENCE_FACTOR, InfluenceFactor.class);
	}
	
	@JsonIgnore
	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		this.addRelation(HAS_INFLUENCE_FACTOR, influenceFactor, true);
	}
	
	@JsonIgnore
	public boolean deleteInfluenceFactor(InfluenceFactor influenceFactor) {
		return DBService.deleteNode(influenceFactor.getId());
	}

	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Rationale> getRationales() {
		return getNodesByRelationship(HAS_RATIONALE, Rationale.class);
	}
	
	@JsonIgnore
	public void addRationale(Rationale rationale) {
		this.addRelation(HAS_RATIONALE, rationale, true);
	}
	
	@JsonIgnore
	public boolean deleteRationale(Rationale rationale) {
		return DBService.deleteNode(rationale.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Alternative> getAlternatives() {
		return getNodesByRelationship(HAS_ALTERNATIVE, Alternative.class);
	}
	
	@JsonIgnore
	public void addAlterantive(Alternative alternative) {
		this.addRelation(HAS_ALTERNATIVE, alternative, true);
	}
	
	@JsonIgnore
	public boolean deleteAlternative(Alternative alternative) {
		return DBService.deleteNode(alternative.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Consequence> getConsquences() {
		return getNodesByRelationship(HAS_CONSEQUENCE, Consequence.class);
	}
	
	@JsonIgnore
	public void addConsequence(Consequence consequence) {
		this.addRelation(HAS_CONSEQUENCE, consequence, true);
	}
	
	@JsonIgnore
	public boolean deleteConsequence(Consequence consequence) {
		return DBService.deleteNode(consequence.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<QualityAttribute> getQualityAttributes() {
		return getNodesByRelationship(HAS_QUALITY_ATTRIBUTES, QualityAttribute.class);
	}
	
	@JsonIgnore
	public void addQualityAttribute(QualityAttribute qualityAttribute) {
		this.addRelation(HAS_QUALITY_ATTRIBUTES, qualityAttribute, true);
	}
	
	@JsonIgnore
	public boolean deleteQualityAttribute(QualityAttribute qualityAttribute) {
		return DBService.deleteNode(qualityAttribute.getId());
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Decision> getRelatedDecisions() {
		return getNodesByRelationship(HAS_RELATED_DECISION, Decision.class);
	}
	
	@JsonIgnore
	public void addRelatedDecision(Decision decision) {
		this.addRelation(HAS_RELATED_DECISION, decision, true);
	}
	
	@JsonIgnore
	public boolean deleteRelatedDecision(Decision decision) {
		return deleteRelationByRelatedNode(HAS_RELATED_DECISION, decision);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<User> getResponsibles() {
		return getNodesByRelationship(HAS_RESPONSIBLE, User.class);
	}
	
	@JsonIgnore
	public void addResponsible(User user) {
		this.addRelation(HAS_RESPONSIBLE, user, true);
	}

	@JsonIgnore
	public boolean deleteResponsible(User user) {
		return deleteRelationByRelatedNode(HAS_RESPONSIBLE, user);
	}
	
	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public List<Document> getDocuments() {
		return getNodesByRelationship(HAS_DOCUMENT, Document.class);
	}
	
	@JsonIgnore
	public void addDocument(Document document) {
		this.addRelation(HAS_DOCUMENT, document, true);
	}
	
	@JsonIgnore
	public boolean deleteDocument(Document document) {
		return deleteRelationByRelatedNode(HAS_DOCUMENT, document);
	}

}

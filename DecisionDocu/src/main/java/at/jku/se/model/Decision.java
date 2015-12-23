package at.jku.se.model;

import java.util.List;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.DBService;

public class Decision extends Node {

	private static final Logger log = LogManager.getLogger(Decision.class);

	private static final String DESCRIPTION = "description";
	private static final String HAS_AUTHOR = "hasAuthor", HAS_INFLUENCE_FACTOR = "hasInfluenceFactor",
			HAS_RATIONALE = "hasRationale", HAS_ALTERNATIVE = "hasAlternative", HAS_CONSEQUENCE = "hasConsequence",
			HAS_QUALITY_ATTRIBUTES = "hasQualityAttribute", HAS_RELATED_DECISIONS = "hasRelatedDecisions",
			HAS_RESPONSIBLES = "hasResponsibles", HAS_DOCUMENT = "hasDocument";

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
		try {
			return super.getDirectProperties().get(DESCRIPTION);
		} catch (Exception e) {
			log.error("Unable to get description");
			return "";
		}
	}

	@JsonIgnore
	public void setDescription(String description) {
		super.addDirectProperty(DESCRIPTION, description);
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getAuthorId() {
		List<RelationshipInterface> relations = this.getRelationships().get(HAS_AUTHOR);
		if (relations.size() == 0) {
			log.warn("Decision '" + this.getId() + "' has no author");
			return "-1";
		}
		if (relations.size() > 1) {
			log.warn("Decision '" + this.getId() + "' has multiple authors");
		}
		return String.valueOf(relations.get(0).getRelatedNode().getId());
	}

	@JsonIgnore
	public void setAuthor(User user) {
		if (!this.getRelationships().get(HAS_AUTHOR).isEmpty()) {
			// there is already an author, need to delete old relationships
			for (RelationshipInterface rel : this.getRelationships().get(HAS_AUTHOR)) {
				DBService.deleteReltionship(rel.getId());
			}
		}
		this.addRelation(HAS_AUTHOR, user, true);
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
	
	// TODO add other methods

}

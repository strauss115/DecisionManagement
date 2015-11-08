package at.jku.se.dm.rest.pojos;

import java.util.Date;
import java.util.LinkedList;

import at.jku.se.dm.rest.ResponseData;

public class Decision extends ResponseData {

	/**
	 * Unique name of decision
	 */
	private String name;
	private Date creationDate;
	private String author;
	private String alternatives, rationale, consequences;
	private LinkedList<InfluenceFactor> influenceFactors;
	private String team; // just team name
	private String group;
	private LinkedList<QualityAttribute> qualityAttributes;
	private LinkedList<String> relatedDecisions;
	private LinkedList<String> responsibles;
	private LinkedList<String> documents;
	
	// ------------------------------------------------------------------------
	
	public Decision() {
		
	}
	
	public Decision(String name, User author, Team team) {
		this.name = name;
		this.author = author.getEMail();
		this.team = team.getName();
		this.creationDate = new Date();
		
		this.influenceFactors = new LinkedList<InfluenceFactor>();
		this.qualityAttributes = new LinkedList<QualityAttribute>();
		this.relatedDecisions = new LinkedList<String>();
		this.responsibles = new LinkedList<String>();
		this.documents = new LinkedList<String>();
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author.getEMail();
	}
	public String getAlternatives() {
		return alternatives;
	}
	public void setAlternatives(String alternatives) {
		this.alternatives = alternatives;
	}
	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
	public String getConsequences() {
		return consequences;
	}
	public void setConsequences(String consequences) {
		this.consequences = consequences;
	}
	public LinkedList<InfluenceFactor> getInfluenceFactors() {
		return influenceFactors;
	}
	public void setInfluenceFactors(LinkedList<InfluenceFactor> influenceFactors) {
		this.influenceFactors = influenceFactors;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group.getName();
	}
	public LinkedList<QualityAttribute> getQualityAttributes() {
		return qualityAttributes;
	}
	public void setQualityAttributes(LinkedList<QualityAttribute> qualityAttributes) {
		this.qualityAttributes = qualityAttributes;
	}
	public LinkedList<String> getRelatedDecisions() {
		return relatedDecisions;
	}
	public void setRelatedDecisions(LinkedList<String> relatedDecisions) {
		this.relatedDecisions = relatedDecisions;
	}
	public LinkedList<String> getResponsibles() {
		return responsibles;
	}
	public void setResponsibles(LinkedList<String> responsibles) {
		this.responsibles = responsibles;
	}
	public LinkedList<String> getDocuments() {
		return documents;
	}
	public void setDocuments(LinkedList<String> documents) {
		this.documents = documents;
	}
	
	
}

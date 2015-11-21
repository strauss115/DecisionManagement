package at.jku.se.dm.rest.pojos;

import java.util.Date;
import java.util.LinkedList;

import at.jku.se.dm.rest.ResponseData;

public class Decision extends ResponseData {

	public static final String ID_PREFIX = "D";
	
	// ------------------------------------------------------------------------
	
	private String name;
	private String description;
	private Date creationDate;
	private String author;
	private LinkedList<InfluenceFactor> influenceFactors;
	private LinkedList<Rationale> rationales;
	private LinkedList<Alternative> alternatives;
	private LinkedList<Consequence> consequences;
	private LinkedList<QualityAttribute> qualityAttributes;
	private String team; // just team IDs
	private String group;
	private LinkedList<String> relatedDecisions;
	private LinkedList<String> responsibles;
	private LinkedList<String> documents;
	
	// ------------------------------------------------------------------------
	
	public Decision() {
		
	}
	
	/**
	 * Constructor used by backend for existing decision in database
	 * 
	 * @param id
	 * @param name
	 * @param author
	 * @param team
	 */
	public Decision(String id, String name, User author, Team team) {
		setId(id);
		this.name = name;
		this.author = author.getEMail();
		this.team = team.getName();
		this.creationDate = new Date();
		
		this.influenceFactors = new LinkedList<InfluenceFactor>();
		this.rationales = new LinkedList<Rationale>();
		this.alternatives = new LinkedList<Alternative>();
		this.consequences = new LinkedList<Consequence>();
		this.qualityAttributes = new LinkedList<QualityAttribute>();
		this.relatedDecisions = new LinkedList<String>();
		this.responsibles = new LinkedList<String>();
		this.documents = new LinkedList<String>();
	}
	
	
	/**
	 * Constructor used by API for new decision objects
	 * 
	 * @param name
	 * @param author
	 * @param team
	 */
	public Decision(String name, User author, Team team) {
		this (generateId(name, ID_PREFIX), name, author, team);
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public LinkedList<Alternative> getAlternatives() {
		return alternatives;
	}
	public void addAlternative(Alternative alternative) {
		alternatives.add(alternative);
	}
	public LinkedList<Rationale> getRationales() {
		return rationales;
	}
	public void addRationale(Rationale rationale) {
		rationales.add(rationale);
	}
	public LinkedList<Consequence> getConsequences() {
		return consequences;
	}
	public void addConsequence(Consequence consequence) {
		consequences.add(consequence);
	}
	public LinkedList<InfluenceFactor> getInfluenceFactors() {
		return influenceFactors;
	}
	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		influenceFactors.add(influenceFactor);
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

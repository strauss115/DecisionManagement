package at.jku.se.rest.web.pojos;

import java.util.Date;
import java.util.LinkedList;

import at.jku.se.rest.response.ResponseData;

public class WebDecision extends ResponseData {

	public static final String ID_PREFIX = "D";
	
	// ------------------------------------------------------------------------
	
	private String name;
	private String description;
	private Date creationDate;
	private String author;
	private LinkedList<InfluenceFactor> influenceFactors;
	private LinkedList<Rationale> rationales;
	private LinkedList<WebAlternative> alternatives;
	private LinkedList<Consequence> consequences;
	private LinkedList<QualityAttribute> qualityAttributes;
	private LinkedList<String> relatedDecisions;
	private LinkedList<String> responsibles;
	private LinkedList<String> documents;
	private DecisionGraph decisionGraph;
	
	// ------------------------------------------------------------------------
	
	public WebDecision() {
		this.name = "";
		this.author = "";
		this.creationDate = new Date();
		
		this.influenceFactors = new LinkedList<InfluenceFactor>();
		this.rationales = new LinkedList<Rationale>();
		this.alternatives = new LinkedList<WebAlternative>();
		this.consequences = new LinkedList<Consequence>();
		this.qualityAttributes = new LinkedList<QualityAttribute>();
		this.relatedDecisions = new LinkedList<String>();
		this.responsibles = new LinkedList<String>();
		this.documents = new LinkedList<String>();
		this.decisionGraph = new DecisionGraph();
	}
	
	/**
	 * Constructor used by backend for existing decision in database
	 * 
	 * @param id
	 * @param name
	 * @param author
	 * @param team
	 */
	public WebDecision(String id, String name, WebUser author) {
		this();
		
		setId(id);
		this.name = name;
		this.author = author.getEMail();
		this.creationDate = new Date();
	}
	
	/**
	 * Constructor used by API for new decision objects
	 * 
	 * @param name
	 * @param author
	 * @param team
	 */
	public WebDecision(String name, WebUser author) {
		this (generateId(name, ID_PREFIX), name, author);
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
	public void setAuthor(WebUser author) {
		this.author = author.getEMail();
	}
	public LinkedList<WebAlternative> getAlternatives() {
		return alternatives;
	}
	public void addAlternative(WebAlternative alternative) {
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
	/**
	 * Calculation of the graph
	 * @return decision as graph
	 */
	public DecisionGraph getDecisionGraph() {
		LinkedList<DecisionGraphNode> decisionNodes = new LinkedList<DecisionGraphNode>();
		LinkedList<DecisionGraphAssociation> decisionAssociation = new LinkedList<DecisionGraphAssociation>();
		DecisionGraphNode dn1 = new DecisionGraphNode();
		dn1.setKey(0);
		dn1.setText("Entscheidung 1");
		dn1.setLoc("0 0");
		decisionNodes.add(dn1);
		decisionNodes.add(new DecisionGraphNode(1,0,"Gruppe","skyblue","right","107 -22"));		
		decisionNodes.add(new DecisionGraphNode(11,1,"Architekturentscheidung","skyblue","right","200 -48"));
		decisionNodes.add(new DecisionGraphNode(2,0,"Lösungsalternative","darkseagreen","right","107 43"));
		decisionNodes.add(new DecisionGraphNode(21,2,"Fat-Client","darkseagreen","right","253 30"));
		decisionNodes.add(new DecisionGraphNode(3,0,"Einflussfaktor","palevioletred","left","-20 -31.75"));
		decisionNodes.add(new DecisionGraphNode(31,3,"Know How","palevioletred","left","-117 -64.25"));
		decisionNodes.add(new DecisionGraphNode(32,3,"Costs","palevioletred","left","-117 -25.25"));

		decisionAssociation.add(new DecisionGraphAssociation(0,1));
		decisionAssociation.add(new DecisionGraphAssociation(0,2));
		decisionAssociation.add(new DecisionGraphAssociation(0,3));
		decisionAssociation.add(new DecisionGraphAssociation(1,11));
		decisionAssociation.add(new DecisionGraphAssociation(2,21));
		decisionAssociation.add(new DecisionGraphAssociation(3,31));
		decisionAssociation.add(new DecisionGraphAssociation(3,32));
		
		this.decisionGraph.setDecisionNodes(decisionNodes);
		this.decisionGraph.setDecisionAssociation(decisionAssociation);
		return decisionGraph;
	}
/*
	public void parseDecisionToJSONData(){
		List<DecisionGraphNode> dn = new LinkedList<DecisionGraphNode>();
		List<DecisionGraphAssociation> da = new LinkedList<DecisionGraphAssociation>();
		DecisionGraphNode dn1 = new DecisionGraphNode();
		dn1.setKey(0);
		dn1.setText("Entscheidung 1");
		dn1.setLoc("0 0");
		dn.add(dn1);
		dn.add(new DecisionGraphNode(11,1,"Architekturentscheidung","skyblue","right","200 -48"));
		dn.add(new DecisionGraphNode(2,0,"Lösungsalternative","darkseagreen","right","107 43"));
		dn.add(new DecisionGraphNode(21,2,"Fat-Client","darkseagreen","right","253 30"));
		dn.add(new DecisionGraphNode(3,3,"Know How","palevioletred","left","-20 -31.75"));
		dn.add(new DecisionGraphNode(32,3,"Costs","palevioletred","left","-117 -25.25"));

		
		da.add(new DecisionGraphAssociation(0,1));
		da.add(new DecisionGraphAssociation(0,2));
		da.add(new DecisionGraphAssociation(0,3));
		da.add(new DecisionGraphAssociation(1,11));
		da.add(new DecisionGraphAssociation(2,21));
		da.add(new DecisionGraphAssociation(3,31));
		da.add(new DecisionGraphAssociation(3,32));
		
		this.decisionGraph.setDecisionNodes((LinkedList)dn);
		this.decisionGraph.setDecisionAssociation((LinkedList)da);
	}*/
	public void setDecisionGraph(DecisionGraph decisionGraph) {
		this.decisionGraph = decisionGraph;
	}
}

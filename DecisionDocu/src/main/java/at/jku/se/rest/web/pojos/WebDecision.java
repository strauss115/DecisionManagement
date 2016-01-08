package at.jku.se.rest.web.pojos;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.QualityAttribute;
import at.jku.se.model.Rationale;
import at.jku.se.rest.response.ResponseData;

public class WebDecision extends ResponseData {

	// ------------------------------------------------------------------------
	
	private String name;
	private String description;
	private Date creationDate;
	private String author;
	private List<InfluenceFactor> influenceFactors;
	private List<Rationale> rationales;
	private List<Alternative> alternatives;
	private List<Consequence> consequences;
	private List<QualityAttribute> qualityAttributes;
	private List<String> relatedDecisions;
	private List<String> responsibles;
	private List<String> documents;
	private String lastActivity;
	private DecisionGraph decisionGraph;
	
	// ------------------------------------------------------------------------
	
	public WebDecision() {
		this.name = "";
		this.author = "";
		this.lastActivity = "";
		this.creationDate = new Date();
		
		this.influenceFactors = new LinkedList<InfluenceFactor>();
		this.rationales = new LinkedList<Rationale>();
		this.alternatives = new LinkedList<Alternative>();
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
	public String getLastActivity() {
		return lastActivity;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	public void addAlternative(Alternative alternative) {
		alternatives.add(alternative);
	}
	public List<Rationale> getRationales() {
		return rationales;
	}
	public void addRationale(Rationale rationale) {
		rationales.add(rationale);
	}
	public List<Consequence> getConsequences() {
		return consequences;
	}
	public void addConsequence(Consequence consequence) {
		consequences.add(consequence);
	}
	public List<InfluenceFactor> getInfluenceFactors() {
		return influenceFactors;
	}
	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		influenceFactors.add(influenceFactor);
	}
	public List<QualityAttribute> getQualityAttributes() {
		return qualityAttributes;
	}
	public void setQualityAttributes(List<QualityAttribute> qualityAttributes) {
		this.qualityAttributes = qualityAttributes;
	}
	public List<String> getRelatedDecisions() {
		return relatedDecisions;
	}
	public void setRelatedDecisions(List<String> relatedDecisions) {
		this.relatedDecisions = relatedDecisions;
	}
	public List<String> getResponsibles() {
		return responsibles;
	}
	public void setResponsibles(List<String> responsibles) {
		this.responsibles = responsibles;
	}
	public List<String> getDocuments() {
		return documents;
	}
	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}
	public void setInfluenceFactors(List<InfluenceFactor> influenceFactors) {
		this.influenceFactors = influenceFactors;
	}
	public void setRationales(List<Rationale> rationales) {
		this.rationales = rationales;
	}
	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}
	public void setConsequences(List<Consequence> consequences) {
		this.consequences = consequences;
	}
	public void setLastActivity(String lastActivity) {
		this.lastActivity = lastActivity;
	}
	
	// ------------------------------------------------------------------------
	
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

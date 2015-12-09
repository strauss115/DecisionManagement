package at.jku.se.dm.rest.pojos;

import java.util.LinkedList;

import at.jku.se.dm.rest.ResponseData;

public class DecisionGraph extends ResponseData {

	private LinkedList<DecisionGraphNode> decisionNodes;
	private LinkedList<DecisionGraphAssociation> decisionAssociations;

	public DecisionGraph(LinkedList<DecisionGraphNode> decisionNodes,
			LinkedList<DecisionGraphAssociation> decisionAssociation) {
		this.decisionNodes = decisionNodes;
		this.decisionAssociations = decisionAssociation;
	}

	public DecisionGraph() {
	}

	public LinkedList<DecisionGraphNode> getDecisionNodes() {
		return decisionNodes;
	}

	public void setDecisionNodes(LinkedList<DecisionGraphNode> decisionNodes) {
		this.decisionNodes = decisionNodes;
	}

	public LinkedList<DecisionGraphAssociation> getDecisionAssociation() {
		return decisionAssociations;
	}

	public void setDecisionAssociation(LinkedList<DecisionGraphAssociation> decisionAssociation) {
		this.decisionAssociations = decisionAssociation;
	}

}

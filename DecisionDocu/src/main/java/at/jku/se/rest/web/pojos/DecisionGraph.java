package at.jku.se.rest.web.pojos;

import java.util.LinkedList;

import at.jku.se.rest.response.ResponseData;

public class DecisionGraph extends ResponseData {

	private LinkedList<DecisionGraphNode> decisionNodes;
	private LinkedList<DecisionGraphAssociation> decisionAssociations;

	/**
	 * Construcotr
	 * @param decisionNodes
	 * @param decisionAssociation
	 */
	public DecisionGraph(LinkedList<DecisionGraphNode> decisionNodes,
			LinkedList<DecisionGraphAssociation> decisionAssociation) {
		this.decisionNodes = decisionNodes;
		this.decisionAssociations = decisionAssociation;
	}

	/**
	 * Default constructor
	 */
	public DecisionGraph() {
	}

	/**
	 * Returns decision nodes
	 * @return A LinkedList of DecisionGraphNode
	 */
	public LinkedList<DecisionGraphNode> getDecisionNodes() {
		return decisionNodes;
	}

	/**
	 * Sets decision nodes
	 * @param decisionNodes
	 */
	public void setDecisionNodes(LinkedList<DecisionGraphNode> decisionNodes) {
		this.decisionNodes = decisionNodes;
	}

	/**
	 * Returns decision associations
	 * @return A LinkedList of DecisionGraphAssociation
	 */
	public LinkedList<DecisionGraphAssociation> getDecisionAssociation() {
		return decisionAssociations;
	}

	/**
	 * Sets DecisionAssociation
	 * @param decisionAssociation
	 */
	public void setDecisionAssociation(LinkedList<DecisionGraphAssociation> decisionAssociation) {
		this.decisionAssociations = decisionAssociation;
	}

}

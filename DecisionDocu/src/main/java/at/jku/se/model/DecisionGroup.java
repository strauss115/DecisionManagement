package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

public class DecisionGroup extends Node {

	public DecisionGroup(String name) {
		super(name);
	}

	public DecisionGroup(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public DecisionGroup() {
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.DECISIONGROUP;
	}

	// ------------------------------------------------------------------------

}

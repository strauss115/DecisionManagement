package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

public class Consequence extends Node {
	
	// ------------------------------------------------------------------------
	
	public Consequence() {

	}

	public Consequence(String name) {
		super(name);
	}
	
	public Consequence(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.CONSEQUENCE;
	}

	// ------------------------------------------------------------------------


}

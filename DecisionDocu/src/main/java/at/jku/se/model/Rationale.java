package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

public class Rationale extends Node {

	// ------------------------------------------------------------------------
	
	public Rationale() {
		
	}
	
	public Rationale(String name) {
		super(name);
	}
	
	public Rationale(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.RATIONALE;
	}

	// ------------------------------------------------------------------------

	
}

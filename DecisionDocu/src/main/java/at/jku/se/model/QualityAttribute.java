package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.strings.NodeString;

public class QualityAttribute extends Node {

	public QualityAttribute(String name) {
		super(name);
	}

	public QualityAttribute(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public QualityAttribute() {
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.QUALITYATTRIBUTE;
	}

	// ------------------------------------------------------------------------


}

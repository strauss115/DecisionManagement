package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.se.database.strings.NodeString;

@JsonTypeName("Property")
public class Property extends Node {

	public Property(String name) {
		super(name);
	}

	public Property() {
		super();
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.PROPERTY;
	}

	// ------------------------------------------------------------------------

}

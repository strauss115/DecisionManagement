package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Property")
public class Property extends Node {

	public Property(String name) {
		super(name);
	}

	public Property() {
		super();
	}
}

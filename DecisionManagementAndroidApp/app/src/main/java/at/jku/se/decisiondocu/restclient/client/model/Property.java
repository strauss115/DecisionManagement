package at.jku.se.decisiondocu.restclient.client.model;

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

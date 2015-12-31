package at.jku.se.decisiondocu.restclient.client.model;

import java.util.List;
import java.util.Map;

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


}

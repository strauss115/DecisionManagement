package at.jku.se.decisiondocu.restclient.client.model;

import java.util.List;
import java.util.Map;

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


	
}

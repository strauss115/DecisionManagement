package at.jku.se.decisiondocu.restclient.client.model;

import java.util.List;
import java.util.Map;

public class Document extends Node {

	private static final String URL = "URL";
	
	// ------------------------------------------------------------------------
	
	public Document() {
		
	}
	
	public Document(String name) {
		super(name);
	}
	
	public Document(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}
	
	// ------------------------------------------------------------------------

}

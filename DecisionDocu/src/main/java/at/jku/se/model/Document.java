package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.database.strings.NodeString;

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

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.DOCUMENT;
	}

	// ------------------------------------------------------------------------
	
	@JsonIgnore
	public String getUrl() {
		return getDirectProperty(URL);
	}
	
	@JsonIgnore
	public void setUrl(String url) {
		super.addDirectProperty(URL, url);
	}
	
	// ------------------------------------------------------------------------
}

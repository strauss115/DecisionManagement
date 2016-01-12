package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

/**
 * Class for Document Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Document extends Node {

	private static final String URL = "URL";
	
	// ------------------------------------------------------------------------
	/**
	 * Default constructor
	 */
	public Document() {
		
	}
	
	/**
	 * Constructor
	 * @param name
	 */
	public Document(String name) {
		super(name);
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param relations
	 */
	public Document(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}
	
	// ------------------------------------------------------------------------

	/**
	 * Returns the type of the node
	 * @return The type of the node as string
	 */
	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.DOCUMENT;
	}

	// ------------------------------------------------------------------------
	
	/**
	 * Returns the url to the document
	 * @return The url to the document
	 */
	@JsonIgnore
	public String getUrl() {
		return getDirectProperty(URL);
	}
	
	/**
	 * Sets the url to the document
	 * @param url
	 */
	@JsonIgnore
	public void setUrl(String url) {
		super.addDirectProperty(URL, url);
	}
	
	// ------------------------------------------------------------------------
}

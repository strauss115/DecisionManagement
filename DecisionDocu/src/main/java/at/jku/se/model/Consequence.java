package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

/**
 * Class for Consequence Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Consequence extends Node {
	
	// ------------------------------------------------------------------------
	
	/**
	 * Default constructor
	 */
	public Consequence() {

	}

	/**
	 * Constructor
	 * @param name
	 */
	public Consequence(String name) {
		super(name);
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param relations
	 */
	public Consequence(String name, Map<String, List<RelationshipInterface>> relations) {
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
		return NodeString.CONSEQUENCE;
	}

	// ------------------------------------------------------------------------


}

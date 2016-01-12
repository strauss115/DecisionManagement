package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

/**
 * Class for Rationale Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Rationale extends Node {

	// ------------------------------------------------------------------------
	/**
	 * Default constructor
	 */
	public Rationale() {
		
	}
	
	/**
	 * Constructor
	 * @param name
	 */
	public Rationale(String name) {
		super(name);
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param relations
	 */
	public Rationale(String name, Map<String, List<RelationshipInterface>> relations) {
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
		return NodeString.RATIONALE;
	}

	// ------------------------------------------------------------------------

	
}

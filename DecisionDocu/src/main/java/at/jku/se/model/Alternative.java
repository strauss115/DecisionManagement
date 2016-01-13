package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

/**
 * Class for Alternative Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Alternative extends Node {
	
	// ------------------------------------------------------------------------

	/**
	 * Constructor
	 * @param name Name of the alternative
	 */
	public Alternative(String name) {
		super(name);
	}
	
	/**
	 * Constructor
	 * @param name Name of the alternative
	 * @param relations List of relations of the alternative
	 */
	public Alternative(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	/**
	 * Default constructor
	 */
	public Alternative() {
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the type of the node
	 * @return The type of the node as string
	 */
	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.ALTERNATIVE;
	}

	// ------------------------------------------------------------------------

}

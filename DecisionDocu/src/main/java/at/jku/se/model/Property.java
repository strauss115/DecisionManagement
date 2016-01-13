package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.se.dm.shared.NodeString;

/**
 * Class for Property Nodes
 * It extends Nodes
 * @author August
 *
 */
@JsonTypeName("Property")
public class Property extends Node {

	/**
	 * Constructor
	 * @param name
	 */
	public Property(String name) {
		super(name);
	}
	
	/**
	 * Default constructor
	 */
	public Property() {
		super();
	}
	
	// ------------------------------------------------------------------------

	/**
	 * Returns the type of the node
	 * @return The type of the node as string
	 */
	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.PROPERTY;
	}

	// ------------------------------------------------------------------------

}

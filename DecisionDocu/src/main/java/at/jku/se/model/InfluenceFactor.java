package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

/**
 * Class for InfluenceFactor Nodes
 * It extends Nodes
 * @author August
 *
 */
public class InfluenceFactor extends Node {

	/**
	 * Constructor
	 * @param name
	 */
	public InfluenceFactor(String name) {
		super(name);
	}

	/**
	 * Constructor
	 * @param name
	 * @param relations
	 */
	public InfluenceFactor(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	/**
	 * Default constructor
	 */
	public InfluenceFactor() {
	}
	
	// ------------------------------------------------------------------------

	/**
	 * Returns the type of the node
	 * @return The type of the node as string
	 */
	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.INFLUENCEFACTOR;
	}

	// ------------------------------------------------------------------------

}

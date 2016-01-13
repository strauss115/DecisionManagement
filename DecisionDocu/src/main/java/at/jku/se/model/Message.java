package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;
import at.jku.se.dm.shared.RelationString;

/**
 * Class for Message Nodes
 * It extends Nodes
 * @author August
 *
 */
public class Message extends Node {
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor
	 * @param name
	 */
	public Message(String name) {
		super(name);
	}

	/**
	 * Default constructor
	 */
	public Message() {
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
		return NodeString.MESSAGE;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the authors email
	 * @return The email of the athor user node object
	 */
	@JsonIgnore
	public String getAuthorEmail() {
		User author = getSingleNodeByRelationship(RelationString.HAS_CREATOR, User.class);
		if (author != null)
			return author.getEmail();
		return "";
	}
	
}


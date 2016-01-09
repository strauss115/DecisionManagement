package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;
import at.jku.se.dm.shared.RelationString;

public class Message extends Node {
	
	// ------------------------------------------------------------------------
	
	public Message(String name) {
		super(name);
	}

	public Message() {
		super();
	}
	
	// ------------------------------------------------------------------------

	@JsonIgnore
	@Override
	public String getNodeType() {
		return NodeString.MESSAGE;
	}

	// ------------------------------------------------------------------------

	@JsonIgnore
	public String getAuthorEmail() {
		User author = getSingleNodeByRelationship(RelationString.HAS_CREATOR, User.class);
		if (author != null)
			return author.getEmail();
		return "";
	}
	
}


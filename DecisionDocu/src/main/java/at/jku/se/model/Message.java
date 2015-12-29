package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.jku.se.dm.shared.NodeString;

public class Message extends Node {
	
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

	
}


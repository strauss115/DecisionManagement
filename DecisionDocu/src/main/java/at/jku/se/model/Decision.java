package at.jku.se.model;

import java.util.List;
import java.util.Map;

public class Decision extends Node {

	public Decision(String name) {
		super(name);
	}

	public Decision(String name, Map<String,List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public Decision() {
		super();
	}
}

package at.jku.se.model;

import java.util.List;
import java.util.Map;

public class Alternative extends Node {

	public Alternative(String name) {
		super(name);
	}

	public Alternative(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public Alternative() {
	}

}

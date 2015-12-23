package at.jku.se.decisiondocu.restclient.client.model;

import java.util.List;
import java.util.Map;

public class DecisionGroup extends Node {

	public DecisionGroup(String name) {
		super(name);
	}

	public DecisionGroup(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public DecisionGroup() {
	}

}

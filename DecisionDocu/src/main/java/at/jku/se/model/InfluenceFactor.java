package at.jku.se.model;

import java.util.List;
import java.util.Map;

public class InfluenceFactor extends Node {

	public InfluenceFactor(String name) {
		super(name);
	}

	public InfluenceFactor(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public InfluenceFactor() {
	}
}

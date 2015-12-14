package at.jku.se.model;

import java.util.List;
import java.util.Map;

public class QualityAttribute extends Node {

	public QualityAttribute(String name) {
		super(name);
	}

	public QualityAttribute(String name, Map<String, List<RelationshipInterface>> relations) {
		super(name, relations);
	}

	public QualityAttribute() {
	}

}

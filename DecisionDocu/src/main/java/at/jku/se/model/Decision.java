package at.jku.se.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;

public class Decision extends Node {

	public Decision(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Decision(String name, Map<String,List<RelationshipInterface>> relations) {
		super(name, relations);
		// TODO Auto-generated constructor stub
	}

	public Decision() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

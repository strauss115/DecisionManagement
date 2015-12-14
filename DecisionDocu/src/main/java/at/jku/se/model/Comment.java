package at.jku.se.model;

import java.util.ArrayList;

public class Comment extends Node {

	private ArrayList<String> history;
	
	public Comment(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void addMessage(String message) {
		this.history.add(message);
	}
	
	public ArrayList<String> getHistory() {
		return history;
	}
	

}


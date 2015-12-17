package at.jku.se.rest.web.pojos;

import java.util.LinkedList;

import at.jku.se.rest.response.ResponseData;

public class Group extends ResponseData {

	/**
	 * Unique group name
	 */
	private String name;
	private String team;
	private LinkedList<String> decisions; // just names of decisions
	
	// ------------------------------------------------------------------------
	
	public Group() {
		
	}
	
	public Group(String name, WebTeam team) {
		this.name = name;
		this.team = team.getName();
		
		this.decisions = new LinkedList<String>();
	}
	
	// ------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}

	public LinkedList<String> getDecisionIds() {
		return decisions;
	}

	public boolean addDecision(WebDecision decision) {
		if (!decisions.contains(decision.getName())) {
			decisions.add(decision.getName());
			return true;
		}
		return false;
	}
	
	public boolean removeDecision(WebDecision decision) {
		if (decisions.contains(decision.getName())) {
			decisions.remove(decision.getName());
			return true;
		}
		return false;
	}
	
	
	
}

package at.jku.se.chatserver;
import java.util.ArrayList;


public class Decision {

	private String name;
	private String shortname;
	private int id;
	private ArrayList<String> history;
	private ArrayList<Propertie> properties;
	
	public Decision(String name, String shortname) {
		this.name = name;
		this.shortname = name;
		this.history = new ArrayList<String>();
		this.properties = new ArrayList<Propertie>();
	}
	
	public String toString() {
		String text = "Entscheidungschatverlauf fuer "+shortname+":\n";
		for (String line: history) {
			text += line+"\n";
		}
		return text;
	}
	
	public void addProbertie(Propertie probertie) {
		this.properties.add(probertie);
	}
	
	public ArrayList<Propertie> getProperties() {
		return this.properties;
	}
	
	public void addMessage(String message) {
		this.history.add(message);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<String> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}
	
}

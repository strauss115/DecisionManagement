package at.jku.se.chatserver;
import java.util.ArrayList;
import java.util.Date;


public class Propertie {

	private String name;
	private String value;
	private String user;
	private Date timestamp;
	private ArrayList<Comment> comments;
	
	public Propertie(String name, String value, String user) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.value = value;
		this.user = user;
		this.timestamp = new Date();
		this.comments = new ArrayList<Comment>();
	}

	public String toString() {
		String text = "Property '" + this.name +"' mit dem Wert '" + this.value +
				"' wurde von " + this.user + " am " + timestamp.toString() + " zugewiesen";
		return text;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

package at.jku.se.chatserver;

public class Comment {

	private String comment;
	private String user;
	
	public Comment(String comment, String user) {
		this.comment = comment;
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}

package at.jku.se.rest.web.pojos;

import java.util.Date;

import at.jku.se.rest.response.ResponseData;

public class Chat extends ResponseData {
	
	private String relatedObjectId;
	private String message;
	private String user;
	private Date time;
	
	// ------------------------------------------------------------------------
	
	public Chat(String relatedObjectId, String message, User user, Date time) {
		this.relatedObjectId = relatedObjectId;
		this.message = message;
		this.user = user.getEMail();
		this.time = time;
	}

	// ------------------------------------------------------------------------
		
	public String getRelatedObjectId() {
		return relatedObjectId;
	}

	public void setRelatedObjectId(String relatedObjectId) {
		this.relatedObjectId = relatedObjectId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user.getEMail();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}

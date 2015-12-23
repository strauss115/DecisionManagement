package at.jku.se.rest.web.pojos;

import java.util.Date;

import at.jku.se.rest.response.ResponseData;

public class Document extends ResponseData {
	
	/**
	 * Unique document name
	 */
	private String name;
	private String url;
	private Date creationDate;
	
	// ------------------------------------------------------------------------
	
	public Document() {
		
	}
	
	public Document(String name, String url, Date creationDate) {
		this.name = name;
		this.url = url;
		this.creationDate = creationDate;
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}

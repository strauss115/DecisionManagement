package at.jku.se.rest.web.pojos;

import at.jku.se.rest.response.ResponseData;

public class Token extends ResponseData {
	
	private String token;
	
	// ------------------------------------------------------------------------
	
	public Token(String token) {
		this.token = token;
	}
	
	// ------------------------------------------------------------------------
	
	public String getToken() {
		return token;
	}

}

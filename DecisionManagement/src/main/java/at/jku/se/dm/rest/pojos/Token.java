package at.jku.se.dm.rest.pojos;

import at.jku.se.dm.rest.ResponseData;

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

package at.jku.se.dm.rest.pojos;

import at.jku.se.dm.rest.RestResponse;

public class Token extends RestResponse {
	
	private String token;
	
	// ------------------------------------------------------------------------
	
	public Token(String token) {
		this.token = token;
	}
	
	// ------------------------------------------------------------------------
	
	public String getToket() {
		return token;
	}

}

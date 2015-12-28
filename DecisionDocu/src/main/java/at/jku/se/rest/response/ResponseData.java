package at.jku.se.rest.response;

/**
 * Abstract superclass for REST response data
 */
public abstract class ResponseData {
	
	private String id;
	
	// ------------------------------------------------------------------------
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	// ------------------------------------------------------------------------
	
}

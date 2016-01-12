package at.jku.se.rest.response;

/**
 * Abstract superclass for REST response data
 */
public abstract class ResponseData {
	
	private String id;
	
	// ------------------------------------------------------------------------
	
	/**
	 * Returns Id as String
	 * @return {@link String}
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets Id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	// ------------------------------------------------------------------------
	
}

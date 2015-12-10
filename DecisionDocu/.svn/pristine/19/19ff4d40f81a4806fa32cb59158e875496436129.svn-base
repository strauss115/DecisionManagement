package at.jku.se.rest.response;

/**
 * Enumeration for all used HTTP status codes
 */
public enum HttpCode {
	
	/**
	 * HTTP Success Status Codes
	 */
	HTTP_200_OK (200),
	HTTP_201_CREATED (201),
	HTTP_202_ACCEPTED (202),
	HTTP_204_NO_CONTENT (204),
	HTTP_205_RESET_CONTENT (205),
	
	/**
	 * HTTP Error Status Codes
	 */
	HTTP_400_BAD_REQUEST (400),
	HTTP_401_UNAUTHORIZED (401),
	HTTP_403_FORBIDDEN (403),
	HTTP_404_NOT_FOUND (404),
	HTTP_405_METHOD_NOT_ALLOWED (405),
	HTTP_406_NOT_ACCEPTABLE (406),
	HTTP_500_SERVER_ERROR (500),
	HTTP_501_NOT_IMPLEMENTED (501),
	HTTP_503_SERVICE_UNAVAIALABLE (503);
	
	// ------------------------------------------------------------------------
	
	private int code;
	
	// ------------------------------------------------------------------------
	
	private HttpCode(int code) {
		this.code = code;
	}
	
	// ------------------------------------------------------------------------
	
	public int getCode() {
		return code;
	}

}

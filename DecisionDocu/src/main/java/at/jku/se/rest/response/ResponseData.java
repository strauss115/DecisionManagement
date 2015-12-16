package at.jku.se.rest.response;

/**
 * Abstract superclass for REST response data
 */
public abstract class ResponseData {
	
	private String session;
	private String id;
	
	// ------------------------------------------------------------------------
	
	public String getSession() {
		return session;
	}
	
	public void setSession(String session) {
		this.session = session;
	}
	
	public String getId() {
		return id;
	}
	
	protected void setId(String id) {
		this.id = id;
	}

	// ------------------------------------------------------------------------
	
	public static boolean checkId(String id, String prefix) {
		if (id.startsWith(prefix))
			return true;
		return false;
	}
	
	public static String generateId(String id, String prefix) {
		if(checkId(id, prefix))
			return id;
		return prefix + id;
	}
	
}

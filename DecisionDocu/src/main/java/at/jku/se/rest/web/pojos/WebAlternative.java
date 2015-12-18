package at.jku.se.rest.web.pojos;

public class WebAlternative extends AttributeValuePair {
	
	public static final String ID_PREFIX = "ALT";
	
	// ------------------------------------------------------------------------
	
	public WebAlternative() {
		
	}
	
	public WebAlternative(String id, String name, String description) {
		super(name, description);
		setId(generateId(id, ID_PREFIX));
	}
	
	// ------------------------------------------------------------------------


}

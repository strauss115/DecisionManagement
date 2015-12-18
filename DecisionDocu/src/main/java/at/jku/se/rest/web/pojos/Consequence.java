package at.jku.se.rest.web.pojos;

public class Consequence extends AttributeValuePair {
	
	public static final String ID_PREFIX = "CON";

	// ------------------------------------------------------------------------
	
	public Consequence() {
		
	}
	
	public Consequence(String id, String name, String description) {
		super(name, description);
		setId(generateId(id, ID_PREFIX));
	}
	
	// ------------------------------------------------------------------------

}

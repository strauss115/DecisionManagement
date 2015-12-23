package at.jku.se.rest.web.pojos;

public class Rationale extends AttributeValuePair {
	
	public static final String ID_PREFIX = "RA";
	
	// ------------------------------------------------------------------------

	public Rationale() {
		
	}
	
	public Rationale(String id, String name, String description) {
		super(name, description);
		setId(generateId(id, ID_PREFIX));
	}
	
	// ------------------------------------------------------------------------

}

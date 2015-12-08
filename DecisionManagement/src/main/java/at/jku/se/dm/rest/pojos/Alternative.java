package at.jku.se.dm.rest.pojos;

public class Alternative extends AttributeValuePair {
	
	public static final String ID_PREFIX = "ALT";
	
	// ------------------------------------------------------------------------
	
	public Alternative() {
		
	}
	
	public Alternative(String id, String name, String description) {
		super(name, description);
		setId(generateId(id, ID_PREFIX));
	}
	
	// ------------------------------------------------------------------------


}

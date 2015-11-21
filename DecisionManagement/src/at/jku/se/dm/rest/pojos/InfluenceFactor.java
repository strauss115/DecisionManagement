package at.jku.se.dm.rest.pojos;

public class InfluenceFactor extends AttributeValuePair {
	
	public static final String ID_PREFIX = "IF";
	
	// ------------------------------------------------------------------------
	
	public InfluenceFactor() {
		
	}
	
	public InfluenceFactor(String id, String name, String description) {
		super(name, description);
		setId(generateId(id, ID_PREFIX));
	}
	
	// ------------------------------------------------------------------------


}

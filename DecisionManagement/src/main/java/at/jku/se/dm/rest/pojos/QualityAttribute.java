package at.jku.se.dm.rest.pojos;

public class QualityAttribute extends AttributeValuePair {
		
	public static final String ID_PREFIX = "QA";
	
	// ------------------------------------------------------------------------

	public QualityAttribute() {
		
	}
	
	public QualityAttribute(String id, String name, String description) {
		super(name, description);
		setId(generateId(id, ID_PREFIX));
	}
	
	// ------------------------------------------------------------------------
	
}

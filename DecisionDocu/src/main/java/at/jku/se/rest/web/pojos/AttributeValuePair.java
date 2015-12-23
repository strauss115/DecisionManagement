package at.jku.se.rest.web.pojos;

import at.jku.se.rest.response.ResponseData;


/**
 * Abstract base class for a attribute value pair e.g. InfluenceFactors
 */
public abstract class AttributeValuePair extends ResponseData {
	
	private String name;
	private String value;

	// ------------------------------------------------------------------------
	
	public AttributeValuePair() {
		
	}
	
	public AttributeValuePair(String name, String description) {
		this.name = name;
		this.value = description;
	}
	
	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
	
	// ------------------------------------------------------------------------
	
}

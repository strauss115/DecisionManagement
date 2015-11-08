package at.jku.se.dm.rest.pojos;

import at.jku.se.dm.rest.ResponseData;

public class QualityAttribute extends ResponseData {
	
	/**
	 * Unique name
	 */
	private String name;
	private String value;
	
	// ------------------------------------------------------------------------

	public QualityAttribute() {
		
	}
	
	public QualityAttribute(String name, String value) {
		this.name = name;
		this.value = value;
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

}

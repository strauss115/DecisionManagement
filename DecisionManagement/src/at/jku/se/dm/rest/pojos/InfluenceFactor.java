package at.jku.se.dm.rest.pojos;

import at.jku.se.dm.rest.ResponseData;

public class InfluenceFactor extends ResponseData {
	
	private String hashTag;
	private String value;
	
	// ------------------------------------------------------------------------
	
	public InfluenceFactor() {
		
	}
	
	public InfluenceFactor(String hashTag, String value) {
		this.hashTag = hashTag;
		this.value = value;
	}
	
	// ------------------------------------------------------------------------

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

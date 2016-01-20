package at.jku.se.dm.shared;

public class RelationString {

	// TODO please check if correct relation strings were used for both APIs !!!
	
	// General
	public final static String HAS_CREATOR = "creator";

	// Decision -- relations
	public static final String HAS_INFLUENCE_FACTOR = "hasInfluenceFactor";
	public static final String HAS_RATIONALE = "hasRationale";
	public static final String HAS_ALTERNATIVE = "hasAlternative";
	public static final String HAS_CONSEQUENCE = "hasConsequence";
	public static final String HAS_QUALITY_ATTRIBUTES = "hasQualityAttribute";
	public static final String HAS_RELATED_DECISION = "hasRelatedDecisions";
	public static final String HAS_RESPONSIBLE = "hasResponsibles";
	public static final String HAS_DOCUMENT = "hasDocuments";
	public static final String HAS_PICTURE = "hasPicture";
	
	// Project -- relations
	public final static String HAS_DECISION = "hasdecision";
	public final static String HAS_PROJECTADMIN = "projectadmin";
	public final static String HAS_ACTIVITY = "hasActivity";
	
	// User -- relations
	public final static String HAS_PROJECT = "hasproject";
	public final static String LIKES = "likes";
	
	// Message -- relations
	public final static String HAS_MESSAGE = "message";
	public final static String CREATE_DNODE = "createdNode";
	
}

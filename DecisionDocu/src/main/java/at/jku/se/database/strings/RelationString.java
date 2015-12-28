package at.jku.se.database.strings;

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
	public final static String HAS_PROJECTADMIN = "projectadmin";
	
	// Project -- relations
	public final static String HAS_DECISION = "hasdecision";
	
	// User -- relations
	public final static String HAS_PROJECT = "hasProject";
	
	// Message -- relations
	public final static String HAS_MESSAGE = "message";
}

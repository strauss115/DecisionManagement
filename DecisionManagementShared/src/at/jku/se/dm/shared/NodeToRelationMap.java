package at.jku.se.dm.shared;

import java.util.HashMap;
import java.util.Map;

public class NodeToRelationMap{
	
	public static HashMap<String,Map<String,String>> NodeToRelationMap = new HashMap<String,Map<String,String>>();
	public static HashMap<String, Boolean> RelationNeedsExistingNode = new HashMap<String, Boolean>();
	
	static{
		
		HashMap<String,String> decision = new HashMap<String, String>();
		decision.put(RelationString.HAS_INFLUENCE_FACTOR, NodeString.INFLUENCEFACTOR);
		decision.put(RelationString.HAS_RATIONALE, NodeString.RATIONALE);
		decision.put(RelationString.HAS_ALTERNATIVE, NodeString.ALTERNATIVE);
		decision.put(RelationString.HAS_CONSEQUENCE, NodeString.CONSEQUENCE);
		decision.put(RelationString.HAS_QUALITY_ATTRIBUTES, NodeString.QUALITYATTRIBUTE);
		decision.put(RelationString.HAS_RELATED_DECISION, NodeString.DECISION);
		decision.put(RelationString.HAS_RESPONSIBLE, NodeString.USER);
		decision.put(RelationString.HAS_DOCUMENT, NodeString.DOCUMENT);
		decision.put(RelationString.HAS_CREATOR, NodeString.USER);
		NodeToRelationMap.put(NodeString.DECISION, decision);
		
		HashMap<String,String> project = new HashMap<String, String>();
		project.put(RelationString.HAS_DECISION, NodeString.INFLUENCEFACTOR);
		project.put(RelationString.HAS_PROJECTADMIN, NodeString.INFLUENCEFACTOR);
		project.put(RelationString.HAS_CREATOR, NodeString.USER);
		NodeToRelationMap.put(NodeString.PROJECT, project);
		
		HashMap<String,String> user = new HashMap<String, String>();
		project.put(RelationString.HAS_PROJECT, NodeString.PROJECT);
		NodeToRelationMap.put(NodeString.USER, user);
		
		RelationNeedsExistingNode.put(RelationString.HAS_CREATOR,true);
		RelationNeedsExistingNode.put(RelationString.HAS_RELATED_DECISION,true);
		RelationNeedsExistingNode.put(RelationString.HAS_RESPONSIBLE,true);
		RelationNeedsExistingNode.put(RelationString.HAS_DOCUMENT,true);
		RelationNeedsExistingNode.put(RelationString.HAS_DECISION,true);
		RelationNeedsExistingNode.put(RelationString.HAS_PROJECTADMIN,true);
		RelationNeedsExistingNode.put(RelationString.HAS_PROJECT,true);
		
		RelationNeedsExistingNode.put(RelationString.HAS_INFLUENCE_FACTOR,false);
		RelationNeedsExistingNode.put(RelationString.HAS_RATIONALE,false);
		RelationNeedsExistingNode.put(RelationString.HAS_ALTERNATIVE,false);
		RelationNeedsExistingNode.put(RelationString.HAS_CONSEQUENCE,false);
		RelationNeedsExistingNode.put(RelationString.HAS_QUALITY_ATTRIBUTES,false);
	}

}

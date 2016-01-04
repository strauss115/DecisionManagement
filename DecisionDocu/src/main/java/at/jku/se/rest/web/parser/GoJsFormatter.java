package at.jku.se.rest.web.parser;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.*;

import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.Decision;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.QualityAttribute;
import at.jku.se.model.Rationale;
import at.jku.se.rest.web.pojos.*;


public class GoJsFormatter {

	/**
	 * method to convert a decision to a go-js-graph (json)
	 * @param decision decision
	 * @return decision graph as json
	 */
	public static String convertDecisionToGoJsJson(WebDecision decision){
			String decisionAsJsonString = "[";
			decisionAsJsonString += "{\"key\": 0, \"text\": \"" + decision.getName() + "\", \"loc\": \"0 0\", \"editable\": false, \"showAdd\": false}";
			// configuration
			int distanceRight = decision.getName().length() * 7;
			int distanceLeft = 50;
			// right side elements (influence factors and rationals)
			int rightElementsCount = decision.getInfluenceFactors().size() + decision.getRationales().size() + decision.getQualityAttributes().size();
			if(rightElementsCount % 2 != 0){
				rightElementsCount++;
			}
			rightElementsCount = rightElementsCount / 2;
			// left side elements (alternatives and consequences)
			int leftElementsCount = decision.getInfluenceFactors().size() + decision.getRationales().size();
			if(leftElementsCount % 2 != 0){
				leftElementsCount++;
			}
			leftElementsCount = leftElementsCount / 2;
			// distance in pixel - y-axes
			int distanceFactor = 50;
			// add influences
			decisionAsJsonString += ",{\"key\": 1,\"parent\":0,\"text\":\"Influence Factors\", \"brush\": \"skyblue\", \"dir\": \"right\", \"loc\": \"" + distanceRight + " -22\",\"editable\": false, \"showAdd\": true}";
			int key = 11;
			for(InfluenceFactor i : decision.getInfluenceFactors()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":1,\"text\":\"" + i.getName() + "\", \"brush\": \"skyblue\", \"dir\": \"right\", \"loc\": \"" + (distanceRight + 250) + " " + (rightElementsCount * -1 * distanceFactor) + "\",\"editable\": false, \"showAdd\": true}";
				rightElementsCount--;
				key++;				
			}
			
			// add rationals
			decisionAsJsonString += ",{\"key\": 2,\"parent\":0,\"text\":\"Rationales\", \"brush\": \"darkseagreen\", \"dir\": \"right\", \"loc\": \"" + distanceRight + " 43\",\"editable\": false, \"showAdd\": true}";
			key = 21;
			for(Rationale r : decision.getRationales()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":2,\"text\":\"" + r.getName() + "\", \"brush\": \"darkseagreen\", \"dir\": \"right\", \"loc\": \"" + (distanceRight + 250) + " " + (rightElementsCount * -1 * distanceFactor) + "\",\"editable\": false, \"showAdd\": true}";
				rightElementsCount--;
				key++;				
			}
			// add alternatives
			decisionAsJsonString += ",{\"key\": 3,\"parent\":0,\"text\":\"Alternatives\", \"brush\": \"palevioletred\", \"dir\": \"left\", \"loc\": \"" + distanceLeft * (-1) + " -100\",\"editable\": false, \"showAdd\": true}";
			key = 31;
			for(Alternative a : decision.getAlternatives()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":3,\"text\":\"" + a.getName() + "\", \"brush\": \"palevioletred\", \"dir\": \"left\", \"loc\": \"" + ((distanceLeft * -1) - 250) + " " + (leftElementsCount * -1 * distanceFactor) + "\",\"editable\": false, \"showAdd\": true}";
				leftElementsCount--;
				key++;				
			}
			// add consequences
			decisionAsJsonString += ",{\"key\": 4,\"parent\":0,\"text\":\"Consequences\", \"brush\": \"coral\", \"dir\": \"left\", \"loc\": \"" + distanceLeft * (-1) + " 0\",\"editable\": false, \"showAdd\": true}";
			key = 41;
			for(Alternative a : decision.getAlternatives()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":4,\"text\":\"" + a.getName() + "\", \"brush\": \"coral\", \"dir\": \"left\", \"loc\": \"" + ((distanceLeft * -1) - 250) + " " + (leftElementsCount * -1 * distanceFactor) + "\",\"editable\": false, \"showAdd\": true}";
				leftElementsCount--;
				key++;				
			}
			// add quality attributes
			decisionAsJsonString += ",{\"key\": 5,\"parent\":0,\"text\":\"Quality Attributes\", \"brush\": \"grey\", \"dir\": \"left\", \"loc\": \"" + distanceLeft * (-1) + " 100\",\"editable\": false, \"showAdd\": true}";
			key = 51;
			for(QualityAttribute qa : decision.getQualityAttributes()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":4,\"text\":\"" + qa.getName() + "\", \"brush\": \"grey\", \"dir\": \"left\", \"loc\": \"" + ((distanceLeft * -1) - 250) + " " + (leftElementsCount * -1 * distanceFactor) + "\",\"editable\": false, \"showAdd\": true}";
				leftElementsCount--;
				key++;				
			}
			decisionAsJsonString += "]";
			return decisionAsJsonString;
	}	
	/**
	 * Method to convert decisions
	 * @return
	 */
	public static String convertDecisionsToRelationshipsOverviewGraph(List<WebDecision> decisions){
		String json = "{\"data\":[";
		
		//{\"key\": 0, \"loc\": \"120 120\", \"text\": \"Entscheidung 1\"},{\"key\": 1, \"loc\": \"330 120\", \"text\": \"Entscheidung 2\"}],\"relations\":[{\"from\": 0, \"to\": 1, \"text\": \"#influences\", \"curviness\": 20}]}";
		int count = 0;
		int y = -200;
		ArrayList<String> from = new ArrayList<String>();
		ArrayList<String> to = new ArrayList<String>();
		// test data
		String testConnection = "";
		// add decision nodes
		for(WebDecision wd : decisions){
			if(!testConnection.equals("")){
				LinkedList<String> con = new LinkedList<String>();
				con.add(testConnection);
				wd.setRelatedDecisions(con);
			}
			if(count != 0){
				json += ",";
			}
			json += "{\"key\":" + wd.getId() + ", \"loc\": \"0 " + y + "\", \"text\": \"" + wd.getName() + "\"}";
			count++;
			y += 100;
			// add decision relationships to from-/to-lists
			for(String rel : wd.getRelatedDecisions()){
				from.add(wd.getId());
				to.add(rel);
			}
			testConnection = wd.getId();
		}
		// remove duplicated relationships
		
		json += "],\"relations\":[";
		count = 0;
		// add relationships
		for(int i = 0; i < from.size();i++){
			if(count != 0){
				json += ",";
			}
			json += "{\"from\": " + from.get(i) + ", \"to\": " + to.get(i) + ", \"text\": \"#relatedTo\", \"curviness\": 20}";
			count++;
		}
		json += "]}";
		return json;
	}
}

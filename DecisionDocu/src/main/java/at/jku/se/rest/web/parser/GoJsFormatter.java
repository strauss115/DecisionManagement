package at.jku.se.rest.web.parser;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.*;

import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.QualityAttribute;
import at.jku.se.model.Rationale;
import at.jku.se.rest.web.pojos.*;


public class GoJsFormatter {

	private static String resultJSON = "";
	
	public String getGoJsString(){
		return resultJSON;
	}
	
	
	public GoJsFormatter(){
		
	}
	
	public GoJsFormatter(WebDecision decision){
			String decisionAsJsonString = "[";
			decisionAsJsonString += "{\"key\": 0, \"text\": \"" + decision.getName() + "\", \"loc\": \"0 0\", \"editable\": false, \"showAdd\": false}";
			// configuration
			int distanceRight = decision.getName().length() * 7;
			int distanceLeft = 50;
			// right side elements (influence factors and rationals)
			int rightElementsCount = decision.getInfluenceFactors().size() + decision.getRationales().size();
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
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":1,\"text\":\"" + i.getName() + "\", \"brush\": \"skyblue\", \"dir\": \"right\", \"loc\": \"" + (distanceRight + 250) + " " + (rightElementsCount * -1 * distanceFactor) + "\",\"editable\": true, \"showAdd\": false}";
				rightElementsCount--;
				key++;				
			}
			
			// add rationals
			decisionAsJsonString += ",{\"key\": 2,\"parent\":0,\"text\":\"Rationals\", \"brush\": \"darkseagreen\", \"dir\": \"right\", \"loc\": \"" + distanceRight + " 43\",\"editable\": false, \"showAdd\": true}";
			key = 21;
			for(Rationale r : decision.getRationales()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":2,\"text\":\"" + r.getName() + "\", \"brush\": \"darkseagreen\", \"dir\": \"right\", \"loc\": \"" + (distanceRight + 250) + " " + (rightElementsCount * -1 * distanceFactor) + "\",\"editable\": true, \"showAdd\": false}";
				rightElementsCount--;
				key++;				
			}
			// add alternatives
			decisionAsJsonString += ",{\"key\": 3,\"parent\":0,\"text\":\"Alternatives\", \"brush\": \"palevioletred\", \"dir\": \"left\", \"loc\": \"" + distanceLeft * (-1) + " -50\",\"editable\": false, \"showAdd\": true}";
			key = 31;
			for(Alternative a : decision.getAlternatives()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":3,\"text\":\"" + a.getName() + "\", \"brush\": \"palevioletred\", \"dir\": \"left\", \"loc\": \"" + ((distanceLeft * -1) - 250) + " " + (leftElementsCount * -1 * distanceFactor) + "\",\"editable\": true, \"showAdd\": false}";
				leftElementsCount--;
				key++;				
			}
			// add consequences
			decisionAsJsonString += ",{\"key\": 4,\"parent\":0,\"text\":\"Consequences\", \"brush\": \"coral\", \"dir\": \"left\", \"loc\": \"" + distanceLeft * (-1) + " 50\",\"editable\": false, \"showAdd\": true}";
			key = 41;
			for(Alternative a : decision.getAlternatives()){
				decisionAsJsonString += ",{\"key\": " + key + ",\"parent\":4,\"text\":\"" + a.getName() + "\", \"brush\": \"coral\", \"dir\": \"left\", \"loc\": \"" + ((distanceLeft * -1) - 250) + " " + (leftElementsCount * -1 * distanceFactor) + "\",\"editable\": true, \"showAdd\": false}";
				leftElementsCount--;
				key++;				
			}
			decisionAsJsonString += "]";
			resultJSON = decisionAsJsonString;
	}		
}

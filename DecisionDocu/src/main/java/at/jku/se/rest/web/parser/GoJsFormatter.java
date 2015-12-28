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


/**
 * @author apa
 *
 */


public class GoJsFormatter {

	private static String resultJSON = "";
	
	public String getGoJsString(){
		return resultJSON;
	}
	
	
	public GoJsFormatter(){
		
	}
	
	public GoJsFormatter(WebDecision decision){
			String decisionAsJsonString = "[";
			decisionAsJsonString += "{\"key\": 0, \"text\": \"Mind Map\", \"loc\": \"0 0\"}";
			
			decisionAsJsonString += "]";
			resultJSON = decisionAsJsonString;
	}		
}

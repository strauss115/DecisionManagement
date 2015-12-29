package at.jku.se.testdata;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.model.Message;
import at.jku.se.model.CustomDate;
import at.jku.se.model.Decision;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Property;
import at.jku.se.model.Project;
import at.jku.se.model.User;

public class SampleObjectProvider {
	
	private static ObjectMapper mapper = new ObjectMapper();
	private static final String DEFAULT_PASSWORD = "password";
	
	private static List<Decision> decisions = new ArrayList<Decision>();
	private static List<User> users = new ArrayList<User>();
	private static List<Project> teams = new ArrayList<Project>();
	
	static{
		
		//mapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
		//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		//Decisions
		Decision dec1 = new Decision("Extend System B to implement interactive approval processing");
		dec1.setId(1);
		dec1.setCreationDate(new CustomDate());
		dec1.addRelation("issue", new Property("Current IT infrastructure doesn’t support interactive approval functionality for most financial products"),true );
		dec1.addRelation("assumption", new Property("Extend System B beyond its original functional boundaries to implement interactive approval processing for the financial products it handles"), true);
		dec1.addRelation("assumption", new Property("We must deliver new capabilities in six months"), true);
		dec1.addRelation("assumption", new Property("We can’t increase the project budget by more than 10 percent"), true);
		
		Property assumption = new Property("reduce duplicate business logic");
		Message comment = new Message("This is a comment");
		comment.addRelation("comment", new Message("subcomment"), true);
		assumption.addRelation("comment", comment, true);
		dec1.addRelation("argument", assumption, true);
		
		Property alternativ = new Property("Extend System B to handle a new product type");
		alternativ.addRelation("argument", new Property("argument of the alternative"), true);
		dec1.addRelation("alternative", alternativ , true);
		dec1.addRelation("alternative", new Property("Develop a replacement for System A"), true);
		
		decisions.add(dec1);
		
		Decision dec2 = new Decision("Rollout only new marketing campaigns on new platform");
		decisions.add(dec2);
		
		Decision dec3 = new Decision("All batch interfaces will be replaced");
		decisions.add(dec3);
		
		//Teams user
		User u1 = new User("user1@u1.com", "u1first", "U1last", DEFAULT_PASSWORD,true);
		u1.setId(10);
		User u2 = new User("user2@u2.com", "u2first", "U2last", DEFAULT_PASSWORD,false);
		u2.setId(11);
		User u3 = new User("user2@u3.com", "u3first", "u3last", DEFAULT_PASSWORD,false);
		User u4 = new User("user2@u4.com", "u4first", "u4last", DEFAULT_PASSWORD,false);
		
		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		
		Project team1 = new Project("Team1", u1, DEFAULT_PASSWORD);
		Project team2 = new Project("Team2", u2, DEFAULT_PASSWORD);
		
		teams.add(team1);
		teams.add(team1);
		
		u1.addToProject(team1);
		u3.addToProject(team1);
		u2.addToProject(team2);
		u4.addToProject(team2);
		
		dec1.addRelation("decisionteam", team1, true);
		dec2.addRelation("decisionteam", team1, true);
		dec2.addRelation("decisionteam", team2, true);
	}
	
	public static List<Decision> getAllDecision(){
		return decisions;
	}
	
	public static void main (String[]args){
		
		try {
			//System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(decisions.toArray(new NodeInterface[0])));
			String json = mapper.writeValueAsString(decisions.toArray(new NodeInterface[0]));
			try {
				NodeInterface[] array = mapper.readValue(json, NodeInterface[].class);
				for(int i=0; i<array.length; i++){
					System.out.println(array[i]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

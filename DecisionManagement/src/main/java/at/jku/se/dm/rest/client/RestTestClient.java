package at.jku.se.dm.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.owlike.genson.Genson;

import at.jku.se.dm.rest.pojos.Group;
import at.jku.se.dm.rest.pojos.Team;

/**
 * Test client to test REST API on local machine
 */
public class RestTestClient {

	private static final Logger log = LogManager.getLogger(RestTestClient.class);
	
	private static Genson json = new Genson();
	
	private static Client client = ClientBuilder.newClient();
	private static WebTarget target = client.target("http://localhost:8080/DecisionManagement/rest/");

	// ------------------------------------------------------------------------
	
	public static void main(String[] args) {
		addNewGroup();
	}
	
	// ------------------------------------------------------------------------
	
	public static void addNewGroup() {
		Group group = new Group("New Group", new Team("team 3"));
		String groupJson = json.serialize(group);		
		
		log.info("PUT group '" + group.getName() + "'");
		Response r = target.path("/group/").request().post(Entity.json(groupJson));	
		
		log.info("Response status: " + r.getStatus());
	}
	
}

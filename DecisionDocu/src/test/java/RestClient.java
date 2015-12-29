import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Invocation.Builder;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.model.Decision;

public class RestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			
			ClientConfig config = new ClientConfig();
		    Client client = ClientBuilder.newClient(config);
			WebTarget service = client.target("http://localhost:8080/DecisionDocu/api/decision");
			Builder builder = service.request().accept(MediaType.APPLICATION_JSON);
			builder.header("Token", "g0up9ej1egkmrtveig59ke0adf");
			Response response = builder.get();
			String string = response.readEntity(String.class);
			ObjectMapper mapper = new ObjectMapper();
			Decision[] decs = mapper.readValue(string, Decision[].class);
			for(Decision dec:decs){
				System.out.println(dec.getName());
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}

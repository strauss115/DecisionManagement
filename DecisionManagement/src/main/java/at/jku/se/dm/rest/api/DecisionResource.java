package at.jku.se.dm.rest.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.parser.GoJsFormatter;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.pojos.Decision;
import at.jku.se.dm.rest.pojos.InfluenceFactor;

@Path("/decision")
public class DecisionResource {
	
	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	
	// not needed so far
	// @Context
	// UriInfo ui;
	
	// ------------------------------------------------------------------------
	
	public DecisionResource() {
		
	}
	
	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		log.debug("GET all decisions");
		
		List<Decision> decisions = SampleObjectProvider.getAllDecisions();
		log.info("GET all decisions returning '" + decisions.size() + "' elements");
		
		return RestResponse.getSuccessResponse(decisions);
	}
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("name") String name) {
		log.debug("GET decision '" + name + "'");
		
		Decision d = SampleObjectProvider.getDecisionByName(name);
		if (d != null) {
			log.info("GET decision returning '" + d.getName() + "'");
			return RestResponse.getSuccessResponse(d);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}
	
	@GET
	@Path("/byTeam/{teamName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByTeamName(@PathParam("teamName") String teamName) {
		log.debug("GET decisions by team name '" + teamName + "'");
		
		List<Decision> decisions = SampleObjectProvider.getDecisionsByTeamName(teamName);
		log.info("GET decisions by team name returning '" + decisions.size() + "' elements");
		
		if (decisions.size() > 0) {
			return RestResponse.getSuccessResponse(decisions);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}
	@GET
	@Path("/getGraphAsJsonById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGraphAsJsonById(@QueryParam("id") String id) {
		log.debug("GET decisions by id '" + id + "'");
		
		Decision decision = SampleObjectProvider.getDecisionById(id);
		Response response = null;
		if (decision != null) {
			GoJsFormatter f2 = new GoJsFormatter(decision);
			String result = f2.getGoJsString();
			System.out.println(result);
			response = Response.status(HttpCode.HTTP_200_OK.getCode()).entity(result).build();
			RestResponse.addResponseHeader(response);
			return response;
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}
}

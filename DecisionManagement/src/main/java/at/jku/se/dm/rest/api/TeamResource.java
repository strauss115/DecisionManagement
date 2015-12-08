package at.jku.se.dm.rest.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.pojos.Team;

@Path("/team")
public class TeamResource {
	
	private static final Logger log = LogManager.getLogger(TeamResource.class);

	// not needed so far
	// @Context
	// UriInfo ui;
	
	// ------------------------------------------------------------------------
	
	public TeamResource() {

	}
	
	// ------------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		log.debug("GET all teams");
		
		List<Team> users = SampleObjectProvider.getAllTeams();
		log.info("GET all teams returning '" + users.size() + "' elements");
		Response response = RestResponse.getSuccessResponse(users);
		response.getHeaders().add("Access-Control-Allow-Origin", "*");
		response.getHeaders().add("Access-Control-Allow-Headers",
				"origin, content-type, accept, authorization");
		response.getHeaders().add("Access-Control-Allow-Credentials",
				"true");
		response.getHeaders().add("Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS, HEAD");
		return response;
	}
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("name") String name) {
		log.debug("GET team '" + name + "'");
		
		Team t = SampleObjectProvider.getTeamByName(name);
		if (t != null) {
			log.info("GET user returning '" + t.getName() + "'");
			return RestResponse.getSuccessResponse(t);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@QueryParam("teamName") String name,
							 @QueryParam("adminUserId") String adminUserId) {
		log.debug("Create team '" + name + "'");
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@PUT
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(@PathParam("teamName") String name, String json) {
		log.debug("EDIT team '" + name + "': " + json);
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@DELETE
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("teamName") String name) {
		log.debug("DELETE team '" + name + "'");
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
}

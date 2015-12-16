package at.jku.se.rest.web.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import at.jku.se.dm.rest.pojos.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/team")
@Api(value = "team")
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
	@ApiOperation(value = "Gets all teams", response = Team.class, responseContainer = "List")
	public Response getAll() {
		log.debug("GET all teams");

		List<Team> users = SampleObjectProvider.getAllTeams();
		log.info("GET all teams returning '" + users.size() + "' elements");

		return RestResponse.getSuccessResponse(users);
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets team by name", response = Team.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No teams found with given name") })
	public Response get(@ApiParam(value = "Team name") @PathParam("name") String name) {
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
	@ApiOperation(value = "Creates a new team")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create team (see response message for further error details)") })
	public Response register(@ApiParam(value = "Team name") @QueryParam("teamName") String name,
			@ApiParam(value = "Administrator of team e-mail address") @QueryParam("adminUserEmail") String adminUserEmail) {
		log.debug("POST Create team '" + name + "'");

		try {
			User admin = SampleObjectProvider.getUserByEMail(adminUserEmail);
			SampleObjectProvider.addTeam(name, admin);
			log.debug("Created team successfully");
			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			log.debug("Failed to create new team '" + e + "'");
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	// @PUT
	// @Path("/{name}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response edit(@PathParam("teamName") String name, String json) {
	// log.debug("EDIT team '" + name + "': " + json);
	//
	// return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	// }

	@DELETE
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deletes a team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Team deleted successfully"),
			@ApiResponse(code = 500, message = "Unable to delete team") })
	public Response delete(@ApiParam(value = "Team name") @PathParam("name") String name) {
		log.debug("DELETE team '" + name + "'");

		try {
			Team t = SampleObjectProvider.getTeamByName(name);
			if (SampleObjectProvider.deleteTeam(t)) {
				log.debug("Deleted team successfully");
				return RestResponse.getSuccessResponse();
			} else
				throw new Exception("Could not delete team");
		} catch (Exception e) {
			log.debug("Failed to delete team '" + e + "'");
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

}

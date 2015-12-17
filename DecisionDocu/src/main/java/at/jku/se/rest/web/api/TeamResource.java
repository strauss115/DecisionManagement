package at.jku.se.rest.web.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import at.jku.se.rest.web.pojos.WebTeam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/team")
@Api(value = "team")
public class TeamResource {

	private static final Logger log = LogManager.getLogger(TeamResource.class);

	// ------------------------------------------------------------------------

	public TeamResource() {

	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all teams", response = WebTeam.class, responseContainer = "List")
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all teams");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
		//
		// List<WebTeam> users = SampleObjectProvider.getAllTeams();
		// log.info("GET all teams returning '" + users.size() + "' elements");
		//
		// return RestResponse.getSuccessResponse(users);
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets team by name", response = WebTeam.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No teams found with given name") })
	public Response get(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team name") @PathParam("name") String name) {
		log.debug("GET team '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// WebTeam t = SampleObjectProvider.getTeamByName(name);
		// if (t != null) {
		// log.info("GET user returning '" + t.getName() + "'");
		// return RestResponse.getSuccessResponse(t);
		// } else {
		// return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		// }
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new team")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create team (see response message for further error details)") })
	public Response register(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team name") @QueryParam("teamName") String name,
			@ApiParam(value = "Administrator of team e-mail address") @QueryParam("adminUserEmail") String adminUserEmail) {
		log.debug("POST Create team '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// WebUser admin = SampleObjectProvider.getUserByEMail(adminUserEmail);
		// SampleObjectProvider.addTeam(name, admin);
		// log.debug("Created team successfully");
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// log.debug("Failed to create new team '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
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
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team name") @PathParam("name") String name) {
		log.debug("DELETE team '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// WebTeam t = SampleObjectProvider.getTeamByName(name);
		// if (SampleObjectProvider.deleteTeam(t)) {
		// log.debug("Deleted team successfully");
		// return RestResponse.getSuccessResponse();
		// } else
		// throw new Exception("Could not delete team");
		// } catch (Exception e) {
		// log.debug("Failed to delete team '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

}

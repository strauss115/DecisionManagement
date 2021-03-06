package at.jku.se.rest.api;

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

import org.apache.logging.log4j.Logger;

import at.jku.se.model.DecisionGroup;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;

import org.apache.logging.log4j.LogManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API Class for WebGroup
 * @author August
 *
 */
@Path("/web/group")
@Api(value = "webGroup")
public class WebGroupResource {

	private static final Logger log = LogManager.getLogger(WebGroupResource.class);
	//private static Genson genson = new Genson();

	// ------------------------------------------------------------------------

	/**
	 * Default constructor
	 */
	public WebGroupResource() {

	}

	// ------------------------------------------------------------------------

	/**
	 * Gets all groups
	 * @param token
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all groups", response = DecisionGroup.class, responseContainer = "List")
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all groups");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// List<Group> groups = SampleObjectProvider.getAllGroups();
		// log.info("GET all groups returning '" + groups.size() + "'
		// elements");
		//
		// return RestResponse.getSuccessResponse(groups);
	}

	@GET
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets group by name", response = DecisionGroup.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No group found with given name") })
	public Response get(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Group name") @PathParam("groupName") String name) {
		log.debug("GET group '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// List<Group> groups = SampleObjectProvider.getAllGroups();
		//
		// Group g = SampleObjectProvider.getGroupByName(name);
		// if (g != null) {
		// log.info("GET group returning '" + g.getName() + "'");
		// return RestResponse.getSuccessResponse(g);
		// } else {
		// return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		// }
	}

	/**
	 * Create new group using JSON format
	 * @param token
	 * @param json
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create new group using JSON format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Group saved successfully"),
			@ApiResponse(code = 500, message = "Unable to save group") })
	public Response createJSON(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Group as JSON") String json) {
		log.debug("POST create group JSON: " + json);
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// Group g = genson.deserialize(json, Group.class);
		// SampleObjectProvider.addGroup(g);
		// log.debug("Created group successfully");
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// log.debug("Failed to create new group '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

	/**
	 * Creates a new group
	 * @param token
	 * @param name
	 * @param teamName
	 * @return
	 */
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new group")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Group saved successfully"),
			@ApiResponse(code = 500, message = "Unable to save group") })
	public Response create(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Name of group") @QueryParam("name") String name,
			@ApiParam(value = "Team name where group belongs to") @QueryParam("teamName") String teamName) {
		log.debug("POST create group '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// WebTeam t = SampleObjectProvider.getTeamByName(teamName);
		// SampleObjectProvider.addGroup(name, t);
		// log.debug("Created group successfully");
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// log.debug("Failed to create new group '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

	// @PUT
	// @Path("/{groupName}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response edit(@PathParam("groupName") String name, String json) {
	// log.debug("EDIT group '" + name + "': " + json);
	//
	// return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	// }

	/**
	 * Deletes a group
	 * @param token
	 * @param name
	 * @return
	 */
	@DELETE
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deletes a group")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Group deleted successfully"),
			@ApiResponse(code = 500, message = "Unable to delete group") })
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@PathParam("groupName") String name) {
		log.debug("DELETE group '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// Group g = SampleObjectProvider.getGroupByName(name);
		// if (SampleObjectProvider.deleteGroup(g)) {
		// log.debug("Deleted group successfully");
		// return RestResponse.getSuccessResponse();
		// }
		// else
		// throw new Exception("Could not delete group");
		// } catch (Exception e) {
		// log.debug("Failed to delete group '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

}

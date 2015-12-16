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

import org.apache.logging.log4j.Logger;

import com.owlike.genson.Genson;

import org.apache.logging.log4j.LogManager;

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.pojos.Group;
import at.jku.se.dm.rest.pojos.Team;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/group")
@Api(value = "group")
public class GroupResource {

	private static final Logger log = LogManager.getLogger(GroupResource.class);
	private static Genson genson = new Genson();

	// not needed so far
	// @Context
	// UriInfo ui;

	// ------------------------------------------------------------------------

	public GroupResource() {

	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all groups", response = Group.class, responseContainer = "List")
	public Response getAll() {
		log.debug("GET all groups");

		List<Group> groups = SampleObjectProvider.getAllGroups();
		log.info("GET all groups returning '" + groups.size() + "' elements");

		return RestResponse.getSuccessResponse(groups);
	}

	@GET
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets group by name", response = Group.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No group found with given name") })
	public Response get(@ApiParam(value = "Group name") @PathParam("groupName") String name) {
		log.debug("GET group '" + name + "'");

		Group g = SampleObjectProvider.getGroupByName(name);
		if (g != null) {
			log.info("GET group returning '" + g.getName() + "'");
			return RestResponse.getSuccessResponse(g);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create new group using JSON format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Group saved successfully"),
			@ApiResponse(code = 500, message = "Unable to save group") })
	public Response createJSON(@ApiParam(value = "Group as JSON") String json) {
		log.debug("POST create group JSON: " + json);

		try {
			Group g = genson.deserialize(json, Group.class);
			SampleObjectProvider.addGroup(g);
			log.debug("Created group successfully");
			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			log.debug("Failed to create new group '" + e + "'");
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new group")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Group saved successfully"),
			@ApiResponse(code = 500, message = "Unable to save group") })
	public Response create(@ApiParam(value = "Name of group") @QueryParam("name") String name,
			@ApiParam(value = "Team name where group belongs to") @QueryParam("teamName") String teamName) {
		log.debug("POST create group '" + name + "'");

		try {
			Team t = SampleObjectProvider.getTeamByName(teamName);
			SampleObjectProvider.addGroup(name, t);
			log.debug("Created group successfully");
			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			log.debug("Failed to create new group '" + e + "'");
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	// @PUT
	// @Path("/{groupName}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response edit(@PathParam("groupName") String name, String json) {
	// log.debug("EDIT group '" + name + "': " + json);
	//
	// return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	// }

	@DELETE
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deletes a group")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Group deleted successfully"),
			@ApiResponse(code = 500, message = "Unable to delete group") })
	public Response delete(@PathParam("groupName") String name) {
		log.debug("DELETE group '" + name + "'");
		
		try {
			Group g = SampleObjectProvider.getGroupByName(name);
			if (SampleObjectProvider.deleteGroup(g)) {
				log.debug("Deleted group successfully");
				return RestResponse.getSuccessResponse();
			}
			else 
				throw new Exception("Could not delete group");
		} catch (Exception e) {
			log.debug("Failed to delete group '" + e + "'");
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

}

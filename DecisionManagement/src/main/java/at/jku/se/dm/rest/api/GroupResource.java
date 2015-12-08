package at.jku.se.dm.rest.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.pojos.Group;

@Path("/group")
public class GroupResource {

	private static final Logger log = LogManager.getLogger(GroupResource.class);
	
	// not needed so far
	// @Context
	// UriInfo ui;
	
	// ------------------------------------------------------------------------
	
	public GroupResource() {
		
	}	

	// ------------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		log.debug("GET all groups");
		
		List<Group> groups = SampleObjectProvider.getAllGroups();
		log.info("GET all groups returning '" + groups.size() + "' elements");
		
		return RestResponse.getSuccessResponse(groups);
	}
	
	@GET
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("groupName") String name) {
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
	public Response create(String json) {
		log.debug("CREATE group: " + json);
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@PUT
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(@PathParam("groupName") String name, String json) {
		log.debug("EDIT group '" + name + "': " + json);
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@DELETE
	@Path("/{groupName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("groupName") String name) {
		log.debug("DELETE group '" + name + "'");
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
}

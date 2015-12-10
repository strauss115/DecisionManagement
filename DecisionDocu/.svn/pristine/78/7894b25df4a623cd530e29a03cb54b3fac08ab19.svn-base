package at.jku.se.rest.api;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.model.Relationship;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = { "relationship" })
@Path("/relationship")
public class RelationshipResource {
	
	private static final Logger log = LogManager.getLogger(RelationshipResource.class);
	
	@POST
	@Path("/{name}/{fromNode}/{toNode}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a relationship to an existing decision", response = Relationship.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response addRelationshipToDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token, 
			@ApiParam(value = "relationship name", required = true) @PathParam("name") String name, 
			@ApiParam(value = "ID of the first node", required = true) @PathParam("fromNode") long fromNode, 
			@ApiParam(value = "ID of the second noede", required = true) @PathParam("toNode") long toNode) {
		log.debug("adding relationship (" + name + ") from node '" + fromNode + "' to node '" + toNode + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@POST
	@Path("/{name}/{fromNode}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a relationship to a new decision", response = Relationship.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response addRelationshipToDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token, 
			@ApiParam(value = "relationship name", required = true) @PathParam("name") String name,
			@ApiParam(value = "ID of the first node", required = true) @PathParam("fromNode") long fromNode, 
			@ApiParam(value = "JSON representation of the second node", required = true) String json) {
		log.debug("adding relationship (" + name + ") from node '" + fromNode + "' to node '" + json + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}

}

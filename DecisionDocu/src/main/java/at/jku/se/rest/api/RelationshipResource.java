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

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.model.Decision;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Relationship;
import at.jku.se.model.User;
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
	
	private static ObjectMapper mapper = new ObjectMapper();
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
			log.debug("createRealtionship invoked");
			try {
				if(!SessionManager.verifySession(token)){
					return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
				}
				long relationid = DBService.addRelationship(fromNode, name, toNode);
				if(relationid<1){
					return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
				}
				return RestResponse.getSuccessResponse(relationid);
			} catch (Exception e) {
				log.debug("Error occured!", e);
				return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
			}
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
		log.debug("createRealtionship invoked");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			NodeInterface node = mapper.readValue(json, NodeInterface.class);
			node = DBService.createRelationshipWithNode(node, name, fromNode, user.getId());
			if(node==null||node.getId()<1){
				return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
			}
			return RestResponse.getSuccessResponse(node);
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

}

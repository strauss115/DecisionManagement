package at.jku.se.rest.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
import org.restlet.resource.Delete;
import org.restlet.resource.Post;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.Decision;
import at.jku.se.model.Node;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API Class of Node
 * @author August
 *
 */
@Api(tags = { "node" })
@Path("/node")
public class NodeResource {
	
	private static final Logger log = LogManager.getLogger(NodeResource.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * With this API method you can create a new node in the database
	 * @param token
	 * @param json
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new node", notes = "With this API method you can create a new node in the database", response = NodeInterface.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response updateSimpleNode(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token, 
			@ApiParam(value = "JSON Object to insert in the database", required = true) String json) {
		log.debug("createSimpleNode invoked");
		log.debug("Object to insert: " + json);
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			try{
				Map<String,LinkedHashMap> request = mapper.readValue(json, Map.class);
				json =mapper.writeValueAsString(request.get("node"));
			}catch (Exception e){
				e.printStackTrace();
			}
			NodeInterface node = mapper.readValue(json, NodeInterface.class);
			return RestResponse.getSuccessResponse(DBService.updateNode(node, user.getId()));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Delete a certain node
	 * @param token
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete a certain node")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response deleteDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the node to delete", required = true) @PathParam("id") long id) {
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			boolean deleted = DBService.deleteNode(id, user);//can only delete nodes where he is creator, admin can delete everything
			//boolean deleted = DBService.deleteNode(id);
			if(deleted){
				return RestResponse.getSuccessResponse();
			}
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Returns a single Node
	 * @param token
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a single Node", response = Decision.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getSimpleNode(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the node to fetch", required = true) @PathParam("id") long id) {
		log.info("Get Node '" + id + "' called");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}			
			return RestResponse.getSuccessResponse(DBService.getNodeByID(NodeInterface.class, id, 2));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	@GET
	@Path("/min/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a single minimum Node", response = Decision.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getMinNode(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the node to fetch", required = true) @PathParam("id") long id) {
		log.info("Get Node '" + id + "' called");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			return RestResponse.getSuccessResponse(DBService.getNodeByID(NodeInterface.class, id, 0));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	@GET
	@Path("/bytype/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns all minimum Nodes of spesific type", response = Decision.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getAllMinNodeOfType(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the node to fetch", required = true) @PathParam("type") String type) {
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			Class modelclass = Class.forName("at.jku.se.model."+type);
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(DBService.getAllNodesOfType(modelclass,0)));
			return RestResponse.getSuccessResponse(DBService.getAllNodesOfType(modelclass,0));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	@Delete
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{nodeid}/{relatednodeid}/{type}")
	@ApiOperation(value = "Deletes a Relationship of node", response = Decision.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response deleteRelOfNode(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the node to fetch", required = true) @PathParam("nodeid") long nodeid,
			@ApiParam(value = "ID of the node to delete Relationship between them", required = true) @PathParam("relatednodeid") long relatednodeid,
			@ApiParam(value = "Type of relationship to delete", required = true) @PathParam("type") String type){
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			if(nodeid<1||relatednodeid<1||type==null||type.length()<1){
				return RestResponse.getResponse(HttpCode.HTTP_400_BAD_REQUEST);
			}
			DBService.deleteRelationship(nodeid,relatednodeid,type);
			return RestResponse.getSuccessResponse("OK");
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Creates or deletes like relationships from user to node
	 * @param token
	 * @param id
	 * @param bool
	 * @return
	 */
	@GET
	@Path("/likes/{id}/{bool}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates or deletes like relationships from user to node")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response likesDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the Node to fetch", required = true) @PathParam("id") long id,
			@ApiParam(value = "Likes: 1, Unlike:0", required = true) @PathParam("bool") int bool) {
		log.debug("Set Like on decision'" + id + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			NodeInterface dec = DBService.getNodeByID(NodeInterface.class, id, 0);
			long rel = DBService.addRelationship(user.getId(), RelationString.LIKES, dec.getId());
			if(bool!=1){
				return RestResponse.getSuccessResponse(DBService.deleteNode(rel));
			}
			return RestResponse.getSuccessResponse(rel);
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

}

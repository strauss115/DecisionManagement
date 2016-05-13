package at.jku.se.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
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
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.Decision;
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
 * API Class for decision
 * @author August
 *
 */
@Api(tags = { "decision" })
@Path("/decision")
public class DecisionResource {
	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	private static ObjectMapper mapper = new ObjectMapper();
	// MasterToken: g0up9ej1egkmrtveig59ke0adf

	
	/**
	 * Get all decision for the user given by the token
	 * @param token
	 * @return A list of all available decisions
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a list of all available decisions", notes = "Returns a list of all available decisions", response = Decision.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getAllDecisions(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token
			) {
		log.info("Get all Decisions invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			return RestResponse.getSuccessResponse(DBService.getAllDecisions(user).toArray(new NodeInterface[0]));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Get all decision for the user given by the token
	 * @param token
	 * @return A list of all available decisions
	 */
	@GET
	@Path("/min")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a list of all available decisions", notes = "Returns a list of all available decisions", response = Decision.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getAllDecisionsForDashBoard(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token
			) {
		log.info("Get all Decisions for Dashboard invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			return RestResponse.getSuccessResponse(DBService.getAllDecisionsForDashboard(user).toArray(new NodeInterface[0]));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	/**
	 * Get the decision identified by the given id
	 * @param token The unique token of the user
	 * @param id
	 * @return A single decision
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a single decision", response = Decision.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the decision to fetch", required = true) @PathParam("id") long id) {
		log.info("Get Decision '" + id + "' called");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			return RestResponse.getSuccessResponse(DBService.getNodeByID(Decision.class, id, user, 2));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Returns a list of decisions that belong to a certain project
	 * @param token The unique token of the user
	 * @param id The Id of the project
	 * @return A list of decisions that belong to a certain project
	 */
	@GET
	@Path("/byProject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a list of decisions that belong to a certain project", response = Decision.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getByProjectName(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the project to fetch", required = true) @PathParam("id") long id) {
		log.debug("GET decisions by project name '" + id + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			return RestResponse.getSuccessResponse(DBService.getAllDecisionsOfProject(id, user).toArray(new NodeInterface[0]));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Insert or update a certain decision
	 * @param token
	 * @param json
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Insert or update a certain decision", response = Decision.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response updateDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision to insert/update as JSON", required = true) String json) {
		try {
			log.info("Test: "+json);
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			Decision dec = mapper.readValue(json, Decision.class);
			Decision newdec = DBService.updateNodeWihtRelationships(dec, user.getId());
			return RestResponse.getSuccessResponse(newdec);
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Delete a certain decision
	 * @param token
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete a certain decision")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response deleteDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the decision to delete", required = true) @PathParam("id") long id) {
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
}

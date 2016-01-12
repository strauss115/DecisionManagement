package at.jku.se.rest.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.model.Decision;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Project;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API Class for Project
 * @author August
 *
 */
@Api(tags = {"project"})
@Path("/project")
public class ProjectResource {

	private static final Logger log = LogManager.getLogger(ProjectResource.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * With this API method you can retreave all available project nodes from the database
	 * @param token
	 * @return Returns all available projects
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns all available projects", notes = "With this API method you can retreave all available project nodes from the database", response = Project.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getAll(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all projects");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			return RestResponse.getSuccessResponse(DBService.getAllProjects().toArray(new NodeInterface[0]));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * With this API method you can retreave all project memberships for a user
	 * @param token
	 * @return Returns all project memberships for a user
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns all project memberships for a user", notes = "With this API method you can retreave all project memberships for a user", response = Project.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response get(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET project for user with token='" + token + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			return RestResponse.getSuccessResponse(DBService.getAllProjectsOfUser(user).toArray(new NodeInterface[0]));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * With this API method you can retreave a single project
	 * @param token
	 * @param id
	 * @return Returns a single project
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a single project", notes = "With this API method you can retreave a single project", response = Project.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response get(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the project to fetch", required = true) @PathParam("id") long id) {
		log.debug("GET project with id='" + id + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			return RestResponse.getSuccessResponse(DBService.getNodeByID(Project.class, id, 2));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * With this API method you can add a single user to a project group
	 * @param token
	 * @param projectId
	 * @param projectPassword
	 * @return Adds a user to a project group
	 */
	@PUT
	@Path("/addUser")
	@ApiOperation(value = "Adds a user to a project group", notes = "With this API method you can add a single user to a project group", response = Project.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response addUserToProject(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the project", required = true) @QueryParam("projectId") long projectId,
			@ApiParam(value = "Password of the project group", required = true) @QueryParam("projectPassword") String projectPassword) {
		log.debug("Add User to Team");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			boolean added = DBService.addUserToProject(user,projectId,projectPassword);
			if(added){
				return RestResponse.getSuccessResponse();
			}
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * With this API method you can create a new project
	 * @param token
	 * @param json
	 * @return Creates a new project
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new project", notes = "With this API method you can create a new project", response = Project.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response create(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "JSON Representation of the project", required = true) String json) {
		log.debug("Create project '" + json + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			Project p = mapper.readValue(json, Project.class);
			return RestResponse.getSuccessResponse(DBService.updateNodeWihtRelationships(p, user.getId()));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
		
	/**
	 * With this API method you can delete a project
	 * @param token
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deletes a project", notes = "With this API method you can delete a project", response = Project.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response delete(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the project to delete", required = true) @PathParam("id") long id) {
		log.debug("DELETE project with id='" + id + "'");
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

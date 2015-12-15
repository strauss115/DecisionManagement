package at.jku.se.rest.api;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {"user"})
@Path("/user")
public class UserResource {
	
	private static final Logger log = LogManager.getLogger(UserResource.class);
	private static ObjectMapper mapper = new ObjectMapper();
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a list of all available users", notes = "Returns a list of all available users", response = User.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getAll(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all users");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			return RestResponse.getSuccessResponse(DBService.getAllUser().toArray(new NodeInterface[0]));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	@GET
	@Path("/{eMail}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a certain user", notes = "Returns a certain user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response get(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "EMail of the user to fetch", required = true) @PathParam("eMail") String eMail) {
		log.debug("GET user '" + eMail + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			return RestResponse.getSuccessResponse(DBService.getUserByEmail(eMail));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login a certain user", notes = "This API method creates an access key (token) for the user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response login(
			@ApiParam(value = "EMail of the user to login", required = true) @QueryParam("eMail") String eMail,
			@ApiParam(value = "Password of the user to login", required = true) @QueryParam("password") String password) {
		log.debug("Login user: " + eMail);

		if (eMail!=null && password!=null) {
			User u = DBService.getUserByEmail(eMail);
			if (u != null && u.getPassword().equals(password)) {
				log.debug("User found!");
				String token = SessionManager.addSession(u);
				try {
					OAuthResponse response = OAuthASResponse
					        .tokenResponse(HttpServletResponse.SC_OK)
					        .setAccessToken(token)
					        .setExpiresIn("3600")
					        .buildJSONMessage();
					return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
				} catch (OAuthSystemException e) {
					log.error("login error occured", e);
					RestResponse.getErrorResponse();
				}
			}	
		}
		return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Register a new user", notes = "This API method creates a new user on the server", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response register(
			@ApiParam(value = "First name of the user", required = true) @QueryParam("firstName") String firstName,
			@ApiParam(value = "Last name of the user", required = true) @QueryParam("lastName") String lastName,
			@ApiParam(value = "Password of the user", required = true) @QueryParam("password") String password,
			@ApiParam(value = "EMail of the user", required = true) @QueryParam("eMail") String eMail) {
		log.debug("Register '" + eMail + "'");

        log.debug("firstName: " + firstName);
        log.debug("lastName: " + lastName);
        log.debug("password: " + password);
        log.debug("eMail: " + eMail);

        User user = new User();
        user.setAdmin(false);
        user.setEmail(eMail);
        user.setName(firstName);
        user.setLastname(lastName);
        user.setPassword(password);

        try {
        	User existinguser = null;
        	try{
        		existinguser = DBService.getUserByEmail(eMail);
        	}catch (Exception e){
        		log.debug("Error occured!", e);
        	}
        	if(existinguser!=null){
        		return RestResponse.getResponse(HttpCode.HTTP_400_BAD_REQUEST);
        	}
        	log.debug(user.getPassword());
        	user = DBService.updateNode(user, -1);
        	
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}

        log.debug(user.toString());

        URI uri = null;
        try {
            uri = new URI("/DecisionDocu/api/user/" + user.getId());
        } catch (URISyntaxException e) {
        	log.debug("Error occured!", e);
        }
        String json = RestResponse.packData(user);
        return Response.created(uri).entity(json).build();
	}

	@PUT
	@ApiOperation(value = "Edit a user", notes = "This API method edits an existing user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response edit(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "JSON Object of the user", required = true) String json) {
		log.debug("EDIT user : " + json);
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			User u = mapper.readValue(json, User.class);
			return RestResponse.getSuccessResponse(DBService.updateNodeWihtRelationships(u, user.getId()));
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete a user", notes = "This API method deletes an existing user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the user", required = true) @PathParam("id") long id) {
		log.debug("DELETE user '" + id + "'");
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

	@PUT
	@Path("/{id}/permission")
	@ApiOperation(value = "Change the user's permission", notes = "This API method can change a users permission from user to admin or vice versa", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPermission(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the user", required = true) @PathParam("eMail") long id,
			@QueryParam("isAdmin") boolean isAdmin) {
		log.debug("Set admin user '" + id + "' to '" + isAdmin + "'");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			if(!user.isAdmin()){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User updateUser = DBService.getUserByEmail(user.getEmail());
			if(updateUser==null){
				return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
			}
			user.setAdmin(isAdmin);
			return RestResponse.getSuccessResponse(DBService.updateNode(user, -1));
			
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
}

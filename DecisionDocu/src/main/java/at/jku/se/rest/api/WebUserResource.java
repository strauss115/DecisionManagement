package at.jku.se.rest.api;

import java.util.LinkedList;
import java.util.List;

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

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import at.jku.se.rest.web.pojos.WebUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/web/user")
@Api(value = "webUser")
public class WebUserResource {

	private static final Logger log = LogManager.getLogger(WebUserResource.class);
	private static UserResource api = new UserResource();

	// ------------------------------------------------------------------------

	public WebUserResource() {

	}

	// ------------------------------------------------------------------------

	/**
	 * Converts a generic user to web API user format
	 */
	public static WebUser convertUser(User user) {
		try {
			if (user != null) {
				WebUser result = new WebUser();

				result.setId(String.valueOf(user.getId()));
				result.setEMail(user.getEmail());
				result.setFirstName(user.getName());
				result.setLastName(user.getLastname());
				result.setAdmin(user.isAdmin());
				// TODO add teams - database implementation missing

				return result;
			} else {
				log.error("Unable to convert user because of null reference");
				return new WebUser();
			}
		} catch (Exception e) {
			log.error("Unexpetected exception when converting user '" + user.getEmail() + "':" + e.getMessage());
			return new WebUser();
		}
	}

	/**
	 * Converts a list of generic user to web API user format
	 */
	public static List<WebUser> convertUser(List<User> users) {
		List<WebUser> webUsers = new LinkedList<WebUser>();

		for (User u : users) {
			if (u != null)
				webUsers.add(convertUser(u));
		}
		// --
		return webUsers;
	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all users", response = WebUser.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized") })
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all users");

		if (!SessionManager.verifySession(token)) {
			log.info("Unauthorized access");
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		List<WebUser> users = convertUser(DBService.getAllUser());

		log.info("GET all users returning '" + users.size() + "' elements");
		return RestResponse.getSuccessResponse(users);
	}

	@GET
	@Path("/{mail}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets user by e-mail address", response = WebUser.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No user found with given e-mail address") })
	public Response get(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "E-mail address of user to get") @PathParam("mail") String eMail) {
		log.debug("GET user '" + eMail + "'");

		if (!SessionManager.verifySession(token)) {
			log.info("Unauthorized access");
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		WebUser u = convertUser(DBService.getUserByEmail(eMail));
		if (u != null) {
			log.info("GET user returning '" + u.getEMail() + "'");
			return RestResponse.getSuccessResponse(u);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login a user and get token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Login successfully"),
			@ApiResponse(code = 401, message = "Login not successfull, please check username and password") })
	public Response login(
			@ApiParam(value = "E-mail address of user", required = true) @QueryParam("eMail") String eMail,
			@ApiParam(value = "Password for login", required = true) @QueryParam("password") String password) {
		log.debug("Login user: " + eMail);
		return api.login(eMail, password);
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Registers a new user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "User registrated successfully"),
			@ApiResponse(code = 500, message = "Server Error"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response register(@ApiParam(value = "First name") @QueryParam("firstName") String firstName,
			@ApiParam(value = "Last name") @QueryParam("lastName") String lastName,
			@ApiParam(value = "Password") @QueryParam("password") String password,
			@ApiParam(value = "E-mail address") @QueryParam("eMail") String eMail) {
		log.debug("POST register user '" + eMail + "'");
		return api.register(firstName, lastName, password, eMail);
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete a user", notes = "This API method deletes an existing user")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@PathParam("id") long id) {
		log.debug("DELETE user '" + id + "'");
		return api.delete(token, id);
	}

	@PUT
	@Path("/{mail}/setPermisssion/{admin}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Not yet implemented")
	public Response setPermission(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@PathParam("id") long id, @PathParam("admin") boolean admin) {
		log.debug("Set admin user '" + id + "' to '" + admin + "'");
		return api.setPermission(token, id, admin);
	}

	// TODO Upload user picture

}

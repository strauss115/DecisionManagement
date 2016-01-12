package at.jku.se.rest.api;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
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

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.Node;

import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import at.jku.se.rest.web.pojos.WebUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API Class for WebUser
 * @author August
 *
 */
@Path("/web/user")
@Api(value = "webUser")
public class WebUserResource {

	private static final Logger log = LogManager.getLogger(WebUserResource.class);
	private static UserResource api = new UserResource();

	// ------------------------------------------------------------------------

	/**
	 * Default constructor
	 */
	public WebUserResource() {

	}

	// ------------------------------------------------------------------------

	/**
	 * Converts a generic user to web API user format
	 * @param user
	 * @return {@link WebUser}
	 */
	public static WebUser convertUser(User user) {
		try {
			if (user != null) {
				WebUser result = new WebUser();
				// --
				result.setId(String.valueOf(user.getId()));
				result.setEMail(user.getEmail());
				result.setFirstName(user.getName());
				result.setLastName(user.getLastname());
				result.setAdmin(user.isAdmin());
				result.setTeams(Node.getListOfIds(DBService.getAllProjectsOfUser(user)));
				
				User u1 = DBService.getNodeByID(User.class, user.getId(), 2);
				
				result.setProfilePicture(String.valueOf(u1.getRelationships().get(RelationString.HAS_PICTURE).get(0).getRelatedNode().getId()));
				// --
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
	 * @param users
	 * @return
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

	/**
	 * Gets all users
	 * @param token
	 * @return {@link Response}
	 */
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

	/**
	 * Gets user by e-mail address
	 * @param token
	 * @param eMail
	 * @return {@link Response}
	 */
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

	/**
	 * Login a user and get token
	 * @param eMail
	 * @param password
	 * @return {@link Response}
	 */
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

	/**
	 * Registers a new user
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param eMail
	 * @return {@link Response}
	 */
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

	/**
	 * Delete a user
	 * Only an administrator or creator (user itself) is able to execute this operation
	 * @param token
	 * @param id
	 * @return {@link Response}
	 */
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete a user", notes = "Only an administrator or creator (user itself) is able to execute this operation")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@PathParam("id") long id) {
		log.debug("DELETE user '" + id + "'");
		return api.delete(token, id);
	}

	/**
	 * Change permissions of a user
	 * Only an administrator can execute this operation
	 * @param token
	 * @param id
	 * @param admin
	 * @return {@link Response}
	 */
	@POST
	@Path("/{id}/setPermisssion")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Change permissions of a user", notes = "Only an administrator can execute this operation")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 204, message = "User not found"),
			@ApiResponse(code = 200, message = "Ok") })
	public Response setPermission(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "User id to set permissions", required = true) @PathParam("id") long id,
			@PathParam("admin") boolean admin) {
		log.debug("Set admin user '" + id + "' to '" + admin + "'");
		return api.setPermission(token, id, admin);
	}

	/**
	 * Changes password of user
	 * An administrator is able to change the password of another user
	 * @param token
	 * @param userId
	 * @param password
	 * @return {@link Response}
	 */
	@POST
	@Path("/{id}/setPassword")
	@ApiOperation(value = "Changes password of user", notes = "An administrator is able to change the password of another user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Password successfully changed"),
			@ApiResponse(code = 204, message = "User or found"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "Server Error") })
	public Response changePassword(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Id of user, use 0 to change current user's password", required = true) @PathParam("id") long userId,
			@ApiParam(value = "New password", required = true) @QueryParam("password") String password) {
		log.debug("POST setPassword for user '" + userId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User sessionUser = SessionManager.getUser(token);

			if (userId == 0) {
				log.debug("Changing user's own password");
				sessionUser.setPassword(password);
				return RestResponse.getSuccessResponse();
			} else {
				User paramUser = DBService.getNodeByID(User.class, userId, 1);
				if (paramUser != null) {
					if (sessionUser.isAdmin() || paramUser.getId() == sessionUser.getId()) {
						log.debug("User is admin or changes own password by given id");
						paramUser.setPassword(password);
						return RestResponse.getSuccessResponse();
					} else {
						log.warn("User is not allowed to change password");
						return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
					}
				} else {
					log.warn("User '" + userId + "' not found");
					return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
				}
			}
		} catch (Exception e) {
			log.debug("Unable to change user password", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	/**
	 * Changes email of user
	 * An administrator is able to change the email of another user
	 * @param token
	 * @param userId
	 * @param email
	 * @return {@link Response}
	 */
	@POST
	@Path("/{id}/setEmail")
	@ApiOperation(value = "Changes email of user", notes = "An administrator is able to change the email of another user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Password successfully changed"),
			@ApiResponse(code = 204, message = "User or found"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "Server Error") })
	public Response changeEmail(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Id of user, use 0 to change current user's email", required = true) @PathParam("id") long userId,
			@ApiParam(value = "New email", required = true) @QueryParam("email") String email) {
		log.debug("POST setEmail for user '" + userId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User sessionUser = SessionManager.getUser(token);

			if (userId == 0) {
				log.debug("Changing user's own email");
				sessionUser.setEmail(email);
				return RestResponse.getSuccessResponse();
			} else {
				User paramUser = DBService.getNodeByID(User.class, userId, 1);
				if (paramUser != null) {
					if (sessionUser.isAdmin() || paramUser.getId() == sessionUser.getId()) {
						log.debug("User is admin or changes own email by given id");
						paramUser.setEmail(email);
						return RestResponse.getSuccessResponse();
					} else {
						log.warn("User is not allowed to change email");
						return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
					}
				} else {
					log.warn("User '" + userId + "' not found");
					return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
				}
			}
		} catch (Exception e) {
			log.debug("Unable to change user email", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	/**
	 * Changes first name of user
	 * An administrator is able to change the first name of another user
	 * @param token
	 * @param userId
	 * @param firstName
	 * @return {@link Response}
	 */
	@POST
	@Path("/{id}/setFirstName")
	@ApiOperation(value = "Changes first name of user", notes = "An administrator is able to change the first name of another user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "First name successfully changed"),
			@ApiResponse(code = 204, message = "User or found"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "Server Error") })
	public Response changeFirstName(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Id of user, use 0 to change current user's first name", required = true) @PathParam("id") long userId,
			@ApiParam(value = "New first name", required = true) @QueryParam("firstName") String firstName) {
		log.debug("POST setFirstName '" + firstName + "' for user '" + userId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User sessionUser = SessionManager.getUser(token);

			if (userId == 0) {
				log.debug("Changing user's own first name");
				sessionUser.setFirstName(firstName);
				return RestResponse.getSuccessResponse();
			} else {
				User paramUser = DBService.getNodeByID(User.class, userId, 1);
				if (paramUser != null) {
					if (sessionUser.isAdmin() || paramUser.getId() == sessionUser.getId()) {
						log.debug("User is admin or changes own first name by given id");
						paramUser.setFirstName(firstName);
						return RestResponse.getSuccessResponse();
					} else {
						log.warn("User is not allowed to change first name");
						return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
					}
				} else {
					log.warn("User '" + userId + "' not found");
					return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
				}
			}
		} catch (Exception e) {
			log.debug("Unable to change user's first name", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	/**
	 * Changes last name of user
	 * An administrator is able to change the last name of another user
	 * @param token
	 * @param userId
	 * @param lastName
	 * @return {@link Response}
	 */
	@POST
	@Path("/{id}/setLastName")
	@ApiOperation(value = "Changes last name of user", notes = "An administrator is able to change the last name of another user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Last name successfully changed"),
			@ApiResponse(code = 204, message = "User or found"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "Server Error") })
	public Response changeLastName(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Id of user, use 0 to change current user's last name", required = true) @PathParam("id") long userId,
			@ApiParam(value = "New last name", required = true) @QueryParam("lastName") String lastName) {
		log.debug("POST setLastName '" + lastName + "' for user '" + userId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User sessionUser = SessionManager.getUser(token);

			if (userId == 0) {
				log.debug("Changing user's own last name");
				sessionUser.setLastname(lastName);
				return RestResponse.getSuccessResponse();
			} else {
				User paramUser = DBService.getNodeByID(User.class, userId, 1);
				if (paramUser != null) {
					if (sessionUser.isAdmin() || paramUser.getId() == sessionUser.getId()) {
						log.debug("User is admin or changes own last name by given id");
						paramUser.setLastname(lastName);
						return RestResponse.getSuccessResponse();
					} else {
						log.warn("User is not allowed to change last name");
						return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
					}
				} else {
					log.warn("User '" + userId + "' not found");
					return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
				}
			}
		} catch (Exception e) {
			log.debug("Unable to change user's last name", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
}

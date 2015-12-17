package at.jku.se.rest.web.api;

import java.util.List;

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

import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import at.jku.se.rest.web.pojos.WebUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/user")
@Api(value = "user")
public class UserResource {

	private static final Logger log = LogManager.getLogger(UserResource.class);

	// ------------------------------------------------------------------------

	public UserResource() {

	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all users", response = WebUser.class, responseContainer = "List")
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all users");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// List<WebUser> users = SampleObjectProvider.getAllUsers();
		// log.info("GET all users returning '" + users.size() + "' elements");
		// return RestResponse.getSuccessResponse(users);
	}

	@GET
	@Path("/{mail}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets user by e-mail address", response = WebUser.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No user found with given e-mail address") })
	public Response get(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "E-mail address of user to get") @PathParam("mail") String eMail) {
		log.debug("GET user '" + eMail + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// WebUser u = SampleObjectProvider.getUserByEMail(eMail);
		// if (u != null) {
		// log.info("GET user returning '" + u.getEMail() + "'");
		// return RestResponse.getSuccessResponse(u);
		// } else {
		// return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		// }
	}

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login a user and get token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Login successfully"),
			@ApiResponse(code = 401, message = "Login not successfull, please check username and password") })
	public Response login(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam("E-mail address of user") @QueryParam("eMail") String eMail,
			@ApiParam("Password for login") @QueryParam("password") String password) {
		log.debug("Login user: " + eMail);
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// List<WebUser> users = SampleObjectProvider.getAllUsers();
		// for (WebUser u : users) {
		// if (u.getEMail().equals(eMail) && u.getPassword().equals(password)) {
		// log.debug("Login of user '" + u.getEMail() + "' successful");
		// Token token = new Token(SessionManager.addSession(u));
		// log.info("Returing session '" + token.getSession() + "'");
		// return RestResponse.getSuccessResponse(token);
		// }
		// }
		// log.info("Unable to authorize user '" + eMail + "'");
		// return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Registers a new user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User saved successfully"),
			@ApiResponse(code = 500, message = "Unable to register user") })
	public Response register(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "First name") @QueryParam("firstName") String firstName,
			@ApiParam(value = "Last name") @QueryParam("lastName") String lastName,
			@ApiParam(value = "Password") @QueryParam("password") String password,
			@ApiParam(value = "E-mail address") @QueryParam("eMail") String eMail) {
		log.debug("POST register user '" + eMail + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// SampleObjectProvider.addUser(eMail, firstName, lastName, password);
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// log.debug("Failed to register new user '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

	// @PUT
	// @Path("/{mail}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response edit(@PathParam("eMail") String mail, String json) {
	// log.debug("EDIT user '" + mail + "': " + json);
	//
	// return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	// }

	@DELETE
	@Path("/{mail}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Not yet implemented")
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@PathParam("eMail") String mail) {
		log.debug("DELETE user '" + mail + "'");

		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}

	@PUT
	@Path("/{mail}/setPermisssion/{admin}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Not yet implemented")
	public Response setPermission(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@PathParam("mail") String mail, @PathParam("admin") boolean admin) {
		log.debug("Set admin user '" + mail + "' to '" + admin + "'");

		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}

	// TODO Upload user picture

}

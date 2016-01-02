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
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.model.Node;
import at.jku.se.model.Project;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import at.jku.se.rest.web.pojos.WebTeam;
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
				// --
				result.setId(String.valueOf(user.getId()));
				result.setEMail(user.getEmail());
				result.setFirstName(user.getName());
				result.setLastName(user.getLastname());
				result.setAdmin(user.isAdmin());
				result.setTeams(Node.getListOfIds(DBService.getAllProjectsOfUser(user)));
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

	@PUT
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

	@PUT
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

	@PUT
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

	@PUT
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

	@PUT
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

	@GET
	@Path("getFile/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets userpic by id", response = WebUser.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No user found with given id") })
	public Response getFile(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Node id") @PathParam("id") long id) {
		
		/* type: MIME-Typ
		 * data: Base64-codierte Datei
		 */
		
		String json = "{\"type\": \"image/jpeg\",\"data\": \"/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABBAAD/7gAOQWRvYmUAZMAAAAAB/9sAhAAFBAQEBAQFBAQFBwUEBQcJBwUFBwkKCAgJCAgKDQoLCwsLCg0MDAwNDAwMDw8REQ8PFxYWFhcZGRkZGRkZGRkZAQYGBgoJChQNDRQWEQ4RFhkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRn/wAARCAMgAlgDAREAAhEBAxEB/8QAgwABAQEBAQEBAQEAAAAAAAAAAAECBgUEAwcIAQEBAQAAAAAAAAAAAAAAAAAAAQIQAAIBAwMCBAQEBAQEBgIDAAABESExAkEDBFFhcYESBZEiMgahsUITwVIjFNFicjPhQyQ08IJTFSU18aJzVBYRAQEBAAAAAAAAAAAAAAAAAAARAf/aAAwDAQACEQMRAD8A/wBTGkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABVtUA6XoBPVgr5IDGW/s43zxXmBP7rjf+rj8UAXJ47tu4t+KA2tzB2yTA1KdgLDAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABMapeIEzzw28fVnl6MdXnQDz9/3v27Zp+5+41piB5u99z/+hswtMmIPg3ff/cd2fS8cMe1xB8mfuPO3Pq5DfZCD8ct3eyc5bub/APM0BlvN/qb8cmUT5uuXxAvzdcvi0BVnuL9ea8MmB+mHK5eEejeyXSWyQfVt+8+4bT/3fV2Yg+3a+5eTjTf2lmuqEHobH3Fwt2m8ntsQens8rjb6na3Mc+0w0B+z/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABQBXUB1f6VfLQD4eX7twuGn+5uLPJfpxq2B4fJ+4+TnOPFxWzi7ZOuceNhB5G9yN/kZPLe3cs2+rEH5QUAAFl9QIAAAALIEhAWX1AgC4FxeWD9WDeOS1TA9Hj+9+4cZpPc/dw6Z1JB7fF+4uJv/AC76exnaXVfgB6+3uY7uKy22s07NMg1Xz1QAoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALF1rqB5vN944nD+X1fubq/Sqgc3y/eeZzJxT/AGtvoijztW9XdusgP/FQAAAAAAAAAAAAAAAAAArUBCajTvcD9tjl8ni5LLY3MlH6XYDoeF9xbe5G3zMfTk75qwg93DPDcwW5t5LPbdslVEGrXAaToAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADykD8eTy9jiYPc3s1j0x1YHL8/wB+3+Unt8adrjukr6skUeR3m/Wr8wIAAAAAAAAAAAAAAAAAAAAAAAAAPq4nP5XCz9WxuP064uz7QSDqeB7zxubisc3+zv67b1A9NpKF110AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFh21dkQeT7l7zs8JZbW1/U5Hay8Sjk+Ryd/l5vd38nk9E7IQfnVOU4fUonhSbxqAAAAAAAAAAAAAAAAAAAAAAAAAADvr1AJtNZKmStn0A972z37Laa4/Nfrw/TuEg6fDPDcwW5hkstt2zVgLZwwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADcYzp1IOe9298eKy43DabdM9xfwKObbbbbct3y1ZRPxAV1uAAAAAAAAAAAAAAAAAAAAAAAAAAAAAtX8OoDSHrZaAeh7d7pve35rFv18Z/Vg9PAg7Hj8na5O0t3ZyWWD+K8QP2hkEKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABe9kBG0l6n8uKq8noQcx7v7zluvLj8V/wBJU3M1r4FHg+c9ygAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADStZuB9fA5+/7furPaf8AT/Xi7QB2nE5ezzNlb2y564a4vuTcH7gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADvpqQcx737u93LLh8fJ+jGm5mrPsijwFS1PAoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWcoBpDqB9fA527wOQt3CXtv68dH4gdrx+TtcrZx3tp/Ln/wDr4kH7EAoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAmfMDwffPdf2cXxONl/WypuZLRMDl4Vk36bqbzqUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACWgFF1qB6HtXuWXA3vTlL4uf149H1Jo7XDPHc28dzDJZYZfTkreBBSgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFNbEHwe6+4Lg8fLJR+9u0wx6FHE5Z5bmTzzfqzydWyiAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAamgHvew+5vb3P7Pfy+TP6G9IJo6il7rqAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGk+XmBnd3MNrDLczcY7anJvqBwvP5mfO5We83/TTjBFHzeUAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFqmmnDVU1eQOy9m9wXO4ywyhbuzTPuQemAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJivTTuBzf3Fz2muBtOYrutadmBzsJUdUrIoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKTXyYH1cDmZ8Lk4by+hOM8epB3WGeO5hjuYNZYZqcclZoDQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD8eXyceJx9zkZ/oxfpXVgcFu7me9m97cc55ttlGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB1H25zf3NvPiZOctmu3Oq1QHvEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADp3sBzP3JyvXu7fEwyphXNdQOfVF4soAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD9+HyMuLytrexfp9Dr/pA73b3Md3bx3MPpzUrzINAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA8AMb26tnbz3snC28WBwG9u5b+9nv5OXm5T7aFH59tAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANSmu0AdZ9v8p7/Fezk/n2XCWsEHtdO9gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAoHifcXK/Z4a2cH/U3Mofghg5MoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9L2TkPY5+GDcbe98r8WTR2kzLWtMfIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADA473/kfve4PBOcdrH0td0MHlFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFTyxyxzwcZYtNMD+gcbdx5Gxs72Fs8U1+RB+oAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAmeS28Ms3bHFv8AP57vbmW9vbu9l9WeTZRgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABVVA677c3nucF7TddrL04+FyD2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABePGoHw+7737Pt+/knGWa9OIHDqkLtLKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD3ftrd9PJ3dpumeK9K7yTR1IAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA6wB4f3LuenibW1/NnP4AcqUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHhcD0PZdxbXuWzk7MDtiAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWIOa+5853Njb6KWUc8UAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH7cXL0cnYy6ZL8wP6Deqs1K8CCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHRdSDk/uTKebjj0xNDxQAAAAAAAAAAAAAAAAAAAAAAAAAkBIAAAAAAAAAAAAAAAAAAAAAAAAA1tuNzby6ZL8wP6FtOdrby64kGgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFIOO+4f/ALLJdMaGh5IAAAAAAAAAAAAAAAAAAAAAAABJAAAKAAAAAAAAAAAAAAAAAAAAAAAALPGNGgP6Dx/+22f9GP4kH6gAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBx33D/9nl/pNDygAAAAAAAAAAAAAAAAAAAAAAACAAAFAAAAAAAAAAAAAAAAAAAAAAAAGq8UB/QeN/22x/8Ax4kH6gAAAAAAAAAAAAAAAAAAAAAAAAAAAAADsQch9xKPcW+uKNDyAAAAAAAAAAAAAAAAAAAAAAAEAOgBANK3AQAVwKAAAAAAAAAAAAAAAAAAAACgEpyS7r8wP6FsKNjax6beJBsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA6KPEg5T7kxjlbeWuSq/M0PEAAAAAAAAAAAAAAAAAAAAAAICMAAAAAL4AAAAAAAAAAAAAAAAAAAAAAAN7Knd28Xrmp+IH9BwXp28VqlHkQaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUg5z7nwX9Dc6Jr8SjmygAAAAAAAAAAAAAAAAAADAgBgAAAAAAJAUBIAAAAAAAAAAAAAAAAAAAAPp4G3+5zdnDX1KnhUDvneliCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH+KA8j7i2v3OAs9cM5fgByBQAAAAAAAAAAAAAAAAAAEASAAAJQAABAKA0AICgAAAAAAAAAAAAAAAAACgep7Bt/ue5YZRTbU5fkTR2IAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA+fn7P9xwt/ZiuWDjyqBwN1PS5QAAAAAAAAAAAAAAAAAABgSQAEuA8QAFAgFkBICQKAAAAAAAAAAAAAAAAAAH8KgdH9sbP/c77qnGGPjcg6MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAoEdU11p8QOC9w2P7fm8jZiMVm3j/p0KPmAAAAAAAAAAAAAAAAAIAAAAI1qAAQA7gAKAVQEAALYAAAAAAAAAAAAAAAAAeNtQO19k2P2PbtqVGW5878bEHogAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABy/3Lx/Tv7fJxXy5r0ZPvjUYPBKAAAAAAAAAAAAAAACQIAAAAIAlWAjo4AqaQCZAoCgAAAkCgAAAAAAAAAAAAAAAP14+zlyeRtbGP/MySfgB/QMMVjjjhioWKSjwRkUoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADz/d+L/dcHPbS+bD58XqoqwOIViigAAAAAAAAAAAAAjYAAAAASwABCAkxcC3uAogJcCgUAAAAUAAAAAAAAAAAAADqB7n25xf3eTlysl8u2ox8WTR1SmI1d2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1KadU6ZeDA4X3PjPiczc24+RucPBgfGUAAAAAAAAAAAAbAgAABAAACAWaATx8gE9QEgUAgEgUAAAAVAAAAAAAAAAAAAq3Cu1HmB2/tPF/teDt4ZKNzL5s/OxB99/wCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALgAPF+4eH+9x8eTt4/1NmmX+lgcmotovpZQAAAAAAAAAADYEkA4YEmAF7gW3gBJAkgKKoC6Aqood9AJDQCjQDSX5AJmuoFQFAkgUBICQKgAAAAAAAAAAB6Ps3CfN5mCyX9Hb+fN+FgO1TlTbouyIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADLHHcxe3lXHNNOQOE9x4efC5We1l/ty3tvqgPkKAAAAAAAABgTQAgEAOwEhgGA0AaASAJZAALZ1ALWEAAtAH4AUBYAqgACAoAAAAAAAAAArZKc3RIDtfZ+F/Z8TFZf7m8vVuPVdiD0QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADtpqB5nvXA/vON68F/W2VOPdAcZWsqGnDWqKAAAAAAAABgQCVnsBZAlAEtgKeYCAItQDtS4CKASV08AFwKA0kAuugCuoFAT1AWAXcgVAUAAAAAAAAA0A9j2Lgf3PJ/udxf0sLTqB117638CBcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAfiByXvvtv9vu5crZX9He+vsxg8aZKAAAAANgEAYEAATUAwIAAtQMw3MAVdNQIgJ5gVWAqAkNXYFqkBagQABWAgCgAKAAAAAAAB+vH4+5yt7HZ21Lydey1A7vi8XDh7GPHwhrFKX3IP2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD89/Y29/Zz2N1J7OSp4gcPz+FucDfy2dz6J/p5dUyj5ezuAAAADAgAABLALgHDAgCwFAnUBOgE8AI/ACoCpqsATuwLddALUCAALICQHmBQHmAlgVAAAABDcYpS8nEK8gdl7P7b/ZbP7m7/ANzuQ8uy0gg9RvogAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP8AxAHx+4cHb5+w9vJfPgp28u/cDid7Y3eNu5bO7i1mnR6PwA/PvoUAAEYACAADAIBTzAjoAVpAXAjfTQAnrABVAfwAXAVrOoBXjRXAutAE9AAFAUAAAAACgEBQADSfwA6T2L2r0xzeTj8y+jB/xIOi/ECgQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC/buB53untmHuOHqx+XfxXyPqBxu7tbmzuZbW7j6c8bYlGGBEAAAAIBAACAEAACAlgFUwEgG4juA1AOoCIAtwFgEAKgPEABoAAAAACAr72A972b2d72S5nKxjDH6NvqTR1HhRaIB+YAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/8ACA873T2vb9x23kmseVhbcVJ7Acdv7G5x83tbuLwyVG3ZlH5dugAAAAjAgFQE1YE7gVgErsCR8QFQEoBDAAPAABUmA8QD7AJoAkABQFwKAAAH2tp1YHQe0eyPcePK5S9OC+jaf8SDp0lbGmONgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAoGXD/wAAPk5/t/H9w2vTvJY5r6M/8QOO5vA5HB3PRvY/I/o3FZlHydlfoAVgEgAIAroBPECqwBTUDLtAABIC4FaqqgRqtACjS4Fl6gSoFqBQJfxAqQAABoBrSvcCAawwz3M1t7eLz3HbFAdR7V7HhsxyeXist39OGiIPdcT/AIEF8LFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJAmoDwt+pMD897Z2uRtvDfXrwemqA5X3L2Pd43q3eMv3Nh1eP6kKPGVKNQ1oUEA8gDAgDxAWUgNJAgCgEAAAACAKAAJgOyAuoBALAUBcAmlLsgPq4Xt/J52Sx2cYwf1Z6IDrvb/a+NwMIxXq3f1Zv+BKPuolNX0Av5gJAqAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAASAuAAnbR3A8zn+y8bmzlgv2951lUTYHLcz2/lcLJrexbxVtzFUKPjh3mP83UA04mI7AJcAPEC9OgEajwAnjcBK89QErzAAOoCJQCwCQF7XAOfMDUAGAQCrfYA03QDe1tbu9ksNnB7mX8uNQOh4H266bnOyrdbZB0O3ht7OC29rD0YrRKANoCSBUwAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAARsAAAkgRgUBAErr5EEePrTxzxWSemVSjxuZ9u8ffeWfGf7W7rh+lijn+V7by+G3+7tv0aZJNplHxgPECR6vpqlfsAcPWgCZq7gHRygMyBfEBSoBMBQAnKYFUKmtwCrXUA5dgEuLSBfzA/Ta2N3fy9Gzhlnm9Ev4ko9vh/bmeaWfLz9Cf8AylfzA6Dj8Pj8TH08bbxw/wA6Xz/ED9k+t+ruBa+XUCvtUAwHgBQABAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAgAABNQEAQB4AL616AW9nYDM1lzIEbblZTkujUog+Dk+0cHkz6tv9vN/q27Fo8fkfbe/jOXG3FmtMcrijyd7gc3Y/wBzZyhXeNij5nkl8rdegFpAESrIES10kA2BafECSkwK6gNKgWEBPUsbgfpt7O9vf7W3lu/kB6XH9h529GWeOO1h3dQPY4/29xNmMt7LLeeuOhKPV2trb2MY2cMdtf5asD9K61fXUC1vMMA4arcApitgKuwACWA0AAAEBQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAACU0AANAIBdQJM1AkxL0ANvQCLJxUgS4YEb+VNalFj1fLFryB82/wuHv03drHLskl+QHw7n2/7duJvFPaf+Vz+YHx5/bCvtch9vUl/AUfPl9s8tOcd/by7VkUfnn9vc7GqeGXxKM/+we4KnpVQH/+e9wt6VUD9V9t83KF+5t4+M/wA/bD7Y3Ia3eRi+vp/wCJKPq2ftvhpfPnlnGjhL8APt2favbtlzt7HzLXKq/ED7scMNv6ccV4JIBKmlwL6m66gJpUAm35AV5dLgFbuBVVAWwBOoBgWkAAAAAgKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAkgAAEAWqA7gR18AJ3/AOAFcz1AlF8raXbX4AHC/wBK+PwAjmaWAkS5emoCa1AkfACttw24QBdm1OqAO8NKmuoEdFKILEqXWdAMta9Cizjr5AS7j8wKsbxRgFk59Lr1ZBfCoB6TroUR9nHYCzPj1AWvV9QDl1/ACubLzIKo0uAfypZOEnYorxyp3AR1oBLAaowAFAAAAFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlwAAABJQCU7ASgH5b/ACeNx8Z3tzHBLq/yA8nkfcnE2nHHwe9krN0QHk8j37n78rDJbWD0S/iUfA+XyfWtz97P9y6abZIPW4f3Hv7cYcvBbqX619QHvcf3Lh8pJ7O6vW/0MD6oeTlQuyASm4gCV8gGVqVAJUrTsBautwMJLoAsqARY1lfTqBYmqVEBGvVEdQC+qKgaol3AqWmoBJO7qgIvADQEpEsCw2k1cDG5v7WynlvZrDuwPH5f3Hs7c48TB7ma/wCZl9Ig8Df9y5vKzeW7uZVtjiogDex7r7hxoxw3nkl/N809q2A9bj/czj08vY/82HzP4CD1uN7rweV/t72Ky/8ATzuB9sp1mnQCpoAoaAoAAAAoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAwIBbASe4EyeOOM5uMeuVEgPL5fvvC4/8ATxf724rJfSB4PK995vITx23+1t9hB5meee5k8tzN5t3bclE8ALHUCTpoBIAKjTxbxyWqcAffxveOfxnCz/cxX6XoSD1tj7k2oWPI22sndoQerx/cuDyMo297H1P9OVAPqcZfS0/Bp/kBGmlFfOwBQ8e66ALoCNt0QD1Qo1AYrJqZ8AJLX1OoFlpyBZlygGKTfVqoBT6pq0BYetPGhB+e7yOPs4zu7iw80yjzN/7g4W0mtr+rl0A8rkfcHL3E1spbOD1Vyjydzd3t/L1725lnl3bgDNIhAE2kBQFdQEPRf+ZUA+vj+583jNft7ryxX6HUg9zifcm3k45eHoy/mxA9vZ5OxycfVsbq3H4/MSD9a6vxAqm6UooeICmgFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEYFkCNrz6ARrp8AJlnht4+rPJY4q7dIA8Xm/cWztera4mL3dxXyskIOe5XuHL5mTe9ut4/y4uEUfIvw6AO2gACdwFQFQEdXIE1nQCpR56gRqO7AnzTMvwA/fZ5nK2f9rey24/lcEH3bXv8A7ht1eeO5H8yl/EQfZt/czX+9x/UtXi0hB9GH3Jwsl82GeC8JEH0bfvvtmWMLdymdcWgNv3b25tRvfgBte5cD/wDsYrsIMZe6+3p/76cagMvevbcPq3pXZSB8+X3F7div6eWec/5WhB+Gf3NsqVtcZ5Pq3Ag+Xd+4+blj6cMcMF4SIPh3vdfcN/6uRlHTQo+N555P1ZNt92AVXVKO1ALEKHVaARzFXIFo1NgGlwACuoF7aAIStQBasKeoG9vd3NprLazeGS1TA9vifcm9txhy8f3NtfrX1f8AEmjouNzONy8FubGayxf6LZfAD933YFASBZAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIwACVNHCV2wPK5/vfF4ieGH9Xe7AcxyvceTzcm97OMdNtUKPkhaOeyAi8vKwBIAAkCT0APvUBdQAiACAiynyAvcCVAPrFwJMWVQJXRyugFcNKmoEopXcCPFNyBMvTKSVQLlj4AWVi3jS2gBYpRCpqArNIS7AVPzAagWsgKyBW4AidwHYC+mAACoCwFAANJAdH/+QP029zPZf7m1k8MuquB7/A+4/TG3zlLf/N0IOi293b3sFntZLPF6gbAICgAAAAAAAAAAAAAAAAAAAAAAAAAAAAJAj7a6gfjyORscXDLc38/Skpa1YHK+4e+7/K9W3sN7XHVI1Yg8m9XcogErWbAFEAIQEgC6QBLAEAbrADLQCaASgFAMCJuIgC6SBHYCWgC3AldbAI7ALAK0m4FbnyAlnIGlUBLxsgCdZiAEqQEgAGoF1AaAKAVUAdwF2BaICR0AKQK61A+nic/k8PP1bGcfzY6Mg6z2/wB24/Pwhxt76+rB6sD0aqKUA14gJAAAAAAAAAAAAAAAAAAAAAAAAAAAQZTTorq6A8/3H3XY4OLxT9e/H09JKOQ5XM3+buPc5GTyS+nGyQH4V8yh4IBIEQClewE1QFAgEbAt1UBSnYA6ywIlIBIA0uoEnQCw7SBIgBoA/ICazoArrYBCAjAtWu/UBPmwKqruBJA1GsgSe0gFDvQDWkgRagFUA+gF8fgBJXQCgOwFdFQABQIA8gKlKA0snjks03jmrZIDofa/fvTGxzXe27/iiDo8clklli08cqrJWAvf9P4gUAAAAAAAAAAAAAAAAAAAAAAAASBLgeH7t74thvj8WMt+2W50EHLZ5vPJ5Z5PLPJy2+pRmktT5AACAQBACuApVASAKBHACkAQCzAETAkzRXAWugI70ArUAKKrAiagBekXAQ8aQBW+wGbwBfVWADcgI9MRqAkB9WoB0pICOgFsqgWXd2AKKtAE9AFV4gVd7gHEgPT2AsAAFIAdIAXQF0AV0ASlcBCaaa+Xr1A9b2z3nd4WWOG7OfFbU4u+JNHW7W7tb+K3dnL1Y5KZ6AfqgAAAAAAAAAAAAAAAAAAAAAAABDrFOr6EHO+8+8rFPi8R/O6bm4jQ5luW5byyyq2AgCgRgEqgGBH8AH4gJ7AQA5QB2AOKdQJe4CJYB9AI4i4D5bgKNygLHYCQ34AZnQBSFFwL3ioBxrQDMXgCqI7gWkgRXa0AWcYgVJqoFUAGBaASkgNYXxArxr3/AJgGsgLuuoFhTABzICG/ABawFAmq7AXWYAsUAICgAI3H8WB6PtvuW77fupzPGd8P4gdlsb+3yNtbuzl6scr9iD9Neq0YFAAAAAAAAAAAAAAAAAAAAAvX9OjA573v3l4J8Piv5/8AmZrvoIOZ1daurZRG6R+ICOgABcCToAroAibgSujoBagEgI0+oB9AFAJrIEArt3ALoBIi7oAToBInUCWYFuAVP8ADcqGogCOZAeluHMANfAC0ATN1ABLzAVAQ3rAFqr1AKKvoAoqX7gXtoAr1p0AJ6AXQCUeoF769ALD6+ICEAALswDbgBEICqYASBQJejsBdZ1A+/wBs9y3fb9zrx8n/AFMXoSDtNre29/bx3tpztZL5Y0A/TxsAAAAAAAAAAAAAAAAAADAlWB4vvfuv9rg+Px3/ANTmozS0QHJpuXOUt3buMCOlYuyg5AgBXAlwFqAPGwEAWAtwM6gWyAK4EgCK8AWzAagSr0AjXS4FpMMCNxMWALJOgBvRAI630AzLrIFmXHYCp4wuwEbUvJasBMAL3AtgK6VgCepAWkUuAd0uoBwqgFWGBXYBRIBVqQLCgB3AOtQCS1ATQAnC8QE0AspqoCIQF0AICgAFFV1Vo6gep7R7pnwN5be652M3VdGTR2OGWOWKyxc451xYGgAAAAAAAAAAAAAAAACNxPX8wPg905+Pt/H9Sf8AXzUbePR9QOK3M893cz3c3OebllGQAGQKBKgPICMCyBJARrIEkC+ACOtwGsgRJua0ANJVQBPyAjmYkCRCdQL4ASIARR9AInAFc2SAPR9AMuvZgEqzk5QGckk6WYGniklqAiszPYC1yoBWmBGo0AqvPYAqWYFjzAj/AJZAfTSQNViLgIbvQBEWr1AUgCqoDxAlgAFQFVQJ4AXwAKQKuoBgAAB9fiuqA6D2H3T9vL+x5GX9PKuzl/L2A6iIytWKMgKwAAAAAAAAAAAAAAAD89/ew2NvPd3XGG3UDhedzM+byMt7N/K6YLt1EHzPqUTuAkCTUCygIBGwK7eIEm/UAwEANOwB1isAGBF0uBbAT8gHgBluYYFyhpwBJhdwC+ICITUgFMWqBJcw/iAbdFPmAltvVJWAlPTOvQAoSqgIr0cLqBVKmLgJivxArb0YFTkCKQLFQKm0BF1YFcUaddQCrcCyApWPMA7UAICw3qAcWYEAv8QE3AVoBe0AVdwJoBZAALgX80Alqqo7yrygOx9l9x/vNhbO4/8AqNpV7og9bxuAAAAAAAAAAAAABYCKqiyu2Byv3B7it/c/tNnJ/tbdc31fQDw7VirsuiKAACTNAJ2AoE1AyrMC0lNgNQCQDu7AR1VAEAG4dbAKaATxATNADAawgJo+oFpCi4CKVAjugD9UygJk2BKOoBtKIcdwE9PmAlNbsBpDtoA9SUVA1dwgImlTUCttq0AV/SpYFpo5AeIEoAAQBbAJVQLoAdAGlfIB+pAUBAEigGtbQA9TilVqBFacasCoCoAATALUCgfvxOVnwuRhv7eVU69+qJo7vY38OTs4b225wzVAP0QFAAAAAAAAAAAEYHwe689cHiPNf7u58uC7jBxGTeWTzyrlk5yfcogFAiAagR+IEkCqLgIioCaAQA2AnQCQBdIAjadI8wJpcBHeADvEyAWoB/ECLtRgI01ATpYAletAJ5gRau8gF6tKAKru3oBV8tEgJ4W1AvqhQlQCRSbAVSqgTqgCc06AaigEUqkgVwA0gAqAavUCNNIA13AtkBP1TcC/jIC/YC95AdwDAICgFQBNwGgAC6UsAAT2AsKuiYHu/b3Pe1uvh7rnHNztP/MTR1MQ2o//ACAQFAAAAAAAAMCSAtW2N2wOJ955j5nMyars7fypaFHnV1ddQEgKgRUAtAEASlgJCAs0kCfVT8QJESgLIEmoCagG3NADUeICEq6gSUwGnq16AGqS9QJKAN9LoA5p1AjVbyA0ioESXxAJNP0unQBVtpAPpfVgKt3AJ0YEVgLWJdQEZemdAGk6gEnDYBd2wDTvIFi0agXWoCUgLKYCJpYC11sgFwJawFrSPMA3FwLNLVAlGBXICIUgJ0AviAX5gEqAAE6IC2ASAA1jllt5Y7mLjLCqYHde3ctcziYbqa9SUZLuQfXIFQAAAAAAABgRTK6MDzPeub/acPJY/wC5uP04+YHF1tq3OTKE3fwAgFAeIE8AFQAEQEigGnSIAlZnQA6gSi8QC6oBMgRWTAraAzRAOj0YF7oA1NdQIqR3AQm21cCRSdQEqLgFkoATabsCQ6yBmXNLAV1mQJ2AX8EBZf1Ky0Aq6zcCzAE9XUCprrPYAk29IAREt6AWQEAXwAaSBJAqAUyAJtAW9wCAqTlwAfcA3VAHRgE4uBdAHiAAeAF1AMAAUgez9v8AMexyXx9x/wBPc+npLIOuAAUAAAAGBAAC9wOL975i5XNyxxf9HZ+Rf6tSjzQAEAkfAAwFgI6gOgCzgC3c0AmtgI0+tOgCZAUvqBaYgYUuWwLrLsApE6AFDARoAhASawAdwDp/DxAKzT+ruBi6uAhtxpAFdFCUsCpyu4ESgCZNT8twC/EAp1UICuGoQBUVaAMXOUJSuoB4xVUqBNXWoGsLNtgXFtXqgDibgWwABTqAiKuoBNOwEa6AWJVNACrRgVpTGgCXZWQBzlYCsBGoB20AaAHZAVugE1AtXYABQAGscstvJZ4P5sXK8UB3nA5OPM4mzvqvqUNdHjRkH0r8VcCgJAAAABoCfmB8nuXJ/s+Fu7s/M1GC7sDhJbrlV5VfiyiAAFAI5AMCS3ABzoAbdAES+7AjSVAE9fgAlXAsx5gLUioEjTVgSQInLrYBa1ugFlQBG6AIaArmKKoEo3UA2oUXYEpEOvcDPpp0QFb/AAAUabloDK7OyuBfFUAzLmtAKtQJLdGBqrSaQC1HUAomjgCzNJANS6AFSQKqNVowLS4DwAVAWYFmQGsKgABNOgFVgLKiAJNwFH2ASAuBXjQAugFYEYBS6gWugFpqAoAAJ366AdF9tcmNzd4mT+XJerb7NKy8SDpdO4FkCQBUAAgCoEUy50uBzX3JyXlubfExfyperJdxg56k+Nig7AEAbgCSAuBLVAtlKqBLgQBbxAS2wDAARgJr/ECNwwLIBNugCsgIqBGm8rwkBmuLmbgVKHeaAOgGWqSBY0AmtKgV1pDS1AnpSblUigBRRS0oAna70YGYylgVUUO4Gp+VV1AJJyAShAXK3QCaSr6gIrAGlSUA0ANxFbgGn/MBXXxAtkAspALKgCYVrgJ9K6gJdAK3MALAKqwCZuBQFkBUAswJUCzIBAWiATICUB+/D38uNydnkY3xzqvwJo77HJZ4Y54ucWk0/EDQACgQAAANrFZZOiivkBwHN33yeXu7z1yYwfOUSwCagUCMCAWewB/ADLakAA7gRuEBdJ1AAK0h3AlK9rgISvqArqBJgBl4gFW7Aj8QDTvdAFLaaUdQF02ragT5VXR2AibmtQJOivIGppWQMqOrpoATuBHEAR1r0Aqa8wNTq0BlVq2+wGsZArbmtgJpQCpzCdQKldIC4zGgGZl2sBdQK+sgS7uBq2gES6gUABbeDAJALgLOAESAkBQDSaAeQEbAICwuoB9gEgVUoAemWtgO09j5H7/AxTvt/L5Imj06UjoAAAAFPMBK0uB8Puu//b8DeyVHkvTj4sDhbyyisCXqAjXQA4Ak6AGouA8AEN3Ak6K4FrrAEpEagRui6gW8AT8gCpl6lbQCQ5bWoCuoFb0VgI+1UBV3AlPVAElUpcBSYVgM9sW4AsQokCUyAuOKTva7AnqTlJeYCutgJKAky6fACubOwGXi201YC/4AWHDkCSoQGpkCS6tqwGl6csJS8UBEkodlotQKrywI01awFlJMAvzA1NALCsBmoGvpejXUAAhgSfl9P4gW3mAh3As1qAVG31Aq6AK9EAh6APEBTUBGrsAla2AoCgCaqALZJdAPf+2eR6N/c2G/qU4ruQdPRUfWhBSgAAmoDUDnvubejDZ2FT1fM/JwMHN+FijNNQAAABH4ASQFZA023oBmkUVQCjzAUAi7gFKYBgE5X8QJDijAR3AUWlWAgBABymvADLbTVACT9TqBU4ol5gSzh1bAzV0ahAaVn0QEeX8qoBNKvyAl2lFALSYt3Ay3ksq2A0slAGG5j03A3Mr5qPoBHSKAVPGKUYD1VSdUBtOspRAGbv1/qd0BV6tVQAn6b1AOWpQEbajUDTylKAK05UgWyUdQJS34gKfDUC31YBUVagS7A002ugE07gVAI7gSjA0nFqgGBJAvjVAFS6owLNAL2AlgKgPr9t3v7fnbO5asfEDu3EV6z8TIQUUAA7AR9rgcb7/vfu+4ZYXx2ksV51GDy62KD06gSVbUB4gLWAjYBwrgRSq6aAWWgEdNKgS9dQEqO4EVqgG+lgDuBGoSgCylCSAJ3oBJkCtKO4GG41AOrvUA6KWAShyBVlCdKaAZblSqZAL+IFU4pgYeTbjsBXCafYAlNUwInL9LVQLlT5WBn5arogKopFwK2l9VWAU5KX5ARtOmoFbigFWTiAFkAluEBpZT9KkCTOLTuAxo0BGoV6Aa9S7yBUk2BXEUQEWVIgBPYBGPqhNtAWErAVuqAlGwCarNQKBV6naAFgEsAomoB08ALpLsAAT8QHiBYAJtZLJXxayXk5A/oHF3f3uNs7l/Xisn5kH72AARQBdQEqZdkB/PuVm93lb2bdf3HPgmyj8Zbr1uAdXQDN56gJlVAsJX8gMuAK0kBALIBOZQGWqpAH0YBLxYD6qQ0AmF3AALUAk9AFqdbAH+VwMtdgKoVlV3kCKcsnYBlcBKiAIuqvYBFQJDeMsCNNxNEBXFJAypnsrAaeUuaJgMVCbyqBidYowNprKmqQEVE0wGTbS7dAJj11A08aynUBdvsgCTdwNVa7ICLKKWfYBGnxA07J6IAspVqAT9XYDUroA0qAegF7IBC0AJ1gA6fMAvdALAXKikCqL6AJ/4AGBEoAOE9QNRIBqI6ASrdNALUABbprsB2XsG7+77dtt3weS+FiD1XVAGBPEA6Afjy9z9vib+5MenbbXwA/n0tvLL+dz8Si9tEBFYCdrMCylpPYCPqwIqgUABH2AlUqgVW/JgZYFTc6gF6powJSK3APt8ADlqwBY6yAmaauwClnpcCLIBOsgROJeoEmH4gWvqsn2Ay3km4hAE57MC1loCJ31Al/EB4LxAjeKcfq6gKvWgGZikgWHdMCyprVAXWioBW070Aspq1OoEsm0AcKHM0sAq6qi6AWvbxAViH5MC0iACtGgFmkdAGMMC6VoAv5AEmmAmvYC9YAkZel69gH6aSA6dQNLuBJr17AIqBoCWAX8QGMPUDVpkCUmjgAnQCgJheIHTfa+5/T5GzP0tNLsyDoJr2ArAOFYB3YHne87no9t3nZUx+IHEKiSKD6AIAR8OoCE6oBDfiA/TDAisAfUBNAIqqHoBV+QCjAidALLi4GaeYCz7gFLYEb6AVNTYDKdW2A8QHVxQA3KTgBVL5mvDUDMfLOMyAaqvCoBoCS5ANq3UCtRVAR2U0kA8polbUDD9WXZARtqkSBfmuqAaTa+Z2Arayh2AL1NxFADaxcfgAdoAKoFhagLOHYCqJhO4F+ZZQ69wEXAOP8QI4ilwNTSHoAlAXSQGgDoBUwLLeoCk1AJy62AivIF1AKrqAp0YFUTMAFNdJAR1YDwAKYAs0AO3kB7n21uejl7mH82DfwRB1X1VQFnTQCMA5cAeT9x5R7fljpluYfgwOPuyiRUCqADkCRAClwIl0AJ1Ak1AXdAL4AR5Q0AXVAAEKwD6bgRNNXAKZhWAiXptfuAitXXsBLMA10ATCa6gYjJfSwNwldJ5agHRStQM2lTLAAR1XZ6gRpQlIB3/AMoFyeK+a66AY9LmbTVIA1WWvgBfU1FAJkpYCHlTpcDSiY1AZTNHD/AA11AJQ5A0lfoAUXYBJ2d/4AajG0AZ+ZwlRAVUTTuwL43AOiAQk66gVWikAWtkA7AFWnQC0VwJRAVTaKMB6qQkBXWFZgNfACJypAssBW6AaVAoBAWEBGBXYD1fYMvT7ljOuGS+JB2NZgB3AAWicgeH9x/NwtvT5pYHKIoiuAmAEgV9GBlqAKuoE1AnUCqMUBHSqYBvqASo2n5AF81UBHD18wEzS8AR+EAVSlMgZ+bUCuE5WunQDMRRurAuNQJ6ocRM/gAs7gE75TXoBl2hVqBpNpuz7gZlrpUCrH+af4AZyb+lR4gE2rNMC94sBltxLuBF6lVOQHrv31AktqE6qoGsZ+IFmKNT0YD1fDqwJMOaNAbjzfQDNVcDdfTGKTAjmU1azAqSm/cBNFUBMLrOoFspfxAk9XQCxo35gHFEqtdALi8tQLWZu+gEVG3adAK63AXVAKq6xAEVJArbdOl2BVFgIlCYFv4gVdLASqp1AtEA0AS0AugKvpaA9H2V/wDyW15gds3VkCGBAK076AeH9yNf2eH+pAcolHmUS9HcBbugGXYB0WoEdHHUBMUAaR5gJTp8QIlWoFpZgZvIFTSoAdKICWcLUB4gSKzNGAaXWnQCSl1AaqAI16svAC5dcQD0euoGXM01AVAkpNsCpP8AS6sBk4lN+AElPGtwJM2cgTJeTAmKU1bA02k6OQMtRIBKgEhzGgFpbUDbjToBhT4roBrvKS6AE16pTAqdZAvzXbhASiUeYFTVI8wFZ7AVNW0ATPggGaWVVSAK7IBKbt4gVRICkJp1YFheLAeQBRYC2AiAt7aXAf5uugFp5gKTQC2uBPHUC2cMBEsA2gEpVdgKB6Hsn/2O14sDtomWiCt2AykBZTV/IDxPuSvEwUfrA5RX6lCVNUBK+QCnkBKgG2/ECTDSuBaQAdUnbqBHFkwD6AK6oDM1jQBawFVWBKayAleQBpTD1AktAWtGBJSlzLegEnyAVUt3dwELqBHHUCNKAK0momKXAwukSBtRkpdEqNAZhLKlNZAmbmiYGcZ/V5AJh0XmBV1yuBL62AsqO4EbaejAOImzAKVE2A3k12YGsVjeAMzd6aAXFat+QBw2BVjFAKlSJqAhR3AKUlZgJmfVQDSrROgEswEUmagai1bASU5evYCxSZAl5AWXiBVESAl6gXuBVVyAcNvSAI3CuArIFlwBe4CQCcXQFA9D2VL/ANw2gO261IFwHi/IDMrpAHjfcn/Z4PT1gclpQosOUAcgR9AKpaAlf8QEr1ASH4qQDemgFSQEUTIB5VogIvmsrASiqBVqApUCJARpq1QDq4VeoEv1bAqU9mBmrdbAV3/gBLu4EomBaZT/ACrUDPbrZgLUfxAtMV6VVZAYyb9SUaAZXrmI8wNwqrXqBF8sp2Ai9Nm72AenuBflSq6sAmkrywLiv5qgYyeUL01U2ATmqsD9llpFGBn5UoVQEQgKovqAmfECtJQ7MBRsC5TFKxYCx8qevcCNw6qAHfRAVzSLAWFE6AEkvMC3QEmL0kBFOoFmgFlPxAsAFQBduAI0o7yBbgVdwJNWAANMC1swPR9k/wDstubAdrlEsgKGrALgSdIA8X7k/wCzw/1IDlJpBQdEBGn/AIgHjSQE9AJOoDQCJuYA1SewEmsaAWgGaASE7OAKrTogDaurgZhgFFJVeoC0wBV81bAR4wk3bqBIpK+IEfzKegBv9QEaUeIBqY9dHpAFlxEeIGL3UdADb0qBVLTfxAyrQqrqBU1HzXAytX3AucNoCqYcpQrAZo6gVVXgBLsC5Q7AXH0pVAlfq/AC4w/EB6tGqgGtQNY5PRAGpYDWugDWALr4AVNOX00Aqr9NfECrF1AnqbURYBaFFAEVf4AXGNQLS2NUBIxvNdQCiAN0YB0AlkAbhSrgF1YCugFQEvVAVpyAgCzAHo+yv/5HbA7RuGQWZ0AWXVgSt4A8X7lf/R7cL9akDlJ7SUSaSqsCeNALZVsBF8ADtAC0fiA7wAVXevQBRATJ0oBNE15gV3QDvoBl4zDskAVLsCxMQ/IDLyq5QEWSi0OJAZTeb/ACy7aASU1CAjThKKAI/ACOncC1Sj9LAmsR4ASyAelvGW/ACZKi69EBfVKh30AiTioE9Uty6QBFFlMAX1NUStfwAVS9WjsBE1oBpNt0QFXyqWBJ9TnKnQDTSmX5ASqmb6AWyeoFqkoV7gSK9OgFitwFZlXAKkN3AvqiWkAiWBqYdfIDPqo4VQNfUpsgLPeZAz6W1OoG7eYEegFcuFoBJrSwGpkA/wAAF72AP8AGtLAWwEpEK4F86gE3NQEp20A9H2R//I7U9AO2yq/lUdSAq00QEt4AKua+QHifccviYTpnYDlWtVRdCiJ1ooXUBe9QGkMCNy4sBUoAnWQEKZYBrVAS9WAmHABpWTqBHeQI2rt0AtWqAG1ENARJS/wAmSbogDeLorpAROi69AL6p8QMxLgA21Cm1wJ6o0mQDl1QF9biAI5apVasDD7S0AeNAF1OqAl6gaTUx1AmWNYn5ewCNE3IFhO+VbMCOb/pVEBHjbvYDTSxxnUCUjrIBV1lKwFxXqmaAHeYfQApmgG8c4jp1AypycvrQC+qsQBfVGoBpzN1oBVZt0QCazoAWrdtAE1pYDThKNAChOEBYdegElNgWQDfcDVIpqBIS8QL4gKQAAqtGvQCTDhgVT5gISrqAeXmAS1VEB6Ps0L3Ha8Lgdq6zLjoQJdgEYu7AkJp1A8X7ka/tMF0zuBytyiPwAQlFADcAFUCOQCqBGwADsAnQBCddQIrOAI02q2AsKYsoAjXRgSKp6gLuLMCAPVV0AnjqAoqLUBb5XV9QK62ugJlGjqBly6uwEc2VMXqBV2dgJKq3qAVfpowM5Nt0dgCc1A0q1v2ARelQMT6VEVYGknHqdloBenenwAjrTRASfmT0TA1Vt5OmOgBQwLKiEBK9QLCqsfICyprRQBXEymAbh2AVrT5QEfygF0YF6dgLZPL8ADsgK0qAI9KArT9VLAK1oAST0A02tNAJP4gE4ATDn8ANKuoErMyAlaoCp1AXAQ1cA7oD0fZn/8AI7XQDtGk28vwICxV5AQtbAVvGyVAPE+5IfCwlfrQHKWorFCAHqm9gJ+QFAgBfLM1kDMKQLpGgCWrqe4B26tgE6qgESluQJLhpgVtz6UroCV1uBn5rzUC5QofxATKq/ADMOzfiA9UuVpYBMARRM6AOrVwKrenXqBJSx9KAzLQBwnMz2QBPHpfQA27LUDCo311AuLbtRAZV5V1oBpvJtOz6AImXE9WAmY1xkCNuq00AuLaUqi1QETnGlmwKvlhaIDfqUSwJKolRdQDnQDWLh9EAxjSqAQ3KdgDoo1gCpwmgDt26APqUpgVSlLYCU7gayUUkCKt6gVPJKXYCgSHV3AtUpAUm17gOyVEBU2gH5gVRbV3AWoATAR8QCa0owLKd2BO1wPS9mSXuWy11f4Adqr5UuQHFIAAROAPE+5H/wBHhL/UgOVuuhRI63ARp1AWowH5gV2T1WgGXN7sCVATAFcqId9AI2lZgSVkobAvytRaAMtNylRAaahzegGXAFcLw0AkpV6ARttdFoBlS51YFolEeIESlOQJDVANPJpJR5gZeWTrqwEekCS3pQBCVMVL1YCEqXb1AixrCcsA08W6VYEWKnoBJbuqAXKqrXowJOSSWV9GBF6prVga1p5gKPGJh6IB/liwGvTSXYCRCr8wC1WqAWqcoCzNGBHOL+XUDSydKSwK5eVqz/ACLGjb1QETit30A1Cxbj6XqATcdANTpAF9UsBklCkBEtKKdQK09KxcAmrgJUzoBJtjotQNOZvTQA6eIErAFU6IAlWoGtewGZjx0AsagWKTABID0fZWl7jtz3A7VN1RAx7gRv5ar5pAZT4PoB4v3JXhYUn5lUDk3MqCiqtAI36qWjUBSK1YEUgVJTcBK0AkOQJMU1AfqAjibXAtqARN495AWuAy6yAmUBjVJgV0tcA3l/L5gEneZfUCZN6IBlHp7gZl9QL6komtAMNt5KEBZ9TlTOoDKHFWmAf1UYEmaagRLqAUpsA1+qfV2AOmKmzf0gTFfM60VgM3nqgNOXg3qgGLmF1VQLljLhqOjAtV8qcgSsVYGk4uwI5etAK0pVaAHUDLbcRdAfosk1DUMBLmZ1/gAx0mtAK5miA003rQCVXcCTLrQCuFEAVTV6AE/Vl0QFdkkq6gG3jRK4BSrgGuquBaenpAFbsBKgVRqBaMAgIrwwLVeAFmgEadIsB6XsiX/uG03cDs8nORBXMgPV8AEuWB4n3Gv+kwfXOvhIHKLIourqASXUCKl/IAk5rqwJaYAOLAErMA4bAmUzIEvVAWvS4EarRx1QCKdIAjiWloBE0tALervoBmVr8QJldVA1S6cAZTumAcAG0ppV2AmWkgJiXrKgCKW6W0Ajyy9URICWpbXgBIs0q6gVvVAWmKvM3Ay6enFWmoBTWVIE9PzOtIsBE0lVTIBJtN2A1jROlYowMy6dWBuNOoBUms9gD+ZqgCf0xQCTkqQBpptSAmVRQ0A6N3A3NV0AjifyAfTUCz8ADSmnkAoqO4Fup6XA1M0v1AzK8OgGqepO03AjrX6YYGndJeYCdGrAR/gBqJ1oBVYDOrnyAro13QFVpAeIBVuA0ArtIHoey/N7ht9KgdrVSkQSX59QP/2Q==\"}";
		return Response.status(HttpCode.HTTP_200_OK.getCode()).entity(json).build();

	}

}

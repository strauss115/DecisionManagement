package at.jku.se.rest.api;

import java.util.LinkedList;
import java.util.List;

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
import at.jku.se.model.Project;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import at.jku.se.rest.web.pojos.WebTeam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/web/team")
@Api(value = "webTeam")
public class WebTeamResource {

	private static final Logger log = LogManager.getLogger(WebTeamResource.class);

	// ------------------------------------------------------------------------

	public WebTeamResource() {

	}

	// ------------------------------------------------------------------------

	/**
	 * Converts a generic team/project to web API team format
	 */
	public static WebTeam convertTeam(Project team) {
		try {
			if (team != null) {
				WebTeam result = new WebTeam();
				// --
				result.setId(String.valueOf(team.getId()));
				result.setAdmin(String.valueOf(team.getAdmin().getId()));
				result.setName(team.getName());

				List<String> teamUserIds = new LinkedList<String>();
				User[] users = DBService.getAllUser().toArray(new User[0]);
				for (User user : users) {
					for (Project project : user.getProjects()) {
						if (project.getId() == team.getId()) {
							teamUserIds.add(String.valueOf(user.getId()));
						}
					}
				}
				result.setUsers(teamUserIds);
				// --
				return result;
			} else {
				log.error("Unable to convert team because of null reference");
				return new WebTeam();
			}
		} catch (Exception e) {
			log.error("Unexpected exception when converting team '" + team.getName() + "': " + e);
			return new WebTeam();
		}
	}

	/**
	 * Converts a list of generic teams/projects to web API team format
	 */
	public static List<WebTeam> convertTeam(List<Project> teams) {
		List<WebTeam> webTeams = new LinkedList<WebTeam>();

		for (Project t : teams) {
			if (t != null)
				webTeams.add(convertTeam(t));
		}
		// --
		return webTeams;
	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all teams", response = WebTeam.class, responseContainer = "List")
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all teams");

		if (!SessionManager.verifySession(token)) {
			log.info("Unauthorized access");
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		List<WebTeam> teams = convertTeam(DBService.getAllProjects());

		log.info("GET all teams returning '" + teams.size() + "' elements");
		return RestResponse.getSuccessResponse(teams);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets team by name", response = WebTeam.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No teams found with given id") })
	public Response get(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team id") @PathParam("id") long id) {
		log.debug("GET team by id'" + id + "'");

		if (!SessionManager.verifySession(token)) {
			log.info("Unauthorized access");
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		Project team = DBService.getNodeByID(Project.class, id, 1);
		if (team != null) {
			return RestResponse.getSuccessResponse(convertTeam(team));
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new team")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create team (see response message for further error details)"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response register(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team name", required = true) @QueryParam("teamName") String name,
			@ApiParam(value = "Administrator of team e-mail address", required = true) @QueryParam("adminUserEmail") String adminUserEmail,
			@ApiParam(value = "Passwort to join team", required = true) @QueryParam("password") String password) {
		log.debug("POST Create team '" + name + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User author = SessionManager.getUser(token);

			log.debug("Looking up user '" + adminUserEmail + "'");
			User admin = DBService.getUserByEmail(adminUserEmail);

			if (admin != null) {
				log.debug("Trying to create team");
				Project team = new Project(name, admin, password);
				DBService.updateNode(team, author.getId());
				// --
				return RestResponse.getSuccessResponse();
			} else {
				String error = "Unable to create team, admin user not found";
				log.error(error);
				return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, error);
			}
		} catch (Exception e) {
			log.error("Unable to create team: " + e);
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	@POST
	@Path("{id}/addUser")
	@ApiOperation(value = "Adds a user to a team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User successfully added to team"),
			@ApiResponse(code = 204, message = "User or team not found"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Server Error") })
	public Response addUserToProject(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the team", required = true) @PathParam("id") long teamId,
			@ApiParam(value = "User id", required = true) @QueryParam("userId") long userId,
			@ApiParam(value = "Password of the team, only necessary when user is not admin") @QueryParam("password") String password) {
		log.debug("POST addUser '" + userId + "' to team '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 1);
			User user = DBService.getNodeByID(User.class, userId, 1);
			User curUser = SessionManager.getUser(token);

			if (team != null && user != null) {
				// only allow action if current user is admin (general or of
				// team) or password is correct
				if (curUser.isAdmin() || team.getAdmin().getId() == curUser.getId()
						|| team.getPassword().equals(password)) {
					log.debug("Adding user to team");
					user.addToProject(team);
					return RestResponse.getSuccessResponse();
				} else {
					log.warn("Team password is wrong");
					return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
				}
			} else {
				log.error("Team or user not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.debug("Unable to add user to team", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	@POST
	@Path("{id}/removeUser")
	@ApiOperation(value = "Removes a user from a team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User successfully removes from team"),
			@ApiResponse(code = 204, message = "User or team not found"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Server Error") })
	public Response removeUserFromProject(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the team", required = true) @PathParam("id") long teamId,
			@ApiParam(value = "User id", required = true) @QueryParam("userId") long userId) {
		log.debug("POST removeUser '" + userId + "' from team '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 1);
			User user = DBService.getNodeByID(User.class, userId, 1);
			User curUser = SessionManager.getUser(token);

			if (team != null && user != null) {
				if (team.getAdmin().getId() == curUser.getId() || curUser.isAdmin()) {
					log.debug("Removing user from team");
					user.deleteFromProject(team);
					return RestResponse.getSuccessResponse();
				} else {
					log.warn("User is not admin of team");
					return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
				}
			} else {
				log.error("Team or user not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.debug("Unable to remove user from team", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	@POST
	@Path("{id}/setName")
	@ApiOperation(value = "Changes name of team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Name successfully changed"),
			@ApiResponse(code = 204, message = "User or team not found"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Server Error") })
	public Response changeName(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the team", required = true) @PathParam("id") long teamId,
			@ApiParam(value = "New name", required = true) @QueryParam("name") String value) {
		log.debug("POST setName '" + value + "' for team '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 1);
			User curUser = SessionManager.getUser(token);

			if (team != null) {
				if (team.getAdmin().getId() == curUser.getId() || curUser.isAdmin()) {
					log.debug("Updating team name");
					team.setName(value);
					DBService.updateNode(team, 0);
					return RestResponse.getSuccessResponse();
				} else {
					log.warn("User is not admin of team");
					return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
				}
			} else {
				log.error("Team not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.debug("Unable to change team name", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	@POST
	@Path("{id}/setPassword")
	@ApiOperation(value = "Changes password of team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Password successfully changed"),
			@ApiResponse(code = 204, message = "User or team not found"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Server Error") })
	public Response changePassword(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the team", required = true) @PathParam("id") long teamId,
			@ApiParam(value = "New password", required = true) @QueryParam("name") String value) {
		log.debug("POST setPassword '" + value + "' for team '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 1);
			User curUser = SessionManager.getUser(token);

			if (team != null) {
				if (team.getAdmin().getId() == curUser.getId() || curUser.isAdmin()) {
					log.debug("Updating team name");
					team.setPassword(value);
					DBService.updateNode(team, 0);
					return RestResponse.getSuccessResponse();
				} else {
					log.warn("User is not admin of team");
					return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
				}
			} else {
				log.error("Team not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.debug("Unable to change team password", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	@POST
	@Path("{id}/setAdmin")
	@ApiOperation(value = "Changes admin of team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Admin successfully changed"),
			@ApiResponse(code = 204, message = "User or team not found"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Server Error") })
	public Response changeName(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the team", required = true) @PathParam("id") long teamId,
			@ApiParam(value = "New admin user id", required = true) @QueryParam("adminId") long adminId) {
		log.debug("POST setAdmin '" + adminId + "' for team '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 1);
			User curUser = SessionManager.getUser(token);
			User newAdmin = DBService.getNodeByID(User.class, adminId, 1);

			if (team != null && newAdmin != null) {
				if (team.getAdmin().getId() == curUser.getId() || curUser.isAdmin()) {
					log.debug("Updating team admin");
					team.setAdmin(newAdmin);
					return RestResponse.getSuccessResponse();
				} else {
					log.warn("User is not admin of team");
					return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
				}
			} else {
				log.error("Team and/or admin user not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.debug("Unable to change team admin", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deletes a team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Team deleted successfully"),
			@ApiResponse(code = 500, message = "Unable to delete team") })
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team id") @PathParam("id") long teamId) {
		log.debug("DELETE team '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 1);
			User curUser = SessionManager.getUser(token);

			if (team != null && (curUser.isAdmin() || team.getAdmin().getId() == curUser.getId())) {
				DBService.deleteNode(team.getId());
				log.debug("Deleted team successfully");
				return RestResponse.getSuccessResponse();
			} else {
				log.warn("User is not admin of team");
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.debug("Failed to delete team", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}

}

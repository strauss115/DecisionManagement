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

	@DELETE
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deletes a team")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Team deleted successfully"),
			@ApiResponse(code = 500, message = "Unable to delete team") })
	public Response delete(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team name") @PathParam("name") String name) {
		log.debug("DELETE team '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// WebTeam t = SampleObjectProvider.getTeamByName(name);
		// if (SampleObjectProvider.deleteTeam(t)) {
		// log.debug("Deleted team successfully");
		// return RestResponse.getSuccessResponse();
		// } else
		// throw new Exception("Could not delete team");
		// } catch (Exception e) {
		// log.debug("Failed to delete team '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

}

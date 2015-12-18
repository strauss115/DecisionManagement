package at.jku.se.rest.api;

import java.util.LinkedList;
import java.util.List;

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
import at.jku.se.model.Decision;
import at.jku.se.model.User;
import at.jku.se.rest.web.parser.GoJsFormatter;
import at.jku.se.rest.web.pojos.WebDecision;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/web/decision")
@Api(value = "webDecision")
public class WebDecisionResource {

	private static final Logger log = LogManager.getLogger(WebDecisionResource.class);
	private static DecisionResource api = new DecisionResource();

	// ------------------------------------------------------------------------

	public WebDecisionResource() {

	}

	// ------------------------------------------------------------------------

	/**
	 * Converts a generic decision to web API decision format
	 */
	public static WebDecision convertDecision(Decision decision) {
		try {
			if (decision != null) {
				WebDecision result = new WebDecision();

				result.setId(String.valueOf(decision.getId()));
				result.setName(decision.getName());
				result.setCreationDate(decision.getCreationDate().getDate());
				// TODO add other attributes - missing db implementation

				return result;
			} else {
				log.error("Unable to convert decision because of null reference");
				return new WebDecision();
			}
		} catch (Exception e) {
			log.error("Unexpetected exception when converting decision '" + decision.getName() + "':" + e.getMessage());
			return new WebDecision();
		}
	}

	/**
	 * Converts a list of generic decision to web API decision format
	 */
	public static List<WebDecision> convertDecision(List<Decision> decisions) {
		List<WebDecision> webDecisions = new LinkedList<WebDecision>();

		for (Decision d : decisions) {
			if (d != null)
				webDecisions.add(convertDecision(d));
		}
		// --
		return webDecisions;
	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions", notes = "Only returns decisions that user has access to", response = WebDecision.class, responseContainer = "List")
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all decisions");

		if (!SessionManager.verifySession(token)) {
			log.warn("Unauthorized access");
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		User user = SessionManager.getUser(token);
		List<WebDecision> result = convertDecision(DBService.getAllDecisions(user));

		log.info("GET all decisions returning '" + result.size() + "' elements");
		return RestResponse.getSuccessResponse(result);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a decision by id", response = WebDecision.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given id") })
	public Response getById(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id") @PathParam("id") long id) {
		log.debug("GET decision by id '" + id + "'");
		return api.getDecision(token, id);
	}

	@GET
	@Path("/byTeam/{teamName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions for a given team", response = WebDecision.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given team name") })
	public Response getByTeamName(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Name of team") @PathParam("teamName") String teamName) {
		log.debug("GET decisions by team name '" + teamName + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// List<Decision> decisions =
		// SampleObjectProvider.getDecisionsByTeamName(teamName);
		// log.info("GET decisions by team name returning '" + decisions.size()
		// + "' elements");
		//
		// if (decisions.size() > 0) {
		// return RestResponse.getSuccessResponse(decisions);
		// } else {
		// return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		// }
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new decison")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)") })
	public Response create(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Name of decision") @QueryParam("name") String name,
			@ApiParam(value = "E-mail address of author") @QueryParam("userEmail") String userEmail,
			@ApiParam(value = "Team name where decisions belongs to") @QueryParam("teamName") String teamName) {
		log.debug("POST create decision name '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
		// try {
		// log.debug("Looking up user '" + userEmail + "'");
		// User author = SampleObjectProvider.getUserByEMail(userEmail);
		//
		// log.debug("Looking up team '" + teamName + "'");
		// Team team = SampleObjectProvider.getTeamByName(teamName);
		//
		// log.debug("Trying to create decision");
		// SampleObjectProvider.addDecision(name, author, team);
		//
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

	@POST
	@Path("/{id}/description")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets the description for a decision")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)") })
	public Response setDescription(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision Id") @PathParam("decisionId") String id,
			@ApiParam("Description to set") @QueryParam("description") String description) {
		log.debug("POST set description for '" + id + "' description '" + description + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);

		// try {
		// log.debug("Looking up decision '" + id + "'");
		// WebDecision d = SampleObjectProvider.getDecisionById(id);
		//
		// d.setDescription(description);
		//
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

	@GET
	@Path("/getGraphAsJsonById")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a JSON formatted graph for web app decision mind map")
	public Response getGraphAsJsonById(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @QueryParam("id") long id) {
		log.debug("GET decisions by id '" + id + "'");
		WebDecision decision = convertDecision(DBService.getDecisionById(id));
		Response response = null;
		if (decision != null) {
			GoJsFormatter f2 = new GoJsFormatter(decision);
			String result = f2.getGoJsString();
			System.out.println(result);
			response = Response.status(HttpCode.HTTP_200_OK.getCode()).entity(result).build();
			RestResponse.addResponseHeaders(response);
			return response;
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}
}

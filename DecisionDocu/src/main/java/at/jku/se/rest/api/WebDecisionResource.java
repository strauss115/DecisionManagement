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
import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.Decision;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.Node;
import at.jku.se.model.Project;
import at.jku.se.model.Rationale;
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
				// --
				result.setId(String.valueOf(decision.getId()));
				result.setName(decision.getName());
				result.setDescription(decision.getDescription());
				result.setCreationDate(decision.getCreationDate().getDate());
				result.setAuthor(decision.getAuthorId());
				result.setInfluenceFactors(decision.getInfluenceFactors());
				result.setRationales(decision.getRationales());
				result.setAlternatives(decision.getAlternatives());
				result.setQualityAttributes(decision.getQualityAttributes());
				result.setRelatedDecisions(Node.getListOfIds(decision.getRelatedDecisions()));
				result.setDocuments(Node.getListOfIds(decision.getDocuments()));
				result.setResponsibles(Node.getListOfIds(decision.getResponsibles()));
				// --
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
	@Path("/byTeam/{teamId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions for a given id", response = WebDecision.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given team id") })
	public Response getByTeamName(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team id") @PathParam("teamId") long teamId) {
		log.debug("GET decisions by team id '" + teamId + "'");
		try {
		Project team = DBService.getNodeByID(Project.class, teamId, 1);
		} catch (Exception e) {
			log.error("No team/project found with id '" + teamId + "'");
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
		
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
			@ApiParam(value = "Team id where decisions belongs to") @QueryParam("teamId") long teamId) {
		log.debug("POST create decision name '" + name + "'");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
//		try {
//			log.debug("Looking up user '" + userEmail + "'");
//			User author = DBService.getUserByEmail(userEmail);
//
//			log.debug("Looking up team id '" + teamId + "'");
//			Team team = SampleObjectProvider.getTeamByName(teamName);
//
//			log.debug("Trying to create decision");
//			SampleObjectProvider.addDecision(name, author, team);
//
//			return RestResponse.getSuccessResponse();
//		} catch (Exception e) {
//			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
//		}
	}

	@POST
	@Path("/{id}/description")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets the description for a decision")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)") })
	public Response setDescription(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision Id") @PathParam("decisionId") long id,
			@ApiParam("Description to set") @QueryParam("description") String description) {
		log.debug("POST set description for '" + id + "' description '" + description + "'");
		try {
			Decision d = DBService.getDecisionById(id);
			d.setDescription(description);
			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			log.error("Unable to set description: " + e);
			return RestResponse.getErrorResponse();
		}
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
		if (decision != null) {
			decision.addAlternative(new Alternative("Alternative 1 very very very long text text very very"));
			decision.addAlternative(new Alternative("A2"));
			decision.addAlternative(new Alternative("Alternative 2 middle length"));
			decision.addInfluenceFactor(new InfluenceFactor("Influence Factor 1 very very very long text text very very"));
			decision.addInfluenceFactor(new InfluenceFactor("IF 2"));
			decision.addInfluenceFactor(new InfluenceFactor("IF 3 middle"));
			decision.addConsequence(new Consequence("Consequence 1 very very very long text text very very"));
			decision.addConsequence(new Consequence("C 2"));
			decision.addConsequence(new Consequence("C 3 long text text very very"));
			decision.addRationale(new Rationale("R 1 very very very very very very long text"));
			decision.addRationale(new Rationale("R 2"));
			decision.addRationale(new Rationale("R 3 "));
			GoJsFormatter f2 = new GoJsFormatter(decision);
			String result = f2.getGoJsString();
			System.out.println(result);
			return Response.status(HttpCode.HTTP_200_OK.getCode()).entity(result).build();
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}
}

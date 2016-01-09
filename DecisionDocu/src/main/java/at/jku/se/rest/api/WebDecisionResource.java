package at.jku.se.rest.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import at.jku.se.dm.shared.NodeString;
import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.Decision;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.Message;
import at.jku.se.model.Node;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Project;
import at.jku.se.model.QualityAttribute;
import at.jku.se.model.Rationale;
import at.jku.se.model.User;
import at.jku.se.rest.web.parser.GoJsFormatter;
import at.jku.se.rest.web.pojos.WebDecision;
import at.jku.se.rest.web.pojos.WebMessage;
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
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

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
				result.setRationales(decision.getRationales());
				result.setAlternatives(decision.getAlternatives());
				result.setQualityAttributes(decision.getQualityAttributes());
				result.setRelatedDecisions(Node.getListOfIds(decision.getRelatedDecisions()));
				result.setDocuments(Node.getListOfIds(decision.getDocuments()));
				result.setResponsibles(Node.getListOfIds(decision.getResponsibles()));
				result.setLastActivity(decision.getLastActivity());
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

	/**
	 * Static helper method to update activity string
	 * 
	 * @param d
	 *            Decision to set last activity
	 * @param activity
	 *            Activity as string
	 */
	private static void updateLastActivity(Decision d, String activity) {
		d.setLastActivity(dateFormat.format(new Date()) + ": " + activity);
	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions", notes = "Only returns decisions that user has access to", response = WebDecision.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized") })
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
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given id"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getById(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id") @PathParam("id") long id) {
		log.debug("GET decision by id '" + id + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Decision d = DBService.getNodeByID(Decision.class, id, 1);
			if (d != null) {
				return RestResponse.getSuccessResponse(convertDecision(d));
			} else {
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("Unable to get decision '" + id + "': " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@GET
	@Path("/byTeam/{teamId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions for a given id", response = WebDecision.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given team id"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getByTeamName(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team id") @PathParam("teamId") long teamId) {
		log.debug("GET decisions by team id '" + teamId + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Project team = DBService.getNodeByID(Project.class, teamId, 2);

			if (team != null) {
				List<Decision> decisions = team.getDecisions();
				if (decisions != null && !decisions.isEmpty()) {
					return RestResponse.getSuccessResponse(convertDecision(decisions));
				}
			}
		} catch (Exception e) {
			log.error("Unable to get decisions with team id '" + teamId + "'");
			return RestResponse.getErrorResponse();
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new decison")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response create(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Name of decision") @QueryParam("name") String name,
			@ApiParam(value = "E-mail address of author") @QueryParam("userEmail") String userEmail,
			@ApiParam(value = "Team id where decisions belongs to") @QueryParam("teamId") long teamId) {
		log.debug("POST create decision name '" + name + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			log.debug("Looking up user '" + userEmail + "'");
			User author = DBService.getUserByEmail(userEmail);

			log.debug("Looking up team id '" + teamId + "'");
			Project team = DBService.getNodeByID(Project.class, teamId, 1);

			if (author != null && team != null) {
				log.debug("Trying to create decision");
				Decision d = new Decision(name);

				updateLastActivity(d, "Created decision");

				DBService.updateNode(d, author.getId());
				// --
				team.addDecision(d);
				// --
				return RestResponse.getSuccessResponse(d);
			} else {
				log.error("Unable to create decision, user or team not found");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to create decision: " + e);
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/description")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets the description for a decision")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response setDescription(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision Id") @PathParam("id") long id,
			@ApiParam("Description to set") @QueryParam("description") String description) {
		log.debug("POST set description for '" + id + "' description '" + description + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Decision d = DBService.getDecisionById(id);
			d.setDescription(description);
			updateLastActivity(d, "Updated description '" + description + "'");
			d.setLastActivity("Set description '" + description + "'");
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

		if (!SessionManager.verifySession(token)) {
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		WebDecision decision = convertDecision(DBService.getDecisionById(id));
		if (decision != null) {
			String result = GoJsFormatter.convertDecisionToGoJsJson(decision);
			System.out.println(result);
			return Response.status(HttpCode.HTTP_200_OK.getCode()).entity(result).build();
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}

	@GET
	@Path("/getTeamGraphsForConnectionAsJsonByTeamId")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a JSON formatted graph for web app decisions relationships")
	public Response getTeamGraphsForConnectionAsJson(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Team id", required = true) @QueryParam("teamId") long teamId) {
		log.debug("GET decisions by team id '" + teamId + "'");

		if (!SessionManager.verifySession(token)) {
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		Project team = DBService.getNodeByID(Project.class, teamId, 2);

		if (team != null) {
			List<Decision> decisions = team.getDecisions();
			if (decisions != null && !decisions.isEmpty()) {
				List<WebDecision> webDecisions = convertDecision(decisions);
				String result = GoJsFormatter.convertDecisionsToRelationshipsOverviewGraph(webDecisions);
				System.out.println(result);
				return Response.status(HttpCode.HTTP_200_OK.getCode()).entity(result).build();
			}
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}
	// ------------------------------------------------------------------------

	@GET
	@Path("/{id}/messages")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all messages for decision")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to get messages due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized to read messages for decision"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response getMessages(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id) {
		log.debug("GET all messages for decision '" + id + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				log.warn("Unauthorized access");
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Decision d = DBService.getNodeByID(Decision.class, id, 3);
			if (d == null) {
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
			// --
			List<WebMessage> result = new LinkedList<WebMessage>();
			for (Message m : d.getMessages()) {
				result.add(WebMessage.getWebMessage(m));
			}
			// --
			return RestResponse.getSuccessResponse(result);
		} catch (Exception e) {
			log.error("Unable to get messages for decision", e);
			return RestResponse.getErrorResponse();
		}
	}

	// ------------------------------------------------------------------------

	@PUT
	@Path("/{id}/removeAttribute")
	@ApiOperation(value = "Removes attributes which are existence dependent to a decision", notes = "influence factor, rationale, alternatives, consequence, quality attributes, document")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized or not allowed to delete other node"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response deleteAttribute(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Attribute node id", required = true) @QueryParam("nodeId") long otherNodeId) {
		log.debug("GET removeAttribute '" + otherNodeId + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			Decision d = DBService.getNodeByID(Decision.class, id, 2);
			if (!d.isRelatedToNode(otherNodeId)) {
				log.warn("Denying to delete node '" + otherNodeId + "' which is not related to '" + id + "'");
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			// --
			NodeInterface otherNode = DBService.getNodeByID(NodeInterface.class, otherNodeId, 1);
			String nodeType = otherNode.getNodeType();
			if (nodeType.equals(NodeString.INFLUENCEFACTOR) || nodeType.equals(NodeString.RATIONALE)
					|| nodeType.equals(NodeString.ALTERNATIVE) || nodeType.equals(NodeString.CONSEQUENCE)
					|| nodeType.equals(NodeString.QUALITYATTRIBUTE) || nodeType.equals(NodeString.DOCUMENT)) {
				DBService.deleteNode(otherNodeId);

				updateLastActivity(d, "Removed an attribute");
				DBService.updateNode(d, 0);

				return RestResponse.getSuccessResponse();
			} else {
				log.warn("Denying to delete unallowed node '" + otherNodeId + "'");
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("Unable to add attribute to decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	// ------------------------------------------------------------------------

	@POST
	@Path("/{id}/addInfluenceFactor")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new influence factor")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addInfluenceFactor(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Value to set", required = true) @QueryParam("value") String value) {
		log.debug("GET addInfluenceFactor '" + value + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User u = SessionManager.getUser(token);
			Decision d = DBService.getDecisionById(id);

			if (d == null) {
				log.error("Unable to add value, decision '" + id + "' not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}

			InfluenceFactor node = new InfluenceFactor(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
				updateLastActivity(d, "Updated influence factors");
				d.addInfluenceFactor(node);
				return RestResponse.getSuccessResponse(node);
			} else {
				log.error("Unable to save new node to database");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to add attribute to decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@POST
	@Path("/{id}/addRationale")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new rationale")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addRationale(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Value to set", required = true) @QueryParam("value") String value) {
		log.debug("GET addRationale '" + value + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User u = SessionManager.getUser(token);
			Decision d = DBService.getDecisionById(id);

			if (d == null) {
				log.error("Unable to add value, decision '" + id + "' not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}

			Rationale node = new Rationale(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
				updateLastActivity(d, "Updated rationals");
				d.addRationale(node);
				return RestResponse.getSuccessResponse(node);
			} else {
				log.error("Unable to save new node to database");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to add attribute to decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@POST
	@Path("/{id}/addAlternative")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new alternative")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addAlternative(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Value to set", required = true) @QueryParam("value") String value) {
		log.debug("GET addAlternative '" + value + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User u = SessionManager.getUser(token);
			Decision d = DBService.getDecisionById(id);

			if (d == null) {
				log.error("Unable to add value, decision '" + id + "' not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}

			Alternative node = new Alternative(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
				updateLastActivity(d, "Updated alternatives");
				d.addAlterantive(node);
				return RestResponse.getSuccessResponse(node);
			} else {
				log.error("Unable to save new node to database");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to add attribute to decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@POST
	@Path("/{id}/addConsequence")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new Consequence")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addConsequence(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Value to set", required = true) @QueryParam("value") String value) {
		log.debug("GET addConsequence '" + value + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User u = SessionManager.getUser(token);
			Decision d = DBService.getDecisionById(id);

			if (d == null) {
				log.error("Unable to add value, decision '" + id + "' not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}

			Consequence node = new Consequence(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
				updateLastActivity(d, "Updated consequences");
				d.addConsequence(node);
				return RestResponse.getSuccessResponse(node);
			} else {
				log.error("Unable to save new node to database");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to add attribute to decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@GET
	@Path("/allQualityAttributes")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a list of all quality attributes")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to get quality attributes due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 200, message = "Ok") })
	public Response getAllQualityAttributes(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET allQualityAttributes");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			return RestResponse.getSuccessResponse(DBService.getAllQualityAttributeNames());

		} catch (Exception e) {
			log.error("Unable get all quality attributes", e);
			return RestResponse.getErrorResponse();
		}
	}

	@POST
	@Path("/{id}/addQualityAttribute")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new QualityAttribute")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addQualityAttribute(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Value to set", required = true) @QueryParam("value") String value) {
		log.debug("GET addQualityAttribute '" + value + "'");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

			User u = SessionManager.getUser(token);
			Decision d = DBService.getDecisionById(id);

			if (d == null) {
				log.error("Unable to add value, decision '" + id + "' not found");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}

			QualityAttribute node = new QualityAttribute(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
				updateLastActivity(d, "Updated quality attributes");
				d.addQualityAttribute(node);
				return RestResponse.getSuccessResponse(node);
			} else {
				log.error("Unable to save new node to database");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to add attribute to decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@PUT
	@Path("/{id}/addRelatedDecision")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new related decision")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addRelatedDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Related decision id", required = true) @QueryParam("value") long relatedId) {
		log.debug("GET addRelatedDecision");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			// --
			Decision d = DBService.getNodeByID(Decision.class, id, 1);
			Decision relatedDecision = DBService.getNodeByID(Decision.class, relatedId, 1);
			// --
			if (d != null && relatedDecision != null) {
				updateLastActivity(d, "Updated related decisions");
				d.addRelatedDecision(relatedDecision);
				return RestResponse.getSuccessResponse();
			} else {
				log.error("Unable to find decisions in database");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("Unable to add related decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@PUT
	@Path("/{id}/removeRelatedDecision")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Removes a related decision")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response removeRelatedDecision(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Related decision id", required = true) @QueryParam("value") long relatedId) {
		log.debug("GET removeRelatedDecision");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			// --
			Decision d = DBService.getNodeByID(Decision.class, id, 1);
			Decision relatedDecision = DBService.getNodeByID(Decision.class, relatedId, 1);
			// --
			if (d.deleteRelatedDecision(relatedDecision)) {
				updateLastActivity(d, "Updated related decisions");
				DBService.updateNode(d, 0);
				return RestResponse.getSuccessResponse();
			} else {
				log.error("Unable to delete related decision");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to delete related decision: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@PUT
	@Path("/{id}/addResponsible")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new responsible")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addResponsible(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Responsible user id", required = true) @QueryParam("value") long relatedId) {
		log.debug("GET addResponsible");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			// --
			Decision d = DBService.getNodeByID(Decision.class, id, 1);
			User relatedUser = DBService.getNodeByID(User.class, relatedId, 1);
			// --
			if (d != null && relatedUser != null) {
				updateLastActivity(d, "Updated responsibles");
				d.addResponsible(relatedUser);
				return RestResponse.getSuccessResponse();
			} else {
				log.error("Unable to find decision/user in database");
				return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("Unable to add responsible: " + e);
			return RestResponse.getErrorResponse();
		}
	}

	@PUT
	@Path("/{id}/removeResponsible")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a new responsible")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Unable to add attribute due to server error"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 204, message = "Decision id not found"), @ApiResponse(code = 200, message = "Ok") })
	public Response addQualityAttribute(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision id", required = true) @PathParam("id") long id,
			@ApiParam(value = "Responsible user id", required = true) @QueryParam("value") long relatedId) {
		log.debug("GET removeRelatedDecision");

		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			// --
			Decision d = DBService.getNodeByID(Decision.class, id, 1);
			User relatedUser = DBService.getNodeByID(User.class, relatedId, 1);
			// --
			if (d.deleteResponsible(relatedUser)) {
				updateLastActivity(d, "Updated responsibles");
				DBService.updateNode(d, 0);
				return RestResponse.getSuccessResponse();
			} else {
				log.error("Unable to delete responsible");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to delete responsible: " + e);
			return RestResponse.getErrorResponse();
		}
	}

}

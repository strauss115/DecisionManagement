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
import at.jku.se.database.strings.NodeString;
import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.Decision;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.Node;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Project;
import at.jku.se.model.QualityAttribute;
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
					RestResponse.getSuccessResponse(convertDecision(decisions));
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
				DBService.updateNode(d, author.getId());
				// --
				team.addDecision(d);
				// --
				return RestResponse.getSuccessResponse();
			} else {
				log.error("Unable to create decision, user or team not found");
				return RestResponse.getErrorResponse();
			}
		} catch (Exception e) {
			log.error("Unable to create decision: " + e);
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	@POST
	@Path("/{id}/description")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets the description for a decision")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response setDescription(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "Decision Id") @PathParam("decisionId") long id,
			@ApiParam("Description to set") @QueryParam("description") String description) {
		log.debug("POST set description for '" + id + "' description '" + description + "'");
		try {
			if (!SessionManager.verifySession(token)) {
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}

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

		if (!SessionManager.verifySession(token)) {
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		WebDecision decision = convertDecision(DBService.getDecisionById(id));
		if (decision != null) {
			decision.addAlternative(new Alternative("Alternative 1 very very very long text text very very"));
			decision.addAlternative(new Alternative("A2"));
			decision.addAlternative(new Alternative("Alternative 2 middle length"));
			decision.addInfluenceFactor(
					new InfluenceFactor("Influence Factor 1 very very very long text text very very"));
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

	// ------------------------------------------------------------------------

	@GET
	@Path("/{id}/removeAttribute")
	@ApiOperation(value = "Removes attributes which are existence dependent to a decision (influence factor, rationale, alternatives, consequence, quality attributes, document")
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

	@GET
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
			InfluenceFactor node = new InfluenceFactor(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
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
			Rationale node = new Rationale(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
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
			Alternative node = new Alternative(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
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
			Consequence node = new Consequence(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
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
			QualityAttribute node = new QualityAttribute(value);
			node = DBService.updateNode(node, u.getId());

			if (node != null) {
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
	
	@GET
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

	@GET
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

	@GET
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

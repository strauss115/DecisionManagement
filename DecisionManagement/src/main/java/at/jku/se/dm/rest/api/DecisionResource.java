package at.jku.se.dm.rest.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.parser.GoJsFormatter;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.pojos.Decision;
import at.jku.se.dm.rest.pojos.Team;
import at.jku.se.dm.rest.pojos.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/decision")
@Api(value = "decision")
public class DecisionResource {

	private static final Logger log = LogManager.getLogger(DecisionResource.class);

	// not needed so far
	// @Context
	// UriInfo ui;

	// ------------------------------------------------------------------------

	public DecisionResource() {

	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions", response = Decision.class, responseContainer = "List")
	public Response getAll() {
		log.debug("GET all decisions");

		List<Decision> decisions = SampleObjectProvider.getAllDecisions();
		log.info("GET all decisions returning '" + decisions.size() + "' elements");

		return RestResponse.getSuccessResponse(decisions);
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a decision by name", response = Decision.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given name") })
	public Response get(@ApiParam(value = "Name of decision") @PathParam("name") String name) {
		log.debug("GET decision '" + name + "'");

		Decision d = SampleObjectProvider.getDecisionByName(name);
		if (d != null) {
			log.info("GET decision returning '" + d.getName() + "'");
			return RestResponse.getSuccessResponse(d);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}

	@GET
	@Path("/byId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a decision by id", response = Decision.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given id") })
	public Response getById(@ApiParam(value = "Decision id") @PathParam("id") String id) {
		log.debug("GET decision by id '" + id + "'");

		Decision d = SampleObjectProvider.getDecisionById(id);
		if (d != null) {
			log.info("GET decision returning '" + d.getName() + "'");
			return RestResponse.getSuccessResponse(d);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}

	@GET
	@Path("/byTeam/{teamName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all decisions for a given team", response = Decision.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No decision found with given team name") })
	public Response getByTeamName(@ApiParam(value = "Name of team") @PathParam("teamName") String teamName) {
		log.debug("GET decisions by team name '" + teamName + "'");

		List<Decision> decisions = SampleObjectProvider.getDecisionsByTeamName(teamName);
		log.info("GET decisions by team name returning '" + decisions.size() + "' elements");

		if (decisions.size() > 0) {
			return RestResponse.getSuccessResponse(decisions);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a new decison")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)") })
	public Response create(@ApiParam(value = "Name of decision") @QueryParam("name") String name,
			@ApiParam(value = "E-mail address of author") @QueryParam("userEmail") String userEmail,
			@ApiParam(value = "Team name where decisions belongs to") @QueryParam("teamName") String teamName) {
		log.debug("POST create decision name '" + name + "'");

		try {
			log.debug("Looking up user '" + userEmail + "'");
			User author = SampleObjectProvider.getUserByEMail(userEmail);

			log.debug("Looking up team '" + teamName + "'");
			Team team = SampleObjectProvider.getTeamByName(teamName);

			log.debug("Trying to create decision");
			SampleObjectProvider.addDecision(name, author, team);

			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	@POST
	@Path("/{id}/description")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets the description for a decision")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unable to create decison (see response message for further error details)") })
	public Response setDescription(@ApiParam(value = "Decision Id") @PathParam("decisionId") String id,
			@ApiParam("Description to set") @QueryParam("description") String description) {
		log.debug("POST set description for '" + id + "' description '" + description + "'");

		try {
			log.debug("Looking up decision '" + id + "'");
			Decision d = SampleObjectProvider.getDecisionById(id);

			d.setDescription(description);

			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}

	@GET
	@Path("/getGraphAsJsonById")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a JSON formatted graph for web app decision mind map")
	public Response getGraphAsJsonById(@ApiParam(value = "Decision id") @QueryParam("id") String id) {
		log.debug("GET decisions by id '" + id + "'");

		Decision decision = SampleObjectProvider.getDecisionById(id);
		Response response = null;
		if (decision != null) {
			GoJsFormatter f2 = new GoJsFormatter(decision);
			String result = f2.getGoJsString();
			System.out.println(result);
			response = Response.status(HttpCode.HTTP_200_OK.getCode()).entity(result).build();
			return response;
		}
		return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
	}
}

package at.jku.se.rest.web.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.model.Message;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;

import io.swagger.annotations.*;

@Path("/web/chat")
@Api(value = "web/chat")
public class ChatResource {

	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	// private static Genson genson = new Genson();

	// ------------------------------------------------------------------------

	public ChatResource() {

	}

	// ------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all saved chat messages", notes = "Returns a list of all chat messages saved on the server", response = Message.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "User is not authorized to execute this operation") })
	public Response getAll(@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token) {
		log.debug("GET all chat messages");

		if (!SessionManager.verifySession(token)) {
			log.warn("Unauthorized access");
			return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
		}

		List<Message> chats = DBService.getAllMessages();
		log.info("GET all chat messages returning '" + chats.size() + "' elements");

		return RestResponse.getSuccessResponse(chats);
	}

	@GET
	@Path("/{relatedObjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all chats for a specific related object", notes = "Returns a list of all chats related to given object", response = Message.class, responseContainer = "List")
	public Response get(@PathParam("relatedObjectId") String relatedObjectId) {
		log.debug("GET chats by related object id '" + relatedObjectId + "'");

		// TODO just return e.g. last 50 chat messages
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Saves a new chat object on server")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Message saved successfully"),
			@ApiResponse(code = 500, message = "Unable to save chat message") })
	public Response create(@ApiParam(value = "Chat object as json string", required = true) String json) {
		log.debug("CREATE chat: " + json);
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
		// try {
		// Chat c = genson.deserialize(json, Chat.class);
		// SampleObjectProvider.addChat(c);
		// log.debug("Created chat successfully");
		// return RestResponse.getSuccessResponse();
		// } catch (Exception e) {
		// log.debug("Failes to create new chat '" + e + "'");
		// return
		// RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR,
		// e.getMessage());
		// }
	}

}

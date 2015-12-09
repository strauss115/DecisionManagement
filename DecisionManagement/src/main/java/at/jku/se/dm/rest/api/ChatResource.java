package at.jku.se.dm.rest.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.owlike.genson.Genson;

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.pojos.Chat;

import io.swagger.annotations.*;

@Path("/chat")
@Api(value = "chat")
public class ChatResource {

	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	private static Genson genson = new Genson();
	
	// ------------------------------------------------------------------------
	
	public ChatResource() {
		
	}
	
	// ------------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all saved chats", 
	    notes = "Returns a list of all chats saved on the server",
	    response = Chat.class,
	    responseContainer = "List")
	public Response getAll() {
		log.debug("GET all chats");
		
		List<Chat> chats = SampleObjectProvider.getAllChats();
		log.info("GET all chats returning '" + chats.size() + "' elements");
		
		return RestResponse.getSuccessResponse(chats);
	}
	
	@GET
	@Path("/{relatedObjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets all chats for a specific related object",
				  notes = "Returns a list of all chats related to given object",
				  response = Chat.class,
				  responseContainer = "List")
	public Response get(@PathParam("relatedObjectId") String relatedObjectId) {
		log.debug("GET chats by related object id '" + relatedObjectId + "'");
		
		// TODO just return e.g. last 50 chat messages
		return RestResponse.getSuccessResponse(SampleObjectProvider.getChatsByRelatedObject(relatedObjectId));
	}
		
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Saves a new chat object on server")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Message saved successfully"),
			@ApiResponse(code = 500, message = "Unable to save chat message")
	})
	public Response create(@ApiParam(value = "Chat object as json string", required = true) String json) {
		log.debug("CREATE chat: " + json);
		
		try {
			Chat c = genson.deserialize(json, Chat.class);
			SampleObjectProvider.addChat(c);
			log.debug("Created chat successfully");
			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			log.debug("Failes to create new chat '" + e + "'");
			return RestResponse.getSimpleTextResponse(HttpCode.HTTP_500_SERVER_ERROR, e.getMessage());
		}
	}
	
}

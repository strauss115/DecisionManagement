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

@Path("/chat")
public class ChatResource {

	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	private static Genson genson = new Genson();
	
	// ------------------------------------------------------------------------
	
	public ChatResource() {
		
	}
	
	// ------------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		log.debug("GET all chats");
		
		List<Chat> chats = SampleObjectProvider.getAllChats();
		log.info("GET all chats returning '" + chats.size() + "' elements");
		
		return RestResponse.getSuccessResponse(chats);
	}
	
	@GET
	@Path("/{relatedObjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("relatedObjectId") String relatedObjectId) {
		log.debug("GET chats by related object id '" + relatedObjectId + "'");
		
		// TODO just return e.g. last 50 chat messages
		return RestResponse.getSuccessResponse(SampleObjectProvider.getChatsByRelatedObject(relatedObjectId));
	}
		
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log.debug("CREATE chat: " + json);
		
		try {
			Chat c = genson.deserialize(json, Chat.class);
			SampleObjectProvider.addChat(c);
			log.debug("Created chat successfully");
			return RestResponse.getSuccessResponse();
		} catch (Exception e) {
			log.debug("Failes to create new chat '" + e + "'");
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
}

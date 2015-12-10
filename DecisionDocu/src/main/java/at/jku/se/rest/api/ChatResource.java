package at.jku.se.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = { "chat" })
@Path("/chat")
public class ChatResource {
	private static final Logger log = LogManager.getLogger(ChatResource.class);
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns a list of all available chats")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") }
	)
	public Response getAll() {
		log.debug("Get ALL chats invoked");
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
		
	}
}

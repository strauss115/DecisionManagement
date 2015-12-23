package at.jku.se.rest.response;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Static class containing methods to generate a REST response
 */
public class RestResponse {

	private static final Logger log = LogManager.getLogger(RestResponse.class);

	/**
	 * Using Jaxon to serialize to JSON
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	// ------------------------------------------------------------------------

	/**
	 * @return Returns a 200 OK response
	 */
	public static Response getSuccessResponse() {
		return getResponse(HttpCode.HTTP_200_OK);
	}

	/**
	 * @return Returns a general 500 server error response
	 */
	public static Response getErrorResponse() {
		return getResponse(HttpCode.HTTP_500_SERVER_ERROR);
	}

	/**
	 * @param code
	 *            HTTP code to generate response
	 * @return Returns a response for REST service
	 */
	public static Response getResponse(HttpCode code) {
		return Response.status(code.getCode()).build();
	}

	// ------------------------------------------------------------------------

	/**
	 * Creates a simple response including a text
	 * 
	 * @param code
	 *            HTTP code to generate response
	 * @param text
	 *            Text which the response should contain
	 * @return Returns a response for REST service
	 */
	public static Response getSimpleTextResponse(HttpCode code, String text) {
		Response r = Response.status(code.getCode()).entity(text).build();
		return addResponseHeaders(r);
	}

	// ------------------------------------------------------------------------

	public static Response getSuccessResponse(Object data) {
		return getResponse(HttpCode.HTTP_200_OK, data);
	}

	public static Response getSuccessResponse(List<Object> data) {
		return getResponse(HttpCode.HTTP_200_OK, data);
	}

	/**
	 * @param code
	 *            HTTP code to generate response
	 * @param data
	 *            Data object which should be attached to REST response
	 * @return Returns a response for REST service
	 */
	public static Response getResponse(HttpCode code, Object data) {
		return getResponseWithData(code, data);
	}

	/**
	 * @param code
	 *            HTTP code to generate response
	 * @param data
	 *            List of data objects which should be attached to REST response
	 * @return Returns a response for REST service
	 */
	public static Response getResponse(HttpCode code, List<Object> data) {
		return getResponseWithData(code, data);
	}

	/**
	 * 
	 * @param response
	 *            response to which the header is added
	 * @return response response with the added header
	 */
	public static Response addResponseHeaders(Response response) {
		response.getHeaders().add("Access-Control-Allow-Origin", "*");
		response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.getHeaders().add("Access-Control-Allow-Credentials", "true");
		response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		return response;
	}

	// ------------------------------------------------------------------------

	public static String packData(Object data) {
		try {
			String jsonData = mapper.writeValueAsString(data);
			return jsonData;
		} catch (Exception e) {
			return null;
		}
	}

	// ------------------------------------------------------------------------

	private static Response getResponseWithData(HttpCode code, Object data) {
		log.debug("Generating '" + code + "' response");
		
		if (data == null) {
			return Response.status(HttpCode.HTTP_500_SERVER_ERROR.getCode()).build();
		}

		try {
			String jsonData = mapper.writeValueAsString(data);
			log.debug("Attaching data: " + jsonData);
			return addResponseHeaders(Response.status(code.getCode()).entity(jsonData).build());
			// --
		} catch (Exception e) {
			log.error("Error serializing data: " + e);
			return addResponseHeaders(Response.status(HttpCode.HTTP_500_SERVER_ERROR.getCode()).build());
		}
	}

}

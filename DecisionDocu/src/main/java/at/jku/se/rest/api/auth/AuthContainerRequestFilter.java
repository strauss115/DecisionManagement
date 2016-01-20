package at.jku.se.rest.api.auth;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.auth.SessionManager;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;

/**
 * API Class for AuthContainer implements ContainerRequestFilter
 * @author August
 *
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthContainerRequestFilter implements ContainerRequestFilter {

	private static final Logger log = LogManager.getLogger(AuthContainerRequestFilter.class);
	private static final List<String> noTokenRequests = new LinkedList<>();
	
	static {
		noTokenRequests.add("user/register");
		noTokenRequests.add("user/login");
		noTokenRequests.add("swagger.json");
	}
	
	@Context
	private HttpServletRequest httpRequest;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		UriInfo uriInfo = requestContext.getUriInfo();
        String currentPath = uriInfo.getRequestUri().toString();
		log.debug("filter invoked for: [" + httpRequest.getMethod() + "] " + currentPath);

		String tokenValue = httpRequest.getHeader("token");
		log.debug("token: " + tokenValue);
		
		if (tokenValue != null && SessionManager.verifySession(tokenValue)) {
			return; // to resource
		}
		if (!tokenNeededForRequest(currentPath)) {
			return; // to resource
		}
		
		
		log.debug("invalid token found --> returning 401");
		requestContext.abortWith(RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED));
	}
	
	private boolean tokenNeededForRequest(String requestUri) {
		for (String s : noTokenRequests) {
			if (requestUri.contains(s)) {
				log.debug("no token needed for this request");
				return false;
			}
		}
		return true;
	}

}

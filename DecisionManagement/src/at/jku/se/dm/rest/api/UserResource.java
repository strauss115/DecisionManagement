package at.jku.se.dm.rest.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

import at.jku.se.dm.data.SampleObjectProvider;
import at.jku.se.dm.rest.HttpCode;
import at.jku.se.dm.rest.RestResponse;
import at.jku.se.dm.rest.SessionManager;
import at.jku.se.dm.rest.pojos.Token;
import at.jku.se.dm.rest.pojos.User;

@Path("/user")
public class UserResource {

	private static final Logger log = LogManager.getLogger(UserResource.class);
	
	// not needed so far
	// @Context
	// UriInfo ui;
	
	// ------------------------------------------------------------------------
	
	public UserResource() {
		
	}
	
	// ------------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		log.debug("GET all users");
		
		List<User> users = SampleObjectProvider.getAllUsers();
		log.info("GET all users returning '" + users.size() + "' elements");
		
		return RestResponse.getSuccessResponse(users);
	}
	
	@GET
	@Path("/{mail}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("mail") String eMail) {
		log.debug("GET user '" + eMail + "'");
		
		User u = SampleObjectProvider.getUserByEMail(eMail);
		if (u != null) {
			log.info("GET user returning '" + u.getEMail() + "'");
			return RestResponse.getSuccessResponse(u);
		} else {
			return RestResponse.getResponse(HttpCode.HTTP_204_NO_CONTENT);
		}
	}
	
	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("eMail") String eMail,
						  @QueryParam("password") String password) {
		log.debug("Login user: " + eMail);
		
		List<User> users = SampleObjectProvider.getAllUsers();
		for (User u : users) {
			if (u.getEMail().equals(eMail) && u.getPassword().equals(password)) {
				log.debug("Login of user '" + u.getEMail() + "' successful");
				Token token = new Token(SessionManager.addSession(u));
				log.info("Returing session '" + token.getSession() + "'");
				return RestResponse.getSuccessResponse(token);
			}
		}
		log.info("Unable to authorize user '" + eMail + "'");
		return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log.debug("CREATE user: " + json);
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@QueryParam("firstName") String firstName,
							 @QueryParam("lastName") String lastName,
							 @QueryParam("password") String password,
							 @QueryParam("eMail") String eMail) {
		log.debug("Register '" + eMail + "'");
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@PUT
	@Path("/{mail}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(@PathParam("eMail") String mail, String json) {
		log.debug("EDIT user '" + mail + "': " + json);
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@DELETE
	@Path("/{mail}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("eMail") String mail) {
		log.debug("DELETE user '" + mail + "'");
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	@PUT
	@Path("/{mail}/setPermisssion/{admin}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPermission(@PathParam("mail") String mail,
								  @PathParam("admin") boolean admin) {
		log.debug("Set admin user '" + mail + "' to '" + admin + "'");
		
		return RestResponse.getResponse(HttpCode.HTTP_501_NOT_IMPLEMENTED);
	}
	
	// TODO Upload user picture
	
}

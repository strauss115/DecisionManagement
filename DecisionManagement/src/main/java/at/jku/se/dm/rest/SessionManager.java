package at.jku.se.dm.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.dm.rest.pojos.User;

public class SessionManager {
	
	private static final Logger log = LogManager.getLogger(SessionManager.class);
	
	private static HashMap<String, User> validSessions = new HashMap<String, User>();
	private static SecureRandom random = new SecureRandom();	
	
	// ------------------------------------------------------------------------

	public static void addSession(String session, User user) {
		log.info("Creating session '" + session + "' for user + '" + user + "'");
		validSessions.put(session, user);
	}
	
	public static String addSession(User user) {
		String session = new BigInteger(130, random).toString(32);
		log.info("Creating session '" + session + "' for user + '" + user + "'");
		validSessions.put(session, user);
		return session;
	}
	
	// TODO remove session and implement timeout
	
	public static boolean verifySession(String sesssion) {
		return validSessions.containsKey(sesssion);
	}
	
}

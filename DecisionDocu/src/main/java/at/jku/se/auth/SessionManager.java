package at.jku.se.auth;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.database.DBService;
import at.jku.se.model.User;

public class SessionManager {

	private static SecureRandom random = new SecureRandom();
	private static final Logger log = LogManager.getLogger(SessionManager.class);
	private static HashMap<String, Session> validSessions = new HashMap<String, Session>();

	static {
		// adding a 12 hours valid super session for testing purposes
		Session supersession = new Session(DBService.getUserByEmail("admin@example.com"));
		supersession.setExpiredate(DateUtils.addHours(new Date(), 12)); 
		validSessions.put("1", supersession);
	}

	// ------------------------------------------------------------------------

	public static String addSession(User user) {
		Session session = new Session(user);
		String token = new BigInteger(130, random).toString(32);
		validSessions.put(token, session);
		removeExpiredTokens();
		log.debug(
				"Adding Token: '" + token + "' (expires: " + session.getExpiredate() + ") for user " + user.getEmail());
		log.debug("Number of valid sessions: " + validSessions.size());
		return token;
	}

	// TODO remove session and implement timeout

	private static void removeExpiredTokens() {
		for (Iterator<String> iterator = validSessions.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			try {
				if (validSessions.get(key).getExpiredate().before(new Date())) {
					log.debug("Removing session: " + validSessions.get(key).toString());
					iterator.remove();
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	public static boolean verifySession(String session) {
		if (validSessions.containsKey(session)) {
			try {
				return validSessions.get(session).getExpiredate().after(new Date());
			} catch (Exception e) {
				log.error(e);
				return true;// Expire date not set
			}
		}
		return false;
	}

	public static User getUser(String token) {
		log.debug("Getting user by token '" + token + "'");
		return validSessions.get(token).getUser();
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(new BigInteger(130, random).toString(32));
		}
	}
}

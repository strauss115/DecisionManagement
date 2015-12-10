package at.jku.se.auth;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.database.DBService;
import at.jku.se.model.User;

public class SessionManager {
	
	private static SecureRandom random = new SecureRandom();
	private static final Logger log = LogManager.getLogger(SessionManager.class);
	private static HashMap<String, Session> validSessions = new HashMap<String, Session>();
	
	static{
		Session supersession = new Session(DBService.getUserByEmail("admin@example.com"));
		supersession.setExpiredate(null);
		validSessions.put("g0up9ej1egkmrtveig59ke0adf",supersession);
	}
	
	// ------------------------------------------------------------------------
	
	public static String addSession(User user) {
		Session session = new Session(user);
		String token = new BigInteger(130, random).toString(32);
		validSessions.put(token, session);
		removeExpiredTokens();
		log.debug("Adding Session: " + session);
		return token;
	}
	
	// TODO remove session and implement timeout
	
	private static void removeExpiredTokens() {
		for (String key:validSessions.keySet()){
			try{
				if(validSessions.get(key).getExpiredate().before(new Date())){
					validSessions.remove(key);
				}
			}catch (Exception e){}//Expire date not set
		}
		
	}

	public static boolean verifySession(String session) {
		if(validSessions.containsKey(session)){
			try{
				return validSessions.get(session).getExpiredate().after(new Date());
			}catch (Exception e){
				return true;//Expire date not set
			}
		}
		return false;
	}
	
	public static User getUser(String token){
		return validSessions.get(token).getUser();
	}
	
	public static void main (String[]args){
		for(int i = 0; i<10; i++){
			System.out.println(new BigInteger(130, random).toString(32));
		}
	}	
}

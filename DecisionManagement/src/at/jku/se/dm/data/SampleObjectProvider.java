package at.jku.se.dm.data;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import at.jku.se.dm.rest.SessionManager;
import at.jku.se.dm.rest.pojos.*;

/**
 * Sample object provider for providing POJO sample data
 */
public class SampleObjectProvider {

	private static final Logger log = LogManager.getLogger(SampleObjectProvider.class);
	private static final String DEFAULT_PASSWORD = "password";

	private static List<Group> groups = new LinkedList<Group>();
	private static List<User> users = new LinkedList<User>();
	private static List<Team> teams = new LinkedList<Team>();
	private static List<Decision> decisions = new LinkedList<Decision>();

	// ------------------------------------------------------------------------

	static {
		User u1 = new User("user1@u1.com", "U1first", "U1last", DEFAULT_PASSWORD);
		User u2 = new User("user2@u2.com", "U2first", "U2last", DEFAULT_PASSWORD);
		User u3 = new User("user2@u3.com", "u3first", "u3last", DEFAULT_PASSWORD);
		User u4 = new User("user2@u4.com", "u4first", "u4last", DEFAULT_PASSWORD);

		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		
		// --
		
		SessionManager.addSession("1", u1);
		SessionManager.addSession("2", u2);

		// --

		Team t1 = new Team("T1", "Team 1");
		Team t2 = new Team("T2", "Team 2");

		teams.add(t1);
		teams.add(t2);

		t1.addUser(u1);
		u1.addToTeam(t1);

		t1.addUser(u2);
		u2.addToTeam(t1);

		t2.addUser(u3);
		u3.addToTeam(t2);

		t2.addUser(u4);
		u4.addToTeam(t2);

		// --

		Decision d1 = new Decision("D1", "Decision 1 Team 1", u1, t1);
		Decision d2 = new Decision("D2", "Decision 2 Team 1", u1, t1);
		Decision d3 = new Decision("D3", "Decision 3 Team 1", u1, t1);
		Decision d4 = new Decision("D4", "Decision 4 Team 1", u1, t1);

		Decision d5 = new Decision("D5", "Decision 5 Team 2", u3, t2);
		Decision d6 = new Decision("D6", "Decision 5 Team 2", u3, t2);

		decisions.add(d1);
		decisions.add(d2);
		decisions.add(d3);
		decisions.add(d4);
		decisions.add(d5);
		decisions.add(d6);

		// --

		Rationale r1 = new Rationale("RA1", "rationale 1", "rationale 1 description");
		Rationale r2 = new Rationale("RA2", "rationale 2", "rationale 2 description");
		Rationale r3 = new Rationale("RA4", "rationale 3", "rationale 3 description");
		
		d1.addRationale(r1);
		d1.addRationale(r2);
		d2.addRationale(r3);
		
		// --
		
		Alternative a1 = new Alternative("ALT1", "Alternative 1", "Alternative 1 description");
		Alternative a2 = new Alternative("ALT1", "Alternative 2", "Alternative 2 description");
		Alternative a3 = new Alternative("ALT1", "Alternative 3", "Alternative 3 description");
		
		d1.addAlternative(a1);
		d2.addAlternative(a2);
		d5.addAlternative(a3);
		
		// --
		
		Group g1 = new Group("Decision Group 1", t1);
		Group g2 = new Group("Decision Group 2", t1); // Empty group
		Group g3 = new Group("Decision Group 3", t2);

		groups.add(g1);
		groups.add(g2);
		groups.add(g3);

		g1.addDecision(d1);
		d1.setGroup(g1);

		g1.addDecision(d2);
		d2.setGroup(g1);

		g3.addDecision(d5);
		d5.setGroup(g3);

		// --
	}

	// ------------------------------------------------------------------------

	public static List<User> getAllUsers() {
		log.debug("Get all users returning '" + users.size() + "' elements");
		return users;
	}

	public static User getUserByEMail(String eMail) {
		for (User u : users) {
			if (u.getEMail().equals(eMail)) {
				log.debug("User '" + eMail + "' found by mail.");
				return u;
			}
		}
		log.warn("User '" + eMail + "' not found. RetueMailrning null.");
		return null;
	}

	// ------------------------------------------------------------------------

	public static List<Team> getAllTeams() {
		log.debug("Get all teams returning '" + teams.size() + "' elements");
		return teams;
	}

	public static Team getTeamByName(String name) {
		for (Team t : teams) {
			if (t.getName().equals(name)) {
				log.debug("Team '" + name + "' found by name.");
				return t;
			}
		}
		log.warn("Team '" + name + "' not found. Returning null.");
		return null;
	}

	// ------------------------------------------------------------------------

	public static List<Decision> getAllDecisions() {
		log.debug("Get all decisions returning '" + decisions.size() + "' elements");
		return decisions;
	}

	public static Decision getDecisionByName(String name) {
		for (Decision d : decisions) {
			if (d.getName().equals(name)) {
				log.debug("Decision '" + name + "' found by name.");
				return d;
			}
		}
		log.warn("Decision '" + name + "' not found. Returning null.");
		return null;
	}
	
	public static List<Decision> getDecisionsByTeamName(String teamName) {
		log.debug("Get decisions by team name '" + teamName + "'");
		List<Decision> result = new LinkedList<Decision>();
		
		for (Decision d : decisions) {
			if (d.getTeam().equals(teamName))
				result.add(d);
		}
		
		log.debug("'" + decisions.size() + "' found by team name.");
		return result;		
	}

	// ------------------------------------------------------------------------

	public static List<Group> getAllGroups() {
		log.debug("Get all groups returning '" + groups.size() + "' elements");
		return groups;
	}

	public static Group getGroupByName(String name) {
		for (Group g : groups) {
			if (g.getName().equals(name)) {
				log.debug("Group '" + name + "' found by name.");
				return g;
			}
		}
		log.warn("Group '" + name + "' not found. Returning null.");
		return null;
	}

	// ------------------------------------------------------------------------

}

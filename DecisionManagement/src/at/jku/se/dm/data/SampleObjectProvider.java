package at.jku.se.dm.data;

import java.util.Date;
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
	private static List<Chat> chats = new LinkedList<Chat>();

	// ------------------------------------------------------------------------

	static {
		User u1 = new User("peter.aichinger@jku.at", "Peter", "Aichinger", DEFAULT_PASSWORD);
		User u2 = new User("thomas.hochgatterer@jku.at", "Thomas", "Hochgatterer", DEFAULT_PASSWORD);
		User u3 = new User("alexey.pastuschenko@jku.at", "Alexey", "Pastuschenko", DEFAULT_PASSWORD);
		User u4 = new User("michael.strauss@jku.at", "Michael", "Strauﬂ", DEFAULT_PASSWORD);
		/// Profile picture url not null!
		u1.setUrlProfilePicture("img/userImg.png");
		u2.setUrlProfilePicture("https://scontent-vie1-1.xx.fbcdn.net/hphotos-xfa1/v/t1.0-9/644391_10204671569381054_8656277940229938364_n.jpg?oh=4a3387f74b111e096aa744ee576cd267&oe=56DC816F");
		u3.setUrlProfilePicture("https://scontent-vie1-1.xx.fbcdn.net/hprofile-xat1/v/t1.0-1/c0.22.160.160/p160x160/12106828_458755630970683_8909953079279626491_n.jpg?oh=340500360d79cc54f464707f3a8861fd&oe=56DBEE32");
		u4.setUrlProfilePicture("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/c33.33.412.412/s160x160/1933927_1229594150156_8079993_n.jpg?oh=352d29018914cf8f04a9c440de75e754&oe=56B23BCB&__gda__=1454364973_744dad36852b796b7f6987853a46625c");
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
		
		Chat c1 = new Chat("D1", "Decision 1 chat message 1", u1, new Date());
		Chat c2 = new Chat("D1", "Decision 1 chat message 2", u2, new Date());
		Chat c3 = new Chat("D2", "Decision 2 chat message 1", u1, new Date());
		Chat c4 = new Chat("D1", "Decision 1 chat message 3", u1, new Date());
		
		chats.add(c1);
		chats.add(c2);
		chats.add(c3);
		chats.add(c4);
		
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

	public static List<Chat> getAllChats() {
		log.debug("Get all chats returning '" + chats.size() + "' elements");
		return chats;
	}
	
	public static List<Chat> getChatsByRelatedObject(String relatedObjectId) {
		log.debug("Get chats by related object id '" + relatedObjectId + "'");
		List<Chat> result = new LinkedList<Chat>();
		
		for (Chat c : chats) {
			if (c.getRelatedObjectId().equals(relatedObjectId))
					result.add(c);
		}
		
		log.debug("'" + result.size() + "' found by related object id.");
		return result;
	}
	
	public static void addChat(Chat c) {
		log.debug("Adding chat '" + c.getMessage() + "' to list");
		chats.add(c);
	}
	
	// ------------------------------------------------------------------------
	
}

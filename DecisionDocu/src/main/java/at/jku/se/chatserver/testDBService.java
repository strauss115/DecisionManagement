package at.jku.se.chatserver;

import at.jku.se.database.DBService;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.User;

public class testDBService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		System.out.println("Testklasse für Zugriff auf DB");
		
		User admin = DBService.getUserByEmail("admin@example.com");
		System.out.println(admin);
		DBService.getAllDecisions(admin).toArray(new NodeInterface[0]);
		
		System.out.println(DBService.getAllDecisions(admin).toArray(new NodeInterface[0]));
		
		for (NodeInterface dec: DBService.getAllDecisions(admin).toArray(new NodeInterface[0]) ) {
			System.out.println(dec);
		}
			
	}

}

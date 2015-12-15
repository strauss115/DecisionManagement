package at.jku.se.chatserver;

import at.jku.se.database.DBService;
import at.jku.se.model.Node;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.User;
import at.jku.se.model.Decision;
import at.jku.se.model.Message;

public class testDBService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		System.out.println("Testklasse für Zugriff auf DB");
		
		User admin = DBService.getUserByEmail("admin@example.com");
		System.out.println(admin);
		DBService.getAllDecisions(admin).toArray(new NodeInterface[0]);
		
		//System.out.println(DBService.getAllDecisions(admin).toArray(new NodeInterface[0]));
		
		//for (NodeInterface dec: DBService.getAllDecisions(admin).toArray(new NodeInterface[0]) ) {
		//	System.out.println(dec);
		//}
		
		Message m1 = new Message("Chatnachricht 5");
		//Message m2 = new Message("Chatnachricht 2");
		
		Node node = DBService.getNodeByID(Decision.class, 5884, admin, 2);

		//node.addRelation("Message", m1, true);
		DBService.createMessage("Chatnachricht 7", node.getId(), admin.getId());
		
		//DBService.addRelationship(node.getId(), "Message", m1.getId());
		DBService.updateNodeWihtRelationships(node, admin.getId());
		
		System.out.println(node);
	}

}

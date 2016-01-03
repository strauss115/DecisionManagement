

import java.util.List;
import java.util.Map;

import at.jku.se.database.DBService;
import at.jku.se.model.Node;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;
import at.jku.se.model.Decision;
import at.jku.se.model.Message;

public class testDBService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		System.out.println("Testklasse für Zugriff auf DB");
		
		User admin = DBService.getUserByEmail("admin@example.com");
		//System.out.println(admin);
		DBService.getAllDecisions(admin).toArray(new NodeInterface[0]);
		
		// Alle Entscheidungen ausgeben
		//System.out.println(DBService.getAllDecisions(admin).toArray(new NodeInterface[0]));
		//for (NodeInterface dec: DBService.getAllDecisions(admin).toArray(new NodeInterface[0]) ) {
		//	System.out.println(dec);
		//}
		
		// Chatnachricht speichern:
		//Node node = DBService.getNodeByID(Decision.class, 5884, admin, 2);
		//DBService.createMessage("Chatnachricht 10", node.getId(), admin.getId());
		//System.out.println(node);
		
		// Chatverlauf auslesen:
		Node node = DBService.getNodeByID(Decision.class, 5884, admin, 2);
		Map<String, List<RelationshipInterface>> rs = node.getRelationships();
		for (RelationshipInterface m : rs.get("message")) {
			System.out.println(m.getRelatedNode().getName());
		}
	}

}

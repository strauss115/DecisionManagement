import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.dm.shared.NodeToRelationMap;

public class NodeToRelation {

	public static void main (String[]args){
		
		try {
			System.out.println(Class.forName("at.jku.se.model."+"Decision"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*ObjectMapper mapper = new ObjectMapper();
		
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(NodeToRelationMap.NodeToRelationMap));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}

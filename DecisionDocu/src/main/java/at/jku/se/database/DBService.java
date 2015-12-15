package at.jku.se.database;

import java.lang.reflect.Constructor;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.neo4j.jdbc.Driver;
import org.neo4j.jdbc.internal.Neo4jConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.strings.NodeString;
import at.jku.se.database.strings.PropertyString;
import at.jku.se.database.strings.RelationString;
import at.jku.se.model.Alternative;
import at.jku.se.model.Message;
import at.jku.se.model.CustomDate;
import at.jku.se.model.Decision;
import at.jku.se.model.DecisionGroup;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.Node;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Project;
import at.jku.se.model.Property;
import at.jku.se.model.QualityAttribute;
import at.jku.se.model.Relationship;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;

@SuppressWarnings("unchecked")
public class DBService {
	
	private static final String CONNECT_STRING = "jdbc:neo4j://ubuntu.mayerb.net:7474/";
	private static Properties properties = new Properties();
	private static Neo4jConnection con;
	private static ObjectMapper mapper = new ObjectMapper();
	private static HashMap<String, Constructor<? extends NodeInterface>> constructors = new HashMap<String, Constructor<? extends NodeInterface>>();
	
	static {
		properties.setProperty("user", "neo4j");
		properties.setProperty("password", "neose");
		
		try{
			Class[] type = {};
			constructors.put(NodeString.DECISION, Decision.class.getConstructor(type));
			constructors.put(NodeString.PROJECT, Project.class.getConstructor(type));
			constructors.put(NodeString.USER, User.class.getConstructor(type));
			constructors.put(NodeString.INFLUENCEFACTOR, InfluenceFactor.class.getConstructor(type));
			constructors.put(NodeString.QUALITYATTRIBUTE, QualityAttribute.class.getConstructor(type));
			constructors.put(NodeString.DECISIONGROUP, DecisionGroup.class.getConstructor(type));
			constructors.put(NodeString.ALTERNATIVE, Alternative.class.getConstructor(type));
			constructors.put(NodeString.PROPERTY, Property.class.getConstructor(type));
			constructors.put(NodeString.MESSAGE, Message.class.getConstructor(type));
		}catch (Exception e){}
	}
	
	private static DBService ref;
	
	private DBService() {
	}
	
	public static DBService getDBService() {
		if (ref == null) {
			ref = new DBService();
		}
		return ref;
	}
	
	private Neo4jConnection getConnection(){
		if(con==null){
			try {
				con = new Driver().connect(CONNECT_STRING, properties);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return con;
	}
	public static List<Decision> getAllDecisions (User user){
		return getAllNodes(Decision.class, user, 2);
	}
	
	public static User getUserByEmail(String email){
		try{
		return getAllNodes(User.class,0,"{email: \""+email+"\"}",null).get(0);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<User> getAllUser() {
		try{
			return getAllNodes(User.class,1);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Decision> getAllDecisionsOfProject(long projectid, User user){
		try{
			Project project = getAllNodes(Project.class, user, 1, 3, null," id(n1)="+projectid+" ").get(0);
			List<Decision> decisions = new ArrayList<Decision>();
			for(RelationshipInterface rel:project.getRelationships().get(RelationString.HASDECISION)){
				try{
					decisions.add((Decision) rel.getRelatedNode());
				}catch (Exception e){}
			}
			return decisions;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Project> getAllProjects(){
		try{
			return getAllNodes(Project.class,2);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Project> getAllProjectsOfUser(User user){
		try{
			return getAllNodes(Project.class,user, 1, 2, null, null);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T extends NodeInterface> T getNodeByID(Class<T> type, long nodeid, User user, int level){
		try{
			
			List<T> nodes = getAllNodes(type, user, level, null,"id(n1)="+nodeid+" ");
			return nodes.get(0);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T extends NodeInterface> T getNodeByID(Class<T> type, long nodeid, int level){
		try{
			
			List<T> nodes = getAllNodes(type, level, null,"id(n1)="+nodeid+" ");
			return nodes.get(0);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns all nodes of a spesific class, where user has access
	 * @param type type of root nodes
	 * @param userid id of the user from session
	 * @param level number of levels from the root node, 0 returns just the node without reletionships,
	 * 1 returns node with the ids of relationships, 2 returns related nodes and the ids of their relationship ... max 5
	 * @return
	 */
	private static <T extends NodeInterface> List<T> getAllNodes(Class<T> type, User user, int level){
		return getAllNodes(type, user, level, null, null);
	}
	
	/**
	 * getAllNodes with full Permission
	 * @param type
	 * @param level
	 * @return
	 */
	private static <T extends NodeInterface> List<T> getAllNodes(Class<T> type, int level){
		return getAllNodes(type, level, null, null);
	}
	
	private static <T extends NodeInterface> List<T> getAllNodes(Class<T> type, int level,
		String attributfilter, String wherefilter){
		String query = getQuery(type, -1, level, attributfilter, wherefilter);
		List<T> nodes = getNodesFromQuery(query);
		
		return (List<T>) nodes;
	}
	
	private static <T extends NodeInterface> List<T> getAllNodes(Class<T> type, User user, int level, 
			String attributfilter, String wherefilter){
		return getAllNodes(type, user, -1, level,attributfilter,wherefilter);
		
	}
	
	private static <T extends NodeInterface> List<T> getAllNodes(Class<T> type, User user, int autorizationlevel, int level, 
			String attributfilter, String wherefilter){
		if(user.isAdmin()){
			return getAllNodes(type, level, attributfilter, wherefilter);
		}
		String query = getQuery(type, user.getId(), autorizationlevel, level, attributfilter, wherefilter);
		List<T> nodes = getNodesFromQuery(query);
		
		return (List<T>) nodes;
		
	}
	
	private static <T extends NodeInterface> List<T> getNodesFromQuery(String query) {
		ResultSet rs = null;
		try{
			rs = executeQuery(query);
			HashMap<Long,NodeInterface> uniqueNodes = new HashMap<Long,NodeInterface>();
			HashMap<Long,Map<String,List<RelationshipInterface>>> relations = new HashMap<Long, Map<String,List<RelationshipInterface>>>();
			HashSet<Long> set = new HashSet<Long>();
			List<T> nodes = new ArrayList<T>();
			boolean isRootlevelNode = true;
			while (isRootlevelNode&&rs.next()){
				if(rs.getString("RelId")==null){
					NodeInterface rootnode = getNodeFromStrings(rs.getLong(4),rs.getString(5),rs.getString(6));
					nodes.add((T)rootnode);
					uniqueNodes.put(rootnode.getId(), rootnode);
				}else{
					isRootlevelNode = false;
				}
			}
			if(!isRootlevelNode){
			do{
				NodeInterface fullnode=null;
				//if(rs.getString(4)!=null){
					if(!uniqueNodes.containsKey(rs.getLong(4))){
						fullnode = getNodeFromStrings(rs.getLong(4),rs.getString(5),rs.getString(6));
						uniqueNodes.put(fullnode.getId(), fullnode);
					}else{
						fullnode=uniqueNodes.get(rs.getLong(4));
					}
				/*}else{
					
				}*/
					
				Relationship rel = new Relationship(rs.getLong(2),fullnode,true);
				String reltyp = rs.getString(3);
				long relnode = rs.getLong(1);
				if(!set.contains(rel.getId())){
					set.add(rel.getId());
				
					if(relations.containsKey(relnode)){
						Map<String,List<RelationshipInterface>> relmap = relations.get(relnode);
						if(relmap.containsKey(reltyp)){
							relmap.get(reltyp).add(rel);
						}else{
							List<RelationshipInterface> rellist = new ArrayList<RelationshipInterface>();
							rellist.add(rel);
							relmap.put(reltyp, rellist);
						}
					}else{
						List<RelationshipInterface> rellist = new ArrayList<RelationshipInterface>();
						rellist.add(rel);
						Map<String,List<RelationshipInterface>> relmap= new HashMap<String,List<RelationshipInterface>>();
						relmap.put(reltyp, rellist);
						relations.put(relnode, relmap);
					}
				}
				
			}while (rs.next());
			}
			
			for(long nodeid:relations.keySet()){
				try{
					uniqueNodes.get(nodeid).setRelationships(relations.get(nodeid));
				}catch (Exception e){
					//System.out.println(nodeid);
					e.printStackTrace();
				}
			}
			rs.close();
			return nodes;
		}catch (Exception e){
			try {
				rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}

	private static NodeInterface getNodeFromStrings(long nodeId, String nodeLable, String directProps) {
		try{
			String[] nodeTypes = mapper.readValue(nodeLable, String[].class);
			String nodeType=nodeTypes[0];
			
			Constructor<? extends NodeInterface> constructor = constructors.get(nodeType);
			if(constructor==null){
				constructor = constructors.get(NodeString.PROPERTY);
			}
			NodeInterface node = constructor.newInstance();
			node.setId(nodeId);
			
			if(directProps!=null){
				Map<String, Object> directProperties = getDirectPropertiesFromJson(directProps);
				if(directProperties.containsKey(PropertyString.NAME)){
					node.setName(directProperties.get(PropertyString.NAME).toString());
					directProperties.remove(PropertyString.NAME);
				}
				try{
					if(directProperties.containsKey(PropertyString.CREATIONDATE)){
						node.setCreationDate(new CustomDate((long)directProperties.get(PropertyString.CREATIONDATE)));
						directProperties.remove(PropertyString.CREATIONDATE);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				for(String string:directProperties.keySet()){
					node.addDirectProperty(string, directProperties.get(string).toString());
				}
			}
			return node;
		
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, Object> getDirectPropertiesFromJson(String directProps) {
		try{
			Map<String, Object> map;
			map =mapper.readValue(directProps, HashMap.class);
			return map;
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getQuery (Class<?extends NodeInterface> type, long userid, int level,
			String afilter, String wfilter){
		return getQuery(type, userid, -1, level, afilter, wfilter);
	}

	private static String getQuery (Class<?extends NodeInterface> type, long userid, int autorizationlevel, int level,
			String afilter, String wfilter){
		/*
		    Match (user:User)-[*]->(n1:Decision)
			Where id(user)=5521
			Return null as RelNodeId, null as RelId, null as Reltype, id(n1)as NodeId, labels(n1)as NodeLable, n1 as Node
			Union
			Match (n1)-[r1]->(n2)
			Return id(n1)as RelNodeId, id(r1)as RelId, type(r1)as Reltype, id(n2)as NodeId, labels(n2)as NodeLable, n2 as Node
			Union
			Match (n2)-[r2]->(n3)
			Return id(n2)as RelNodeId, id(r2)as RelId, type(r2)as Reltype, id(n3)as NodeId, labels(n3)as NodeLable, null as Node
		 */
		String nodetype = type.getSimpleName();
		if(autorizationlevel<1){
			autorizationlevel = level;
		}
		String usercontrol = "", where = "";
		if(userid>0){
			usercontrol = " (user:User)-[*0.."+autorizationlevel+"]-> ";
			where = " Where id(user)="+userid+" ";
		}
		if(afilter==null||afilter.length()==0){
			afilter="";
		}
		if(wfilter==null||wfilter.length()==0){
			wfilter="";
		}else{
			if(where.length()>0){
				where=where+" AND "+wfilter;
			}else{
				where="Where "+wfilter;
			}
		}
		StringBuilder query = new StringBuilder();
		StringBuilder match = new StringBuilder();
		query.append("Match ").append(usercontrol).append("(n1");
		if(!nodetype.equals(NodeInterface.class.getSimpleName())){
		query.append(":").append(nodetype);
		}
		query.append(afilter).append(")");
		match.append(query.toString());
		query.append(where)
		.append("Return null as RelNodeId, null as RelId, null as Reltype, id(n1)as NodeId, labels(n1)as NodeLable, n1 as Node");
		
		for(int i=2; i<=level; i++){
			match.append("-[r").append(i-1).append("]->(n").append(i).append(")");
			query.append(" Union ").append(match.toString()).append(where)
				.append(" Return id(n").append(i-1).append(")as RelNodeId, id(r")
				.append(i-1).append(")as RelId, type(r").append(i-1).append(")as Reltype, id(n").append(i)
				.append(")as NodeId, labels(n").append(i).append(")as NodeLable, n").append(i).append(" as Node");
		}
		if(level>0){
			query.append(" Union ").append(match.toString()).append("-[r").append(level).append("]->(n").append(level+1).append(")").append(where)
			.append(" Return id(n").append(level).append(")as RelNodeId, id(r").append(level).append(")as RelId, type(r")
			.append(level).append(")as Reltype, id(n").append(level+1).append(")as NodeId, labels(n").append(level+1)
			.append(")as NodeLable, null as Node");
		}
		return query.toString();
	}
	
	/**
	 * Adds Relationship to existing nodes, if relationship already exist does nothing
	 * @param startnode
	 * @param type
	 * @param endnode
	 * @return id of created or existing Relationship, if error -1
	 */
	public static long addRelationship(long startnode, String type, long endnode){
		if(!(startnode>0&&endnode>0&&type!=null&&type.length()>0)){
			return -1;
		}
		String query = "Match (n1), (n2) Where id(n1)="+startnode+" AND id(n2)="+endnode+
				" Create Unique (n1)-[r:"+type+"]->(n2) Return id(r)";
		ResultSet rs = null;
		try {
			rs = executeQuery(query);
			rs.next();
			long relid= rs.getLong(1);
			rs.close();
			return relid;
		} catch (Exception e) {
			try {
				rs.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public static boolean deleteReltionship(long relationshipid){
		if(relationshipid<1){
			return false;
		}
		String query = "Match ()-[r]-() Where id(r)="+relationshipid+" Delete r return id(r)";
		ResultSet rs = null;
		try {
			rs = executeQuery(query);
			rs.next();
			boolean deleted = rs.getLong(1)>0;
			rs.close();
			return deleted;
		} catch (Exception e) {
			try {
				rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	public static <T extends NodeInterface> T createRelationshipWithNode(T node, String type, long startNode, long creatorid){
		node = updateNode(node, creatorid);
		if(node==null||node.getId()<1){
			return null;
		}
		long relid = addRelationship(startNode,type,node.getId());
		if(relid<1){
			return null;
		}
		return node;	
	}
	
	public static Message createMessage(String message, long relatedNode, long creatorid){
		Message messageNode = new Message(message);
		messageNode = createRelationshipWithNode(messageNode, RelationString.Message, relatedNode, creatorid);
		return messageNode;
	}
	
	
	
	/**
	 * If node has id the properties of the node will be updated, otherwise the node will be created
	 * @param node
	 * @param creatorid
	 * @return the created or updated node
	 */
	public static <T extends NodeInterface> T updateNode(T node, long creatorid){
		if(node==null){
			return null;
		}
		StringBuilder query;
		if(node.getId()>0){
			query = new StringBuilder(getUpdateExistingNodeQuery(node));
		}else{
			query = new StringBuilder(getCreateNewNodeQuery(node));
		}
		query.append(" Return id(n1), labels(n1), n1");
		
		ResultSet rs = null;
		try {
			rs = executeQuery(query.toString());
			rs.next();
			T dbnode =(T)getNodeFromStrings(rs.getLong(1),rs.getString(2),rs.getString(3));
			node.setId(dbnode.getId());
			node.setCreationDate(dbnode.getCreationDate());
			rs.close();
		} catch (Exception e1) {
			try {
				rs.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
			return null;
		}
		
		if(creatorid>0&&creatorid!=node.getId()){
			try{
				User creator = getNodeByID(User.class,creatorid,0);
				long relid = addRelationship(node.getId(),RelationString.CREATOR,creator.getId());
				node.addRelation(RelationString.CREATOR, new Relationship(relid,creator,true));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return node;
	}
	
	public static <T extends NodeInterface> T updateNodeWihtRelationships(T node, long creatorid){
		HashSet<T> uniqueNodes = new HashSet<T>();
		int amount = updateNodesRecursivly(node, creatorid, uniqueNodes);
		System.out.println(amount);
		return node;
	}
	
	private static <T extends NodeInterface> int updateNodesRecursivly(T node, long creatorid, HashSet<T> uniqueNodes) {
		if (uniqueNodes.contains(node)){
			return 0;
		}
		uniqueNodes.add(node);
		updateNode(node,creatorid);
		if(node.getRelationships()==null){
			return 1;
		}
		int nodecount = 1;
		for(String key:node.getRelationships().keySet()){
			for(RelationshipInterface rel:node.getRelationships().get(key)){
				try{
					nodecount+=updateNodesRecursivly((T) rel.getRelatedNode(),creatorid,uniqueNodes);
					addRelationship(node.getId(),key,rel.getRelatedNode().getId());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return nodecount;
	}

	public static boolean deleteNode (long nodeid){
		if(nodeid<1){
			return false;
		}
		String query = "Match (n) Where id(n)="+nodeid+ " Optional Match (n)-[r]-()"+
				" Delete n, r Return id(n)";
		System.out.println(query);
		ResultSet rs = null;
		try {
			rs = executeQuery(query);
			rs.next();
			boolean deleted = rs.getLong(1)>0;
			rs.close();
			return deleted;
		} catch (Exception e) {
			try {
				rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteNode (long nodeid, User user){
		System.out.println (nodeid);
		System.out.println(user);
		if(user==null){
			return false;
		}
		if(user.isAdmin()){
			System.out.println("Admin");
			return deleteNode(nodeid);
		}
		if(nodeid<1){
			return false;
		}
		//User must be creator
		String query = "Match (n)-[creator]->(u:User) Where id(n)="+nodeid+
				" AND id(u)="+user.getId()+ " Optional Match (n)-[r]-() Delete n, r Return id(n)";
		ResultSet rs = null;
		try {
			rs = executeQuery(query);
			rs.next();
			boolean deleted = rs.getLong(1)>0;
			rs.close();
			return deleted;
		} catch (Exception e) {
			try {
				rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	private static String getCreateNewNodeQuery(NodeInterface node) {
		StringBuilder builder = new StringBuilder("Create (n1:");
		builder.append(node.getNodeType());
		builder.append(singleNodePropertyClause(node)).append(")");
		return builder.toString();
	}

	private static String getUpdateExistingNodeQuery(NodeInterface node){
		StringBuilder builder = new StringBuilder("Match (n1) Where id(n1)=");
		builder.append(node.getId()).append(" set n1 += ");
		builder.append(singleNodePropertyClause(node));
		return builder.toString();
	}
	
	/*public static void updateNode(NodeInterface node, long userid, int level){
		String query = getCreateQuery (node, userid, level);	
	}
	private static String getCreateQuery(NodeInterface node, long userid, int level){
		if(node==null){
			return null;
		}
		HashMap<NodeInterface,Integer> uniqueNodeList = new HashMap<NodeInterface,Integer>();
		HashMap<RelationshipInterface,NodeInterface> uniqueRelList = new HashMap<RelationshipInterface,NodeInterface>();
		StringBuilder match = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder set = new StringBuilder();
		StringBuilder create = new StringBuilder();
		StringBuilder _return = new StringBuilder();
		Integer actid = new Integer(1);
		getCreateQueryRecursivly(node,userid,level,uniqueNodeList,match,where,set,create,_return);
		
		return null;
	}
	private static void getCreateQueryRecursivly(NodeInterface node, long userid, int level,
			HashMap<NodeInterface, Integer> uniqueNodeList, StringBuilder match, StringBuilder where, StringBuilder set,
			StringBuilder create, StringBuilder _return) {
		if(node.getId()>0){
			
		}
		
	}*/

	private static String singleNodePropertyClause(NodeInterface node){
		StringBuilder match = new StringBuilder("{");
		if(node.getName()!=null&&node.getName().length()>0){
			match.append("name:\"").append(node.getName()).append("\"").append(", ");
		}
		match.append("creationdate:");
		if(node.getCreationDate()!=null){
			match.append(node.getCreationDate().dateToLong());
		}else{
			match.append("TimeStamp()");
		}
		try{
			for(String prop:node.getAllDirectProperties().keySet()){
				match.append(", ").append(prop).append(":\"").append(node.getAllDirectProperties().get(prop)).append("\"");
			}
		}catch(Exception e){}//no direct properties
		
		match.append("}");
		return match.toString();
	}
	
	public static boolean addUserToProject(User user, long projectId, String projectPassword) {
		Project project = getNodeByID(Project.class,projectId,0);
		if(project==null||project.getPassword()==null||project.getPassword().length()<=0){
			return false;
		}
		if(!project.getPassword().equals(projectPassword)){
			return false;
		}
		try{
			return addRelationship(user.getId(),RelationString.HASPROJECT,projectId)>0;
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static ResultSet executeQuery(String query)throws Exception{
			//System.out.println(query);
			Statement stmt = getDBService().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			stmt.closeOnCompletion();
			return rs;
	}
	
	public static void main (String[] args){
		User admin = getUserByEmail("admin@example.com");
		/*User normal = getUserByEmail("user1@u3.com");
		List<Decision>decisions = getAllDecisions(admin);

		/*try {
			List<Decision> teamDecisions = getAllDecisionsOfProject(5526);
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(teamDecisions.toArray(new NodeInterface[0])));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		/*
		
		/*Add + Delete Relationships
		Decision dec = getNodeByID(Decision.class,5527,2);
		System.out.println(dec.getRelationships().get(RelationString.HASQUALITYATTRIBUTE).size());
		System.out.println(addRelationship(dec.getId(),RelationString.HASQUALITYATTRIBUTE,5559));//Alread Existing Relation
		dec = getNodeByID(Decision.class,5527,2);
		System.out.println("Amount Quality Attributes: "+dec.getRelationships().get(RelationString.HASQUALITYATTRIBUTE).size());
		long newrelid = addRelationship(dec.getId(),RelationString.HASQUALITYATTRIBUTE,5558);//new Rel
		dec = getNodeByID(Decision.class,5527,2);
		System.out.println("Amount Quality Attributes: "+dec.getRelationships().get(RelationString.HASQUALITYATTRIBUTE).size());
		System.out.println("Deleted Relationship:" +deleteReltionship(newrelid));
		dec = getNodeByID(Decision.class,5527,2);
		System.out.println("Amount Quality Attributes: "+dec.getRelationships().get(RelationString.HASQUALITYATTRIBUTE).size());*/
		
		/*Update+create+delete Node*/
		/*Decision dec = getNodeByID(Decision.class,5527,2);
		dec.setName("Extend System B");
		System.out.println(updateNode(dec,admin.getId()).getName());//Update Node
		Property newissue = new Property("new issue");
		System.out.println(updateNode(newissue,admin.getId()));//Create Node
		System.out.println("New node deleted: "+deleteNode(5572));*/
		
		/*Decision dec = new Decision("Decision 1");
		Property issue = new Property("Issue 1");
		dec.addRelation(RelationString.ISSUE, issue, true);
		Alternative alt = new Alternative ("Alternative 1");
		dec.addRelation(RelationString.ALTERNATIVE, alt, true);
		Property assumption = new Property ("Assumption 1");
		alt.addRelation(RelationString.ASSUMPTION, assumption, true);
		assumption.addRelation(RelationString.INFLUENCES, dec, true);
		System.out.println(dec);
		System.out.println(updateNodeWihtRelationships(dec, admin.getId()));*/
		
		/*try {
			String json = mapper.writeValueAsString(getAllDecisions(admin).toArray(new NodeInterface[0]));
			mapper.readValue(json, Decision[].class);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*NodeInterface node = getNodeByID(NodeInterface.class,5861,0);
		System.out.println(node);*/
		
		Message m1 = new Message("Chatnachricht 6");
		Node node = DBService.getNodeByID(Decision.class, 5884, admin, 2);
		//System.out.println(node);
		//node.addRelation("Message", m1, true);
		//DBService.updateNodeWihtRelationships(node, admin.getId());
		createMessage("Chatnachricht 7", node.getId(),admin.getId());
	}

}

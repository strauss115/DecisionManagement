package at.jku.se.chatserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.dm.shared.NodeToRelationMap;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.CustomDate;
import at.jku.se.model.Message;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Property;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;

public class ClientThread extends Thread {
	private DataInputStream is = null;
	PrintStream os = null;
	private ServerListener server = null;
	private Socket clientSocket = null;
	private String name;

	private static final Logger log = LogManager.getLogger(ClientThread.class);

	private NodeInterface node = null;
	private User user = null;
	
	private Map<String, String> nodeToRelation;
	

	public ClientThread(ServerListener server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
	}

	@SuppressWarnings("deprecation")
	public void run() {

		try {
			// create streams
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());

			MsgWrapper msg = new MsgWrapper();
			msg.setCreator("server");
			msg.setTimestamp(new CustomDate(System.currentTimeMillis()));

			while (true) {
				msg.setMessage("Retrieving Login Data...");
				System.out.println(msg.toString());
				os.println(msg.toString());
				name = is.readLine().trim();
				if (name.indexOf('?') == -1) {
					break;
				} else {
					msg.setMessage("The name should not contain '?' character.");
					os.println(msg.toString());
				}

				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (name.equals("/quit")) {
				throw new Exception("Name must not be /quit !");
			}

			String[] parts = name.split("@");

			if (parts.length >= 2) {
				log.debug("user: " + parts[0]);
				log.debug("node: " + parts[1]);
			}

			// Get the User Node Object via DBService
			user = SessionManager.getUser(parts[0]);

			// Get the Node Object via DBService
			node = DBService.getNodeByID(NodeInterface.class, Integer.parseInt(parts[1]), user, 3);
			
			nodeToRelation = NodeToRelationMap.NodeToRelationMap.get(node.getClass().getSimpleName());
			
			// System.out.println(node);

			msg.setMessage("Welcome " + user.getName() + " to '" + node.getName()
					+ "' chat room.\nTo leave enter /quit in a new line.");

			os.println(msg.toString());

			// Chatverlauf an diesen Client senden
			Map<String, List<RelationshipInterface>> rs = node.getRelationships();
			if (rs.get("message") != null) {
				for (RelationshipInterface m : rs.get(RelationString.HAS_MESSAGE)) {

					if (m.getRelatedNode() instanceof Message) {

						msg = new MsgWrapper((Message) m.getRelatedNode());
						if (msg != null) {
							log.debug("Attaching data: " + msg.toString());
							os.println(msg.toString());
						}
					}
				}
			}

			synchronized (this) {
				String message = "The user " + user.getName() + " entered the chat.";

				Message m = this.saveMessage(message, true);
				try {
					this.sendToOtherClients(new MsgWrapper(m).toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			//---------------------------------------------
			// --------------------- Start the conversation--------------
			//-----------------------------------------------------------
			while (true) {
				String line = is.readLine();
				if (line.startsWith("/quit")) {
					break;
				}
				try{
					checkLine(line);
				}catch(Exception e){
					sendError("Error handling the line");
				}

				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			synchronized (this) {
				String message = "The user " + user.getName() + " is leaving the chat room.";
				Message m = this.saveMessage(message, true);
				try {
					this.sendToOtherClients(new MsgWrapper(m).toString());
				} catch (Exception e1) {
					log.error(e1);
				}
			}
			// os.println("*** Bye " + user.getName() + " ***");

		} catch (IOException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
			String message = "The user " + (user == null ? "null" : user.getName()) + "("
					+ clientSocket.getInetAddress() + ") is leaving the chat room.";
			Message m = this.saveMessage(message, true);
			try {
				this.sendToOtherClients(new MsgWrapper(m).toString());
			} catch (Exception e1) {
				log.error(e1);
			}
		} finally {
			// close thread
			synchronized (this) {
				server.clientLeft(clientSocket, this);
			}
			// close streams
			try {
				is.close();
			} catch (IOException e) {
				log.error(e);
			}
			os.close();
			try {
				clientSocket.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
		log.debug("client thread beendet");
	}
	
	private void checkLine(String line){
		if(line==null||line.length()<5){
			sendNormalMessage(line);
			return;
		}
		if(!line.startsWith("#")){
			sendNormalMessage(line);
			return;
		}
		int delimeter = line.indexOf(' ');
		if(delimeter==-1){
			sendError("Creation tags need blank");
			return;
		}
		String relation = line.substring(1, delimeter);
		String nodename = line.substring(delimeter+1, line.length());
		
		if(relation.length()<5){
			sendError("Relation discription needs more than 4 characters");
		}
		
		if(nodename.length()<5){
			sendError("Node discription needs more than 4 characters");
		}
		
		if(NodeToRelationMap.RelationNeedsExistingNode.containsKey(relation)){
			//Well known Relations
			try{
				if(!nodeToRelation.containsKey(relation)){
					throw new Exception("not allowed");
				}
			}catch (Exception e){
				sendError("Relation of type "+relation+"not allowed for Nodetype: "+node.getClass().getSimpleName());
				return;
			}
			
			if(nodename.startsWith("@")){
				createRelationShipForKnownRelation(line,relation,nodename);
				return;
			}
			createNodeForKnownRelation(line,relation,nodename);
			return;
		}
		if(nodename.startsWith("@")){
			createRelationShipForUnknownRelation(line, relation, nodename);
			return;
		}
		createNodeForUnknownRelation(line,relation,nodename);
	}
	
	private void createNodeForKnownRelation(String line, String relation, String nodename) {
		try{
			if(NodeToRelationMap.RelationNeedsExistingNode.get(relation)){
				throw new Exception("Exception");
			}
		} catch (Exception e){
			sendError("Relations of type "+relation+ " need already existing node");
			return;
		}
		try {
			Constructor<?extends NodeInterface> constructor = DBService.getConstructors().get(node.getClass().getSimpleName());
			System.out.println(constructor);
			NodeInterface createdNode = constructor.newInstance();
			createdNode.setName(nodename);
			createdNode = DBService.createRelationshipWithNode(createdNode, relation, node.getId(), user.getId());
			if(createdNode==null||createdNode.getId()<0){
				throw new Exception("Exception");
			}
			sendMessageWithContent(line,createdNode);
		} catch (Exception e){
			e.printStackTrace();
			sendError("Error creating Node");
			return;
		}
		
	}
	
	private void createNodeForUnknownRelation(String line, String relation, String nodename) {
		try {
			NodeInterface createdNode = new Property();
			createdNode.setName(nodename);
			createdNode = DBService.createRelationshipWithNode(createdNode, relation, node.getId(), user.getId());
			if(createdNode==null||createdNode.getId()<0){
				throw new Exception("Exception");
			}
			sendMessageWithContent(line,createdNode);
		} catch (Exception e){
			e.printStackTrace();
			sendError("Error creating Node");
			return;
		}
		
	}

	private void createRelationShipForKnownRelation(String line, String relation, String nodename){
		NodeInterface relatedNode = getRelatedNode(nodename);
		if(relatedNode==null){
			return;
		}
		if(!relatedNode.getClass().getSimpleName().equals(nodeToRelation.get(relation))){
			sendError("Relation of type "+relation+"not allowed to Node of Type: "+relatedNode.getClass().getSimpleName());
			return;
		}
		try{
			if(DBService.addRelationship(node.getId(), relation, relatedNode.getId())<0){
				throw new Exception("Exception");
			}
		}catch (Exception e){
			sendError("Relation couldn't be created");
			return;
		}
		sendMessageWithContent(line,relatedNode);
	}
	
	private void createRelationShipForUnknownRelation(String line, String relation, String nodename){
		NodeInterface relatedNode = getRelatedNode(nodename);
		if(relatedNode==null){
			return;
		}
		try{
			if(DBService.addRelationship(node.getId(), relation, relatedNode.getId())<0){
				throw new Exception("Exception");
			}
		}catch (Exception e){
			sendError("Relation couldn't be created");
			return;
		}
		sendMessageWithContent(line,relatedNode);
	}
	
	private NodeInterface getRelatedNode(String nodename){
		long reletednodeid = -1;
		try{
			reletednodeid = Long.parseLong(nodename.substring(1, nodename.length()));	
		}catch (Exception e){
			sendError("Could not parse Id of Node");
		}
		try{
			NodeInterface relatedNode = DBService.getNodeByID(NodeInterface.class, reletednodeid, 0);
			if(relatedNode==null){
				throw new Exception("Couldn't finde node");
			}
			return relatedNode;
		}catch (Exception e){
			sendError("Could not get Node with id "+reletednodeid);
		}
		return null;
	}
	
	private void sendMessageWithContent(String message, NodeInterface createdNode){
		synchronized (this) {
			try {
				Message m = this.saveMessage(message, false);
				this.sendToOtherClients((new MsgWrapper(m,createdNode).toString()));
			} catch (Exception e1) {
				os.println("Error sending the message");
			}
			log.debug("Message received: '" + message + "' from user '" + user.getEmail() + "'");
		}
	}
	
	private void sendNormalMessage(String message){
		synchronized (this) {
			try {
				Message m = this.saveMessage(message, false);
				this.sendToOtherClients((new MsgWrapper(m).toString()));
			} catch (Exception e1) {
				os.println("Error sending the message");
			}
			log.debug("Message received: '" + message + "' from user '" + user.getEmail() + "'");
		}
	}
	
	private void sendError (String message){
		MsgWrapper msg = new MsgWrapper();
		msg.setCreator("server");
		msg.setTimestamp(new CustomDate(System.currentTimeMillis()));
		msg.setMessage(message);
		os.println(msg.toString());
	}

	/*private String checkLine(String line, String user) {
		if (line.startsWith("/quit"))
			return line;
		if (line.startsWith("?"))
			return line;
		if (line.startsWith("#") && !line.contains("@")) {// Propertie with
																// value
			return "Property '"
					+ line.substring(1, line.length()).split(" ", 2)[0]
					+ "' mit dem Wert '"
					+ line.substring(1, line.length()).split(" ", 2)[1]
					+ "' zur entscheidung hinzugefuegt";
		} else if (line.startsWith("@") && line.contains("#Comment")) { // Comment
																		// on
																		// propertie
			String propertie = line.substring(1, line.length()).split(" #Comment ", 2)[0];
			String comment = line.substring(1, line.length()).split(" #Comment ", 2)[1];
			
			return "Kommentar '" + comment + "' zur Eigenschaft '" + propertie + "' hinzugefuegt";
		}
		return line;
	}*/

	public long getNodeId() {
		return node.getId();
	}

	private Message saveMessage(String message, boolean server) {
		// TODO: user id switchen falls server msg
		if (node != null && user != null) {
			return DBService.createMessage(message, node.getId(), user.getId());
		}
		return new Message(message);
	}

	private void sendToOtherClients(String message) {
		server.notifyAll(message, this.getNodeId());
	}
}

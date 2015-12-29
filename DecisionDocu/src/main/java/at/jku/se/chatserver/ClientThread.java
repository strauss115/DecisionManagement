package at.jku.se.chatserver;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.CustomDate;
import at.jku.se.model.Message;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;

public class ClientThread extends Thread {
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private String name;
	
	private final ArrayList<ClientThread> clientthreads;
	private static final Logger log = LogManager.getLogger(ClientThread.class);
	
	private NodeInterface node = null;
	private User user = null;

	public ClientThread(Socket clientSocket, ArrayList<ClientThread> clientthreads) {
		this.clientSocket = clientSocket;
		this.clientthreads = clientthreads;
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
				os.println(msg.toString());
				name = is.readLine().trim();
				if (name.indexOf('?') == -1) {
					break;
				} else {
					msg.setMessage("The name should not contain '?' character.");
					os.println(msg.toString());
				}
			}

			String[] parts = name.split("@");

			// Get the User Node Object via DBService
			//user = DBService.getNodeByID(User.class, Integer.parseInt(parts[0]), 1);
			user = SessionManager.getUser(parts[0]);
			
			// Get the Node Object via DBService
			node = DBService.getNodeByID(NodeInterface.class, Integer.parseInt(parts[1]), user, 2);
			//System.out.println(node);

			msg.setMessage("Welcome " + user.getName()
			+ " to '" + node.getName() + "' chat room.\nTo leave enter /quit in a new line.");
			
			os.println(msg.toString());

			// Chatverlauf an diesen Client senden
			Map<String, List<RelationshipInterface>> rs = node.getRelationships();
			if (rs.get("message") != null) {
				for (RelationshipInterface m : rs.get(RelationString.HAS_MESSAGE)) {
					
					if (m.getRelatedNode() instanceof Message) {
						
						msg = new MsgWrapper((Message)m.getRelatedNode());
						if (msg != null) {
							log.debug("Attaching data: " + msg.toString());
							
							os.println(msg.toString());
						}
					}
				}
			}
			
			synchronized (this) {
				String message = "The user " + user.getName()
						+ " entered the chat.";
				
				Message m = this.saveMessage(message, true);
				try {
					this.sendToOtherClients(new MsgWrapper(m).toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			// Start the conversation
			while (true) {
				String line = is.readLine();
				line = checkLine(line, parts[0]);
				if (line.startsWith("/quit")) {
					break;
				}
				
				synchronized (this) {
					String message = line;
					
					Message m = this.saveMessage(message, false);
					try {
						this.sendToOtherClients(new MsgWrapper(m).toString());
					} catch (Exception e1) {
						log.error(e1);;
					}
					log.debug("Message received: '" + line + "' from user '" + user.getEmail() + "'");
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
			//os.println("*** Bye " + user.getName() + " ***");

			// close thread
			synchronized (this) {
				clientthreads.remove(this);
			}
			
		} catch (IOException e) {
			log.error(e);
		}
		catch (Exception e) {			
			log.error(e);
			String message = "The user " + (user == null ? "null" : user.getName()) + " is leaving the chat room.";
			Message m = this.saveMessage(message, true);
			try {
				this.sendToOtherClients(new MsgWrapper(m).toString());
			} catch (Exception e1) {
				log.error(e1);
			}
			clientthreads.remove(this);
		}
		finally {
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

	private String checkLine(String line, String user) {
		if (line.startsWith("/quit"))
			return line;
		else if (line.startsWith("?"))
			return line;
		else if (line.startsWith("#") && !line.contains("@")) {// Propertie with
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
	}
	
	public long getNodeId() {
		return node.getId();
	}
	
	private Message saveMessage(String message, boolean server) {
		// TODO: user id switchen falls server msg
		return DBService.createMessage(message, node.getId(), user.getId());
	}
	
	private void sendToOtherClients(String message) {
		for (ClientThread thread : clientthreads) {
			if (thread.getNodeId() == this.getNodeId())
				thread.os.println(message);
		}
	}
	
	private void sendToNodesClients(String message) {
		for (ClientThread thread : clientthreads) {
			if (thread != null && thread.getNodeId() == this.getNodeId())
				thread.os.println(message);
		}
	}
}

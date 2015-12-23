package at.jku.se.chatserver;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.jku.se.database.DBService;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;

public class ClientThread extends Thread {
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private String name;
	
	private final ArrayList<ClientThread> clientthreads;
	
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
			
			while (true) {
				os.println("Retrieving Login Data...");
				name = is.readLine().trim();
				if (name.indexOf('?') == -1) {
					break;
				} else {
					os.println("The name should not contain '?' character.");
				}
			}

			String[] parts = name.split("@");

			// Get the User Node Object via DBService
			user = DBService.getNodeByID(User.class, Integer.parseInt(parts[0]), 1);
			
			// Get the Node Object via DBService
			node = DBService.getNodeByID(NodeInterface.class, Integer.parseInt(parts[1]), user, 2);
			//System.out.println(node);

			os.println("Welcome " + user.getName()
					+ " to '" + node.getName() + "' chat room.\nTo leave enter /quit in a new line.");

			// Chatverlauf an diesen Client senden
			Map<String, List<RelationshipInterface>> rs = node.getRelationships();
			if (rs.get("message") != null) {
				for (RelationshipInterface m : rs.get("message")) {
					os.println(m.getRelatedNode().getName());
				}
			}
			
			synchronized (this) {
				String message = "The user " + user.getName()
						+ " entered the chat.";
				
				this.saveMessage(message);
				this.sendToOtherClients(message);
			}
			
			// Start the conversation
			while (true) {
				String line = is.readLine();
				line = checkLine(line, parts[0]);
				if (line.startsWith("/quit")) {
					break;
				}
				
				synchronized (this) {
					String message = "<" + user.getName() + "> " + line;
					
					this.saveMessage(message);
					this.sendToNodesClients(message);
				}
			}
			synchronized (this) {
				String message = "The user " + user.getName() + " is leaving the chat room.";
				this.saveMessage(message);
				this.sendToNodesClients(message);
			}
			os.println("*** Bye " + user.getName() + " ***");

			// close thread
			synchronized (this) {
				clientthreads.remove(this);
			}
			
			// close streams
			is.close();
			os.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		catch (Exception e) {			
			System.out.println(e);
			String message = "The user " + user.getName() + " is leaving the chat room.";
			this.saveMessage(message);
			this.sendToOtherClients(message);
			clientthreads.remove(this);
		}
	}

	private String checkLine(String line, String user) {
		if (line.startsWith("/quit"))
			return line;
		else if (line.startsWith("?"))
			return line;
		else if (line.startsWith("#") && !line.contains("@")) {// Propertie with
																// value
			return "Propertie '"
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
	
	private void saveMessage(String message) {
		DBService.createMessage(message, node.getId(), user.getId());
	}
	
	private void sendToOtherClients(String message) {
		for (ClientThread thread : clientthreads) {
			if (thread != this && thread.getNodeId() == this.getNodeId())
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

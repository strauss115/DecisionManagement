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
	private String clientName = null;
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
			
			/* OLD
			String decname = parts[1];
			int j = 0, index = -1;
			
			for (DecisionOld dec : Server.decs) {
				if (dec.getShortname().equals(decname)) { index = j; break; }
				j++;
			}
			if (index == -1) {
				Server.decs.add(new DecisionOld(decname, decname));
			} */

			os.println("Welcome " + user.getName()
					+ " to '" + node.getName() + "' chat room.\nTo leave enter /quit in a new line.");

			// Chatverlauf senden
			Map<String, List<RelationshipInterface>> rs = node.getRelationships();
			System.out.println(rs);
			if (rs.get("message") == null) {
				System.out.println("Keine Nachrichten vorhanden");
			}
			else {
				for (RelationshipInterface m : rs.get("message")) {
					os.println(m.getRelatedNode().getName());
				}
			}
			
			System.out.println("Chatverlauf gesendet");
			
			synchronized (this) {
				for (ClientThread thread : clientthreads) {
					if (thread == this) {
						clientName = "?" + user.getName();
						break;
					}
				}
				String message = "The user " + user.getName()
						+ " entered the chat.";
				
				// Nachricht speichern:
				DBService.createMessage(message, node.getId(), user.getId());

				// Nachricht an alle Clients dieses Nodes senden:
				for (ClientThread thread : clientthreads) {
					if (thread != this && thread.getNodeId() == this.getNodeId())
						thread.os.println(message);
				}
			}
			
			// Start the conversation
			while (true) {
				String line = is.readLine();
				line = checkLine(line, parts[0]);
				if (line.startsWith("/quit")) {
					break;
				}
				
				// Sending to the others
				synchronized (this) {
					String message = "<" + name + "> " + line;
					// Nachricht speichern:
					DBService.createMessage(message, node.getId(), user.getId());
					for (ClientThread thread : clientthreads) {
						if (thread.clientName != null && thread.getNodeId() == this.getNodeId())
							thread.os.println(message);
					}
				}
			}
			synchronized (this) {
				for (ClientThread thread : clientthreads) {
					if (thread != this && thread.clientName != null && thread.getNodeId() == this.getNodeId())
						thread.os.println("The user " + user.getName()
								+ " is leaving the chat room.");
				}
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
			System.out.println("Thread Error");
		}
		catch (Exception e) {
			System.out.println(e);
			System.out.println("Thread Error2");
			LeaveChat();
		}
	}
	
	private void LeaveChat () {
		for (ClientThread thread : clientthreads) {
			if (thread != this && thread.clientName != null && thread.getNodeId() == this.getNodeId())
				thread.os.println("The user " + user.getName()
						+ " is leaving the chat room.");
		}
		clientthreads.remove(this);
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
}

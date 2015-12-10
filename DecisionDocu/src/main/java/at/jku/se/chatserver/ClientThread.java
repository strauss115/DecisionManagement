package at.jku.se.chatserver;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
	private String clientName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private String name;
	
	private final ArrayList<ClientThread> clientthreads;

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
			// Find the Decision Object if exists, otherwise create it
			String decname = parts[1];
			int j = 0, index = -1;
			
			for (Decision dec : Server.decs) {
				if (dec.getShortname().equals(decname)) { index = j; break; }
				j++;
			}
			if (index == -1) {
				Server.decs.add(new Decision(decname, decname));
			}

			os.println("Welcome " + parts[0]
					+ " to " + decname + " chat room.\nTo leave enter /quit in a new line.");

			// send chat history
			for (String line : Server.decs.get(j)
					.getHistory()) {
				os.println(line);
			}
			
			synchronized (this) {
				for (ClientThread thread : clientthreads) {
					if (thread == this) {
						clientName = "?" + name;
						break;
					}
				}
				String message = "*** A new user " + name
						+ " entered the decision chat ***";
				Server.decs.get(j).addMessage(message);
				for (ClientThread thread : clientthreads) {
					if (thread != this)
						thread.os.println(message);
				}
			}
			
			// Start the conversation
			while (true) {
				String line = is.readLine();
				line = checkLine(line, j, parts[0]);
				if (line.startsWith("/quit")) {
					break;
				}
				
				// Sending to the others
				synchronized (this) {
					String message = "<" + name + "> " + line;
					Server.decs.get(j)
							.addMessage(message);
					for (ClientThread thread : clientthreads) {
						if (thread.clientName != null)
							thread.os.println(message);
					}
				}
			}
			synchronized (this) {
				for (ClientThread thread : clientthreads) {
					if (thread != this && thread.clientName != null)
						thread.os.println("*** The user " + name
								+ " is leaving the chat room !!! ***");
				}
			}
			os.println("*** Bye " + name + " ***");

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
			System.out.println("Thread Error2");
			LeaveChat();
		}
	}
	
	private void LeaveChat () {
		for (ClientThread thread : clientthreads) {
			if (thread != this && thread.clientName != null)
				thread.os.println("*** The user " + name
						+ " is leaving the chat room !!! ***");
		}
		clientthreads.remove(this);
	}

	private String checkLine(String line, int deci, String user) {
		if (line.startsWith("/quit"))
			return line;
		else if (line.startsWith("?"))
			return line;
		else if (line.startsWith("#") && !line.contains("@")) {// Propertie with
																// value
			Server.decs.get(deci)
					.addProbertie(new Propertie(line.substring(1, line.length())
							.split(" ", 2)[0], line.substring(1, line.length())
							.split(" ", 2)[1], user));
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

			for (Propertie prop : Server.decs.get(deci).getProperties()) {
				if (prop.getName().equals(propertie))
					prop.addComment(new Comment(comment, user));
			}
			
			return "Kommentar '" + comment + "' zur Eigenschaft '" + propertie + "' hinzugefuegt";
		}
		return line;
	}
}

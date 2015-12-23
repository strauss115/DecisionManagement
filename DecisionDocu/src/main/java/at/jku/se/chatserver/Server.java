package at.jku.se.chatserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server implements Runnable {
	private static final Logger log = LogManager.getLogger(Server.class);
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private static final int portNumber = 2222;

	// This chat server can accept up to maxClientsCount clients' connections.
	private static final int maxClientsCount = 10;
	private static final ArrayList<ClientThread> clientthreads = new ArrayList<ClientThread>();

	@Override
	public void run() {
		log.info("starting chatserver on port " + portNumber);
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			log.error("Error occured", e);
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("New connection requested...");
				clientthreads.add(new ClientThread(clientSocket, clientthreads));
				clientthreads.get(clientthreads.size() - 1).start();

				log.debug(clientthreads.size() + " clients are connected now.");

				if (clientthreads.size() == maxClientsCount) {
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				log.error("Error occured", e);
			}
		}	
	}
	
	public void close() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
				log.debug("Socket closed!");
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
}

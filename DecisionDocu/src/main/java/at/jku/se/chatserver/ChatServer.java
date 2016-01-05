package at.jku.se.chatserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatServer implements Runnable, ServerListener {
	private static final Logger log = LogManager.getLogger(ChatServer.class);
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
				log.debug("New connection requested from '" + clientSocket.getInetAddress() + "'");
				
				synchronized (this) {
					
					if (clientthreads.size() == maxClientsCount) {
						PrintStream os = new PrintStream(clientSocket.getOutputStream());
						MsgWrapper msg = MsgWrapper.dummy();
						msg.setMessage("Server too busy. Try later.");
						os.println(msg.toString());
						os.close();
						clientSocket.close();
					} else {
						ClientThread newThread = new ClientThread(this, clientSocket);
						clientthreads.add(newThread);
						newThread.start();
						
						log.debug(clientthreads.size() + " clients are connected now.");	
					}
				}
			} catch (IOException e) {
				log.error("Error occured", e);
			}
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
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

	@Override
	public void clientLeft(Socket socket, ClientThread thread) {
		log.debug("Client: " + socket.getInetAddress() + " leaves chat");
		synchronized (this) {
			clientthreads.remove(thread);
			log.debug(clientthreads.size() + " clients are connected now.");
		}
	}

	@Override
	public void notifyAll(String msg, long nodeId) {
		synchronized (this) {
			for (ClientThread thread : clientthreads) {
				if (thread.getNodeId() == nodeId)
					thread.getOutputStream().println(msg);
			}
		}
		
	}
}

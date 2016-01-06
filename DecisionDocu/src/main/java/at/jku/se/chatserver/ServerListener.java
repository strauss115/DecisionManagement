package at.jku.se.chatserver;

import java.net.Socket;

/**
 * 
 * @author martin
 *
 */
public interface ServerListener {

	/**
	 * Send the String message to all connected clients
	 * @param msg Message
	 * @param nodeId ID of the related node
	 */
	void notifyAll(String msg, long nodeId);
	
	/**
	 * Nofify the server when a client left the chat
	 * @param socket
	 * @param thread
	 */
	void clientLeft(Socket socket, ClientThread thread);
}

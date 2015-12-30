package at.jku.se.chatserver;

import java.net.Socket;

public interface ServerListener {

	void notifyAll(String msg, long nodeId);
	void clientLeft(Socket socket, ClientThread thread);
}

package at.jku.se.chatserver;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	// This chat server can accept up to maxClientsCount clients' connections.
	private static final int maxClientsCount = 10;
	private static final ArrayList<ClientThread> clientthreads= new ArrayList<ClientThread>();
	public static ArrayList<Decision> decs = new ArrayList<Decision>();

	public static void main(String args[]) {

		// The default port number.
		int portNumber = 2222;
		if (args.length < 1) {
			System.out
					.println("Usage: java Server <portNumber>\n"
							+ "Now using port number=" + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}

	
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println("Main Error 1");
			System.out.println(e);
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("New connection requested...");
				clientthreads.add(new ClientThread(clientSocket, clientthreads));
				clientthreads.get(clientthreads.size()-1).start();
				
				System.out.println(clientthreads.size()+" clients are connected now.");
				
				if (clientthreads.size() == maxClientsCount) {
					PrintStream os = new PrintStream(
							clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println("Main Error 2");
				System.out.println(e);
			}
		}
	}
}

package at.jku.se.decisiondocu.chat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class represents the client side of the chat mechanism
 */
public class ChatClient {

    public static final String QUIT_MESSAGE = "/quit";
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages
     * received from server
     */
    public ChatClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient() {
        mRun = false;
    }

    /**
     * Run method
     * @param ip IP - Address of the server
     * @param port Chat-Server Port
     */
    public void run(String ip, int port) {

        mRun = true;

        try {
            // get server inet addr
            InetAddress serverAddr = InetAddress.getByName(ip);

            Log.d("serverAddr", serverAddr.toString());
            Log.d("TCP ChatClient", "C: Connecting...");

            // create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, port);
            Log.e("TCP Server IP", ip);

            try {
                String serverMessage = null;

                // send the message to the server
                out = new PrintWriter(
                        new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())), true);


                // receive the message which the server sends back
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine(); // listen for msg

                    // if msg == null --> end
                    if (serverMessage != null && mMessageListener != null) {
                        // call the method messageReceived from ChatActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
                        + serverMessage + "'");

            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }

    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}
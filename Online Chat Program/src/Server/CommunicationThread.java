package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import chat_msg_user_info.ChatMessage;
//CommunicationThread is a class that essentially handles the data flow from client to server and
//back to client
public class CommunicationThread extends Thread {
	private Socket clientSocket;	//Stores current socket being read
	private ConnectionThread server;	//Uses the instance of connectionthread in order to message users

	//Main constructor that starts reading in client input
	public CommunicationThread(ConnectionThread server, Socket clientSoc) {
		clientSocket = clientSoc;
		this.server = server;
		start();
	}

	//Run that runs simultaneously
	public void run() {
		//Try and catch which reads in user input
		try {
			//Opens an input and output stream
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String inputLine;
			//While loop that reads in input
			while ((inputLine = in.readLine()) != null) {
				//If the input starts with a To it sends a private message
				if(inputLine.startsWith("To")){
					ChatMessage temp = new ChatMessage(inputLine, clientSocket);
					server.messages.add(temp);
					server.notifyClients(1, null, null);
				}
				//If the input starts with Bya it disconnects the client
				else if (inputLine.equals("Bye.")){
					server.removeOnlineUsers(clientSocket);
				}
				//If anything else the message gets sent to everyone online
				else{
					out.println(inputLine);
					server.notifyClients(2, inputLine, clientSocket);
				}
			}
			//Closes streams and closes socket
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			//System.exit(1);
		}
	}
}
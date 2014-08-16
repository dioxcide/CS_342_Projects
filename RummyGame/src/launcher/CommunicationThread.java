package launcher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
//CommunicationThread is a class that essentially handles the data flow from client to server and
//back to client
public class CommunicationThread extends Thread {
	private Socket clientSocket;	//Stores current socket being read
	private ConnectionThread server;	//Uses the instance of connectionthread in order to message users
	private ObjectOutputStream out;					//Opens output stream to each client;
	private ObjectInputStream in;

	//Main constructor that starts reading in client input
	public CommunicationThread(ConnectionThread server, Socket clientSocket, ObjectOutputStream out, ObjectInputStream in) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.out = out;
		this.in = in;
		start();
	}
	//Getter function
	public Socket getSocket(){
		return clientSocket;
	}
	//Parsing functions. This one parses for the Sending User name
	public String getUser(String temp){
		String delims = ":";
		String[] tokens = temp.split(delims);
		String user = tokens[3];
		return user;
	}
	//Parsing functions. This one parses for the Message being sent
	public String getMsg(String temp){
		String delims = ":";
		String[] tokens = temp.split(delims);
		String msg = tokens[4];
		return msg;
	}
	//Parsing Functions. This one parses for the Recipient user
	public String getTo(String temp){
		String delims = ":";
		String[] tokens = temp.split(delims);
		String To = tokens[1];
		return To;
	}
	//Run that runs simultaneously
	@SuppressWarnings("unchecked")
	public void run() {
		//Try and catch which reads in user input
		try {

			String inputLine;
			//While loop that reads in input
			Object tempObj;
			Deck upDeck;
			ArrayList<Card> upPile;
			boolean upFinished;
			server.synchronizeDeck();
			server.synchronizePile();
			
			while ((tempObj = in.readObject())!= null) {
				// If the input starts with a To it sends a private message
				if (tempObj instanceof User) {
					User winner = (User)tempObj;
					String winnerName = winner.getUser();
					server.sendToAll("Winner of this round is "+winnerName+"!\nDisconnected");
				} 
				//If input is a deck the server deck is updated
				else if(tempObj instanceof Deck){
					upDeck = (Deck)tempObj;
					server.updateDeck(upDeck);
					server.synchronizeDeck();
				}
				//If input is an Arraylist the discardpile from the server is updated
				else if(tempObj instanceof ArrayList<?>){
					upPile = (ArrayList<Card>)tempObj;
					server.updateDiscard(upPile);
					server.synchronizePile();
				}
				//If the input is a boolean the player turn is updated
				else if(tempObj instanceof Boolean){
					upFinished = (boolean)tempObj;
					if(upFinished == true){
						server.playerTurn++;
						if(server.playerTurn>=server.clientsReady){
							server.playerTurn = 0;
						}
						server.determineTurn();
					}
				}
				//If the object is a string the message is processed
				else if (tempObj instanceof String) {
					inputLine = (String) tempObj;

					if (inputLine.startsWith("To")) {
						String from = getUser(inputLine);
						String message = getMsg(inputLine);
						String to = getTo(inputLine);

						ChatMessage temp = new ChatMessage(" " + message, from,
								to, getSocket());

						server.messages.add(temp);
						server.sendToSpecifiedUser();
					}
					
					// If the input starts with Bye it disconnects the client
					else if (inputLine.equals("Bye.")) {
						server.removeConnection(getSocket());
					}
					
					// If anything else the message gets sent to everyone online
					else if (inputLine.equals("Start")) {
						server.incrementReadyUsers();
						if (server.getClientsReady() >= 2 && server.getClientsReady() == server.getOnlineUsers()) {
							
							server.gameStarted = true;
							server.distributeHands();
							server.sendToAll("Starting Game!");
							server.determineTurn();
						}
					} 
					
					else {
						server.sendToAll(inputLine);
					}
				}
			}
			//Closes streams and closes socket
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			//System.exit(1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
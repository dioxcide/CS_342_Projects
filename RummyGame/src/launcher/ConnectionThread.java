package launcher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

//ConnectionThread is essentially a class that deals with all incoming connections that are coming to the server
//and accepts them.
public class ConnectionThread extends Thread {
	//Has access to server gui
	private Server gui;
	//Two array lists to keep track of messages being sent and clients connected
	private ArrayList<Clients> clients = new ArrayList<Clients>();
	public ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	private static ArrayList<User> userHands = new ArrayList<User>();
	public ArrayList<Card> discardPile = new ArrayList<Card>();
	public ArrayList<ObjectOutputStream> outs = new ArrayList<ObjectOutputStream>();
	public ArrayList<ObjectInputStream> ins = new ArrayList<ObjectInputStream>();
	
	public Deck gameDeck;
	private int numberOfUsers;
	public int playerTurn = 0;
	public boolean gameStarted = false;
	public int clientsReady = 0;
	private int port;

	//Main constructor that starts this connection thread
	public ConnectionThread(Server es3) {
		gui = es3;
		start();
	}
	
	//Method that sends the message to a specific user
	public void sendToSpecifiedUser()throws IOException{
		synchronized(clients){//Synchronized in case clients disconnect
			int totalMsg = messages.size();
			
			for(int i=0;i<clients.size();i++){	//For loop That goes through all the clients until it 
				
				String temp = clients.get(i).getUN();	//finds the specific client to message
				ChatMessage msg1 = messages.get(totalMsg-1);
				String To = msg1.getTo();
				
				if(temp.equals(To)){
					ObjectOutputStream out = outs.get(i);
					out.writeObject("PRIV MSG FROM - "+msg1.getFrom()+"\n"+msg1.getMessage());
				}
			}
		}
	}
	
	// Send a message to all clients (utility routine)
	public void sendToAll(String message) throws IOException {
		// We synchronize on this because another thread might be
		// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		synchronized (clients) {
			// For each client ...
			for (int i = 0;i<clients.size();i++) {
					// ... get the output stream ...
				ObjectOutputStream out = outs.get(i);
					// ... and send the message
				out.writeObject(message);
			}
		}
	}
	//Removes disconnected users
	public void removeConnection(Socket s) throws IOException {
		// Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streams
		synchronized (clients) {
			String offline = null;
			// Remove it from our list
			for(int i =0;i<clients.size();i++){
				Socket temp = clients.get(i).getSocket();
				if(temp == s){
					offline = clients.get(i).getUN();
					clients.remove(i);
					outs.remove(i);
				}
			}
			//Update the Online User List for all other Clients
			for(int i =0;i<clients.size();i++){
				ObjectOutputStream out = outs.get(i);
				out.writeObject("Offline:"+offline);
			}
			// Make sure it's closed
			try {
				s.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	// Removes disconnected users
	public void removeAllConnections() throws IOException {
		// Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streams
		synchronized (clients) {
			// Remove it from our list
			for (int i = 0; i < clients.size(); i++) {
					ObjectOutputStream out = outs.get(i);
					ObjectInputStream in = ins.get(i);
					out.writeObject("Disconnected!");
					clients.remove(i);
					outs.remove(i);
					ins.remove(i);
					out.close();
					in.close();
			}
			gui.serverSocket.close();
		}
	}
	//Synchronize newly updated deck with all the users online
	public void synchronizeDeck(){
		synchronized(clients){
			for(int i = 0;i < clients.size();i++){
				ObjectOutputStream out = outs.get(i);
				try {
					out.writeObject(gameDeck); //Writes the deck to all the users
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//Synchronizes newly updated discard pile with all the users online
	public void synchronizePile(){
		synchronized(outs){
			for(int i = 0;i < clients.size();i++){
				ObjectOutputStream out = outs.get(i);
				try {
					out.writeObject(discardPile);	//Writes to all the users the newly updated discard pile
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//Creates an Object User depending on the amount of users playing
	public User createdHand(String tempName){
		ArrayList<Card> tempHand = new ArrayList<Card>();
		Hashtable<String,ArrayList<Card>> Melds = new Hashtable<String,ArrayList<Card>>();
		if(getOnlineUsers()==2){
			for(int i = 0;i<10;i++){
				tempHand.add(gameDeck.drawFromDeck());		//Creates deck of 10
			}
		}
		else if(getOnlineUsers() ==3 || getOnlineUsers() == 4){
			for(int i = 0;i<7;i++){
				tempHand.add(gameDeck.drawFromDeck());		//Creates deck of 7
			}	
		}
		else if(getOnlineUsers() == 5 || getOnlineUsers() == 6){
			for(int i = 0;i<6;i++){
				tempHand.add(gameDeck.drawFromDeck());		//Creates deck of 6
			}
		}
		
		User temp = new User(Melds, tempHand, tempName);
		
		return temp;
	}
	//Distribute initial hands to each user
	public void distributeHands() throws IOException{
		// We synchronize on this because another thread might be
				// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		gameDeck = new Deck();
		synchronized (clients) {
			// For each client ...
			for (int i = 0; i < clients.size(); i++) {
				
				String tempName = clients.get(i).getUN();
				// ... get the output stream ...
				ObjectOutputStream out = outs.get(i);
				User t = createdHand(tempName);
				userHands.add(t);
			
				// ... and send the object
				out.writeObject(t);
			}
			synchronizeDeck();
			synchronizePile();
		}

	}
	//Determines the users turns
	public void determineTurn(){
		ObjectOutputStream out;
		synchronized(outs){
			if (playerTurn < numberOfUsers) {
				for (int i = 0; i <outs.size(); i++) {
					out = outs.get(i);

					if (i == playerTurn) {			//Depending on what playerTurn is it will send either true or false to each client essentially telling it if it's
						try {						//turn or not
							out.writeObject(true);
							out.writeObject("It is your turn!");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					else {
						try {
							out.writeObject(false);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	//Run processes new connections being made between the client and server
	public void run() {
		gui.serverContinue = true;

		try {
			gui.serverSocket = new ServerSocket(0);
			try {
				while (gui.serverContinue) {
					setPort(gui.serverSocket.getLocalPort());//Accepts the socket and reads in the username for
					gui.Port.setText("Port: "+port);		//the socket
					
					Socket clientSocket = gui.serverSocket.accept();
					String userName = null;
					
					ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());					//Opens output stream to each client;
					ObjectInputStream in = new ObjectInputStream(//Opens stream to update clients with info
							clientSocket.getInputStream());
				
					userName = (String)in.readObject();
					
					out.writeObject("Connected to Server!");
					outs.add(out);
					ins.add(in);
					incrementUsers();
					
					Clients cl1 = new Clients(clientSocket, userName);	//Stores username with the socket it is 
					clients.add(cl1);									//Associated with 
					
					for(int i =0;i<clients.size();i++){
						
						ObjectOutputStream out2 = outs.get(i);					//Opens output stream to each client
						for(int j =0;j<clients.size();j++){
							String tempClient = clients.get(j).getUN();	//Populates Online user list
							out2.writeObject("User:"+tempClient);
						}
						
					}
					
					new CommunicationThread(this, clientSocket, out, in);
				}
			} catch (IOException e) {
				System.out.println("Swag1: "+e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Swag2: "+e);
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("Swag3: "+e);
		} 
	}
	
	// Setters and getters

	public void updateDeck(Deck updatedDeck) {
		this.gameDeck = updatedDeck;
	}

	public void updateDiscard(ArrayList<Card> updatedPile) {
		this.discardPile = updatedPile;
		}
	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

	public void incrementUsers() {
		this.numberOfUsers++;
	}

	public int getOnlineUsers() {
		return this.numberOfUsers;
	}

	public void incrementReadyUsers() {
		this.clientsReady++;
	}

	public int getClientsReady() {
		return this.clientsReady;
	}
	
	public static ArrayList<User> getPlayers() {
		return userHands;
	}
}
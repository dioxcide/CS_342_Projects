package launcher;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

//Main class that brings everything together. Essentially the client class that sets up the gui
//and connects the client or sets up a server
public class GameGUI extends JFrame implements ActionListener, Runnable, MouseListener{
	private static final long serialVersionUID = 1L; //makes eclipse happy =)		
	public static GameGUI test1;
	private GameFrameWork handleMoves;
	private static JPanel p1, p2, p3, p4;									//Declerations
	private static JPanel handLayout, centerLayout;							//Gives easier access later
	private User userHand;
	private JLabel userLabel = new JLabel("   Users Online");
	private static JMenuBar menu = new JMenuBar();							//Menubar
	private JMenu m1 = new JMenu("Menu");									//Menu
	private JMenuItem HostServer, Connect;									//JMenuitems..
	private JMenu help = new JMenu("Help");
	private JMenuItem ServerSetup, ClientConnect, Messaging;
	private JButton [] options = new JButton[7];							//Jbuttons for Msg, Msg All, and Dc
	private JButton [] Hand;
	private JButton [] Meldings;
	private JButton Deck;
	private JButton DiscardPile;
    private JTextField textField;											//Textfields and TextArea for chat
    private JTextArea textArea;
    private JTextArea usersArea;
   
    boolean connected;		
    private String sendTo = null;													//Strings to keep track of
    private String Username;												//Users being sent msg's and 
    private String[] PossibleKeys  = {"Ace" ,"2", "3", "4", "5", "6", "7", "8", "9","10", "Jack", "Queen", "King","hearts", "spades", "diamonds", "clubs"};

    
    private Socket echoSocket;												//Individual client names
    private ObjectOutputStream out;												//Streams to comm. with server
    private ObjectInputStream in;
    private Deck updatingDeck;
    private ArrayList<Card> discard;
    private Color fieldGREEN = new Color(23,125,54);
    public int handSize = 0;

	//Main constructor which sets up and implements the GUI for the program
	public GameGUI(){
		super("Rummy");
		p1 = new JPanel(new GridLayout(1,3));	//3 panels for buttons users online and chat
		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2,BoxLayout.PAGE_AXIS));
		p3 = new JPanel(new GridBagLayout());
		p4 = new JPanel(new GridBagLayout());
		
		Dimension d = new Dimension(20,20);				//Setting online userpanel
		userLabel.setPreferredSize(d);				
		p2.add(userLabel);
		usersArea = new JTextArea(10, 10);
		usersArea.setEditable(false);
		usersArea.addMouseListener(this);
        JScrollPane scroll = new JScrollPane(usersArea);

        p2.add(scroll);								//Adding it to the 2nd panel 
		
        options[0] = new JButton("Start Game");
        options[1] = new JButton("Place Melds");
        options[2] = new JButton("Lay Off Cards");
        options[3] = new JButton("Discard Card");
		options[4] = new JButton("Message");			//Implementing buttons ass well as adding them
		options[5] = new JButton("Message All");		// to the 1st panel and adding actionlistners to 
		options[6] = new JButton("Disconnect");			//them
        
		options[0].addActionListener(this);
		options[4].addActionListener(this);
		options[5].addActionListener(this);
		options[6].addActionListener(this);
		
		for(int j=0; j<7; j++){						//Adding...
			p1.add(options[j]);
		}
		
		HostServer = new JMenuItem("HostServer");		//Sets up multiple JMenu items
		Connect = new JMenuItem("Connect");				//Adds acction listeners to them
		HostServer.addActionListener(this);
		Connect.addActionListener(this);
		m1.add(HostServer);
		m1.add(Connect);
		menu.add(m1);
		
		ServerSetup = new JMenuItem("ServerSetup");				//More JMenuItems being set up..
		ClientConnect = new JMenuItem("ClientConnect");
		Messaging = new JMenuItem("Messaging");
		ServerSetup.addActionListener(this);
		ClientConnect.addActionListener(this);
		Messaging.addActionListener(this);
		help.add(ServerSetup);
		help.remove(ClientConnect);
		help.add(Messaging);
		menu.add(help);
		
		textField = new JTextField(20);							//Sets up textField and textArea
        textField.addActionListener(this);						//One are for typing messages
		textArea = new JTextArea(5, 20);						//And the other being the container
        textArea.setEditable(false);							//for the chat
        JScrollPane scrollPane = new JScrollPane(textArea);
         
        //adding components to this panel 	
        GridBagConstraints gridConstraint = new GridBagConstraints();
        gridConstraint.gridwidth = GridBagConstraints.REMAINDER;

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        p3.add(textField, gridConstraint);

        gridConstraint.fill = GridBagConstraints.BOTH;
        gridConstraint.weightx = 1.0;
        gridConstraint.weighty = 1.0;
        p3.add(scrollPane, gridConstraint);					//Adds the chat to panel 3
        
        p4.setBackground(fieldGREEN);
        //TODO@ implement these only when game starts, however for testing leave as is...
        initPlayerHandButtons(5); //number of players determines player hand size
        initCenterButtons("Discards");
	}
	//Initializes melded GUI
	public void initPlayerMelds(int cards) {
		handSize = cards;
		
		GridBagConstraints gridConstraintPH = new GridBagConstraints();
		gridConstraintPH.weightx = 1.0;
        gridConstraintPH.weighty = 1.0;
        gridConstraintPH.insets = new Insets(0,0,5,5); //specifies the external padding 
        
        handLayout = new JPanel(new GridBagLayout());
        Meldings = new JButton[handSize]; 
		for(int i=0; i<handSize; i++) {
			//user the userHand User object to properly assign card values
			Icon icon = new ImageIcon("src/resources/blank_card.png");
			Meldings[i] = new JButton("",icon);
	
			Meldings[i].setBackground(Color.WHITE);	
			Font rankFont = new Font("Arial",Font.BOLD,14);
			Meldings[i].setFont(rankFont);
			
			Meldings[i].setVerticalTextPosition(SwingConstants.TOP);
			Meldings[i].setHorizontalTextPosition(SwingConstants.CENTER);
			Meldings[i].setPreferredSize(new Dimension(50,75));
			handLayout.add(Meldings[i],gridConstraintPH);
		}
		
		gridConstraintPH.anchor = GridBagConstraints.NORTH; //determine where (within the area) to place the component
        gridConstraintPH.ipadx = 0; //Specifies the internal padding
		gridConstraintPH.ipady = 0; //Specifies the internal padding
		handLayout.setBackground(fieldGREEN);
		p4.add(handLayout,gridConstraintPH);
		p4.revalidate();
		p4.repaint();
	}
	//initialized Player Hand
	public void initPlayerHandButtons(int cards) {
		handSize = cards;
		
		GridBagConstraints gridConstraintPH = new GridBagConstraints();
		gridConstraintPH.weightx = 1.0;
        gridConstraintPH.weighty = 1.0;
        gridConstraintPH.insets = new Insets(0,0,5,5); //specifies the external padding 
        
        handLayout = new JPanel(new GridBagLayout());
        Hand = new JButton[handSize]; 
		for(int i=0; i<handSize; i++) {
			//user the userHand User object to properly assign card values
			Icon icon = new ImageIcon("src/resources/blank_card.png");
			Hand[i] = new JButton("",icon);
	
			Hand[i].setBackground(Color.WHITE);	
			Font rankFont = new Font("Arial",Font.BOLD,14);
			Hand[i].setFont(rankFont);
			
			Hand[i].setVerticalTextPosition(SwingConstants.TOP);
			Hand[i].setHorizontalTextPosition(SwingConstants.CENTER);
			Hand[i].setPreferredSize(new Dimension(50,75));
			handLayout.add(Hand[i],gridConstraintPH);
		}
		
		gridConstraintPH.anchor = GridBagConstraints.SOUTH; //determine where (within the area) to place the component
        gridConstraintPH.ipadx = 0; //Specifies the internal padding
		gridConstraintPH.ipady = 0; //Specifies the internal padding
		handLayout.setBackground(fieldGREEN);
		p4.add(handLayout,gridConstraintPH);
		p4.revalidate();
		p4.repaint();
	}
	//Initializes Deck button
	public void initCenterButtons(String d) {
		GridBagConstraints gridConstraintD = new GridBagConstraints();
		gridConstraintD.weightx = 1.0;
        gridConstraintD.weighty = 1.0;
        gridConstraintD.insets = new Insets(0,5,5,5); //specifies the external padding
        centerLayout = new JPanel(new GridBagLayout()); //layout for deck and discard pile
		
      	//sets up deck JButton
		Icon icon = new ImageIcon("src/resources/back_cover.png");
		Deck = new JButton(icon);
		Deck.setPreferredSize(new Dimension(50,75));
		
		Icon iconB = new ImageIcon("src/resources/blank_card.png");
		DiscardPile = new JButton(convertString(d),iconB);
	
		DiscardPile.setBackground(Color.WHITE); 
		Font rankFont = new Font("Arial",Font.BOLD,9);
		DiscardPile.setFont(rankFont);
		
		DiscardPile.setVerticalTextPosition(SwingConstants.TOP);
		DiscardPile.setHorizontalTextPosition(SwingConstants.CENTER);
		DiscardPile.setPreferredSize(new Dimension(50,75));
		
		centerLayout.add(Deck,gridConstraintD);
		centerLayout.add(DiscardPile,gridConstraintD);
		gridConstraintD.gridx = 0; //needed for centering
		gridConstraintD.gridy = 0; //needed for centering
		gridConstraintD.anchor = GridBagConstraints.CENTER; //determine where (within the area) to place the component
		centerLayout.setBackground(fieldGREEN);
		p4.add(centerLayout,gridConstraintD);
	}

	
	@Override//General actionperformed method for the event listener
	public void actionPerformed(ActionEvent e) {
		//Sends the message to everyone
		if(connected && (e.getSource() == textField || e.getSource() == options[5]) ) {
			sendMessage();
		}
		
		//Sends a private message
		else if( connected && (e.getSource() == textField || e.getSource() == options[4]) ) {
			sendPrivateMessage(sendTo);
		}
		
		else if(connected && e.getSource() == options[0]) {		
			options[0].setText("Starting Game...");
			options[0].removeActionListener(this);
			try {
				out.writeObject("Start");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//Disconnects the client from the server
		else if(connected && e.getSource() == options[6]) {
			textArea.append("\nDisconnected\n");
			try {
				out.writeObject("Bye.");
				out.close();
				in.close();
				usersArea.setText("");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		else if(connected && e.getSource() == options[3]){
			Card toRemove = null;
			ArrayList <Card> currHand = userHand.getHand();
			for(int i=0; i< currHand.size(); i++) {
				if(Hand[i].getBackground() == Color.GREEN) {
					toRemove = currHand.get(i);
				}
			}
			
			handleMoves = new GameFrameWork(discard, updatingDeck, userHand, out, in);
			userHand = handleMoves.DiscardCard(toRemove);
			updateGUIHand(-1);
			
			try {
				out.writeObject(true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(connected && e.getSource() == options[2]){
			ArrayList<Card> currHand = userHand.getHand();
			ArrayList<Card> meldedHand = new ArrayList<Card>();
			
			for(int i=0; i<currHand.size(); i++) {
				if(Hand[i].getBackground() == Color.GREEN) {
					meldedHand.add(currHand.get(i));
				}
			}
			
			handleMoves = new GameFrameWork( discard, updatingDeck, userHand, out, in);
			userHand = handleMoves.LayingOff(meldedHand);
			updateGUIHand(-3);
		}
		
		if(connected && e.getSource() == options[1]) {
			//either have server place meld, or can put in GUI implementation
			ArrayList<Card> currHand = userHand.getHand();
			ArrayList<Card> meldedHand = new ArrayList<Card>();
			
			for(int i=0; i<currHand.size(); i++) {
				if(Hand[i].getBackground() == Color.GREEN) {
					meldedHand.add(currHand.get(i));
				}
			}
			
			handleMoves = new GameFrameWork( discard, updatingDeck, userHand, out, in);
			userHand = handleMoves.Melding(meldedHand);
			updateGUIHand(-3);
		}
		
		//Connects the client to a server
		if(e.getSource() == Connect){
			String address = JOptionPane	//Displays the following message to the user and prompts for the
					.showInputDialog(		// server that they want to connect to
							this,
							"Enter Server and Port to connect to...Ex: IP:Port");
			ManageConnection(address);
		}
		
		//Allows the user to host a new server
		else if(e.getSource() == HostServer){
			new Server(this);
		}
		
		//Several options that process the menu options
		if(e.getSource() == ServerSetup) {
			JOptionPane.showMessageDialog(this,
					"The server is vital for messaging other clients and needs to remain open for the entirety\n"
					+ "of the chat session...\n" 
					+ "To create a server, have any user act as the server host. Go to Menu->HostServer and\n"
					+ "select the Start Server option. You may specify a desired port number or default.\n",
  				    "Server Setup", JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getSource() == ClientConnect) {
			JOptionPane.showMessageDialog(this,
					"A server must be setup prior to any client connection, as there would be nothing to connect to...\n"
					+ "Have each user connect their clients to the server by going to Menu->Connect and enter the\n"
					+ "host's IP Adress and Port Number. These two numbers will need to be acquired from the host.\n", 
  				    "Connecting Clients", JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getSource() == Messaging) {
			JOptionPane.showMessageDialog(this,"Message All: To message all Users you can type in the box and press message all to do so\n"
					+"\nPrivate Message: To message one individual User you may click their name on the side\n"
  				    + "panel where the online users are displayed until it gets highlighted gray type a message\n"
  				    + "and press message!\n NOTE IT MAY REQUIRE MORE THAN ONE CLICK TO HIGHLIGHT THE NAME!!!!\n"
  				    + "\nDisconnect: To disconnect all you need to do is press the disconnect button and you will be \n"
  				    + "disconnected from the server.", "Messagin Options", JOptionPane.PLAIN_MESSAGE);
		}
		
		if(e.getSource() == Deck) {
			handleMoves = new GameFrameWork(discard, updatingDeck, userHand, out, in);
			this.userHand = handleMoves.DrawFromDeck();
			updateGUIHand(1);
			Deck.removeActionListener(this);
			DiscardPile.removeActionListener(this);
		}
		
		if(e.getSource() == DiscardPile) {
			handleMoves = new GameFrameWork(discard, updatingDeck, userHand, out, in);
			userHand = handleMoves.DrawFromDiscard();
			updateGUIHand(1);
			DiscardPile.removeActionListener(this);
			Deck.removeActionListener(this);
		}
		
		for(int i=0; i<Hand.length; i++) {
			if(e.getSource() == Hand[i]) {
				//select the card for...
				if(Hand[i].getBackground() == Color.GREEN) 
					{ Hand[i].setBackground(Color.WHITE); }
				else 						  
					{ Hand[i].setBackground(Color.GREEN); }
			}
		}
	}
	
	//MouseClicket handles the clicking of certain users from the online users panel
	public void mouseClicked(MouseEvent e) {
		//Assigns sendTo to the user that got clicked/highlighted
		if(usersArea.getSelectedText() !=null){
			sendTo = usersArea.getSelectedText();
		}
	}
	
	//SendMessage essentially sends the server the message that is to be sent to all
	//the clients
	public void sendMessage() {
		String msg = null;
		if(textField.getText().equals("")){
			msg = " ";
		}
		else{
			msg = textField.getText();
		}
		String message = getUsername() + ": " + msg;
        try {
			out.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Sends the message to the server
        textField.setText(""); //clear textfield
	}
	
	//SendPrivateMessage essentially sends the server the message that is to be sent to
	//One specific user
	public void sendPrivateMessage(String To){
		String msg = null;
		if(textField.getText().equals("")){
			msg = " ";
		}
		else{
			msg = textField.getText();
		}
		if(To!=null){
			String message = "To:"+To+":From:"+getUsername()+":"+msg;
			try {
				out.writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	//Sends the private message to the user
			textArea.append("PRIV MSG TO - "+To+"\n"+Username+": "+textField.getText()+"\n");
			textField.setText("");
		}
	}
	
	//Essentially returns false if the user is already in the online list
	public boolean userOnline(String userArea, String user){
		String delims = "\n";
		String[] tokens = userArea.split(delims);	//Splits the textArea up and compares it
		for(int i = 0;i<tokens.length;i++){			//to the user trying to be added to the list
			if(tokens[i].equals(user)){
				return false;
			}
		}
		return true;
	}
	//Repopulates melded hand
	public void populateMelds(Hashtable<String,ArrayList<Card>> tempMelds ){
		int amountOfMelds = 0;
		ArrayList<Card>allMelds = new ArrayList<Card>();
		
		for(int i = 0;i < PossibleKeys.length;i++){
			if(tempMelds.containsKey(PossibleKeys[i])){
				ArrayList<Card> tempMeldHand = tempMelds.get(PossibleKeys[i]);
				amountOfMelds+=tempMeldHand.size();
				for(int j=0; j<tempMeldHand.size(); j++) {
					Card tempCard = tempMeldHand.get(j);
					allMelds.add(tempCard);
				}
			}
		}
		
		initPlayerMelds(amountOfMelds);
		
		for(int i = 0;i < allMelds.size();i++){
					Card tempCard = allMelds.get(i);
					String tempSuit = tempCard.getSuitString();
					String tempRank = tempCard.getRankString();
					Icon suitIcon = new ImageIcon("Main game/src/resources/" + tempSuit + ".png");
					
					if(tempSuit == "hearts" || tempSuit == "diamonds")
						{ DiscardPile.setForeground(Color.RED); }
					if(tempSuit == "spades" || tempSuit == "clubs")
						{ DiscardPile.setForeground(Color.BLACK); }
					try {
						Meldings[i].setText(tempRank);
						Meldings[i].setIcon(suitIcon);
					  } catch(Exception e) {
						  System.out.println("Hand size: " + allMelds.size() + "\nError at JButton: " + i);
					  	}
						
		}
	}
	
	//Conditions
	//-3 : placing a meld 						 	: not implemented
	//-1 : discarding a card 						: not implemented
	// 0 : unknown / standard ?						: implemented
	// 1 : drawing card from deck / discard pile 	: not implemented
	public void updateGUIHand(int condition){
		ArrayList <Card> tempHand = userHand.getHand();
		Hashtable<String,ArrayList<Card>> tempMelds = userHand.getMelds();
		
		//standard case, simply overwrite JButtons
		if(condition == 0) {
			for(int i=0; i<tempHand.size(); i++) {
				Card tempCard = tempHand.get(i);
				String tempSuit = tempCard.getSuitString();
				String tempRank = tempCard.getRankString();
				Icon suitIcon = new ImageIcon("Main game/src/resources/" + tempSuit + ".png");
				if(tempSuit == "hearts" || tempSuit == "diamonds")
					{ DiscardPile.setForeground(Color.RED); }
				if(tempSuit == "spades" || tempSuit == "clubs")
					{ DiscardPile.setForeground(Color.BLACK); }
				try {
					Hand[i].setText(tempRank);
					Hand[i].setIcon(suitIcon);
				  } catch(Exception e) {
					  System.out.println("Hand size: " + tempHand.size() + "\nError at JButton: " + i);
				  	}
			}
		}
		//if first turn, need to use the extra JButton allowed during initialization
		//else set visibility true for next unused JButton, and set new appropriate values
		if(condition == 1) {
			Hand = null;
			p4.removeAll();
			p4.revalidate();
			p4.repaint();
			initPlayerHandButtons(tempHand.size());
			
			if (discard != null && discard.size() > 0) {
				initCenterButtons(discard.get(0).toString());
			} else {
				initCenterButtons("Discard");
			}
			
			for(int i=0; i<tempHand.size(); i++) {
				Card tempCard = tempHand.get(i);
				String tempSuit = tempCard.getSuitString();
				String tempRank = tempCard.getRankString();
				Icon suitIcon = new ImageIcon("Main game/src/resources/" + tempSuit + ".png");
				if(tempSuit == "hearts" || tempSuit == "diamonds")
					{ DiscardPile.setForeground(Color.RED); }
				if(tempSuit == "spades" || tempSuit == "clubs")
					{ DiscardPile.setForeground(Color.BLACK); }
				try {
					Hand[i].setVisible(true);
					Hand[i].setText(tempRank);
					Hand[i].setIcon(suitIcon);
					Hand[i].addActionListener(this);
				  } catch(Exception e) {
					  System.out.println("Hand size: " + tempHand.size() + "\nError at JButton: " + i);
				  	}
			}
			populateMelds(tempMelds);
		}
		
		//handles discard, remove card from hand and set corresponding JButton visibility false
		if(condition == -1) {
			Hand = null;
			p4.removeAll();
			p4.revalidate();
			p4.repaint();
			initPlayerHandButtons(tempHand.size());
			
			if (discard != null && discard.size() > 0) {
				initCenterButtons(discard.get(0).toString());
			} else {
				initCenterButtons("Discard");
			}
			
			for(int i=0; i<tempHand.size(); i++) {
				Card tempCard = tempHand.get(i);
				String tempSuit = tempCard.getSuitString();
				String tempRank = tempCard.getRankString();
				Icon suitIcon = new ImageIcon("Main game/src/resources/" + tempSuit + ".png");
				if(tempSuit == "hearts" || tempSuit == "diamonds")
					{ DiscardPile.setForeground(Color.RED); }
				if(tempSuit == "spades" || tempSuit == "clubs")
					{ DiscardPile.setForeground(Color.BLACK); }
				Hand[i].setText(tempRank);
				Hand[i].setIcon(suitIcon);
				Hand[i].addActionListener(this);
			}
			populateMelds(tempMelds);
		}


		//handles mass discard, same as condition -1 but on multiple JButtons
		if(condition == -3) {
			Hand = null;
			p4.removeAll();
			p4.revalidate();
			p4.repaint();
			initPlayerHandButtons(tempHand.size());

			if (discard != null && discard.size() > 0) {
				initCenterButtons(discard.get(0).toString());
			} else {
				initCenterButtons("Discard");
			}
			
			for(int i=0; i<tempHand.size(); i++) {
				Card tempCard = tempHand.get(i);
				String tempSuit = tempCard.getSuitString();
				String tempRank = tempCard.getRankString();
				Icon suitIcon = new ImageIcon("Main game/src/resources/" + tempSuit + ".png");
				if(tempSuit == "hearts" || tempSuit == "diamonds")
					{ DiscardPile.setForeground(Color.RED); }
				if(tempSuit == "spades" || tempSuit == "clubs")
					{ DiscardPile.setForeground(Color.BLACK); }
				try {
					Hand[i].setText(tempRank);
					Hand[i].setIcon(suitIcon);
					Hand[i].addActionListener(this);
				  } catch(Exception e) {
					  System.out.println("Hand size: " + tempHand.size() + "\nError at JButton: " + i);
				  	}
			}
			populateMelds(tempMelds);
		}
		//conditions -3 -1 and 1 all require the deck or discard pile to be handled as well  
	}
	//Enables disables buttons depening on if its the players turn
	public void enableDisableButtons(boolean start){
		if(start == true){
			options[1].addActionListener(this);
			options[2].addActionListener(this);
			options[3].addActionListener(this);
			options[4].addActionListener(this);
			for(int i = 0;i < Hand.length;i++){
				Hand[i].addActionListener(this);
			}
			Deck.addActionListener(this);
			DiscardPile.addActionListener(this);
		}
		else if (start == false){
			options[1].removeActionListener(this);
			options[2].removeActionListener(this);
			options[3].removeActionListener(this);
			options[4].removeActionListener(this);
			for(int i = 0;i < Hand.length;i++){
				Hand[i].removeActionListener(this);
			}
			Deck.removeActionListener(this);
			DiscardPile.removeActionListener(this);
		}
	}
	
	//Determines if the user has won
	public boolean isWinner(){
		if (userHand != null) {
			if (userHand.getHand() != null) {
				if (userHand.getHand().size() == 0) {
					
					try {
						out.writeObject(userHand);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
				}
			}
		}
		return false;
	}
	
	// Background thread runs this: show messages from other window
	@SuppressWarnings({ "unused", "unchecked" })
	public void run() {
		try {
			// Receive messages one-by-one, forever
			while (true) {
				// Get the next message
				Object msg = in.readObject();
				String message;
				
				if(msg instanceof User){
					setHand((User)msg);
					updateGUIHand(0);
				}
				
				else if(msg instanceof Deck){
					setUpdatingDeck((Deck)msg);
				}
				
				else if(msg instanceof ArrayList<?>){
					setDiscard((ArrayList<Card>)msg);
				}
				
				else if(msg instanceof Boolean){
					boolean start = (boolean)msg;
					enableDisableButtons(start);
				}
				
				else if (msg instanceof String) {
					message = (String) msg;
					// If message is null then that means the user is
					// disconnected from the server
					if (message == null) {
						textArea.append("\nDISCONNECTED FROM SERVER\n");
						usersArea = new JTextArea("");
					}

					// Updates online users
					else if ((message.length() >= 5)
							&& (message.substring(0, 4)).equals("User")) {
						String delims = ":";
						String[] tokens = message.split(delims);
						if (userOnline(usersArea.getText(), tokens[1])) {
							usersArea.append(tokens[1] + "\n");
						}
					}

					// Updates offline users
					else if ((message.length() >= 8)
							&& message.substring(0, 7).equals("Offline")) {
						String delims = ":";
						String[] tokens = message.split(delims);
						int n = usersArea.getText().indexOf(tokens[1]);
						usersArea.replaceRange("", n, n + tokens[1].length());
					}
					
					else if ((message.length() >= 12)
							&& message.substring(0, 12).equals("Disconnected")) {
						textArea.append("\nDisconnected\n");
						try {
							out.close();
							in.close();
							usersArea.setText("");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					// Updates general all chat
					else {
						// Print it to our text window
						textArea.append(message + "\n");
					}
				}
				
				if(isWinner()){
					JOptionPane.showMessageDialog(this,
							"You've won this round of Rummy!\nGoodBye!",
		  				    "Winner!", JOptionPane.PLAIN_MESSAGE);
					textArea.append("\nDisconnected\n");
					try {
						out.close();
						in.close();
						usersArea.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				}
			}
		} catch (IOException ie) {
			System.out.println("Swag4: "+ie);
			System.out.println(ie);
		} catch (ClassNotFoundException e) {
			System.out.println("Swag5: "+e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Manage connection method essentially handles the connection from the server to the client
	public void ManageConnection(String address){
		if (address != null) {	//If the user entered an address that wasnt null then 
			String machineName = null;
			int portNum = -1;
			//Essentially connects to the server
			try {
				String delims = ":";
				String[] tokens = address.split(delims);
				if(!(address.equals(""))){
					String port = tokens[1];
					machineName = tokens[0];
					portNum = Integer.parseInt(port);
				}
				
				if(machineName !=null && portNum!= -1){
					echoSocket = new Socket(machineName, portNum);	//Creates socket and input and output streams
					out = new ObjectOutputStream(echoSocket.getOutputStream());
					in = new ObjectInputStream(echoSocket.getInputStream());
					String username = JOptionPane	//Displays the following message to the user and prompts for the
							.showInputDialog(		// server that they want to connect to
									this,
									"Enter a username that you want to be known as: ");
					setUsername(username);
					out.writeObject(Username);
					connected = true;
					String connection = (String)in.readObject();
					textArea.append(connection+"\n");
					new Thread(this).start();
				}
				else{
					JOptionPane.showMessageDialog(this,
							"No IP or Port Entered!\n","Error", JOptionPane.PLAIN_MESSAGE);
				}
			} catch (NumberFormatException e) {
			} catch (UnknownHostException e) {
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
						"Couldn't get I/O for the connection to: "
								+ machineName,"Error", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	//Getters and Setters
	public Deck getUpdatingDeck() {
		return updatingDeck;
	}
	
	public void setUpdatingDeck(Deck updatingDeck) {
		this.updatingDeck = updatingDeck;
	}
	
	public ArrayList<Card> getDiscard() {
		return discard;
	}

	public void setDiscard(ArrayList<Card> discard) {
		this.discard = discard;
	}

	// Getter and setter functions
	public String getUsername() {
		return this.Username;
	}

	public void setUsername(String username) {
		this.Username = username;
	}

	public void setHand(User hand) {
		this.userHand = hand;
		Hand = null;
		p4.removeAll();
		p4.revalidate();
		p4.repaint();
		initPlayerMelds(0);
		initPlayerHandButtons(hand.getHand().size());
		initCenterButtons("Discards");
	}
	
	private String convertString(String d) {
		if(d.equals("10")) return "10";
		else return (""+d.charAt(0));
	}
	
	//Main method to bring everything back together
	public static void main(String[] args){
		test1 = new GameGUI();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();	//gets default location
		int x = (int) ((dimension.getWidth() - 1000) / 2);
		int y = (int) ((dimension.getHeight() - 400) / 2);
		test1.setLocation(x, y);										//Sets location for the board
		test1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//General exit operation
		test1.setJMenuBar(menu);//Adds the menu to the GUI
		test1.add(p1, BorderLayout.SOUTH);								//Adds the panels to the JFrame
		test1.add(p2, BorderLayout.EAST);
		test1.add(p3, BorderLayout.WEST);
		test1.add(p4, BorderLayout.CENTER);
		test1.setSize(1100, 375);										//Sets the size for the JFrame
		test1.setVisible(true);											//Sets the JFrame to be visible and
		test1.setResizable(true);	
	}

	//Empty method needed for mouselistener
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}

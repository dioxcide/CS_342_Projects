import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
//Main class where the GUI for the minesweeper is executed
public class MinesweeperGame extends JFrame implements MouseListener{ //Extended JFrame in order to make the GUI as well as implementing mouselistener in order to handle clicking actions
	private int seconds = 0, setTimer = 0, bombsLeft = 10, count = 0, TenthScore = 0;		//General variables to keep track of bombs left and time left	 
	private Node root;										//BST Node created to keep track of high Scores
	private static JPanel p1, p2;							//Created 2 panels one for the reset, time, and bombs display and the other being the board
	private JButton[][] grid = new JButton[10][10];			//Creating a grid of buttons for the board
	private final JButton reset = new JButton("Reset");		//Reset button
	private static JMenuBar menu = new JMenuBar();			//Menu Bar to display certain options
	private int mines[][] = new int[10][10];				//Keep track of board containing mines and numbers
	private JMenu m1 = new JMenu("Game");					//Menu that contains 3 sub items
	private JMenu m2 = new JMenu("Help");					//Menu that contains 2 sub items
	private JMenuItem exit = new JMenuItem("Exit");			//Sub MenuItem to menu m1
	private JMenuItem Score = new JMenuItem("Top Ten");
	private JMenuItem Reset = new JMenuItem("Reset");
	private JMenuItem Help = new JMenuItem("Help");			//Sub MenuItem to menu m2
	private JMenuItem About = new JMenuItem("About");	
	private JLabel timer = new JLabel("Time: ");			//JLabel for the timer display
	private JLabel bombs = new JLabel(String.valueOf("Bombs: "+bombsLeft));	//JLabel for bombs left
	private board gameboard = new board();					//Calling an instance of board to create a new board
	private HighScores scores = new HighScores();			//Calling an instance of HighScores in order to keep track of high Scores
	private final Icon m = new ImageIcon("m.jpg");			//Multiple Icons being declared that are used through the program
	private final Icon one = new ImageIcon("one.jpg");
	private final Icon two = new ImageIcon("two.jpg");
	private final Icon three = new ImageIcon("three.jpg");
	private final Icon four = new ImageIcon("four.jpg");
	private final Icon five = new ImageIcon("five.jpg");
	private final Icon six = new ImageIcon("six.jpg");
	private final Icon seven = new ImageIcon("seven.jpg");
	private final Icon eight = new ImageIcon("eight.jpg");
	private final Icon mine = new ImageIcon("mine.jpg");
	private final Icon Flag = new ImageIcon("mark.jpg");
	private Timer t = new Timer();							//Instance of time declared to create the clock
	
	public MinesweeperGame(){	
		super("MineSweeper");								//Super MineSweeper 
		menu.add(m1);										//Adds the first drop down menu
		m1.add(Score);										//Adds sub menus to the drop down menu
		m1.add(Reset);
		m1.add(exit);
		Score.addMouseListener(this);						//Implementing mouse listener for all the sub menus
		Reset.addMouseListener(this);
		exit.addMouseListener(this);
		menu.add(m2);										//Adds 2nd drop down menu
		m2.add(About);										//Adds sub menus to the drop down menu
		m2.add(Help);
		About.addMouseListener(this);						//Implementing mouse listener for all sub menus
		Help.addMouseListener(this);
		reset.addMouseListener(this);
		bombs.setBackground(Color.WHITE);					//Sets BackGround and Foreground colors
		bombs.setForeground(Color.RED);						//For Both JLabels (timer and bombs)
		timer.setBackground(Color.WHITE);
		timer.setForeground(Color.RED);
		
		File file = new File("HighScores.txt");
        try {
             Scanner scanner = new Scanner(file); 
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(":");
                int t = Integer.parseInt(tokens[1]);
                String name = tokens[0];
                addNode(t, name);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		
		p1 = new JPanel(new GridLayout(1,3));				//Creates the 1st JPanel containing a 1 row 3 column set up
		reset.setIcon(new ImageIcon("angrybird.gif"));		//Adds an icon to the reset button
		p1.add(bombs);										//Adds the following buttons to the JPanel
		p1.add(reset);
		p1.add(timer);
		
		p2 = new JPanel();									//Creates 2nd JPanel that has a 10 row by 10 column setup
		p2.setLayout(new GridLayout(10, 10));
		for(int i = 0; i<10;i++){		
			for(int j = 0; j<10;j++){
				grid[i][j] = new JButton("");				//Double for Loop to initialize all the buttons in grid 
				grid[i][j].setBackground(Color.LIGHT_GRAY);	//And then adding them to the panel
				grid[i][j].addMouseListener(this);
				p2.add(grid[i][j]);
			}
		}
		
		mines = gameboard.getBoard();					//Calls gameboard method getBoard() in order to get int board
	}													//that has all the info about the board
	
	private class UpdateUITask extends TimerTask {	//Class invokes the timer 
        public void run() {
            EventQueue.invokeLater(new Runnable() {
            	public void run() {
                    timer.setText(String.valueOf("Time "+seconds++));
                }
            });
        }
    }

	public void mouseClicked(MouseEvent e){			//Method handles user clicks on the board
		if(e.getButton() == MouseEvent.BUTTON1){	//Goes through if the user right clicks a button on the board
			for (int x = 0; x < 10; x++) {
	            for (int y = 0; y < 10; y++) {
	                if (e.getSource() == grid[x][y] && grid[x][y].getIcon() != m && grid[x][y].getIcon() != Flag) {	
	                	if(setTimer == 0){	//Initializes the timer 
	                		t = new Timer();
	        				t.schedule(new UpdateUITask(), 0, 1000);
	        				setTimer++;
	        			}
	             
	                	grid[x][y].removeMouseListener(this);	//Makes the button not clickable afterwards
	                    
	                	if (mines[x][y] >= 99) {		//If the button contains a mine then the method displayMines is 
							grid[x][y].setIcon(mine);	//Called
							displayMines();
							break;
	                    } 
	                    else if (mines[x][y] != 0) {	//If the button pressed has more than one mine around it
	                    	if(mines[x][y] == 1){		//It will do the following
	                    		grid[x][y].setIcon(one);			//If it only has one mine around it will display
	                            grid[x][y].setBackground(Color.WHITE);	// the icon one
	                    	}
	                    	else if(mines[x][y] == 2){           //If it has multiple mines in it will display the certain         		
	                    		grid[x][y].setIcon(two);		//Icon pertaining to how many mines are around the button
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    	else if(mines[x][y] == 3){  			  		
	                    		grid[x][y].setIcon(three);
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    	else if(mines[x][y] == 4){	
	                    		grid[x][y].setIcon(four);
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    	else if(mines[x][y] == 5){
	                    		grid[x][y].setIcon(five);
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    	else if(mines[x][y] == 6){
	                    		grid[x][y].setIcon(six);
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    	else if(mines[x][y] == 7){
	                    		grid[x][y].setIcon(seven);
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    	else if(mines[x][y] == 8){
	                    		grid[x][y].setIcon(eight);
	                            grid[x][y].setBackground(Color.WHITE);
	                    	}
	                    } 
	                    else if (mines[x][y] == 0){				//If they pressed a button with 0 mines around it
	                    	floodFill(x, y);					//then floodfill is invoked
	                    }
	                    checkWin();								//After each click checkWin is called to see if the user
	                }											//has found all the mines
	            }
	        }
		}
		else if(e.getButton() == MouseEvent.BUTTON3){ 		//Only evaluates when the user right clicks
			for (int x = 0; x < 10; x++) {
	            for (int y = 0; y < 10; y++) {
	                if (e.getSource() == grid[x][y]) { //Essentialy replaces the blank button with a picture
	                	if(grid[x][y].getBackground() == Color.LIGHT_GRAY  && bombsLeft != 0 && grid[x][y].getIcon() == null){
	                		bombs.setText(String.valueOf("Bombs: "+ --bombsLeft));//Decrements bombs from JLabel bomb
	                		grid[x][y].setIcon(m);				//First right click replaces button with an M icon
	                	}
	                	else if(grid[x][y].getIcon() == m && grid[x][y].getBackground() == Color.LIGHT_GRAY){
	                		bombs.setText(String.valueOf("Bombs: "+ ++bombsLeft));	//Increments bombs from JLabel
	                		grid[x][y].setIcon(Flag);		//2nd right click sets the button icon to flag
	                	}
	                	else if(grid[x][y].getIcon() == Flag){		//Sets the button icon back to null if they 
	                		grid[x][y].setIcon(null);				//click 3 times
	                	}
	                }
	            }
	        }
		}
	}
	
	public void mousePressed(MouseEvent e){ //Another mouse event handler which essentially handles the menus.
		if (e.getSource() == reset || e.getSource() == Reset) {	//Handles reset menu button and reset button in the p1 panel
			t.cancel();		//Stops the timer
	    	t.purge();		
			gameboard = new board();//Gets a new board for a new set of mines
			bombsLeft = 10;			//Resets bombsLeft in the jLabel to 10
			seconds = 0; 			//Resets Seconds passed to 0
			count = 0;
			setTimer = 0;
            mines = gameboard.getBoard();	//Retrieves newly created board from the gameboard class
            for (int x = 0; x < 10; x++) {		
                for (int y = 0; y < 10; y++) {
                	grid[x][y].removeMouseListener(this);		//Double for loop to reinitialize each button
                    grid[x][y].addMouseListener(this);			//Back to its untouched state
                    grid[x][y].setBackground(Color.LIGHT_GRAY);
                    grid[x][y].setIcon(null);
                }
            }
        }
		else if(e.getSource() == exit){		//Handles the exit menu item
			System.exit(0);					//Exits program
		}
		else if(e.getSource() == About){	//Handles about menu item
			JOptionPane.showMessageDialog(this,
					"              This game was created\n                                   by\n                        Antonio Villarreal\n                                  and\n                  Joseph Grosspietsch","About",
					JOptionPane.PLAIN_MESSAGE); 	//Displays the creators of the game
		}
		else if(e.getSource() == Help){	//Displays a general dialogue to help understand the objective of the game
			JOptionPane.showMessageDialog(this,
					"The goal of the game is to uncover all the squares that do not contain mines (with the left mouse button)\n"
					+ "without being blown up by clicking on a square with a mine underneath.\n"
					+ "The location of the mines is discovered by a process of logic\n. "
					+ "Clicking on the game board will reveal what is hidden underneath the chosen\n"
					+ "square or squares (a large number of blank squares may be revealed in one go if \n"
					+ "they are adjacent to each other). Some squares are blank but some\n"
					+ "contain numbers (1 to 8), each number being the number of mines adjacent to the\n"
					+ "uncovered square. To help avoid hitting a mine, the location of a suspected mine\n"
					+ "can be marked by flagging it with the right mouse button. The game is won once\n"
					+ "all blank squares have been uncovered without hitting a mine, any remaining mines\n"
					+ " not identified by flags being automatically flagged by the computer.","Help",
					JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getSource() == Score){	//Calls the score class and displays the top ten scores
			scores = new HighScores();	//Calls the highscore class 
    		scores.displayData(root);	//Sets the BST Tree too displayData to be stored in the score board.
			scores.displayTopTen();
		}
	}
	
	public void mouseEntered(MouseEvent e) {}	//The following three methods were required in order to use
												//MouseListener they essentially do nothing
	public void mouseExited(MouseEvent e){}
	
	public void mouseReleased(MouseEvent e){}
	
    public void floodFill(int x, int y) {		//FloodFill Method
        if (x >= 0 && x <= 9 && y >= 0 && y <= 9) {
            for (int z = 1; z < 10; z++) {		//For loop goes through 1-9 trying to determine what mine at x and y is
                if (mines[x][y] == z) {
                	if(mines[x][y] == 1){			//Sets respective icon according to number of bombs around it
                		grid[x][y].setIcon(one);
                        grid[x][y].setBackground(Color.WHITE);
                        
                	}
                	else if(mines[x][y] == 2){
                		grid[x][y].setIcon(two);
                        grid[x][y].setBackground(Color.WHITE);
                        
                	}
                	else if(mines[x][y] == 3){
                		grid[x][y].setIcon(three);
                        grid[x][y].setBackground(Color.WHITE);
                	}
                	else if(mines[x][y] == 4){
                		grid[x][y].setIcon(four);
                        grid[x][y].setBackground(Color.WHITE);
                	}
                	else if(mines[x][y] == 5){
                		grid[x][y].setIcon(five);
                        grid[x][y].setBackground(Color.WHITE);
                        
                	}
                	else if(mines[x][y] == 6){
                		grid[x][y].setIcon(six);
                        grid[x][y].setBackground(Color.WHITE);
                	}
                	else if(mines[x][y] == 7){
                		grid[x][y].setIcon(seven);
                        grid[x][y].setBackground(Color.WHITE);
                	}
                	else if(mines[x][y] == 8){
                		grid[x][y].setIcon(eight);
                        grid[x][y].setBackground(Color.WHITE);
                	}
                    return;
                }
            }
            if (mines[x][y] == 0 && grid[x][y].getBackground() != Color.WHITE) {	//Recursively calls
            	grid[x][y].setBackground(Color.WHITE);							//FloodFill to fill in blank squares
                grid[x][y].removeMouseListener(this);							//When a user presses one
                floodFill(x - 1, y);
                floodFill(x + 1, y);
                floodFill(x, y - 1);
                floodFill(x, y + 1);
            }
        }
    }

    public void displayMines(){		//Display Mines method
    	t.cancel();			//Stops timer
    	t.purge();
    	for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				grid[x][y].removeMouseListener(this);
				if (mines[x][y] >= 99) {	//For loop That reveals the mines on the board to the user
					grid[x][y].setIcon(mine);
				}
			}
		}
    	JOptionPane.showMessageDialog(this,
				"YOU LOST LOSER!!","You Just Lost",		//Displays the following dialog to the user
				JOptionPane.PLAIN_MESSAGE);
    }
    
    public void checkWin(){	//checkWin method checks if the use has won
    	int totalChecked = 0;
    	for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				if (mines[x][y] != 99 && grid[x][y].getBackground() == Color.WHITE) {
					totalChecked++;		//Double For Loop checks if the user has revealed all 90 buttons
				}						//That do not contain mines
			}
		}	
    	if(totalChecked == 90){	//If the user revealed all 90 squares the if statement gets executed
    		t.cancel();	//Stops time
    		t.purge();
    		for (int x = 0; x < 10; x++) {
    			for (int y = 0; y < 10; y++) {
    				if (mines[x][y] >= 99) {
    					grid[x][y].setIcon(m);	//Displays where all the remaining mines were
    				}
    			}
    		}
    		
    		Traversal(root);

    		if(TenthScore == 0 || seconds <= TenthScore){
    			String input = JOptionPane	//Displays the following message to the user and prompts for their name
        				.showInputDialog(
        						this,
        						"Congrats bruh! You Just Won!\nEnter your name to be added to the high score list: ");
	    		scores = new HighScores();	//Calls the highscore class 
	    		addNode(seconds, input);	//Adds the high score to the BST Implemented in this File
	    		scores.displayData(root);	//Sets the BST Tree too displayData to be stored in the score board.
    		}
    		else{
        		JOptionPane	//Displays the following message to the user and prompts for their name
				.showMessageDialog(
						this,
						"Congrats bruh! You Just Won!\nEnter your name to be added to the high score list: ");
    		}
    	}
    }
    
    public void addNode(int time, String name){//General method to add to BST 
		Node newNode = new Node(time, name);	//Declares a new Node
		if(root == null){			//Recursively adds to the BST 
			root = newNode;
		}
		else{
			Node tempNode = root;
			Node parent;
			while(true){
				parent = tempNode;
				if(time<tempNode.time){
					tempNode = tempNode.left;
					if(tempNode == null){
						parent.left = newNode;
						return;
					}
				}
				else{
					tempNode = tempNode.right;
					if(tempNode == null){
						parent.right = newNode;
						return;
					}
				}
				
			}
		}
	}
    
    public void Traversal(Node tempNode){	//Method adds top 10 scores from BST to arraylist
		if(tempNode!=null){		//Traverses the list in order to add to the arrayLists
			Traversal(tempNode.left);
			count++;
			if(count == 10){
				TenthScore = tempNode.time;
			}
			Traversal(tempNode.right);
		}
	}
    
	public static void main(String[] args) {	//MAIN METHOD
		MinesweeperGame mainBoard = new MinesweeperGame();	//Makes an instance of the class MinesweeperGame
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();	//gets default location
		int x = (int) ((dimension.getWidth() - 500) / 2);
		int y = (int) ((dimension.getHeight() - 500) / 2);

		mainBoard.setLocation(x, y);										//Sets location for the board
		mainBoard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//General exit operation
		mainBoard.setJMenuBar(menu);										//Adds the menu to the GUI
		mainBoard.add(p1, BorderLayout.NORTH);								//Adds the panels to the JFrame
		mainBoard.add(p2, BorderLayout.CENTER);
		mainBoard.setSize(500, 500);										//Sets the size for the JFrame
		mainBoard.setVisible(true);											//Sets the JFrame to be visible and
		mainBoard.setResizable(true);										//Resizable
	}
}

class Node{ //Node Class for BST
	int time;			//BST Stores time and name of the user in order to keep track of high scores.
	String name;
	Node left;
	Node right;
	
	Node(int time, String name){
		this.time = time;
		this.name = name;
	}
	public String toString(){
		return name+ " has time " +time;
	}
}

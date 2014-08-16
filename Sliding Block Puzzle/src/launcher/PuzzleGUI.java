package launcher;

import classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JPanel;
//Main class that brings everything together and implements the GUI 
public class PuzzleGUI extends JFrame implements MouseMotionListener{
	//Instances of Classes
	private FileInput boardPieces = new FileInput(puzzleNumber);
	private int numPieces = boardPieces.getNumPieces();
	private Pieces[] currentPiece = new Pieces[numPieces];
	private PuzzleSolverV2 solved;
	//
	private static int puzzleNumber = 0;
	private static int maxPuzzles = 12;						//change if adding puzzles
	private static final long serialVersionUID = 1L;
	private char[] mobility = new char[numPieces];
	private static JPanel p1, p2, p3;						
	private JButton[] grid = new JButton[numPieces];
	private Rectangle[] grid2 = new Rectangle[numPieces];
	private static JMenuBar menu = new JMenuBar();			//Menu Bar to display certain options
	private JMenu m1 = new JMenu("Game");					//Menu that contains 3 sub items
	private JMenuItem exit = new JMenuItem("Exit");			//Sub MenuItem to menu m1
	private JMenuItem Help = new JMenuItem("Help");			//Sub MenuItem to menu m2
	private JMenuItem About = new JMenuItem("About");
	private JMenuItem Next = new JMenuItem("Next");
	private JMenuItem Previous = new JMenuItem("Previous");
	private JButton Hint = new JButton("Hint");
	private JButton Solve = new JButton("Solve");
	private JButton Reset = new JButton("Reset");
	private JLabel timer, moves, min;
	private Timer timeClock;
	private int timeAccumulator, moveCount, min2Solve = 0;
	
	//General setup of the gui of the program
	public PuzzleGUI (int puzzleNumber) {
		super("Sliding Block Puzzle");
		int start, end, height, width;
		p1 = new JPanel();	//Creates 3 jpanels
		p2 = new JPanel();
		p3 = new JPanel();
		
		menu.add(m1);				//Basically creates the menu
		m1.add(About);
		m1.add(Help);
		m1.add(Next);
		m1.add(Previous);
		m1.add(exit);
		
		p2.setLayout(null);
		for(int i =0;i<numPieces;i++){								//Basically creates all the puzzles pieces and adds them to the 2nd jpanel
			start = boardPieces.blockStart(i)*75;
			end = boardPieces.blockEnd(i)*75;
			height = boardPieces.blockLength(i)*75;
			width = boardPieces.blockWidth(i)*75;
			mobility[i] = boardPieces.blockMobile(i);
			
			grid[i] = new JButton();
			grid[i].addMouseMotionListener(this);
			grid[i].setBackground(Color.LIGHT_GRAY);
			grid[i].setBounds(end, start, width, height);
			currentPiece[i] = new Pieces((start/75), (end/75), (width/75), (height/75), i+1, mobility[i]);	//Also creates each indiviudal piece using the pieces class
			grid2[i] = new Rectangle(end, start, width, height);
			p2.add(grid[i]);
			
			try {																						//Adds nice wood photo to each piece
				Image icon = ImageIO.read(getClass().getResourceAsStream("/resources/" + "wood.gif"));
			    Image scaledIcon = icon.getScaledInstance(width+50, height, Image.SCALE_FAST);
			    repaint();
			    grid[i].setIcon(new ImageIcon(scaledIcon));
			    if(i==numPieces-1){
			    	grid[i].setText("Z");
			    }
			    else{
			    	grid[i].setText(""+(i+1));
			    }
			    grid[i].setHorizontalTextPosition(JButton.CENTER);
			    grid[i].setVerticalTextPosition(JButton.CENTER);
			    grid[i].setFont(new Font("Monospaced", Font.BOLD, 32));
			  } catch(IOException ex){}
			
		}
		
		p3.add(Reset);					//Delcaration of more buttons
		p3.add(Hint);					
		p3.add(Solve);
		
		timer = new JLabel("| Timer: " + timeAccumulator + " | ");			//Setting a couple of JLabels to keep track of time moves and least amount of moves...
		int delay = 1000;
	    timeClock = new Timer(delay,new TimerHandler() );
	    timeClock.start();
		menu.add(timer);
		
		moves = new JLabel(" Moves: " + moveCount + " | ");
		menu.add(moves);
		
		min = new JLabel(" Minimum: " + min2Solve + " ");
		menu.add(min);
		
		implementListeners();
		
	}
	
	private void implementListeners() {
		// when user clicks alt+g allows for shortcuts mentioned below 
		m1.setMnemonic('g');
			
		exit.setMnemonic('x');
		exit.addActionListener(
				new ActionListener() {
					// terminate application when user clicks exitItem, or alt+x
					public void actionPerformed(ActionEvent event) { System.exit(1); }
			    }
		);
		Help.setMnemonic('h');
		Help.addActionListener(
				new ActionListener() {
					// displays help when user clicks HelpItem, or alt+p
					public void actionPerformed(ActionEvent event) {
						JOptionPane.showMessageDialog( PuzzleGUI.this,
			    				  "For more information about Blocked go to:\n"
			    				  + "http://en.wikipedia.org/wiki/Sliding_puzzle", 
			    				  "About", JOptionPane.PLAIN_MESSAGE );
					}
			    }
		);
		About.setMnemonic('a');
		About.addActionListener(
				new ActionListener() {
					// displays about information when user clicks AboutItem, or alt+a
					public void actionPerformed(ActionEvent event) {
						JOptionPane.showMessageDialog( PuzzleGUI.this,
			    				  "Developers:\nAntonio Villarreal & Krassimir Manolov", 
			    				  "About", JOptionPane.PLAIN_MESSAGE );
					}
			    }
		);
		Next.setMnemonic('n');
		Next.addActionListener(
				new ActionListener() {
					// displays about information when user clicks AboutItem, or alt+a
					public void actionPerformed(ActionEvent event) {
						dispose();
						puzzleNumber++;
						if(puzzleNumber == maxPuzzles) { puzzleNumber=0; }
						restart(puzzleNumber);
					}
			    }
		);
		Previous.setMnemonic('p');
		Previous.addActionListener(
				new ActionListener() {
					// displays about information when user clicks AboutItem, or alt+a
					public void actionPerformed(ActionEvent event) {
						dispose();
						puzzleNumber--;
						if(puzzleNumber == -1) { puzzleNumber=(maxPuzzles-1); }
						restart(puzzleNumber);
					}
			    }
		);
		Reset.setMnemonic('r');
		Reset.addActionListener(
				new ActionListener() {
					// reinitializes game when user clicks resetItem, or alt+r
					public void actionPerformed(ActionEvent event) { 
						dispose();
						restart(puzzleNumber);
					}
			    }
		);
		Hint.setMnemonic('h');
		Hint.addActionListener(
				new ActionListener() {
					// reinitializes game when user clicks resetItem, or alt+h
					public void actionPerformed(ActionEvent event) { 
						solved = new PuzzleSolverV2(currentPiece, numPieces, 6,6);
						solved.printHint(numPieces);
					}
			    }
		);
		Solve.setMnemonic('s');
		Solve.addActionListener(
				new ActionListener() {
					// reinitializes game when user clicks resetItem, or alt+s
					public void actionPerformed(ActionEvent event) { 
						solved = new PuzzleSolverV2(currentPiece, 8, 6,6);
						solved.printSolution();
					}
			    }
		);
	}
	
	private class TimerHandler implements ActionListener {
		// accumulates time between events (every second)
	    public void actionPerformed(ActionEvent event) {
	    	// very weird but works, change if see fit
	    	timeAccumulator += timeClock.getDelay() / 1000;
	    	timer.setText("| Timer: " + timeAccumulator + " | ");
	    }
	}
	
	public void stopTimer() {
		// stops the timer when necessary
		timeClock.stop();
	}
	
	public void addMove() {
		moveCount++;
		moves.setText(" Moves: " + moveCount + " |");
	}
	
	public void restart(int PuzzleNumber) {
		//makes sure all old data is removed, maybe redundant
		p1.removeAll();
		p2.removeAll();
		p3.removeAll();
		menu.removeAll();
		
		//copied over main method code to reinitialize game
		PuzzleGUI  GUI = new PuzzleGUI(PuzzleNumber);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();	//gets default location
		int x = (int) ((dimension.getWidth() - 500) / 2);
		int y = (int) ((dimension.getHeight() - 500) / 2);
		GUI.setLocation(x,y);
		GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		GUI.setJMenuBar(menu);
		GUI.add(p1, BorderLayout.NORTH);								//Adds the panels to the JFrame
		GUI.add(p2, BorderLayout.CENTER);
		GUI.add(p3, BorderLayout.SOUTH);
		GUI.setSize(465, 560);										//Sets the size for the JFrame
		GUI.setVisible(true);											//Sets the JFrame to be visible and
		GUI.setResizable(true);
	}
	//Boolean detects if the user has gotten the Z piece to the right side
	public boolean isWinner(){
		boolean playerWon = false;
		int x = grid[numPieces-1].getX();
		x+= grid[numPieces-1].getWidth();

		if (x == 450) {			//If the piece reaches 450 the user has won
			playerWon = true;
		}
		return playerWon;
	}
	//Collision method that detects collisions between pieces.
	public boolean isCollision(Rectangle r1, int index, int x, int y, JButton temp){
		boolean collision = false;
		
		for(int i=0;i<numPieces;i++){		//Detects if the piece intersects any of the other pieces
			if(grid2[i].intersects(r1) && i != index){
				return collision = false;
			}
		}
		
		x += temp.getWidth();
		y += temp.getHeight();
		
		if(x >=0 && x <= 450){		//Makes sure the pieces dont go out of bounds
			if(y>=0 && y<=450){
				collision = true;
			}
		}
		
		return collision;
	}
	@Override
	public void mouseDragged(MouseEvent e) {		//Mouse Eventhandler that handles mouse movement
		int nX, nY, x, y, finalX, finalY, var, var2;
		Rectangle temp;
		
		for(int i = 0;i<numPieces;i++){
			if (e.getSource() == grid[i]) {
				temp = grid2[i];
				nX = grid[i].getX();
				nY = grid[i].getY();
				var = nX / 75;
				var2 = nY / 75;
				x = e.getX();
				y = e.getY();
				if (var != 0) {
					x *= var;
				}
				if (var2 != 0) {
					y *= var2;
				}
				
				finalX = x - nX;
				finalY = y - nY;
				if(finalX >150 && (mobility[i] == 'b' || mobility[i] == 'h')) {//Handles the movement depending if the user wants to move up down right or left...
					temp.setLocation(nX+75,nY);
					if(isCollision(temp, i, nX+75,nY, grid[i])){
						grid[i].setLocation(nX + 75, nY);
						currentPiece[i].y +=1;
						grid2[i].setLocation(nX + 75, nY);
						addMove();
					}
				}
				else if(finalX < -150 && (mobility[i] == 'b' || mobility[i] == 'h')) {//Handles the movement depending if the user wants to move up down right or left...
					temp.setLocation(nX-75,nY);
					if(isCollision(temp, i, nX-75,nY, grid[i])){
						grid[i].setLocation(nX - 75, nY);
						currentPiece[i].y -=1;
						grid2[i].setLocation(nX - 75, nY);
						addMove();
					}
				}
				else if(finalY > 150 && (mobility[i] == 'b' || mobility[i] == 'v')) {//Handles the movement depending if the user wants to move up down right or left...
					temp.setLocation(nX,nY+75);
					if(isCollision(temp, i, nX,nY+75, grid[i])){
						grid[i].setLocation(nX, nY+75);
						currentPiece[i].x +=1;
						grid2[i].setLocation(nX, nY+75);
						addMove();
					}
				}
				else if(finalY < -150 && (mobility[i] == 'b' || mobility[i] == 'v')) {//Handles the movement depending if the user wants to move up down right or left...
					temp.setLocation(nX,nY-75);
					if(isCollision(temp, i, nX,nY-75, grid[i])){
						grid[i].setLocation(nX, nY-75);
						currentPiece[i].x -=1;
						grid2[i].setLocation(nX, nY-75);
						addMove();
					}
				}
			}
		}
		
		if(isWinner()){
			JOptionPane	//Displays the following message to the user and prompts for their name
			.showMessageDialog(
					this,
					"Congrats bruh! You Just Solved The Puzzle!\n");
			stopTimer();
			
			//on to the next one...
			dispose();
			puzzleNumber++;
			
			if(puzzleNumber < 10) { 
				restart(puzzleNumber); 
			}
			else {
				stopTimer();
				JOptionPane.showMessageDialog(this,"No More Puzzles for You, Mister!\n");	//User recieves this dialogue if they have completed all the puzzles
				System.exit(1);
			}
			
		}
	}

	public void mouseMoved(MouseEvent e) {}
	
	public static void main(String[] args){
		PuzzleGUI  GUI = new PuzzleGUI(puzzleNumber);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();	//gets default location
		int x = (int) ((dimension.getWidth() - 500) / 2);
		int y = (int) ((dimension.getHeight() - 500) / 2);
		GUI.setLocation(x,y);
		GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		GUI.setJMenuBar(menu);
		GUI.add(p1, BorderLayout.NORTH);								//Adds the panels to the JFrame
		GUI.add(p2, BorderLayout.CENTER);
		GUI.add(p3, BorderLayout.SOUTH);
		GUI.setSize(465, 560);										//Sets the size for the JFrame
		GUI.setVisible(true);											//Sets the JFrame to be visible and
		GUI.setResizable(false);
	}
}
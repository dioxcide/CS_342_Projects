package classes;

import java.util.*;

//PuzzleSolver Class essentially creates a hint/solution for the user if they requested it.

public class PuzzleSolverV2
{
	private Move hint = null;	//Move to store hint
	private Snapshot solution = null;				//SnapShot to store the solution 
	private ArrayList<Snapshot> queue = new ArrayList<Snapshot>();		// Snapshots to process
	private HashSet<Integer> pConfigs;	// Previous configurations of the board
	private int rows;				//ROws of the board
	private int columns;			//Columns of the Board
	private int movesNeeded = 0;	//Moves needed variable for later use

	//Main Method that initializes the puzzle solver and performs BFS
	public PuzzleSolverV2(Pieces[] blocks, int numBlocks, int rows, int columns){	//Takes the Pieces from the board as parameters and the number of them including rows 
		this.rows = rows;															//Columns
		this.columns = columns;
		Snapshot FirstSnap = new Snapshot(null,blocks, numBlocks, 'a', 0, 0);	//Creates first snap shot
		pConfigs = new HashSet<Integer>();									//sets new hashset
		queue.add(FirstSnap);												//Adds first board to queue
		HashKey(FirstSnap.boardSnap());										//Adds first board to hashset
		
		while(!queue.isEmpty()){											//While loop to process queue
			FirstSnap = queue.get(0);										//Gets first board on the queue
			queue.remove(0);												//and removes it
			Pieces[] updatedPieces = FirstSnap.piecesSnap();				//Gets pieces
			
			if(foundSolution(FirstSnap)){									//If a solution is found the loop breaks
				solution = FirstSnap;
				hint = FirstSnap.moves.get(1);
				break;
			}
			
			for(int i=0;i<numBlocks;i++){									//For loop that goes through each piece and sees if they can be moved...
				char movement = updatedPieces[i].mobility;
				
				switch(movement){
					case 'h':
						Horizontal(FirstSnap, i);
						break;
					case 'v':
						Vertical(FirstSnap, i);
						break;
					case 'b':
						Horizontal(FirstSnap, i);
						Vertical(FirstSnap, i);
						break;
					default:
						break;
				}
			}
		}
		
		if (solution == null)
			System.out.println("Puzzle has no solution");
	}
	//Method that sees if the current block can be moved horizontally in either direction
	public void Horizontal(Snapshot temp, int specificBlock){
		Pieces[] tempPiece = temp.piecesSnap();									//Gets the certain pieces values x,y,width, height, etx
		int x = tempPiece[specificBlock].x;
		int y = tempPiece[specificBlock].y;
		int width = tempPiece[specificBlock].width;
		int height = tempPiece[specificBlock].height;
		int id = tempPiece[specificBlock].id;
		char mobility = tempPiece[specificBlock].mobility;
		boolean Right = tempPiece[specificBlock].moveRight(temp.boardSnap());			//Booleans to determine if the piece can be moved left or right
		boolean Left = tempPiece[specificBlock].moveLeft(temp.boardSnap());
		
		if(Right){																		//If the piece can be moved to the right then a new snap shot is created
																						//Of the piece moving to the right
			Pieces movedPiece = new Pieces(x,y+1,width,height,id,mobility);
			tempPiece[specificBlock] = movedPiece;
			Snapshot newRightSnap = new Snapshot(temp, tempPiece, tempPiece.length, 'r', 1, id);
			
			if (HashKey(newRightSnap.board) == true){
				queue.add(newRightSnap);
			}
		}
		if(Left){																		//If the piece can move to the left then a new snapshot is created 
																						//One in which the piece is moving to the left
			Pieces movedPiece = new Pieces(x,y-1,width,height,id,mobility);
			tempPiece[specificBlock] = movedPiece;
			Snapshot newLeftSnap = new Snapshot(temp, tempPiece, tempPiece.length, 'l', -1, id);
			
			if (HashKey(newLeftSnap.board) == true){
				queue.add(newLeftSnap);
			}
		}
	}
	//Vetical method essentially if the specific piece can be moved up or down and if it can it creates a new snap shot of it
	public void Vertical(Snapshot temp, int specificBlock){
		Pieces[] tempPiece = temp.piecesSnap();							//Stores all values pertaining to the certain piece
		int x = tempPiece[specificBlock].x;
		int y = tempPiece[specificBlock].y;
		int width = tempPiece[specificBlock].width;
		int height = tempPiece[specificBlock].height;
		int id = tempPiece[specificBlock].id;
		char mobility = tempPiece[specificBlock].mobility;
		boolean Up = tempPiece[specificBlock].moveUp(temp.boardSnap());
		boolean Down = tempPiece[specificBlock].moveDown(temp.boardSnap());
		
		if(Up){																					//If the piece can move up then it creates a new snapshot of the piece Going up
			Pieces movedPiece = new Pieces(x-1,y,width,height,id,mobility);
			tempPiece[specificBlock] = movedPiece;
			Snapshot newUpSnap = new Snapshot(temp, tempPiece, tempPiece.length, 'u', 1, id);
			
			if (HashKey(newUpSnap.board) == true){
				queue.add(newUpSnap);
			}
		}
		if(Down){																				
			Pieces movedPiece = new Pieces(x+1,y,width,height,id,mobility);					//If the piece can move down it creates a snap shot of it moving down
			tempPiece[specificBlock] = movedPiece;
			Snapshot newDownSnap = new Snapshot(temp, tempPiece, tempPiece.length, 'd', -1, id);
			
			if (HashKey(newDownSnap.board) == true){
				queue.add(newDownSnap);
			}
		}
	}
	//Snap shot class that holds all the information of that current snapshot like board pieces etc
	private class Snapshot
	{
		private int board[][];			// Current board
		public ArrayList<Move> moves = new ArrayList<Move>();	// Moves it took to get this board
		private Pieces[] snapshotPieces;
		
		public Snapshot(Snapshot old, Pieces[] blocks, int numBlocks, char direction, int value, int ID){	//Main method to store snapshot
			this.board = createBoard(blocks, numBlocks);
			this.snapshotPieces = blocks;
			printBoard(board);
			if(movesNeeded !=0){
				for (int i=0; i<old.moves.size(); i++)				//Stores all previous moves to get to the current board
				{
					Move temp = old.moves.get(i);
					this.moves.add(temp);
				}
			
				Move temp = new Move(direction, value, ID);
				this.moves.add(temp);
			}
			else if(movesNeeded==0){
				Move temp = new Move(direction, value, ID);
				this.moves.add(temp);
			}
			movesNeeded++;
			
		}
		public int[][] boardSnap(){
			return this.board;
		}
		public Pieces[] piecesSnap(){
			return this.snapshotPieces;
		}
	}
	
	public int[][] createBoard(Pieces[] blocks, int numBlocks){	//Creates a board of arbitrary size and returns it
		int [][] temp = new int[rows][columns];
		
		for(int i=0;i<numBlocks;i++){
			int x = blocks[i].x;			//Gets piece info and creates the board
			int y = blocks[i].y;
			int height = blocks[i].height; 
			int width = blocks[i].width;
			int id = blocks[i].id;
			
			for(int j=0;j<height;j++){
				for(int k=0;k<width;k++){
					temp[x+j][y+k] = id;
				}
			}
		}
		return temp;
	}
	
	public void printBoard(int board[][]){			//Method function to print out the board
		for(int j=0;j<rows;j++){
			for(int k=0;k<columns;k++){
				System.out.printf("%d ", board[j][k]);
			}
			System.out.printf("\n");
		}
	}
	//Class move that stores the spefici move that was just made
	private class Move	
	{
		public char direction;	//Stores the direction in which the piece is going
		public int value;		// -1 for left/down, +1 for right/up
		public int blockID;		// Which block was moved

		public Move(char direction, int value, int ID)
		{
			this.direction = direction;
			this.value = value;
			this.blockID = ID;
		}
	}
	//HashKey Method hashes specific board to hashset
	public boolean HashKey(int key[][]){	//Hashes
		int k = 0;	// Initial key

		for (int i=0; i<rows; i++)
			for (int j = 0; j < columns; j++)
				if(key[i][j] != 0){
					k += (i*j);
				}

		// Returns false if the board is already in the hashset
		boolean ret = pConfigs.add(k);
		return ret;
	}
	//FoundSolution method determines if a solution has been found
	public boolean foundSolution(Snapshot temp){
		int [][] tempBoard = temp.boardSnap();
		
		for(int i=0;i<rows;i++){
				if(tempBoard[i][columns-1] == 8){	//Checks if the Z piece is on the last column
					return true;
				}
		}
		return false;
	}
	
	public void printHint(int numBlocks)		//Prints hint method allows the printing of a certain hint
	{
		if (solution != null) {
			int id = hint.blockID;

			if (id == 8)
				System.out.print("Hint: Move block Z one spot ");
			else
				// Numbers
				System.out.print("Hint: Move block " + id + " one spot ");

			if (hint.direction == 'u') {
				System.out.print("up\n");
			} 
			else if (hint.direction == 'd'){
				System.out.print("down\n");
			}
			else if (hint.direction == 'r') {
				System.out.print("right\n");
			}
			else if (hint.direction == 'l'){
				System.out.print("left\n");
			}
		}
		if (solution == null)
			System.out.println("Puzzle has no solution");
	}

	public void printSolution() {	//PrintSolution method prints out a solution to the current board
		if (solution != null) {
			for (int i = 0; i < solution.moves.size(); i++) {
				int id = solution.moves.get(i).blockID;

				if (id == 8)
					System.out.print("Move block Z one spot ");
				else
					// Numbers
					System.out.print("Move block " + id + " one spot ");

				if (hint.direction == 'u') {
					System.out.print("up\n");
				} else if (hint.direction == 'd') {
					System.out.print("down\n");
				} else if (hint.direction == 'r') {
					System.out.print("right\n");
				} else if (hint.direction == 'l') {
					System.out.print("left\n");
				}
			}
		}
		
		if (solution == null)
			System.out.println("Puzzle has no solution");
	}
}
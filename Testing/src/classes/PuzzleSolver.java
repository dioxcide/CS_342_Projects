package classes;

import java.util.*;

public class PuzzleSolver
{
	private Move hint;	
	private Snapshot solution;				
	private ArrayList<Snapshot> queue;		// Snapshots to process
	private HashSet<Integer> prevConfigs;	// Previous configurations of the board
	private int rows;
	private int columns;
	private ArrayList<Pieces> blocks;

	private class Snapshot
	{
		public int board[][];			// Current board
		public ArrayList<Move> moves;	// Moves it took to get this board
		public ArrayList<Pieces> snapshotPieces;

		public Snapshot(int b[][])
		{
			this.board = copyBoard(b);
			moves = new ArrayList<Move>();
			snapshotPieces = new ArrayList<Pieces>();

			// Copy list of Piecess
			for (int i=0; i<blocks.size(); i++)
			{
				Pieces temp = blocks.get(i);
				this.snapshotPieces.add(temp);
			}
			solution = null;
		}

		// Constructor for a snapshot that implements a move
		public Snapshot(Snapshot old, char dir, int value, int updateBlock)
		{
			this.board = copyBoard(old.board); 	// Board will be updated in appropriate move method

			// Copy list of moves
			this.moves = new ArrayList<Move>(old.moves.size());
			for (int i=0; i<old.moves.size(); i++)
			{
				Move temp = new Move(old.moves.get(i));
				this.moves.add(temp);
			}

			Move move = new Move(dir, value, updateBlock);
			this.moves.add(move);	// Add new move

			// Copy list of blocks
			snapshotPieces = new ArrayList<Pieces>();
			for (int i=0; i<old.snapshotPieces.size(); i++)
			{
				Pieces temp = blocks.get(i);
				this.snapshotPieces.add(temp);
			}

			// Update the block moved
			if (dir == 'v')
				this.snapshotPieces.get(updateBlock).x += value;
			if (dir == 'h')
				this.snapshotPieces.get(updateBlock).y += value;
		}
	}

	private class Move
	{
		public char direction;	
		public int value;		// -1 for left/down, +1 for right/up
		public int blockID;		// Which block was moved

		public Move(char direction, int value, int ID)
		{
			this.direction = direction;
			this.value = value;
			this.blockID = ID;
		}

		// Constructor that creates a copy of the move parameter
		public Move (Move m)
		{
			this(m.direction, m.value, m.blockID);
		}
	}

	/**------------------------------------------------------------------------
	 * Tries to move each block in every possible way to fund a solution
	 * ------------------------------------------------------------------------*/
	
	public PuzzleSolver(Pieces[] currentPieces){
		int [][] board = new int[6][6];
		ArrayList<Pieces> blocks = new ArrayList<Pieces>();
		for(int i =0;i<8;i++){
			blocks.add(currentPieces[i]);
		}
		board = createBoard(currentPieces);
		PuzzleSolver2(6,6, board, blocks);
	}
	
	public int[][] createBoard(Pieces[] currentPieces){
		int[][] temp = new int[6][6];

		for (int i = 0; i < 8; i++) {
			int ii, jj;
			int x = currentPieces[i].x;
			int y = currentPieces[i].y;
			int width = currentPieces[i].width;
			int height = currentPieces[i].height;
			int id = currentPieces[i].id;

			for (ii = 0; ii < height; ii++) {
				for (jj = 0; jj < width; jj++) {
					// System.out.println(x+","+y);
					temp[x + ii][y + jj] = id;
				}
			}
		}
		return temp;
	}
	
	public void PuzzleSolver2(int rows, int columns, int board[][], ArrayList<Pieces> blocks)
	{
		this.rows = rows;
		this.columns = columns;
		this.blocks = blocks;

		prevConfigs = new HashSet<Integer>();
		hint = null;
		queue = new ArrayList<Snapshot>();
		Snapshot snapshot = new Snapshot(board);
		queue.add(snapshot);
		addToPrevConfig(board);		// Add starting board to configurations list
		while (!queue.isEmpty())	// While the queue is not empty
		{
			//---------------------------------------------------------
			// Check to see if the goal piece is on the right end of
			// board - solution was found
			//---------------------------------------------------------
			snapshot = queue.get(0);	// Get snapshot on top of the queue
			queue.remove(0);

			for (int i=1; i<rows-1; i++)	// Scan rightmost column for goal piece
			{
				if (snapshot.board[i][columns-2] == 8)	// Solution found
				{
					solution = snapshot;
					hint = snapshot.moves.get(0);
					break;
				}
			}

			// If hint is not null then a solution was found - break out of search loop
			if (hint != null)
				break;

			//---------------------------------------------------------
			// No solution found yet so move each block in every possible 
			// way and add each new snapshot to the queue
			//---------------------------------------------------------
			for (int i=0; i<blocks.size(); i++)
			{
				char movement = blocks.get(i).mobility;

				// Move block based on movement specification
				switch (movement)
				{
					case 'h':
						horizontalMove(snapshot, i);
						break;

					case 'v':
						verticalMove(snapshot, i);
						break;

					case 'b':
						horizontalMove(snapshot, i);
						verticalMove(snapshot, i);
						break;

					default:	// No movement
						break;
				}
			}
		} // End while (!queue.isEmpty())

		// No solution was found
		if (solution == null)
			System.out.println("Puzzle has no solution");

	}

	/**------------------------------------------------------------------------
	 * Tries to move a block left or right on the board. If it is a valid move 
	 * then a new sanpshot is created and added to the end queue.
	 * 
	 * @param current		current snapshot whose block is being moved
	 * @param block			block to move
	 * ------------------------------------------------------------------------*/
	public void horizontalMove(Snapshot current, int block)
	{
		boolean canMoveRight = true;
		boolean canMoveLeft = true;

		// Don't need these but makes code more readable
		int ID = current.snapshotPieces.get(block).id;
		int startrow = current.snapshotPieces.get(block).x;
		int startcol = current.snapshotPieces.get(block).y;
		int height = current.snapshotPieces.get(block).height;		
		int width = current.snapshotPieces.get(block).width;
		Pieces temp = current.snapshotPieces.get(block);
		int [][] puzzle = current.board;
			
		canMoveRight = temp.moveRight(puzzle);
		canMoveLeft = temp.moveLeft(puzzle);

		if (canMoveRight) {
			Snapshot right = new Snapshot(current, 'h', 1, block);
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			System.out.println("Before");
			printBoard(right.board);
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<width; j++)
				{
					right.board[startrow+i][startcol+j+1] = ID;
					visited[startrow+i][startcol+j+1] = 'v';

					if (visited[startrow+i][startcol+j] == 'u'){
						right.board[startrow+i][startcol+j] = 0;
					}
				}
			}
			System.out.println("After");
			printBoard2(visited);
			printBoard(right.board);

			if (addToPrevConfig(right.board) == true){
				System.out.println("Added to Q");
				queue.add(right);
			}
		}

		if (canMoveLeft) {
			Snapshot left = new Snapshot(current, 'h', -1, block);
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			System.out.println("Before");
			printBoard(left.board);
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<width; j++)
				{
					left.board[startrow+i][startcol+j-1] = ID;
					visited[startrow+i][startcol+j-1] = 'v';

					if (visited[startrow+i][startcol+j] == 'u'){
						left.board[startrow+i][startcol+j] = 0;
					}
				}
			}
			System.out.println("After");
			printBoard2(visited);
			printBoard(left.board);
			// Make sure an identical board is not current on the queue - if not
			// add new snapshot
			if (addToPrevConfig(left.board) == true){
				System.out.println("Added to Q");
				queue.add(left);
			}
		}
	}

	/**------------------------------------------------------------------------
	 * Tries to move a block up or down on the board. If it is a valid move 
	 * then a new sanpshot is created and added to the end queue.
	 * 
	 * @param current		current snapshot whose block is being moved
	 * @param block			block to move
	 * ------------------------------------------------------------------------*/
	public void verticalMove(Snapshot current, int block)
	{
		boolean canMoveUp = true;
		boolean canMoveDown = true;

		// Don't need these but makes code more readable
		int ID = current.snapshotPieces.get(block).id;
		int startrow = current.snapshotPieces.get(block).x;
		int startcol = current.snapshotPieces.get(block).y;
		int width = current.snapshotPieces.get(block).width;		
		int height = current.snapshotPieces.get(block).height;
		Pieces temp = current.snapshotPieces.get(block);
		int [][] puzzle = current.board;
		
		canMoveUp = temp.moveUp(puzzle);
		canMoveDown = temp.moveDown(puzzle);

		if (canMoveUp)
		{
			Snapshot up = new Snapshot(current, 'v', -1, block);
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			System.out.println("Before");
			printBoard(up.board);
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					up.board[startrow+j-1][startcol+i] = ID;
					visited[startrow+j-1][startcol+i] = 'v';

					if (visited[startrow+j][startcol+i] == 'u'){
						up.board[startrow+j][startcol+i] = 0;
					}
				}
			}
			System.out.println("After");
			printBoard2(visited);
			printBoard(up.board);
			// Make sure an identical board is not current on the queue - if not add new snapshot
			if (addToPrevConfig(up.board) == true){
				System.out.println("Added to Q");
				queue.add(up);
			}
		}

		if (canMoveDown)
		{
			Snapshot down = new Snapshot(current, 'v', 1, block);
			// Update the board to reflect the move - move the block
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			System.out.println("Before");
			printBoard(down.board);
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					down.board[startrow+1+j][startcol+i] = ID;
					visited[startrow+1+j][startcol+i] = 'v';
					
					if (visited[startrow+j][startcol+i] == 'u'){
						down.board[startrow+j][startcol+i] = 0;
					}
				}
			}
			System.out.println("After");
			printBoard2(visited);
			printBoard(down.board);
			// Make sure an identical board is not current on the queue - if not add new snapshot
			if (addToPrevConfig(down.board) == true){
				System.out.println("Added to Q");
				queue.add(down);
			}
		}
	}

	public int[][] copyBoard(int original[][] )
	{
		int copy[][] = new int[rows][columns];

		for (int i = 0; i < copy.length; i++) 
			for (int j = 0; j < copy.length; j++) 
				copy[i][j] = original[i][j];

		return copy;
	}

	public boolean addToPrevConfig(int key[][])
	{	
		int s = 0;	// Convert board to a string

		for (int i=0; i<rows; i++)
			for (int j = 0; j < columns; j++)
				if(key[i][j] != 0){
					s += (i*j);
				}

		// Hashsets do not allow duplicate values - duplicate strings would not be added (false returned)
		boolean ret = prevConfigs.add(s);
		return ret;
	}
	
	public void printBoard(int board[][]){
		System.out.printf("\n");
		for (int i = 0; i <6; i++){ 
			for (int j = 0; j < 6; j++) {
				System.out.printf("%d \t", board[i][j]);
			}
			System.out.printf("\n");
		}
	}
	public void printBoard2(char board[][]){
		System.out.printf("\n");
		for (int i = 0; i <6; i++){ 
			for (int j = 0; j < 6; j++) {
				System.out.printf("%c \t", board[i][j]);
			}
			System.out.printf("\n");
		}
	}
}
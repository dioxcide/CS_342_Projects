
import java.util.Random;

//Board Class created in order to create the board
public class board {
	private int [][] board;	//Creates a board containing general board info
	private Random random = new Random();	//Random generator
	
	public board(){	//Board method initializes board
		board = new int [10][10];
		createBoard();
		setMines();
		completeBoard();
	}
	
	public void createBoard(){	//First sets the board to all zeros
		for(int i = 0; i<10;i++){
			for(int j = 0; i<10;i++){
				board[i][j] = 0;
			}
		}
	}
	
	public void setMines(){	//Sets randomly generated mines on the board
		boolean mines;
    	int Row, Column;
    	for(int i =0;i<10;i++)	//For loop to set 10 mines
    	{
    		do{						//Do while loop to set up a mine and make sure it doesnt a replace another mine
    			Row = random.nextInt(8) + 1;	//Randomly generates a number between 0-9
                Column = random.nextInt(8) + 1;
                
                if(board[Row][Column] == 99)
                	mines = true;
                else
                    mines = false;
    			
    		}while(mines);
    		board[Row][Column] = 99;	//Sets the board equal to 99 in order to represent a mine
    	}
	}
	
	public void completeBoard(){	//Sets the numbers representing the number of mines around the certain part of the grid
		int north, south, east, west, northeast, northwest, southeast, southwest, result = 0; //int declaration
    	for(int i=0;i<10;i++)
    	{
    		for(int j=0;j<10;j++)
    		{
    			result = 0;					//Result stores how many mines are around the certain point on the board
    			if(i>0){			//Each if statement increments result representing the amount of mines around 	
    				north = board[i-1][j];	//The certain position
    				if(north == 99){
    					result++;
    				}
    			}
    			if(i<9){
    				south = board[i+1][j]; 
    				if(south == 99){
    					result++;
    				}
    			}
    			if(j>0){
    				west = board[i][j-1];
    				if(west == 99){
    					result++;
    				}
    			}
    			if(j<9){
    				east = board[i][j+1];
    				if(east == 99){
    					result++;
    				}
    			}
    			if(i<9 && j <9){
    				northeast = board[i+1][j+1]; 
    				if(northeast == 99){
    					result++;
    				}
    			}
    			if(i<9 && j>0){
    				northwest = board[i+1][j-1];
    				if(northwest == 99){
    					result++;
    				}
    			}
    			if(i>0 && j<9){
    				southeast = board[i-1][j+1];
    				if(southeast == 99){
    					result++;
    				}
    			}
    			if(i>0 && j>0){
    				southwest = board[i-1][j-1];
    				if(southwest == 99){
    					result++;
    				}
    			}

    			if (board[i][j] != 99) {
					board[i][j] = result;	//Sets the certain position equal to result as long as it doesnt contain a mine
				}
	
    		}// End of inner loop
    	}// End of outer loop
	}
	 
	public int[][] getBoard(){ //Method returns board 
		return board;
	}
	
}

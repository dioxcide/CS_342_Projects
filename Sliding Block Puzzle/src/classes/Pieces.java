package classes;
//General class pieces hold piece information for each block
public class Pieces {
	public int x, y, width,height, id;
	public char mobility;
	//Pieces holds general information about each block piece
	public Pieces(int x, int y, int width, int height, int id, char mobility){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.mobility = mobility;
	}
	//General method to print piece values
	public void printValues(){
		System.out.println("PRINTING VALUES: X: "+this.x+" Y: "+this.y+" Width: "+this.width+" Height: "+this.height+" ID: "+this.id);
	}
	//Several booleans to detect if the piece can move up down left or right
	boolean moveLeft(int puzzle[][]){
		int finalWidth = this.y -1; 
		
		for(int i=0;i<height;i++){						//Goes through the whole piece determining if it can be moved left
			if(finalWidth <0){
				return false;
			}
			else if(finalWidth >= 0 && puzzle[this.x+i][finalWidth] != 0){
				return false;
			}
		}
		return true;
	}
	//Several booleans to detect if the piece can move up down left or right
	boolean moveRight(int puzzle[][]){
		int finalWidth = this.y + this.width;
		
		for (int i = 0; i < height; i++) {
			if(finalWidth >5){
				return false;
			}
			else if(finalWidth <= 5 && puzzle[this.x+i][finalWidth] != 0) {			//Goes through the whole piece determining if it can be moved right
				return false;
			}
		}
		return true;
	}
	
	//Several booleans to detect if the piece can move up down left or right
	boolean moveUp(int puzzle[][]){
		int finalHeight= this.x -1;
		
		for (int i = 0; i < width; i++) {
			if(finalHeight < 0){
				return false;
			}
			else if (finalHeight >= 0 && puzzle[finalHeight][this.y+i] != 0) {		//Goes through the whole piece determining if it can be moved up
				return false;
			}
		}
		return true;
	}
	//Several booleans to detect if the piece can move up down left or right
	boolean moveDown(int puzzle[][]){
		int finalHeight = this.x + this.height;
				
		for (int i = 0; i < width; i++) {
			if(finalHeight > 5){
				return false;
			}
			else if (finalHeight <= 5 && puzzle[finalHeight][this.y+i] != 0) {			//Goes through the whole piece determining if it can be moved down
				return false;
			}
		};
		return true;
	}
}

package classes;

public class Pieces {
	public int x, y, width,height, id;
	public char mobility;
	
	public Pieces(int x, int y, int width, int height, int id, char mobility){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.mobility = mobility;
	}
	
	public void printValues(){
		System.out.println("PRINTING VALUES: X: "+this.x+" Y: "+this.y+" Width: "+this.width+" Height: "+this.height+" ID: "+this.id);
	}
	
	boolean moveLeft(int puzzle[][]){
		int finalWidth = this.y -1; 
		
		for(int i=0;i<height;i++){
			if(finalWidth <0){
				return false;
			}
			else if(finalWidth >= 0 && puzzle[this.x+i][finalWidth] != 0){
				return false;
			}
		}
		System.out.println("Moving...Left");
		printValues();
		return true;
	}
	
	boolean moveRight(int puzzle[][]){
		int finalWidth = this.y + this.width;
		
		for (int i = 0; i < height; i++) {
			if(finalWidth >5){
				return false;
			}
			else if(finalWidth <= 5 && puzzle[this.x+i][finalWidth] != 0) {
				return false;
			}
		}
		System.out.println("Moving...Right");
		printValues();
		return true;
	}
	
	boolean moveUp(int puzzle[][]){
		int finalHeight= this.x -1;
		
		for (int i = 0; i < width; i++) {
			if(finalHeight < 0){
				return false;
			}
			else if (finalHeight >= 0 && puzzle[finalHeight][this.y+i] != 0) {
				return false;
			}
		}
		System.out.println("Moving...Up");
		printValues();
		return true;
	}
	
	boolean moveDown(int puzzle[][]){
		int finalHeight = this.x + this.height-1;
				
		for (int i = 0; i < width; i++) {
			if(finalHeight > 5){
				return false;
			}
			else if (finalHeight <= 5 && puzzle[finalHeight][this.y+i] != 0) {
				return false;
			}
		}
		System.out.println("Moving...Down");
		printValues();
		return true;
	}
}

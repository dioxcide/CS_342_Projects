package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.StringBuilder;

public class FileInput {
	private int rows, columns, start, end, width1, length1, lineRead = 0;
	private char mobility1;
	private int numPieces;
	private Node root;
	public StringBuilder puzzle = new StringBuilder("src/resources/puzzle0.txt"); //can change in future resets
																				  //also needs to be of type so can be changed
	
	public FileInput(int puzzleNumber){
		setPuzzle(puzzleNumber);
		String currentPuzzle = puzzle.toString();
		File file = new File(currentPuzzle);
        try {
             Scanner scanner = new Scanner(file); 
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                if(lineRead == 0){
	                rows = Integer.parseInt(tokens[0]);
	                columns = Integer.parseInt(tokens[1]);
                }
                else{
                	start =  Integer.parseInt(tokens[0]);
                	end = Integer.parseInt(tokens[1]);
                	width1 = Integer.parseInt(tokens[2]);
                	length1 = Integer.parseInt(tokens[3]);
                	String s = tokens[4];
                	mobility1 = s.charAt(0);
                	addNode(start, end, width1, length1, mobility1);
                	numPieces++;
                }
                lineRead++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        listTraversal(root, 10);
	}
	
	/*private String getResource(String string) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	public int getNumPieces(){
		return numPieces;
	}
	
	public void setPuzzle(int i) {
		//casts integer as char for puzzle file: src/resources/puzzle[].txt
		char insert = (char) (i+48);	//converts into corresponding ascci char
		if(i>9) { insert += 7; }		//if more than 9 puzzles, uses A-Z
		puzzle.setCharAt(20,insert);
		System.out.println(puzzle);
	}

	public void addNode(int startPos, int endPos, int width, int length, char mobility){//General method to add to List
		Node newNode = new Node(startPos, endPos, width, length, mobility);	//Declares a new Node
		newNode.next = root;
		root = newNode;
	}
	
	public Node listTraversal(Node root1, int spefVal){
		Node newNode = root1;
		int i = 0;
		while(spefVal != i && newNode.next != null){
			newNode = newNode.next;
			i++;
		}
		return newNode;
	}
	
	public int boardRow(){
		return rows;
	}
	public int boardCol(){
		return columns;
	}
	public int blockStart(int specificBlock){
		Node specificVal = listTraversal(root, specificBlock);
		int startVal = specificVal.startPos-1;
		return startVal;
	}
	public int blockEnd(int specificBlock){
		Node specificVal = listTraversal(root, specificBlock);
		int endVal = specificVal.endPos-1;
		return endVal;
	}
	public int blockLength(int specificBlock){
		Node specificVal = listTraversal(root, specificBlock);
		int lengthVal = specificVal.length;
		return lengthVal;
	}
	public int blockWidth(int specificBlock){
		Node specificVal = listTraversal(root, specificBlock);
		int widthVal = specificVal.width;
		return widthVal;
	}
	public char blockMobile(int specificBlock){
		Node specificVal = listTraversal(root, specificBlock);
		char mobileVal = specificVal.mobility;
		return mobileVal;
	}
	
}
class Node{ 
	int startPos;			
	int endPos;
	int width;
	int length;
	char mobility;
	Node next;
	
	Node(int startPos, int endPos, int width, int length, char mobility){
		this.startPos = startPos;
		this.endPos = endPos;
		this.width = width;
		this.length = length;
		this.mobility = mobility;
	}
	public String toString(){
		return "StartPos: " +startPos+ " endPos: " +endPos+" Width: " +width+" Length: " +length+" Mobility: "+mobility;
	}
}
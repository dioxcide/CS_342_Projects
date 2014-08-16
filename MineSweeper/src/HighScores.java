import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//High Scores class
public class HighScores{
	private ArrayList<String> names = new ArrayList<String>();	//Array lists implemented to store top ten scores
	private ArrayList<Integer> times = new ArrayList<Integer>();
	private int count = 0;
	private JFrame frame = new JFrame("Top Ten");	//New JFrame Created
	private File statText = new File("HighScores.txt");
 
	public void writing() {
	    try {
	//What ever the file path is.
	    	if (!statText.exists()) {
	    		statText.createNewFile();
			}
	    	FileWriter fw = new FileWriter(statText.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
	        for(int i = 0; i<10;i++){
	        	bw.write(names.get(i)+":"+times.get(i));
	        	bw.newLine();
	        }
	        bw.close();
	    } catch (IOException e) {
	        System.err.println("Problem writing to the file statsTest.txt");
	    }
	}
    /** Creates the reusable dialog. */
	public void displayData(Node root){ //Method Adds top ten scores from the BST to the array lists
		Traversal(root, count);		//Populates array lists
		initArrayList(count);		//Initializes blank parts of the arrayList
		
		String[] columnNames = { "Name", "Time" };	//String for JTable
		Object[][] data = { { names.get(0), times.get(0) },	//Object contain top 10 scores
				{ names.get(1), times.get(1) }, 
				{ names.get(2), times.get(2) },
				{ names.get(3), times.get(3) }, 
				{ names.get(4), times.get(4) },
				{ names.get(5), times.get(5) }, 
				{ names.get(6), times.get(6) },
				{ names.get(7), times.get(7) }, 
				{ names.get(8), times.get(8) },
				{ names.get(9), times.get(9) } };
		
		writing();
		
		JPanel panel = new JPanel();		//New JPanel to store JTable
		panel.setLayout(new BorderLayout());	//Sets general borderLayout for the JPanel
		JTable table = new JTable(data, columnNames);	//Creates a new JTable for the top ten scores 
		panel.add(table, BorderLayout.CENTER);			//Adds the table to the panel
		
		frame.getContentPane().add(panel);				//Adds the Panel to the JFrame
		frame.pack();
		frame.setSize(250,225);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();	//gets default location
		int x = (int) ((dimension.getWidth() - 250) / 2);
		int y = (int) ((dimension.getHeight() - 225) / 2);
		frame.setLocation(x,y);
		frame.setVisible(false);
	}
	
	
	public void displayTopTen(){
		frame.setVisible(true);		//Displays top ten scores to the user
	}
	
	public void initArrayList(int i){	//Method to initialize the rest of the arrayList if they weren't filled
		while(i!=10){
			names.add("_");	//Adds and _ and 0 to blank ararylist 
			times.add(999);
			i++;
		}
	}
	
	public void Traversal(Node tempNode, int count){	//Method adds top 10 scores from BST to arraylist
		if(tempNode!=null &&count != 10){		//Traverses the list in order to add to the arrayLists
			Traversal(tempNode.left, count);
			names.add(tempNode.name);
			times.add(tempNode.time);
			count++;
			Traversal(tempNode.right, count);
		}
	}
}

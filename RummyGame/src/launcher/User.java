package launcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1846855511759579806L;
	/**
	 * 
	 */
	private ArrayList<Card> hand;
	private String user;
	private Hashtable<String,ArrayList<Card>> Melds;
	private int gameScore = 0;
	
	//Main constructor used to store hashtable array list and username
	public User(Hashtable<String,ArrayList<Card>> Melds, ArrayList<Card> hand, String user){
		this.hand = hand;
		this.user = user;
		this.setMelds(Melds);
	}
	//Getters and setters
	
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	public String getUser(){
		return this.user;
	}

	public Hashtable<String,ArrayList<Card>> getMelds() {
		return Melds;
	}

	public void setMelds(Hashtable<String,ArrayList<Card>> melds) {
		Melds = melds;
	}
	
	public int getScore() {
		return gameScore;
	}
	
	public void updateScore(int points) {
		gameScore += points;
	}
}

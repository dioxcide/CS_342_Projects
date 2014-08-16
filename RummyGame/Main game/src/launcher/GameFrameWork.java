package launcher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

//Class that was meant to handle general card moves
public class GameFrameWork {
	private User Hand;			//Local variables to be used later to access several different things
	private ArrayList<Card> DiscardPile;
	private Deck currDeck;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int[] ranks = new int[14];
	private int[] suits = new int[4];
	
	//Main constructor assigns local class vairables
	public GameFrameWork(ArrayList<Card> DiscardPile, Deck currDeck, User Hand, ObjectOutputStream out, ObjectInputStream in){
		this.setDiscardPile(DiscardPile);
		this.setDeck(currDeck);
		this.setHand(Hand);
		this.setOut(out);
		this.setIn(in);
	}
	
	//getter
	public Card topDiscard(){
		return DiscardPile.get(0);
	}
	//Method that draws from the deck from the server
	public User DrawFromDeck(){
		
		if (currDeck.getNumCards() > 0) {	//If the deck still has cards it draws from the deck
			String tempName = Hand.getUser();
			ArrayList<Card> tempHand = Hand.getHand();
			Hashtable<String, ArrayList<Card>> Melds = Hand.getMelds();	//Stores general info from user

			Card addedCard = currDeck.drawFromDeck();
			tempHand.add(addedCard);						//Adds new card to users hand
			User t = new User(Melds, tempHand, tempName);

			try {
				out.writeObject(currDeck);				//Updates the deck on the server
			} catch (IOException e1) {
				System.out.println("Error1: " + e1);
			}
			return t;	//Returns t
		}
		else{
			return DrawFromDiscard();//Else if deck is empty it draws from discard pile
		}
	}
		//Method that draws a card from the discard pile
	public User DrawFromDiscard(){
		
		String tempName = Hand.getUser();							//Stores general info
		ArrayList<Card> tempHand = Hand.getHand();
		Hashtable<String, ArrayList<Card>> Melds = Hand.getMelds();
		
		if (DiscardPile.size() > 0) {						//If there are cards on the discard pile it will draw from it
			Card addedCard = DiscardPile.get(0);
			tempHand.add(addedCard);
			DiscardPile.remove(0);							//Adds card to hand and removes it from pile

			User t = new User(Melds, tempHand, tempName);

			try {
				out.writeObject(currDeck);				//Updates discard pile on server
			} catch (IOException e1) {
				System.out.println("Error1: " + e1);
			}

			return t;
		}
		else{													//If discard pile is empty nothing happens
			User t = new User(Melds, tempHand, tempName);
			return t;
		}
	}
	
	//Method that enumerates the total number of cards per rank
	public int[] getTotalRanks(ArrayList<Card> MeldedHand){
		int[] tempRanks = new int[14];
		
		for(int i  = 0;i<14;i++){
			for(int j = 0;j<MeldedHand.size();j++){
				Card temp = MeldedHand.get(j);
				if(temp.getRank() == i){
					tempRanks[i]++;		//Increments if the rank is equal to that certain part of the array
				}
			}
		}
		return tempRanks;
	}
	//Method that enumerates the total number of cards per suit
	public int[] getTotalSuits(ArrayList<Card> MeldedHand){
		int[] tempSuits = new int[4];
		
		for(int i  = 0;i<4;i++){
			for(int j = 0;j<MeldedHand.size();j++){
				Card temp = MeldedHand.get(j);
				if(temp.getSuit() == i){
					tempSuits[i]++;		//Increments tempSuit if the suit matches the outer for loop
				}
			}
		}
		
		return tempSuits;
	} 
	//Method detects if the melded hand is a run
	public boolean isRun(ArrayList<Card> MeldedHand){
		int[]tempRank = getRanks();
		int[] tempSuit = getSuits();
		int cardsInSeq = 0;
		boolean run = false;
		
		for (int k = 0; k < 13; k++) { 
			if (tempRank[k] ==  1 && tempRank[k + 1] == 1)
				cardsInSeq++;						//Determines the number of cards in sequence
		}
		cardsInSeq++;
		if (cardsInSeq == MeldedHand.size()) {
			for(int i = 0;i<tempSuit.length;i++){
				if(tempSuit[i] == MeldedHand.size()){
					run = true;								//If the number of sequenced cards matches the meldedhand size and so do the suits then it returns true
				}
			}
		}

		return run;
	}
	//Method detects if the melded hand is a set
	public boolean isSet(ArrayList<Card> MeldedHand){
		int[]tempRank = getRanks();
		int[] tempSuit = getSuits();
		boolean set = false;
		
		for(int i = 0;i<tempSuit.length;i++){
			if(tempSuit[i] != MeldedHand.size()){
				for(int j = 0;j < tempRank.length;j++){
					if(tempRank[j] == 3 || tempRank[j] == 4){
						set = true;			//If all the suits do not match and there are matching ranks then it return true
					}
				}
			}
		}

		return set;
	}
	//Method essentially removes the users melded hand from there hand and places it into a hashtable
	public User Melding(ArrayList<Card> MeldedHand){
		String User = Hand.getUser();
		ArrayList<Card>origHand = Hand.getHand();
		Hashtable <String,ArrayList<Card>> Melds = Hand.getMelds();
		
		setRanks(getTotalRanks(MeldedHand));
		setSuits(getTotalSuits(MeldedHand));
		
		if(isRun(MeldedHand)){
			System.out.println("Hand is run meldable");
			for(int i = 0; i<origHand.size();i++){
				for(int j = 0;j<MeldedHand.size();j++){
					if(origHand.get(i).equals(MeldedHand.get(j))){
						origHand.remove(i);							//Removes from orig hand
					}
				}
			}
			Melds.put(MeldedHand.get(0).suitString(), MeldedHand);//Adds meldedhand to hashtable
		}
		else if(isSet(MeldedHand)){
			System.out.println("Hand is set meldable");
			for(int i = 0; i<origHand.size();i++){
				for(int j = 0;j<MeldedHand.size();j++){
					if(origHand.get(i).equals(MeldedHand.get(j))){
						origHand.remove(i);								//Removes from original hand
					}
				}
			}
			Melds.put(MeldedHand.get(0).rankString(), MeldedHand);		//Adds to hashtable
		}
		else{
			System.out.println("Hand not meldable");
		}
		
		User t = new User(Melds, origHand, User);
		
		return t;
	}
	//Method essentially processes layoffs with melded hands
	public User LayingOff(ArrayList<Card> MeldedHand){
		String User = Hand.getUser();
		ArrayList<Card>origHand = Hand.getHand();
		Hashtable <String,ArrayList<Card>> Melds = Hand.getMelds();
		
		setRanks(getTotalRanks(MeldedHand));
		setSuits(getTotalSuits(MeldedHand));
		
		if(isRun(MeldedHand)){											//Detects if hand is a run hand
			System.out.println("Hand is run meldable");
			if (Melds.containsKey(MeldedHand.get(0).suitString())) {
				
				ArrayList<Card>temp = Melds.get(MeldedHand.get(0).suitString());
				
				for (int i = 0; i < origHand.size(); i++) {
					for (int j = 0; j < MeldedHand.size(); j++) {
						if (origHand.get(i).equals(MeldedHand.get(j))) {
							temp.add(MeldedHand.get(j));
							origHand.remove(i);									//Removes from orig hand
						}
					}
				}
				
				Melds.put(MeldedHand.get(0).suitString(), temp);			//adds new melded hand to hashtable
			}
			
			else{
				System.out.println("Hand Can't Be Laid Off it needs to be melded!");
			}
		}
		
		else if(isSet(MeldedHand)){													//Detects if hand is a set hand
			if (Melds.containsKey(MeldedHand.get(0).rankString())) {
				
				ArrayList<Card>temp = Melds.get(MeldedHand.get(0).rankString());
				
				for (int i = 0; i < origHand.size(); i++) {
					for (int j = 0; j < MeldedHand.size(); j++) {
						if (origHand.get(i).equals(MeldedHand.get(j))) {
							temp.add(MeldedHand.get(j));
							origHand.remove(i);
						}
					}
				}
				Melds.put(MeldedHand.get(0).rankString(), temp);

			}
			
			else{
				System.out.println("Hand Can't Be Laid Off it needs to be melded!");
			}
			
		}
		else{
			System.out.println("Hand not meldable");
		}
		
		User t = new User(Melds, origHand, User);
		
		return t;
	}
	//Method allows uses to discard one card from there hand
	public User DiscardCard(Card card){
		String tempName = Hand.getUser();
		ArrayList<Card> tempHand = Hand.getHand();
		Hashtable<String,ArrayList<Card>> Melds = Hand.getMelds();
		for(int i  = 0;i<tempHand.size();i++){
			
			Card t = tempHand.get(i);
			
			if(t.equals(card)){
				DiscardPile.add(t);	//Adds to the discard pile
				tempHand.remove(i);	//Removes it from the hand
			}
		}

		try {
			out.writeObject(DiscardPile);		///Updates server discard pile
		} catch (IOException e1) {
			System.out.println("Error2: "+e1);
		}
		
		User t = new User(Melds, tempHand, tempName);
		
		return t;
	}

	//All the methods below are getters and setters
	public User getHand() {
		return Hand;
	}
	public void setHand(User hand) {
		Hand = hand;
	}

	public void setDiscardPile(ArrayList<Card> pile) {
		this.DiscardPile = pile;
	}
	
	public void setDeck(Deck deck){
		this.currDeck = deck;
	}
	
	public int[] getRanks() {
		return ranks;
	}

	public void setRanks(int[] ranks) {
		this.ranks = ranks;
	}

	public int[] getSuits() {
		return suits;
	}

	public void setSuits(int[] suits) {
		this.suits = suits;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	
}

package launcher;

import java.io.Serializable;

public class Card implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 589164258272619285L;
	private int rank, suit;//General arrays to store suits and ranks
    private static String[] suits = { "hearts", "spades", "diamonds", "clubs" };
    private static String[] ranks  = {"Ace" ,"2", "3", "4", "5", "6", "7", "8", "9","10", "Jack", "Queen", "King"};

    //This refers to a certain suit and rank
    Card(int suit, int rank)
    {
        this.rank=rank;
        this.suit=suit;
    }
    //Helps represent suit and rank of a card as a string
    public @Override String toString()
    {
          return ranks[rank] + " of " + suits[suit];
    }
    //Get functions in order to access the specific rank and suit from 
    //the suits and ranks arrays

    public String rankString(){
    	return ranks[rank];
    }
    
    public String suitString(){
    	return suits[suit];
    }
    
    public int getRank() {
         return rank;
    }

    public int getSuit() {
        return suit;
    }

    // Returns the rank as String, for easier use with JButton constructor (String,icon)
    public String getRankString() {
        if(rank == 10)      { return "J"; }
    	else if(rank == 11) { return "Q"; }
    	else if(rank == 12) { return "K"; }
    	else if(rank == 0)  { return "A"; }
    	else                { return ranks[rank]; }
    }
    
    public int getRankValue() {
    	if(rank > 8) return 10; //10,J,Q,K all have a value of 10
    	else return rank+1;		//A,2,3,4,5,6,7,8,9 all have face value
    }

    // Returns String as is, so can easily access resource files of the same name
    public String getSuitString() {
        return suits[suit];
    }
}

package launcher;
import java.io.Serializable;
import java.util.Collections;
import java.util.ArrayList;

public class Deck implements Serializable{
        /**
	 * 
	 */
	private static final long serialVersionUID = -651322167619335212L;
	private ArrayList<Card> cards;// Arraylist of the type card to contain the
									// deck

	// Method to return a newly shuffled deck
	public Deck() {
		cards = new ArrayList<Card>(); // Allocates memory for the deck
		for (int a = 0; a < 4; a++) {
			for (int b = 0; b < 13; b++) {
				cards.add(new Card(a, b)); // Adds a new card of a specific suit
											// and rank to the deck
			}
		}

		Collections.shuffle(cards); // Shuffles the deck
	}

	public Card topCard() {
		return cards.get(0);
	}

	// This method draws from the deck and removes the card from the deck at the
	// same time
	public Card drawFromDeck() {
		return cards.remove(0);
	}

	// This method returns the total number of cards left in the deck.
	public int getNumCards() {
		return cards.size();
	}

}

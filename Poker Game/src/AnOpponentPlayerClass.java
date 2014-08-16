import java.util.Arrays;
import java.util.Scanner;

public class AnOpponentPlayerClass {

	private Card[][] computer_hand;
	private int Ace_index = 0;
	private int index_to_discard, i, j;
	@SuppressWarnings("unused")
	private int counter = 0, numUsers = 0, num_discard = 0;
	private int inner_loop_counter = 0;
	private int[] cards_to_discard = new int[5];
	@SuppressWarnings("unused")
	private int [] index_cards_to_discard = new int[5];
	Scanner reader = new Scanner(System.in);

	public AnOpponentPlayerClass(CardPile MyDeck) {
		System.out.println("Enter the number of players that you wish to play against: ");
		//get user input for numUsers
		numUsers=reader.nextInt();
		while(numUsers <= 0 || numUsers > 3)
		{
			System.out.println("Invalid number of players! Please enter a number between 1 and 3\n");
			numUsers=reader.nextInt();
		}
		computer_hand = new Card[numUsers][5];
		for(int j = 0;j<numUsers;j++)
		{
			for (i = 0; i < 5; i++) {
				computer_hand[j][i] = MyDeck.drawFromDeck();
			}// The computer has a hand with 5 cards.
		}
}

	public void displayHand(int numPlayers) {               
		Card C;
		int l = 2;
		for (int i = 0; i < numPlayers; i++) {
			System.out.printf("\nAIHand[%d]: ", l++);
			for (int j = 0; j < 5; j++) {
				C = computer_hand[i][j];
				System.out.printf("%s, ", C.toString());
			}
			System.out.printf("\n");
		}
		System.out.printf("\n");
	}

	public Card[][] getAiHand() { 
		return computer_hand;
	}
	
	public int getPlayers(){
		return numUsers;
	}

	public void Discard_computers_hand(CardPile MyDeck) {
		for (int a = 0; a < numUsers; a++) {
			for (i = 0; i < 5; i++) {// looping through the computer's hand comparing each card to all the other cards in the hand.
				inner_loop_counter = 0; // reinitializing it to check the next
										// iteration
				for (j = 0; j < i; j++) {
					if (computer_hand[a][i].getRank() == computer_hand[a][j].getRank()) {
						counter++;
						inner_loop_counter++;
					}
				}
				for (j = i + 1; j < 5; j++) {
					if (computer_hand[a][i].getRank() == computer_hand[a][j].getRank()) {
						counter++;
						inner_loop_counter++;
					}
				}
				if (inner_loop_counter == 0) { // This card has no pair/s and
												// goes
												// on the discard list
					cards_to_discard[i] = i;
				} 
				else {
					cards_to_discard[i] = 100;
				}
			}// We are checking if the computer has a pair or better 

			if (counter == 2) { // pair
				// num_discard=3;
				for (int k = cards_to_discard.length - 1; k >= 0; k--) {
					int num_discard = cards_to_discard[k];

					if (num_discard < 100) {
						computer_hand[a][num_discard] = MyDeck.drawFromDeck();
					}
				}
			}

			else if (counter == 4) { // Two pairs
				// num_discard=1;
				for (int k = cards_to_discard.length - 1; k >= 0; k--) {
					int num_discard = cards_to_discard[k];
					if (num_discard < 100) {
						computer_hand[a][num_discard] = MyDeck.drawFromDeck();
					}
				}
			}

			else if (counter == 6) { // 3 of a kind
				for (int k = cards_to_discard.length - 1; k >= 0; k--) {
					int num_discard = cards_to_discard[k];
					if (num_discard < 100) {
						computer_hand[a][num_discard] = MyDeck.drawFromDeck();
					}
				}
			}

			else if (counter == 8) {
				System.out.println("The computer has a full house"); // full
																		// house
			}

			else if (counter == 12) { // 4 of a kind

				for (int k = cards_to_discard.length - 1; k >= 0; k--) {
					int num_discard = cards_to_discard[k];
					if (num_discard < 100) {
						computer_hand[a][num_discard] = MyDeck.drawFromDeck();
					}
				}
			}

			else { // The computer has no pair ,two pair, 3 of a kind or 4 of a kind.
				int num_same_suit = 0;
				int cards_inseq = 0;
				int suit_inner_loop;

				for (i = 0; i < 5; i++) { // We are going through all the cards,hoping to find 4 of the same suit or 5 of the same suit.
					suit_inner_loop = 0;
					for (j = 0; j < i; j++) {
						if (computer_hand[a][i].getSuit() == computer_hand[a][j].getSuit()) {
							num_same_suit++;
							suit_inner_loop++;
						}
						// same_suit.add(i);
					}
					for (j = i + 1; j < 5; j++) {
						if (computer_hand[a][i].getSuit() == computer_hand[a][j].getSuit()) {
							num_same_suit++;
							suit_inner_loop++;
						}
						// same_suit.add(i);
					}
					if (suit_inner_loop == 0) {
						index_to_discard = i;
					}

				}// An array with all the same suits is constructed

				if (num_same_suit == 12) {// We have 4 of the same suit.We want to discard the card from the other suit. 
					computer_hand[a][index_to_discard] = MyDeck.drawFromDeck();
				} 
				else if (num_same_suit == 20) {// All cards are from the same suit. We are not discarding anything.
					System.out
							.println("All of the computer's cards are from the same suit");
				} 
				else {
					// checking if the user has 4 cards or 5 cards in a sequence.
					int k;
					for (k = 0; k < 4; k++) { //We are going through The array and checking how many cards are in a sequence.
						if (computer_hand[a][k].getRank() + 1 == computer_hand[a][k + 1].getRank())
							cards_inseq++;
					}
					if (cards_inseq == 4) // We have a straight!
						System.out
								.println("The computer has 5 cards in sequence");
					else if (cards_inseq == 3) {// Only one card is out of sync, We are discarding it. 
						if (computer_hand[a][0].getRank() + 1 != computer_hand[a][1].getRank()) {
							System.out.println("The first card is out of sequence. Being discarded....\n");
							computer_hand[a][0] = MyDeck.drawFromDeck();
						}

						else {
							computer_hand[a][4] = MyDeck.drawFromDeck();
						}
					}

					else {
						// No sequence is found. we are checking if the hand
						// contains an Ace.
						for (k = 1; k < 6; k++) {
							if (computer_hand[a][k - 1].getRank() + 1 == 1) // We have the Ace at Index 0 not 13!
								Ace_index = k; // Ace_index is initiallized in
												// the
												// top to 0, so if Ace_index has
												// a
												// diferent value,
												// we know that there is an Ace
												// in
												// the hand,
						}
						if (Ace_index != 0) // find 4 cards to discard
							for (k = 0; k < 5; k++) {
								if (k != Ace_index - 1)
									computer_hand[a][k] = MyDeck.drawFromDeck();
							}
						else { // We are keeping the highest 2card and dicarding the remaining 3.
							int[] numValues = new int[5];
							for (int i = 0; i < 5; i++) {
								numValues[i] = computer_hand[a][i].getRank();
							}
							Arrays.sort(numValues);   // We Sort the hand from low to high
							int[] discValues = new int[3];
							for (int i = 0; i < 3; i++) {
								discValues[i] = numValues[i];
							}

							for (int i = 0; i < 3; i++) {  // We discard the and replace the first 3 indicies.
								for (int j = 0; j < 5; j++) {
									if (computer_hand[a][j].getRank() == discValues[i]) {
										computer_hand[a][j] = MyDeck.drawFromDeck();
									}
								}
							}

						}
					}
				}
				// No sequence is found. we are checking if the hand contains an
				// Ace

			}
		}
	}
}
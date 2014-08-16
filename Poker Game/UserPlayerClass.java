import java.util.Scanner;

public class UserPlayerClass{
	private Card[] hands;					//Private card 2d array to represent users hands
	Scanner reader = new Scanner(System.in);
	
	//Populates players hands and takes CardPile d as the deck to draw from.
	public UserPlayerClass(CardPile d) {
		// Allocates memory for the total # of hands
		hands = new Card[5];

		// Populates each players hands from drawing the deck
		for (int j = 0; j < 5; j++) {
			hands[j] = d.drawFromDeck();
		}
	}
	
	//General method to print out all users hands
	public void displayHand() {
		Card C; // Variable to represent a single card
		System.out.printf("\nDealing hands please wait.....\n");
		System.out.printf("\nPlayers hand: ");
		for (int j = 0; j < 1; j++) {
			C = hands[j]; // Sets C to represent a single card
			System.out.printf("%s, ", C.toString()); // Prints out the cards
														// from the hand
		}
		System.out.printf("\nDealing hands please wait.....\n");
		System.out.printf("\nPlayers hand: ");
		for (int j = 0; j < 2; j++) {
			C = hands[j]; // Sets C to represent a single card
			System.out.printf("%s, ", C.toString()); // Prints out the cards
														// from the hand
		}
		System.out.printf("\nDealing hands please wait.....\n");
		System.out.printf("\nPlayers hand: ");
		for (int j = 0; j < 3; j++) {
			C = hands[j]; // Sets C to represent a single card
			System.out.printf("%s, ", C.toString()); // Prints out the cards
														// from the hand
		}
		System.out.printf("\nDealing hands please wait.....\n");
		System.out.printf("\nPlayers hand: ");
		for (int j = 0; j < 4; j++) {
			C = hands[j]; // Sets C to represent a single card
			System.out.printf("%s, ", C.toString()); // Prints out the cards
														// from the hand
		}
		System.out.printf("\nDealing hands please wait.....\n");
		System.out.printf("\nPlayers hand: ");
		for (int j = 0; j < 5; j++) {
			C = hands[j]; // Sets C to represent a single card
			System.out.printf("%s, ", C.toString()); // Prints out the cards
														// from the hand
		}
		System.out.printf("\n");
	}
	
	public int [] handPairs()
	{
		int[] rankPairs = new int[13];
		// Essentially gets the ranks of each card in the hand of each player

		for (int j = 0; j < 13; j++) {
			rankPairs[j] = 0;
		}

		for (int j = 0; j < 5; j++) {
			int a = hands[j].getRank();
			rankPairs[a]++;
		}

		return rankPairs;
	}
	
	//Method that goes through each player and asks if they want to discard any cards
	public void discardHand(CardPile d)
	{	//Loop that goes through every player and asks if they want to discard cards
		System.out.printf("How many cards would you like to remove?");
		int numDiscards = reader.nextInt(); // Reads how many cards they want to
											// discard
		
		int[] handPair = handPairs();
		if (numDiscards > 0 && numDiscards < 4) // If they choose greater than 0
												// then it prompts the user for
												// which cards they want to
												// remove
		{
			System.out.printf("\nSpecify the card number that you want to discard (Each card in your hand represents 1-5 respectively): ");
			int cardToDiscard;
			for (int a = 0; a < numDiscards; a++) {
				cardToDiscard = reader.nextInt(); // Discards the card that is
													// specified the user and
													// draws another one from
													// the deck
				while (cardToDiscard < 0 || cardToDiscard > 5) {
					System.out
							.printf("\nInvalid card to discard please try again!\n");
					cardToDiscard = reader.nextInt();
				}
				cardToDiscard = cardToDiscard - 1;
				hands[cardToDiscard] = d.drawFromDeck();
			}
		} 
		else if (numDiscards > 3) {
			if(handPair[0] != 1)
			{
				while (numDiscards > 3 || numDiscards < 0) {
					System.out
							.printf("\nInvalid number of cards to discard please try again!\n");
					System.out
							.printf("How many cards would you like to remove?");
					numDiscards = reader.nextInt(); // Reads how many cards they
													// want to discard
				}
				if (numDiscards > 0 && numDiscards < 6) // If they choose
					// greater
					// than 0 then it
					// prompts
					// the user for which
					// cards
					// they want to remove
				{
					System.out.printf("\nSpecify the card number that you want to discard (Each card in your hand represents 1-5 respectively): ");
					int cardToDiscard;
					for (int a = 0; a < numDiscards; a++) {
						cardToDiscard = reader.nextInt(); // Discards the card
						// that
						// is specified the
						// user
						// and draws another
						// one
						// from the deck
						while (cardToDiscard < 0 || cardToDiscard > 5) {
							System.out
									.printf("\nInvalid card to discard please try again!\n");
							cardToDiscard = reader.nextInt();
						}
						cardToDiscard = cardToDiscard - 1;
						hands[cardToDiscard] = d.drawFromDeck();
					}
				} 
				else // If user chooses anything else it prints the following
				// message
				{
					System.out.printf("\nPlayer is discarding 0 cards\n");
				}
			} 
			else if(handPair[0] >= 1) {
				if (numDiscards > 0 && numDiscards < 6) // If they choose
														// greater
														// than 0 then it
														// prompts
														// the user for which
														// cards
														// they want to remove
				{
					System.out
							.printf("\nSpecify the card number that you want to discard (Each card in your hand represents 1-5 respectively): ");
					int cardToDiscard;
					for (int a = 0; a < numDiscards; a++) {
						cardToDiscard = reader.nextInt(); // Discards the card
															// that
															// is specified the
															// user
															// and draws another
															// one
															// from the deck
						while (cardToDiscard < 0 || cardToDiscard > 5) {
							System.out
									.printf("\nInvalid card to discard please try again!\n");
							cardToDiscard = reader.nextInt();
						}
						cardToDiscard = cardToDiscard - 1;
						hands[cardToDiscard] = d.drawFromDeck();
					}
				} 
				else // If user chooses anything else it prints the following
						// message
				{
				System.out.printf("\nPlayer is discarding 0 cards\n");
				}
			}
		} 
		else // If user chooses anything else it prints the following message
		{
			System.out.printf("\nPlayer is discarding 0 cards\n");
		}
		reader.close();
	}
	
	//Method returns the new 2d array containing each players hand
	public Card[] getHands()
	{
		return hands;
	}
}
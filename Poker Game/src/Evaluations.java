import java.util.Arrays;
import java.lang.Math;

public class Evaluations {
	public int[] Straight(Card[][] total_hands, int numPlayers) {
		int[] cards_inseq = new int[numPlayers];
		int[] cardsWStraight = new int[numPlayers];

		for (int j = 0; j < numPlayers; j++) {
			cards_inseq[j] = 0;
			cardsWStraight[j] = 0;
		}

		for (int i = 0; i < numPlayers; i++) {
			for (int k = 0; k < 4; k++) {
				if (total_hands[i][k].getRank() + 1 == total_hands[i][k + 1].getRank())
					cards_inseq[i]++;
			}
			if (cards_inseq[i] == 4) {
				cardsWStraight[i]++;
			}
			if(total_hands[i][0].getRank()==0&&total_hands[i][1].getRank()==1&&total_hands[i][2].getRank()==2&&total_hands[i][3].getRank()==3&&total_hands[i][4].getRank()==12){
				cardsWStraight[i]++;
			}
		}

		return cardsWStraight;
	}
    // Check if any of the players have a straight. An array with 1's and 0's is returned,
	// 1 means that the player has a straight 0 means the player doesn't have a flush. 
	public int[] Flush(Card[][] total_hands, int numPlayers) {
		int[][] rankPairs = new int[numPlayers][4];
		int[] playersWFlush = new int[numPlayers];
		// Essentially gets the ranks of each card in the hand of each player
		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 5; j++) {
				int a = total_hands[i][j].getSuit();
				rankPairs[i][a]++;
			}
		}
		
		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 4; j++) {
				if(rankPairs[i][j] == 5)
				{
					playersWFlush[i] = 1;
				}
			}
		}
		return playersWFlush;

	}
    //Check if any of the players have a flush if the do a value of 1 is being stored in the array that is returned. 
	public int[] HighestCard(Card[][] total_hands, int numPlayers) {
		// Finds the highest card in a hand. The function returns an array with the highest card of each player.
		int[] highestCard = new int[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			highestCard[i] = total_hands[i][0].getRank();
		}
		for (int i = 0; i < numPlayers; i++) {
			for (int j = 1; j < 5; j++) {
				if (total_hands[i][j].getRank() > highestCard[i]) {
					highestCard[i] = total_hands[i][j].getRank();
				}
			}
		}
		return highestCard;
	}

	public int[] StraightFlush(Card[][] total_hands, int numPlayers) {
		//Checks if any of the players have a straightflush based on the 2 arryas that the functions straight and flush returned. 
		int[] flush = Flush(total_hands, numPlayers);
		int[] straight = Straight(total_hands, numPlayers);
		int[] StraightFlush = new int[numPlayers];

		for (int i = 0; i < numPlayers; i++) {
			StraightFlush[i] = 0;
		}
		for (int i = 0; i < numPlayers; i++) {
			if (flush[i] == 1 && straight[i] == 1) {
				StraightFlush[i]++;
			}
		}

		return StraightFlush;
	}

	public int[] FindPairs(Card[][] total_hands, int numPlayers) {
		//This function returns an array with the numerical values 0,2,8,3,5,4 representing 
		// HighCard Hand, a pair,a two pair,3 of a kind, fullhouse,4 of a kind respectively.
		int[][] rankPairs = new int[numPlayers][13];
		int[] playersWPairs = new int[numPlayers];
		// Essentially gets the ranks of each card in the hand of each player
		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 13; j++) {
				rankPairs[i][j] = 0;
			}
		}

		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 5; j++) {
				int a = total_hands[i][j].getRank();
				rankPairs[i][a]++;
			}
		}

		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 13; j++) {
				if (rankPairs[i][j] == 4) {
					playersWPairs[i] = 4;
				} else if (rankPairs[i][j] == 3) {
					for (int k = 0; k < 13; k++) {
						if (rankPairs[i][k] == 2) {
							playersWPairs[i] = 5;
						} else if (playersWPairs[i] != 5
								&& playersWPairs[i] != 8) {
							playersWPairs[i] = 3;
						}
					}
				} else if (rankPairs[i][j] == 2) {
					int a = j + 1;
					for (int k = a; k < 13; k++) {
						if (rankPairs[i][k] == 2) {
							playersWPairs[i] = 8;
						} else if (playersWPairs[i] != 8
								&& playersWPairs[i] != 5) {
							playersWPairs[i] = 2;
						}
					}
				}

			}

		}
		return playersWPairs;
	}

	public Card[][] sortCards(Card[][] total_hands, int numPlayers) {
		// We sort the hand by rank. form low to high rank
		Card[][] sorted_hands = total_hands;
		boolean flag = true;
		while (flag) {
			flag = false;
			for (int i = 0; i < numPlayers; i++) {
				Card C;
				for (int j = 0; j < 4; j++) {
					if (sorted_hands[i][j].getRank() > sorted_hands[i][j + 1]
							.getRank()) {
						C = sorted_hands[i][j];
						sorted_hands[i][j] = sorted_hands[i][j + 1];
						sorted_hands[i][j + 1] = C;
						flag = true;
					}
				}
			}
		}

		return sorted_hands;
	}

	public void FHTieBreaker(Card[][] total_hands, int numPlayers, int[] playerPairs, int playersWFH)
	{// We break a tie between two or more hands that have a full house. We break the tie based on 
		// on the rank of the 3 pair.
		int[] handsWFH = new int[playersWFH];
		int sepIterator = 0;

		for (int i = 0; i < numPlayers; i++) {
			if (playerPairs[i] == 5) {
				handsWFH[sepIterator] = i;
				sepIterator++;
			}
		}
		if (playersWFH == 2) {
			int hand1 = handsWFH[0];
			int hand2 = handsWFH[1];

			if (total_hands[hand1][2].getRank() > total_hands[hand2][2]
					.getRank()) {
				System.out.printf("Player %d has won!\n", hand1 + 1);
			} 
			else if(total_hands[hand1][2].getRank() == total_hands[hand2][2].getRank())
			{
				int i =0;
				while(total_hands[hand1][i].getRank() == total_hands[hand2][i].getRank())
				{
					i++;
				}
				if (total_hands[hand1][i].getRank() > total_hands[hand2][i].getRank()) {
					System.out.printf("Player %d has won!\n", hand1 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n", hand2 + 1);
				}
			}
			else {
				System.out.printf("Player %d has won!\n", hand2 + 1);
			}
		} 
		else if (playersWFH == 3) {
			int hand1 = handsWFH[0];
			int hand2 = handsWFH[1];
			int hand3 = handsWFH[2];

			if (total_hands[hand1][2].getRank() > total_hands[hand2][2].getRank()) {
				if (total_hands[hand1][2].getRank() > total_hands[hand3][2].getRank()) {
					System.out.printf("Player %d has won!\n",hand1 + 1);
				} else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			}
			else if(total_hands[hand1][2].getRank() == total_hands[hand2][2].getRank() && total_hands[hand1][2].getRank() == total_hands[hand3][2].getRank() &&total_hands[hand2][2].getRank() == total_hands[hand3][2].getRank())
			{
				int i =0;
				while(total_hands[hand1][i].getRank() == total_hands[hand2][i].getRank() && total_hands[hand1][i].getRank() == total_hands[hand3][i].getRank() &&total_hands[hand2][i].getRank() == total_hands[hand3][i].getRank())
				{
					i++;
				}
				if (total_hands[hand1][i].getRank() > total_hands[hand2][i].getRank()) {
					if (total_hands[hand1][i].getRank() > total_hands[hand3][i].getRank()) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} else {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					}
				}
				else {
					if (total_hands[hand2][i].getRank() > total_hands[hand3][i].getRank()) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					}
				}
			}
			else {
				if (total_hands[hand2][2].getRank() > total_hands[hand3][2].getRank()) {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			}
		} 
		else if (playersWFH == 4) {
			int hand1 = handsWFH[0];
			int hand2 = handsWFH[1];
			int hand3 = handsWFH[2];
			int hand4 = handsWFH[3];

			if (total_hands[hand1][2].getRank() > total_hands[hand2][2].getRank()) {
				if (total_hands[hand1][2].getRank() > total_hands[hand3][2].getRank()) {
					if (total_hands[hand1][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else if(total_hands[hand1][2].getRank() < total_hands[hand3][2].getRank()){
					if (total_hands[hand3][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("LPPlayer %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			} 
			else if(total_hands[hand2][2].getRank() > total_hands[hand1][2].getRank()){
				if (total_hands[hand2][2].getRank() > total_hands[hand3][2].getRank()) {
					if (total_hands[hand2][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else if(total_hands[hand2][2].getRank() < total_hands[hand4][2].getRank()){
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else if(total_hands[hand2][2].getRank() < total_hands[hand3][2].getRank()){
					if (total_hands[hand3][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else if(total_hands[hand3][2].getRank() < total_hands[hand4][2].getRank()){
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
			else if(total_hands[hand1][2].getRank() == total_hands[hand2][2].getRank() || total_hands[hand1][2].getRank() == total_hands[hand3][2].getRank() ||total_hands[hand2][2].getRank() == total_hands[hand3][2].getRank() ||total_hands[hand2][2].getRank() == total_hands[hand4][2].getRank() ||total_hands[hand1][2].getRank() == total_hands[hand4][2].getRank() ||total_hands[hand3][2].getRank() == total_hands[hand4][2].getRank())
			{
				int i =0;
				while(total_hands[hand1][i].getRank() == total_hands[hand2][i].getRank() && total_hands[hand1][i].getRank() == total_hands[hand3][i].getRank() && total_hands[hand2][i].getRank() == total_hands[hand3][i].getRank() && total_hands[hand2][i].getRank() == total_hands[hand4][i].getRank() && total_hands[hand1][i].getRank() == total_hands[hand4][i].getRank() && total_hands[hand3][i].getRank() == total_hands[hand4][i].getRank())
				{
					i++;
				}
				if (total_hands[hand1][i].getRank() > total_hands[hand2][i].getRank()) {
					if (total_hands[hand1][i].getRank() > total_hands[hand3][i].getRank()) {
						if (total_hands[hand1][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand1 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					} 
					else {
						if (total_hands[hand3][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand3 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					}
				} 
				else {
					if (total_hands[hand2][i].getRank() > total_hands[hand3][i].getRank()) {
						if (total_hands[hand2][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand2 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					} 
					else {
						if (total_hands[hand3][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand3 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					}
				}
				
			}
		}
	}
	
	public void FlushTieBreaker(Card[][] total_hands, int numPlayers, int[] playersFlush, int playersWF)
	{// We break a the tie by checking the highest card between the hands that are in a tie. If both have the same
		// high card we go on to check the next high card until we can break the tie or we finish going through the hand.
		int[] handsWF = new int[playersWF];
		int sepIterator = 0;

		for (int i = 0; i < numPlayers; i++) {
			if (playersFlush[i] == 1) {
				handsWF[sepIterator] = i;
				sepIterator++;
			}
		}

		if (playersWF == 2) {
			int hand1 = handsWF[0];
			int hand2 = handsWF[1];

			if (total_hands[hand1][4].getRank() > total_hands[hand2][4].getRank()) {
				System.out.printf("Player %d has won!\n",hand1 + 1);
			}
			else if(total_hands[hand1][4].getRank() == total_hands[hand2][4].getRank())
			{
				int i = 3;
				while(total_hands[hand1][i].getRank() == total_hands[hand2][i].getRank() && i >=0)
				{
					i--;
				}
				if (total_hands[hand1][i].getRank() > total_hands[hand2][i].getRank()) {
					System.out.printf("Player %d has won!\n",hand1 + 1);
				}
				else {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				}
				
			}
			else {
				System.out.printf("Player %d has won!\n",hand2 + 1);
			}
		} 
		else if (playersWF == 3) {
			int hand1 = handsWF[0];
			int hand2 = handsWF[1];
			int hand3 = handsWF[2];

			if (total_hands[hand1][4].getRank() > total_hands[hand2][4].getRank()) {
				if (total_hands[hand1][4].getRank() > total_hands[hand3][4].getRank()) {
					System.out.printf("Player %d has won!\n",hand1 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			} 
			else if(total_hands[hand1][4].getRank() == total_hands[hand2][4].getRank() || total_hands[hand1][4].getRank() == total_hands[hand3][4].getRank() ||total_hands[hand2][4].getRank() == total_hands[hand3][4].getRank())
			{
				int i = 3;
				while(total_hands[hand1][i].getRank() == total_hands[hand2][i].getRank() && total_hands[hand1][i].getRank() == total_hands[hand3][i].getRank() && total_hands[hand2][i].getRank() == total_hands[hand3][i].getRank() && i >=0)
				{
					i--;
				}
				if (total_hands[hand1][i].getRank() > total_hands[hand2][i].getRank()) {
					if (total_hands[hand1][i].getRank() > total_hands[hand3][i].getRank()) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					}
				} 
				else {
					if (total_hands[hand2][i].getRank() > total_hands[hand3][i].getRank()) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					}
				}
			}
			else {
				if (total_hands[hand2][4].getRank() > total_hands[hand3][4].getRank()) {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			}
		} 
		else if (playersWF == 4) {
			int hand1 = handsWF[0];
			int hand2 = handsWF[1];
			int hand3 = handsWF[2];
			int hand4 = handsWF[3];

			if (total_hands[hand1][4].getRank() > total_hands[hand2][4].getRank()) {
				if (total_hands[hand1][4].getRank() > total_hands[hand3][4].getRank()) {
					if (total_hands[hand1][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else {
					if (total_hands[hand3][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
			else if(total_hands[hand1][4].getRank() == total_hands[hand2][4].getRank() || total_hands[hand1][4].getRank() == total_hands[hand3][4].getRank() ||total_hands[hand2][4].getRank() == total_hands[hand3][4].getRank() ||total_hands[hand2][4].getRank() == total_hands[hand4][4].getRank() ||total_hands[hand1][4].getRank() == total_hands[hand4][4].getRank() ||total_hands[hand3][4].getRank() == total_hands[hand4][4].getRank())
			{
				int i = 3;
				while(total_hands[hand1][i].getRank() == total_hands[hand2][i].getRank() && total_hands[hand1][i].getRank() == total_hands[hand3][i].getRank() &&total_hands[hand2][i].getRank() == total_hands[hand3][i].getRank() && total_hands[hand2][i].getRank() == total_hands[hand4][i].getRank() && total_hands[hand1][i].getRank() == total_hands[hand4][i].getRank() &&total_hands[hand3][i].getRank() == total_hands[hand4][i].getRank() && i >=0)
				{
					i--;
				}
				if (total_hands[hand1][i].getRank() > total_hands[hand2][i].getRank()) {
					if (total_hands[hand1][i].getRank() > total_hands[hand3][i].getRank()) {
						if (total_hands[hand1][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand1 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					} 
					else {
						if (total_hands[hand3][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand3 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					}
				}
				else {
					if (total_hands[hand2][i].getRank() > total_hands[hand3][i].getRank()) {
						if (total_hands[hand2][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand2 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					} 
					else {
						if (total_hands[hand3][i].getRank() > total_hands[hand4][i].getRank()) {
							System.out.printf("Player %d has won!\n",hand3 + 1);
						} 
						else {
							System.out.printf("Player %d has won!\n",hand4 + 1);
						}
					}
				}
			}
			else {
				if (total_hands[hand2][4].getRank() > total_hands[hand3][4].getRank()) {
					if (total_hands[hand2][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else {
					if (total_hands[hand3][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
		}
	}
	
	public void StraightTieBreaker(Card[][] total_hands, int numPlayers, int[] playersStraight, int playersWS)
	{// We break the tie by checking the highest card of the hands that are in a tie.
		int[] handsWS = new int[playersWS];
		int sepIterator = 0;

		for (int i = 0; i < numPlayers; i++) {
			if (playersStraight[i] == 1) {
				handsWS[sepIterator] = i;
				sepIterator++;
			}
		}

		if (playersWS == 2) {
			int hand1 = handsWS[0];
			int hand2 = handsWS[1];

			if (total_hands[hand1][4].getRank() > total_hands[hand2][4].getRank()) {
				System.out.printf("Player %d has won!\n",hand1 + 1);
			} 
			else if(total_hands[hand1][4].getRank() == total_hands[hand2][4].getRank())
			{
				System.out.printf("There is a draw!\n");
			}
			else {
				System.out.printf("Player %d has won!\n",hand2 + 1);
			}
		} 
		else if (playersWS == 3) {
			int hand1 = handsWS[0];
			int hand2 = handsWS[1];
			int hand3 = handsWS[2];

			if (total_hands[hand1][4].getRank() > total_hands[hand2][4].getRank()) {
				if (total_hands[hand1][4].getRank() > total_hands[hand3][4].getRank()) {
					System.out.printf("Player %d has won!\n",hand1 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			} 
			else if(total_hands[hand1][4].getRank() == total_hands[hand2][4].getRank() || total_hands[hand1][4].getRank() == total_hands[hand3][4].getRank() ||total_hands[hand2][4].getRank() == total_hands[hand3][4].getRank())
			{
				System.out.printf("There is a draw!\n");
			}
			else {
				if (total_hands[hand2][4].getRank() > total_hands[hand3][4].getRank()) {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			}
		} 
		else if (playersWS == 4) {
			int hand1 = handsWS[0];
			int hand2 = handsWS[1];
			int hand3 = handsWS[2];
			int hand4 = handsWS[3];

			if (total_hands[hand1][4].getRank() > total_hands[hand2][4].getRank()) {
				if (total_hands[hand1][4].getRank() > total_hands[hand3][4].getRank()) {
					if (total_hands[hand1][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else {
					if (total_hands[hand3][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			} 
			else if(total_hands[hand1][4].getRank() == total_hands[hand2][4].getRank() || total_hands[hand1][4].getRank() == total_hands[hand3][4].getRank() ||total_hands[hand2][4].getRank() == total_hands[hand3][4].getRank() ||total_hands[hand2][4].getRank() == total_hands[hand4][4].getRank() ||total_hands[hand1][4].getRank() == total_hands[hand4][4].getRank() ||total_hands[hand3][4].getRank() == total_hands[hand4][4].getRank())
			{
				System.out.printf("There is a draw!\n");
			}
			else {
				if (total_hands[hand2][4].getRank() > total_hands[hand3][4].getRank()) {
					if (total_hands[hand2][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else {
					if (total_hands[hand3][4].getRank() > total_hands[hand4][4].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
		} 
	}
	
	public void ThreeTieBreaker(Card[][] total_hands, int numPlayers, int[] playerPairs, int playersW3)
	{// We check the rank of the 3 of a kind. whichever hand has the higher 3 of a hand rank is declared as the winner.
		int[] handsW3 = new int[playersW3];
		int sepIterator = 0;

		for (int i = 0; i < numPlayers; i++) {
			if (playerPairs[i] == 3) {
				handsW3[sepIterator] = i;
				sepIterator++;
			}
		}

		if (playersW3 == 2) {
			int hand1 = handsW3[0];
			int hand2 = handsW3[1];

			if (total_hands[hand1][2].getRank() > total_hands[hand2][2].getRank()) {
				System.out.printf("Player %d has won!\n",hand1 + 1);
			} 
			else {
				System.out.printf("Player %d has won!\n",hand2 + 1);
			}
		} 
		else if (playersW3 == 3) {
			int hand1 = handsW3[0];
			int hand2 = handsW3[1];
			int hand3 = handsW3[2];

			if (total_hands[hand1][2].getRank() > total_hands[hand2][2].getRank()) {
				if (total_hands[hand1][2].getRank() > total_hands[hand3][2].getRank()) {
					System.out.printf("Player %d has won!\n",hand1 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			} 
			else {
				if (total_hands[hand2][2].getRank() > total_hands[hand3][2].getRank()) {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			}
		} 
		else if (playersW3 == 4) {
			int hand1 = handsW3[0];
			int hand2 = handsW3[1];
			int hand3 = handsW3[2];
			int hand4 = handsW3[3];

			if (total_hands[hand1][2].getRank() > total_hands[hand2][2].getRank()) {
				if (total_hands[hand1][2].getRank() > total_hands[hand3][2].getRank()) {
					if (total_hands[hand1][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else {
					if (total_hands[hand3][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			} 
			else {
				if (total_hands[hand2][2].getRank() > total_hands[hand3][2].getRank()) {
					if (total_hands[hand2][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else {
					if (total_hands[hand3][2].getRank() > total_hands[hand4][2].getRank()) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
		}
	}
	
	public void HighRankTieBreaker(Card[][] total_hands, int [] playerPairs, int numPlayers, int[] highestCard)
	{ // We break the tie by going through the array from high to low until we can reach a tie breaker.
		int playersW0 = 0;

		for (int i = 0; i < numPlayers; i++) {
			if (playerPairs[i] == 0) {
				playersW0++;
			}
		}

		if (playersW0 == 2) {
			if (highestCard[0] > highestCard[1]) {
				System.out.printf("Player 1 is the winner!\n");
			} 
			else if(total_hands[0][4].getRank() == total_hands[1][4].getRank())
			{
				int i = 3;
				while(total_hands[0][i].getRank() == total_hands[1][i].getRank() && i>=0)
				{
					i--;
				}
				if (total_hands[0][i].getRank() > total_hands[1][i].getRank()) {
					System.out.printf("Player 1 is the winner!\n");
				} 
				else {
					System.out.printf("Player 2 is the winner!");
				}
			}
			else {
				System.out.printf("Player 2 is the winner!");
			}
		} 
		else if (playersW0 == 3) {
			if (highestCard[0] > highestCard[1]) {
				if (highestCard[0] > highestCard[2]) {
					System.out.printf("Player 1 is the winner!\n");
				} 
				else {
					System.out.printf("Player 3 is the winner!");
				}
			} 
			else if(total_hands[0][4].getRank() == total_hands[1][4].getRank() || total_hands[0][4].getRank() == total_hands[2][4].getRank() || total_hands[2][4].getRank() == total_hands[1][4].getRank())
			{System.out.printf("IN FINAL EVAL!\n");
				int i = 3;
				while(total_hands[0][i].getRank() == total_hands[1][i].getRank() && total_hands[0][i].getRank() == total_hands[2][i].getRank() && total_hands[2][i].getRank() == total_hands[1][i].getRank() && i>=0)
				{
					i--;
				}
				if (total_hands[0][i].getRank() > total_hands[1][i].getRank()) {
					if (total_hands[0][i].getRank() > total_hands[2][i].getRank()) {
						System.out.printf("Player 1 is the winner!\n");
					} 
					else {
						System.out.printf("Player 3 is the winner!");
					}
				}
				else if(total_hands[0][i].getRank() < total_hands[1][i].getRank()){
					if (total_hands[1][i].getRank() > total_hands[2][i].getRank()) {
						System.out.printf("Player 2 is the winner!\n");
					} 
					else {
						System.out.printf("Player 3 is the winner!");
					}
				}
			}
			else {
				if (highestCard[1] > highestCard[2]) {
					System.out.printf("Player 2 is the winner!\n");
				} 
				else {
					System.out.printf("Player 3 is the winner!");
				}
			}
		}
		else if (playersW0 == 4) {
			if (highestCard[0] > highestCard[1]) {
				if (highestCard[0] > highestCard[2]) {
					if (highestCard[0] > highestCard[3]) {
						System.out.printf("Player 1 is the winner!\n");
					} 
					else {
						System.out.printf("Player 4 is the winner!\n");
					}
				} 
				else {
					if (highestCard[2] > highestCard[3]) {
						System.out.printf("Player 3 is the winner!\n");
					} 
					else {
						System.out.printf("Player 4 is the winner!\n");
					}
				}
			} 
			else if(total_hands[0][4].getRank() == total_hands[1][4].getRank() || total_hands[0][4].getRank() == total_hands[2][4].getRank() || total_hands[2][4].getRank() == total_hands[1][4].getRank())
			{
				int i = 3;
				while(total_hands[0][i].getRank() == total_hands[1][i].getRank() && total_hands[0][i].getRank() == total_hands[2][i].getRank() && total_hands[2][i].getRank() == total_hands[1][i].getRank() && i>=0)
				{
					i--;
				}
				if (total_hands[0][i].getRank() > total_hands[1][i].getRank()) {
					if (total_hands[0][i].getRank() > total_hands[2][i].getRank()) {
						if (total_hands[0][i].getRank() > total_hands[3][i].getRank()) {
							System.out.printf("Player 1 is the winner!\n");
						} 
						else {
							System.out.printf("Player 4 is the winner!\n");
						}
					} 
					else {
						if (total_hands[2][i].getRank() > total_hands[4][i].getRank()) {
							System.out.printf("Player 3 is the winner!\n");
						} 
						else {
							System.out.printf("Player 4 is the winner!\n");
						}
					}
				} 

				else {
					if (total_hands[1][i].getRank() > total_hands[2][i].getRank()) {
						if (total_hands[1][i].getRank() > total_hands[3][i].getRank()) {
							System.out.printf("Player 2 is the winner!\n");
						} 
						else {
							System.out.printf("Player 4 is the winner!\n");
						}
					} 
					else {
						if (total_hands[2][i].getRank() > total_hands[3][i].getRank()) {
							System.out.printf("Player 3 is the winner!\n");
						} 
						else {
							System.out.printf("Player 4 is the winner!\n");
						}
					}
				}
			}
			else {
				if (highestCard[1] > highestCard[2]) {
					if (highestCard[1] > highestCard[3]) {
						System.out.printf("Player 2 is the winner!\n");
					} 
					else {
						System.out.printf("Player 4 is the winner!\n");
					}
				} 
				else {
					if (highestCard[2] > highestCard[3]) {
						System.out.printf("Player 3 is the winner!\n");
					} 
					else {
						System.out.printf("Player 4 is the winner!\n");
					}
				}
			}
		}
	}
	
	public void TwoPairTieBreaker(Card[][] total_hands, int numPlayers, int[] playerPairs, int playersW2)
	{ // First we compare the rank of the higher ranked pair. if the higher ranked pairs are equal, we check the lower ranked pairs if they are equal as well 
		// we check the single card and see if we can break the tie.
		int[][] rankPairs = new int[numPlayers][13];
		int[] handsW2 = new int[playersW2];
		int [] equalPairs = new int[3];
		int [] equalPairs2 = new int[3];
		int sepIterator = 0;

		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 5; j++) {
				int a = total_hands[i][j].getRank();
				rankPairs[i][a]++;
			}
		}

		for (int i = 0; i < numPlayers; i++) {
			if (playerPairs[i] == 8) {
				handsW2[sepIterator] = i;
				sepIterator++;
			}
		}

		if (playersW2 == 2) {
			int hand1 = handsW2[0];
			int hand2 = handsW2[1];
			int rank1 = total_hands[hand1][3].getRank();
			int rank2 = total_hands[hand2][3].getRank();
			int j = 0, k =0;
			int maxRank = Math.max(rank1, rank2);
			 
			if(rank1 == rank2 && rank1 == maxRank)
			{
				for(int i =12;i>=0;i--)
				{
					if(rankPairs[hand1][i] == 2 || rankPairs[hand1][i] == 1)
					{
						equalPairs[j] = i;
						j++;
					}
					if(rankPairs[hand2][i] == 1 || rankPairs[hand2][i] == 2)
					{
						equalPairs2[k] = i;
						k++;
					}
				}
				int i = 0;
				while(equalPairs[i] == equalPairs2[i] && i<3)
				{
					i++;
				}
				if(equalPairs[i] > equalPairs2[i]){
						System.out.println("Player 1 is the winner");
				}
				else if(equalPairs[i] < equalPairs2[i]) {
					System.out.println("Player 2 is the winner");	
				}
				else{
					System.out.println("The game ended in a tie");
				}
			}
		}
		else if (playersW2 == 3) {
			int hand1 = handsW2[0];
			int hand2 = handsW2[1];
			int hand3 = handsW2[2];
			int rank1 = total_hands[hand1][3].getRank();
			int rank2 = total_hands[hand2][3].getRank();
			int rank3 = total_hands[hand3][3].getRank();
			int j = 0, k =0;
			int maxRank = Math.max(Math.max(rank1, rank2), rank3);

			if (rank1 > rank2) {
				if (rank1 > rank3) {
					System.out.printf("Player %d has won!\n",hand1 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			} 
			else if(rank1 < rank2) {
				if (rank2 > rank3) {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				}
			}
			else if(rank3 > rank1)
			{
				if (rank3 > rank2) {
					System.out.printf("Player %d has won!\n",hand3 + 1);
				} 
				else {
					System.out.printf("Player %d has won!\n",hand2 + 1);
				}
			}
			else if(rank1 == rank2 || rank1 == rank3||rank2 == rank3)
			{
				if(rank1 == rank2 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 2 || rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand2][i] == 1 || rankPairs[hand2][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}

				else if(rank1 == rank3 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 2 || rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand3][i] == 1 || rankPairs[hand2][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 3 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank3 == rank2 && rank2 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand3][i] == 2 || rankPairs[hand3][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand2][i] == 1 || rankPairs[hand2][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 3 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
			}

		} 
		else if (playersW2 == 4) {
			int hand1 = handsW2[0];
			int hand2 = handsW2[1];
			int hand3 = handsW2[2];
			int hand4 = handsW2[3];
			int rank1 = total_hands[hand1][3].getRank(), rank2 = total_hands[hand2][3].getRank(), rank3 = total_hands[hand3][3].getRank(), rank4 = total_hands[hand4][3].getRank();
			int maxRank = Math.max(Math.max(rank1, rank2), Math.max(rank3, rank4));
			int j = 0, k =0;
			
			if (rank1 > rank2) {
				System.out.printf("Swag0\n");
				if (rank1 > rank3) {
					if (rank1 > rank4) {
						System.out.printf("Player %d has won!\n",hand1 + 1);
					} 
					else if(rank1 < rank4) {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else if(rank1<rank3) {
					if (rank3 > rank4) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else if(rank3 < rank4) {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			} 
			else if(rank2 > rank1){
				System.out.printf("Swag1\n");
				if (rank2> rank3) {
					if (rank2> rank4) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else if(rank2<rank4){
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else if(rank2<rank3){
					if (rank3 > rank4) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else if(rank3<rank4){
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
			else if(rank3 > rank1){
				System.out.printf("Swag2\n");
				if (rank3> rank2) {
					if (rank3> rank4) {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					} 
					else if(rank3<rank4){
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				} 
				else if(rank3<rank2){
					if (rank2 > rank4) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else if(rank2<rank4){
						System.out.printf("Player %d has won!\n",hand4 + 1);
					}
				}
			}
			else if(rank4 > rank1)
			{
				System.out.printf("Swag3\n");
				if (rank4> rank2) {
					if (rank4> rank3) {
						System.out.printf("Player %d has won!\n",hand4 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					}
				} 
				else {
					if (rank2 > rank3) {
						System.out.printf("Player %d has won!\n",hand2 + 1);
					} 
					else {
						System.out.printf("Player %d has won!\n",hand3 + 1);
					}
				}
			}
			else if(rank1 == rank2 || rank1 == rank3 ||rank2 == rank3 ||rank2 == rank4||rank1 == rank4 ||rank3 == rank4)
			{
				if(rank1 == rank2&& rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 2 || rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand2][i] == 1 || rankPairs[hand2][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank1 == rank3&& rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 2 || rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand3][i] == 1 || rankPairs[hand3][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 3 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank2 == rank3&& rank2 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand2][i] == 2 || rankPairs[hand2][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand3][i] == 1 || rankPairs[hand3][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 2 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 3 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank2 == rank4&& rank2 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand2][i] == 2 || rankPairs[hand2][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand4][i] == 1 || rankPairs[hand4][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 2 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 4 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank1 == rank4&& rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 2 || rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand4][i] == 1 || rankPairs[hand4][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 4 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank3 == rank4&& rank3 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand3][i] == 2 || rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand4][i] == 1 || rankPairs[hand4][i] == 2)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 3 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 4 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
			}
		}
	}
	
	public void OnePairTieBreaker(Card[][] total_hands, int numPlayers, int[] playerPairs, int playersW1)
	{ //We break the tie by comparing the rank of the pair if both pairs have the same rank we are looking to break the tie by finding the highest card.
		int[][] rankPairs = new int[numPlayers][13];
		// Essentially gets the ranks of each card in the hand of each player

		for (int i = 0; i < numPlayers; i++) {
			for (int j = 0; j < 5; j++) {
				int a = total_hands[i][j].getRank();
				rankPairs[i][a]++;
			}
		}
		
		int[] handsW1 = new int[playersW1];
		int sepIterator = 0;

		for (int i = 0; i < numPlayers; i++) {
			if (playerPairs[i] == 2) {
				handsW1[sepIterator] = i;
				sepIterator++;
			}
		}

		if (playersW1 == 2) {
			int hand1 = handsW1[0];
			int hand2 = handsW1[1];
			int rank1=0, rank2=0;
			
			for(int i =0;i<13;i++)
			{
				if(rankPairs[0][i] == 2)
				{
					rank1 = i;
				}
				if(rankPairs[1][i] == 2)
				{
					rank2 = i;
				}
			}
			if (rank1 > rank2) {
				System.out.printf("Player 1 has won!\n");
			} 
			else if(rank1 == rank2)
			{
				int [] equalPairs = new int[3];
				int [] equalPairs2 = new int[3];
				int k = 0, j=0;
				for(int i =12;i>=0;i--)
				{
					if(rankPairs[hand1][i] == 1)
					{
						equalPairs[j] = i;
						j++;
					}
					if(rankPairs[hand2][i] == 1)
					{
						equalPairs2[k] = i;
						k++;
					}
				}
				int i = 0;
				while(equalPairs[i] == equalPairs2[i]&& i<3)
				{
					i++;
				}
				if(equalPairs[i] > equalPairs2[i]){
					System.out.println("Player 1 is the winner\n");
				}
				else if(equalPairs[i] < equalPairs2[i]){
					System.out.println("Player 2 is the winner\n");	
				}
				else
				{
					System.out.printf("There is a draw!\n");
				}
				
			}
			else {
				System.out.printf("Player 2 has won!\n");
			}
		} 
		else if (playersW1 == 3) {
			int hand1 = handsW1[0];
			int hand2 = handsW1[1];
			int hand3 = handsW1[2];
			int rank1=0, rank2=0, rank3=0;
			for(int i =0;i<13;i++)
			{
				if(rankPairs[hand1][i] == 2)
				{
					rank1 = i;
				}
				if(rankPairs[hand2][i] == 2)
				{
					rank2 = i;
				}
				if(rankPairs[hand3][i] == 2)
				{
					rank3 = i;
				}
			}

			if (rank1 >rank2) {
				if (rank1 > rank3) {
					System.out.printf("Player 1 has won!\n");
				} 
				else {
					System.out.printf("Player 3 has won!\n");
				}
			}
			else if(rank2>rank1){
				if (rank2 > rank3) {
					System.out.printf("Player 2 has won!\n");
				} 
				else if(rank3>rank2){
					System.out.printf("Player 3 has won!\n");
				}
			}
			else if(rank3>rank1){
				if (rank3 > rank2) {
					System.out.printf("Player 3 has won!\n");
				} 
				else if(rank2>rank3){
					System.out.printf("Player 2 has won!\n");
				}
			}
		
			else if(rank1 == rank2 || rank1 == rank3|| rank2 == rank3)
			{
				int [] equalPairs = new int[3];
				int [] equalPairs2 = new int[3];
				int j = 0, k = 0;
				int maxRank = Math.max(Math.max(rank1, rank2), rank3);
				if(rank1 == rank2 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand2][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
					
				}
				
				else if(rank1 == rank3 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						else if(rankPairs[hand3][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 3 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
					
				}
				
				else if(rank2 == rank3 && rank2 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand2][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						else if(rankPairs[hand3][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 2 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 3 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
			}
		} 
		else if (playersW1 == 4) {
			int hand1 = handsW1[0];
			int hand2 = handsW1[1];
			int hand3 = handsW1[2];
			int hand4 = handsW1[3];
			int rank1=0, rank2=0, rank3=0, rank4=0;
			for(int i =0;i<13;i++)
			{
				if(rankPairs[hand1][i] == 2)
				{
					rank1 = i;
				}
				else if(rankPairs[hand2][i] == 2)
				{
					rank2 = i;
				}
				else if(rankPairs[hand3][i] == 2)
				{
					rank3 = i;
				}
				else if(rankPairs[hand4][i] == 2)
				{
					rank4 = i;
				}
			}

			if (rank1 > rank2) {
				if (rank1> rank3) {
					if (rank1 > rank4) {
						System.out.printf("Player 1 has won!\n");
					} 
					else {
						System.out.printf("Player 4 has won!\n");
					}
				} 
				else {
					if (rank3 > rank4) {
						System.out.printf("Player 3 has won!\n");
					} 
					else {
						System.out.printf("Player 4 has won!\n");
					}
				}
			} 
			else if(rank1<rank2){
				if (rank2 > rank3) {
					if (rank2 > rank4) {
						System.out.printf("Player 2 has won!\n");
					} 
					else if(rank2<rank4){
						System.out.printf("Player 4 has won!\n");
					}
				} 
				else if(rank2<rank3){
					if (rank3 > rank4) {
						System.out.printf("Player 3 has won!\n");
					} 
					else if(rank3<rank4){
						System.out.printf("Player 4 has won!\n");
					}
				}
			}
			else if(rank4>rank1){
				if (rank4 > rank2) {
					if (rank4 > rank3) {
						System.out.printf("Player 4 has won!\n");
					} 
					else if(rank4<rank3){
						System.out.printf("Player 3 has won!\n");
					}
				} 
				else if(rank4<rank2){
					if (rank2 > rank3) {
						System.out.printf("Player 2 has won!\n");
					} 
					else if(rank2<rank3){
						System.out.printf("Player 3 has won!\n");
					}
				}
			}
			else if(rank3>rank1){
				if (rank3 > rank2) {
					if (rank3 > rank4) {
						System.out.printf("Player 3 has won!\n");
					} 
					else if(rank3<rank4){
						System.out.printf("Player 4 has won!\n");
					}
				} 
				else if(rank3<rank2){
					if (rank2 > rank4) {
						System.out.printf("Player 2 has won!\n");
					} 
					else if(rank2<rank4){
						System.out.printf("Player 4 has won!\n");
					}
				}
			}
			else if(rank1 == rank2 ||rank1 == rank3||rank1 == rank4||rank3 == rank2||rank3 == rank4||rank2 == rank4)
			{
				int [] equalPairs = new int[3];
				int [] equalPairs2 = new int[3];
				int j = 0, k = 0;
				int maxRank = Math.max(Math.max(rank1, rank2), Math.max(rank3, rank4));
				 
				if(rank1 == rank2 && rank1 == maxRank)
				{
					System.out.println("");
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand2][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank1 == rank3 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand3][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank1 == rank4 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand1][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand4][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank2 == rank3 && rank2 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand2][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand3][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank4 == rank3 && rank1 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand4][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand3][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 2;
					while(equalPairs[i] == equalPairs2[i] && i>=0)
					{
						i--;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
				else if(rank2 == rank4 && rank2 == maxRank)
				{
					for(int i =12;i>=0;i--)
					{
						if(rankPairs[hand2][i] == 1)
						{
							equalPairs[j] = i;
							j++;
						}
						if(rankPairs[hand4][i] == 1)
						{
							equalPairs2[k] = i;
							k++;
						}
					}
					int i = 0;
					while(equalPairs[i] == equalPairs2[i] && i<3)
					{
						i++;
					}
					if(equalPairs[i] > equalPairs2[i]){
							System.out.println("Player 1 is the winner");
					}
					else if(equalPairs[i] < equalPairs2[i]) {
						System.out.println("Player 2 is the winner");	
					}
					else{
						System.out.println("The game ended in a tie");
					}
				}
	            
			}
		}
		
	}
	
	public void Eval(Card[][] total_hands, int numPlayers) {
		// This is the main evaluation method that uses every other method to evaluate each and every hand
		total_hands = sortCards(total_hands, numPlayers);
		int[] playersFlush = Flush(total_hands, numPlayers);
		int[] playersStraight = Straight(total_hands, numPlayers);
		int[] plStnFl = StraightFlush(total_hands, numPlayers);
		int[] playerPairs = FindPairs(total_hands, numPlayers);
		int[] highestCard = HighestCard(total_hands, numPlayers);
		int playersW4 = 0;
		int[] indexofplayersW4 = new int[numPlayers];
		int[] tiebreaker = new int[numPlayers];
		int StFLcounter = 0;
		int[] indxStFl = new int[numPlayers];

		for (int i = 0; i < numPlayers; i++) {
			indxStFl[i] = 100;
		}
		for (int i = 0; i < numPlayers; i++) {
			if (plStnFl[i] == 1) {
				StFLcounter++;
				indxStFl[i] = i;
			}
		}

		int indexofplayerW4 = 0;

		if (StFLcounter == 0) // No one has a straightflash go down the // hierarchy
		{// Check for 4 of a kind
			for (int i = 0; i < numPlayers; i++) {
				if (playerPairs[i] == 4) {
					playersW4++;
					indexofplayerW4 = i;
				}
			}
			if (playersW4 == 1)
			{
				System.out.printf("Player %d is the winner",indexofplayerW4 + 1);
			}

			else if (playersW4 > 1) {// break a tie.
				for (int k = 0; k < playersW4; k++) {
					indexofplayersW4[k] = -100;
				}
				for (int k = 0; k < playersW4; k++) {
					if (playerPairs[k] == 4)
						indexofplayersW4[k] = k;
				}
				for (int k = 0; k < playersW4; k++) {
					if (indexofplayersW4[k] != -100) {
						tiebreaker[k] = highestCard[indexofplayersW4[k]];
					}
				}

				// Go through the array tiebreaker and find the highest card.
				Arrays.sort(tiebreaker);
				int maximum = tiebreaker[0];
				int index = 0;
				for (int i = 0; i < numPlayers; i++) {
					if (tiebreaker[i] > maximum) {
						maximum = tiebreaker[i];
						index = i;
					}
				}

				System.out.printf("Player %d is the winner!\n", index + 1);
			}

			else {
				int playersWFH = 0;
				int indexofplayerWFH = 0;

				for (int i = 0; i < numPlayers; i++) {
					if (playerPairs[i] == 5) {
						playersWFH++;
						indexofplayerWFH = i;
					}
				}
				if (playersWFH == 1)
				{
					System.out.printf("Player %d is the winner",
							indexofplayerWFH + 1);
				}

				else if (playersWFH > 1) {// break a tie.
					FHTieBreaker(total_hands,numPlayers, playerPairs, playersWFH);
				}

				else {
					int playersWF = 0;
					int indexofplayerWF = 0;

					for (int i = 0; i < numPlayers; i++) {
						if (playersFlush[i] == 1) {
							playersWF++;
							indexofplayerWF = i;
						}
					}

					if (playersWF == 1) {
						System.out.printf("Player %d has won!\n",indexofplayerWF + 1);
					} 
					else if (playersWF > 1) {
						FlushTieBreaker(total_hands, numPlayers, playersFlush, playersWF);
					}
					else {
							int playersWS = 0;
							int indexofplayerWS = 0;

							for (int i = 0; i < numPlayers; i++) {
								if (playersStraight[i] == 1) {
									playersWS++;
									indexofplayerWS = i;
								}
							}

							if (playersWS == 1) {
								System.out.printf("Player %d has won!\n",indexofplayerWS + 1);
							} 
							else if (playersWS > 1) {
								StraightTieBreaker(total_hands, numPlayers, playersStraight, playersWS);
							} 
							else {
								int playersW3 = 0;
								int indexofplayerW3 = 0;

								for (int i = 0; i < numPlayers; i++) {
									if (playerPairs[i] == 3) {
										playersW3++;
										indexofplayerW3 = i;
									}
								}

								if (playersW3 == 1) {
									System.out.printf("Player %d is the winner\n",indexofplayerW3 + 1);
								} 
								else if (playersW3 > 1) {
									ThreeTieBreaker(total_hands, numPlayers, playerPairs, playersW3);
								} 
								else {
									int playersW2 = 0;
									int indexofplayerW2 = 0;

									for (int i = 0; i < numPlayers; i++) {
										if (playerPairs[i] == 8) {
											playersW2++;
											indexofplayerW2 = i;
										}
									}

									if (playersW2 == 1) {
										System.out.printf("Player %d is the winner\n",indexofplayerW2 + 1);
									} 
									else if (playersW2 > 1) {
										TwoPairTieBreaker(total_hands, numPlayers, playerPairs, playersW2);
									} 
									else {
										int playersW1 = 0;
										int indexofplayerW1 = 0;

										for (int i = 0; i < numPlayers; i++) {
											if (playerPairs[i] == 2) {
												playersW1++;
												indexofplayerW1 = i;
											}
										}

										if (playersW1 == 1) {
											System.out.printf("Player %d is the winner\n",indexofplayerW1 + 1);
										} 
										else if (playersW1 > 1) {
											OnePairTieBreaker(total_hands, numPlayers, playerPairs, playersW1);
										} 
										else {
											HighRankTieBreaker(total_hands, playerPairs, numPlayers, highestCard);
										}
									}
								}
							}
						}
					}
				}
			}

		else if (StFLcounter == 1) {
			for (int i = 0; i < numPlayers; i++) {
				if (plStnFl[i] == 1) {
					System.out.printf("Player [%d] is the winner!\n", i);
				}
			}
		} 
		else {
			int[] tieBreaker = new int[numPlayers];
			for (int i = 0; i < numPlayers; i++) {
				tiebreaker[i] = -100;
			}
			for (int i = 0; i < numPlayers; i++) {
				if (indxStFl[i] != -100) {
					tieBreaker[i] = highestCard[indxStFl[i]];
				}

			}
			int max = tiebreaker[0];
			int index_of_max = 0;
			for (int i = 1; i < numPlayers; i++) {
				if (tiebreaker[i] > max)
					max = tiebreaker[i];
				index_of_max = i;
			}
			System.out.printf("The Winner is player %d\n", index_of_max);
		}

	}

}

public class MainofMain {

	public static void main(String[] args) {
		CardPile deck = new CardPile();
		UserPlayerClass hand = new UserPlayerClass(deck);
		AnOpponentPlayerClass ai_hand = new AnOpponentPlayerClass(deck);
		hand.displayHand();
		hand.discardHand(deck);
		hand.displayHand();;
		ai_hand.Discard_computers_hand(deck);
		ai_hand.displayHand(ai_hand.getPlayers());
		Card[][] total_hand = new Card[ai_hand.getPlayers()+1][5];
		Card[] userHand = hand.getHands();
		Card[][] aiHand = ai_hand.getAiHand();
		
		for(int i=0;i<5;i++)
		{	
			total_hand[0][i] = userHand[i];
		}
	
		int a =0;
		for(int i=1;i<ai_hand.getPlayers()+1;i++)
		{
			for(int j=0;j<5;j++)
			{
				total_hand[i][j] = aiHand[a][j];
			}
			a++;
		}
		
		Evaluations eval = new Evaluations();
		
		eval.Eval(total_hand, ai_hand.getPlayers()+1);
		
		
		

	}
}
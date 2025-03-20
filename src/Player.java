import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * @author You
 *
 */
public class Player {
	// Add member variables as needed. You MAY NOT use static variables, or otherwise allow direct communication between
	// different instances of this class by any means; doing so will result in a score of 0.

	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";

	CardKnowledge[] knowledges;
	CardKnowledge[] whatPartnerKnows;
	int focusCardIndex; //implement to be updated as given hints

	Random random;

	/**
	 * This default constructor should be the only constructor you supply.
	 */
	public Player() {
		random = new Random();
		knowledges = new CardKnowledge[5];
		for (int i = 0; i < 5; i++) {
			knowledges[i] = new CardKnowledge();
		}
		whatPartnerKnows = new CardKnowledge[5];
		for (int i = 0; i < 5; i++) {
			whatPartnerKnows[i] = new CardKnowledge();
		}
	}

	public HashSet<Card> getImpossibleCards(Board currentBoard)
	{
		HashMap<Card, Integer> cardCounts = new HashMap<>();

		//check plays
		for(int i = 0; i < currentBoard.tableau.size(); i++)
		{
			for(int j = 0; j < currentBoard.tableau.get(i); j++)
			{
				Card card = new Card(i, j);
				if(cardCounts.containsKey(card))
				{
					cardCounts.put(card, cardCounts.get(card) + 1);
				}
				else
				{
					cardCounts.put(card, 1);
				}
			}
		}

		//check discards
		for(Card c : currentBoard.discards)
		{
			if(cardCounts.containsKey(c))
			{
				cardCounts.put(c, cardCounts.get(c) + 1);
			}
			else
			{
				cardCounts.put(c, 1);
			}
		}

		//fill impossible cards
		HashSet<Card> impossibleCards = new HashSet<>();
		for(Card c : cardCounts.keySet())
		{
			if(c.value == 1 && cardCounts.get(c) == 3)
			{
				impossibleCards.add(c);
			}
			else if(c.value == 2 && cardCounts.get(c) == 2)
			{
				impossibleCards.add(c);
			}
			else if(c.value == 3 && cardCounts.get(c) == 2)
			{
				impossibleCards.add(c);
			}
			else if(c.value == 4 && cardCounts.get(c) == 2)
			{
				impossibleCards.add(c);
			}
			else if(c.value == 5 && cardCounts.get(c) == 1)
			{
				impossibleCards.add(c);
			}
		}
		return impossibleCards;
	}
	
	/**
	 * This method runs whenever your partner discards a card.
	 * @param startHand The hand your partner started with before discarding.
	 * @param discard The card he discarded.
	 * @param disIndex The index from which he discarded it.
	 * @param draw The card he drew to replace it; null, if the deck is empty.
	 * @param drawIndex The index to which he drew it.
	 * @param finalHand The hand your partner ended with after redrawing.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerDiscard(Hand startHand, Card discard, int disIndex, Card draw, int drawIndex, 
			Hand finalHand, Board boardState) {
		whatPartnerKnows[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
	}
	
	/**
	 * This method runs whenever you discard a card, to let you know what you discarded.
	 * @param discard The card you discarded.
	 * @param disIndex The index from which you discarded it.
	 * @param drawIndex The index to which you drew the new card (if drawSucceeded)
	 * @param drawSucceeded true if there was a card to draw; false if the deck was empty
	 * @param boardState The state of the board after play.
	 */
	public void tellYourDiscard(Card discard, int disIndex, int drawIndex, boolean drawSucceeded, Board boardState) {
		//update knowledge
		//call default constructor on cardKnowledge at drawIndex
		//update cardKnowledge at disIndex with board state and discard

		if(drawSucceeded)
		{
			knowledges[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
		}
		else
		{
			knowledges[drawIndex] = null;
		}
	}

	public void tellYourDiscard(int index, boolean drawSucceeded, Board boardState) {
		//update knowledge
		//call default constructor on cardKnowledge at drawIndex
		//update cardKnowledge at disIndex with board state and discard

		if(drawSucceeded)
		{
			knowledges[index] = new CardKnowledge(getImpossibleCards(boardState));
		}
		else
		{
			knowledges[index] = null;
		}

	}
	
	/**
	 * This method runs whenever your partner played a card
	 * @param startHand The hand your partner started with before playing.
	 * @param play The card she played.
	 * @param playIndex The index from which she played it.
	 * @param draw The card she drew to replace it; null, if the deck was empty.
	 * @param drawIndex The index to which she drew the new card.
	 * @param finalHand The hand your partner ended with after playing.
	 * @param wasLegalPlay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerPlay(Hand startHand, Card play, int playIndex, Card draw, int drawIndex,
			Hand finalHand, boolean wasLegalPlay, Board boardState) {

		whatPartnerKnows[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
	}

	
	/**
	 * This method runs whenever you play a card, to let you know what you played.
	 * @param play The card you played.
	 * @param playIndex The index from which you played it.
	 * @param drawIndex The index to which you drew the new card (if drawSucceeded)
	 * @param drawSucceeded  true if there was a card to draw; false if the deck was empty
	 * @param wasLegalPlay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellYourPlay(Card play, int playIndex, int drawIndex, boolean drawSucceeded,
							 boolean wasLegalPlay, Board boardState) {
		//update kowledge

		knowledges[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
	}

	public void tellYourPlay(int index, boolean drawSucceeded, Board boardState) {
		//update kowledge

		if(drawSucceeded)
		{
			knowledges[index] = new CardKnowledge(getImpossibleCards(boardState));
		}
		else
		{
			knowledges[index] = null;
		}
	}



	/**
	 * This method runs whenever your partner gives you a hint as to the color of your cards.
	 * @param color The color hinted, from Colors.java: RED, YELLOW, BLUE, GREEN, or WHITE.
	 * @param indices The indices (from 0-4) in your hand with that color.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		//update knowledge
		for(int index : indices){
			knowledges[index].knowColor(color);
		}
	}

	public void tellColorHint(int color, ArrayList<Integer> indices) {
		//update knowledge
		for(int index : indices){
			knowledges[index].knowColor(color);
		}
	}
	
	/**
	 * This method runs whenever your partner gives you a hint as to the numbers on your cards.
	 * @param number The number hinted, from 1-5.
	 * @param indices The indices (from 0-4) in your hand with that number.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		//update knowledge
		for(int index : indices){
			knowledges[index].knowValue(number);
		}
	}

	public void tellNumberHint(int number, ArrayList<Integer> indices) {
		//update knowledge
		for(int index : indices){
			knowledges[index].knowValue(number);
		}
	}

	public ArrayList<Integer> getIndicesOfColor(Hand partnerHand, int color)
	{
		ArrayList<Integer> indices = new ArrayList<>();
		for(int i = 0; i < partnerHand.size(); i++)
		{
			if(partnerHand.get(i).color == color)
			{
				indices.add(i);
			}
		}
		return indices;
	}

	public ArrayList<Integer> getIndicesOfValue(Hand partnerHand, int value)
	{
		ArrayList<Integer> indices = new ArrayList<>();
		for(int i = 0; i < partnerHand.size(); i++)
		{
			if(partnerHand.get(i).value == value)
			{
				indices.add(i);
			}
		}
		return indices;
	}

	public boolean isPartnerCardFullyLessThanOrEqualToAllBoardValues(Card c, Board boardState)
	{
		for(int i = 0; i < boardState.tableau.size(); i++)
		{
			if(c.value > boardState.tableau.get(c.color))
			{
				return false;
			}
		}
		return true;
	}

	public boolean isColorOfCardFullyUsed(Card c, Board boardState)
	{
		return boardState.tableau.get(c.color) == 5;
	}
	
	/**
	 * This method runs when the game asks you for your next move.
	 * @param yourHandSize How many cards you have in hand.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The current state of the board.
	 * @return A string encoding your chosen action. Actions should have one of the following formats; in all cases,
	 *  "x" and "y" are integers.
	 * 	a) "PLAY x y", which instructs the game to play your card at index x and to draw a card back to index y. You
	 *     should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Illegal plays will consume a fuse; at 0 fuses, the game ends with a score of 0.
	 *  b) "DISCARD x y", which instructs the game to discard the card at index x and to draw a card back to index y.
	 *     You should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Discarding returns one hint if there are fewer than the maximum number available.
	 *  c) "NUMBERHINT x", where x is a value from 1-5. This command informs your partner which of his cards have a value
	 *     of the chosen number. An error will result if none of his cards have that value, or if no hints remain.
	 *     This command consumes a hint.
	 *  d) "COLORHINT x", where x is one of the RED, YELLOW, BLUE, GREEN, or WHITE constant values in Colors.java.
	 *     This command informs your partner which of his cards have the chosen color. An error will result if none of
	 *     his cards have that color, or if no hints remain. This command consumes a hint.
	 */
	public String ask(int yourHandSize, Hand partnerHand, Board boardState) {
		System.out.println(YELLOW + "NEW ASK: -----------------------------------------------------------" + RESET);
		System.out.println("Here is the information that I know about each card:");
		for(int i = 0; i < yourHandSize; i++)
		{
			System.out.println("Card " + i + ": " + knowledges[i].getOptions());
		}

		//do I have an obvious discard?
			//update method
			//Discard
		//do I have an obvious play?
			//update method
			//Play
		//does partner have obvious hints?
			//update method
			//Hint
		//Tokens > 2
			//Hint color/number that has most of that type
		//else
			//Discard rightmost unknown card

		//obvious discard?
		for(int i = 0; i < knowledges.length; i++)
		{
			if(knowledges[i].isDiscardable(boardState))
			{
				tellYourDiscard(i, true, boardState);
				System.out.println(GREEN + "Discard " + i + " " + i + RESET);
				return "DISCARD " + i + " " + i;
			}
		}

		//obvious play?
		for(int i = 0; i < knowledges.length; i++)
		{
			if(knowledges[i].isDefinitelyPlayable(boardState))
			{
				tellYourPlay(i, true, boardState);
				System.out.println(GREEN + "Play " + i + " " + i + RESET);
				return "PLAY " + i + " " + i;
			}
//			if(knowledges[i].couldBePlayable(boardState))
//			{
//				if(random.nextInt(3) == 1)
//				{
//					tellYourPlay(i, true, boardState);
//					return "PLAY " + i + " " + i;
//				}
//			}
		}

		//obvious hints?
			//does partner have a playable card?
			//does partner have a discardable card?
				//number complete
				//color complete
			//does partner have > 2 of a number/color?

		//this is checking if partner has a playable card
		if(boardState.numHints > 0)
		{
			for(int i = 0; i < partnerHand.size(); i++)
			{
				Card currentCard = partnerHand.get(i);
				//TODO: this needs fixed
				System.out.println(BLUE + "Checking if partner has a playable card: " + currentCard.value + " == " + ((int)(boardState.tableau.get(i)) + 1) + " && " + currentCard.color + " == " + i + RESET);
				if(currentCard.value == boardState.tableau.get(i) + 1 && currentCard.color == i)
				{
					if(whatPartnerKnows[i].getKnownValue() != currentCard.value)
					{
						tellNumberHint(currentCard.value, getIndicesOfValue(partnerHand, currentCard.value));
						System.out.println(GREEN + "Number hint " + currentCard.value + RESET);
						return "NUMBERHINT " + currentCard.value;
					}
				}
			}

			//this hints a guaranteed discard
			for(int i = 0; i < partnerHand.size(); i++)
			{
				Card currentCard = partnerHand.get(i);
				if(isColorOfCardFullyUsed(currentCard, boardState) || isPartnerCardFullyLessThanOrEqualToAllBoardValues(currentCard, boardState))
				{
					if(whatPartnerKnows[i].getKnownValue() != currentCard.value)
					{
						tellNumberHint(currentCard.value, getIndicesOfValue(partnerHand, currentCard.value));
						System.out.println(GREEN + "Number hint " + currentCard.value + RESET);
						return "NUMBERHINT " + currentCard.value;
					}
				}
			}
		}

		//if we have a lot of hints, give a hint that gives partner the most info possible
		if(boardState.numHints > 1)
		{
			//hint color/number that has most of that type
			int[] colorCounts = new int[5];
			int[] valueCounts = new int[5];
			for(int i = 0; i < partnerHand.size(); i++)
			{
				Card currentCard = partnerHand.get(i);
				colorCounts[currentCard.color]++;
				valueCounts[currentCard.value - 1]++;
			}

			int maxColor = 0;
			int maxColorCount = 0;
			for(int i = 0; i < colorCounts.length; i++)
			{
				if(colorCounts[i] > maxColorCount)
				{
					maxColor = i;
					maxColorCount = colorCounts[i];
				}
			}

			int maxValue = 0;
			int maxValueCount = 0;
			for(int i = 0; i < valueCounts.length; i++)
			{
				if(valueCounts[i] > maxValueCount)
				{
					maxValue = i + 1;
					maxValueCount = valueCounts[i];
				}
			}

			if(maxColorCount > maxValueCount)
			{
				System.out.println(BLUE + "It seems like the most in this card are colors " + Colors.suitColor(maxColor) + RESET);
				tellColorHint(maxColor, getIndicesOfColor(partnerHand, maxColor));
				System.out.println(GREEN + "Color hint " + maxColor + RESET);
				return "COLORHINT " + maxColor;
			}
			else
			{
				System.out.println(BLUE + "It seems like the most in this card are values " + maxValue + RESET);
				tellNumberHint(maxValue, getIndicesOfValue(partnerHand, maxValue));
				System.out.println(GREEN + "Number hint " + maxValue + RESET);
				return "NUMBERHINT " + (maxValue);
			}
		}



		return "DISCARD 0 0";
	}
}

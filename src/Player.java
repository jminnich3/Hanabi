import java.lang.reflect.Array;
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
	ArrayList<Integer> focus;

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

	public ArrayList<Card> getCardsThatHaveOneLeft(Board currentBoard)
	{

		//build a map that contains all initial counts of cards
		HashMap<Card, Integer> cardCounts = new HashMap<>();
		for(int clr=Colors.MIN_COLOR; clr<=Colors.MAX_COLOR; clr++) {
			for (int val=Card.MIN_VALUE; val<=Card.MAX_VALUE; val++) {
				Card theCard = new Card(clr, val);

				if(val == 1)
				{
					cardCounts.put(theCard, 3);
				}
				else if(val == 2)
				{
					cardCounts.put(theCard, 2);
				}
				else if(val == 3)
				{
					cardCounts.put(theCard, 2);
				}
				else if(val == 4)
				{
					cardCounts.put(theCard, 2);
				}
				else
				{
					cardCounts.put(theCard, 1);
				}
			}
		}

		//remove all cards that have been played
		for(int i = 0; i < currentBoard.tableau.size(); i++)
		{
			for(int j = 0; j < currentBoard.tableau.get(i); j++)
			{
				Card card = new Card(i, j);
				if(cardCounts.containsKey(card))
				{
					cardCounts.remove(card);
				}
				else
				{
					System.out.println(RED + "I THOUGHT STARTING AT 0 WOULD BE A BAD IDEA!!!" + RESET);
				}
			}
		}

		//subtract discarded cards from card counts
		for(Card c : currentBoard.discards)
		{
			if(cardCounts.containsKey(c))
			{
				cardCounts.put(c, cardCounts.get(c) - 1);
			}
		}

		//return an arrayList of cards that have a count of 1
		ArrayList<Card> cardsWithOneLeft = new ArrayList<>();
		for(Card c : cardCounts.keySet())
		{
			if(cardCounts.get(c) == 1)
			{
				cardsWithOneLeft.add(c);
			}
		}

		return cardsWithOneLeft;
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
	

	 // This method runs whenever your partner discards a card.
	public void tellPartnerDiscard(Hand startHand, Card discard, int disIndex, Card draw, int drawIndex, 
			Hand finalHand, Board boardState) {
		whatPartnerKnows[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
	}
	
	// This method runswhenever you discard a card, to let you know what you discarded.
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

	// This method runs whenever your partner played a card
	public void tellPartnerPlay(Hand startHand, Card play, int playIndex, Card draw, int drawIndex,
			Hand finalHand, boolean wasLegalPlay, Board boardState) {

		whatPartnerKnows[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
	}

	// This method runs whenever you play a card, to let you know what you played.
	public void tellYourPlay(Card play, int playIndex, int drawIndex, boolean drawSucceeded,
							 boolean wasLegalPlay, Board boardState) {
		if(drawSucceeded)
		{
			knowledges[drawIndex] = new CardKnowledge(getImpossibleCards(boardState));
		}
		else
		{
			knowledges[drawIndex] = null;
		}
	}

	//  This method runs whenever your partner gives you a hint as to the color of your cards.
	public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		focus = indices;
		//update knowledge
		for (int index : indices) {
			knowledges[index].knowColor(color);
		}
	}

	 // This method runs whenever your partner gives you a hint as to the numbers on your cards.
	public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		focus = indices;

		for(int index : indices){
			knowledges[index].knowValue(number);
		}
	}

	public void updatePartnerNumberHint(int number, ArrayList<Integer> indices) {
		for(int index : indices){
			whatPartnerKnows[index].knowValue(number);
		}
	}

	public void updatePartnerColorHint(int color, ArrayList<Integer> indices) {
		for(int index : indices){
			whatPartnerKnows[index].knowColor(color);
		}
	}

	public void resetFocus()
	{
		focus = null;
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

		// debug info
		System.out.println(YELLOW + "NEW ASK: -----------------------------------------------------------" + RESET);
		System.out.println("Here is the information that I know about each card:");
		for(int i = 0; i < yourHandSize; i++)
		{
			System.out.println("Card " + i + ": " + knowledges[i].getOptions());
		}
		System.out.println();

		System.out.println("PARTNER CARD INFO:");
		for(int i = 0; i < yourHandSize; i++)
		{
			System.out.println("Card " + i + ": " + whatPartnerKnows[i].getOptions());
		}

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
				// tellYourPlay(i, true, boardState);
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

				System.out.println(BLUE + "Checking if partner has a playable card: " + currentCard.value + " of " + Colors.suitColor(currentCard.color)  + " == " + ((int)(boardState.tableau.get(currentCard.color)) + 1) + " of " + Colors.suitColor(currentCard.color) + RESET);

				//currentCard.value == boardState.tableau.get(currentCard.color) + 1
				if(currentCard.value == boardState.tableau.get(currentCard.color) + 1)
				{
					if(whatPartnerKnows[i].getKnownValue() != currentCard.value)
					{
						updatePartnerNumberHint(currentCard.value, getIndicesOfValue(partnerHand, currentCard.value));
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
						updatePartnerNumberHint(currentCard.value, getIndicesOfValue(partnerHand, currentCard.value));
						System.out.println(GREEN + "Number hint " + currentCard.value + RESET);
						return "NUMBERHINT " + currentCard.value;
					}
				}
			}
		}

		//if we have a lot of hints, give a hint that gives partner the most info possible
		if(boardState.numHints > 6)
		{
			//hint color/number that has most of that type UNLESS they already know about it
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
				updatePartnerColorHint(maxColor, getIndicesOfColor(partnerHand, maxColor));
				System.out.println(GREEN + "Color hint " + maxColor + RESET);
				return "COLORHINT " + maxColor;
			}
			else
			{
				System.out.println(BLUE + "It seems like the most in this card are values " + maxValue + RESET);
				updatePartnerColorHint(maxValue, getIndicesOfValue(partnerHand, maxValue));
				System.out.println(GREEN + "Number hint " + maxValue + RESET);
				return "NUMBERHINT " + (maxValue);
			}
		}

		return "DISCARD 0 0";
	}

	public boolean leftMostPlayPossible(ArrayList<Integer> focusIndexes, Board boardState){
		return knowledges[focusIndexes.getFirst()].couldBePlayable(boardState);
	}

	public boolean uniqueNumLeftOn(Hand partnerHand, int focusIndex){
		int val = partnerHand.get(focusIndex).value;

		for (int i = 0; i < focusIndex; i++) {
			if(partnerHand.get(i).value == val){
				return false;
			}
		}

		return true;
	}

	public boolean uniqueColorLeftOn(Hand partnerHand, int focusIndex){
		int color = partnerHand.get(focusIndex).color;

		for (int i = 0; i < focusIndex; i++) {
			if(partnerHand.get(i).color == color){
				return false;
			}
		}

		return true;
	}


	public int getDiscardIndex(){
		for (int i = 4; i >= 0; i--) {
			if(knowledges[i].numOptions() == 25){
				return i;
			}
		}
		return -1;
	}

}

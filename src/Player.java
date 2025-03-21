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
			for(int j = 1; j <= currentBoard.tableau.get(i); j++)
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
		//ORDER OF PRIORITY CHOICES

		//If we received a hint, (focus.size() > 0)
		if(focus != null && !focus.isEmpty()){
			//ALL OF THESE ARE WITHIN OUR FOCUS INDICES

			//is there an obvious discard?
			for(int idx : focus){
				if(knowledges[idx].isDiscardable(boardState)){
					//discard it
					resetFocus();
					return "DISCARD " + idx + " " + idx;

				}
			}

			//is there an obvious play? obvious when num options == 1 and playable
			for(int idx : focus){
				if(knowledges[idx].isDefinitelyPlayable(boardState)){
					resetFocus();
					return "PLAY " + idx + " " + idx;
				}
			}

			//is the left most card playable?
			if(knowledges[focus.getFirst()].couldBePlayable(boardState)){
				//play it
				int playIndex = focus.getFirst();
				resetFocus();
				return "PLAY " + playIndex + " " + playIndex;
			}
		}

		resetFocus();
		//If our token count > 6
		//if(boardState.numHints > 6) {
		for (int i = 0; i < 5; i++) {
			if (knowledges[i].isDefinitelyPlayable(boardState)) {
				return "PLAY " + i + " " + i;
			}
		}

		//is there an obvious discard?
		for (int i = 0; i < 5; i++) {
			if (knowledges[i].isDiscardable(boardState)) {
				//discard it
				return "DISCARD " + i + " " + i;
			}
		}




		//If we have tokens
		if(boardState.numHints > 0) {
			//if partner has obvious play
			for (int i = 0; i < 5; i++) {
				if (playable(partnerHand.get(i), boardState)) {
					//send a hint with left most card being highlighted
					//if numleft is unique
					if(uniqueNumLeftOn(partnerHand, i))
					{
						//System.out.println(BLUE + "Hinting to play" + RESET);
						return "NUMBERHINT " + partnerHand.get(i).value;
					}
					//if colorleft is unique
					if(uniqueColorLeftOn(partnerHand, i))
					{
						//System.out.println(BLUE + "Hinting to play" + RESET);
						return "COLORHINT " + partnerHand.get(i).color;
					}
				}
			}
			//if partner has obvious discard
			for (int i = 0; i < 5; i++) {
				if (discardable(partnerHand.get(i), boardState)) {
					//send a hint with left most card being highlighted
					//if numleft is unique
					if(uniqueNumLeftOn(partnerHand, i))
					{
						//System.out.println(BLUE + "Hinting to discard" + RESET);
						return "NUMBERHINT " + partnerHand.get(i).value;
					}
					//if colorleft is unique
					if(uniqueColorLeftOn(partnerHand, i))
					{
						//System.out.println(BLUE + "Hinting to discard" + RESET);
						return "COLORHINT " + partnerHand.get(i).color;
					}
				}
			}
			//if the card in the discard position is important
			int partnerDiscardIndex = getPartnerDiscardIndex();
			if(partnerDiscardIndex != -1)
			{
				Card partnerCard = partnerHand.get(partnerDiscardIndex);
				if(getCardsThatHaveOneLeft(boardState).contains(partnerCard))
				{
					//this card is important
					if(!whatPartnerKnows[partnerDiscardIndex].couldBePlayable(boardState))
					{
						//this card is NOT playable
						//if its value is leftmost unique
						if(uniqueNumLeftOn(partnerHand, partnerDiscardIndex))
						{
							//send a hint of its value
							//System.out.println(BLUE + "Hinting to save important card" + RESET);
							return "NUMBERHINT " + partnerCard.value;
						}
					}
				}
			}
		}

		int discardIndex = getDiscardIndex();
		if(discardIndex != -1){
			return "DISCARD " + discardIndex + " " + discardIndex;
		}
		else
		{
			discardIndex = getLeftMostNonNullIndex();
			return "DISCARD " + discardIndex + " " + discardIndex;
		}
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

	public int getPartnerDiscardIndex(){
		for (int i = 4; i >= 0; i--) {
			if(whatPartnerKnows[i].numOptions() == 25){
				return i;
			}
		}
		return -1;
	}

	public boolean playable(Card c, Board boardState){
		return c.value == (boardState.tableau.get(c.color) + 1);
	}

	public boolean discardable(Card c, Board boardState){
		System.out.println(RED + "Checking to see if " + c.toString() + " is discardable" + RESET);
		for (int i = 0; i < boardState.tableau.size(); i++) {
			if (boardState.tableau.get(i) < c.value) {
				// This card option is still useful for the tableau
				return false;
			}
		}
		return true;
	}

	public int getLeftMostNonNullIndex()
	{
		int minOptions = 26;
		int minIndex = 5;

		for (int i = 4; i >= 0; i--) {
			if(knowledges[i] != null && knowledges[i].numOptions() < minOptions){
				minOptions = knowledges[i].numOptions();
				minIndex = i;
			}
		}

		return minIndex;
	}

}

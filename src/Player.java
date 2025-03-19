import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author You
 *
 */
public class Player {
	// Add member variables as needed. You MAY NOT use static variables, or otherwise allow direct communication between
	// different instances of this class by any means; doing so will result in a score of 0.


	/**
	 * This default constructor should be the only constructor you supply.
	 */
	public Player() {

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

	}



	/**
	 * This method runs whenever your partner gives you a hint as to the color of your cards.
	 * @param color The color hinted, from Colors.java: RED, YELLOW, BLUE, GREEN, or WHITE.
	 * @param indices The indices (from 0-4) in your hand with that color.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		
	}
	
	/**
	 * This method runs whenever your partner gives you a hint as to the numbers on your cards.
	 * @param number The number hinted, from 1-5.
	 * @param indices The indices (from 0-4) in your hand with that number.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		
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
		// Step 1: Check if any card can be played
		for (int i = 0; i < yourHandSize; i++) {
			if (isPlayable(getCardFromHand(i), boardState)) {
				int drawIndex = getDrawIndex();
				return "PLAY " + i + " " + drawIndex;
			}
		}

		// Step 2: Check if a card is likely playable
		for (int i = 0; i < yourHandSize; i++) {
			if (isLikelyPlayable(getCardFromHand(i), boardState)) {
				int drawIndex = getDrawIndex();
				return "PLAY " + i + " " + drawIndex;
			}
		}

		// Step 3: Check if a hint guarantees a playable card
		if (boardState.getHintTokens() > 0) {
			for (int i = 0; i < partnerHand.size(); i++) {
				if (isGuaranteedPlayable(partnerHand.getCard(i), boardState)) {
					return giveOptimalHint(partnerHand, i);
				}
			}
		}

		// Step 4: Provide a hint to improve knowledge
		if (boardState.getHintTokens() > 0 && shouldProvideKnowledgeHint(partnerHand, boardState)) {
			return giveGeneralKnowledgeHint(partnerHand);
		}

		// Step 5: Provide a correctional hint if a critical card is at risk
		int criticalIndex = findCriticalCardAtRisk(partnerHand, boardState);
		if (boardState.getHintTokens() > 0 && criticalIndex != -1) {
			return giveOptimalHint(partnerHand, criticalIndex);
		}

		// Step 6: Discard if low on clues and discard is safe
		if (boardState.getHintTokens() == 0 || shouldDiscardSafely(yourHandSize, boardState)) {
			int discardIndex = findSafeDiscardIndex();
			int drawIndex = getDrawIndex();
			return "DISCARD " + discardIndex + " " + drawIndex;
		}

		// Step 7: Discard to provide subliminal information
		if (canProvideSubliminalHint(boardState)) {
			int discardIndex = findSubliminalDiscardIndex();
			int drawIndex = getDrawIndex();
			return "DISCARD " + discardIndex + " " + drawIndex;
		}

		// Step 8: Avoid hinting a maxed-out color
		if (boardState.isColorMaxedOut()) {
			return "DISCARD " + findSafeDiscardIndex() + " " + getDrawIndex();
		}

		// Step 9: Prioritize hinting a known 5 in the endgame
		if (boardState.isEndgame() && partnerHasKnownFive(partnerHand)) {
			return hintFive(partnerHand);
		}

		// Step 10: Optimize every play or hint if the deck is nearly empty
		if (boardState.getRemainingDeckSize() <= 2) {
			return optimizeFinalPlaysOrHints(partnerHand, boardState);
		}

		// Default: Discard the oldest card
		int fallbackDiscard = findOldestCardIndex();
		return "DISCARD " + fallbackDiscard + " " + getDrawIndex();
	}

	// Placeholder methods for logic
	/*
	private boolean isPlayable(Card card, Board boardState) {
		return true;
	}

	private boolean isLikelyPlayable(Card card, Board boardState) {
		return true;
	}

	private boolean isGuaranteedPlayable(Card card, Board boardState) {
		return true;
	}

	private boolean shouldProvideKnowledgeHint(Hand partnerHand, Board boardState) {
		return true;
	}

	private int findCriticalCardAtRisk(Hand partnerHand, Board boardState) {
		return -1;
	}

	private boolean shouldDiscardSafely(int handSize, Board boardState) {
		return true;
	}

	private boolean canProvideSubliminalHint(Board boardState) {
		return true;
	}

	private int findSafeDiscardIndex() {
		return 0;
	}

	private int findSubliminalDiscardIndex() {
		return 0;
	}

	private boolean partnerHasKnownFive(Hand partnerHand) {
		return true;
	}

	private String hintFive(Hand partnerHand) {
		return "NUMBERHINT 5";
	}

	private int findOldestCardIndex() {
		return 0;
	}

	private String optimizeFinalPlaysOrHints(Hand partnerHand, Board boardState) {
		return "DISCARD 0 0";
	}

	private int getDrawIndex() {
		return 0;
	}

	private String giveOptimalHint(Hand partnerHand, int cardIndex) {
		Card card = partnerHand.getCard(cardIndex);
		return shouldGiveNumberHint(card) ? "NUMBERHINT " + card.getValue() : "COLORHINT " + card.getColor();
	}

	private boolean shouldGiveNumberHint(Card card) {
		return true;
	}

	private Card getCardFromHand(int index) {
		return new Card(Colors.RED, 1);
	}

	private String giveGeneralKnowledgeHint(Hand partnerHand) {
		return "NUMBERHINT 1";
	}
	*/
}

//CODE FLOW

/*
IF (bot has a known playable card)
    → Play the card
ELSE IF (bot can infer a likely playable card)
    → Play the card
ELSE IF (giving a hint will guarantee a playable card for the other bot)
    → Give the hint
ELSE IF (giving a hint will improve the other bot's knowledge)
    → Give the hint
ELSE IF (the other bot is likely to discard a critical card)
    → Provide a correctional hint
ELSE IF (low on clues and discard is safe)
    → Discard a guaranteed safe card
ELSE IF (discarding provides subliminal information)
    → Discard to hint indirectly
ELSE IF (a color is maxed out)
    → Avoid hinting that color
ELSE IF (endgame with a known 5)
    → Prioritize hinting the 5
ELSE IF (deck is nearly empty)
    → Optimize every play or hint
ELSE
    → Default to discarding the oldest card
 */

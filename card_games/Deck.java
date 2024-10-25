/**
 * Author: Demetrius Semanko
 * Purpose: This class is designed to represent a basic Deck in a card game.
 * It can represent a shoe, a deck, or be extended to represent hands in a game.
 * Every Deck instance should contain
 */

package card_games;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> deck = new ArrayList<Card>();
    /**
     * Adds a Card-type element to this instance of Deck's ArrayList
     * @param card the Card-type element to be added to this instance of Deck's ArrayList
     */
    public void addCard(Card card) {
        this.deck.add(card);
    }
    /**
     * Clears this Deck's ArrayList of all elements then adds a new deck of cards
     */
    public void freshDeck() {
        this.deck.clear();
        this.addDeck();
    }
    /**
     * Adds one full deck of cards to this instance of Deck
     */
    private void addDeck() {
        for (int i = 0; i < Card.SUITS.length; i++) {
            for (int j = 0; j < Card.RANKS.length; j++) {
                this.deck.add(new Card(Card.RANKS[j], Card.SUITS[i]));
            }
        }
    }
    /**
     * Adds a specified number of full-decks to this instance of Deck
     * @param amount the number of full-decks to be added to this instance of Deck
     */
    public void addDeck(int amount) {
        if (amount >= 1) {
            for (int i = 1; i <= amount; i++) {
                this.addDeck();
            }
        }
    }
    /**
     * Returns a Card-type instance from the passed index within this instance of Deck's ArrayList.
     * Returns IllegalArgumentExcepetion if the passed index is invalid.
     * @return returns a Card-type instance from the passed index within this instance of Deck's ArrayList.
     */
    public Card getCard(int index) {
        if (index >= 0 && index < this.deck.size()) {
            return this.deck.get(index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    /**
     * Draws, removes, then returns a Card-type element from this instance of Deck
     * @param randomly the boolean-type randomly-state that determines if the Card-type element is drawn from the top of this Deck or randomly
     */
    public Card drawCard(boolean randomly) {
        if (randomly) {
            Random r = new Random();
            int randInd = r.nextInt(this.deck.size());

            Card returnCard = this.deck.get(randInd);
            this.deck.remove(randInd);

            return returnCard;
        } else {
            Card returnCard = this.deck.get(0);
            this.deck.remove(0);

            return returnCard;
        }
    }
    /**
     * Returns the int-type size of this Deck's ArrayList
     * @return the int-type size of this Deck's ArrayList
     */
    public int size() {
        return this.deck.size();
    }
    /**
     * Shuffles this Deck's ArrayList elements once
     */
    private void shuffle() {
        Random r = new Random();
        for (int i = 0; i < this.deck.size(); i++) {
            int firstIndex = r.nextInt(this.deck.size() - 1);
            int secondIndex = r.nextInt(this.deck.size() - 1);

            Card temp = this.deck.get(firstIndex);
            this.deck.set(firstIndex, this.deck.get(secondIndex));
            this.deck.set(secondIndex, temp);
        }
    }
    /**
     * Shuffles this Deck's ArrayList elements a specified number of times.
     * Specified by the passed parameter.
     * @param amount the number of times this Deck's ArrayList is to be shuffled
     */
    public void shuffle(int amount) {
        if (amount >= 1) {
            for (int i = 1; i <= amount; i++) {
                this.shuffle();
            }
        }
    }
    /**
     * Returns the rank-value total of this Deck's ArrayList.
     * This will sum the rank-values of each Card-type element and return that sum.
     * A boolean-type ignoreRevealed parameter must be passed to determine if the sum
     * is to include or exclude Card-type elements which have the revealed-state set to true.
     * @param ignoreRevealed false honors the revealed-state of Card-instances; true dishonors the state. If the state is honored, their values are NOT included.
     * @return returns the rank-value total of this Deck's ArrayList
     */
    public int getCardValueTotal(boolean ignoreRevealed) {
        int sum = 0;
        for (Card card : this.deck) {
            if (card.getRevealed()) { sum += card.getRankValue(); }
            else if (ignoreRevealed) { sum += card.getRankValue(); }
        }

        return sum;
    }

    public String toString() {
        String outString = "";
        for (int i = 0; i < this.deck.size(); i++) {
            outString += this.deck.get(i).toString() + "\n";
        }
        return outString;
    }
}
package card_games;

/**
 * Author: Demetrius Semanko
 * Purpose: This class is designed to represent a Card in a card game.
 * Every Card has a rank, a suit, and a state of revelation.
 */

import java.util.Random;

public class Card {
    private String rank;
    private String suit;
    private boolean revealed;
    /**
     * String array of all possible card ranks in the Western card deck 
     */
    public static final String[] RANKS = {
        "Ace",
        "Two",
        "Three",
        "Four",
        "Five",
        "Six",
        "Seven",
        "Eight",
        "Nine",
        "Ten",
        "Jack",
        "Queen",
        "King"
    };
    /*
     * String array of all possible card suits in the Western card deck
     */
    public static final String[] SUITS = {
        "Clubs",
        "Spades",
        "Diamonds",
        "Hearts"
    };
    /**
     * The default constructor; Creates an instance of card with randomly selected rank and suit.
     */
    public Card() {
        this("Ace", "Spades");

        Random r = new Random();

        this.rank = RANKS[r.nextInt(RANKS.length)];
        this.suit = SUITS[r.nextInt(SUITS.length)];
    }
    /**
     * The rank-and-suit constructor; Creates an instance of card with the passed rank and suit.
     * Sets the revealed-state of the card to true by default.
     * @param rank the String-type rank that the new instance of Card is to have
     * @param suit the String-type suit that the new instance of Card is to have
     */
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        this.revealed = true;
    }
    /**
     * The rank constructor; Creates an instance of card with the passed rank.
     * Default sets the suit to "Something"; This is the "Joker" constructor.
     * @param rank the String-type rank that the new instance of Card is to have
     */
    public Card(String rank) {
        this(rank, "Something");
    }
    /** 
     * Returns the String-type rank of this instance of Card
     */
    public String getRank() {
        return this.rank;
    }
    /**
     * Returns the int-type rank-value of this instance of Card
     * @return the int-type rank-value of this instance of Card; returns 0 if invalid rank is found in this instance of Card
     */
    public int getRankValue() {
        switch(this.rank) {
            case "Ace": return 1;
            case "Two": return 2;
            case "Three": return 3;
            case "Four": return 4;
            case "Five": return 5;
            case "Six": return 6;
            case "Seven": return 7;
            case "Eight": return 8;
            case "Nine": return 9;
            case "Ten": return 10;
            case "Jack": return 10;
            case "Queen": return 10;
            case "King": return 10;
       	    default: return 0;
	}
    }
    /** 
     * Returns the String-type suit of this instance of Card
     * @return the String-type suit of this instance of Card
     */
    public String getSuit() {
        return this.suit;
    }
    /**
     * Returns the boolean-type revealed-state of this instance of Card
     * @return the boolean-type revealed-state of this instance of Card
     */
    public boolean getRevealed() {
        return this.revealed;
    }
    /** 
     * Sets the boolean-type revealed-state of this instance of Card
     * @param revealed the boolean-type state this instance of Card is to be set to
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
    /**
     * Returns the String-type representation of this instance of Card.
     * If the revealed-state is false, then "***HIDDEN***" is returned instead.
     * @return the String-type representation of this instance of Card
     */
    public String toString() {
        if (this.revealed) { return this.rank + " of " + this.suit; } 
        else { return "***HIDDEN***"; }
    }
}

import card_games.Deck;

public class PlayerHand extends Deck {
    private boolean isNewHand; // Boolean for if the hand is new, true at construction
    private boolean isStanding; // Boolean for if the hand is standing, false at construction
    private boolean isSplitBorn; // Boolean for if the hand was born from a split, false at construction 
    private int handBet;

    public PlayerHand() {
        super();
        this.isNewHand = true;
        this.isStanding = false;
        this.isSplitBorn = false;
    }

    public boolean getIsNewHand() {
        return this.isNewHand;
    }

    public void setIsNewHand(boolean isNewHand) {
        this.isNewHand = isNewHand;
    }

    public boolean getIsStanding() {
        return this.isStanding;
    }

    public void setIsStanding(boolean isStanding) {
        this.isStanding = isStanding;
    }

    public boolean getIsSplitBorn() {
        return this.isSplitBorn;
    }

    public void setIsSplitBorn(boolean isSplitBorn) {
        this.isSplitBorn = isSplitBorn;
    }

    public void setHandBet(int handBet) {
        this.handBet = handBet;
    }

    public int getHandBet() {
        return this.handBet;
    }
}

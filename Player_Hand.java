import card_games.Deck;

public class Player_Hand extends Deck {
    private boolean isNewHand;
    private boolean isStanding;
    private boolean isSplitBorn;
    private int handBet;

    public Player_Hand() {
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
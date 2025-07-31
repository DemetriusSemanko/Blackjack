import card_games.Card;
import card_games.Deck;
import java.util.Scanner;
import java.util.ArrayList;

public class BlackjackCore {
    // Blackjack moves
    public static final String HIT = "HIT";
    public static final String STAND = "STAND";
    public static final String SPLIT = "SPLIT";
    public static final String INSURANCE = "INSURANCE";
    public enum Moves {
	    HIT, STAND, SPLIT, INSURANCE
    }
    // Hand identifiers
    // Consider changing this
    public static final String PLAYER = "PLAYER";
    public static final String DEALER = "DEALER";
    // Hand results
    public static final String BUST = "BUST";
    public static final String PUSH = "PUSH";
    public static final String LOSS = "LOSS";
    public static final String WIN = "WIN";
    public static final String BLACKJACK = "BLACKJACK";
    public enum Results {
	    BUST, PUSH, LOSS, WIN, BLACKJACK
    }
    /**
     * Blackjack game state which tracks the shoe, all hands
     * (player and dealer), and the game's state of completition
     */
    private ArrayList<PlayerHand> playerHands = new ArrayList<>();
    private BlackjackDealer dealerHand = new BlackjackDealer();
    private Deck shoe = new Deck();
    private int playerBalance = 0;
    private boolean done = false;
    
    /**
     * Constructs a new instance of the Blackjack class,
     * where a new Blackjack game begins
     */
    public Blackjack(int shoeSize) {
        shoe.addDeck(shoeSize);
        PlayerHand initialPlayerHand = new PlayerHand();
        initialDeal(initialPlayerHand);
        playerHands.add(initialPlayerHand);
    }

    /**
     * Handles the initial dealing for the player and dealer hands.
     * Two Card-type instances are drawn from the shoe for each Hand.
     * The first card (0th) in the dealer hand is hard-coded to be the "hole" card.
     * @param shoe the reference to the shoe which contains the undealt playing cards
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     */
    private void initialDeal(PlayerHand playerHand) {
        for (int i = 1; i <= 2; i++) {
            playerHand.addCard(shoe.drawCard(false));
            dealerHand.addCard(shoe.drawCard(false));
        }
        dealerHand.getCard(0).setRevealed(false);
    }
    /**
     * CORE
     * Returns the requested buy-in amount from the user. This is only asked once during
     * a single loop of the overall game. The buy-in must be greater than 0.
     * @return the requested buy-in amount
     */
    public static int getPlayerBuyIn() {
        int playerBuyIn = 0;

        boolean done = false;
        while (!done) {
            System.out.print("Please enter a whole integer buy in: $");
            String entry = KEYBOARD.nextLine();
            Scanner lineScan = new Scanner(entry);
            if (lineScan.hasNextInt()) {
                playerBuyIn = lineScan.nextInt();
                if (playerBuyIn > 0) {
                    lineScan.close();
                    done = true;
                }
            }
        }
        
        return playerBuyIn;
    }
    /**
     * CORE
     * Handles the looping of the player's hand. First it generates and displays the options available to the user
     * based off of the dealer's and player's hand. Then the user makes their choice, the choice's changes/effects
     * are made, and then conditions for terminating the overall loop are assessed and applied.
     * @param shoe the reference to the shoe which contains the undealt playing cards
     * @param playerHand the reference to a single instance of type PlayerHand
     * @param dealerHand the reference to a single instance of type BlackjackDealer
     * @param playerHands the refernece to an ArrayList of type PlayerHand (which contains all of the player's hands)
     */
    public static void playerHandLoop(Deck shoe, PlayerHand playerHand, BlackjackDealer dealerHand, ArrayList<PlayerHand> playerHands) {
        boolean playerHandEnd = false;
        while(!playerHandEnd) {
            printHand(PLAYER, playerHand);
            ArrayList<String> options = playerChoices(playerHand, dealerHand);
            if (options.size() > 0) {
                printPlayerChoices(options);                
                String choiceStr = getPlayerChoice(options);
                makeChanges(shoe, playerHand, dealerHand, choiceStr, playerHands);
            }

            if (playerHand.getIsStanding()) {
                playerHandEnd = true;
            } else if (getCardValueTotalBlackjack(playerHand) > 21) {
                playerHandEnd = true;
            } else if (getCardValueTotalBlackjack(playerHand) == 21) {
                System.out.println(BLACKJACK);
                playerHandEnd = true;
            }
        }
    }
    /**
     * A function which can print the hand of both 
     * BlackjackDealer and PlayerHand types thanks to their inheritance from Deck
     * @param handHolder the "name"/"assignment" of the hand. Used in displaying ownership
     * @param hand the Deck-type reference to the handholder's hand
     */
    public static void printHand(String handHolder, Deck hand) {
        System.out.println("\n" + SPACER + handHolder + " HAND" + SPACER);
        System.out.println(hand.toString());

        int sum = 0;
        sum = getCardValueTotalBlackjack(hand);

        System.out.println(handHolder + "'S HAND TOTAL: " + sum);
    }
    /**
     * CORE
     * Calculates, populates, and returns an ArrayList<String> instance which contains the choices available
     * to the player for the given playerHand.
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     * @return an ArrayList<String> instance which contains the available player choices
     */
    public static ArrayList<String> playerChoices(PlayerHand playerHand, BlackjackDealer dealerHand) {
        ArrayList<String> choices = new ArrayList<String>();
        if (playerHand.size() > 1) {
            if (playerHand.getIsNewHand()) {
                
                if (playerHand.getCard(0).getRankValue() == playerHand.getCard(1).getRankValue()) {
                    choices.add(SPLIT);
                }
                
                if (dealerHand.getCard(1).getRank().equals(Card.RANKS[0])) { // Card.RANKS[0] == "Ace"
                    choices.add(INSURANCE);
                }
            }
        }
        if (getCardValueTotalBlackjack(playerHand) < 21) {
            choices.add(HIT);
            choices.add(STAND);
        }

        return choices;
    }
    /**
     * CORE
     * Makes the changes to the passed PlayerHand-type instance based on the choice made by the player.
     * @param shoe the reference to the shoe which contains the undealt playing cards
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     * @param choice the player's choice
     * @param playerHands an ArrayList of type PlayerHand which contains all player hands
     */
    public static void makeChanges(Deck shoe, PlayerHand playerHand, BlackjackDealer dealerHand, String choice, ArrayList<PlayerHand> playerHands) {
        if (choice.equals(HIT)) {
            playerHand.addCard(shoe.drawCard(false));
            playerHand.setIsNewHand(false);
        } else if (choice.equals(STAND)) {
            playerHand.setIsStanding(true);
        } else if (choice.equals(SPLIT)) {
            playerHands.add(new PlayerHand());
            playerHands.get(playerHands.size() - 1).addCard(playerHand.drawCard(false));
            playerHandLoop(shoe, playerHands.get(playerHands.size() - 1), dealerHand, playerHands);
        } else if (choice.equals(INSURANCE)) {
            playerHand.setIsNewHand(false);
        }
    }
    /**
     * CORE
     * A method for calculating the total of the passed hand. This method makes it so
     * that any hidden cards are not added to the total. This is to maintain the
     * hidden-ness of the hole card.
     * @param hand a Deck-type object representing the hand that is to be totaled
     * @return the total of the hand, depending on if revealed cards should be ignored
     */
    public static int getCardValueTotalBlackjack(Deck hand) {
        int sum = hand.getCardValueTotal(false);
        for (int i = 0; i < hand.size(); i++) {
            if (hand.getCard(i).getRank().equals("Ace") && hand.getCard(i).getRevealed() == true) {
                if (sum + 10 < 22) { sum += 10; }
            }
        }
        return sum;
    }
}

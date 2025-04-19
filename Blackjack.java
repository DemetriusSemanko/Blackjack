import card_games.Card;
import card_games.Deck;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
// import java.util.concurrent.TimeUnit;

public class Blackjack {
    public static final String HIT = "HIT";
    public static final String STAND = "STAND";
    public static final String SPLIT = "SPLIT";
    public static final String INSURANCE = "INSURANCE";
    public static final String PLAYER = "PLAYER";
    public static final String DEALER = "DEALER";
    public static final String BUST = "BUST";
    public static final String PUSH = "PUSH";
    public static final String LOSS = "LOSS";
    public static final String WIN = "WIN";
    public static final String BLACKJACK = "BLACKJACK";
    public static final String SPACER = "---";
    public static final String SPACER_LONG = "---------";

    public static void main(String[] args) /* throws InterruptedException */ {
        introduction();

        int shoeSize = getShoeSize();
        int playerBalance = getPlayerBuyIn();
        boolean doneFinal = false;
        while(!doneFinal) {
            /**
             * Declaring the shoe, initializing it by adding decks (determined
             * earlier in the program). Finally, the shoe is shuffled.
             */
            Deck shoe = new Deck();
            shoe.addDeck(shoeSize);
            shoe.shuffle(5);
            /**
             * Declaring an ArrayList of type Player_Hand. Initializing it with
             * an instance of Player_Hand.
             */
            ArrayList<Player_Hand> playerHand = new ArrayList<Player_Hand>();
            playerHand.add(new Player_Hand());
            /**
             * Declaring one instance of type Blackjack_Dealer
             */
            Blackjack_Dealer dealerHand = new Blackjack_Dealer(false);

            System.out.println(SPACER_LONG + "BALANCE: " + playerBalance + SPACER_LONG);

            initialDeal(shoe, playerHand.get(0), dealerHand);
            printHand(DEALER, dealerHand);

            boolean gameEnd = false;
            while(!gameEnd) {
                playerHandLoop(shoe, playerHand.get(0), dealerHand, playerHand);

                for (int i = 0; i < dealerHand.size(); i++) {
                    if (!dealerHand.getCard(i).getRevealed()) { dealerHand.getCard(i).setRevealed(true); }
                }
                
                boolean dealerHandEnd = false;
                while(!dealerHandEnd) {
                    printHand(DEALER, dealerHand);
                    //printHand(PLAYER, playerHand);

                    // TimeUnit.SECONDS.sleep(1);
                    if (getCardValueTotalBlackjack(dealerHand, false) >= 17) {
                        dealerHandEnd = true;
                    } else {
                        dealerHand.addCard(shoe.drawCard(false));
                    }
                }

                gameEnd = true;
            }
            printResults(playerHand, dealerHand);
            doneFinal = isNotDone();
        }
    }
    /**
     * Returns the requested buy-in amount from the user. This is only asked once during
     * a single loop of the overall game. The buy-in must be greater than 0.
     * @return the requested buy-in amount
     */
    public static int getPlayerBuyIn() {
        int playerBuyIn = 0;

        boolean done = false;
        while (!done) {
            System.out.print("Please enter a whole integer buy in: $");
            Scanner keyboard = new Scanner(System.in);
            String entry = keyboard.nextLine();
            Scanner lineScan = new Scanner(entry);
            if (lineScan.hasNextInt()) {
                playerBuyIn = lineScan.nextInt();
                if (playerBuyIn > 0) {
                    done = true;
                }
            }
        }

        return playerBuyIn;
    }
    /**
     * Handles the looping of the player's hand. First it generates and displays the options available to the user
     * based off of the dealer's and player's hand. Then the user makes their choice, the choice's changes/effects
     * are made, and then conditions for terminating the overall loop are assessed and applied.
     * @param shoe the reference to the shoe which contains the undealt playing cards
     * @param playerHand the reference to a single instance of type Player_Hand
     * @param dealerHand the reference to a single instance of type Blackjack_Dealer
     * @param playerHands the refernece to an ArrayList of type Player_Hand (which contains all of the player's hands)
     */
    public static void playerHandLoop(Deck shoe, Player_Hand playerHand, Blackjack_Dealer dealerHand, ArrayList<Player_Hand> playerHands) {
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
            } else if (getCardValueTotalBlackjack(playerHand, false) > 21) {
                playerHandEnd = true;
            } else if (getCardValueTotalBlackjack(playerHand, false) == 21) {
                System.out.println("\nBlackjack!");
                playerHandEnd = true;
            }
        }
    }
    /**
     * Simple function which abstracts the loop for displaying the player's options.
     * @param options the options generated based on hand information, stored as ArrayList of type String
     */
    public static void printPlayerChoices(ArrayList<String> options) {
        int i = 1;
        for (String option : options) {
            System.out.println(i + ": " + option);
            i++;
        }
    }
    /**
     * A function which can print the hand of both 
     * Blackjack_Dealer and Player_Hand types thanks to their inheritance from Deck
     * @param handHolder the "name"/"assignment" of the hand. Used in displaying ownership
     * @param hand the Deck-type reference to the handholder's hand
     */
    public static void printHand(String handHolder, Deck hand) {
        System.out.println("\n" + SPACER + handHolder + " HAND" + SPACER);
        System.out.println(hand.toString());

        int sum = 0;
        sum = getCardValueTotalBlackjack(hand, false);

        System.out.println(handHolder + "'S HAND TOTAL: " + sum);
    }
    /**
     * A simple introduction that is displayed to the user upon program execution
     */
    public static void introduction() {
        System.out.println("\nWelcome to the game of Blackjack!");
        System.out.println("You will play against the dealer.");
        System.out.println("You will both draw from the shoe.");
        System.out.println("The shoe can have any number of decks.");
        System.out.println("The shoe is shuffled.\n");
    }
    /**
     * Returns boolean-type value that determines if the player wants to
     * play another game.
     * @return boolean-type value representing the choice for another game
     */
    public static boolean isNotDone() {
        Scanner sc = new Scanner(System.in);
        String reply = "";
        while (!reply.toUpperCase().equals("Y") && !reply.toUpperCase().equals("N")) {
            System.out.print("\nDo you want to play again? (Y/N): ");
            reply = sc.nextLine();
        }
        if (reply.toUpperCase().equals("Y")) {
            return false;
        } else { // Due to boolean conditions in previous while-loop header, if answer wasn't Y, it must be N
            return true;
        }
    }
    /**
     * Returns int-type value that represents the number of desired decks in the shoe.
     * @return int-type value that represents the number of desired decks in the shoe.
     */
    public static int getShoeSize() {
        System.out.print("\nHow many decks would you like to have in the shoe?: ");
        Scanner sc = new Scanner(System.in);
        int shoeSize = 0;

        boolean done = false;
        while (!done) {
            String line = sc.nextLine();
            Scanner lineScan = new Scanner(line);
            if (lineScan.hasNextInt()) {
                done = true;
                shoeSize = lineScan.nextInt();
            } else {
                System.out.print("\nPlease enter an integer number: ");
            }
        }

        return shoeSize;
    }
    /**
     * Handles the initial dealing for the player and dealer hands.
     * Two Card-type instances are drawn from the shoe for each Hand.
     * The first card (0th) in the dealer hand is hard-coded to be the "hole" card.
     * @param shoe the reference to the shoe which contains the undealt playing cards
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     */
    public static void initialDeal(Deck shoe, Player_Hand playerHand, Blackjack_Dealer dealerHand) {
        for (int i = 1; i <= 2; i++) {
            playerHand.addCard(shoe.drawCard(false));
            dealerHand.addCard(shoe.drawCard(false));
        }
        dealerHand.getCard(0).setRevealed(false);
    }
    /**
     * Calculates, populates, and returns an ArrayList<String> instance which contains the available player choices.
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     * @return an ArrayList<String> instance which contains the available player choices
     */
    public static ArrayList<String> playerChoices(Player_Hand playerHand, Blackjack_Dealer dealerHand) {
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
        if (getCardValueTotalBlackjack(playerHand, false) < 21) {
            choices.add(HIT);
            choices.add(STAND);
        }

        return choices;
    }
    /**
     * Parses and returns the player's choice after they are presented with their available player choices.
     * The choice is verified: first if it is int-type; second if it falls within the range of choices.
     * @param choices the reference to the available player choices
     * @return the player's choice
     */
    public static String getPlayerChoice(ArrayList<String> choices) {
        boolean validChoice = false;
        int choiceNum = 0;
        while (!validChoice) {
            boolean done = false;
            while (!done) {
                Scanner sc = new Scanner(System.in);

                System.out.print("Please enter the number which corresponds to your desired move: ");
                String line = sc.nextLine();

                Scanner lineScan = new Scanner(line);

                if (lineScan.hasNextInt()) {
                    done = true;
                    choiceNum = lineScan.nextInt();
                }
            }

            if (choiceNum >= 1 && choiceNum <= choices.size()) {
                validChoice = true;
            }
        }

        String choiceStr = choices.get(choiceNum - 1);
        return choiceStr;
    }
    /**
     * Makes the changes to the passed Player_Hand-type instance based on the choice made by the player.
     * @param shoe the reference to the shoe which contains the undealt playing cards
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     * @param choice the player's choice
     * @param playerHands an ArrayList of type Player_Hand which contains all player hands
     */
    public static void makeChanges(Deck shoe, Player_Hand playerHand, Blackjack_Dealer dealerHand, String choice, ArrayList<Player_Hand> playerHands) {
        if (choice.equals(HIT)) {
            playerHand.addCard(shoe.drawCard(false));
            playerHand.setIsNewHand(false);
        }
        if (choice.equals(STAND)) {
            playerHand.setIsStanding(true);
        }
        
        if (choice.equals(SPLIT)) {
            playerHands.add(new Player_Hand());
            playerHands.get(playerHands.size() - 1).addCard(playerHand.drawCard(false));
            playerHandLoop(shoe, playerHands.get(playerHands.size() - 1), dealerHand, playerHands);
        }
        
        if (choice.equals(INSURANCE)) {
            playerHand.setIsNewHand(false);
        }
    }
    /**
     * Prints the resolution of the hand. It does so by comparing the sums of the player and dealer hands.
     * @param playerHand the reference to the player hand
     * @param dealerHand the reference to the dealer hand
     */
    public static void printHandResolution(Player_Hand playerHand, Blackjack_Dealer dealerHand) {
        String result = "DEFAULT";
        int playerHandValueTotal = getCardValueTotalBlackjack(playerHand, false);
        int dealerHandValueTotal = getCardValueTotalBlackjack(dealerHand, false);

        if (playerHandValueTotal > 21) { result = BUST; }
        else if (playerHandValueTotal == dealerHandValueTotal) { result = PUSH; }
        else if (playerHandValueTotal == 21) { result = BLACKJACK; }
        else if (playerHandValueTotal < dealerHandValueTotal) {
            if (dealerHandValueTotal < 22) { result = LOSS; }
            else if (dealerHandValueTotal > 21 && playerHandValueTotal < 21) { result = WIN; }
        } else if (playerHandValueTotal > dealerHandValueTotal) {
            if (playerHandValueTotal < 21) { result = WIN; }
        }

        System.out.println(result);
    }
    /**
     * Prints the results of the entire game. It first prints the dealer hand, then it prints the player hand/s.
     * @param playerHands an ArrayList of type Player_Hand which contains all player hands
     * @param dealerHand the reference to the dealer hand
     */
    public static void printResults(ArrayList<Player_Hand> playerHands, Blackjack_Dealer dealerHand) {
        System.out.println(SPACER_LONG + "RESULTS" + SPACER_LONG);
        printHand(DEALER, dealerHand);
        for (Player_Hand hand : playerHands) {
            printHand(PLAYER, hand);
            printHandResolution(hand, dealerHand);
        }
    }
    /**
     * 
     */
    public static int getCardValueTotalBlackjack(Deck hand, boolean ignoreRevealed) {
        int sum = hand.getCardValueTotal(ignoreRevealed);
        for (int i = 0; i < hand.size(); i++) {
            if (hand.getCard(i).getRank().equals("Ace") && hand.getCard(i).getRevealed() == true) {
                if (sum + 10 < 22) { sum += 10; }
            }
        }
        return sum;
    }
}

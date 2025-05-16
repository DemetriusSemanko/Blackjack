import card_games.Card;
import card_games.Deck;
import java.util.Scanner;
import java.util.ArrayList;

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

    private static final Scanner KEYBOARD = new Scanner(System.in);

    public static void main(String[] args) {
        introduction();

        int shoeSize = getShoeSize();
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
             * Declaring an ArrayList of type PlayerHand. Initializing it with
             * an instance of PlayerHand.
             */
            ArrayList<PlayerHand> playerHand = new ArrayList<PlayerHand>();
            playerHand.add(new PlayerHand());
            /**
             * Declaring one instance of type BlackjackDealer
             */
            BlackjackDealer dealerHand = new BlackjackDealer();


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

                    if (getCardValueTotalBlackjack(dealerHand) >= 17) {
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
        String reply = "";
        while (!reply.toUpperCase().equals("Y") && !reply.toUpperCase().equals("N")) {
            System.out.print("\nDo you want to play again? (Y/N): ");
            reply = KEYBOARD.nextLine();
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
        int shoeSize = 0;

        boolean done = false;
        while (!done) {
            String line = KEYBOARD.nextLine();
            Scanner lineScan = new Scanner(line);
            if (lineScan.hasNextInt()) {
                shoeSize = lineScan.nextInt();
                lineScan.close();
                done = true;
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
    public static void initialDeal(Deck shoe, PlayerHand playerHand, BlackjackDealer dealerHand) {
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
                System.out.print("Please enter the number which corresponds to your desired move: ");
                String line = KEYBOARD.nextLine();

                Scanner lineScan = new Scanner(line);

                if (lineScan.hasNextInt()) {
                    choiceNum = lineScan.nextInt();
                    lineScan.close();
                    done = true;
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
        }
        if (choice.equals(STAND)) {
            playerHand.setIsStanding(true);
        }
        
        if (choice.equals(SPLIT)) {
            playerHands.add(new PlayerHand());
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
    public static void printHandResolution(PlayerHand playerHand, BlackjackDealer dealerHand) {
        String result = "DEFAULT";
        int playerHandValueTotal = getCardValueTotalBlackjack(playerHand);
        int dealerHandValueTotal = getCardValueTotalBlackjack(dealerHand);

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
     * @param playerHands an ArrayList of type PlayerHand which contains all player hands
     * @param dealerHand the reference to the dealer hand
     */
    public static void printResults(ArrayList<PlayerHand> playerHands, BlackjackDealer dealerHand) {
        System.out.println(SPACER_LONG + "RESULTS" + SPACER_LONG);
        printHand(DEALER, dealerHand);
        for (PlayerHand hand : playerHands) {
            printHand(PLAYER, hand);
            printHandResolution(hand, dealerHand);
        }
    }
    /**
     * A method for calculating the total of the passed hand. This method makes it so
     * that any unreveled cards are not added to the total (to maintain the hidden-ness
     * of the hole card).
     * @param hand a Deck-type object representing the hand that is to be totaled
     * @param ignoreRevealed a flag for if we should count the revealed cards
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

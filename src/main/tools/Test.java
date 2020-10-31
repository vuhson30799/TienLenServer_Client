package main.tools;

import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Tien Len (13)!");
        int userCommand = 0;
        Scanner userInput = new Scanner(System.in);
        while (userCommand != 3) {
            userCommand = intro(userInput);
        }
        userInput.close();
    }


    // Methods
    private static int intro(Scanner userInput) throws IOException {
        int userCommand = 0;

        GameManager game;

        printOptions();
        userCommand = userInput.nextInt();
        if (0 < userCommand && userCommand <= 4) {
            if (userCommand == 1) {
                game = new GameManager();
                game.play(userInput);
            }
            else if (userCommand == 2) {
                viewRules();
            }
            else if (userCommand == 3){
                System.out.println("Thanks for Playing!");
            }
        }
        else {
            System.out.println("Invalid Option!");
        }

        return userCommand;
    }

    private static void printOptions() {
        System.out.println("Enter the number the option you would like to select:");
        System.out.println("[1] Start New Game");
        System.out.println("[2] View Rules");
        System.out.println("[3] Quit Game");
    }

    private static void viewRules() {
        System.out.println("------------------------------------------------------------------------------------------------------");

        System.out.println("*** Note: The rules you are about to read covers a simplified version of the actual Tien Len game."
                + "\nWith that in mind, not all offical rules are present in this game. ***");

        System.out.println("------------------------------------------------------------------------------------------------------");

        System.out.println("~ ~ ~ Tien Len (aka 13 or Killer) Rules ~ ~ ~");
        System.out.println("[Adapted from the rules on the Tien Lien Wikipedia Page]");

        System.out.println();

        System.out.println("Cards:");
        System.out.println("> Standard 52 Playing Cards");
        System.out.println("> Ranking of cards from highest to lowest: 2 A K Q J 10 9 8 7 6 5 4 3");
        System.out.println("> Ranking of suites from highest to lowest: Hearts, Diamonds, Clubs, Spades");

        System.out.println();

        System.out.println("Legal Combinations:");
        System.out.println("> Single: A single played card. Singles can only be defeated by singles that are higher in rank.");
        System.out.println("> Double: A combination of exactly 2 cards of the same rank. A double can only be defeated by a pair of a higher rank than the highest card of the previous pair.");
        System.out.println("> Triple: A combination of exactly 3 cards of the same rank. They can only be defeated by a triple of a higher rank.");
        System.out.println("> Straight: A combination of at least 3 cards that are in numerical sequence. The order of the cards must be in a consecutive order. "
                + "\nThe highest possible ending card in a straight is an A, and the lowest beginning card is the 3.");

        System.out.println();

        System.out.println("Twos and Bombs:");
        System.out.println("> 2's are the strongest card and can only be played as a single.");
        System.out.println("> The only way to beat a 2 is with a Bomb - 3 doubles in numerical sequence. ie. 3-3-4-4-5-5");
        System.out.println("> You can beat a bomb with another bomb of a higher rank than the highest card of the previous pair.");

        System.out.println();

        System.out.println("Play:");
        System.out.println("> Each player is dealt 13 cards.");
        System.out.println("> The person with the 3 of spades will go first.");
        System.out.println("> The direction of play is decided according to the players' preferences");
        System.out.println("> In a turn, a player will decide to play or not (called passing). A player who passes cannot play anymore until the remaining players pass.");
        System.out.println("> If a player chooses to play, they may only play a combination that matches the combination of the current round" +
                " \n(ie. If the player who started the round played a single, then only singles may be played until the next round starts).");
        System.out.println("> When a player plays a combination and everyone else passes, he or she has control and can play any leagl combination.");
        System.out.println("> The first player to shed all 13 cards is the winner.");

        System.out.println("------------------------------------------------------------------------------------------------------");
    }
}

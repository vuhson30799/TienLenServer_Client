package main.tools;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameManager {
    private static final String TURN_TITLE_FORMAT = "*** TURN %d - PLAYER %d's TURN **************************************************";
    private static final String PASS_ROUND_TITLE_FORMAT = "~~ main.Player %s has already passed for this current round ~~";
    Deck deck;
    PlayedPile pile;
    Player p1;
    Player p2;
    Player p3;
    Player p4;
    Set<Player> players;

    public GameManager() {
        System.out.println("Generating New Game...");

        deck = new Deck();
        prepareDeck();
        deck.sortDeck();

        p1 = new Player("1", false);
        p2 = new Player("2", false);
        p3 = new Player("3", false);
        p4 = new Player("4", false);

        pile = new PlayedPile();
    }

    // Methods
    public void play(Scanner userInput) {
        System.out.println("Game Started!\n");
        deck.shuffle();
        deal();

        int whoseTurn = pile.findStarter(p1, p2, p3, p4);
        int turnCounter = 1;


        do {
            pile.newRound(whoseTurn, p1, p2, p3, p4);

            if (whoseTurn == 1) {
                playTurn(p1, pile, turnCounter, whoseTurn, userInput);
            }
            else if (whoseTurn == 2) {
                playTurn(p2, pile, turnCounter, whoseTurn, userInput);
            }
            else if (whoseTurn == 3) {
                playTurn(p3, pile, turnCounter, whoseTurn, userInput);
            }
            else {
                playTurn(p4, pile, turnCounter, whoseTurn, userInput);
            }

            whoseTurn = whoseTurn == 0 ? 1 : (whoseTurn + 1) % 5;
            turnCounter++;

            System.out.println("*** END OF TURN ***************************************************************\n");
        } while (!pile.isGameOver(p1, p2, p3, p4));

    }

    private void playTurn(Player player, PlayedPile pile, int turnCounter, int whoseTurn, Scanner userInput) {
        System.out.println(String.format(TURN_TITLE_FORMAT, turnCounter, whoseTurn));
        if (!player.isPassing()) {
            pile.playerTurn(player, 1, player.checkAI(), turnCounter, userInput);
        }
        else {
            System.out.println(String.format(PASS_ROUND_TITLE_FORMAT, player.getName()));
        }
    }

    private void deal() {
        while (!deck.isEmpty()) {
            p1.drawCard(deck);
            p2.drawCard(deck);
            p3.drawCard(deck);
            p4.drawCard(deck);
        }

        p1.sortHand(true);
        p2.sortHand(true);
        p3.sortHand(true);
        p4.sortHand(true);
    }

    private void prepareDeck() {
        Card temp;
        int size = deck.size();
        int shiftSize = 1;
        int currVal;
        int aceIndex = 0;
        int twoIndex = 1;
        int aceVal = 12;
        int twoVal = 49;

        for (int i = 0; i < size; i++) {
            temp = deck.removeAt(0);
            currVal = temp.getSVal();

            if (i == aceIndex) {
                temp.setSVal(aceVal);
                temp.setRank(14);
                aceIndex += 13;
                aceVal += 12;
                shiftSize += 1;
            }
            else if (i == twoIndex) {
                temp.setSVal(twoVal);
                twoIndex += 13;
                twoVal += 1;
            }
            else {
                temp.setSVal(currVal - shiftSize);
                currVal = temp.getSVal();
            }

            deck.placeTop(temp);
        }
    }
}

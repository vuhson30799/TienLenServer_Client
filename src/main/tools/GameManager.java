package main.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private static final String TURN_TITLE_FORMAT = "*** TURN %d - PLAYER %s's TURN **************************************************";
    private static final String PASS_ROUND_TITLE_FORMAT = "~~ main.Player %s has already passed for this current round ~~";
    Deck deck;
    PlayedPile pile;
    List<Player> players;

    public GameManager() {

        System.out.println("Generating New Game...");

        deck = new Deck();
        prepareDeck();
        deck.sortDeck();

        players = Arrays.asList(new Player("1", false),
                new Player("2", false),
                new Player("3", false),
                new Player("4", false));

        pile = new PlayedPile();
    }

    // Methods
    public void play(Scanner userInput) {
        System.out.println("Game Started!\n");
        deck.shuffle();
        deal();

        int whoseTurn = pile.findStarter(players);
        int turnCounter = 1;


        do {
            pile.newRound(whoseTurn, players);

            playTurn(players.get(whoseTurn), pile, turnCounter, userInput);
            whoseTurn = (whoseTurn + 1) % 4;
            turnCounter++;

            System.out.println("*** END OF TURN ***************************************************************\n");
        } while (!pile.isGameOver(players));

    }

    private void playTurn(Player player, PlayedPile pile, int turnCounter, Scanner userInput) {
        System.out.println(String.format(TURN_TITLE_FORMAT, turnCounter, player.getName()));
        if (!player.isPassing()) {
            pile.playerTurn(player, player.getName(), player.checkAI(), turnCounter, userInput);
        }
        else {
            System.out.println(String.format(PASS_ROUND_TITLE_FORMAT, player.getName()));
        }
    }

    private void deal() {
        while (!deck.isEmpty()) {
            players.forEach(player -> player.drawCard(deck));
        }

        players.forEach(player -> player.sortHand(true));
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

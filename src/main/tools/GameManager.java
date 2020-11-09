package main.tools;

import main.dto.ClientToServerData;
import main.dto.PlayerData;
import main.thread.ServerSendingThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static main.constant.Application.PORT;

public class GameManager implements Serializable {
    private static final long serialVersionUID = 100L;
    private static final String TURN_TITLE_FORMAT = "*** TURN %d - PLAYER %s's TURN **************************************************";
    private static final String PASS_ROUND_TITLE_FORMAT = "~~ main.Player %s has already passed for this current round ~~";
    private transient Deck deck;
    private transient PlayedPile pile;
    private final transient ServerSocket socket;
    private transient List<Socket> outputClients;
    private List<Player> players;
    private int whoseTurn;
    private boolean isGameOver;

    public GameManager() throws IOException {

        System.out.println("Generating New Game...");

        deck = new Deck();
        prepareDeck();
        deck.sortDeck();

        pile = new PlayedPile();
        players = new ArrayList<>();
        socket = new ServerSocket(PORT);
        outputClients = new ArrayList<>();
    }

    public void run() {
        try {
            waitingForPlayersConnect();

            notifyAllPlayersStartingGame();
            System.out.println("Game Started!\n");

            deck.shuffle();
            deal();
            whoseTurn = pile.findStarter(players);

            ServerSendingThread sendingThread = new ServerSendingThread(outputClients, this);
            sendingThread.start();
            this.play(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyAllPlayersStartingGame() {
        outputClients.forEach(outputClient -> {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(outputClient.getOutputStream());
                outputStream.writeObject("Game started!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void waitingForPlayersConnect() {
        try {
            int playerIndex = 0;
            while (players.size() != 4) {
                Socket client = socket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                Object o = inputStream.readObject();
                if (o instanceof PlayerData) {
                    PlayerData player = (PlayerData) o;
                    players.add(new Player(player.getPlayerName(), false));
                } else {
                    throw new IllegalArgumentException("Player Data is invalid");
                }

                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(new PlayerData(playerIndex++));

                outputClients.add(client);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methods
    public void play(ServerSocket socket) {
        int turnCounter = 1;
        try {
            do {
                pile.newRound(whoseTurn, players);

                //received played turn from client
                Socket inputClient = socket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(inputClient.getInputStream());
                ClientToServerData data = getPlayerTurnInfo(inputStream);

                if (handleTurn(players.get(whoseTurn), pile, turnCounter, data)) {
                    //send result to client
                    ServerSendingThread sendingThread = new ServerSendingThread(outputClients, this);
                    sendingThread.start();
                    turnCounter++;
                    System.out.println("*** END OF TURN ***************************************************************\n");
                }
                isGameOver = pile.isGameOver(players);
            } while (!isGameOver);

            Player player = players.stream()
                    .filter(Player::isHandEmpty)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Something went wrong!"));
            ServerSendingThread sendingThread = new ServerSendingThread(outputClients, this, false, String.format("PLAYER %s WINS! GAME OVER!%n", player.getName()));
            sendingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean handleTurn(Player player, PlayedPile pile, int turnCounter, ClientToServerData data) {
        //handle played turn
        try {
            playTurn(player, pile, turnCounter, data);
            do{
                whoseTurn = (whoseTurn + 1) % 4;
            } while (checkPlayerAlreadyPassed(whoseTurn));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ServerSendingThread sendingThread = new ServerSendingThread(Collections.singletonList(outputClients.get(whoseTurn)), this, true, e.getMessage());
            sendingThread.start();
            return false;
        }
    }

    private boolean checkPlayerAlreadyPassed(int whoseTurn) {
        return this.getPlayers().get(whoseTurn).isPassing();
    }

    private void playTurn(Player player, PlayedPile pile, int turnCounter, ClientToServerData data) {
        System.out.println(String.format(TURN_TITLE_FORMAT, turnCounter, player.getName()));
        if (!player.isPassing()) {
            pile.playerTurn(player, player.getName(), player.checkAI(), turnCounter, data);
        }
        else {
            System.out.println(String.format(PASS_ROUND_TITLE_FORMAT, player.getName()));
        }
    }

    private ClientToServerData getPlayerTurnInfo(ObjectInputStream inputStream) {
        try {
            return (ClientToServerData) inputStream.readObject();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not get input from client");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Received invalid data from client");
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

    public PlayedPile getPile() {
        return pile;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}

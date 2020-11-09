package main.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"java:S106","java:S1206"})
public class Player implements Serializable {
    private static final long serialVersionUID = 101L;

    private final List<Card> hand;
    private boolean passTurn;
    //This field means this player is not exist, current game doesn't have full clients.
    private final boolean isAI;
    private final String name;

    public Player(String name, boolean isAI) {
        this.name = name;
        passTurn = false;
        this.isAI = isAI;
        this.hand = new ArrayList<>();
    }

    public boolean isHandEmpty() {
        return hand.isEmpty();
    }

    public boolean isPassing() {
        return passTurn;
    }

    public boolean checkAI() {
        return isAI;
    }

    public String getName() { return this.name;}

    public void setPassing(boolean isPassing) {
        passTurn = isPassing;
    }

    public void drawCard(Deck deck) {
        hand.add(deck.drawCard());
    }

    public void sortHand(boolean byRank) {
        if (byRank) {
            for (Card card : hand) {
                card.setSort(true);
            }
        }
        if (!hand.isEmpty()) {
            Collections.sort(hand);
        }
    }

    public Card checkAt(int i) {
        Card cardAt = null;
        if (i >= 0 && i <= hand.size()) {
            cardAt = hand.get(i);
        }
        return cardAt;
    }

    public int getIndexOf(Card card) {
        return this.hand.indexOf(card);
    }

    public void removeCardAt(int i) {
        hand.remove(i);
    }

    public int handSize() {
        return hand.size();
    }

    public void displayHand() {
        for (int i = 0; i < hand.size(); i++) {
            System.out.print("[" + (i + 1) + "] " + hand.get(i) + ";");
        }
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player player = (Player) o;
            return this.name.equals(player.getName());
        }
        return false;
    }

    public List<Card> getHand() {
        return hand;
    }
}

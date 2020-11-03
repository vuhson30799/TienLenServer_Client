package main.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Player implements Serializable {
    private static final long serialVersionUID = 101L;

    private ArrayList<Card> hand = new ArrayList<>();
    private boolean passTurn;
    private boolean isAI;
    private String name;

    // Constructor
    Player(String name, boolean isAI) {
        this.name = name;
        passTurn = false;
        this.isAI = isAI;
    }

    // Methods
    public boolean isHandEmpty() {
        return hand.isEmpty();
    }

    public boolean isPassing() {
        return passTurn;
    }

    public void setAI(boolean isAI) {
        this.isAI = isAI;
    }

    public boolean checkAI() {
        return isAI;
    }

    public String getName() { return this.name;}
    public void setName(String name) { this.name = name;}

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

    public Card getCardAt(int i) {
        return hand.get(i);
    }

    public int getIndexOf(Card card) {
        return this.hand.indexOf(card);
    }

    public void removeCardAt(int i) {
        hand.remove(i);
    }

    public void emptyHand() {
        hand.clear();
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

    public ArrayList<Card> getHand() {
        return hand;
    }
}

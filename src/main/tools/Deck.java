package main.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;

    Deck() {
        initialDeck();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public void initialDeck() {
        cards = new ArrayList<>();
        for(int suite = 0; suite < 4; suite++) {
            for (int rank = 0; rank < 13; rank++) {
                cards.add(new Card(suite + 1, rank + 1, (suite * 13) + (rank + 1)));
            }
        }
    }

    public void sortDeck() {
        if (!cards.isEmpty()) {
            Collections.sort(cards);
        }
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1);
        }
        return null;
    }

    public void placeTop(Card spCard) {
        cards.add(spCard);
    }

    public void insertCard(int index, Card spCard) {
        cards.add(index, spCard);
    }

    public Card removeAt(int i) {
        return cards.remove(i);
    }

    public void cutDeck() {
        if (cards.size() > 1) {
            int midSize = cards.size()/2;
            for (int index = 0; index < midSize; index++) {
                cards.add(cards.remove(0));
            }
        }
    }

    public void shuffle() {
        hinduShuffle(3);
        rippleShuffle(2);
        hinduShuffle(1);
        cutDeck();
    }

    public void rippleShuffle(int reps) {
        int size = cards.size();
        int midSize = cards.size()/2;
        ArrayList<Card> LHalf = new ArrayList<Card>(midSize);
        ArrayList<Card> RHalf = new ArrayList<Card>(size - midSize);
        Random randomizer = new Random();

        for (int num = 0; num < reps; num++) {

            for (int index = 0; index < size; index++) {
                if (index < midSize) {
                    LHalf.add(cards.remove(0));
                }
                else {
                    RHalf.add(cards.remove(0));
                }
            }

            while (cards.size() != size) {
                if ((randomizer.nextInt() % 2) == 0) {
                    if (!LHalf.isEmpty()) {
                        cards.add(LHalf.remove(0));
                    }
                    else {
                        while (!RHalf.isEmpty()) {
                            cards.add(RHalf.remove(0));
                        }
                        break;
                    }
                }
                else {
                    if (!RHalf.isEmpty()) {
                        cards.add(RHalf.remove(0));
                    }
                    else {
                        while (!LHalf.isEmpty()) {
                            cards.add(LHalf.remove(0));
                        }
                        break;
                    }
                }
            }
        }
    }

    public void hinduShuffle(int reps) {
        Random randomizer = new Random();
        int randNum;
        int lBound, uBound;
        int range;
        for (int num = 0; num < reps; num++) {
            lBound = 0;
            uBound = 0;
            range = 0;
            do {
                randNum = (randomizer.nextInt(4)) + 2;
                uBound = cards.size() - randNum;

                for (int index = lBound; index < uBound; index++) {
                    cards.add(cards.remove(lBound));
                }

                lBound += randNum;
                range = uBound - lBound;

            } while (range > 10);
        }
    }

    public void printSize() {
        System.out.println("Size of main.Deck is: " + cards.size());
    }

    public void printDeck() {
        printSize();
        for (Card card : cards) {
            System.out.println(card.toString());
        }
        System.out.println();
    }

}

package main.tools;

import main.utils.DefaultUtils;
import main.utils.Utils;

import static main.constant.CardError.*;

public class Card implements Comparable<Card> {
    private int suite;
    private int primaryRank;
    private int secondaryValue;
    private boolean sortByRank;

    // Constructors
    Card() {
        suite = 0;
        primaryRank = 0;
        secondaryValue = 0;
        sortByRank = false;
    }

    Card(int newSuite, int newPRank) {
        if (newSuite < 1 || newSuite > 4) {
            throw new IllegalArgumentException(SUITE_ERROR);
        }

        if (newPRank < 1 || newPRank > 13) {
            throw new IllegalArgumentException(RANK_ERROR);
        }

        suite = newSuite;
        primaryRank = newPRank;
        secondaryValue = 0;
        sortByRank = false;
    }

    Card(int newSuite, int newPRank, int newSVal) {
        if (newSuite < 1 || newSuite > 4) {
            throw new IllegalArgumentException(SUITE_ERROR);
        }

        if (newPRank < 1 || newPRank > 13) {
            throw new IllegalArgumentException(RANK_ERROR);
        }

        if (newSVal < 1) {
            throw new IllegalArgumentException(SECONDARY_VALUE_ERROR);
        }

        suite = newSuite;
        primaryRank = newPRank;
        secondaryValue = newSVal;
        sortByRank = false;
    }

    // Methods
    public int getSuite() {
        return this.suite;
    }

    public int getRank() {
        return this.primaryRank;
    }

    public int getSVal() {
        return this.secondaryValue;
    }

    public int getCardIndex() {
        return ((this.suite-1) * 13) + this.getRank();
    }

    public void setSuite(int newSuite) {
        if (newSuite < 1 || newSuite > 4) {
            throw new IllegalArgumentException(SUITE_ERROR);
        }
        else {
            suite = newSuite;
        }
    }

    public void setRank(int newPRank) {
        if (newPRank < 1 || newPRank > 14) {
            throw new IllegalArgumentException(RANK_ERROR);
        }
        else {
            primaryRank = newPRank;
        }
    }

    public void setSVal(int newSVal) {
        if (newSVal > 0) {
            secondaryValue = newSVal;
        }
    }

    public void setSort(boolean sortRank) {
        sortByRank = sortRank;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o == this) {
            equal = true;
        } else if (o instanceof Card) {
            Card inqObj = (Card) o;
            if (this.suite == inqObj.suite && this.primaryRank == inqObj.primaryRank) {
                equal = true;
            }
        }
        return equal;
    }

    @Override
    public int compareTo(Card compareCard) {

        int sVal1 = this.getSVal();
        int sVal2 = compareCard.getSVal();

        if (sortByRank) {
            sVal1 = this.getRank();
            sVal2 = compareCard.getRank();

            if ((sVal1 == 1 && sVal2 != 1) || (sVal1 != 1 && sVal2 == 1)
                    || (sVal1 == 2 && sVal2 != 2) || (sVal1 != 2 && sVal2 == 2)) {
                sVal1 = this.getSVal();
                sVal2 = compareCard.getSVal();
            } else if (sVal1 == sVal2) {
                sVal1 = this.getSuite();
                sVal2 = compareCard.getSuite();
            }
        }

        return sVal1 - sVal2;
    }

    @Override
    public String toString() {
        Utils utils = new DefaultUtils();
        return String.format("%s of %s",
                utils.resolveRank(primaryRank),
                utils.resolveSuite(suite));
    }
}

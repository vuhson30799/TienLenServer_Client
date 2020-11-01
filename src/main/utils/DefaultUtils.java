package main.utils;

import static main.constant.Rank.*;
import static main.constant.Suite.*;

public class DefaultUtils implements Utils {
    private static final String IMAGE_PATH_FORMAT = "./resources/images/%s/%s.png";

    @Override
    public String generateImagePathFromCardValue(String suite, String rank) {
        return String.format(IMAGE_PATH_FORMAT, suite, rank);
    }

    @Override
    public String resolveSuite(int suite) {
        switch (suite) {
            case 1:
                return SPADES;
            case 2:
                return CLUBS;
            case 3:
                return DIAMONDS;
            case 4:
                return HEARTS;
            default:
                throw new IllegalArgumentException("There no available suite for this value");
        }
    }

    @Override
    public String resolveRank(int rank) {
        switch (rank) {
            case 1:
                return ACE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            case 8:
                return EIGHT;
            case 9:
                return NINE;
            case 10:
                return TEN;
            case 11:
                return JACK;
            case 12:
                return QUEEN;
            case 13:
                return KING;
            default:
                throw new IllegalArgumentException("There is no available rank for this value");
        }
    }
}

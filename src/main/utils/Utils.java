package main.utils;

public interface Utils {
    /**
     * This method will generate image path from suite and rank of a card.
     * @param suite There will be 4 values for suite.
     * @param rank There will be 13 values for card.
     * @return Image path of that card.
     */
    String generateImagePathFromCardValue(String suite, String rank);

    String resolveSuite(int suite);

    String resolveRank(int rank);
}

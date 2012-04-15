package schillingerapp;

/**
 * Helper object that takes in a BinaryPattern and compares it to a
 * schillingerPattern at all possible indices.
 * 
 * @author anparks and prhayman
 */
public class FuzzyMatcher {

    /* global variable needed to keep track of which pattern was inputted */
    int patternIndex;

    /**
     * Finds the number of hits between a schillingerPattern and a testArray 
     * while taking into account the offset.  It does this by shifting the 
     * longer pattern over one space, effectively ignoring the first element
     * until the two patterns are of equal length. OffsetMatch returns a 
     * double[] filled with the percentage of matches where the array index 
     * corresponds to the offset at which it was found. Additionally, the match
     * percentage value is the percentage of similarity, plus the ratio of their lengths.
     * Thus, patterns that are closer in length to the original pattern will have
     * a higher match value.
     * 
     * @param binPattern
     * @param schillingerPattern
     * @param patternIndex
     * @return A double[] filled with the percentage of matches.
     */
    public double[] offsetMatch(BinaryPattern binPattern, int[] schillingerPattern, int patternIndex) {

        this.patternIndex = patternIndex;
        int[] longer;
        int[] shorter;
        int[] testArray = binPattern.getBinaryPattern();

        // For length and shifting purposes, it is important to determine which
        // of the two arrays is the longer one.
        if (testArray.length > schillingerPattern.length) {
            longer = testArray;
            shorter = schillingerPattern;
        } else {
            longer = schillingerPattern;
            shorter = testArray;
        }

        // Creates a new array to store the hit values found for each offset 
        double[] offsetArray = new double[longer.length - shorter.length + 1];

        // Cycles through each offset to test for hits 
        for (int offset = 0; offset < (longer.length - shorter.length + 1); offset++) {
            offsetArray[offset] = (((double) findHits(longer, shorter, offset)) /
                    shorter.length + ((double) shorter.length / (double) longer.length)) / 2;
        // weighted Resuls
        }

        return offsetArray;

    }

    /**
     * Helper Method: looks at each item in the array and increments if
     * equal, thus finding the number of hits or "Hit Count".
     *
     * @param longer The longer of the two patterns (determined by offsetMatch)
     * @param shorter The shorter of the two patterns (determined by offsetMatch)
     * @param offset
     * @return The number of "hits" found between the two patterns.
     */
    public int findHits(int[] longer, int[] shorter, int offset) {
        int hitCount = 0;

        for (int i = 0; i < shorter.length; i++) {
            if (longer[i + offset] == shorter[i]) {
                hitCount++;
            }
        }

        return hitCount;
    }

    /**
     * Helper Method: finds the maximum percentage of hits in the hitArray.
     * Returns a Match object, containing the maximum value and the index
     * referencing the offset at which it was found.
     * 
     * @param matchValues
     * @return A Match object containing the max value and the offset index.
     */
    public Match findMax(double[] matchValues) {

        double maximum = matchValues[0];
        int offset = 0;

        // start with the first value
        for (int i = 1; i < matchValues.length; i++) {
            if (matchValues[i] > maximum) {
                maximum = matchValues[i];   // new maximum
                offset = i;
            }
        }

        Match match = new Match(maximum, offset, patternIndex);

        return match;
    }
}
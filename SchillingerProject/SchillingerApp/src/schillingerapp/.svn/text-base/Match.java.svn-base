package schillingerapp;

import java.text.DecimalFormat;

/* -------------------------------------------------------------------------
 * The Match class is a Data Object with three fields.
 *
 * -matchValue: the percentage of similarity between two int arrays added with
 *              the ratio of the lengths of the SchillingerPattern and inputed
 *              string. Patterns that have closer lengths to the original will
 *              subsequently have higher matchValues.
 *
 * -offset: this variable is strictly an indexing variable.  It records at
 *          which offset the highest percentage mach was found.
 *
 * -patternIndex: This keeps track of which pattern was compared to the user
 *                input.  Needed when the HitListDB is sorted.
 * -------------------------------------------------------------------------
 *
 * @author prhayman
 */
public class Match {

    /* Instantiates the two fields of a Match object */
    public double matchValue;
    public int offset;
    public int patternIndex;

    /**
     * Constructor sets all three fields of a Match Object
     *
     * @param matchValue
     * @param offset
     * @param patternIndex
     */
    public Match(double matchValue, int offset, int patternIndex) {
        this.matchValue = matchValue;
        this.offset = offset;
        this.patternIndex = patternIndex;
    }
    /**
     * Returns the matchValue
     * 
     * @return matchValue
     */
    public double getMatchValue() {
        return matchValue;
    }

    /**
     * Returns the offset
     *
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the patternIndex
     * 
     * @return patternIndex
     */
    public int getPatternIndex() {
        return patternIndex;
    }

    /**
     * Sets the MatchValue
     *
     * @param newMatch
     */
    public void setMatchValue(double newMatch) {
        this.matchValue = newMatch;
    }

    /**
     * Sets the offset
     *
     * @param offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Sets the patternIndex
     * @param patternIndex
     */
    public void setPatternIndex(int patternIndex) {
        this.patternIndex = patternIndex;
    }

    /**
     * Returns the Match as a string; fields are separated by commas
     * and percentages are rounded to two decimal places.
     * @return string
     */
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        String string = "";
        if (getOffset() > 9) {
            string += "" + df.format(getMatchValue()*100) + "%, Offset:" + getOffset() + " Pattern:" + getPatternIndex() + "  ";
        } else {
            string += "" + df.format(getMatchValue()*100) + "%, Offset:" + getOffset() + "  Pattern:" + getPatternIndex() + " ";
        }
        return string;
    }
}
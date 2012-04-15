/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schillingerapp;

/**
 *
 * @author jones
 */
public class RhythmPattern {

    /* Fields in a RhythmPattern object.  

    - Description is the identifier for the pattern, such as 5:3 R where 5:3
    are the generators and R is resultant
    - pattern is an int[] of 1's and 0's, representing the durational rhythm
    - patternLength is the length of the pattern int[] */
    private String description;
    private int[] pattern;
    private int patternLength;

    public RhythmPattern(String desc, int length, int[] pat) {
        this.description = desc;
        this.patternLength = length;
        this.pattern = pat;
    }

    /**
     * getter function
     * @return pattern
     */
    public int[] getPattern() {
        return pattern;
    }

    /**
     * getter function
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter function- sets description to input
     * @param desc
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * setter function- sets pattern to input
     * @param desc
     */
    public void setPattern(int[] pat) {
        this.pattern = pat;
    }

    /**
     * setter function- sets patternLength to input
     * @param desc
     */
    public void setPatternLength(int len) {
        this.patternLength = len;
    }

    /**
     * getter function
     * @return patternLength
     */
    public int getPatternLength() {
        return patternLength;
    }

    /**
     * puts the RhythmPattern in string form
     * @return
     */
    public String toString() {
        String string = "";
        string += this.description + ", " + this.patternLength + ", ";

        for (int i = 0; i < this.pattern.length; i++) {
            string += this.pattern[i];
        }

        return string;
    }

    /**
     * getter function returns pattern in String form
     * @return string
     */
    public String getPatternToString() {
        String string = "";
        for (int i = 0; i < this.pattern.length; i++) {
            string += pattern[i];
        }
        return string;
    }
}
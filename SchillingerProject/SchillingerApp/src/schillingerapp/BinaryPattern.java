package schillingerapp;

/**
 * Represents a BinaryPattern Object.  When looking at rhythms
 * within music, we extract the pattern, and represent it in and int[] of 1's
 * and 0's. The 1's represent "note events", either a note or a rest, and the
 * 0's are the duration which that note is held.  Additionally, we needed
 * another field to keep track of what beat each 1 and 0 stands for.
 *
 * Example:
 * 1,1,1 (.25) represents three quarternotes in succession.
 * ----------------------------------------------------------------------------
 * @author prhayman
 */
public class BinaryPattern {

    /* These values are both necessary to create a BinaryPattern object.
     * The first value, binaryPattern, is an int[] containing the relative
     * rhythmic information, while the commonDenominator value is a double
     * value required to specify the precise "meter" of the binaryPattern;
     * that is, to define what rhythmic value (eigth, quarter, etc.) each
     * element in the binaryPattern array represents.
     */
    int[] binaryPattern;
    double commonDenominator;
    
    /**
     * Constructor initializing both fields of a BinaryPattern
     * 
     * @param binaryPattern
     * @param commonDenominator
     */
    public BinaryPattern(int[] binaryPattern, double commonDenominator){
        this.binaryPattern = binaryPattern;
        this.commonDenominator = commonDenominator;
    }
    
    /**
     * Returns the commonDenominator double value.
     */
    public double getCommonDenominator(){
        return commonDenominator;
    }
    
    /**
     * Returns the binaryPattern int[] value.
     */
    public int[] getBinaryPattern(){
        return binaryPattern;
    }
    
    /**
     * Assigns the commonDenominator double value.
     * @param newValue The value to which common denominator will be set.
     */
    public void setCommonDenominator(double newValue){
        commonDenominator = newValue;
    }
    
    /**
     * Assigns the binaryPattern int[] value.
     *
     * @param newPattern The int[] to which the binaryPattern field will be set.
     */
    public void setBinaryPattern(int[] newPattern){
        binaryPattern = newPattern;
    }
    
    /**
     * Produces a string representation of the two BinaryPattern components in
     * the format: binaryPattern (commonDenominator). For example, if a Binary 
     * Pattern object were created given parameters { 1, 0, 1, 1, 1, 0 } and
     * 0.125, binPat.toString() would return: "101110 (0.125)"
     * 
     * @return The string representation of a BinaryPattern.
     */
    public String toString(){
        String string = "";
        for( int i = 0; i < binaryPattern.length; i++){
            string += binaryPattern[i];
        }
        string += " (" + commonDenominator + ")";
        
        return string;
    }
}

package schillingerapp;


//for floor() and ceil() methods
import java.lang.Math.*;
import java.util.ArrayList;

/**
 * HitListDB represents a 2-D array of Match objects.  Each row is unique
 * to a SchillingerPattern read out of a database.  Additionally, each column
 * represents the results of a specific test using the SchillingerPatterns.
 * The match objects contained are the maximum percentages of similarity found
 * by using the FuzzyMatcher Class.
 *
 * AS OF NOW (SUBJECT TO CHANGE):
 * 3 COLUMNS
 * -The first column is a comparison between the inputted BinaryPattern and
 *  the whole Schillinger Pattern.
 *
 * -The second column is a comparison between the inputted BinaryPattern and
 *  the first half of the Schillinger Pattern.
 *
 * -The third column is a comparison between the inputted BinaryPattern and
 *  the last half of the Schillinger Pattern.
 * ----------------------------------------------------------------------------
 * @author prhayman
 */
public class HitListDB {

    // Global Variable	
    Match[][] hitListMatrix;       // Initializes the dimensional variables for the HitListDB
    private final int COLUMNS = 3;
    /* Columns is of type final because the program is designed such that each
     * column of the hitListDB has a unique property and is predefined.
     */
    int ROWS; // read in from a SchillingerDB
    int patternIndex;
    BinaryPattern input;

    /**
     * Constructor: Takes in only a BinaryPattern from the user input and uses
     * a local coppy of the SchillingerDatabase to run comparisons
     *
     * @param binPattern
     */
    public HitListDB(BinaryPattern binPattern) {
        this.input = binPattern;
        SchillingerDB database = new SchillingerDB();
    
        ROWS = database.countLinesInDB();
        hitListMatrix = new Match[ROWS][COLUMNS];
        FuzzyMatcher fuzzyMatcher = new FuzzyMatcher();

        // Initializes each point in the Matrix
        for (int i = 0; i < ROWS; i++) {
            patternIndex = i;

            // reads the 'i'th line in the database, extracting the pattern with
            // index 'i'.
            int[] pattern = database.getPatternFromDB(i);

            // runs through the predetermined unique columns to run the
            // appropriate tests
            for (int j = 0; j < COLUMNS; j++) {

                if (j == 0) { //whole schillinger Pattern

                    // Sends to findMax the hitArray returned by offsetMatch in
                    // FuzzyMatcher
                    hitListMatrix[i][j] =
                            fuzzyMatcher.findMax(fuzzyMatcher.offsetMatch(binPattern, pattern, i));
                } else if (j == 1) {// firstHalf of schillinger Pattern
                    int[] firstHalf = getFirstHalfPattern(pattern);

                    hitListMatrix[i][j] = fuzzyMatcher.findMax(fuzzyMatcher.offsetMatch(binPattern, firstHalf, i));
                } else {// last column, lastHalf of pattern
                    int[] lastHalf = getLastHalfPattern(pattern);

                    hitListMatrix[i][j] = fuzzyMatcher.findMax(fuzzyMatcher.offsetMatch(binPattern, lastHalf, i));
                }
            }
        }

    /*        for (int i = 0; i < lines; i++) {
    for (int j = 0; j < COLUMNS; j++) {
    hitListMatrix[i][j].setMatchValue(hitListMatrix[i][j].getMatchValue() / 2);
    }

    }*/

    }

    /**
     * Helper method to initials the first half of a RhythmPattern with odd
     * middle objects included in both halves.
     *
     * @param pattern
     * @return
     */
    public int[] getFirstHalfPattern(int[] pattern) {
        double originalLength = pattern.length;
        int[] firstHalf = new int[(int) Math.ceil(originalLength / 2)];

        for (int i = 0; i < firstHalf.length; i++) {
            firstHalf[i] = pattern[i];
        }

        return firstHalf;
    }

    /**
     * Helper method to initials the last half of a RhythmPattern with odd
     * middle objects included in both halves

     * @param pattern
     * @return
     */
    public int[] getLastHalfPattern(int[] pattern) {
        double originalLength = pattern.length;
        int[] lastHalf = new int[(int) Math.ceil(originalLength / 2)];

        for (int i = 0; i < lastHalf.length; i++) {
            lastHalf[i] = pattern[i + (int) Math.floor(originalLength / 2)];
        }

        return lastHalf;
    }

    /* Helper method to find the number of lines
     *  (number of RhythmPatterns tested) */
    public int getLines() {
        return ROWS;
    }

    /* Helper method fo find the number of columns
     *  (number of unique tests */
    public int getColumns() {
        return COLUMNS;
    }

    /**
     * Prints out the MatchObject that represent the maximum hit percentages
     * for each schillingerPattern and the inputted testArray.
     */
    public void printHitListDB() {

        System.out.print("Full Pattern Match:\t\t Left Half Match: \t\t   Right Half Match");
        for (int i = 0; i < ROWS; i++) {
            System.out.println();
            for (int j = 0; j < COLUMNS; j++) {
                if (hitListMatrix[i][j].getPatternIndex() > 9 &&
                        hitListMatrix[i][j].getOffset() > 9) {
                    System.out.print(hitListMatrix[i][j] + "\t");
                } else if (hitListMatrix[i][j].getPatternIndex() > 9 &&
                        hitListMatrix[i][j].getOffset() <= 9) {
                    System.out.print(hitListMatrix[i][j] + " \t");
                } else if (hitListMatrix[i][j].getPatternIndex() <= 9 &&
                        hitListMatrix[i][j].getOffset() > 9) {
                    System.out.print(hitListMatrix[i][j] + " \t");
                } else {
                    System.out.print(hitListMatrix[i][j] + "  \t");
                }
            }
        }
        System.out.println();
    }

    /* This method is used to sort the hitListMatrix such that each column is
     * still unique to the previously designed tests, only with the match values
     * sorted in order from highest to lowest matchValues. */
    public void sortDB() {
        // Local Variables
        Match[] firstColumn = new Match[ROWS];
        Match[] secondColumn = new Match[ROWS];
        Match[] thirdColumn = new Match[ROWS];

        /* initializes each Match[] such that each arry corresponds
         * to the columns */
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (j == 0) {
                    firstColumn[i] = hitListMatrix[i][j];
                } else if (j == 1) {
                    secondColumn[i] = hitListMatrix[i][j];
                } else {
                    thirdColumn[i] = hitListMatrix[i][j];
                }
            }
        }

        //Sorts each array using bubbleSort
        this.bubbleSort1(firstColumn);
        this.bubbleSort1(secondColumn);
        this.bubbleSort1(thirdColumn);

        // Rewrites the database in its sorted state
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (j == 0) {
                    hitListMatrix[i][j] = firstColumn[i];
                } else if (j == 1) {
                    hitListMatrix[i][j] = secondColumn[i];
                } else {
                    hitListMatrix[i][j] = thirdColumn[i];
                }
            }
        }
    }

    /**
     * Helper Method used to sort an array via BubbleSort techniques
     * @param array
     */
    public void bubbleSort1(Match[] array) {
        int length = array.length;
        for (int pass = 1; pass < length; pass++) {  // count how many times
            // This next loop becomes shorter and shorter
            for (int i = 0; i < length - pass; i++) {
                if (array[i].getMatchValue() < array[i + 1].getMatchValue()) {
                    // exchange elements
                    Match temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
        }
    }

    /**
     * Get the topMAtches integer and prints out that many sequences that are
     * equivalent to the input integer
     * @param topMatches
     */
    public void printTopMatches(int topMatches) {

        SchillingerDB database = new SchillingerDB();


        for (int j = 0; j < topMatches; j++) {
            int[] nthMatch = this.getNthMatch(j + 1);
            int row = nthMatch[0];
            int column = nthMatch[1];
            if (input.getBinaryPattern().length >= database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()).length) {
                for (int i = 0; i < input.getBinaryPattern().length; i++) {
                    System.out.print(input.getBinaryPattern()[i]);
                }
                System.out.println();


                for (int i = 0; i < hitListMatrix[row][column].getOffset(); i++) {
                    System.out.print(" ");
                }

                if (column == 0) {
                    for (int i = 0; i < database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()).length; i++) {
                        System.out.println(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())[i]);
                    }
                } else if (column == 1) {
                    for (int i = 0; i < getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        System.out.println(getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i]);
                    }
                } else { // column == 2
                    for (int i = 0; i < getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        System.out.println(getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i]);
                    }
                }
                System.out.println(" " + hitListMatrix[row][column]);
                System.out.println();

            } else {
                for (int i = 0; i < hitListMatrix[row][column].getOffset(); i++) {
                    System.out.print(" ");
                }

                for (int i = 0; i < input.getBinaryPattern().length; i++) {
                    System.out.print(input.getBinaryPattern()[i]);
                }
                System.out.println();

                if (column == 0) {
                    for (int i = 0; i < database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()).length; i++) {
                        System.out.println(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())[i]);
                    }
                } else if (column == 1) {
                    for (int i = 0; i < getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        System.out.println(getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i]);
                    }
                } else { // column == 2
                    for (int i = 0; i < getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        System.out.println(getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i]);
                    }
                }
                    System.out.println(" " + hitListMatrix[row][column]);
                    System.out.println();

            }
        }
    }



    public String topMatchesString(int topMatches) {
        String string = "";

        SchillingerDB database = new SchillingerDB();

        for (int j = 0; j < topMatches; j++) {
            int[] nthMatch = this.getNthMatch(j + 1);
            int row = nthMatch[0];
            int column = nthMatch[1];
            //Prints the BinaryPattern of the input first if it is shorter.
            if (input.getBinaryPattern().length >= database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()).length) {
                for (int i = 0; i < input.getBinaryPattern().length; i++) {
                    string += input.getBinaryPattern()[i];
                }

                string += "\n";

                //Prints a space for each index of the offset to line them up
                for (int i = 0; i < hitListMatrix[row][column].getOffset(); i++) {
                    string += " ";
                }

                //Prints the database binaryPattern
                if (column == 0) {
                    for (int i = 0; i < database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()).length; i++) {
                        string += database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())[i];
                    }
                } else if (column == 1) {
                    for (int i = 0; i < getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        string += getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i];
                    }
                } else { // column == 2
                    for (int i = 0; i < getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        string += getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i];
                    }
                }
                string += " " + hitListMatrix[row][column] + "\n";
            //string += "\n";

            } else {

                for (int i = 0; i < hitListMatrix[row][column].getOffset(); i++) {
                    string += " ";
                }

                for (int i = 0; i < input.getBinaryPattern().length; i++) {
                    string += input.getBinaryPattern()[i];
                }
                string += "\n";

                //Prints the database binaryPattern
                if (column == 0) {
                    for (int i = 0; i < database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()).length; i++) {
                        string += database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())[i];
                    }
                } else if (column == 1) {
                    for (int i = 0; i < getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        string += getFirstHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i];
                    }
                } else { // column == 2
                    for (int i = 0; i < getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex())).length; i++) {
                        string += getLastHalfPattern(database.getPatternFromDB(hitListMatrix[row][column].getPatternIndex()))[i];
                    }
                }
                string += " " + hitListMatrix[row][column] + "\n";
            //string += "\n";
            }
        }

        return string;

    }

    public int findTopMatchColumn() {
        Match max = hitListMatrix[0][0];
        int column = 0;

        for (int i = 0; i < COLUMNS; i++) {
            if (hitListMatrix[0][i].getMatchValue() > max.getMatchValue()) {
                max = hitListMatrix[0][i];
                column = i;
            }
        }

        return column;
    }

    public Match findTopMatch() {
        Match max = hitListMatrix[0][0];

        for (int i = 0; i < COLUMNS; i++) {
            if (hitListMatrix[0][i].getMatchValue() > max.getMatchValue()) {
                max = hitListMatrix[0][i];
            }
        }

        return max;
    }    
    
    /**
     * Uses the hitlist and a given integer n. Finds the nth greatest number in the
     * hitlist
     * @param n
     * @return solution
     */
    public int[] getNthMatch(int n) {

        //Declared variables
        int[] solution = new int[2];
        boolean hasFoundNthMatch = false;
        int numGreater = 0;
        int numEqual = 0;
        int columns = 0;
        int rows = 0;
        solution[0] = rows;
        solution[1] = columns;

        //while flag is not set
        while (!hasFoundNthMatch) {
            Match nthMatch = hitListMatrix[solution[0]][solution[1]];//the value we are testing
            //reset variables to 0
            numGreater = 0;
            numEqual = 0;
            columns = 0;
            rows = 0;
            //if n is the nth number or the n-1th highest number
            //e.g if n =3 then if n is the third highest number
            //or the highest number
            while (numGreater < n) {
                //if at end of iteration
                if (columns == 2 && rows >= n) {
                    //check if this is the correct number
                    if (numGreater == n - 1 || numGreater + numEqual >= n) {
                        hasFoundNthMatch = true;
                    }
                    numGreater = n + 1;//increase temp to end loop
                } else if (rows == (n - 1) && columns != 2) {
                    if (nthMatch.getMatchValue() < hitListMatrix[rows][columns].getMatchValue()) {
                        numGreater++;//if number is bigger than nthMAtch, increase temp
                    } else if (nthMatch.getMatchValue() == hitListMatrix[rows][columns].getMatchValue()) {
                        numEqual++;
                    //if equal, increase numEqual(accounts for equalities)
                    //in figuring out the nth highest number
                    }
                    //if loop hasn't ended increase the iteration
                    if (numGreater < n) {
                        columns++;
                        rows = 0;
                    }
                } else {
                    if (nthMatch.getMatchValue() < hitListMatrix[rows][columns].getMatchValue()) {
                        numGreater++;//if number is bigger than nthMAtch, increase temp
                    } else if (nthMatch.getMatchValue() == hitListMatrix[rows][columns].getMatchValue()) {
                        numEqual++;
                    //if equal, increase numEqual(accounts for equalities)
                    //in figuring out the nth highest number
                    }
                    //if loop hasn't ended increase the iteration
                    if (numGreater < n) {
                        rows++;
                    }
                }
            }

            //if the flag isn't set, increase the iteration for nthMatch
            if (!hasFoundNthMatch) {
                if (solution[0] == n - 1) {
                    solution[1]++;
                    solution[0] = 0;
                } else {
                    solution[0]++;
                }
            }
        }

        return solution;
    }
    
    public int[] getNthMatchNew(int n) {
    	
    	/* List of the column index of each match. For example, match n
    	 * comes from column topMatchesColumnList.get(n-1).
    	 */
    	ArrayList<Integer> topMatchesColumnList = new ArrayList<Integer>();
    	ArrayList<Integer> topMatchesRowList = new ArrayList<Integer>();
    	// Next element to consider in 1st, 2nd, or 3rd column of hitListMatrix
    	int iFirstCol = 0;
    	int iSecondCol = 0;
    	int iThirdCol = 0;
    	// The number of TopMatches calculated
    	int numTopMatches = 0;
    	
    	// Runs until the end of any of the three columns is reached
    	while (iFirstCol < ROWS && iSecondCol < ROWS && iThirdCol < ROWS) {
    		
    		/* If the MatchValue in the first column at index iFirstCol is
    		 * greater than the values in the second and third columns at 
    		 * indices iSecondCol and iThirdCol, respectively, then the first
    		 * column Match object is added to the topMatchList ArrayList,
    		 * and the first column index (iFirstCol) is incremented. The 
    		 * same test is performed with the second and third columns.
    		 */
    		if (hitListMatrix[iFirstCol][0].getMatchValue() > 
    				hitListMatrix[iSecondCol][1].getMatchValue() &&
    			hitListMatrix[iFirstCol][0].getMatchValue() > 
    				hitListMatrix[iThirdCol][2].getMatchValue() ) {
    			// MatchValue in first column is highest
    			
    			topMatchesColumnList.add(1);
    			topMatchesRowList.add(iFirstCol + 1);
    			iFirstCol++;
    			
    		} else if (hitListMatrix[iSecondCol][1].getMatchValue() > 
    				   	   hitListMatrix[iFirstCol][0].getMatchValue() &&
    				   hitListMatrix[iSecondCol][1].getMatchValue() > 
    				   	   hitListMatrix[iThirdCol][2].getMatchValue() ) {
    			// MatchValue in second column is highest 
    			
    			topMatchesColumnList.add(2);
    			topMatchesRowList.add(iSecondCol + 1);
    			iSecondCol++;
    			
    		} else { // MatchValue in third column is highest
    			
    			topMatchesColumnList.add(3);
    			topMatchesRowList.add(iThirdCol + 1);
    			iThirdCol++;
    		}

    		numTopMatches++;
    	}

    	int[] nthMatchRowAndColumn = { topMatchesRowList.get(n-1), topMatchesColumnList.get(n-1) };
    	
//    	System.out.println("The number of top matches found is: " + numTopMatches);
//    	for(Match match : topMatchesList){
//    		System.out.println(match.toString());
//    	}
    	
    	return nthMatchRowAndColumn;
    }
    
}
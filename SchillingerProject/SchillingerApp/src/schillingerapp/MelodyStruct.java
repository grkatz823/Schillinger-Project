/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schillingerapp;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 *
 * @author prhayman
 */
public class MelodyStruct {

    //Global Variables
    MelodyItem[] melodyArray;
    BinaryPattern binPat;
    String musicString;
    Vector<String> tokenStream;
    LilyPond lily;
    int[] scale;
    String[] chromatic;

    /**
     * Used for analyzing the original input.  Constructor takes in the musicString
     * representing the input and creates an empty MelodyStruct.  Each field
     * is then filled in individually as information is calculated.
     *
     * @param musicString
     */
    public MelodyStruct(String musicString) {
        this.musicString = musicString;

        // Creates a BinaryPattern from music String to be stored as global variable
        Converter converter = new Converter();
        binPat = converter.convertMusicStringToBinaryPattern(musicString);


        // Initializes the empty melodyArray of proper length
        this.melodyArray = new MelodyItem[binPat.binaryPattern.length];

        // Initializes each element of melodyArray to an empty MelodyItem
        for (int i = 0; i < melodyArray.length; i++) {
            melodyArray[i] = new MelodyItem();
        }

        System.out.println("inMelodyStruct: " + musicString);
        //KeyFinder keyFinder = new KeyFinder(musicString);
        //this.setScale(keyFinder.bestMatchScale);
        //keyFinder.chromatic = this.chromatic;

    }

    /**
     * Creates and initializes a new MelodyStruct from a LilyPond file.
     *
     * @param lily
     */
    public MelodyStruct(LilyPond lily) {
        this.lily = lily;

        Converter converter = new Converter();
        // Creates a binary pattern from the tokenStream
        this.binPat = converter.convertLilyPondToBinaryPattern(lily);
        this.melodyArray = new MelodyItem[binPat.binaryPattern.length];

        // Initializes each element of melodyArray to an empty MelodyItem
        for (int i = 0; i < melodyArray.length; i++) {
            melodyArray[i] = new MelodyItem();
        }
        
        //KeyFinder keyFinder = new KeyFinder(lily);
        //this.setScale(keyFinder.bestMatchScale);
        //keyFinder.chromatic = this.chromatic;
    }

    /**
     * Used for generating the new melody.  Constructor takes in the binaryPattern
     * generated by the Rhythm Generator, and creates an empty MelodyStruct to
     * be filled in with the resulting permutations.
     *
     * @param binPat
     */
    public MelodyStruct(BinaryPattern binPat) {
        // Initializes the empty melodyArray of proper length
        this.melodyArray = new MelodyItem[binPat.binaryPattern.length];
        this.binPat = binPat;

        // Initializes each element of melodyArray to an empty MelodyItem
        for (int i = 0; i < melodyArray.length; i++) {
            melodyArray[i] = new MelodyItem();
        }

        for (int i = 0; i < melodyArray.length; i++) {
            if (this.binPat.binaryPattern[i] == 1) {
                melodyArray[i].setMidiNoteValue(-2);
            } else {
                melodyArray[i].setMidiNoteValue(-1);
            }
        }
    }

    /**
     * Populates all notes in midi value notation in the proper timed location
     * in melodyArray
     */
    public void populateMidiNoteValues() {

        // These lines create an int[] of the midiNotes contained within the
        // musicString one right after another with no duration information
        MelodyAnalyzer parser = new MelodyAnalyzer();
        int[] midiNoteValues = parser.getNoteValues(musicString);
        int attackCounter = 0;

        // Fills in the melodyArray with midiNotes and proper duration
        for (int i = 0; i < melodyArray.length; i++) {
            if (binPat.binaryPattern[i] == 1) {
                melodyArray[i].setMidiNoteValue(midiNoteValues[attackCounter]);
                attackCounter++;
            }
        }
    }

    /**
     * Populates all notes in midi value notation in the proper timed location
     * in melodyArray
     *
     * @param lily
     */
    public void populateMidiNoteValues(LilyPond lily) {
        MelodyAnalyzer analyzer = new MelodyAnalyzer();
        int[] midiNoteValues = analyzer.getNoteValues(this.lily);
        int attackCounter = 0;

        // Fills in the melodyArray with midiNotes and proper duration
        for (int i = 0; i < melodyArray.length; i++) {
            if (binPat.binaryPattern[i] == 1) {
                melodyArray[i].setMidiNoteValue(midiNoteValues[attackCounter]);
                attackCounter++;
            }
        }
    }

    /**Works as a setter method for InfoPoints.
     *
     * populates the InfoPoints into the melodyArray (PAXIS, MAX, MIN, MAXPA and MINPA)
     *
     * @param pitchAxis
     */
    public void populateInfoPoints(int pitchAxis) {
        populatePitchAxis(pitchAxis);
        populateMax();
        populateMin();
    //fixFront();
    //fixBack();
    }

    public void fixBack() {
        int currentIndex = this.melodyArray.length - 1;

        while (this.melodyArray[currentIndex].getMidiNoteValue() < 0) {
            currentIndex--;
        }
        int lastNoteIndex = currentIndex;

        if (this.melodyArray[lastNoteIndex].isExpendable()) {
            while (this.melodyArray[currentIndex].getInfoPoint() != MelodyItem.InfoPoint.PAXIS) {
                this.melodyArray[currentIndex].setInfoPoint(MelodyItem.InfoPoint.NULL);
                currentIndex--;
            }

            if (this.melodyArray[lastNoteIndex].getMidiNoteValue() < this.melodyArray[currentIndex].midiNoteValue) {
                this.melodyArray[lastNoteIndex].setInfoPoint(MelodyItem.InfoPoint.MIN);
            } else {
                this.melodyArray[lastNoteIndex].setInfoPoint(MelodyItem.InfoPoint.MAX);

            }


        }
    }

    void fixFront() {
        if (this.melodyArray[0].isExpendable()) {
            int leftIndex = 0;
            int currentIndex = 1;

            while (this.melodyArray[currentIndex].getInfoPoint() != MelodyItem.InfoPoint.PAXIS) {
                this.melodyArray[currentIndex].setInfoPoint(MelodyItem.InfoPoint.NULL);
                currentIndex++;

            }

            if (this.melodyArray[leftIndex].getMidiNoteValue() < this.melodyArray[currentIndex].midiNoteValue) {
                this.melodyArray[leftIndex].setInfoPoint(MelodyItem.InfoPoint.MIN);
            } else {
                this.melodyArray[leftIndex].setInfoPoint(MelodyItem.InfoPoint.MAX);
            }


        }
    }

    /**
     * Helpter Method.  Finds all the PAXIS's and marks them in the melodyArray
     *
     * @param pitchAxis
     */
    public void populatePitchAxis(int pitchAxis) {
        for (int i = 0; i < this.melodyArray.length; i++) {
            if (this.melodyArray[i].getMidiNoteValue() % 12 == pitchAxis) {
                this.melodyArray[i].setInfoPoint(MelodyItem.InfoPoint.PAXIS);
            }
        }
    }

    /**
     * Helper Method.  Finds all the local Maxima of Midi Notesbetween PAXIS
     * points as well as leading and ending phrases.
     */
    public void populateMax() {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 1;

        // Walks through the melodyArray
        while (currentIndex < melodyArray.length) {
            // Finds phrases
            while (melodyArray[currentIndex].getInfoPoint() != MelodyItem.InfoPoint.PAXIS && currentIndex < melodyArray.length - 1) {
                currentIndex++;
            }

            // Sets phrase endpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;
            // System.out.println(leftIndex + "," + rightIndex);

            // Call to helperMethod.  Finds the max midiValue
            int maxIndex = this.findMax(leftIndex, rightIndex);

            // Code only sets a maximum if it has not been set to something else
            // previously, and is greater than both endpoints.
            if (melodyArray[maxIndex].getInfoPoint() == MelodyItem.InfoPoint.NULL && melodyArray[maxIndex].getMidiNoteValue() > melodyArray[leftIndex].getMidiNoteValue() && melodyArray[maxIndex].getMidiNoteValue() > melodyArray[rightIndex].getMidiNoteValue()) {

                melodyArray[maxIndex].setInfoPoint(MelodyItem.InfoPoint.MAX);
            }

            currentIndex++;

        }
    }

    /**
     * Helper Method.  Finds the maximum midiNoteValue in the inputted phrase
     *
     * @param leftIndex
     * @param rightIndex
     * @return
     */
    public int findMax(int leftIndex, int rightIndex) {
        int maxIndex = leftIndex;

        for (int i = leftIndex + 1; i < rightIndex; i++) {
            if (melodyArray[i].getMidiNoteValue() > melodyArray[maxIndex].getMidiNoteValue()) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    /**
     * Helper Method.  Finds and sets all local Minima between PAXIS's and
     * leading and ending phrases.
     */
    public void populateMin() {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 1;

        // walks through array
        while (currentIndex < melodyArray.length) {
            // finds phrases
            while (melodyArray[currentIndex].getInfoPoint() != MelodyItem.InfoPoint.PAXIS && currentIndex < melodyArray.length - 1) {
                currentIndex++;
            }

            // Sets phrase endpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;

            // Call to helper method
            int minIndex = this.findMin(leftIndex, rightIndex);

            // Code only runs if the point found has not already been set to
            // something else previously, and is smaller than both endpoints
            if (melodyArray[minIndex].getInfoPoint() == MelodyItem.InfoPoint.NULL && melodyArray[minIndex].getMidiNoteValue() < melodyArray[leftIndex].getMidiNoteValue() && melodyArray[minIndex].getMidiNoteValue() < melodyArray[rightIndex].getMidiNoteValue()) {

                melodyArray[minIndex].setInfoPoint(MelodyItem.InfoPoint.MIN);
            }

            currentIndex++;

        }
    }

    /**
     * Helper Method.  Finds the minimum note value in an inputted phrase.
     *
     * @param leftIndex
     * @param rightIndex
     * @return
     */
    public int findMin(int leftIndex, int rightIndex) {
        int minIndex = leftIndex;

        for (int i = leftIndex + 1; i < rightIndex; i++) {
            if (melodyArray[i].getMidiNoteValue() < melodyArray[minIndex].getMidiNoteValue() && melodyArray[i].getMidiNoteValue() > -1) {//-1 signifies duration or rests
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Works as a setter method for Trajectories.
     */
    public void populateTrajectories() {

        //System.out.println("in Populate Trajectories");
        //System.out.println(this);

        // Finds and replaces MAXs with MAXPA and MINs with MINPA
        this.fixDiscontinuities();

        //System.out.println(this);

        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 1;

        // Walks through array
        while (currentIndex < melodyArray.length) {
            // Finds phrases
            while (melodyArray[currentIndex].isExpendable() && currentIndex < melodyArray.length - 1) {
                currentIndex++;
            }


            // Marks phrase endpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;
            currentIndex++;

            //  System.out.println(this);
            //  System.out.println(leftIndex + "," + rightIndex + "," + currentIndex);

            //System.out.println("Before switch (boop)");
            //System.out.print("This switch statement is being fed an argument of: ");
            //System.out.print(melodyArray[leftIndex].getInfoPoint() + "!");

            //  System.out.println(this);

            //          System.out.println("leftIndex = " + leftIndex);

            // All possibilities for enpoint combinations, and proper trajectories
            // that correspond to each.
            switch (melodyArray[leftIndex].getInfoPoint()) {
                case PAXIS:
                    // System.out.println("PAXIS (boop)");
                    if (melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MAX ||
                            melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MAXPA) {

                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("A");
                        }
                    } else if (melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MIN ||
                            melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MINPA) {

                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("C");
                        }
                    } else if (melodyArray[leftIndex].getMidiNoteValue() >
                            melodyArray[rightIndex].getMidiNoteValue()) {
                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("C");
                        }
                    } else if (melodyArray[leftIndex].getMidiNoteValue() <
                            melodyArray[rightIndex].getMidiNoteValue()) {
                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("A");
                        }
                    } else { //if same
                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("P");
                        }

                    }
                    break;

                case MAX: // Can only got to a PAXIS
                    //System.out.println("MAX (boop)");
                    for (int i = leftIndex; i < rightIndex; i++) {
                        melodyArray[i].setTrajectory("B");
                    }
                    break;
                case MIN: // Can only go to a PAXIS
                    //System.out.println("MIN (boop)");
                    for (int i = leftIndex; i < rightIndex; i++) {
                        melodyArray[i].setTrajectory("D");
                    }
                    break;
                case MAXPA: // Can only go to a MIN
                    //System.out.println("MAXPA (boop)");
                    for (int i = leftIndex; i < rightIndex; i++) {
                        melodyArray[i].setTrajectory("C");
                    }
                    break;
                case MINPA: // Can only go to a MAX
                    //System.out.println("MINPA (boop)");
                    for (int i = leftIndex; i < rightIndex; i++) {
                        melodyArray[i].setTrajectory("A");
                    }
                    break;
                default: //special cases for beginning phrase
                    //System.out.println("default (boop)");
                    if (melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MAX ||
                            melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MAXPA) {
                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("A");
                        }
                        melodyArray[leftIndex].setInfoPoint(MelodyItem.InfoPoint.MINPA);
                    } else if (melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MIN ||
                            melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MINPA) { //assumes MIN or MINPA
                        for (int i = leftIndex; i < rightIndex; i++) {
                            melodyArray[i].setTrajectory("C");
                        }
                        melodyArray[leftIndex].setInfoPoint(MelodyItem.InfoPoint.MAXPA);
                    } else { //right side is a pitch axis
                        if (melodyArray[leftIndex].getMidiNoteValue() < melodyArray[rightIndex].getMidiNoteValue()) {
                            for (int i = leftIndex; i < rightIndex; i++) {
                                melodyArray[i].setTrajectory("D");
                            }

                        } else {
                            for (int i = leftIndex; i < rightIndex; i++) {
                                melodyArray[i].setTrajectory("B");
                            }
                        }
                    }
                    break;
            }

        }

        // Last index is a special case
        String lastTrajectory = melodyArray[leftIndex - 1].getTrajectory();
        for (int i = leftIndex; i < melodyArray.length; i++) {
            melodyArray[i].setTrajectory(lastTrajectory);
        }
    }

    /**
     * Helper Method.  Finds where a MAX and a MIN are next to eachother and
     * replaces the first one with a MAXPA or a MINPA
     */
    public void fixDiscontinuities() {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 1;

        // Walks through array
        while (currentIndex < melodyArray.length) {
            // Finds sequential InfoPoints
            while (melodyArray[currentIndex].getInfoPoint() == MelodyItem.InfoPoint.NULL && currentIndex < melodyArray.length - 1) {
                currentIndex++;
            }

            //Marks phrase endpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;

            // if MAX and MIN
            if (melodyArray[leftIndex].getInfoPoint() == MelodyItem.InfoPoint.MAX && melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MIN) {
                melodyArray[leftIndex].setInfoPoint(MelodyItem.InfoPoint.MAXPA);
            }
            if (melodyArray[leftIndex].getInfoPoint() == MelodyItem.InfoPoint.MIN && melodyArray[rightIndex].getInfoPoint() == MelodyItem.InfoPoint.MAX) {
                melodyArray[leftIndex].setInfoPoint(MelodyItem.InfoPoint.MINPA);
            }

            currentIndex++;
        }
    }

    /**
     * Works as a Setter Method for LineValues.  It takes the midiNoteValues
     * on either side of a phrase, and calculates slopes, and subsequently, the
     * values of the line at each possible attack point. pitchAxis is needed in
     * order to move MAXPAs and MINPAs to the proper lineValue.
     *
     * @param pitchAxis
     */
    public void populateLineValues(int pitchAxis) {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 1;
        double slope = 0;

        //walks through the array
        while (currentIndex < melodyArray.length) {
            // Finds two ends of a trajectory
            while (melodyArray[currentIndex].isExpendable() && currentIndex < melodyArray.length - 1) {
                currentIndex++;
            }

            // Sets phrase enpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;
            currentIndex++;

            // If not MAXPA or MINPA, the left lineValue will always be the same
            // as the corresponding midiNoteValue.  If MINPA or MAXPA, lineValue
            // will become the proper pitchAxis.  For Max, it moves down towards
            // the next pitchAxis, for MINPA, it moves up.
            if (melodyArray[leftIndex].getInfoPoint() != MelodyItem.InfoPoint.MAXPA ||
                    melodyArray[leftIndex].getInfoPoint() != MelodyItem.InfoPoint.MINPA) {

                melodyArray[leftIndex].setLineValue(melodyArray[leftIndex].getMidiNoteValue());

            } else if (melodyArray[leftIndex].getInfoPoint() == MelodyItem.InfoPoint.MINPA) {
                double tmp = melodyArray[leftIndex].getMidiNoteValue();
                while ((int) tmp % 12 != pitchAxis) {
                    tmp++;
                }
                melodyArray[leftIndex].setLineValue(tmp);
            } else {// if MAXPA
                double tmp = melodyArray[leftIndex].getMidiNoteValue();
                while ((int) tmp % 12 != pitchAxis) {
                    tmp--;
                }
                melodyArray[leftIndex].setLineValue(tmp);
            }

            // Finds slope for calculating lineValues.  Type Casting was used
            // to ensure precision between int division.
            slope = (double) (melodyArray[rightIndex].getMidiNoteValue() -
                    melodyArray[leftIndex].getMidiNoteValue()) / (double) (rightIndex - leftIndex);

            // Sets each point of the line to it's proper value
            for (int i = leftIndex + 1; i < rightIndex; i++) {
                melodyArray[i].setLineValue(melodyArray[i - 1].getLineValue() + slope);
            }
        }

        // Lase note is special case
        double lastLineValue = melodyArray[leftIndex].getLineValue();

        for (int i = leftIndex + 1; i < melodyArray.length; i++) {
            melodyArray[i].setLineValue(lastLineValue);
        }
    }

    public void fixLineValues() {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 1;
        double slope = 0;

        //walks through the array
        while (currentIndex < melodyArray.length) {
            // Finds two ends of a trajectory
            while (melodyArray[currentIndex].isExpendable() && currentIndex < melodyArray.length - 1) {
                currentIndex++;
            }

            // Sets phrase enpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;
            currentIndex++;


            // Finds slope for calculating lineValues.  Type Casting was used
            // to ensure precision between int division.
            slope = (double) (melodyArray[rightIndex].getLineValue() -
                    melodyArray[leftIndex].getLineValue()) / (double) (rightIndex - leftIndex);

            // Sets each point of the line to it's proper value
            for (int i = leftIndex + 1; i < rightIndex; i++) {
                melodyArray[i].setLineValue(melodyArray[i - 1].getLineValue() + slope);
            }
        }
    }

    public void populateAttacks() {

        for (int i = 0; i < melodyArray.length; i++) {
            if (melodyArray[i].getMidiNoteValue() == -2) {
                if (melodyArray[i].getTrajectory().equals("A") ||
                        melodyArray[i].getTrajectory().equals("B")) {
                    if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.ASC) {
                        melodyArray[i].setMidiNoteValue((int) Math.ceil(melodyArray[i].getLineValue()));
                    } else if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.INS) {
                        melodyArray[i].setMidiNoteValue((int) Math.floor(melodyArray[i].getLineValue()));
                    } else {// if info point
                        melodyArray[i].setMidiNoteValue((int) melodyArray[i].getLineValue());
                    }
                } else {//if C or D
                    if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.ASC) {
                        melodyArray[i].setMidiNoteValue((int) Math.floor(melodyArray[i].getLineValue()));
                    } else if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.INS) {
                        melodyArray[i].setMidiNoteValue((int) Math.ceil(melodyArray[i].getLineValue()));
                    } else {// if info point
                        melodyArray[i].setMidiNoteValue((int) melodyArray[i].getLineValue());
                    }
                }
            }
        }
    }

    /**
     * Method is a continuation of populate InfoPoints, but is called later
     * because it requires the information in other lines of the melodyStruct.
     * Using the LineValues, and Trajectories, it determines whether the actual
     * note is inscribed or ascribed from the trajectory, and sets the appropriate
     * value in InfoPoint.
     */
    public void populateScribes() {
        int currentNoteValue;
        int currentIndex = 0;

        // Finds leading Rests
        while (melodyArray[currentIndex].getMidiNoteValue() < 0) {
            currentIndex++;
        }
        // Sets the current note to compare to lineValues
        currentNoteValue = melodyArray[currentIndex].getMidiNoteValue();

        // Walks through Array
        for (int i = 0; i < melodyArray.length; i++) {
            // Checks for durations and rests (initialized to -1)
            if (melodyArray[i].getMidiNoteValue() >= 0) {
                currentNoteValue = melodyArray[i].getMidiNoteValue();
            }

            // if Trajectory is A or B, midiNoteValues that are less than LineValues
            // are inscribed.  Conversely, if C or D, midiNotesValue that are less
            // than lineValues are ascribed.
            if (melodyArray[i].getTrajectory().equals("A") || melodyArray[i].getTrajectory().equals("B")) {
                // code only executes if point has not already been set
                if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.NULL) {
                    if (currentNoteValue < melodyArray[i].getLineValue()) {
                        melodyArray[i].setInfoPoint(MelodyItem.InfoPoint.INS);
                    } else {//is ascribed
                        melodyArray[i].setInfoPoint(MelodyItem.InfoPoint.ASC);
                    }
                }

            } else {// If C or D trajectories
                // Only executes if point has not already been set
                if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.NULL) {
                    if (currentNoteValue > melodyArray[i].getLineValue()) {
                        melodyArray[i].setInfoPoint(MelodyItem.InfoPoint.INS);
                    } else {//is ascribed
                        melodyArray[i].setInfoPoint(MelodyItem.InfoPoint.ASC);
                    }
                }
            }
        }
    }

    /**
     * Prints out all four fields as they are stored in melodyArray.  Lines are
     * represented as follows:
     *
     * 1st: midiNote Values
     * 2nd: infoPoints
     * 3rd: Trajectories
     * 4th: lineValues (to two decimal places)
     */
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        String string = "";

        for (int i = 0; i < melodyArray.length; i++) {
            string += melodyArray[i].getMidiNoteValue() + "   \t";
        }
        string += "\n";

        for (int i = 0; i < melodyArray.length; i++) {
            if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.PAXIS ||
                    melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.MINPA ||
                    melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.MAXPA) {
                string += melodyArray[i].getInfoPoint() + "\t";
            } else if (melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.NULL) {
                string += melodyArray[i].getInfoPoint() + " \t";
            } else {
                string += melodyArray[i].getInfoPoint() + "  \t";
            }
        }
        string += "\n";

        for (int i = 0; i < melodyArray.length; i++) {
            if (melodyArray[i].getTrajectory() != null) {
                string += melodyArray[i].getTrajectory() + "    \t";
            } else {
                string += melodyArray[i].getTrajectory() + " \t";
            }
        }
        string += "\n";

        for (int i = 0; i < melodyArray.length; i++) {
            string += df.format(melodyArray[i].getLineValue()) + "\t";
        }
        string += "\n";


        return string;
    }

    public double getCommonDenominator() {
        return binPat.getCommonDenominator();
    }

    public void setScale(int[] newScale) {
        this.scale = newScale;
    }

    public void setChromatic(String[] newChromatic) {
        this.chromatic = newChromatic;
    }
}
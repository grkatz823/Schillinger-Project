package schillingerapp;

import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author jestuart
 */
public class KeyFinder {

    /*
     * The root of the scale.
     */
    public int root;

    /*
     * The index of the mode.
     * Ionian = 0  //major     Dorian = 1
     * Phrygian = 2            Lydian = 3
     * Mixolydian = 4          Aeolian = 5 //minor
     * Locrian = 6
     */
    public int modeIndex;

    /*
     * The LilyPond string representing
     * both mode and key. For example, A locrian would
     * be represented by "\key a \locrian"
     */
    public String lilyPondKeyString;

    /*
     * The ultimate product of KeyFinder: an int[] of the
     * closest matching scale.
     */
    public int[] bestMatchScale;

    /*
     * Used in convertMelodyStructToLilyPond to determine what
     * notes to use for enharmonics.
     */
    public String[] chromatic = new String[12];

    /*
     * The global histogram variable to which the modal scales
     * are compared to determine the best match.
     */
    public int[] histogram;

    /*
     * The default constructor is ONLY called in SchillingerGUI to provide
     * access to the stringToIndex method.
     */
    public KeyFinder() {
    }

    /**
     * This constructor takes in a JFugue musicString and sets the global
     * variables, root, histogram, and mode accordingly.
     *
     * Root is considered to be the pitchAxis, aka the note used most often in
     * input.
     *
     * The histogram is an analysis of all the pitches including sharps and flats
     * and is used in determining the best fit scale.
     *
     * @param musicString
     */
    public KeyFinder(String musicString) {
        MelodyAnalyzer analyzer = new MelodyAnalyzer();
        MelodyStruct melody = analyzer.createAnalysis(musicString);
        this.root = analyzer.pitchAxis;
        this.histogram = analyzer.histogram;
        this.determineMode();
    }

    /**
     * Constructor made for future use.  Takes in a lilyPond Object, and sets
     * the root, histogram, and scales accordingly.
     */
    public KeyFinder(LilyPond lily) {
        MelodyAnalyzer analyzer = new MelodyAnalyzer();
        MelodyStruct melody = analyzer.createAnalysis(lily.musicString);
        this.root = analyzer.pitchAxis;
        this.histogram = analyzer.histogram;
        this.determineMode();
        this.determineSharpsOrFlats(lily.tokenStream);
    }

    /**
     * Takes in a LilyPond object, a MelodyAnalyzer pitch axis (which
     * becomes the global variable root), and a MelodyAnalyzer histogram,
     * and uses these three to calculate bestMatchScale and chromatic.
     *
     * @param lily
     * @param pAxis
     * @param histogram
     */
    public KeyFinder(LilyPond lily, int pAxis, int[] histogram) {
        this.root = pAxis;
        this.histogram = histogram;
        this.determineMode();
        this.determineSharpsOrFlats(lily.tokenStream);
    }

    /**
     * Takes in a MelodyAnalyzer pitch axis (which
     * becomes the global variable root), and a MelodyAnalyzer histogram,
     * and uses these three to calculate bestMatchScale and chromatic.
     *
     * @param lily
     * @param pAxis
     * @param histogram
     */
    public KeyFinder(int pAxis, int[] histogram) {
        this.root = pAxis;
        this.histogram = histogram;
        this.determineMode();
    }

    /**
     * Takes a string (such as AMAJOR), and checks its key and whether it's
     * major or minor and returns the corresponding scale in the form of a int[]
     * containing midiNote values from 0-11.
     * 
     * @param string
     * @return index
     */
    public int[] keyStringToScale(String string) {

        this.root = 0;
        String keyString = string.toUpperCase();

        switch (keyString.charAt(0)) {
            case 'A':
                root = 9;
                break;
            case 'B':
                root = 11;
                break;
            case 'C':
                root = 0;
                break;
            case 'D':
                root = 2;
                break;
            case 'E':
                root = 4;
            case 'F':
                root = 5;
                break;
            case 'G':
                root = 7;
                break;
        }

        // Adjusts root for sharps and flats
        if (keyString.charAt(1) == '#') {
            root++;
        } else if (keyString.charAt(1) == 'b') {
            root--;
        }

        // Special case to account for Cb and B#
        if (root < 0) {
            root += 12;
        } else if (root > 11) {
            root -= 12;
        }

        // Throws error if keyString is not in proper format or unrecognized
        if (!keyString.contains("MAJOR") && !keyString.contains("MIN")) {
            System.err.println("Error: key unrecognized. Please enter a valid key.");
        }

        // Sets modeIndex to Ionian or Aeolian according to the keyString
        if (keyString.contains("MAJOR")) {
            this.modeIndex = 0;
        } else if (keyString.contains("MIN")) {
            this.modeIndex = 5;
        }

        this.setBestMatchScale();

        return this.bestMatchScale;
    }

    /**
     * Compares the histogram to all seven modes and determines the highest
     * match among them.
     */
    private void determineMode() {

        //Define the int[] scales to which the histogram will be matched.
        int[] ionianScale = this.createIonianScale(root);
        int[] dorianScale = this.createDorianScale(root);
        int[] phrygianScale = this.createPhrygianScale(root);
        int[] lydianScale = this.createLydianScale(root);
        int[] mixolydianScale = this.createMixolydianScale(root);
        int[] aeolianScale = this.createAeolianScale(root);
        int[] locrianScale = this.createLocrianScale(root);

        int ionianMatchValue = 0;
        int dorianMatchValue = 0;
        int phrygianMatchValue = 0;
        int lydianMatchValue = 0;
        int mixolydianMatchValue = 0;
        int aeolianMatchValue = 0;
        int locrianMatchValue = 0;

        /* Double array containing the match values for all 7 modes
         * ordered from ionian to locrian.
         */
        double[] matchValueArray = new double[7];
        // Keeps track of the highest match value
        double maxMatchValue = 0;
        // The total number of frequencies in the histogram
        int matchValueDenominator = 0;

        // Calculate the value of matchValueDenominator
        for (int freq : histogram) {
            matchValueDenominator += freq;
        }

        // Calculate the raw match values for all 7 modes
        for (int i = 0; i < 7; i++) {
            ionianMatchValue += histogram[ionianScale[i]];
            //System.out.println("ionianMatchValue " + ionianMatchValue);
            dorianMatchValue += histogram[dorianScale[i]];
            //System.out.println("dorianMatchValue " + dorianMatchValue);
            phrygianMatchValue += histogram[phrygianScale[i]];
            //System.out.println("phrygianMatchValue " + phrygianMatchValue);
            lydianMatchValue += histogram[lydianScale[i]];
            //System.out.println("lydianMatchValue " + lydianMatchValue);
            mixolydianMatchValue += histogram[mixolydianScale[i]];
            //System.out.println("mixolydianMatchValue " + mixolydianMatchValue);
            aeolianMatchValue += histogram[aeolianScale[i]];
            //System.out.println("aeolianMatchValue " + aeolianMatchValue);
            locrianMatchValue += histogram[locrianScale[i]];
        //System.out.println("locrianMatchValue " + locrianMatchValue);
        //System.out.println();
        }

        // Fill the match array with the adjusted match values between 0 and 1
        matchValueArray[0] = (double) ionianMatchValue / matchValueDenominator;
        matchValueArray[1] = (double) dorianMatchValue / matchValueDenominator;
        matchValueArray[2] = (double) phrygianMatchValue / matchValueDenominator;
        matchValueArray[3] = (double) lydianMatchValue / matchValueDenominator;
        matchValueArray[4] = (double) mixolydianMatchValue / matchValueDenominator;
        matchValueArray[5] = (double) aeolianMatchValue / matchValueDenominator;
        matchValueArray[6] = (double) locrianMatchValue / matchValueDenominator;

        // Determine the highest match value and its corresponding index
        for (int i = 0; i < 7; i++) {
            if (matchValueArray[i] > maxMatchValue) {
                maxMatchValue = matchValueArray[i];
                this.modeIndex = i;
            }
        }
        setBestMatchScale();
    }

    /**
     * Given a binaryScale (a "template" for a given mode) and
     * a root, constructs a scale of the given mode on the given
     * root. The result is an int[] of intervals starting on C. For
     * example, createScale(ionianBinaryPattern, 0) would output
     * a C Ionian scale: {0, 2, 4, 5, 7, 9, 11}
     *
     * @param binaryScale
     * @param root
     * @return
     */
    private int[] createScale(int[] binaryScale, int root) {

        /* Counter used to keep track of the current interval. It's initialized
         * to the root value, and its value will be added to the resultScale
         * array during each iteration if the current value of binaryScale[]
         * is 1, otherwise its value is simply incremented.
         */
        int index = root;

        /* Creates a new int[] with a length of 7 that will
         * be filled with the intervals of a given scale.
         */
        int[] resultScale = new int[7];

        /* Counter to keep track of the current position in
         * the resultScaleArray
         */
        int resultCount = 0;

        //Populate resultScale!
        for (int i = 0; i < binaryScale.length; i++) {
            if (binaryScale[i] == 1) {
                if (index >= 12) {
                    // As all values in resultScale must be <= 11
                    resultScale[resultCount] = index - 12;
                } else {
                    resultScale[resultCount] = index;
                }
                // Increment the current position in the result array
                resultCount++;
                // Increment the current interval value
                index++;
            } else {
                index++;
            }
        }

        /* Ensures that any interval values reduced by 12 by the nested
         * if above are moved to the beginning of the resultScale array.
         */
        Arrays.sort(resultScale);

        return resultScale;
    }

    /**
     * Creates an Ionian (major) scale based on the given root.
     *
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createIonianScale(int root) {
        int[] ionianBinaryScale = {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1};
        return this.createScale(ionianBinaryScale, root);
    }

    /**
     * Creates a Dorian scale based on the given root.
     *
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createDorianScale(int root) {
        int[] dorianBinaryScale = {1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0};
        return this.createScale(dorianBinaryScale, root);
    }

    /**
     * Creates a Phrygian scale based on the given root.
     *
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createPhrygianScale(int root) {
        int[] phrygianBinaryScale = {1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0};
        return this.createScale(phrygianBinaryScale, root);
    }

    /**
     * Creates a Lydian scale based on the given root.
     *
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createLydianScale(int root) {
        int[] lydianBinaryScale = {1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1};
        return this.createScale(lydianBinaryScale, root);
    }

    /**
     * Creates a Mixolydian scale based on the given root.
     *
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createMixolydianScale(int root) {
        int[] mixolydianBinaryScale = {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0};
        return this.createScale(mixolydianBinaryScale, root);
    }

    /**
     * Creates an Aeolian (natural minor) scale based on the
     * given root.
     *
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createAeolianScale(int root) {
        int[] aeolianBinaryScale = {1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0};
        return this.createScale(aeolianBinaryScale, root);
    }

    /**
     * Creates a Locrian scale based on the given root.
     * 
     * @param root
     * @return An interval array of the form 0 >= x >= 12
     */
    public int[] createLocrianScale(int root) {
        int[] locrianBinaryScale = {1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0};
        return this.createScale(locrianBinaryScale, root);
    }

    /**
     * Test code
     * 
     * @param pitch
     */
    private void printNoteName(int pitch) {
        String[] notes = {"C ", "C#/Db ", "D ", "D#/Eb ", "E", "F ",
            "F#/Gb", "G", "G#/Ab", "A ", "A#/Bb ", "B "};

        if (pitch >= 12) {
            pitch -= 12;
        }

        System.out.println(notes[pitch]);
    }

    /**
     * Uses the global variables modeIndex and root, set in determineMode,
     * to set the global variable bestMatchScale.
     * 
     */
    private void setBestMatchScale() {
        switch (modeIndex) {
            case 0:
                this.bestMatchScale = this.createIonianScale(root);
                break;
            case 1:
                this.bestMatchScale = this.createDorianScale(root);
                break;
            case 2:
                this.bestMatchScale = this.createPhrygianScale(root);
                break;
            case 3:
                this.bestMatchScale = this.createLydianScale(root);
                break;
            case 4:
                this.bestMatchScale = this.createMixolydianScale(root);
                break;
            case 5:
                this.bestMatchScale = this.createAeolianScale(root);
                break;
            case 6:
                this.bestMatchScale = this.createLocrianScale(root);
                break;
        }
    }

    /**
     * Uses the global variables modeIndex and root to create a LilyPond
     * key string (e.g., \key c \aeolian)
     * 
     * @return A LilyPond key string (e.g., "\key c \aeolian")
     */
    public String getLilyPondKeyString() {
        String rootString = "";
        String modeString = "";
        switch (root) {
            case 0:
                rootString = "c";
                break;
            case 1:
                rootString = "df";
                break;
            case 2:
                rootString = "d";
                break;
            case 3:
                rootString = "ef";
                break;
            case 4:
                rootString = "e";
                break;
            case 5:
                rootString = "f";
                break;
            case 6:
                rootString = "fs";
                break;
            case 7:
                rootString = "g";
                break;
            case 8:
                rootString = "af";
                break;
            case 9:
                rootString = "a";
                break;
            case 10:
                rootString = "bf";
                break;
            case 11:
                rootString = "b";
                break;
        }
        switch (modeIndex) {
            case 0:
                modeString = "ionian";
                break;
            case 1:
                modeString = "dorian";
                break;
            case 2:
                modeString = "phrygian";
                break;
            case 3:
                modeString = "lydian";
                break;
            case 4:
                modeString = "mixolydian";
                break;
            case 5:
                modeString = "aeolian";
                break;
            case 6:
                modeString = "locrian";
                break;
        }
        return "\\key " + rootString + " \\" + modeString;
    }

    /**
     * Uses the global variable tokenStream to determine whether sharps or
     * flats should be used in the chromatic scale, setting the global variable
     * "chromatic". This will be used later when converting MelodyStruct back
     * to LilyPond.
     *
     */
    private void determineSharpsOrFlats(Vector<String> tokenStream) {
        // Chromatic scale with all sharps for accidentals
        String[] sharpChromatic = {"c", "cS", "d", "dS", "e", "f",
            "fS", "g", "gS", "a", "aS", "b"};
        // Chromatic scale with all flats for accidentals
        String[] flatChromatic = {"c", "dF", "d", "eF", "e", "f",
            "gF", "g", "aF", "a", "bF", "b"};
        // Counter for number of sharps in tokenStream
        int sharpCount = 0;
        // Counter for number of flats in tokenStream
        int flatCount = 0;

        /*
         * Counts the number of sharps/flats in token stream,
         * incrementing sharpCount/flatCount accordingly.
         */
        for (String noteToken : tokenStream) {
            if (noteToken.contains("S")) {
                sharpCount++;
            } else if (noteToken.contains("F")) {
                flatCount++;
            }
        }

        if (sharpCount > flatCount) {
            this.chromatic = sharpChromatic;
        } else {
            this.chromatic = flatChromatic;
        }
    }
}

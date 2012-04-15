/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schillingerapp;

import java.math.*;

/**
 *
 * @author anparks
 */
public class RhythmGenerator {

    /**
     * Index containing all possibilities for the permutation of a rhythm pattern.
     */
    public enum permType {

        /**
         * Swaps the first half of the pattern with the second.
         * swapFirstHalfWithSecond(int[] pat)
         */
        SWAP,
        /**
         * Repeats the first half of the pattern.
         * repeatFirsthalf(int[] pat)
         */
        REPEAT1,
        /**
         * Repeats the second half of the pattern.
         * repeatSecondHalf (int[] pat)
         */
        REPEAT2,
        /**
         * Permutes using only circular permuations.
         * circularlyPermutePattern(int[] binPat, int offset)
         */
        CIRCPERM,
        /**
         * Provides the fractioned counterpart.
         * returnFractionedCounterpart(int index)
         */
        FRACTIONED,
        /**
         * Balances the rhythmic pattern.
         * balanceRhythm(int[] rhythmPat)
         */
        BALANCE
    }

    /**
     * Computes a rhythmically complementary pattern using the balancing algorithm
     * described in Chapter V of Book 1.
     * 
     * @param   rhythmPat   A rhythm pattern in int[] form.
     * @return  The resulting int[].
     */
    public int[] balanceRhythm(int[] rhythmPat) throws MyException {

        //System.out.println("Welcom to balanceRhythm!");
        SchillingerDB schDB = new SchillingerDB();
        Converter converter = new Converter();
        int index = schDB.getIndexFromDB(rhythmPat);
        int[] resultingPat;
        RhythmPattern schillingerPattern = schDB.getSchillingerPatternFromDB(index);
        int[] counterpartPat = this.returnFractionedCounterpart(index);

        // System.out.println("index: " + index + "\n schillingerPat: " + schillingerPattern);

        // For rhythm patterns with only 2 generators:
        // 3 generator patterns will throw MyException
        if (index < 38) {
            int a = Character.getNumericValue(schillingerPattern.getDescription().charAt(0));
            int b = Character.getNumericValue(schillingerPattern.getDescription().charAt(2));
            // "m" is the number of times to repeat the unfractioned pattern
            int m = a / b;
            // For fractioned patterns.
            if (index % 2 != 0) {
                // Final length of the resulting pattern
                int patLength = m * counterpartPat.length + (a * a - m * a * b);
                // Creates a new int[]
                resultingPat = new int[patLength];
                // Duplicates the counterpartPat m times
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < counterpartPat.length; j++) {
                        resultingPat[j + counterpartPat.length * i] = counterpartPat[j];
                    }
                }
                // Creates an int[] with the proper duration of the last note
                int[] lastNote = converter.getBinaryEquivalent(a * a - m * a * b);
                // Adds on the last note to the resulting pattern
                for (int i = 0; i < lastNote.length; i++) {
                    resultingPat[i + counterpartPat.length * m] = lastNote[i];
                }
                //System.out.println(converter.convertIntArrayToString(resultingPat));
                return resultingPat;
            } else { // For unfractioned patterns
                //The original is considered to be a part of m.  Since we
                //do not return the original, we subtract one from m.
                m--;

                // Finds the length and creates the resultingPat
                int patLength = m * rhythmPat.length + counterpartPat.length +
                        (a * a - (m + 1) * a * b);
                resultingPat = new int[patLength];

                // Adds on the original pattern m times.
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < rhythmPat.length; j++) {
                        resultingPat[j + rhythmPat.length * i] = rhythmPat[j];
                    }
                }
                // Adds on the fractioned counterpart
                for (int i = 0; i < counterpartPat.length; i++) {
                    resultingPat[i + m * rhythmPat.length] = counterpartPat[i];
                }

                // Creates an int[] with the proper duration of the last note
                int[] lastNote = converter.getBinaryEquivalent(a * a - (m + 1) * a * b);
                // Adds on the last note to the resulting pattern
                for (int i = 0; i < lastNote.length; i++) {
                    resultingPat[i + m * rhythmPat.length + counterpartPat.length] = lastNote[i];
                }
                // System.out.println(converter.convertIntArrayToString(resultingPat));
                return resultingPat;
            }
        } else {
            // Not yet implemented.
            throw new MyException("This pattern has three generators");
        }
    }

    /**
     * Calls all needed methods based in input of musicString, and permutation
     * types that do not need any other parameters.  Returns a BinaryPattern.
     * 
     * @param musicString
     * @param permutation
     * @return A BinaryPattern.
     */
    public BinaryPattern createRhythm(String musicString, permType permutation) {
        Converter converter = new Converter();
        BinaryPattern binPat = converter.convertMusicStringToBinaryPattern(musicString);

        switch (permutation) {
            case SWAP:
                binPat.setBinaryPattern(this.swapFirstHalfWithSecond(binPat.getBinaryPattern()));
                break;

            case REPEAT1:
                binPat.setBinaryPattern(this.repeatFirstHalf(binPat.getBinaryPattern()));
                break;

            case REPEAT2:
                binPat.setBinaryPattern(repeatSecondHalf(binPat.getBinaryPattern()));
                break;

        }

        return binPat;
    }

    /**
     *  Calls all needed methods based in input of BinaryPattern binPat, and permutation
     * types that do not need any other parameters.  Returns a BinaryPattern.
     * @param binPat
     * @param permutation
     * @return
     */
    public BinaryPattern createRhythm(BinaryPattern binPat, permType permutation) {

        switch (permutation) {
            case SWAP:
                binPat.setBinaryPattern(this.swapFirstHalfWithSecond(binPat.getBinaryPattern()));
                break;

            case REPEAT1:
                binPat.setBinaryPattern(this.repeatFirstHalf(binPat.getBinaryPattern()));
                break;

            case REPEAT2:
                binPat.setBinaryPattern(this.repeatSecondHalf(binPat.getBinaryPattern()));
                break;
            case FRACTIONED:
                binPat.setBinaryPattern(this.returnFractionedCounterpart(binPat));
                break;
            case BALANCE:
                try {
                    binPat.setBinaryPattern(this.balanceRhythm(binPat.getBinaryPattern()));
                } catch (Exception e) {

                    System.out.println("Error: " + e);
                }
                break;
        }

        //  System.out.println("End of CreateRhythm");
        return binPat;
    }

    /**
     * createRhythm works to call all needed methods, and has a parameter input
     * for those generators that require more information.  Returns the
     * generated BinaryPattern.
     *
     * @param musicString A musicString.
     * @param permutation Any value of enum permType.
     * @param index A Schillinger index value.
     * @return The resulting BinaryPattern.
     */
    public BinaryPattern createRhythm(String musicString, permType permutation, int index) {
        Converter converter = new Converter();
        BinaryPattern binPat = converter.convertMusicStringToBinaryPattern(musicString);

        switch (permutation) {
            case CIRCPERM:
                binPat.setBinaryPattern(this.circularlyPermutePattern(binPat.getBinaryPattern(), index));
                break;

            case FRACTIONED:
                binPat.setBinaryPattern(this.returnFractionedCounterpart(index));
                break;
        }

        return binPat;
    }

    /**
     *
     * createRhythm works to call all needed methods, and has a parameter input
     * for those generators that require more information.  Returns the
     * generated BinaryPattern.
     *
     * @return The resulting BinaryPattern.
     * @param binPat
     * @param permutation
     * @param index
     * @return
     */
    public BinaryPattern createRhythm(BinaryPattern binPat, permType permutation, int index) {

        switch (permutation) {
            case CIRCPERM:
                binPat.setBinaryPattern(this.circularlyPermutePattern(binPat.getBinaryPattern(), index));
                break;

            case FRACTIONED:
                binPat.setBinaryPattern(this.returnFractionedCounterpart(index));
                break;

        }

        return binPat;
    }

    /**
     * This method works to generate the rhythm possibilities based in an
     * inputted binary pattern.  It calls all of the other methods in the class
     * designed to generate a unique rhythm and compiles them into a music
     * string where the original plays first, followed by the other options.
     *
     * @param binPat
     * @return
     */
    /*public String generatePossibilites(
    BinaryPattern binPat) {

    // Initializes variables to be used
    String musicString = "";
    Converter converter = new Converter();
    double commonDenominator = binPat.commonDenominator;
    double nextStartTime = binPat.getBinaryPattern().length * commonDenominator * 480;

    //Creates binary patterns of the results
    BinaryPattern swap = new BinaryPattern(this.swapFirstHalfWithSecond(binPat.getBinaryPattern()), commonDenominator);
    BinaryPattern repeatFirst = new BinaryPattern(this.repeatFirstHalf(binPat.getBinaryPattern()), commonDenominator);
    BinaryPattern repeatSecond = new BinaryPattern(this.repeatSecondHalf(binPat.getBinaryPattern()), commonDenominator);
    BinaryPattern circPerm = new BinaryPattern(this.circularlyPermutePattern(binPat.getBinaryPattern(), 4), commonDenominator);
    BinaryPattern rep = new BinaryPattern(this.repeatFirstHalf(binPat.getBinaryPattern()), commonDenominator);

    //Adds the next possibility onto the MusicString, each on a unique track
    //and starting at the end of the original pattern.
    musicString +=
    converter.convertBinaryPatternToMusicString(binPat, 0, 0);
    musicString +=
    converter.convertBinaryPatternToMusicString(swap, 1, (int) nextStartTime);
    musicString +=
    converter.convertBinaryPatternToMusicString(repeatFirst, 2, (int) nextStartTime);
    musicString +=
    converter.convertBinaryPatternToMusicString(repeatSecond, 3, (int) nextStartTime);
    musicString +=
    converter.convertBinaryPatternToMusicString(circPerm, 4, (int) nextStartTime);
    musicString +=
    converter.convertBinaryPatternToMusicString(rep, 5, (int) nextStartTime);

    return musicString;
    }*/
    /**
     * cuts the int[] pat in half and places the second half at the beginning
     * of the new int[] newPat which is then returned
     * @param pat
     * @return newPat
     */
    public int[] swapFirstHalfWithSecond(int[] pat) {
        //System.out.println("swapFirstHalfWithSecond");
        int[] tenPat = this.convertBinaryPatternToBaseTen(pat);
        int[] firstHalf = this.getFirstHalfPattern(tenPat);
        int[] secondHalf = this.getLastHalfPattern(tenPat);
        int[] newPat = new int[tenPat.length];
        int centerNote = this.getCenterNote(tenPat);


        if (tenPat.length % 2 == 1) {
            for (int i = 0; i < secondHalf.length; i++) {
                newPat[i] = secondHalf[i];
            }

            newPat[secondHalf.length] = centerNote;

            for (int i = 0; i < firstHalf.length; i++) {
                newPat[i + secondHalf.length + 1] = firstHalf[i];
            }

            newPat = this.convertBaseTenToBinary(newPat);
        } else {
            for (int i = 0; i < firstHalf.length; i++) {
                newPat[i] = secondHalf[i];
            }

            for (int i = 0; i < firstHalf.length; i++) {
                newPat[i + secondHalf.length] = firstHalf[i];
            }

            newPat = this.convertBaseTenToBinary(newPat);
        }
        //System.out.println("end of swap");
        return newPat;

    }

    /**
     * returns the middle note of an odd length int[] pattern
     * @param pat
     * @return
     */
    public int getCenterNote(int[] pat) {
        if (pat.length % 2 == 1) {
            int index = pat.length / 2;
            return pat[index];
        } else {
            return -1;
        }
    }

    /**
     * Takes the int[] pat, takes out the first half, and prints out the
     * first half twice (the second half is not printed out) in int[] newPat
     * @param pat
     * @return
     */
    public int[] repeatFirstHalf(int[] pat) {
        //System.out.println("repeatFirstHalf");
        int[] baseTenPat = this.convertBinaryPatternToBaseTen(pat);
        int[] firstHalf = this.getFirstHalfPattern(baseTenPat);
        int[] newPat = new int[pat.length];


        if (baseTenPat.length % 2 == 0) {
            int[] evenLengthPattern = new int[firstHalf.length * 2];

            for (int i = 0; i < firstHalf.length; i++) {
                evenLengthPattern[i] = firstHalf[i];
            }

            for (int i = 0; i < firstHalf.length; i++) {
                evenLengthPattern[i + firstHalf.length] = firstHalf[i];
            }

            newPat = this.convertBaseTenToBinary(evenLengthPattern);
        } else {
            int[] oddLengthPattern = new int[firstHalf.length * 2 + 1];

            for (int i = 0; i < firstHalf.length; i++) {
                oddLengthPattern[i] = firstHalf[i];
            }

            oddLengthPattern[firstHalf.length] = this.getCenterNote(baseTenPat);

            for (int i = 0; i < firstHalf.length; i++) {
                oddLengthPattern[i + 1 + firstHalf.length] = firstHalf[i];
            }

            newPat = this.convertBaseTenToBinary(oddLengthPattern);
        }
        return newPat;
    }

    /**
     * Takes the int[] pat, takes out the second half, and prints out the
     * second half twice (the first half is not printed out) in int[] newPat
     * @param pat
     * @return
     */
    public int[] repeatSecondHalf(int[] pat) {
        // System.out.println("repeatSecondHalf");
        Converter converter = new Converter();
        // converts the input binary pattern to base ten
        int[] patBaseTen = this.convertBinaryPatternToBaseTen(pat);
        // the second half of the input base ten pattern (WITHOUT
        // the center note if odd pattern)
        int[] secondHalf = this.getLastHalfPattern(patBaseTen);
        int[] newPat = new int[pat.length];

        if ((patBaseTen.length % 2) == 0) { // even pattern
            //System.out.println("isEven");
            int[] evenPatternBaseTen = new int[(secondHalf.length * 2)];

            for (int i = 0; i < secondHalf.length; i++) {
                evenPatternBaseTen[i] = secondHalf[i];
            }

            for (int i = 0; i < secondHalf.length; i++) {
                evenPatternBaseTen[i + secondHalf.length] = secondHalf[i];
            }

            newPat = this.convertBaseTenToBinary(evenPatternBaseTen);
            //System.out.println(converter.convertIntArrayToString(evenPatternBaseTen));
            //System.out.println(converter.convertIntArrayToString(newPat));

        } else { // odd pattern
            //System.out.println("isOdd");
            int[] oddPatternBaseTen = new int[(secondHalf.length * 2) + 1];
            int centerNote = this.getCenterNote(patBaseTen);

            for (int i = 0; i < secondHalf.length; i++) {
                oddPatternBaseTen[i] = secondHalf[i];
            }

            oddPatternBaseTen[secondHalf.length] = centerNote;

            for (int i = 0; i < secondHalf.length; i++) {
                oddPatternBaseTen[i + 1 + secondHalf.length] = secondHalf[i];
            }

            newPat = this.convertBaseTenToBinary(oddPatternBaseTen);
            // System.out.println(converter.convertIntArrayToString(pat));
            // System.out.println(converter.convertIntArrayToString(newPat));

            //System.out.println(converter.convertIntArrayToString(oddPatternBaseTen));
            //System.out.println(converter.convertIntArrayToString(newPat));

        }
        return newPat;
    }

    /**
     * Returns the Fractioned or Unfractioned counterpart to the pattern index
     * found in HitlistDB 
     * @param index
     * @return
     */
    public int[] returnFractionedCounterpart(int index) {
        // System.out.println("returnFractionedCounterpart");
        SchillingerDB database = new SchillingerDB();
        int[] newPat;

        if (index % 2 == 0) {
            newPat = database.getPatternFromDB(index + 1);
        } else {
            newPat = database.getPatternFromDB(index - 1);
        }

        return newPat;
    }

    public int[] returnFractionedCounterpart(BinaryPattern binPat) {
        // System.out.println("returnFractionedCounterpart");

        SchillingerDB database = new SchillingerDB();
        int index = database.getIndexFromDB(binPat.getBinaryPattern());

        int[] newPat;

        if (index % 2 == 0) {
            newPat = database.getPatternFromDB(index + 1);
        } else {
            newPat = database.getPatternFromDB(index - 1);
        }

        return newPat;
    }

    public int[] makeFractionedCounterpart(int index) {
        int[] newRhythm;

        SchillingerDB database = new SchillingerDB();
        RhythmPattern schillingerPattern = database.getSchillingerPatternFromDB(index);
        int a = Character.getNumericValue(schillingerPattern.getDescription().charAt(0));
        int b = Character.getNumericValue(schillingerPattern.getDescription().charAt(2));

        if(index %2 == 0){
            newRhythm = makeTwoGeneratorFractionedCounterpart(a,b);
        } else {
            newRhythm = makeTwoGeneratorOriginalPattern(a,b);
        }

        return newRhythm;
    }

    public int[] makeTwoGeneratorFractionedCounterpart(int majorGen, int minorGen) {
        int maxGen = Math.max(majorGen, minorGen);
        int minGen = Math.min(majorGen, minorGen);
        int[] fractioned = new int[maxGen * maxGen];

        for (int i = 0; i < fractioned.length; i++) {
            fractioned[i] = 0;
        }


        for (int j = 0; j < maxGen - minGen + 1; j++) {
            for (int i = 0; i < fractioned.length; i++) {
                if (i == 0 || i % maxGen == 0) {
                    fractioned[i] = 1;
                }
            }

            for (int k = 0; k < maxGen * minGen; k++) {
                if (k % minGen == 0 || k == 0) {
                    fractioned[k + (j * maxGen)] = 1;
                }
            }
        }
        return fractioned;
    }

    public int[] makeTwoGeneratorOriginalPattern(int majorGen, int minorGen) {
        int maxGen = Math.max(majorGen, minorGen);
        int minGen = Math.min(majorGen, minorGen);
        int[] pattern = new int[maxGen * minGen];

        for (int i = 0; i < pattern.length; i++) {
            pattern[i] = 0;
        }

        for (int i = 0; i < pattern.length; i++) {
            if (i % maxGen == 0 || i % minGen == 0) {
                pattern[i] = 1;
            }

        }


        return pattern;
    }

    public int[] circularlyPermutePattern(int[] binPat, int offset) {
        // System.out.println("circularlyPermutePattern");
        int[] tenBase = this.convertBinaryPatternToBaseTen(binPat);
        int[] newPat = new int[tenBase.length];

        for (int i = 0; i <
                tenBase.length - offset; i++) {
            newPat[i] = tenBase[i + offset];
        }

        for (int i = 0; i <
                offset; i++) {
            newPat[tenBase.length - offset + i] = tenBase[i];
        }

        int[] newBinaryPat = this.convertBaseTenToBinary(newPat);

        return newBinaryPat;
    }

    /**
     *      Helper method to initials the first half of a RhythmPattern with odd
     * middle objects included in both halves
     * @param pattern
     * @return firstHalf
     */
    public int[] getFirstHalfPattern(int[] pattern) {
        // System.out.println("getFirstHalfPattern");
        double originalLength = pattern.length;
        int[] firstHalf = new int[(int) Math.floor(originalLength / 2)];

        for (int i = 0; i <
                firstHalf.length; i++) {
            firstHalf[i] = pattern[i];
        }

        return firstHalf;
    }

    /**
     * Helper method to initials the last half of a RhythmPattern with odd
     * middle objects included in both halves
     * @param pattern
     * @return lastHalf
     */
    public int[] getLastHalfPattern(int[] pattern) {
        // System.out.println("getLastHalfPattern");
        double originalLength = pattern.length;
        int[] lastHalf = new int[(int) Math.floor(originalLength / 2)];

        for (int i = 0; i < lastHalf.length; i++) {
            lastHalf[i] = pattern[i + (int) Math.ceil(originalLength / 2)];
        }

        return lastHalf;
    }

    /**
     * NEEDS TO BE COMMENTED
     * @param pat
     * @return newPat
     */
    public int[] convertBinaryPatternToBaseTen(int[] pat) {
        // System.out.println("convertBinaryPatternToBaseTen");

        // new array has length equal to number of 1's in pattern
        int oneCount = 0;
        for (int i = 0; i <
                pat.length; i++) {
            if (pat[i] == 1) {
                oneCount++;
            }

        }


        int[] newPat = new int[oneCount];

        // count until next 1 appears
        int binaryCount = 0;
        int index = -1;

        for (int j = 0; j <
                pat.length; j++) {

            if (pat[j] == 1) {
                index++;
                binaryCount =
                        0;
                binaryCount++;

            }

            if (pat[j] == 0) {
                binaryCount++;
            }

            newPat[index] = binaryCount;

        }

        Converter converter = new Converter();
        //System.out.println(converter.convertIntArrayToString(newPat));
        return newPat;
    }

    /**
     * NEEDS TO BE COMMENTED
     * @param tenBase
     * @return
     */
    public int[] convertBaseTenToBinary(int[] tenBase) {
        //  System.out.println("In convertBaseTenToBinary");
        int[] binaryPat;
        int length = 0;
        int index = 0;

        for (int i = 0; i <
                tenBase.length; i++) {
            length += tenBase[i];
        }

        binaryPat = new int[length];

        for (int i = 0; i <
                tenBase.length; i++) {
            for (int j = 0; j <
                    tenBase[i]; j++) {
                if (j == 0) {
                    binaryPat[index] = 1;
                } else {
                    binaryPat[index + j] = 0;
                }

            }
            index += tenBase[i];
        }

        return binaryPat;
    }
}
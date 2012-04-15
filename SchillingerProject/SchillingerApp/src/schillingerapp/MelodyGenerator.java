package schillingerapp;

import java.lang.Math.*;

/**
 * Generates melodies, in theory.
 *
 * @author prhayman
 */
public class MelodyGenerator {

    /**
     * The number of pitch axes calculated in given musicString.
     */
    public int numOfPAXIS;
    /**
     * Array containing the pitch values of the pitch axes.
     * For example, a musicString with pitch axes at C5 and G5
     * would produce an int[] with the values 72 and 79.
     */
    public int[] pAXISvalues;
    /**
     *
     */
    public int[] newBinaryPattern;
    /**
     * Array listing all the pitch values at each attack point, disregarding
     * duration information. Used to create a histogram of midi note values,
     * from which such information as the number and value of pitch axes is
     * calculated.
     */
    public int[] listOfNotes;
    MelodyStruct newMelody;
    MelodyStruct original;

    /**
     * Enumeration of all possibile melody permutations.
     */
    public enum MelodyPerm {

        /**
         * Returns the original melody as is
         */
        NOCHANGE,
        /**
         * Permutes the melody vertically about the pitch axis
         */
        VERTICAL
    }

    /**
     * Constsructor takes in two MelodyStructs
     *
     * @param original
     * @param newMelody
     */
    public MelodyGenerator(MelodyStruct original, MelodyStruct newMelody) {
        this.newMelody = newMelody;
        this.original = original;
        //if the length of the two melody structs are not equal, make them equal
        if (original.melodyArray.length - newMelody.melodyArray.length != 0) {
            //Expands or contracts the original such that it matches the generated rhythm
            this.expandOrContractStruct();
        } else {
            /*if the lengths are equal, make the newMelody struct equal to the original struct
             * for each item in the array with the exeption of the midiNotes.  Since a
             * melody has not been generated, the pattern is set to -1 and -2, where -1 is a durationi
             * and -2 is an attack.
             */
            for (int i = 0; i < original.melodyArray.length; i++) {
                newMelody.melodyArray[i].setInfoPoint(original.melodyArray[i].getInfoPoint());//sets infoPoint (IE paxis, asc, ins, etc)
                newMelody.melodyArray[i].setLineValue(original.melodyArray[i].getLineValue());//sets LineValue (point on trajectory)
                newMelody.melodyArray[i].setTrajectory(original.melodyArray[i].getTrajectory());//sets Trajectory (A,B,C,D,P)
                if (original.melodyArray[i].getMidiNoteValue() > -1) {//if note value is an attack
                    newMelody.melodyArray[i].setMidiNoteValue(-2);//set it to -2
                } else {
                    newMelody.melodyArray[i].setMidiNoteValue(-1);//else keep it as -1
                }
            }
        }
    }

    /**
     *
     * @param permutation
     */
    public void permuteTrajectories(MelodyPerm permutation) {
        switch (permutation) {
            case NOCHANGE: //if permutation = NOCHANGE, do nothing
                newMelody = original;
                break;
            case VERTICAL: //if permutation = VERTICAL
                //System.out.println("Before vertical");
                this.permuteVertical(); //permute it Vertically
                //System.out.println("After");
                break;
        }
    }

    /**
     * This method does the brunt of the work.  It take the trajectories and turns
     * A to C and B to D, and fills in all the other fields to match the new
     * melody pattern.
     */
    public void permuteVertical() {

        //variables
        numOfPAXIS = 0;
        pAXISvalues = new int[8];


        //finds the number of existing PAXIS values and saves those values to
        // an array.
        for (int i = 0; i < original.melodyArray.length; i++) {
            if (original.melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.PAXIS) {
                if (!containsValue(pAXISvalues, original.melodyArray[i].getMidiNoteValue())) {
                    pAXISvalues[numOfPAXIS] = original.melodyArray[i].getMidiNoteValue();
                    numOfPAXIS++;
                }
            }
        }

        //System.out.println("Num of PAXIS: " + numOfPAXIS);

        pAXISvalues = bubbleSort1(pAXISvalues);//sort these values

        //set indexes to 0
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 0;


        // Looks for first nonexpendable infopoint
        while (currentIndex < newMelody.melodyArray.length - 1 && newMelody.melodyArray[currentIndex].isExpendable()) {
            currentIndex++;
        }


        //Sets the index of the first nonexpendable infopoint at leftIndex
        leftIndex = currentIndex;
        if (currentIndex < newMelody.melodyArray.length - 1) {//protection from walking off the end of the array
            currentIndex++;
        }


        // Looks for next nonexpendable infopoint
        while (currentIndex < newMelody.melodyArray.length - 1 && newMelody.melodyArray[currentIndex].isExpendable()) {
            currentIndex++;
        }


        //Sets the next nonexpendable infopoint to rightIndex
        if (currentIndex < newMelody.melodyArray.length - 1) {//protection from walking off the end of the array
            rightIndex = currentIndex;
            currentIndex++;
        } else {
            rightIndex = newMelody.melodyArray.length - 1;
        }

        System.out.println("before fixLeft");
        //saves the MelodyItem located at leftIndex in newMelody in a variable
        MelodyItem prevElement = newMelody.melodyArray[leftIndex];

        //changes the MelodyItem located at leftIndex in newMelody by calling fixLeft
        newMelody.melodyArray[leftIndex] = fixLeft(leftIndex, rightIndex);

        System.out.println("after fixLeft");

        //saves the MelodyItem located at rightIndex in newMelody in a variable
        MelodyItem saveRight = newMelody.melodyArray[rightIndex];

        //changes the MelodyItem located at rightIndex in newMelody by calling fixRight
        newMelody.melodyArray[rightIndex] = fixRight(prevElement, rightIndex);

        //Sets the previous element to Save Right
        System.out.println("right here");
        prevElement = saveRight;

        System.out.println("Before BIG 3 while");
        // Walks through the melodyArray
        while (currentIndex < newMelody.melodyArray.length) {
            // Finds phrases

            System.out.println("Before 4 while");
            /*find the next instance of MIN,MAX,PAXIS,MINPA,MAXPA or the end of the melody array
             * and sets it to the current index*/
            while (newMelody.melodyArray[currentIndex].isExpendable() && currentIndex < newMelody.melodyArray.length - 1) {
                currentIndex++;
            }

            System.out.println("After 4 while");
            // Sets phrase endpoints
            leftIndex = rightIndex;
            rightIndex = currentIndex;
            currentIndex++;

            //saves the MelodyItem located at rightIndex in newMelody in a variable
            saveRight = newMelody.melodyArray[rightIndex];
            //changes the MelodyItem located at rightIndex in newMelody by calling fixRight
            newMelody.melodyArray[rightIndex] = fixRight(prevElement, rightIndex);
            //Sets the previous element to Save Right
            prevElement = saveRight;
        }
        System.out.println("After 3 while");



        System.out.println(newMelody);



        //System.out.println(newMelody);
        // Fix the rest of Trajectories
        for (int i = 0; i < newMelody.melodyArray.length; i++) {
            if (newMelody.melodyArray[i].isExpendable()) {
                String trajectory = newMelody.melodyArray[i].getTrajectory();//get the trajectory from newMelody
                System.out.println(trajectory);
                String invTraj = invertTrajectory(trajectory);//invert it
    
                newMelody.melodyArray[i].setTrajectory(invTraj);//place it back into newMelody
            }

            newMelody.fixLineValues();//calls fixLineValues in melodyStruct
        }

        System.out.println("there");

        // Last note is special case to setLineValues
        double lastLineValue = newMelody.melodyArray[leftIndex].getLineValue();

        //sets last Note
        for (int i = leftIndex + 1; i < newMelody.melodyArray.length; i++) {
            newMelody.melodyArray[i].setLineValue(lastLineValue);
        }


        newMelody.populateAttacks();//call Populate Attacks from Melody Struct
        this.fitToScale();//Changes the notes so that they are within the
        //proper scale


    }

    public void fitToScale() {
        System.out.println("In fitToScale");
        Converter converter = new Converter();
        System.out.println("Scale: " + converter.convertIntArrayToString(newMelody.scale));

        int[] scale = newMelody.scale;
//        System.out.println("scale length: " + converter.convertIntArrayToString(scale));

        //for the length of newMelody
        for (int i = 0; i < newMelody.melodyArray.length; i++) {
            //if the note value at index i is an attack and the note value is not in the scale
            if (newMelody.melodyArray[i].getMidiNoteValue() > -1 &&
                    !inScale(scale, newMelody.melodyArray[i].getMidiNoteValue())) {
                if (newMelody.melodyArray[i].getInfoPoint() == MelodyItem.InfoPoint.ASC) {//if ascribed
                    //add one to the midi note value at index i
                    newMelody.melodyArray[i].setMidiNoteValue(newMelody.melodyArray[i].getMidiNoteValue() + 1);
                } else {//if Inscribed
                    //subtract one from the midi note value at index i
                    newMelody.melodyArray[i].setMidiNoteValue(newMelody.melodyArray[i].getMidiNoteValue() - 1);
                }
            }
        }
    }

    public boolean inScale(int[] scale, int note) {
        boolean isIn = false;//set flag

        //for the length of the scale
        for (int i = 0; i < scale.length; i++) {
            //if the note is in the scale
            if (scale[i] == note % 12) {
                //set flag to true
                isIn = true;
            }
        }

        return isIn;
    }

    public MelodyItem fixRight(MelodyItem prevElement, int right) {
        MelodyItem newRight = new MelodyItem();//create resulting Melody Item

        //check the infopoint of melodyArray at the right index
        switch (newMelody.melodyArray[right].getInfoPoint()) {
            case PAXIS://if a pitch Axis
                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint remains a PAXIS
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- Line value is exchanged such that the PAxis value is moved to the new and proper value.


                newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());//keep MidiNoteValue the same
                newRight.setInfoPoint(MelodyItem.InfoPoint.PAXIS);//set Info Point to PAXIS
                newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));//invert the trajectory
                newRight.setLineValue((double) exchangePAXIS((int) newMelody.melodyArray[right].getLineValue()));//rotates the Pitch Axes (if more than 1)
                break;
            case MAX://if a MAX
                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint becomes MIN
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is set to the difference between the old left and right such that
                //  the values have been inverted vertically around proper pitchAxes.

                if (prevElement.getInfoPoint() == MelodyItem.InfoPoint.PAXIS) {//if previous element is a PAXIS

                    double leftLineValue = prevElement.getLineValue();//leftLineValue is the previous element's line value
                    double rightLineValue = newMelody.melodyArray[right].getLineValue();//rightLineValue is the Line Value in Melody Array at index right
                    double difference = leftLineValue - rightLineValue;//negative
                    //rightLineValue becomes the previous element's line Value AFTER the pitch axis has been exchanged PLUS the difference
                    rightLineValue = (double) (exchangePAXIS((int) prevElement.getLineValue())) + difference;

                    newRight.setLineValue(rightLineValue);//set the line value
                    newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());//set note value to the note value in newMelody's melodyArray at index right
                    newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));//invert the trajectory from newMeloyr then set it to newRight
                    newRight.setInfoPoint(MelodyItem.InfoPoint.MIN);//set InfoPoint to MIN
                } else { //prevElement.getInfoPoint() == MINPA
                    int currentIndex = numOfPAXIS - 1;

                    while (currentIndex > 0 && pAXISvalues[currentIndex] < prevElement.getLineValue()) {//
                        currentIndex--;
                    }

                    double leftLineValue = pAXISvalues[currentIndex];
                    double rightLineValue = newMelody.melodyArray[right].getLineValue();
                    double difference = leftLineValue - rightLineValue;
                    rightLineValue = (double) (exchangePAXIS((int) leftLineValue)) + difference;

                    newRight.setLineValue(rightLineValue);
                    newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());
                    newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));
                    newRight.setInfoPoint(MelodyItem.InfoPoint.MIN);
                }
                break;
            case MIN:
                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint becomes MAX
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is set to the difference between the old left and right such that
                //  the values have been inverted vertically around proper pitchAxes.

                if (prevElement.getInfoPoint() == MelodyItem.InfoPoint.PAXIS) {
                    double leftLineValue = prevElement.getLineValue();
                    double rightLineValue = newMelody.melodyArray[right].getLineValue();
                    double difference = leftLineValue - rightLineValue;//positive

                    rightLineValue = (double) (exchangePAXIS((int) prevElement.getLineValue())) + difference;
                    newRight.setLineValue(rightLineValue);
                    newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());
                    newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));
                    newRight.setInfoPoint(MelodyItem.InfoPoint.MAX);
                } else { //prevElement.getInfoPoint() == MAXPA
                    int currentIndex = 0;

                    while (currentIndex < numOfPAXIS && pAXISvalues[currentIndex] > prevElement.getLineValue()) {
                        currentIndex++;
                    }

                    double leftLineValue = pAXISvalues[currentIndex];
                    double rightLineValue = newMelody.melodyArray[right].getLineValue();
                    double difference = leftLineValue - rightLineValue;//positive
                    rightLineValue = (double) (exchangePAXIS((int) leftLineValue)) + difference;

                    newRight.setLineValue(rightLineValue);
                    newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());
                    newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));
                    newRight.setInfoPoint(MelodyItem.InfoPoint.MAX);
                }
                break;
            case MAXPA:
                //MAXPA can only come from min

                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint becomes MINPA
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is the result of finding the corresponding PA to the MAXPA,
                //  exchanging it, via exchangePAXIS, and subtracting the difference between the original
                //  MAXPA and the PA to create the MINPA.

                int currentIndex = 0;

                //Finds the index of the closest PA equal to or lower than the MAXPA because it is
                //attached to this value.
                while (currentIndex < numOfPAXIS - 1 && this.pAXISvalues[currentIndex] > (int) newMelody.melodyArray[right].getLineValue()) {
                    currentIndex++;
                }


                int paValue = pAXISvalues[currentIndex];
                int maxPAValue = (int) newMelody.melodyArray[right].getLineValue();
                int difference = maxPAValue - paValue;//positive

                paValue = exchangePAXIS(paValue);

                newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());
                newRight.setInfoPoint(MelodyItem.InfoPoint.MINPA);
                newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));
                newRight.setLineValue(paValue - difference);

                break;
            case MINPA:
                //MINPA values are offsetted from a PAxis value just above it.  This method
                //finds that value, and exchanges it to the proper place and adds the difference
                //between the original PAxis value and the MINPA value to create a MAXPA.

                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint changes from MINPA to MAXPA
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is found by exchanging the corresponding PAxis value, and adding
                // the difference between the original PAxis value and the MINPA line value.


                currentIndex = numOfPAXIS - 1;

                //Finds the index of the closest PA equal to or higher than the MINPA because it is
                //attached to this value.
                while (currentIndex > 0 && this.pAXISvalues[currentIndex] < (int) newMelody.melodyArray[right].getLineValue()) {
                    currentIndex--;
                }


                paValue = pAXISvalues[currentIndex];
                int minPAValue = (int) newMelody.melodyArray[right].getLineValue();
                difference = paValue - minPAValue;//positive

                paValue = exchangePAXIS(paValue);

                newRight.setMidiNoteValue(newMelody.melodyArray[right].getMidiNoteValue());
                newRight.setInfoPoint(MelodyItem.InfoPoint.MAXPA);
                newRight.setTrajectory(invertTrajectory(newMelody.melodyArray[right].getTrajectory()));
                newRight.setLineValue(paValue + difference);
                break;
            default:
                newRight = newMelody.melodyArray[right];
        }

        return newRight;
    }

    public MelodyItem fixLeft(int left, int right) {
        Converter conv = new Converter();
        MelodyItem newLeft = new MelodyItem();
        //System.out.println("in fixLeft: " + left + ", " + right);
        //System.out.println(newMelody.melodyArray[left].getInfoPoint());

        switch (newMelody.melodyArray[left].getInfoPoint()) {
            case PAXIS:
                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint remains a PAXIS
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- Line value is exchanged such that the PAxis value is moved to the new and proper value.
                newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
                newLeft.setInfoPoint(MelodyItem.InfoPoint.PAXIS);
                newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
                newLeft.setLineValue((double) exchangePAXIS((int) newMelody.melodyArray[left].getLineValue()));
                break;
            case MAX:
                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint changes from Max to Min
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is set to the difference between the old left and right,
                //    so that the slope remains the same when the right PAxis is exchanged
                double leftLineValue = newMelody.melodyArray[left].getLineValue();
                double rightLineValue = newMelody.melodyArray[right].getLineValue();
                double difference = leftLineValue - rightLineValue;

                rightLineValue = (double) exchangePAXIS((int) newMelody.melodyArray[right].getLineValue());
                newLeft.setLineValue(rightLineValue - difference);
                newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
                newLeft.setInfoPoint(MelodyItem.InfoPoint.MIN);
                newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
                break;
            case MIN:
                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint changes from MIN to MAX
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is set to the difference between the old left and right,
                //    so that the slope remains the same when the right PAxis is exchanged

                leftLineValue = newMelody.melodyArray[left].getLineValue();
                rightLineValue = newMelody.melodyArray[right].getLineValue();
                difference = leftLineValue - rightLineValue;//negative

                rightLineValue = (double) exchangePAXIS((int) newMelody.melodyArray[right].getLineValue());
                newLeft.setLineValue(rightLineValue - difference);//essentially add
                newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
                newLeft.setInfoPoint(MelodyItem.InfoPoint.MAX);
                newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
                break;
            case MINPA:
                //MINPA values are offsetted from a PAxis value just above it.  This method
                //finds that value, and exchanges it to the proper place and adds the difference
                //between the original PAxis value and the MINPA value to create a MAXPA.

                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint changes from MINPA to MAXPA
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is found by exchanging the corresponding PAxis value, and adding
                // the difference between the original PAxis value and the MINPA line value.


                int currentIndex = numOfPAXIS - 1;

                //Finds the index of the closest PA equal to or higher than the MINPA because it is
                //attached to this value.
                while (currentIndex > 0 && this.pAXISvalues[currentIndex] < (int) newMelody.melodyArray[left].getLineValue()) {
                    currentIndex--;
                }


                int paValue = pAXISvalues[currentIndex];
                int minPAValue = (int) newMelody.melodyArray[left].getLineValue();
                difference = paValue - minPAValue;//positive

                paValue = exchangePAXIS(paValue);

                newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
                newLeft.setInfoPoint(MelodyItem.InfoPoint.MAXPA);
                newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
                newLeft.setLineValue(paValue + difference);

//                //Special cases needed for jumps from PAxis to PAxis.  Checks for Case
//                Boolean isPAxis = false;
//                for (int i = 0; i < pAXISvalues.length; i++) {
//                    if (pAXISvalues[i] == (int) newMelody.melodyArray[left].getLineValue()) {
//                        isPAxis = true;
//                    }
//                }
//
//                if (isPAxis) {
//                    //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
//                    //- InfoPoint changes from MIN to MAX
//                    //- Trajectory is inverted from A to C, B to D, and vice versa.
//                    //- LineValue is set to the difference between the old left and right,
//                    //    so that the slope remains the same when the right PAxis is exchanged
//
//                    leftLineValue = newMelody.melodyArray[left].getLineValue();
//                    rightLineValue = newMelody.melodyArray[right].getLineValue();
//                    difference = leftLineValue - rightLineValue;//negative
//
//                    rightLineValue = (double) exchangePAXIS((int) newMelody.melodyArray[right].getLineValue());
//                    newLeft.setLineValue(rightLineValue - difference);//essentially add
//                    newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
//                    newLeft.setInfoPoint(MelodyItem.InfoPoint.MAX);
//                    newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
//
//                } else {
//
//
//                }

//                if (isPAxis) { //For special case from one PAXIS to another
//
//                    //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
//                    //- InfoPoint changes from MINPA to MAXPA
//                    //- Trajectory is inverted from A to C, B to D, and vice versa.
//                    //- LineValue is set to the difference between the old left and right,
//                    //    so that the slope remains the same when the right PAxis is exchanged
//
//
//                    int currentIndex = numOfPAXIS - 1;
//
//                    //Finds the PAxis above the current noteValue which also happens to be a PAxis.
//                    while (pAXISvalues[currentIndex] <= newMelody.melodyArray[left].getLineValue()) {
//                        currentIndex--;
//                    }
//
//                    //System.out.println("In MinPA after while");
//                    rightLineValue = pAXISvalues[currentIndex];
//                    leftLineValue = newMelody.melodyArray[left].getLineValue();
//                    difference = leftLineValue - rightLineValue;
//                    //System.out.println("Set some stuff");
//                    newLeft.setLineValue((double) exchangePAXIS((int) rightLineValue) - difference);
//                    newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
//                    newLeft.setInfoPoint(MelodyItem.InfoPoint.MAXPA);
//                    newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
//                //System.out.println("finished!!!");
//                } else {
//                    //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
//                    //- InfoPoint changes from MINPA to MAXPA
//                    //- Trajectory is inverted from A to C, B to D, and vice versa.
//                    //- LineValue is set to the difference between the old left and right,
//                    //    so that the slope remains the same when the right PAxis is exchanged
//
//                    leftLineValue = newMelody.melodyArray[left].getLineValue();
//                    rightLineValue = newMelody.melodyArray[right].getLineValue();
//                    difference = leftLineValue - rightLineValue;
//
//                    rightLineValue = (double) exchangePAXIS((int) newMelody.melodyArray[right].getLineValue());
//                    newLeft.setLineValue(rightLineValue - difference);
//                    newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
//                    newLeft.setInfoPoint(MelodyItem.InfoPoint.MAX);
//                    newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
//                }
                break;
            case MAXPA:
                //MAXPA values are offsetted from a PAxis value just below it.  This method
                //finds that value, and exchanges it to the proper place and adds the difference
                //between the original PAxis value and the MAXPA value to create a MINPA.

                //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint changes from MAXPA to MINPA
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is found by exchanging the corresponding PAxis value, and adding
                // the difference between the original PAxis value and the MAXPA line value.


                currentIndex = 0;

                //Finds the index of the closest PA equal to or lower than the MAXPA because it is
                //attached to this value.
                while (currentIndex < numOfPAXIS - 1 && this.pAXISvalues[currentIndex] > (int) newMelody.melodyArray[left].getLineValue()) {
                    currentIndex++;
                }


                paValue = pAXISvalues[currentIndex];
                int maxPAValue = (int) newMelody.melodyArray[left].getLineValue();
                difference = maxPAValue - paValue;//positive

                paValue = exchangePAXIS(paValue);

                newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
                newLeft.setInfoPoint(MelodyItem.InfoPoint.MINPA);
                newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
                newLeft.setLineValue(paValue - difference);

                //Special cases needed for jumps from PAxis to PAxis.  Checks for Case
//                isPAxis = false;
//                for (int i = 0; i < pAXISvalues.length; i++) {
//                    if (pAXISvalues[i] == (int) newMelody.melodyArray[left].getLineValue()) {
//                        isPAxis = true;
//                    }
//                }
//
//                if (isPAxis) { //For special case from one PAXIS to another
//
//                    //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
                //- InfoPoint changes from MINPA to MAXPA
                //- Trajectory is inverted from A to C, B to D, and vice versa.
                //- LineValue is set to the difference between the old left and right,
                //    so that the slope remains the same when the right PAxis is exchanged

//
//                    int currentIndex = 0;
//
//                    //Finds the PAxis above the current noteValue which also happens to be a PAxis.
//                    while (pAXISvalues[currentIndex] >= newMelody.melodyArray[left].getLineValue()) {
//                        currentIndex++;
//                    }
//
//                    //System.out.println("In MinPA after while");
//                    rightLineValue = pAXISvalues[currentIndex];
//                    leftLineValue = newMelody.melodyArray[left].getLineValue();
//                    difference = leftLineValue - rightLineValue;
//                    //System.out.println("Set some stuff");
//                    newLeft.setLineValue((double) exchangePAXIS((int) rightLineValue) - difference);
//                    newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
//                    newLeft.setInfoPoint(MelodyItem.InfoPoint.MAXPA);
//                    newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
//                //System.out.println("finished!!!");
//                } else {
//                    //- midiNoteValue does not change because it is a binaryPattern saved as -1 and -2.
//                    //- InfoPoint changes from MINPA to MAXPA
//                    //- Trajectory is inverted from A to C, B to D, and vice versa.
//                    //- LineValue is set to the difference between the old left and right,
//                    //    so that the slope remains the same when the right PAxis is exchanged
//
//                    leftLineValue = newMelody.melodyArray[left].getLineValue();
//                    rightLineValue = newMelody.melodyArray[right].getLineValue();
//                    difference = leftLineValue - rightLineValue;
//
//                    rightLineValue = (double) exchangePAXIS((int) newMelody.melodyArray[right].getLineValue());
//                    newLeft.setLineValue(rightLineValue - difference);
//                    newLeft.setMidiNoteValue(newMelody.melodyArray[left].getMidiNoteValue());
//                    newLeft.setInfoPoint(MelodyItem.InfoPoint.MAX);
//                    newLeft.setTrajectory(invertTrajectory(newMelody.melodyArray[left].getTrajectory()));
//                }
                break;
        }
        //System.out.println("after the switch");
        return newLeft;
    }

    public String invertTrajectory(String currentTrajectory) {
        String newTrajectory = "";


        switch (currentTrajectory.charAt(0)) {//get trajectory at character 0
            case 'A'://if A
                newTrajectory = "C";//set newTrajectory to C
                break;
            case 'B'://if B
                newTrajectory = "D";//set to D
                break;
            case 'C'://if C
                newTrajectory = "A";//set to A
                break;
            case 'D'://if D
                newTrajectory = "B";//set to B
                break;
            case 'P'://if P
                newTrajectory = "P";//keep the same
                break;
            default:
                //System.out.println("You are in the default case");
                break;
        }

        return newTrajectory;
    }

    public int exchangePAXIS(int currentValue) {
        int index = 0;
        //for the length of pAXISvalues
        for (int i = 0; i < pAXISvalues.length; i++) {
            //if pAXISvalues at index i equal the current value
            if (pAXISvalues[i] == currentValue) {
                //set the index to i
                index = i;
            }
        }
        //set and return newValue
        int newValue = pAXISvalues[(numOfPAXIS - 1) - index];

        return newValue;
    }

    /* Helper Method used to sort an array via BubbleSort techniques */
    public int[] bubbleSort1(int[] input) {
        int[] array = input;

        int length = array.length;

        for (int pass = 1; pass < length; pass++) {  // count how many times
            // This next loop becomes shorter and shorter
            for (int i = 0; i < length - pass; i++) {
                if (array[i] < array[i + 1]) {
                    // exchange elements
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
        }

        return array;
    }

    public boolean containsValue(int[] pAXISvalues, int value) {

        for (int i = 0; i < pAXISvalues.length; i++) {//for length of pAXISvalues
            if (pAXISvalues[i] == value) {//if value at index i = int value
                return true;//true
            }
        }

        return false;//else false
    }

    /* Permute trajectories & extremes
     *      (Vertical Perm)
     *        Find PAXIS values
     *          if odd number of Axes, mirror, of even, swap. (for vertical perm)
     *          Find extremes, and mirror.
     *      populate Line Values
     *      Using existing ASC and INS, populate midiNoteValues at attack points
     *
     *      return struct.
     */
    /**
     * Takes the original and the newMelody structs and makes them the same length
     * by adding or subtracting elements at regular intervals without deleting
     * important infopoints.
     */
    public void expandOrContractStruct() {

        int difference = original.melodyArray.length - newMelody.melodyArray.length;
        int segmentLength = newMelody.melodyArray.length / difference; //Items need
        //to be added and removed at equal intervals.  That interval is the segmentLength
        int elementsChanged = 0;
        newBinaryPattern = new int[newMelody.melodyArray.length];
        int expendableElements = 0;//infoPoint = ASC, INS, or NULL


        // Saves permuted BinaryPattern stored within newMelody as -1 and -2.
        // where -1 is duration, and -2 is attack saved in the midiNoteValue field.
        for (int i = 0; i < newMelody.melodyArray.length; i++) {
            newBinaryPattern[i] = newMelody.melodyArray[i].getMidiNoteValue();
        }


        //if positive shrink
        if (difference > 0) {
            // Finds the number of expendable elements
            for (int i = 0; i < original.melodyArray.length; i++) {
                if (original.melodyArray[i].isExpendable()) {
                    expendableElements++;
                }
            }
            // Checks for special case: not enough expendable items in array
            if (expendableElements < difference) {
                // sets to null for deletion later
                for (int i = 0; i < original.melodyArray.length; i++) {
                    if (original.melodyArray[i].isExpendable()) {
                        original.melodyArray[i] = new MelodyItem();
                        elementsChanged++;
                    }
                }
                // nullifies the rest starting with the last elements
                while (elementsChanged < difference) {
                    for (int i = original.melodyArray.length - 1; i > 0; i--) {
                        original.melodyArray[i] = new MelodyItem();
                        elementsChanged++;
                    }
                }
            } else if (expendableElements > difference) {//more than enough expendable items
                int[] expendableIndices = new int[expendableElements];
                int currentIndex = 0;

                // creates an int[] containing the indices of expendable Elements
                for (int i = 0; i < original.melodyArray.length; i++) {
                    if (original.melodyArray[i].isExpendable()) {
                        expendableIndices[currentIndex] = i;
                        currentIndex++;
                    }
                }

                segmentLength = expendableIndices.length / difference;
                currentIndex = segmentLength - 1; // starts at the end of first segment

                while (elementsChanged < difference && currentIndex < original.melodyArray.length) {
                    original.melodyArray[expendableIndices[currentIndex]] = new MelodyItem();
                    elementsChanged++;
                    currentIndex += segmentLength;
                }

            }

            //Shift elements to be sequential
            for (int i = 0; i < original.melodyArray.length - elementsChanged; i++) {
                if (original.melodyArray[i].getTrajectory() == null) {// checks for empty MelodyItem
                    for (int j = i; j < original.melodyArray.length - 1; j++) {
                        original.melodyArray[j] = original.melodyArray[j + 1];
                    }
                }
            }

            // Fills in the new melody with the final permutation of original.
            // with the saved binary pattern for attack points.  Actual note
            // values have not been generated, so the midinotes are -1 and -2.
            for (int i = 0; i < newMelody.melodyArray.length; i++) {
                newMelody.melodyArray[i].setMidiNoteValue(newBinaryPattern[i]);
                newMelody.melodyArray[i].setInfoPoint(original.melodyArray[i].getInfoPoint());
                newMelody.melodyArray[i].setLineValue(original.melodyArray[i].getLineValue());
                newMelody.melodyArray[i].setTrajectory(original.melodyArray[i].getTrajectory());
            }

        } else if (difference < 0) {//expand if negative
            difference = Math.abs(difference);
            segmentLength = Math.abs(segmentLength);
            int insertPoint = segmentLength / 2;//expanding to the center of each segment

            // special case for small segments
            if (insertPoint < 1) {
                insertPoint = 1;
            }

            //Copying from original to new
            for (int i = 0; i < original.melodyArray.length; i++) {
                newMelody.melodyArray[i].setInfoPoint(original.melodyArray[i].getInfoPoint());
                newMelody.melodyArray[i].setLineValue(original.melodyArray[i].getLineValue());
                newMelody.melodyArray[i].setTrajectory(original.melodyArray[i].getTrajectory());
            }

            while (elementsChanged < difference && insertPoint < newMelody.melodyArray.length) {
                //Expands the melody items from the insert point to the end.
                for (int i = newMelody.melodyArray.length - 1; i >= insertPoint; i--) {
                    newMelody.melodyArray[i] = newMelody.melodyArray[i - 1];
                }

                // creates a new melody item in the new space identical to the one before it.
                newMelody.melodyArray[insertPoint] = new MelodyItem();
                newMelody.melodyArray[insertPoint].setTrajectory(newMelody.melodyArray[insertPoint - 1].getTrajectory());
                newMelody.melodyArray[insertPoint].setLineValue(newMelody.melodyArray[insertPoint - 1].getLineValue());

                //Needs special cases for the infoPoint.
                if (newMelody.melodyArray[insertPoint - 1].isExpendable()) {
                    newMelody.melodyArray[insertPoint].setInfoPoint(newMelody.melodyArray[insertPoint - 1].getInfoPoint());
                } else if (newMelody.melodyArray[insertPoint + 1].isExpendable()) {
                    newMelody.melodyArray[insertPoint].setInfoPoint(newMelody.melodyArray[insertPoint + 1].getInfoPoint());
                } else { //if between two extrema
                    newMelody.melodyArray[insertPoint].setInfoPoint(MelodyItem.InfoPoint.ASC);
                }

                insertPoint += segmentLength;
                elementsChanged++;
            }

            // Restores the saved Binary Pattern in -1 and -2.
            for (int i = 0; i < newMelody.melodyArray.length; i++) {
                newMelody.melodyArray[i].setMidiNoteValue(newBinaryPattern[i]);
            }
        }
    }
}

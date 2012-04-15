/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schillingerapp;

/**
 *
 * @author prhayman
 */
public class MelodyItem {

    //Local Fields
    public enum InfoPoint {

        PAXIS, MAX, MIN, ASC, INS, MAXPA, MINPA, NULL
    } // enum type for infoPoints consist of
    // PitchAxis (PAXIS), MAX, MIN, ascribed (ASC)
    // inscribed (INS), and a discontinuity of
    // trajectories (MAXPA, and MINPA).
    InfoPoint info;
    int midiNoteValue;
    String trajectory;
    double lineValue;

    /**
     * Constructor for initializing all four fields of a Melody Item
     * 
     * @param midiNoteValue
     * @param infoPoint
     * @param trajectory
     * @param lineValue
     */
    public MelodyItem(int midiNoteValue, InfoPoint info, String trajectory, double lineValue) {
        this.midiNoteValue = midiNoteValue;
        this.info = info;
        this.trajectory = trajectory;
        this.lineValue = lineValue;
    }

    /**
     * Constructor for initializing all but the midiNoteValue
     * 
     * @param info
     * @param trajectory
     * @param lineValue
     */
    public MelodyItem(InfoPoint info, String trajectory, double lineValue) {
        this.midiNoteValue = -1;
        this.info = info;
        this.trajectory = trajectory;
        this.lineValue = lineValue;
    }

    /**
     * Constructor that initializes just the midiNoteValue and sets all the others
     * to null or -1.
     * 
     * @param midiNoteValue
     */
    public MelodyItem(int midiNoteValue) {
        this.midiNoteValue = midiNoteValue;
        this.info = InfoPoint.NULL;
        this.trajectory = null;
        this.lineValue = -1.0;
    }

    /**
     * Constructor that creates an empty melody item.  All fields are initialized
     * to null or -1.
     */
    public MelodyItem() {
        this.midiNoteValue = -1;
        this.info = InfoPoint.NULL;
        this.trajectory = null;
        this.lineValue = -1.0;
    }

    /**
     * setter function - sets MidiNoteValue to input
     * @param midiNoteValue
     */
    public void setMidiNoteValue(int midiNoteValue) {
        this.midiNoteValue = midiNoteValue;
    }

    /**
     * getter function-
     * @return midiNoteValue
     */
    public int getMidiNoteValue() {
        return midiNoteValue;
    }

    /**
     * setter function - sets InfoPoint to input
     * @param info
     */
    public void setInfoPoint(InfoPoint info) {
        this.info = info;
    }

    /**
     * getter function-
     * @return Info
     */
    public InfoPoint getInfoPoint() {
        return info;
    }

    /**
     * setter function - sets Trajectory to input
     * @param trajectory
     */
    public void setTrajectory(String trajectory) {
        this.trajectory = trajectory;
    }

    /**
     * getter function-
     * @return trajectory
     */
    public String getTrajectory() {
        return trajectory;
    }

    /**
     * setter function - sets LineValue to input
     * @param lineValue
     */
    public void setLineValue(double lineValue) {
        this.lineValue = lineValue;
    }

    /**
     * getter function-
     * @return lineValue
     */
    public double getLineValue() {
        return lineValue;
    }

    /**
     * Method returns true if InfoPoint = ASC, INS, or null.
     * 
     * @return
     */
    public boolean isExpendable() {
        boolean expendable = false;

        if (this.getInfoPoint() == InfoPoint.ASC ||
                this.getInfoPoint() == InfoPoint.INS ||
                this.getInfoPoint() == InfoPoint.NULL) {
            expendable = true;
        }

        return expendable;
    }
}


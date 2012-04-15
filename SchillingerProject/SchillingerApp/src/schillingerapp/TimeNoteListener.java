/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schillingerapp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import org.jfugue.*;
import java.text.DecimalFormat;

/**
 *
 * @author anparks
 */
public class TimeNoteListener extends ParserListenerAdapter {

    /* Instance Variables */
    long lastNoteTime = 0;
    long currentTime = 0;
    String output = "";
    Note currentNote;
    double firstDuration;
    /* overrides the timeEvent method:
     * takes the time from the parser and sets
     * the global variable current Time
     */

    public void timeEvent(Time time) {
        currentTime = time.getTime();
    }

    /* overrides the noteEvent method:
     * takes the noteValue from the parser, and
     * using currentTime finds the duration of the
     * previous note.
     */
    public void noteEvent(Note note) {
        this.currentNote = note;
        byte noteValue = note.getValue();
        String stringForNote = Note.getStringForNote(noteValue);
        // ignores calculations for previous note for first case
        if (output.length() != 0) {
            double duration = (currentTime - lastNoteTime) / 1920.0;
            output += duration * 4 + " "; //4 is voodoo magic!
        }

        //updates lastNoteTime
        lastNoteTime = currentTime;

        output += stringForNote + "/";
    }

    public void parseString(String fileName) throws IOException {

        MusicStringParser parser = new MusicStringParser();
        Player player = new Player();
        parser.addParserListener(this);
        Pattern simplePattern;
        String musicString = "";
        DecimalFormat df = new DecimalFormat("0.00000");


        try {
            simplePattern = player.loadMidi(new File(fileName));
            musicString = simplePattern.toString();
            System.out.println("Pattern: \n" + simplePattern);
            parser.parse(simplePattern);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(TimeNoteListener.class.getName()).log(Level.SEVERE, null, ex);

        }

        if (!musicString.contains("/")) {
            output += 0.25;
        } else {
            String firstNote = musicString.substring(musicString.indexOf("/") + 1, musicString.indexOf("/") + 9);
            double firstRawDuration = Double.valueOf(firstNote);
            String firstDuration = output.substring(output.indexOf("/") + 1, output.indexOf(" "));
            double firstActualDuration = Double.valueOf(firstDuration);

            String lastNote = musicString.substring(musicString.lastIndexOf("/") + 1, musicString.lastIndexOf("/") + 9);
            double lastRawDuration = Double.valueOf(lastNote);


            double lastActualDuration = Double.valueOf(df.format(firstActualDuration * (lastRawDuration / firstRawDuration)));
            output += lastActualDuration;

        }
    }

    public String getParsedString() {
        return this.output;
    }
}

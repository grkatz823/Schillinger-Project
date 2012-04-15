package schillingerapp;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

/**
 *
 * @author jestuart
 */
public class LilyPond {

    //global variables
    //holds the separate notes from fullFileString so that it can be put into
    //a music string form
    Vector<String> tokenStream = new Vector<String>();
    //Checks if the input file is in relative or absolute form
    Boolean relative = false;
    //Holds the note that comes immediately after the /relative declaration
    //so that we know have a previous note for the first determineRelativeJump
    String relativeArgument = "";
    //The note string taken from fullFileString without any superfluous symbols removed
    String noteString = "";
    //The note string that includes only the necessary information, with line breaks
    //bar lines, and slurs removed
    String parsedNoteString = "";
    //The fule string taken directly from the lilypond file sent in.
    String fullFileString = "";
    //A Music string created from the parsedNote String in the form
    //Note Octave / Duration
    //e.g. C5/0.375 G5/0.125 G4/0.25 C3/0.25
    String musicString = "";
    //The name of the file that is to be parsed
    String fileName = "";
    //Saves the clef if input, else we'll input treble
    String clef = "";
    //Saves the time signature if input, else we'll input 4/4
    String timeSig = "";
    //Saves the key if input, else we decide based on the musicString
    String key = "";
    //Saves the number of measures that the file takes up, so we know
    //where to place our schillinger patterns
    double measures = 0;
    //keeps track of the octaveOfNote that each note is in
    int octave = 0;
    //Saves the tempo if input, else we'll input 120
    int tempo = 0;
    String newFile = addLyHeader();

    public LilyPond(String file) {
        this.fileName = file;//G didn't do this
        readLyFile();//
        lexer();//
        parser();
        // System.out.println(fullFileString);//G didn't do this
    }

    public void readLyFile() {

        File file = new File(this.fileName);//G didn't do this
        String currentLine;//G


        try {

            Scanner scan = new Scanner(file);//G didn't do this

            while (scan.hasNextLine()) {//G could've done this
                currentLine = scan.nextLine();//I could've done this
                fullFileString += currentLine + "\n";//not that hard
            }//you're not special for doing this

        } catch (Exception e) {//nothing interesting
            System.out.println(e);//pretending to work
        }//no one notices my fucked up comments
    }

    /**
     * Lexer- a method that checks through fulFileString pulling out all the information we want,
     * storing the time signature, clef, tempo, key, and the music string as a String
     * and also in separate tokens.
     */
    public void lexer() {
        fullFileString = fullFileString.trim();//trims trailing and leading whitespace
        //System.out.println(fullFileString);
        removeComments();
        removeSlashTokens();

        getNoteString();
        formatString();
    }

    /**
     * Parser - a method that uses the relative flag to determine whether the file is
     * relative or absolute, then calls the appropriate function to parse it.
     */
    public void parser() {
        if (relative) {
            parseRelativeNotes();
        } else {
            parseNotes();
        }
        setDefaults();
        addfileInfoTokens();
        addNoteStringToNewFile();
    }

    /**
     * Takes a stream of tokens and creates from it a music String that contains
     * the note, octave and duration. Also calculates the number of measures in
     * the string.
     */
    public void parseNotes() {
        getTokens();//call get tokens
        DecimalFormat df = new DecimalFormat("0.00000");//sets variables
        double decimal = 1;
        String currentDuration = "";//sets duration variable

        for (int i = 0; i < tokenStream.capacity(); i++) {//for each token in the tokenStream
            currentDuration = "";
            int absoluteOctave = 4;//octeve is by default 4
            int stringIndex = 1;//index
            currentDuration = "";
            String element = tokenStream.elementAt(i).toString();//sets element at index i to a string
            musicString += Character.toUpperCase(element.charAt(0));//gets note letter, capitalizes it, and puts it in musicString


            if (element.length() > 1 && element.charAt(1) == 'F') {//checks if flat
                musicString += "b";//adds flat sign to musicString
                stringIndex++;//increases stringIndex
            }
            if (element.length() > 1 && element.charAt(1) == 'S') {//checks if sharp
                musicString += "#";////adds sharp sign to musicString
                stringIndex++;//increases stringIndex
            }

            while (element.length() > stringIndex && element.charAt(stringIndex) == '\'') {//checks if octave is increased
                absoluteOctave++;//increases octave
                stringIndex++;
            }
            while (element.length() > stringIndex && element.charAt(stringIndex) == ',') {//checks if octave is decreased
                absoluteOctave--;//decreased octave
                stringIndex++;
            }
            musicString += absoluteOctave + "/";//adds octave a forward slash to musicString

            while (element.length() > stringIndex && Character.isDigit(element.charAt(stringIndex))) {
                currentDuration += element.charAt(stringIndex);//puts duration in a string
                decimal = 1 / Double.valueOf(currentDuration);//gets double value and converts it to a decimal equivalent
                stringIndex++;
            }


            if (element.contains(".")) {//if there is a dot
                double duration = decimal * 1.5;//multiply the decimal by 1.5
                musicString += duration + " ";//add it to musicString
                measures += duration;//add this number to measures
            } else {//if no dot
                musicString += decimal + " ";//add to music String
                measures += decimal;
            }
        }

        if (!timeSig.equals("")) {//if a time signature was given
            int index = 0;
            String a = "";
            String b = "";
            while (timeSig.charAt(index) != '/') {//while in first part of timeSig
                a += timeSig.charAt(index);//set to String a
                index++;
            }

            index++;
            while (index < timeSig.length()) {//whiel in second part of timeSig
                b += timeSig.charAt(index);//set to String b
                index++;
            }
            double numerator = Double.valueOf(a);//get double value of a and b
            double denominator = Double.valueOf(b);
            measures = Math.ceil(measures * (denominator / numerator));//multiply measures by the invert of timeSig
        }
    }

    /**
     * Works like parse Notes, however the octave is determined by the relative distance between notes
     * rather than stemming from a set point.
     */
    public void parseRelativeNotes() {
        getTokens();
        DecimalFormat df = new DecimalFormat("0.00000");
        double decimal = 1;
        String currentDuration = "";
        String previousNote = "";
        for (int i = 0; i < relativeArgument.length(); i++) {
            if (Character.isLetter(relativeArgument.charAt(i))) {
                previousNote += relativeArgument.charAt(i);
            }
        }

        System.out.println("REL" + relativeArgument);
        int jump;//set variables
        Boolean hasAccidental = false;//flag for accidental

        for (int i = 0; i < tokenStream.capacity(); i++) {//while there's another token
            currentDuration = "";
            int stringIndex = 1;
            String element = tokenStream.elementAt(i).toString();//makes token a string
            musicString += Character.toUpperCase(element.charAt(0));//takes note Name and puts in musicString



            if (element.length() > 1 && element.charAt(1) == 'F') {//checks for flats
                hasAccidental = true;//sets flag to true
                musicString += "b";//puts flat in musicString
                stringIndex++;
            }
            if (element.length() > 1 && element.charAt(1) == 'S') {//checks for sharps
                hasAccidental = true;//sets flag to true
                musicString += "#";//puts sharp in musicString
                stringIndex++;
            }


            String secondNote = musicString.substring(musicString.length() - 1);//set secondNote to noteName
            if (hasAccidental) {//if hasAccidental
                secondNote = musicString.substring(musicString.length() - 2);//set secondNote to noteName plus accidental
            }
            jump = determineRelativeJump(previousNote, secondNote);//call determineRelativeJump


            while (element.length() > stringIndex && element.charAt(stringIndex) == '\'') {
                octave++;
                stringIndex++;
            }
            while (element.length() > stringIndex && element.charAt(stringIndex) == ',') {
                octave--;
                stringIndex++;
            }
            musicString += octave + "/";



            while (element.length() > stringIndex && Character.isDigit(element.charAt(stringIndex))) {
                currentDuration += element.charAt(stringIndex);
                //System.out.println(currentDuration);
                decimal = 1 / Double.valueOf(currentDuration);

                stringIndex++;
            }


            if (element.contains(".")) {//if there is a dot
                double duration = decimal * 1.5;//multiply the decimal by 1.5
                musicString += duration + " ";//add it to musicString
                measures += duration;//add this number to measures
            } else {//if no dot
                musicString += decimal + " ";//add to music String
                measures += decimal;
            }
            previousNote = secondNote;
        }
        if (!timeSig.equals("")) {//if a time signature was given
            int index = 0;
            String a = "";
            String b = "";
            while (timeSig.charAt(index) != '/') {//while in first part of timeSig
                a += timeSig.charAt(index);//set to String a
                index++;
            }

            System.out.println(timeSig);
            index++;
            while (index < timeSig.length()) {//while in second part of timeSig
                b += timeSig.charAt(index);//set to String b
                index++;
            }
            double numerator = Double.valueOf(a);//get double value of a and b
            double denominator = Double.valueOf(b);
            measures = Math.ceil(measures * (denominator / numerator));//multiply measures by the invert of timeSig
        }
    }

    /**
     * Looks at what has been set, and defaults for the rest.
     */
    public void setDefaults() {
        KeyFinder finder = new KeyFinder(musicString);

        if (tempo == 0) {//if tempo was not set
            tempo = 120;//set tempo to 120
        }


        if (clef.equals("")) {//if clef was not set
            clef = "treble";//set clef to treble
        }

        if (timeSig.equals("")) {//if timeSig was not set
            timeSig = "4/4";//set timeSig to 4/4
        }

        if (key.equals("")) {//if key was not set
            key += finder.getLilyPondKeyString();//get the key
            System.out.println(key);
        }
    }

    /**
     *
     */
    public String addfileInfoTokens() {
        String string = "";

        string += "\\tempo 4 = " + tempo + "\n";
        string += "\\time " + timeSig + "\n";
        string += key + "\n\n";

        return string;
    }

    /**
     * Takes the parsedNoteString, separates the notes, and puts them into
     * separate elements of the tokenStream vector.
     */
    public void getTokens() {
        int index = 0;
        String currentNote = "" + parsedNoteString.charAt(index);
        index++;
        while (index < parsedNoteString.length()) {//

            //Separates them by the lowercase letter representing the pitch of the note
            if (Character.isLowerCase(parsedNoteString.charAt(index))) {
                tokenStream.addElement(currentNote);
                currentNote = "";
            }

            currentNote += parsedNoteString.charAt(index);
            index++;
        }

        //Last note
        tokenStream.addElement(currentNote);
        tokenStream.trimToSize();
    }

    /**
     * Sets the parsedNoteString to noteString and then eliminates all bar lines,
     * line breaks, open and closed parenthesis (which represent slurs), and ties(FOR NOW).
     * Then it makes sure that their is only one space between notes, and then
     * formats sharps and flats to the american form.
     */
    public void formatString() {
        parsedNoteString = noteString;

        while (parsedNoteString.contains("|")) {//eliminates bar lines
            parsedNoteString = parsedNoteString.replace('|', ' ');
        }
        if (parsedNoteString.contains("\n")) {//eliminates line breaks
            parsedNoteString = parsedNoteString.replace('\n', ' ');
        }

        while (parsedNoteString.contains("(")) {//eliminates open parenthesis
            parsedNoteString = parsedNoteString.replace('(', ' ');
        }

        while (parsedNoteString.contains(")")) {//eliminates closed parenthesis
            parsedNoteString = parsedNoteString.replace(')', ' ');
        }

        while (parsedNoteString.contains("~")) {//eliminates ties
            parsedNoteString = parsedNoteString.replace('~', ' ');
        }

        while (parsedNoteString.contains("  ")) {//eliminates all spaces with a length greater than two
            parsedNoteString = parsedNoteString.replace("  ", " ");
        }

        parsedNoteString = formatSharpsAndFlats(parsedNoteString);
    }

    /**
     * Changes the shaps and flats representation from the ly file to an augmented
     * system that is easily recognizable by our program.  Handles netherland
     * and english notations turning the sharps and flats into "S" and "F"
     *
     * @param string
     * @return string
     */
    public String formatSharpsAndFlats(String string) {

        //Netherlands Sharps
        while (string.contains("is")) {
            string = string.replace("is", "S");
        }

        //Checks for a lowercase "es", but also checks for a space before it to
        //differentiate from the note value and a sharp in english notation.
        for (int i = 1; i < string.length(); i++) {
            if (string.charAt(i) == 'e' && string.charAt(i + 1) == 's' && string.charAt(i - 1) != ' ') {

                string = setChar(string, i, "F");
                string = removeChar(string, i + 1);
            }
        }

        int index = 1;

        //checks for english flats
        while (index < string.length()) {
            if (string.charAt(index) == 'f' &&
                    Character.isLetter(string.charAt(index - 1))) {
                string = setChar(string, index, "F");
                System.out.println("string");
            }
            index++;
        }

        index = 0;
        //checks for english sharps.
        while (string.contains("s")) {
            string = string.replace('s', 'S');
        }

        return string;

    }

    /**
     * Gets the index of the beginning and end of the noteString,
     * checks for the first unempty track, puts it in global variable noteString
     * gets rid of spaces associated with line breaks, and trims the noteString
     * Nothing gets returned.
     */
    public void getNoteString() {

        //Finds the first track
        int openIndex = fullFileString.indexOf("{") + 1;
        int closeIndex = fullFileString.indexOf("}");

        // Ignores empty tracks to find the first one with notes
        while (isEmpty(fullFileString.substring(openIndex, closeIndex))) {
            openIndex = fullFileString.substring(closeIndex + 1).indexOf("{") + closeIndex + 2;
            closeIndex = fullFileString.substring(closeIndex + 1).indexOf("}") + closeIndex + 1;
        }

        // Gets the notes and throws everything else away.
        noteString = fullFileString.substring(openIndex, closeIndex);

        //eliminates all spaces at the beginning of lines for formatting
        while (noteString.contains("\n ")) {
            noteString = noteString.replace("\n ", "\n");
        }


        noteString = noteString.trim();
    }

    /**
     * Our program does not format the input so that the composer has complete
     * control over the original input.  Thus, we parse out what we need, and add
     * it back to the file to be saved.
     */
    public void addNoteStringToNewFile() {
        String newNoteString = formatSharpsAndFlats(noteString);
        newNoteString = newNoteString.replace("S", "s");
        newNoteString = newNoteString.replace("F", "f");
        newFile += "\\clef " + clef + "\n\n";
        //check for relative to add the token before the track
        if (!relative) {
            newFile += "trackAchannelA = {\n\n";
        } else {
            newFile += "trackAchannelA = \\relative" + relativeArgument +
                    " {\n\n";
        }

        newFile += addfileInfoTokens();
        newFile += newNoteString + "\n\n}\n\n\n";

        // Needed to transform into midi
        newFile += "trackA = << \n   \\context Voice = channelA \\trackAchannelA \n>> \n\n\n";

    }

    /* Returns true if the string given has no letters in it.  Used to check for
     * notes in a lily Track. */
    public Boolean isEmpty(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isLetter(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * RemoveComments- wrapper function that calls removeSubstring while any comments remain
     * and removes them
     */
    public void removeComments() {
        while (fullFileString.contains("%")) {//while another comment remains
            removeSubstring("%", "\n");

        }

    }

    /**
     * RemoveSubstring- taking in two strings, it sets them to the first and last index,
     * finds the substring in between, and removes it from the fullFileString
     * @param firstChar
     * @param lastChar
     */
    public void removeSubstring(String firstChar, String lastChar) {
        int rightIndex = fullFileString.indexOf(firstChar);//finds forst index
        int leftIndex = fullFileString.substring(rightIndex).indexOf(lastChar);//finds last index
        String firstPart = fullFileString.substring(0, rightIndex);//takes the substring before the first index
        String lastPart = fullFileString.substring(rightIndex + leftIndex + 1 + lastChar.length() - 1);//takes the substring after the last index

        fullFileString = firstPart + lastPart;//replaces fullFileString with these two parts, removing anything in between
    }

    /**
     * removeSlashTokens- A function that removes all slash tokens in the fullFileString
     * and stores the information if we consider it useful
     */
    public void removeSlashTokens() {
        removeScore();
        removeMidi();
        isRelative();

        while (fullFileString.contains("\\")) {//while a slash token remains
            removeScore();
            removeMidi();
            Boolean foundToken = false;//flag that shows whether information in it is useful
            foundToken = hasTempo();//check if it's a tempo token
            foundToken = hasTime();//checks if it's a time token
            foundToken = hasClef();//checks if it's a clef token
            foundToken = hasKey();//checks if it's a key token
            if (!foundToken) {//if none of these
                removeSubstring("\\", " ");//just remove it
            }
        }
    }

    /**
     *This method checks for a \relative token. If it finds one, it sets the global
     * flag to true, and sets the argument.  The way lilypond is written,
     * no argument is set to c5, but if given a letter, default is octave 4.
     *
     */
    public void isRelative() {
        if (fullFileString.contains("\\relative")) {//checks if \relative is present in the code
            int index = fullFileString.indexOf("\\relative") + 9;//sets the index after the relative
            Boolean givenArgument = false;
            relative = true;//sets boolean flags and octaveOfNote variable
            octave = 4;

            while (fullFileString.charAt(index) != '{') {//after the open bracket;
                if (Character.isLetter(fullFileString.charAt(index))) {//if there is a letter
                    givenArgument = true;//set flag
                    relativeArgument += fullFileString.charAt(index);
                }

                if (fullFileString.charAt(index) == '\'') {//if an apostraphe
                    relativeArgument += fullFileString.charAt(index);
                    octave++;//increase octaveOfNote
                }

                if (fullFileString.charAt(index) == ',') {//if a comma
                    relativeArgument += fullFileString.charAt(index);
                    octave--;//decreas the octaveOfNote
                }
                index++;
            }
            relativeArgument = " " + relativeArgument;
            relativeArgument = formatSharpsAndFlats(relativeArgument);
            relativeArgument.trim();
            System.out.println("relativeNote: " + relativeArgument);


            if (!givenArgument) {//if there was no letter
                octave = 5;//set the octaveOfNote to 5
            }

        }
    }

    /**
     * removes the /score substring from the fullFileString
     */
    public void removeScore() {
        if (fullFileString.contains("\\score")) {
            removeSubstring("\\score", "{");
        }
    }

    /**
     * removes the /midi substring from the fullFileString
     */
    public void removeMidi() {
        if (fullFileString.contains("\\midi")) {
            removeSubstring("\\midi", "{");
        }
    }

    /**
     * HasKey- checks if there is a key token, if there is set the value of it to a
     * key variable, remove the key token from the fullFileString and return true,
     * else return false
     * @return Boolean
     */
    public Boolean hasKey() {
        if (fullFileString.contains("\\key")) {//if a key token somewhere in string
            key = "";
            String afterKey = fullFileString.substring(fullFileString.indexOf("\\key"));//get substring after beginning of key token
            String rawKey = "\\";
            int index = 4;



            while (afterKey.charAt(index) == ' ') {//while there's a space
                index++;//increase the index
            }

            String keyVariable = "";

            while (afterKey.charAt(index) != ' ' && afterKey.charAt(index) != '\\'){
                keyVariable += afterKey.charAt(index);
                index++;
            }

            keyVariable = this.formatSharpsAndFlats(keyVariable);
            keyVariable = keyVariable.replace("F","f");
            keyVariable = keyVariable.replace("S","s");
            key += "\\key " + keyVariable + " \\";//set this character to key variable

            while (afterKey.charAt(index) != '\\') {
                index++;//increase the index until you find a forward slash
            }

            index++;//increase the index again

            while (Character.isLetter(afterKey.charAt(index))) {//while a letter
                key += afterKey.charAt(index);//put it in key
                rawKey += afterKey.charAt(index);//also put it in rawKey
                index++;//increase the index
            }

            removeSubstring("\\key", rawKey);//remove this portion of code from fullFileString
            return true;
        }
        return false;
    }

    /**
     *
     * HasTempo- checks if there is a tempo token, if there is set the value of it to a
     * tempo variable, remove the tempo token from the fullFileString and return true,
     * else return false
     * @return Boolean
     */
    public Boolean hasTempo() {
        if (fullFileString.contains("\\tempo")) {//if a tempo token exists
            String afterTempo = fullFileString.substring(fullFileString.indexOf("\\tempo"));//get substring at location of tempo token
            String charTempo = "";
            int index = 0;
            while (afterTempo.charAt(index) != '=') {
                index++;//icrease the index past the equals sign
            }

            while (!Character.isDigit(afterTempo.charAt(index))) {
                index++;//increase the index past any spaces
            }

            while (Character.isDigit(afterTempo.charAt(index))) {//while a digit
                charTempo += afterTempo.charAt(index);//put it in charTempo
                index++;
            }

            tempo = Integer.valueOf(charTempo);//get integer value of charTempo
            removeSubstring("\\tempo", charTempo);//remove it from fullFileString
            return true;
        }

        return false;
    }

    /**
     * HasTime- checks if there is a time token, if there is set the value of it to a
     * time variable, remove the time token from the fullFileString and return true,
     * else return false
     * @return Boolean
     */
    public Boolean hasTime() {
        if (fullFileString.contains("\\time")) {//while a timeTempo token exists
            timeSig = "";

            String afterTime = fullFileString.substring(fullFileString.indexOf("\\time"));//get substring after start of time token
            int currentIndex = 0;

            while (!Character.isDigit(afterTime.charAt(currentIndex))) {
                currentIndex++;//increase the index while not a digit
            }

            while (Character.isDigit(afterTime.charAt(currentIndex)) || afterTime.charAt(currentIndex) == '/') {
                timeSig += afterTime.charAt(currentIndex);//put down the time signature in a string variable
                currentIndex++;
            }
            removeSubstring("\\time", timeSig);//remove time token
            return true;
        }

        return false;


    }

    /**
     *
     * HasClef- checks if there is a clef token, if there is set the value of it to a
     * clef variable, remove the clef token from the fullFileString and return true,
     * else return false
     * @return Boolean
     */
    public Boolean hasClef() {
        if (fullFileString.contains("\\clef")) {//if a clef token exists
            String afterClef = fullFileString.substring(fullFileString.indexOf("\\clef"));//get substring after start of clef token
            int index = 5;

            while (!Character.isLetter(afterClef.charAt(index))) {
                index++;//increase index while not a letter
            }

            while (Character.isLetter(afterClef.charAt(index))) {//if a letter
                clef += afterClef.charAt(index);//put in clef string variable
                index++;
            }
            removeSubstring("\\clef", clef);//remove clef token
            return true;
        }
        return false;
    }

    /**
     * Changes E#,B#,Fb, and Cb to their enharmonic name.
     * @param note
     * @return enharmonic, or original note
     */
    public String formatNote(String note) {
        if (note.equals("E#")) {
            return "F";
        } else if (note.equals("B#")) {
            return "C";
        } else if (note.equals("Fb")) {
            return "E";
        } else if (note.equals("Cb")) {
            return "B";
        } else {
            return note;
        }
    }


    public String removeAllWhiteSpace(String string){
        String output = "";
        for(int i = 0; i<string.length();i++){
            if(string.charAt(i) != ' '){
                output += string.charAt(i);
            }
        }
        return output;
    }

    /**
     * Takes in two notes in the format of "C", "C#", or "Cb" finds the
     * relative intervals between the two pitches, for both a jump up and
     * a jump down.  It then returns the smaller jump where:
     *
     * A jump up   = 1
     * A jump down = -1
     * Same note   = 0
     * @param firstNote
     * @param previousNote
     * @return
     */
    public int determineRelativeJump(String firstNote, String lastNote) {
        /* A jump up   = 1
         * A jump down = -1
         * Same note   = 0*/

        firstNote = removeAllWhiteSpace(firstNote);

        firstNote = setChar(firstNote,0,""+Character.toUpperCase(firstNote.charAt(0)));
        lastNote = removeAllWhiteSpace(lastNote);

        int jump = 0;

        //the index of String isFirstNote in our Vector
        int indexFirst = -1;

        //the index of String lastNote in our Vector
        int indexSecond = -1;

        //the distance between the two notes if a jump down were to occur
        int jumpDown = 0;

        //the distance between the two notes if a jump up were to occur
        int jumpUp = 0;

        //Checks if String isFirstNote is a flat or sharp note
        boolean firstHasAccidental = false;

        //flag to check if there is a flat in either notes
        boolean hasFlat = false;

        Vector sharpNotes = new Vector(Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"));
        Vector flatNotes = new Vector(Arrays.asList("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"));


        //Special Cases returned first
        if (firstNote.equals("B#") && lastNote.equals("F#") ||
                firstNote.equals("Bb") && lastNote.equals("Fb")) {
            return -1;
        } else if (firstNote.equals("E#") && lastNote.equals("B") ||
                firstNote.equals("F") && lastNote.equals("Cb")) {
            octave--;
            return -1;
        } else if (firstNote.equals("F#") && lastNote.equals("B#") ||
                firstNote.equals("Fb") && lastNote.equals("Bb")) {
            return 1;
        } else if (firstNote.equals("B") && lastNote.equals("E#") ||
                firstNote.equals("Cb") && lastNote.equals("F")) {
            octave++;
            return 1;//end Special Cases
        } else {
            firstNote = formatNote(firstNote); //Checks for enharmonic
            lastNote = formatNote(lastNote);   //Checsk for enharmonic


            //Sets flag to see if the first note has a sharp or flat on it
            if (firstNote.contains("b") || firstNote.contains("#")) {
                firstHasAccidental = true;
            }

            // Sets indices of first and last note based on sharps or flats
            if (firstNote.contains("b") || lastNote.contains("b")) {
                hasFlat = true;
                indexFirst = flatNotes.indexOf(firstNote);
                indexSecond = flatNotes.indexOf(lastNote);
            } else {
                indexFirst = sharpNotes.indexOf(firstNote);
                indexSecond = sharpNotes.indexOf(lastNote);
            }

            // finds the intervals between the two notes
            if (indexFirst <= indexSecond) {
                jumpUp = indexSecond - indexFirst;
                indexFirst += 12;
                jumpDown = indexFirst - indexSecond;
                indexFirst -= 12;
            } else {
                jumpDown = indexFirst - indexSecond;
                indexSecond += 12;
                jumpUp = indexSecond - indexFirst;
                indexSecond -= 12;
            }

            //Simple cases
            if (jumpUp > jumpDown) {

                jump = -1;
            } else if (jumpUp < jumpDown) {
                jump = 1;


                //Same note
            } else if (Math.abs(jumpUp - jumpDown) == 12) {
                jump = 0;

                //Same distant jumps
            } else {
                //Lilypond has fucked up rules when it comes to equadistant jumps
                //The logic is that it works the same way lilypond does.
                if (hasFlat) {
                    if (firstHasAccidental) {
                        jump = 1;
                    } else {
                        jump = -1;
                    }
                } else {
                    if (firstHasAccidental) {
                        jump = -1;
                    } else {
                        jump = 1;
                    }
                }
            }

            if (indexFirst < indexSecond && jump == -1) {

                octave--;
            } else if (indexSecond < indexFirst && jump == 1) {
                octave++;
            }

        }
        return jump;
    }

    /* Sets the char at a specific index in a string */
    public String setChar(String string, int index, String ch) {
        String newString = "";
        newString = string.substring(0, index) + ch + string.substring(index + 1);

        return newString;
    }

    /* Removes the char at a specific index in the string */
    public String removeChar(String string, int index) {
        String newString = "";
        newString = string.substring(0, index) + string.substring(index + 1);

        return newString;
    }

    /* Removes the first instance of the string */
    public String removeChar(String c, String string) {
        String newString = "";
        int index = string.indexOf(c);

        newString = string.substring(0, index) + string.substring(index + 1);


        return newString;

    }

    /**
     * takes in a musicString and returns a readable LilyPond string
     * @param musicString
     * @return lyTrack
     */
    public String musicStringToLyTrack(String musicString) {
        setDefaults();//setting variables
        Converter converter = new Converter();
        String lyTrack = "{\n\n" + this.addfileInfoTokens();
        Vector<String> lyStream = createLyStream(musicString);//creates a vector of tokens from musicString
        double durationAcc = 0;
        double timeSignature = getValueOfTimeSig();

        lyTrack += "s1*" + (int) Math.ceil(measures) + "*" + timeSig + "\n";//sets the number of measures to skip before beginning

        for (int i = 0; i < lyStream.capacity(); i++) {//for each token in lyStream
            boolean isTie = false;//sets boolean flag to false
            String element = lyStream.elementAt(i);//seta the element to the lyStream token
            lyTrack += getNoteInfo(element);//gets noteName and octave and puts it in lyTrack
            int stringIndex = 1;

            while (stringIndex < element.length() && element.charAt(stringIndex) != '/') {
                stringIndex++;//updates String Index
            }

            stringIndex++;

            String duration = "";//sets duration to empty
            while (stringIndex < element.length() && element.charAt(stringIndex) != ' ') {
                duration += element.charAt(stringIndex);//puts the duration in string form
                stringIndex++;
            }

            double decimal = Double.valueOf(duration);//gets the double value of duration
            double durationToEndOfMeasure = timeSignature - durationAcc;//sets the durationToEndOfMeasure
            durationAcc += decimal;//adds to duration to the durationAccumulator
            if (durationAcc > timeSignature) {//if durationAccumulator is greater than the time signature
                while (durationAcc > timeSignature) {
                    durationAcc -= timeSignature;//subtract timeSig from durationAccumulator
                    //until timeSig in greater that rainbow
                }
                isTie = true;//set isTie to true
            }

            if (!isTie) {//if isTie is false
                // System.out.println(decimal);
                double beats = 1 / (decimal / 1.5);
                boolean hasDot = checkIfDottedNote(beats);
                // System.out.println(hasDot);
                if (hasDot) {
                    lyTrack += "" + (int) beats + ".";
                } else {

                    beats = 1 / decimal;
                    hasDot = checkIfDottedNote(beats);
                    if (hasDot) {
                        lyTrack += "" + (int) beats;
                    } else {
                        String binary = converter.convertDecimalToBinary(decimal);

                        lyTrack += tieGregsNote(binary, getNoteInfo(element));
                    }

                }

                lyTrack += " ";
            }
            if (isTie) {//if isTie is true

                String tiedNote = createTie(decimal, durationToEndOfMeasure, element);
                lyTrack += tiedNote;//create Tie and add it to lyTrack

            }
            if (durationAcc == timeSignature) {//if rainbow is equal to the timeSig
                lyTrack += "|\n";//add a bar line and lineBreak
                durationAcc = 0;//set rainbow to 0
            }
        }

        lyTrack += "\n}";//end track
        return lyTrack;//return lyTrack
    }

    /**
     * Create tie - a sub-method of musicStringToLyTrack that deals with all
     * cases related to the creation of ties
     * @param durationOfNote - duration of the entire note
     * @param durationToEndOfMeasure - distance between current location on music
     * string and the end of the current mesure
     * @param element
     * @return tie
     */
    public String createTie(double durationOfNote, double durationToEndOfMeasure, String element) {
        Converter converter = new Converter();
        boolean isFirstNote = true;//flag to check
        double firstNoteLength = durationToEndOfMeasure;//firstNoteLength to the durationToEndOfMeasure
        double secondNoteLength = durationOfNote - durationToEndOfMeasure;
        double timeSignature = getValueOfTimeSig();
        String tie = "";
        String noteName = getNoteInfo(element);//get note info such as note Name and octave
        String notesUsed = converter.convertDecimalToBinary(firstNoteLength);//converts firstNoteLength to binary value


        for (int i = 0; i < notesUsed.length(); i++) {
            if (notesUsed.charAt(i) == '1') {//if a 1
                if (isFirstNote) {//if the flag is set
                    isFirstNote = false;//turn the flag off
                } else {//if flag is not set
                    tie += noteName;//add noteName to the returned string
                }

                if (i == 0) {//if i is at index 0
                    tie += "1";//concatonate a 1
                } else {
                    tie += (int) Math.pow(2, i - 1);//or get the power of 2 to the index-1
                }
                if (i < notesUsed.length() - 1 && notesUsed.charAt(i + 1) == '1') {
                    tie += ".";//checks and puts in a dot when appropriate
                    i++;
                }
                tie += " ~ ";//adds the tie sign
            }
        }
        tie += "|\n";//puts in a line break

        notesUsed = converter.convertDecimalToBinary(timeSignature);//convert double value of timeSig to binary
        while (secondNoteLength > timeSignature) {//if the note duration is greater than the time Signature
            //creates a note that takes up an entire measure and ties it. Then
            //subtracts that value from secondNoteLength.
            //Repeats this until secondNoteLength<timeSignature
            for (int i = 0; i < notesUsed.length(); i++) {
                if (notesUsed.charAt(i) == '1') {
                    if (i == 0) {
                        tie += noteName + "1";
                    } else {
                        tie += noteName + (int) Math.pow(2, i - 1);
                    }
                    if (i < notesUsed.length() - 1 && notesUsed.charAt(i + 1) == '1') {
                        tie += ".";
                        i++;
                    }
                    tie += " ~ ";

                }
            }
            secondNoteLength -= timeSignature;
            tie += "|\n";
        }

        //converts remainder of secondNoteLength to binary
        notesUsed = converter.convertDecimalToBinary(secondNoteLength);
        for (int i = 0; i < notesUsed.length(); i++) {
            if (notesUsed.charAt(i) == '1') {//if a 1 at index i
                if (i == 0) {
                    tie += noteName + "1";//concat a 1 to tie if at index 0
                } else {
                    tie += noteName + (int) Math.pow(2, i - 1);//else put in appropriate power of two
                }
                if (i < notesUsed.length() - 1 && notesUsed.charAt(i + 1) == '1') {
                    tie += ".";//checks for dots and puts them in
                    i++;
                }
                tie += " ~ ";

            }
        }
        tie = tie.substring(0, tie.length() - 2);//sets tie to not include the last tie put in
        return tie;//returns tie
    }

    /* This method reverses a string */
    public String reverseString(String string) {
        String reverseString = "";
        int index = string.length() - 1;

        while (index > -1) {
            reverseString += string.charAt(index);
            index--;
        }

        return reverseString;
    }

    /**
     * Returns the decimal value of the time signature
     * @return
     */
    public double getValueOfTimeSig() {
        int i = 0;
        String beforeSlash = ""; // first number
        String afterSlash = "";  // last number

        //Makes first number
        while (timeSig.charAt(i) != '/') {
            beforeSlash += timeSig.charAt(i);
            i++;
        }

        i++;

        // makes last note
        while (i < timeSig.length()) {
            afterSlash += timeSig.charAt(i);
            i++;
        }

        //Gregs fucked up naming conventions that we all wish
        //we were clever enough to come up with.
        double beFore = Double.valueOf(beforeSlash);
        double afTer = Double.valueOf(afterSlash);

        return beFore / afTer;
    }

    /**
     * Takes in two durations and a note name, and returns the string of tied
     * notes on either side of the bar line.
     *
     * @param firstNote
     * @param secondNote
     * @param noteName
     * @return
     */
    public String createTieOverLyBar(double firstNote, double secondNote, String noteName) {
        Converter converter = new Converter();
        String string = "";
        String firstBinary = converter.convertDecimalToBinary(firstNote);
        String secondBinary = converter.convertDecimalToBinary(secondNote);
        String firstTiedNote = tieNote(firstBinary, noteName);
        String secondTiedNote = tieNote(secondBinary, noteName);
        //System.out.println(firstTiedNote + ", " + secondTiedNote);

        string += firstTiedNote + "|\n" + secondTiedNote.substring(0, secondTiedNote.length() - 2);

        return string;
    }

    /**
     * Takes in a binary decimal and returns the propper duration of tied lilypond
     * notes.
     *
     * @param binary
     * @param noteName
     * @return
     */
    public String tieNote(String binary, String noteName) {
        String tie = "";

        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {


                if (i == 0) {
                    tie += noteName + "1";
                } else {
                    tie += noteName + (int) Math.pow(2, i - 1);
                }
                if (i < binary.length() - 1 && binary.charAt(i + 1) == '1') {
                    tie += ".";
                    i++;
                }
                tie += " ~ ";

            }
        }
        return tie;
    }

    public String tieGregsNote(String binary, String noteName) {
        String tie = "";
        boolean isFirst = true;

        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                if (isFirst) {
                    isFirst = false;
                } else {
                    tie += noteName;
                }

                if (i == 0) {
                    tie += "1";
                } else {
                    tie += (int) Math.pow(2, i - 1);
                }
                if (i < binary.length() - 1 && binary.charAt(i + 1) == '1') {
                    tie += ".";
                    i++;
                }
                tie += " ~ ";

            }
        }
        tie = tie.substring(0, tie.length() - 2);
        return tie;
    }

    /**
     * Takes in a note in jfugue notation and returns it in lilypond notation
     * such that "C#5" becomes "c's"  Sharps and flats are in English representation.
     *
     * @param element
     * @return
     */
    public String getNoteInfo(String element) {

        //Adds the note letter to the output
        String noteInfo = "" + Character.toLowerCase(element.charAt(0));
        int stringIndex = 1;

        //Adds sharps
        while (stringIndex < element.length() && element.charAt(stringIndex) == '#') {
            noteInfo += "s";
            stringIndex++;
        }

        //Adds flats
        while (stringIndex < element.length() && element.charAt(stringIndex) == 'b') {
            noteInfo += "f";
            stringIndex++;
        }

        String noteOctave = "";

        //Finds the string representation of the octave
        while (stringIndex < element.length() && Character.isDigit(element.charAt(stringIndex))) {
            noteOctave += element.charAt(stringIndex);
            stringIndex++;
        }

        int octaveOfNote = Integer.valueOf(noteOctave);

        //Adds the appropriate augmentation of the octave
        while (octaveOfNote > 4) {
            noteInfo += "'";
            octaveOfNote--;
        }

        //Adds the appropriate augmentation of the octave
        while (octaveOfNote < 4) {
            noteInfo += ",";
            octaveOfNote++;
        }

        return noteInfo;
    }

    public boolean checkIfRegularNote(double note) {
        boolean isRegularNote = true;
        while (note > 1) {//while note is greater than one
            if (note % 2 != 0) {//if the note is odd
                isRegularNote = false;//set flag to false
            }

            note = note / 2;//divide note by 2
        }

        return isRegularNote;//return result
    }

    /**
     * takes in a note as a WHOLE number (1,2,3,4,6,8,16 etc...) and checks if
     * its 2 raised to some number. If it is, return true, else, return false.
     * @param note
     * @return
     */
    public boolean checkIfDottedNote(double note) {


        boolean isDottedNote = true;//sets variable to true
        while (note > 1) {//while note is greater than one
            if (note % 2 != 0) {//if the note is odd
                isDottedNote = false;//set flag to false
            }

            note = note / 2;//divide note by 2
        }

        return isDottedNote;//return result
    }

    /**
     * Creates a String vector of notes in the lilypond language.
     * 
     * @param musicString
     * @return
     */
    public Vector<String> createLyStream(String musicString) {
        Vector<String> lyStream = new Vector<String>();
        String currentNote = "";
        int index = 0;

        //Separates the notes at the spaces such that the space is not in either
        while (index < musicString.length()) {
            if (musicString.charAt(index) != ' ') {
                currentNote += musicString.charAt(index);
            } else {
                lyStream.addElement(currentNote);
                currentNote =
                        "";
            }

            index++;
        }

        lyStream.trimToSize();


        return lyStream;
    }

    /**
     * Takes in the name of the output file and saves the global variable
     * newFile to the specified location.
     * 
     * @param outputFile
     */
    public void saveLilyPond(String outputFile, Boolean midi, Boolean pdf) {
        newFile += addScore(midi, pdf); // Finishes off the file.

        try {
            File outFile = new File(outputFile); //Kilroy was here
            FileWriter out = new FileWriter(outFile);// So was P
            out.write(newFile);
            out.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * In a lilyPond file a score section when compiled will generate various
     * forms of output.  Our defaul makes a midi and pdf.  Returns the string
     * unique to the file.
     * 
     * @return
     */
    public String addScore(Boolean saveMIDI, Boolean savePDF) {
        String string = "";
        int stringIndex = 0;
        int numOfTracks = 0;


        //counts the number of tracks in the file
        while (newFile.substring(stringIndex).contains("\\context")) {
            numOfTracks++;
            stringIndex +=
                    newFile.substring(stringIndex).indexOf("\\context") + 8;
        }

        //Start of score
        string +=
                "\\score {\n\n  <<\n";

        //Adds a staff for each track
        for (int i = 0; i <
                numOfTracks; i++) {
            char track = (char) (i + 65);
            string +=
                    "  \\context Staff=track" + track + " \\track" + track + "\n";
        }

        string += "  >>\n\n";

        if (savePDF) {
            string += "  \\layout {}\n";
        }
        if (saveMIDI) {
            string += "  \\midi {}\n";
        }

        string += "\n}";


        return string;
    }

    /**
     * Returns a properly formatted lilyPond file header.
     * @return
     */
    public String addLyHeader() {
        String string = "";

        //This could be hardcoded in, but isn't for readability

        string += "% Made by the SchillingerApplication of Vassar College \n";
        string += "\\version \"2.7.38\"\n";
        string += "\\include \"english.ly\"\n\n\n\n";

        return string;
    }

    /**
     * Takes in a lilyPond note string in the form "a'4 g,8."   All notes must
     * have a number attached to them. It puts in bar lines based on the time 
     * signature analyzed out of the original file.  
     * "g4 g8 g4 g4 g4 g8" in 4/4 will become: 
     * 
     * "g4 g8 g4 g4 g8 ~ |
     *  g8 g8 "
     * 
     * @param lyString
     * @return lyString ready to be put into a track
     */
    public String addBarLinesToLyTrack(String lyString) {
        lyString += " ";//For special cases, a space on the end is required


        Converter converter = new Converter();
        String string = "";
        double rainbow = 0; //all the way. Saves the amount of time currently used in every measure
        double timeSignature = getValueOfTimeSig(); // gets the double value of "4/4", etc.
        double currentNoteDuration = 0;
        String currentNoteDurationString = ""; // string representaiton of currentNoteDuration in ly form
        String currentNoteName = ""; // takes all of the ly string "cs'"
        int index = 0;

        if (lyString.contains("*")) { // Ignores s1*8 measures
            index = lyString.indexOf("*");
            index++;
        }
        while (Character.isDigit(lyString.charAt(index))) {// Ignores s1*8 measures
            index++;
        }


        // while loop runs through the entire string
        while (index < lyString.length()) {
            currentNoteDurationString = "";

            // finds the first number of the duration of the note
            if (Character.isDigit(lyString.charAt(index))) {
                int nameIndex = index - 1;
                currentNoteName = "";
                // looking backwards it saves the note info
                while (nameIndex > -1 && lyString.charAt(nameIndex) != ' ') {
                    currentNoteName += lyString.charAt(nameIndex);
                    nameIndex--;
                }
                //because it stepped backwards, it must be reversed
                currentNoteName = reverseString(currentNoteName);

                //Finds all numbers related to the duration (IE 16)
                while (Character.isDigit(lyString.charAt(index))) {
                    currentNoteDurationString += lyString.charAt(index);
                    index++;
                }

                //Checks for a dot and adjusts appropriately
                if (index < lyString.length() && lyString.charAt(index) == '.') {
                    System.out.println("currentNoteDuration: " + currentNoteDurationString);
                    currentNoteDuration = 1 / Double.valueOf(currentNoteDurationString) * 1.5;
                    currentNoteDurationString += ".";
                } else {
                    currentNoteDuration = 1 / Double.valueOf(currentNoteDurationString);
                }

                //Adds the current note to the current measure accumulator
                rainbow += currentNoteDuration;


                // If exactly at the end of a measure add a bar line and reset 
                if (rainbow == timeSignature) {
                    rainbow -= timeSignature;
                    string += currentNoteName + currentNoteDurationString + " |\n";
                } // If note goes over the barline make ties
                else if (rainbow > timeSignature) {
                    //If only one measure is needed  
                    if (rainbow < 2 * timeSignature) {
                        double secondNote = rainbow - timeSignature;
                        double firstNote = currentNoteDuration - secondNote;
                        string += createTieOverLyBar(firstNote, secondNote, currentNoteName);
                        rainbow = secondNote;
                    } else {//if note takes up more than one measure
                        double firstNote = timeSignature - (rainbow - currentNoteDuration);
                        string += this.tieNote(converter.convertDecimalToBinary(firstNote), currentNoteName) + "|\n";
                        currentNoteDuration -= firstNote;
                        while (currentNoteDuration > timeSignature) {
                            string += this.tieNote(converter.convertDecimalToBinary(timeSignature), currentNoteName) + "|\n";
                            currentNoteDuration -= timeSignature;
                        }
                        string += tieNote(converter.convertDecimalToBinary(currentNoteDuration), currentNoteName);
                        string = string.substring(0, string.length() - 2);
                        rainbow = currentNoteDuration;
                    }
                } else {// Not at the end ofa measure
                    string += currentNoteName + currentNoteDurationString + " ";
                }
            }
            index++;// end of loop
        }
        return string;
    }

    /**
     * This method can take in either a JFugue musicString or a LilyPond musicString.
     * It then adds barLines to the track, and formats it properly to be added
     * on to the new file.  
     * 
     * @param musicString
     * @param track
     */
    public void addTrack(String musicString, int track) {
        String lyTrack = "";
        //Checks for which formatting is appropriate
        if (musicString.contains("/")) {
            lyTrack += musicStringToLyTrack(musicString) + "\n\n";
        } else {
            lyTrack = "{\n\n" + this.addfileInfoTokens() +
                    "s1*" + (int) Math.ceil(measures) + "*" + timeSig + "\n";
            lyTrack += addBarLinesToLyTrack(musicString) + "\n\n}";

        }
        
        //Checks for internal Format;
        lyTrack = lyTrack.replace("S", "s");
        lyTrack = lyTrack.replace("F", "f");


        //System.out.println("saving track: \n" + lyTrack);
        //Gets the ascii value so that "1" turns into "A"
        track += 64;
        char trackChar = (char) track;


        newFile +=
                "track" + trackChar + "channelA = " + lyTrack + "\n\n";
        newFile +=
                "track" + trackChar + " = << \n  \\context Voice = channelA" +
                "\\track" + trackChar + "channelA \n>>\n\n\n";
    }

    /**
     * Takes the tokenStream of a lilyPond file and returns an int[] with the
     * corresponding midiNote Values
     * 
     * @return midiNoteArray
     */
    public int[] getMidiNoteValues() {
        Converter conv = new Converter();
        int[] noteValuesArray = new int[tokenStream.capacity()];
        int index = 0;

        if (relative) {
            MelodyAnalyzer analyzer = new MelodyAnalyzer();
            noteValuesArray = analyzer.getNoteValues(this.musicString);
        } else {
            for (String noteToken : tokenStream) {
                noteValuesArray[index] = conv.convertLilyNoteToMidiNoteValue(noteToken);
                index++;
            }
        }
        return noteValuesArray;
    }
}

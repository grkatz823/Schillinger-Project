package schillingerapp;

import java.util.*;
import java.io.*;

/**
 *
 * @author jones
 */
public class SchillingerDB {

    private RhythmPattern schDB[] = new RhythmPattern[100];
    public int size = 0;
    private String textFile = "SchDB.txt";

    public SchillingerDB() {
        readSchDB();
    }

    /**
     * reads in the schillinger database
     */
    public void readSchDB() {
        File file = new File(this.textFile);
        //int [] intArr = new int[100];
        int[] intArr;
        this.size = 0;
        try {
            Scanner scan = new Scanner(file);
            String delims = ",";
            String tempLine;
            while (scan.hasNextLine()) {
                tempLine = scan.nextLine();
//                System.out.println(tempLine);
                StringTokenizer tempTokens = new StringTokenizer(tempLine, delims);
//                while(tempTokens.hasMoreTokens()){
//                    System.out.println(tempTokens.nextToken());
//                }
                String description = tempTokens.nextToken();
                int patLength = Integer.parseInt(tempTokens.nextToken());
                intArr = new int[patLength];
                for (int i = 0; i < patLength; i++) {
                    intArr[i] = Integer.parseInt(tempTokens.nextToken());
                }

                schDB[size] = new RhythmPattern(description, patLength, intArr);
                size++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
    /**
     * outputs the schillinger database as a side effect
     */
    public void writeSchDB() {

        // create an outputstream and a print stream
        try {
            FileOutputStream outStream = new FileOutputStream(textFile);
            PrintStream printStream = new PrintStream(outStream);

            for (int i = 0; i < this.size; i++) {
                printStream.print(schDB[i].getDescription() + ",");
                printStream.print(schDB[i].getPatternLength() + ",");
                for (int j = 0; j < schDB[i].getPatternLength(); j++) {
                    printStream.print(schDB[i].getPattern()[j]);
                    if (j < schDB[i].getPatternLength() - 1) {
                        printStream.print(",");
                    }
                }
                printStream.println();
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    public void printDatabase() {
        int[] tempArray;

        for (int i = 0; i < this.size; i++) {
            //Because of spacing problems, certain lines need special cases
            if (i < 2) {
                System.out.print(schDB[i].getDescription() + ",  " + schDB[i].getPatternLength() + ",  ");
            } else if (i == 2) {
                System.out.print(schDB[i].getDescription() + "," + schDB[i].getPatternLength() + ",  ");
            } else if (i < this.size - 2) {
                System.out.print(schDB[i].getDescription() + ", " + schDB[i].getPatternLength() + ",  ");
            } else {
                System.out.print(schDB[i].getDescription() + ", " + schDB[i].getPatternLength() + ", ");
            }

            //Prints the binay pattern.
            tempArray = schDB[i].getPattern();
            for (int j = 0; j < schDB[i].getPatternLength(); j++) {
                System.out.print(tempArray[j]);
            }


            System.out.println();
        }
        System.out.println();
    }
    /**
     * Converts a line of the schillinger database to a string and returns it
     * @return string
     */
    public String toString() {
        int[] tempArray;
        String string = "";

        for (int i = 0; i < this.size; i++) {
            //Because of spacing problems, certain lines need special cases
            if (i < 2) {
                string += schDB[i].getDescription() + ",  " + schDB[i].getPatternLength() + ",  ";
            } else if (i == 2) {
                string += schDB[i].getDescription() + "," + schDB[i].getPatternLength() + ",  ";
            } else if (i < this.size - 2) {
                string += schDB[i].getDescription() + ", " + schDB[i].getPatternLength() + ",  ";
            } else {
                string += schDB[i].getDescription() + ", " + schDB[i].getPatternLength() + ", ";
            }

            //Prints the binay pattern.
            tempArray = schDB[i].getPattern();
            for (int j = 0; j < schDB[i].getPatternLength(); j++) {
                string += tempArray[j];
            }
            string += "\n";
        }

        return string;
    }

    /**
     * getter function that returns the size
     * @return
     */
    public int getSize() {
        return size;
    }
    /**
     * Reads a text file line by line, increasing int lines by one each time
     * until the currentLine is null meaning the text file has ended.
     * Then returns the number stored in lines.
     * @return lines
     */
    public int countLinesInDB() {

        LineNumberReader lineNumberReader = null;
        int lines = 0;

        try {
            lineNumberReader = new LineNumberReader(new FileReader(textFile));
            String currentLine = lineNumberReader.readLine();
            while (currentLine != null) {
                lines = lineNumberReader.getLineNumber();
                currentLine = lineNumberReader.readLine();
            }
        // Catchblock can be found at:
        // http://www.javadb.com/using-the-linenumberreader-class
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (lineNumberReader != null) {
                    lineNumberReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return lines;
    }
    /**
     * getter function that returns the pattern at a given index
     * @param index
     * @return
     */
    public int[] getPatternFromDB(int index) {
        return schDB[index].getPattern();
    }

    /**
     * Converts the given int Array into a string and checks it against
     * each pattern of the database which must also be converted into strings.
     * If a match is found, the integer i representing the index is returned
     * @param pattern
     * @return
     */
    public int getIndexFromDB(int[] pattern) {
        Converter converter = new Converter();
        String converterString = converter.convertIntArrayToString(pattern);

        for (int i = 0; i < this.size; i++) {
            String schillingerString = converter.convertIntArrayToString(this.schDB[i].getPattern());
            if (converterString.equalsIgnoreCase(schillingerString)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * given an int[] pattern, finds the corresponding Schillinger pattern
     * @param pattern
     * @return this.schDB[i]
     */
    public RhythmPattern getSchillingerPatternFromDB(int[] pattern) {
        Converter converter = new Converter();
        String converterString = converter.convertIntArrayToString(pattern);

        for (int i = 0; i < this.size; i++) {
            String schillingerString = converter.convertIntArrayToString(this.schDB[i].getPattern());
            if (converterString.equalsIgnoreCase(schillingerString)) {
                return this.schDB[i];
            }
        }
        return null;
    }

    /**
     * getter function, return the schillinger pattern at a given int index
     * @param index
     * @return
     */
    public RhythmPattern getSchillingerPatternFromDB(int index) {
        return schDB[index];
    }

    /**
     * getter function, returns the pattern length at a given int index
     * @param index
     * @return
     */
    public int getPatternLengthFromDB(int index) {
        return schDB[index].getPatternLength();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schillingerapp;

import javax.sound.midi.*;
import org.jfugue.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author jones
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidMidiDataException {



    }

    public static void testCode() {



        //              TimeNoteListener myListener = new TimeNoteListener();
//        int pitchAxis = 0;
//        Converter converter = new Converter();
//        Player player = new Player();
//        try {
//            RhythmGenerator gen = new RhythmGenerator();
//            MusicGenerator musicMaker = new MusicGenerator();
//            int[] pitter = {3,3,2,2,1,1,1,1,2,2,3,3};
//            BinaryPattern binPat = new BinaryPattern(pitter,0.125);
//            String musicString = "c5/1.5 c5/1.5 c5/1.0 c5/1.0 c5/0.5 c5/0.5 c5/0.5 c5/0.5 c5/1.0 c5/1.0 c5/1.5 c5/1.5";
//            BinaryPattern newPat = gen.createRhythm(musicString, RhythmGenerator.permType.SWAP);
//            System.out.println("NEW BINPAT: " + newPat);
//
//
//            MelodyStruct melody = new MelodyStruct(musicString);
//            melody.musicString = musicString;
//            melody.setKey(pitchAxis);
//
//
//            melody.populateMidiNoteValues();
//            melody.populateInfoPoints(pitchAxis);
//            melody.populateTrajectories();
//            melody.populateLineValues(pitchAxis);
//            melody.populateScribes();
//
//            System.out.println(melody);
//            String convertedMelody = converter.convertMelodyStructToMusicString(melody, 0, 0);
//            System.out.println(convertedMelody);
//            player.saveMidi(convertedMelody, new File("melodyStructTests.mid"));
//
////            String testResults = musicMaker.generatePossibilities(musicString);
////            System.out.println("After musicMaker.generatePossibilities()");
////            permInfoTextArea.setText(musicMaker.permInfo);
////            player.saveMidi(testResults, new File(output));
//        } catch (Exception e) {
//            System.err.println("Error in generateMusicButtonActionPerformed: " + e);
//        }




        //        try {
//            SchillingerDB database = new SchillingerDB();
//            RhythmPattern rhythmPat = database.getSchillingerPatternFromDB(2);
//            BinaryPattern binPat = new BinaryPattern(rhythmPat.getPattern(), 0.125);
//            RhythmGenerator rhythmGen = new RhythmGenerator();
//            int[] newRhythm = rhythmGen.balanceRhythm(binPat.getBinaryPattern());
//            BinaryPattern newPattern = new BinaryPattern(newRhythm, 0.125);
//
//            Player player = new Player();
//            Converter converter = new Converter();
//            String musicString = converter.convertBinaryPatternToMusicString(newPattern, 0, 0);
//            player.saveMidi(musicString, new File("BalanceTest.mid"));
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }


//                RhythmGenerator rhythmGen = new RhythmGenerator();
//        Converter converter = new Converter();
//        String temp = converter.convertIntArrayToString(rhythmGen.makeTwoGeneratorFractionedCounterpart(9, 7));
//       // System.out.println(temp);
//       // System.out.println(converter.convertIntArrayToString(rhythmGen.convertBinaryPatternToBaseTen(rhythmGen.makeTwoGeneratorFractionedCounterpart(9, 7))));
//
//
//        for (int i = 0; i < 37; i++) {
//            SchillingerDB database = new SchillingerDB();
//            RhythmPattern rhythmPat = database.getSchillingerPatternFromDB(i);
//            RhythmGenerator gen = new RhythmGenerator();
//
//            int[] first = gen.repeatFirstHalf(rhythmPat.getPattern());
//            int[] second = gen.repeatSecondHalf(rhythmPat.getPattern());
//            int[] swap = gen.swapFirstHalfWithSecond(rhythmPat.getPattern());
//
//            if (swap.length != rhythmPat.getPatternLength()) {
//                System.out.println("swap fails on: " + i + "," + rhythmPat.getDescription());
//                System.out.println(converter.convertIntArrayToString(gen.convertBinaryPatternToBaseTen(rhythmPat.getPattern())));
//            }
//        }


//        Converter converter = new Converter();
//        RhythmGenerator gen = new RhythmGenerator();
//        int[] pat = gen.makeTwoGeneratorOriginalPattern(3, 4);
//
//        //System.out.println(converter.convertIntArrayToString(pat));
//
//
//
//
//        SchillingerDB database = new SchillingerDB();
//        for (int i = 0; i < 38; i++) {
//            RhythmPattern schillingerPattern = database.getSchillingerPatternFromDB(i);
//            String schPat = converter.convertIntArrayToString(schillingerPattern.getPattern());
//            int a = Character.getNumericValue(schillingerPattern.getDescription().charAt(0));
//            int b = Character.getNumericValue(schillingerPattern.getDescription().charAt(2));
//            int[] newPat;
//            if (i % 2 == 0) {
//                newPat = gen.makeTwoGeneratorOriginalPattern(a,b);
//            } else {
//                newPat = gen.makeTwoGeneratorFractionedCounterpart(a,b);
//            }
//
//            String newPatToString = converter.convertIntArrayToString(newPat);
//            if (!newPatToString.equals(schPat)) {
//             //   System.out.println(schillingerPattern);
//                System.out.println(schillingerPattern.getDescription() + "," + newPat.length + "," + newPatToString);
//            }
//        }
//
//




        try {

            Player player = new Player();
            MusicGenerator musicMaker = new MusicGenerator();

            TimeNoteListener myListener = new TimeNoteListener();

            String musicString = "C5/0.375 D5/0.125 E5/0.25 F5/0.25";
            //          System.out.println(myListener.getParsedString());
            String testResults = musicMaker.generatePossibilities(musicString);
            //System.out.println(testResults);
            player.saveMidi(testResults, new File("tempoTest.mid"));
        } catch (Exception e) {

            System.out.println("Exception: " + e);
        }



//       tester.printDatabaseMatches();

//       Binary Pattern binPat = tester.getBinaryPatternFromFile("fourBarMelody.mid");

        //      int[] pitter = {1,0,1,1,1,0};
        //      BinaryPattern testArray = new BinaryPattern(pitter, 0.25);

        //      tester.showTopMatchesAndOffset(3, testArray);


        //create a music string
//        String testString = "C5/0.375 D5/0.125 E5/0.25 F5/0.25";
//        tester.showNoteValues(testString);


//        //create a music string
//        Converter converter = new Converter();
//        SchillingerDB SchDB = new SchillingerDB();
//
//
//        //Test MusicString Examples
//        String gershwinExample = "G5/0.125 Ab5/0.125 Bb5/0.25 G5/0.25 D6/0.25 " +
//                "C6/0.25 Bb5/0.25 G5/0.25 D6/0.25 C6/0.125 Bb5/0.125 G5/0.125 " +
//                "G5/0.125 G5/0.5 F5/0.25 D5/0.75 G5/0.125 A5/0.125 Bb5/0.25 " +
//                "G5/0.25 D6/0.25 C5/0.25 Bb5/0.25 G5/0.25 C5/0.25 C5/0.25 F5/1.00";
//
//        String otherExample = "D5/0.25 E5/0.25 G5/0.25 A5/0.25 B5/.125 D6/0.125 " +
//                "E6/0.25 E5/0.375 G5/0.125 A5/0.125 B5/0.125 G5/0.375 D5/0.125 " +
//                "E5/0.25 D5/0.5 D5/0.25 E5/0.25 G5/0.25 A5/0.5 B5/0.125 " +
//                "D6/0.125 E6/0.5 G6/0.25 F#6/0.125 G6/0.0625 E6/0.25 E6/0.5 " +
//                "D6/0.25 G6/0.25 F#6/0.25 G6/0.25 E6/0.5 G6/126 E6/0.125 " +
//                "D6/0.125 E6/0.125 D6/0.25 D6/0.125 C6/0.0625 B5/0.0625 " +
//                "A5/0.375 G5/0.125 E5/0.5";
//        BinaryPattern gershwinPattern = converter.convertMusicStringToBinaryPattern(gershwinExample);
//        BinaryPattern otherPattern = converter.convertMusicStringToBinaryPattern(otherExample);
//        HitListDB hitlist = new HitListDB(gershwinPattern);
//        System.out.println();
//        System.out.println("HitListDB original make for Gershwin Example:");
//        hitlist.printHitListDB();
//        System.out.println();
//
//        System.out.println("HitListDB sorted for Gershwin Example:");
//        hitlist.sortDB();
//        hitlist.printHitListDB();
//
//        System.out.println();
//        System.out.println("Index of Top Hit: " + hitlist.hitListMatrix[0][0].getPatternIndex());

        //Testing TimeNoteListener Class

        SchillingerDB schDB = new SchillingerDB();
        Converter converter = new Converter();

        Player player = new Player();
        try {
            Pattern pit = player.loadMidi(new File("fourBarMelody.mid"));
            System.out.println(pit.toString());

            TimeNoteListener myListener = new TimeNoteListener();
            myListener.parseString("fourBarMelody.mid");

            System.out.println();
            System.out.println("Parsed music String:");
            System.out.println(myListener.output);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        //Testing RhythmGenerator Class and subMethods
        RhythmGenerator gen = new RhythmGenerator();

        System.out.println("SchillingerPattern from Binary to Base Ten:");
        int[] newPat = gen.convertBinaryPatternToBaseTen(schDB.getPatternFromDB(0));
        for (int i = 0; i < newPat.length; i++) {
            System.out.print(newPat[i]);
        }

        System.out.println();
        System.out.println();

        System.out.println("SchillingerPattern from Base Ten to Binary:");
        int[] oldPat = gen.convertBaseTenToBinary(newPat);
        for (int i = 0; i < oldPat.length; i++) {
            System.out.print(oldPat[i]);
        }

        System.out.println();
        System.out.println();


        System.out.println("Circularly Permuted two indices:");
        int[] schoobiePat = gen.circularlyPermutePattern(oldPat, 2);

        for (int i = 0; i < schoobiePat.length; i++) {
            System.out.print(schoobiePat[i]);
        }
        System.out.println();

        BinaryPattern binaryTest = new BinaryPattern(schDB.getPatternFromDB(0), 0.125);
        String testString = "C5/0.375 C5/0.125 C5/0.25 C5/0.25";


        try {
            TimeNoteListener myListener = new TimeNoteListener();
            myListener.parseString("fourBarMelody.mid");


            binaryTest = converter.convertMusicStringToBinaryPattern(myListener.output);
            System.out.println();
            //String testResults = gen.generatePossibilites(binaryTest);
            System.out.println("RhythmGenerator Results:");
            //System.out.println(testResults);

            Player playah = new Player();
        //playah.saveMidi(testResults, new File("testResults.mid"));

        } catch (Exception e) {
        }

        MelodyStruct testStruct = new MelodyStruct(testString);
        System.out.println(testStruct);

        MelodyAnalyzer melody = new MelodyAnalyzer();
        MelodyStruct analysis = melody.createAnalysis("C5/0.375 G5/0.125 G5/0.25 C5/0.25 E5/0.25 F5/0.25 G5/0.5 C6/0.375 B5/0.125 A5/0.125 B5/0.375 A5/0.125 G5/0.125 D6/0.25 E6/0.25 C6/0.25 B6/.125");
        for (int i = 0; i < melody.histogram.length; i++) {
            System.out.print(melody.histogram[i] + " ");
        }
        System.out.println();

        System.out.println(analysis);
//
//        MelodyGenerator generator = new MelodyGenerator();
//        int[] listOfNotes = generator.PermuteMelody(analysis, MelodyGenerator.MelodyPerm.NOCHANGE);
//        System.out.println("list of Notes:");
//        for(int i=0; i< listOfNotes.length; i++){
//            System.out.print(listOfNotes[i] + " ");
//        }
//        System.out.println();

    }
}

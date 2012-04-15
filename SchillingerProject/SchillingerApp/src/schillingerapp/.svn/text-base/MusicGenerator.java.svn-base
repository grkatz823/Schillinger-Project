package schillingerapp;

import javax.sound.midi.*;
import org.jfugue.*;
import java.io.*;
import java.util.Random;

/**
 * Calls MelodyGenerator and RhythmGenerator, merging the output of both methods
 * into a MusicString.
 *
 * @author prhayman
 */
public class MusicGenerator { //If this doesn't work, add "extends Exception"

    String permInfo = "";
    BinaryPattern rhythm;
    int[] scale;
    int NUMRESULTS = 4;
    int[] topMatch;
    int matchIndex;

    public MusicGenerator() {
    }

    public MusicGenerator(int key) {
        KeyFinder finder = new KeyFinder();
        this.scale = finder.createIonianScale(key);
    }

    public MusicGenerator(int[] scale) {
        this.scale = scale;
    }

    /**
     * Repeatedly calls RhythmGenerator and MelodyGenerator and combines the
     * results to form the possibilities returned to the user in the form
     * of a musicString.
     *
     * @param musicString
     * @return
     */
    public void generatePossibilities(String musicString, LilyPond file) {

        System.out.println("Entered generatePossibilities");
        RhythmGenerator generator = new RhythmGenerator();
        Random randomGenerator = new Random();
        Converter converter = new Converter();
        MelodyStruct original = this.populateOriginal(musicString);

        String results = "";
        System.out.println(original);

        
        int nextTime = this.getNextTime(musicString);
        int[] usedPerms = new int[NUMRESULTS];
        int perm = randomGenerator.nextInt(8);
        String permString = Integer.toString(perm);

        for (int i = 0; i < NUMRESULTS; i++) {
            permInfo += (i + 1) + ") ";
            original = this.populateOriginal(musicString);


            rhythm = this.findNewRhythmPattern(musicString);
            String convIntArr = converter.convertIntArrayToString(usedPerms);
            while (convIntArr.contains(permString)) {
                perm = randomGenerator.nextInt(8);
                permString = Integer.toString(perm);
            }
            usedPerms[i] = perm;
            BinaryPattern permRhythm = rhythm;
            System.out.println("PERM" + perm);
            switch (perm) {
                case 1:
                    permRhythm = rhythm;
                    permInfo += "New rhythm as is. \n";
                    break;
                case 2:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.SWAP);
                    permInfo += "Swapped the first and second half.\n";
                    break;
                case 3:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.REPEAT1);
                    permInfo += "Repeated the first half. \n";
                    break;
                case 4:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.REPEAT2);
                    permInfo += "Repeated the second half. \n";
                    break;
                case 5:
                    int[] tenBase = generator.convertBinaryPatternToBaseTen(rhythm.getBinaryPattern());
                    int random = randomGenerator.nextInt(tenBase.length) + 1;
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.CIRCPERM, random);
                    permInfo += "Circularly permuted " + random + " spots.\n";
                    break;
                case 6:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.BALANCE);
                    permInfo += "Rhythm has been balanced. \n";
                    break;
                case 7:
                    permRhythm = new BinaryPattern(generator.makeFractionedCounterpart(matchIndex), rhythm.getCommonDenominator());
                    System.out.println(permRhythm);
                    permInfo += "Fractioned counterpart. \n";
                    original = this.populateOriginal(musicString);
                    //System.out.println(permRhythm);
                    break;
            }

            MelodyStruct newMelody = this.populateNewMelody(permRhythm, original);
            System.out.println(newMelody);
            System.out.println("BINPAT:" + newMelody.binPat);
            results = converter.convertMelodyStructToMusicString(newMelody, i + 1, nextTime);
            System.out.println("RESULTS:" + results);
            // results = file.musicStringToLyTrack(results);
            // System.out.println("RESULTS2:" + results);
            file.addTrack(results, i + 2);
        }

        //System.out.println(permInfo);
        System.out.println("generatepossibilities ending");
    }

    /**
     * Repeatedly calls RhythmGenerator and MelodyGenerator and combines the 
     * results to form the possibilities returned to the user in the form
     * of a musicString.
     * 
     * @param musicString
     * @return
     */
    public String generatePossibilities(String musicString) {

        System.out.println("Entered generatePossibilities");
        RhythmGenerator generator = new RhythmGenerator();
        Random randomGenerator = new Random();
        Converter converter = new Converter();
        MelodyStruct original = this.populateOriginal(musicString);

        String results = "";// "V0 @0 " + " " + converter.convertMelodyStructToMusicString(original, 0, 0);
        System.out.println(original);

        //original.binPat.commonDenominator = original.binPat.commonDenominator*4;

        int nextTime = this.getNextTime(musicString);
        int[] usedPerms = new int[NUMRESULTS];
        int perm = randomGenerator.nextInt(8);
        String permString = Integer.toString(perm);

        for (int i = 0; i < NUMRESULTS; i++) {
            permInfo += (i + 1) + ") ";
            original = this.populateOriginal(musicString);


            rhythm = this.findNewRhythmPattern(musicString);
            String convIntArr = converter.convertIntArrayToString(usedPerms);
            while (convIntArr.contains(permString)) {
                perm = randomGenerator.nextInt(8);
                permString = Integer.toString(perm);
            }
            usedPerms[i] = perm;
            BinaryPattern permRhythm = rhythm;
            //System.out.println("NOW IN PERM: " + perm);
            switch (perm) {
                case 1:
                    permRhythm = rhythm;
                    permInfo += "New rhythm as is. \n";
                    break;
                case 2:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.SWAP);
                    permInfo += "Swapped the first and second half.\n";
                    break;
                case 3:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.REPEAT1);
                    permInfo += "Repeated the first half. \n";
                    break;
                case 4:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.REPEAT2);
                    permInfo += "Repeated the second half. \n";
                    break;
                case 5:
                    int[] tenBase = generator.convertBinaryPatternToBaseTen(rhythm.getBinaryPattern());
                    int random = randomGenerator.nextInt(tenBase.length) + 1;
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.CIRCPERM, random);
                    permInfo += "Circularly permuted " + random + " spots.\n";
                    break;
                case 6:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.BALANCE);
                    permInfo += "Rhythm has been balanced. \n";
                    break;
                case 7:
                    permRhythm = generator.createRhythm(rhythm, RhythmGenerator.permType.FRACTIONED);
                    permInfo += "Fractioned counterpart. \n";
                    original = this.populateOriginal(musicString);
                    //System.out.println(permRhythm);
                    break;
            }

            MelodyStruct newMelody = this.populateNewMelody(permRhythm, original);
            results = converter.convertMelodyStructToMusicString(newMelody, i + 1, nextTime);

        }

        //System.out.println(permInfo);
        return results;
    }

    /**
     * Repeatedly calls RhythmGenerator and MelodyGenerator and combines the
     * results to form the possibilities returned to the user in the form
     * of a musicString.
     *
     * @param lily
     * @return
     */
    public void generatePossibilities(LilyPond lily) {
        RhythmGenerator rhythmGen = new RhythmGenerator();
        Random randomGen = new Random();
        Converter converter = new Converter();
        MelodyStruct original = this.populateOriginal(lily.musicString);
        System.out.println(original);

        int[] usedPerms = new int[NUMRESULTS];
        int perm = randomGen.nextInt(8);
        String permString = Integer.toString(perm);

        for (int i = 0; i < NUMRESULTS; i++) {
            permInfo += (i + 1) + ") ";
            original = this.populateOriginal(lily.musicString);
            rhythm = this.findNewRhythmPattern(lily);
            String convIntArr = converter.convertIntArrayToString(usedPerms);
            while (convIntArr.contains(permString)) {
                perm = randomGen.nextInt(8);
                permString = Integer.toString(perm);
            }
            usedPerms[i] = perm;
            BinaryPattern permRhythm = rhythm;
            //System.out.println("NOW IN PERM: " + perm);
            switch (perm) {
                case 1:
                    permRhythm = rhythm;
                    permInfo += "New rhythm as is. \n";
                    break;
                case 2:
                    permRhythm = rhythmGen.createRhythm(rhythm, RhythmGenerator.permType.SWAP);
                    permInfo += "Swapped the first and second half.\n";
                    break;
                case 3:
                    permRhythm = rhythmGen.createRhythm(rhythm, RhythmGenerator.permType.REPEAT1);
                    permInfo += "Repeated the first half. \n";
                    break;
                case 4:
                    permRhythm = rhythmGen.createRhythm(rhythm, RhythmGenerator.permType.REPEAT2);
                    permInfo += "Repeated the second half. \n";
                    break;
                case 5:
                    int[] tenBase = rhythmGen.convertBinaryPatternToBaseTen(rhythm.getBinaryPattern());
                    int random = randomGen.nextInt(tenBase.length) + 1;
                    permRhythm = rhythmGen.createRhythm(rhythm, RhythmGenerator.permType.CIRCPERM, random);
                    permInfo += "Circularly permuted " + random + " spots.\n";
                    break;
                case 6:
                    permRhythm = rhythmGen.createRhythm(rhythm, RhythmGenerator.permType.BALANCE);
                    permInfo += "Rhythm has been balanced. \n";
                    break;
                case 7:
                    permRhythm = rhythmGen.createRhythm(rhythm, RhythmGenerator.permType.FRACTIONED);
                    permInfo += "Fractioned counterpart. \n";
                    original = this.populateOriginal(lily.musicString);
                    //System.out.println(permRhythm);
                    break;
            }

            MelodyStruct newMelody = this.populateNewMelody(permRhythm, original);
            System.out.println(newMelody);
            String lilyTrack = converter.convertMelodyStructToLilyPond(newMelody);
            System.out.println("After converter");
            lily.addTrack(lilyTrack, i + 2);
        }
    }

    /**
     * This method gets the ending time for a music string.  It does this by
     * looking at the last values in the music string, parsing out the time
     * and the last note duration.  The duration is then multiplied by the
     * proper coefficient, and added to the last start time.
     *
     * @param musicString
     * @return
     */
    public int getNextTime(String musicString) {
        double nextTime = 0;

        Converter converter = new Converter();
        BinaryPattern binpat = converter.convertMusicStringToBinaryPattern(musicString);
        int length = binpat.binaryPattern.length;
        nextTime = length * 120 * binpat.getCommonDenominator();


        return (int) nextTime;
    }

    /**
     * NOT YET WORKING
     * @param lily
     * @return
     */
    public int getNextTime(LilyPond lily) {
        double nextTime;

        Converter converter = new Converter();
        BinaryPattern binPat = converter.convertLilyPondToBinaryPattern(lily);
        int binLength = binPat.binaryPattern.length;
        nextTime = binLength * 120 * binPat.commonDenominator;

        return (int) nextTime;
    }

    /**
     *fills the melody struct with the proper information after receiving the
     * original melody Struct and a binary pattern
     *
     * @param rhythm
     * @param original
     * @return
     */
    public MelodyStruct populateNewMelody(BinaryPattern rhythm, MelodyStruct original) {
        MelodyStruct newMelody = new MelodyStruct(rhythm);

        for (int i = 0; i < newMelody.melodyArray.length; i++) {
            if (rhythm.getBinaryPattern()[i] == 1) {
                newMelody.melodyArray[i].setMidiNoteValue(-2);
            } else {//if 0
                newMelody.melodyArray[i].setMidiNoteValue(-1);
            }
        }

        Converter converter = new Converter();
        newMelody.setScale(original.scale);
        System.out.println("Chromatic: " + converter.convertStringArrayToString(original.chromatic))
                ;
        newMelody.setChromatic(original.chromatic);

        MelodyGenerator melodyGenerator = new MelodyGenerator(original, newMelody);

        melodyGenerator.permuteTrajectories(MelodyGenerator.MelodyPerm.VERTICAL);
        newMelody = melodyGenerator.newMelody;
        newMelody.binPat = rhythm;

        return newMelody;
    }

    /**
     * Analyzes a musicString and populates a MelodyStruct with the
     * results of the analysis.
     * 
     * @param musicString
     * @return
     */
    public MelodyStruct populateOriginal(String musicString) {

        MelodyAnalyzer analyzer = new MelodyAnalyzer();
        MelodyStruct original = analyzer.createAnalysis(musicString);

        return original;
    }

    /**
     * Analyzes a LilyPond tokenStream and populates a MelodyStruct with the
     * results of the analysis.
     * @param lily
     * @return
     */
    public MelodyStruct populateOriginal(LilyPond lily) {
        MelodyAnalyzer analyzer = new MelodyAnalyzer();
        MelodyStruct original = analyzer.createAnalysis(lily);

        return original;
    }

    /**
     * Helper Method: Converts the input music string to a binary pattern.
     * Then creates and sorts a hitlist from this binary pattern and gets the
     * Nth match (currently also the top match). Uses the top match to form a 
     * schillinger pattern and creates a new binary pattern from that called
     * newPat, which is then returned.
     * @param musicString
     * @return newPat
     */
    public BinaryPattern findNewRhythmPattern(String musicString) {
        Converter converter = new Converter();
        BinaryPattern binPat = converter.convertMusicStringToBinaryPattern(musicString);

        System.out.println(binPat);
        HitListDB hitList = new HitListDB(binPat);
        hitList.sortDB();

        topMatch = hitList.getNthMatch(1);
        matchIndex = hitList.hitListMatrix[topMatch[0]][topMatch[1]].getPatternIndex();

        SchillingerDB db = new SchillingerDB();
        int patternIndex = hitList.hitListMatrix[topMatch[0]][topMatch[1]].getPatternIndex();
        int[] schillingerPattern = db.getPatternFromDB(patternIndex);
        double commonDenom = binPat.getCommonDenominator();
        BinaryPattern newPat = new BinaryPattern(schillingerPattern, commonDenom);
        return newPat;
    }

    /**
     * Helper Method: Converts the input LilyPond to a binary pattern.
     * Then creates and sorts a hitlist from this binary pattern and gets the
     * Nth match (currently also the top match). Uses the top match to form a
     * schillinger pattern and creates a new binary pattern from that called
     * newPat, which is then returned.
     * 
     * @param lily
     * @return newPat
     */
    public BinaryPattern findNewRhythmPattern(LilyPond lily) {
        Converter converter = new Converter();
        BinaryPattern binPat = converter.convertLilyPondToBinaryPattern(lily);

        HitListDB hitList = new HitListDB(binPat);
        hitList.sortDB();
        topMatch = hitList.getNthMatch(1);


        SchillingerDB db = new SchillingerDB();
        int patternIndex = hitList.hitListMatrix[topMatch[0]][topMatch[1]].getPatternIndex();
        int[] schillingerPattern = db.getPatternFromDB(patternIndex);
        double commonDenom = binPat.getCommonDenominator();
        BinaryPattern newPat = new BinaryPattern(schillingerPattern, commonDenom);
        return newPat;
    }

    /**
     * Returns the permutation info
     * @return this.perminfo
     */
    public String getPermInfo() {
        return this.permInfo;
    }
}

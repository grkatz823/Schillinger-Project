package schillingerapp;

import javax.swing.*;
import org.jfugue.*;
import java.io.*;
import java.lang.Thread.*;

/**
 * 
 * @author jestuart
 */
public class SchillingerGUI extends javax.swing.JFrame {

    /**
     * Allows the user to specify the input and output files.
     */
    public JFileChooser fc;
    /**
     * The file to be analyzed
     */
    public String input;
    /**
     * The file to which the program will save the output
     */
    public String output;
    /**
     * The string representation of the key, if specified (e.g. AMAJOR)
     */
    public String key;
    /**
     * Determines whether key is to be determined automatically
     */
    public boolean findKey;
    public boolean successfulSoFar = true;
    /**
     * Determines whether results are to viewed as a pdf
     */
    public boolean openPDF;
    public boolean savePDF;
    public boolean saveMIDI;
    public boolean saveLY;
    public LilyPond file;
    String musicString = "";

    public SchillingerGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        inputLabel = new javax.swing.JLabel();
        inputTextField = new javax.swing.JTextField();
        inputButton = new javax.swing.JButton();
        outputLabel = new javax.swing.JLabel();
        resultTextField = new javax.swing.JTextField();
        resultButton = new javax.swing.JButton();
        keyLabel = new javax.swing.JLabel();
        keyTextField = new javax.swing.JTextField();
        keyCheckBox = new javax.swing.JCheckBox();
        pdfCheckBox = new javax.swing.JCheckBox();
        generateMusicButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        schPatMatchLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        schPatMatchTextArea = new javax.swing.JTextArea();
        permInfoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        permInfoTextArea = new javax.swing.JTextArea();
        savePDFBox = new javax.swing.JCheckBox();
        saveMIDIBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        saveLYBox = new javax.swing.JCheckBox();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("The Composer's Intelligent Assistant");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        inputLabel.setText("Please load midi file:");

        inputTextField.setText("/home/grkatz/NetBeansProjects/SchillingerApp/fourBarMelody-midi.ly");
        inputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextFieldActionPerformed(evt);
            }
        });

        inputButton.setText("Browse");
        inputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputButtonActionPerformed(evt);
            }
        });

        outputLabel.setText("Save results to:");

        resultTextField.setText("TestResults.ly");
        resultTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultTextFieldActionPerformed(evt);
            }
        });

        resultButton.setText("Browse");
        resultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultButtonActionPerformed(evt);
            }
        });

        keyLabel.setText("Please enter key: (Ex. AMajor)");

        keyCheckBox.setSelected(true);
        keyCheckBox.setText("Find key automatically");

        pdfCheckBox.setText("Open results as pdf");

        generateMusicButton.setText("Generate Music");
        generateMusicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateMusicButtonActionPerformed(evt);
            }
        });

        schPatMatchLabel.setText("Schillinger Match:");

        schPatMatchTextArea.setColumns(20);
        schPatMatchTextArea.setEditable(false);
        schPatMatchTextArea.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 13));
        schPatMatchTextArea.setRows(5);
        jScrollPane1.setViewportView(schPatMatchTextArea);

        permInfoLabel.setText("Permutation Information");

        permInfoTextArea.setColumns(20);
        permInfoTextArea.setEditable(false);
        permInfoTextArea.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 13));
        permInfoTextArea.setRows(5);
        jScrollPane2.setViewportView(permInfoTextArea);

        savePDFBox.setSelected(true);
        savePDFBox.setText("PDF");

        saveMIDIBox.setSelected(true);
        saveMIDIBox.setText("MIDI");

        jLabel1.setText("Save as:");

        saveLYBox.setSelected(true);
        saveLYBox.setText("LY");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addComponent(inputLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                            .addComponent(resultTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultButton)
                            .addComponent(inputButton)))
                    .addComponent(keyLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(keyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(keyCheckBox)
                            .addComponent(pdfCheckBox))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(saveLYBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                                .addComponent(generateMusicButton)
                                .addGap(12, 12, 12))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(savePDFBox)
                                    .addComponent(saveMIDIBox))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 271, Short.MAX_VALUE))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addComponent(permInfoLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addComponent(schPatMatchLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputButton)
                    .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(outputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultButton)
                    .addComponent(resultTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keyLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyCheckBox)
                    .addComponent(savePDFBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pdfCheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveMIDIBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(generateMusicButton)
                            .addComponent(saveLYBox))))
                .addGap(24, 24, 24)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(schPatMatchLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(permInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Creates a file chooser and allows the user to specify the directory
     * where the results are to be saved and what the file is to be called.
     * 
     * @param evt
     */
    private void resultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultButtonActionPerformed
        // Initializes a new Swing file chooser dialog.
        fc = new JFileChooser();
        // File to display only files with the midi (.mid) file extension.
        fc.addChoosableFileFilter(new MidiFileFilter());
        if (evt.getSource() == resultButton) {
            int returnVal = fc.showOpenDialog(SchillingerGUI.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                output = fc.getSelectedFile().toString();
                resultTextField.setText(output);
            }
        }
    }//GEN-LAST:event_resultButtonActionPerformed

    /**
     * Creates a file chooser and allows the user to specify the input
     * midi file.
     *
     * @param evt
     */
    private void inputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputButtonActionPerformed
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new MidiFileFilter());

        if (evt.getSource() == inputButton) {
            int returnValue = fc.showOpenDialog(SchillingerGUI.this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                input = fc.getSelectedFile().toString();
                inputTextField.setText(input);

                if (resultTextField.getText().equals("TestResults.mid")) {
                    resultTextField.setText(getDirectory(input) + "TestResults.mid");
                }
            }
        }
    }//GEN-LAST:event_inputButtonActionPerformed

    /**
     * Collects all user specifications from the GUI and executes the program
     * accordingly.
     *
     * @param evt
     */
    private void generateMusicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateMusicButtonActionPerformed
        successfulSoFar = true;
        // Clear text areas
        permInfoTextArea.setText("");
        schPatMatchTextArea.setText("");
        // Input midi file location
        input = inputTextField.getText();
        // Results file location
        output = resultTextField.getText();
        // Boolean value: determines if key should be found automatically
        findKey = keyCheckBox.isSelected();
        // Boolean value: determines if results should be opened as a pdf
        openPDF = pdfCheckBox.isSelected();
        key = keyTextField.getText();
        savePDF = savePDFBox.isSelected();
        saveMIDI = saveMIDIBox.isSelected();
        saveLY = saveLYBox.isSelected();
        String fileName = getFileName(input);
        String outputName = getFileName(output);



        if (openPDF && !savePDF) {
            JOptionPane.showMessageDialog(this, "A pdf is not currently being saved. \n" +
                    "Please select that option to be able to open as pdf.");
            successfulSoFar = false;
        }


        if (successfulSoFar) {
            if (input.substring(input.indexOf(".")).equals(".ly")) {
                permuteLilyPondFile();
            } else if (input.substring(input.indexOf(".")).equals(".mid")) {
                permuteMidiFile(fileName, ".mid");
            } else if (input.substring(input.indexOf(".")).equals(".midi")) {
                permuteMidiFile(fileName, ".midi");
            } else {
                JOptionPane.showMessageDialog(this, "File must be of type .ly, .mid or .midi .");
            }

        }




        /* Converts the MusicString (from input file) to a BinaryPattern and
         * produces a sorted HitListDB based on this pattern, from which the
         * top match is determined using the getNthMatch() method. The top
         * match is then displayed in the schPatMatchTextArea.
         */

        Converter converter = new Converter();
        BinaryPattern binPat = converter.convertMusicStringToBinaryPattern(musicString);
        HitListDB hitList = new HitListDB(binPat);
        hitList.sortDB();
        int[] index = hitList.getNthMatch(1);
        int topMatch = hitList.hitListMatrix[index[0]][index[1]].getPatternIndex();
        String topMatches = "Input Matched to Pattern: " + topMatch + " in column " + index[1] + ".\n";
        topMatches += hitList.topMatchesString(1);
        schPatMatchTextArea.setText(topMatches);

        //Compiles the created ly file.
        if (successfulSoFar) {
            try {
                outputName = getFileName(output);
                String curDir = System.getProperty("user.dir");
                String cmd = "lilypond " + curDir + "/" + outputName + ".ly";
                System.out.println(cmd);
                Runtime run = Runtime.getRuntime();
                Process p = run.exec(cmd);
                int exitCode = p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
                successfulSoFar = false;
            }
        }


        //This block removes the files that were created by the compile,
        //but not asked for by the user.
        if (successfulSoFar) {
            try {
                // Determines the current working directory
                // (i.e. ~/NetBeansProjects/SchillingerApp)
                String curDir = System.getProperty("user.dir");
                String cmd = "rm -f " + curDir + "/" + outputName + ".ps ";
                if (!saveLY) {
                    cmd += curDir + "/" + outputName + ".ly ";
                }
                if (!savePDF) {
                    cmd += curDir + "/" + outputName + ".pdf ";
                }
                Runtime run = Runtime.getRuntime();
                cmd = cmd.substring(0, cmd.length() - 1);//remove last space
                System.out.println(cmd);

                Process p = run.exec(cmd);
                int exitCode = p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(-1);
                this.successfulSoFar = false;

            }

        }

        /* If pdfCheckBox is selected and there have been no errors
         * it opens the newly created .pdf in the evince pdf viewer
         */
        if (openPDF && successfulSoFar) {
            //Run shell script CIAscript.sh to open results as pdf in evince
            try {
                // Determines the current working directory
                // (i.e. ~/NetBeansProjects/SchillingerApp)
                String curDir = System.getProperty("user.dir");
                String cmd = "evince " + curDir + "/" + outputName + ".pdf";
                System.out.println(cmd);
                Runtime run = Runtime.getRuntime();
                run.exec(cmd);
            } catch (Exception e) {
                System.out.println(e);
                System.exit(-1);
                this.successfulSoFar = false;
            }
        }
    }//GEN-LAST:event_generateMusicButtonActionPerformed

    public void permuteLilyPondFile() {

        KeyFinder finder = new KeyFinder();

        if (findKey) {
            try {
                MusicGenerator musicMaker = new MusicGenerator();
                file = new LilyPond(input);
                musicString = file.musicString;
                System.out.println("Before musicMaker.generatePossibilities()");
                musicMaker.generatePossibilities(musicString, file);
                System.out.println("After musicMaker.generatePossibilities()");
                permInfoTextArea.setText(musicMaker.permInfo);
                file.saveLilyPond(output, saveMIDI, savePDF);
            } catch (Exception e) {
                System.err.println("Error in generateMusicButtonActionPerformed: " + e);
                this.successfulSoFar = false;
            }
        } else {
            int[] scale = finder.keyStringToScale(key);
            try {
                MusicGenerator musicMaker = new MusicGenerator(scale);
                file = new LilyPond(input);
                musicString = file.musicString;
                musicMaker.generatePossibilities(musicString, file);

                permInfoTextArea.setText(musicMaker.permInfo);
                file.saveLilyPond(output, saveMIDI, savePDF);
            } catch (Exception e) {
                System.err.println("Error: " + e);
                this.successfulSoFar = false;
            }
        }


    }

    public void permuteMidiFile(String fileName, String extension) {

        //Converts midi2ly
        try {
            // Determines the current working directory
            // (i.e. ~/NetBeansProjects/SchillingerApp)
            String curDir = System.getProperty("user.dir");
            String cmd = "midi2ly -ao " + curDir + fileName + ".ly " + curDir + fileName + extension;
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            int exitCode = p.waitFor();
        } catch (Exception e) {
            this.successfulSoFar = false;
            System.out.println(e);
            System.exit(-1);
        }

        KeyFinder finder = new KeyFinder();
        String newFile = fileName + ".ly";
        if (findKey) {
            try {
                MusicGenerator musicMaker = new MusicGenerator();
                file = new LilyPond(newFile);
                musicString = file.musicString;
                System.out.println("Before musicMaker.generatePossibilities()");
                musicMaker.generatePossibilities(musicString, file);
                System.out.println("After musicMaker.generatePossibilities()");
                permInfoTextArea.setText(musicMaker.permInfo);
                file.saveLilyPond(output, saveMIDI, savePDF);
            } catch (Exception e) {
                System.err.println("Error in generateMusicButtonActionPerformed: " + e);
                this.successfulSoFar = false;

            }
        } else {
            int[] scale = finder.keyStringToScale(key);
            try {
                MusicGenerator musicMaker = new MusicGenerator(scale);
                file = new LilyPond(input);
                musicString = file.musicString;
                musicMaker.generatePossibilities(musicString, file);

                permInfoTextArea.setText(musicMaker.permInfo);
                file.saveLilyPond(output, saveMIDI, savePDF);
            } catch (Exception e) {
                System.err.println("Error: " + e);
                this.successfulSoFar = false;
            }
        }


    }

    public String getFileName(String pathName) {
        String fileName = "";
        if (pathName.contains("/")) {
            fileName = pathName.substring(pathName.lastIndexOf("/"), pathName.indexOf("."));
        } else {
            fileName = pathName.substring(0, pathName.indexOf("."));
        }

        return fileName;
    }

    /**
     * Terminates the program when the window is closed.
     * @param evt
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void resultTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resultTextFieldActionPerformed

    private void inputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTextFieldActionPerformed

    /**
     * Calls the SchillingerGUI constructor, which initializes all the
     * components of the interface, and sets the window to visible.
     * 
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SchillingerGUI().setVisible(true);
            }
        });
    }

    public String getDirectory(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("/") + 1);
    }

    public String getInputFile() {
        return input;
    }

    public String getOutputFile() {
        return output;
    }

    public Boolean findKey() {
        return findKey;
    }

    public String getKey() {
        return key;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton generateMusicButton;
    private javax.swing.JButton inputButton;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JCheckBox keyCheckBox;
    private javax.swing.JLabel keyLabel;
    private javax.swing.JTextField keyTextField;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JCheckBox pdfCheckBox;
    private javax.swing.JLabel permInfoLabel;
    private javax.swing.JTextArea permInfoTextArea;
    private javax.swing.JButton resultButton;
    private javax.swing.JTextField resultTextField;
    private javax.swing.JCheckBox saveLYBox;
    private javax.swing.JCheckBox saveMIDIBox;
    private javax.swing.JCheckBox savePDFBox;
    private javax.swing.JLabel schPatMatchLabel;
    private javax.swing.JTextArea schPatMatchTextArea;
    // End of variables declaration//GEN-END:variables
}

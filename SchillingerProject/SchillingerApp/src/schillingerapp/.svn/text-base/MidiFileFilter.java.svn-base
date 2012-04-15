/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schillingerapp;

import java.io.*;

/**
 *
 * @author prhayman
 */
  public class MidiFileFilter extends javax.swing.filechooser.FileFilter  {

      /**
       * Checks if a given file is a midi file
       * @param file
       * @return
       */
      public boolean accept(File file)
    {
        //Convert to lower case before checking extension
        return file.getName().toLowerCase().endsWith(".mid");
    }
    /**
     * Produces a string title of the midi file
     * @return String
     */
    public String getDescription()
    {
        return "Midi File (*.mid)";
    }
  }



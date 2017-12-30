package examples;

import de.ueberdosis.mp3info.*;
import de.ueberdosis.util.OutputCtr;
import de.ueberdosis.mp3info.id3v2.*;

import de.ueberdosis.mp3info.gui.Id3JPanel;

import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** Reads a V2 tag from a file and opens a window to edit 
 * all V2 frames encountered.
 * Makes use of the selfupdate option of createJPanel ()
 * (Ie. every parameter has its own update button)
*/

public class id3d implements Defines {
    private static boolean frameClosed = false;

    public static void main (String args[]) {
        OutputCtr.println (0, "Mp3 Id ("+ VERSION + ") and bitrate reader.\n"+
                            "Milon Krejca <milon@iname.com>, " +
                            "Florian Heer <heer@ueberdosis.de>(c) 2001");
        if (args.length != 1)
            OutputCtr.println (0, "Usage: ID3Reader <filename>");
        else {
	    ExtendedID3Tag xtag = null;
	    // Sets the level of output!
	    OutputCtr.setLevel (10);
            try {
		// Let ID3Reader do its thing...
                ID3Reader x = new ID3Reader (args[0]);
		xtag = x.getExtendedID3Tag ();
                OutputCtr.println (0, xtag);

		// If we have a V2 tag we can get into the nicer things...
		ID3V2Tag v2tag = ID3Reader.getV2Tag ();
		if (v2tag != null) {
		    Vector displayPanels = new Vector ();
		    OutputCtr.println (2, "Changing v2tag..");
		    java.util.Iterator iter = v2tag.getFrames ().iterator ();
		    // Gathering all available Panels that can display
		    // (if they could edit, just the better)
		    while (iter.hasNext ()) {
			ID3V2Frame frm = 
			    (ID3V2Frame)iter.next ();
			if (frm.canDisplay ()) 
			    // Although I asked the frame only if it can
			    // display its contents, I try to get an edit
			    // Component. All Frames should be safe there,
			    // ie. only creating display Components if
			    // they can't create an edit Component, even if
			    // the user asked for one.
			    displayPanels.add (frm.createJPanel(true, true));
		    }

		    // Usual GUI code...
		    final JFrame jframe = new JFrame ("mp3info editor");
		    jframe.setSize (600,600);
		    Container cp = jframe.getContentPane ();
		    cp.setLayout (new FlowLayout (FlowLayout.LEFT));
		    iter = displayPanels.iterator ();
		    while (iter.hasNext ()) {
			Id3JPanel pnl = (Id3JPanel)iter.next();
			cp.add(pnl);
		    }
		    frameClosed = false;
		    jframe.addWindowListener 
			( new WindowAdapter () {
				public void windowClosing (WindowEvent e) {
				    frameClosed = true;
				    jframe.dispose ();
				}
			    });
		    jframe.show ();

		    // Wait for the user to close the window...
		    // not too elegant, but this is example code, isn't it?
		    try {
			while (!frameClosed) {
			    Thread.sleep (500);
			}
		    }
		    catch (Exception ex) {};
		    OutputCtr.println (3, v2tag);
		    OutputCtr.println (2, "Writing tag to "+args[0]);
		    // The user closed the window and presumably changed
		    // some information, so we write it.
		    ID3V2Writer.writeTag (new File (args[0]), v2tag);
		}

            }
            catch(IOException e) {
                OutputCtr.println(0, e);
            }
	    System.exit (0);
        }
    }

}

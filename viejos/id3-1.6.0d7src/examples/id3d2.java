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
 * all V2 frames encountered. This time a single update button
 * is created by the caller.
*/

public class id3d2 implements Defines {
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
                ID3Reader x = new ID3Reader (args[0]);
		xtag = x.getExtendedID3Tag ();
                OutputCtr.println (0, xtag);
		ID3V2Tag v2tag = ID3Reader.getV2Tag ();
		if (v2tag != null) {
		    final Vector editPanels = new Vector ();
		    OutputCtr.println (2, "Changing v2tag..");
		    java.util.Iterator iter = v2tag.getFrames ().iterator ();

		    while (iter.hasNext ()) {
			ID3V2Frame frm = 
			    (ID3V2Frame)iter.next ();
			if (frm.canEdit ()) editPanels.add (frm.createJPanel(true, false));
		    }
		    final JFrame jframe = new JFrame ("mp3info editor");
		    Container cp = jframe.getContentPane ();
		    cp.setLayout (new FlowLayout (FlowLayout.LEFT));
		    int width = 0;
		    int height = 0;
		    iter = editPanels.iterator ();
		    while (iter.hasNext ()) {
			Id3JPanel pnl = (Id3JPanel)iter.next();
			cp.add(pnl);
			Dimension d = pnl.getPreferredSize ();
			if (width < d.width) width=d.width;
			height += d.height;
		    }
		    JButton btn = new JButton ("OK");
		    Dimension bDim = new Dimension (Configuration.buttonWidth, 
						    Configuration.buttonHeight);
		    btn.setMinimumSize (bDim);
		    btn.setPreferredSize (bDim);
		    btn.addActionListener (new ActionListener () {
			    public void actionPerformed (ActionEvent e) {
				Iterator it = editPanels.iterator ();
				while (it.hasNext ()) {
				    Id3JPanel jp = (Id3JPanel)it.next();
				    jp.updateData ();
				}
			    }
			});
		    cp.add (btn);
		    frameClosed = false;
		    jframe.addWindowListener 
			( new WindowAdapter () {
				public void windowClosing (WindowEvent e) {
				    frameClosed = true;
				    jframe.dispose ();
				}
			    });
		    jframe.setSize (width+10,height+80+Configuration.buttonWidth);

		    jframe.show ();
		    try {
			while (!frameClosed) {
			    Thread.sleep (500);
			}
		    }
		    catch (Exception ex) {};
		    OutputCtr.println (3, v2tag);
		    OutputCtr.println (2, "Writing tag to "+args[0]);
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

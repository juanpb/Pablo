package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/** Original artist(s)/performer(s) 
 *
 * @author Florian Heer
 * @version $Id: FrameTOPE.java,v 1.6 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTOPE extends FrameT {
    public String getLongName ()  { return "Original artist(s)/performer(s)"; }

    public FrameTOPE () { super ("TOPE"); }

    public FrameTOPE (ID3V2Frame frame) {
	super (frame);
    }

    public FrameTOPE (ID3V2Frame frm, DataSource ds) 
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nArtist: " 
	    + text;
    }

    public boolean canEdit () { return true; }
    public boolean canDisplay () { return true; }

    public Id3JPanel createJPanel (boolean edit, boolean selfupdate) {
	return super.createJPanel (edit, selfupdate, "Orig. Artist:");
    }

}

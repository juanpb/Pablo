package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/** Content type frame 
 *
 * @author Florian Heer
 * @version $Id: FrameTCON.java,v 1.7 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTCON extends FrameT {
    public String getLongName ()  { return "Content type frame"; }

    public FrameTCON () { super ("TCON"); }

    public FrameTCON (ID3V2Frame frame) {
	super (frame);
    }

    public FrameTCON (ID3V2Frame frm, DataSource ds) 
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nContentType : " 
	    + text;
    }

    public boolean canEdit () { return true; }
    public boolean canDisplay () { return true; }

    public Id3JPanel createJPanel (boolean edit, boolean selfupdate) {
	return super.createJPanel (edit, selfupdate, "Content Type:");
    }

}

package de.ueberdosis.mp3info.id3v2;

/** Length Frame.
 * The 'Length' frame contains the length of the audiofile in milliseconds, 
 * represented as a numeric string
 * 
 * @author Florian Heer
 * @version $Id: FrameTLEN.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTLEN extends FrameT {
    public String getLongName ()  { return "Length frame"; }

    public FrameTLEN (ID3V2Frame frame) {
	    super (frame);
    }

    public FrameTLEN (ID3V2Frame frm, DataSource ds) 
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nPlaying time (in ms) : " 
	    + text;
    }

    /** Only to be set by the application */
    public boolean canEdit () { return false; }
}

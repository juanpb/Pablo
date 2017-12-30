package de.ueberdosis.mp3info.id3v2;

/** Textwriter Frame.
 * The 'Lyricist(s)/Text writer(s)' frame is intended for the
 * writer(s) of the text or lyrics in the recording
 *
 * @author Florian Heer
 * @version $Id: FrameTEXT.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTEXT extends FrameT {
    public String getLongName ()  { return "Textwriter frame"; }

    public FrameTEXT (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTEXT (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nText writer : "
	    + text;
    }

}

package de.ueberdosis.mp3info.id3v2;

/** Playlist delay frame
 * The 'Playlist delay' defines the numbers of milliseconds of
 * silence between every song in a playlist.
 *
 * @author Florian Heer
 * @version $Id: FrameTDLY.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTDLY extends FrameT {
    public String getLongName ()  { return "Playlist delay frame"; }

    public FrameTDLY (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTDLY (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nDelay : "
	    + text;
    }

}

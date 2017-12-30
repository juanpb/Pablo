package de.ueberdosis.mp3info.id3v2;

/** Interpreter Frame
 * The 'Interpreted, remixed, or otherwise modified by' frame contains
 * more information about the people behind a remix and similar
 * interpretations of another existing piece.
 *
 * @author Florian Heer
 * @version $Id: FrameTPE4.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTPE4 extends FrameT {
    public String getLongName ()  { return "Interpreter frame"; }

    public FrameTPE4 (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTPE4 (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nInterpreter/Remixer : "
	    + text;
    }

}

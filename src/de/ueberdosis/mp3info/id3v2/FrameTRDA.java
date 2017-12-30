package de.ueberdosis.mp3info.id3v2;

/** Recording date frame
 * The 'Recording dates' frame is a intended to be used as complement to
 * the "TYER", "TDAT" and "TIME" frames. E.g. "4th-7th June, 12th June"
 * in combination with the "TYER" frame.
 *
 * @author Florian Heer
 * @version $Id: FrameTRDA.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTRDA extends FrameT {
    public String getLongName ()  { return "Recording date frame"; }

    public FrameTRDA (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTRDA (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nRecording date: "
	    + text;
    }

}

package de.ueberdosis.mp3info.id3v2;

/** Date frame
 * @author Florian Heer
 * @version $Id: FrameTDAT.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTDAT extends FrameT {
    public String getLongName ()  { return "Copyright frame"; }

    public FrameTDAT (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTDAT (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nDate : "
	    + text;
    }

}

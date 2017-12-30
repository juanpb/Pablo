package de.ueberdosis.mp3info.id3v2;

/** Payment url frame
 * The 'Payment' frame is a URL pointing at a webpage that will handle the
 * process of paying for this file.
 *
 * @author Florian Heer
 * @version $Id: FrameWPAY.java,v 1.4 2003/10/22 23:51:02 heer Exp $
 */
public class FrameWPAY extends FrameW {
    public String getLongName ()  { return "Payment URL frame"; }

    public FrameWPAY (ID3V2Frame frm) {
        super (frm);
    }

    public FrameWPAY (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + "\nURL : "
	    + url;
    }

}

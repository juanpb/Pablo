package de.ueberdosis.mp3info.id3v2;

/**
 * Commercial url frame
 * The 'Commercial information' frame is a URL pointing at a webpage with
 * information such as where the album can be bought. There may be more
 * than one "WCOM" frame in a tag, but not with the same content.
 *
 * @author Florian Heer
 * @version $Id: FrameWCOM.java,v 1.4 2003/10/22 23:51:02 heer Exp $
 */
public class FrameWCOM extends FrameW {
    public String getLongName() {
        return "Commercial URL frame";
    }

    public FrameWCOM(ID3V2Frame frm) {
        super(frm);
    }

    public FrameWCOM(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + "\nURL : "
                + url;
    }

}

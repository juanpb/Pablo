package de.ueberdosis.mp3info.id3v2;

/**
 * Publisher url frame
 * The 'Publishers official webpage' frame is a URL pointing at the
 * official wepage for the publisher.
 *
 * @author Florian Heer
 * @version $Id: FrameWPUB.java,v 1.4 2003/10/22 23:51:02 heer Exp $
 */
public class FrameWPUB extends FrameW {
    public String getLongName() {
        return "Publisher URL frame";
    }

    public FrameWPUB(ID3V2Frame frm) {
        super(frm);
    }

    public FrameWPUB(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + "\nURL : "
                + url;
    }

}

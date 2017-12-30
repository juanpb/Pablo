package de.ueberdosis.mp3info.id3v2;

/**
 * Band Frame
 * The 'Band/Orchestra/Accompaniment' frame is used for additional
 * information about the performers in the recording.
 *
 * @author Florian Heer
 * @version $Id: FrameTPE2.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTPE2 extends FrameT {
    public String getLongName() {
        return "Band frame";
    }

    public FrameTPE2(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTPE2(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nBand : "
                + text;
    }

}

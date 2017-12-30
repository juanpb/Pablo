package de.ueberdosis.mp3info.id3v2;

/**
 * BPM frame
 *
 * @author Florian Heer
 * @version $Id: FrameTBPM.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTBPM extends FrameT {
    public String getLongName() {
        return "BPM frame";
    }

    public FrameTBPM(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTBPM(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nBPM : "
                + text;
    }

}

package de.ueberdosis.mp3info.id3v2;

/**
 * Part of a set Frame
 * The 'Part of a set' frame is a numeric string that describes which part
 * of a set the audio came from. This frame is used if the source described
 * in the "TALB" frame is divided into several mediums, e.g. a double CD.
 * The value may be extended with a "/" character and a numeric string
 * containing the total number of parts in the set. E.g. "1/2".
 *
 * @author Florian Heer
 * @version $Id: FrameTPOS.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTPOS extends FrameT {
    public String getLongName() {
        return "Part of set frame";
    }

    public FrameTPOS(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTPOS(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nPart : "
                + text;
    }

}

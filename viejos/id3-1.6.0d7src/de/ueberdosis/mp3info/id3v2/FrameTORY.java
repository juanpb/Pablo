package de.ueberdosis.mp3info.id3v2;

/**
 * Original release year Frame
 * The 'Original release year' frame is intended for the year when the
 * original recording, if for example the music in the file should be a
 * cover of a previously released song, was released. The field is
 * formatted as in the "TYER" frame.
 *
 * @author Florian Heer
 * @version $Id: FrameTORY.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTORY extends FrameT {
    public String getLongName() {
        return "Original release year frame";
    }

    public FrameTORY(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTORY(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nOriginal release year : "
                + text;
    }

}

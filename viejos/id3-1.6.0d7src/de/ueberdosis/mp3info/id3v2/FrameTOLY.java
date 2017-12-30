package de.ueberdosis.mp3info.id3v2;

/**
 * Original lyricist Frame
 * The 'Original lyricist(s)/text writer(s)' frame is intended for the
 * text writer(s) of the original recording, if for example the music in
 * the file should be a cover of a previously released song. The text
 * writers are seperated with the "/" character.
 *
 * @author Florian Heer
 * @version $Id: FrameTOLY.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTOLY extends FrameT {
    public String getLongName() {
        return "Original lyricist frame";
    }

    public FrameTOLY(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTOLY(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nOriginal lyricist : "
                + text;
    }

}

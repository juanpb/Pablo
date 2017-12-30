package de.ueberdosis.mp3info.id3v2;

/**
 * Original album/movie/show title Frame
 * The 'Original album/movie/show title' frame is intended for the title of
 * the original recording (or source of sound), if for example the music in
 * the file should be a cover of a previously released song.
 *
 * @author Florian Heer
 * @version $Id: FrameTOAL.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTOAL extends FrameT {
    public String getLongName() {
        return "Original album frame";
    }

    public FrameTOAL(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTOAL(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nOriginal title : "
                + text;
    }

}

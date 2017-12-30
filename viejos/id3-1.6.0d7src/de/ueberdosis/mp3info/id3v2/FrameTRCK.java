package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Track frame
 * The 'Track number/Position in set' frame is a numeric string
 * containing the order number of the audio-file on its original recording.
 * This may be extended with a "/" character and a numeric string
 * containing the total numer of tracks/elements on the original recording.
 * E.g. "4/9".
 *
 * @author Florian Heer
 * @version $Id: FrameTRCK.java,v 1.7 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTRCK extends FrameT {
    public String getLongName() {
        return "Track frame";
    }

    public FrameTRCK() {
        super("TRCK");
    }

    public FrameTRCK(ID3V2Frame frame) {
        super(frame);
    }

    public FrameTRCK(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nTrackNumber: "
                + text;
    }

    public boolean canDisplay() {
        return true;
    }

    public boolean canEdit() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Track number: ");
    }

}

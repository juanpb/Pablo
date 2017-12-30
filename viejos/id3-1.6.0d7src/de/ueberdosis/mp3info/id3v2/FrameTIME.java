package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Time Frame.
 * The 'Time' frame is a numeric string in the HHMM format
 * containing the time for the recording.
 *
 * @author Florian Heer
 * @version $Id: FrameTIME.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTIME extends FrameT {
    public String getLongName() {
        return "Time frame";
    }

    public FrameTIME(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTIME(ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nTime : "
                + text;
    }

    public boolean canEdit() {
        return true;
    }

    public boolean canDisplay() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Time of recording:");
    }

}

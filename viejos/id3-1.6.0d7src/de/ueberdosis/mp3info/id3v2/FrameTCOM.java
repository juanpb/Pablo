package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Composer frame
 *
 * @author Florian Heer
 * @version $Id: FrameTCOM.java,v 1.7 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTCOM extends FrameT {
    public String getLongName() {
        return "Composer frame";
    }

    public FrameTCOM() {
        super("TCOM");
    }

    public FrameTCOM(ID3V2Frame frame) {
        super(frame);
    }

    public FrameTCOM(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nComposer : "
                + text;
    }

    public boolean canEdit() {
        return true;
    }

    public boolean canDisplay() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Composer:");
    }

}

package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Copyright frame
 *
 * @author Florian Heer
 * @version $Id: FrameTCOP.java,v 1.6 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTCOP extends FrameT {
    public String getLongName() {
        return "Copyright frame";
    }

    public FrameTCOP() {
        super("TCOP");
    }

    public FrameTCOP(ID3V2Frame frame) {
        super(frame);
    }

    public FrameTCOP(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nCopyright (c): "
                + text;
    }

    public boolean canEdit() {
        return true;
    }

    public boolean canDisplay() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Copyright:");
    }

}

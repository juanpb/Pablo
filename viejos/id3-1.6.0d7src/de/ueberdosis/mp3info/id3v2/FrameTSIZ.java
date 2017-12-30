package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Size frame
 * The 'Size' frame contains the size of the audiofile in bytes, excluding
 * the ID3v2 tag, represented as a numeric string.
 *
 * @author Florian Heer
 * @version $Id: FrameTSIZ.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTSIZ extends FrameT {
    public int legalSince() {
        return 3;
    }

    public int deprecatedSince() {
        return 4;
    }

    public String getLongName() {
        return "Size frame";
    }

    public FrameTSIZ(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTSIZ(ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nSize : "
                + text;
    }

    public boolean canDisplay() {
        return true;
    }

    public boolean canEdit() {
        return false;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Audio size:");
    }

}

package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Encoded by Frame.
 *
 * @author Florian heer
 * @version $Id: FrameTENC.java,v 1.7 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTENC extends FrameT {
    public String getLongName() {
        return "Encoder frame";
    }

    public FrameTENC() {
        super("TENC");
    }

    public FrameTENC(ID3V2Frame frame) {
        super(frame);
    }

    public FrameTENC(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nEncoded by: "
                + text;
    }

    public boolean canDisplay() {
        return true;
    }

    public boolean canEdit() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Encoded by: ");
    }

}

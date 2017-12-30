package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Title/Songname/Content description frame
 *
 * @author Florian Heer
 * @version $Id: FrameTIT2.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTIT2 extends FrameT {
    public String getLongName() {
        return "Title frame";
    }

    public FrameTIT2() {
        super("TIT2");
    }

    public FrameTIT2(ID3V2Frame frame) {
        super(frame);
    }

    public FrameTIT2(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nTitle : "
                + text;
    }


    public boolean canEdit() {
        return true;
    }

    public boolean canDisplay() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return super.createJPanel(edit, selfupdate, "Title:");
    }


}

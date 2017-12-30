package de.ueberdosis.mp3info.id3v2;

/**
 * Publisher Frame
 * The 'Publisher' frame simply contains the name of the label or publisher.
 *
 * @author Florian Heer
 * @version $Id: FrameTPUB.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTPUB extends FrameT {
    public String getLongName() {
        return "Publisher frame";
    }

    public FrameTPUB(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTPUB(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nPublisher : "
                + text;
    }

}

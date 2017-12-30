package de.ueberdosis.mp3info.id3v2;

/**
 * Title 3 Frame.
 * The 'Subtitle/Description refinement' frame is used for information
 * directly related to the contents title (e.g. "Op. 16" or "Performed
 * live at Wembley").
 *
 * @author Florian Heer
 * @version $Id: FrameTIT3.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTIT3 extends FrameT {
    public String getLongName() {
        return "Title 3 frame";
    }

    public FrameTIT3(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTIT3(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nTitle 3 : "
                + text;
    }

}

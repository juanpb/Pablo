package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.mp3info.gui.EncDescValJPanel;
import de.ueberdosis.mp3info.gui.Id3JPanel;
import de.ueberdosis.util.OutputCtr;

/**
 * User defined url frame This frame is intended for URL links concerning the
 * audiofile in a similar way to the other "W"-frames. The frame body consists
 * of a description of the string, represented as a terminated string, followed
 * by the actual URL. The URL is always encoded with ISO-8859-1. There may be
 * more than one "WXXX" frame in each tag, but only one with the same
 * description.
 * <p/>
 * &lt;Header for 'User defined URL link frame', ID: "WXXX"&gt; <br>
 * Text encoding $xx <br>
 * Description &lt;text string according to encoding&gt; $00 (00) <br>
 * URL &lt;text string&gt;
 *
 * @author Florian Heer
 * @version $Id: FrameWXXX.java,v 1.12 2005/03/06 14:58:06 heer Exp $
 */

public class FrameWXXX extends ID3V2Frame implements EncDescValFrame {

    private byte encoding = 0;

    private String description = "";

    private String value = "";

    /**
     * the text to be displayed in this Frames JComponent
     */
    private static final String caption1 = "URL description:";

    /**
     * the text to be displayed in this Frames JComponent
     */
    private static final String caption2 = "URL:";

    public String getLongName() {
        return "User defined url frame";
    }

    public FrameWXXX() {
        super("WXXX");
    }

    public FrameWXXX(ID3V2Frame frame) {
        super(frame);
        if (frame instanceof FrameWXXX) {
            FrameWXXX frm = (FrameWXXX) frame;
            encoding = frm.encoding;
            description = new String(frm.description);
            value = new String(frm.value);
        }
    }

    public FrameWXXX(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);

        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte();
            description = ds.readString(encoding);
            value = ds.readString(ENCODING_BYTE_ISO88591);
        } catch (DatamismatchException ex) {
            OutputCtr.println(0, getLongName() + "\n" + ex);
        }
    }

    public String toString() {
        return getLongName() + "\nEncoding   : " + encoding
                + "\nDescription: " + description + "\nURL        : " + value;
    }

    public boolean containsData() {
        return (description.length() > 0 || value.length() > 0);
    }

    public boolean canDisplay() {
        return true;
    }

    public boolean canEdit() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return new EncDescValJPanel(edit, selfupdate, caption1, description,
                caption2, value, this);
    }

    public void setDescription(String txt) {
        if (!txt.equals(description)) {
            dataChanged = true;
            description = new String(txt);
        }
    }

    public String getDescription() {
        return new String(description);
    }

    public void setValue(String txt) {
        if (!txt.equals(value)) {
            dataChanged = true;
            value = new String(txt);
        }
    }

    public byte[] getData() {
        byte[] descAr = Helper.getStringBytes(description, encoding);
        byte[] valAr = Helper.getStringBytes(value, ENCODING_BYTE_ISO88591);
        int delimSize = Helper.getStringDelimiterSize(encoding);
        byte[] bAr = new byte[descAr.length + valAr.length + delimSize + 1];
        bAr[0] = encoding;
        System.arraycopy(descAr, 0, bAr, 1, descAr.length);
        for(int i = 0; i < delimSize; i++)
            bAr[descAr.length + 1 + i] = 0;
        System.arraycopy(valAr, 0, bAr, descAr.length + 1 + delimSize,
                valAr.length);
        return bAr;
    }

    public String getValue() {
        return new String(value);
    }
}
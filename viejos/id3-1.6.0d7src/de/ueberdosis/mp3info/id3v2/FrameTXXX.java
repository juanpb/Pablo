package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.util.OutputCtr;

/**
 * User defined text frame This frame is intended for one-string text
 * information concerning the audiofile in a similar way to the other
 * "T"-frames. The frame body consists of a description of the string,
 * represented as a terminated string, followed by the actual string. There may
 * be more than one "TXXX" frame in each tag, but only one with the same
 * description.
 * <p/>
 * &lt;Header for 'User defined text information frame', ID: "TXXX"&gt; <br>
 * Text encoding $xx <br>
 * Description &lt;text string according to encoding&gt; $00 (00) <br>
 * Value &lt;text string according to encoding&gt;
 *
 * @author Florian Heer
 * @version $Id: FrameTXXX.java,v 1.5 2005/03/05 20:40:33 heer Exp $
 */
public class FrameTXXX extends ID3V2Frame {

    private byte encoding = 0;

    private String description = "";

    private String value = "";

    public String getLongName() {
        return "User defined text frame";
    }

    public FrameTXXX(ID3V2Frame frame) {
        super(frame);
        if (frame instanceof FrameTXXX) {
            FrameTXXX frm = (FrameTXXX) frame;
            encoding = frm.encoding;
            description = frm.description;
            value = frm.value;
        }
    }

    public FrameTXXX(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);

        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte();
            description = ds.readString(encoding);
            value = ds.readString(encoding);
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        } catch (DatamismatchException dex) {
            OutputCtr.println(4, dex);
        }
    }

    public String toString() {
        return getLongName() + "\nDescription: " + description
                + "\nValue      : " + value;
    }

    public byte getEncodingByte() {
        return encoding;
    }

    public void setEncodingByte(byte b) throws DatamismatchException {
        if (b < 0 || b > 3)
            throw new DatamismatchException("Setting encoding to " + b
                    + " is illegal. Legal values are " + "0 - 3.");
        encoding = b;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String v) {
        value = v;
    }

    public byte[] getData() {
        byte[] descAr = Helper.getStringBytes(description, encoding);
        byte[] valAr = Helper.getStringBytes(value, encoding);
        byte[] bAr = Helper.reallocateByteBuffer(descAr.length + valAr.length
                + Helper.getStringDelimiterSize(encoding) + 1);
        bAr[0] = encoding;
        System.arraycopy(descAr, 0, bAr, 1, descAr.length);
        for(int i = 1; i < Helper.getStringDelimiterSize(encoding); i++)
            bAr[descAr.length + i] = 0;
        System.arraycopy(valAr, 0, bAr, descAr.length
                + Helper.getStringDelimiterSize(encoding) + 1, valAr.length);
        return bAr;
    }

}
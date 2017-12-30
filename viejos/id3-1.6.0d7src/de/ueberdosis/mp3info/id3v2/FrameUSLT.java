package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.util.OutputCtr;

/**
 * Unsychronised lyrics/text transcription This frame contains the lyrics of the
 * song or a text transcription of other vocal activities. The head includes an
 * encoding descriptor and a content descriptor. The body consists of the actual
 * text. The 'Content descriptor' is a terminated string. If no descriptor is
 * entered, 'Content descriptor' is $00 (00) only. Newline characters are
 * allowed in the text. There may be more than one 'Unsynchronised lyrics/text
 * transcription' frame in each tag, but only one with the same language and
 * content descriptor.
 * <p/>
 * &lt;Header for 'Unsynchronised lyrics/text transcription', ID: "USLT"&gt;<br>
 * Text encoding $xx <br>
 * Language $xx xx xx <br>
 * Content descriptor &lt;text string according to encoding&gt; $00 (00)<br>
 * Lyrics/text &lt;full text string according to encoding&gt;
 *
 * @author Florian Heer
 * @version $Id: FrameUSLT.java,v 1.6 2005/03/05 20:25:01 heer Exp $
 */
public class FrameUSLT extends ID3V2Frame {

    private byte encoding = 0;

    private String language = "";

    private String description = "";

    private String text = "";

    public String getLongName() {
        return "Unsychronised lyrics/text transcription frame";
    }

    public FrameUSLT(ID3V2Frame frame) {
        super(frame);
        if (frame instanceof FrameUSLT) {
            FrameUSLT frm = (FrameUSLT) frame;
            language = frm.language;
            description = frm.description;
            text = frm.text;
            encoding = frm.encoding;
        }
    }

    public FrameUSLT(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte();
            language = new String(ds.getBytes(3));
            description = ds.readString(encoding);

            text = new String(ds.getBytes(ds.getBytesLeft()));
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        } catch (DatamismatchException dex) {
            OutputCtr.println(4, dex);
        }
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nlanguage: "
                + language + "\ntext: " + text;
    }

    public byte[] getData() {
        byte[] descAr = Helper.getStringBytes(description, encoding);
        byte[] textAr = Helper.getStringBytes(text, encoding);
        byte[] langAr = language.getBytes();
        byte[] bAr = Helper.reallocateByteBuffer(4 + descAr.length
                + textAr.length + Helper.getStringDelimiterSize(encoding),
                null);
        bAr[0] = encoding;
        System.arraycopy(langAr, 0, bAr, 1, 3);
        System.arraycopy(textAr, 0, bAr, 4, descAr.length);
        for(int i = 1; i < Helper.getStringDelimiterSize(encoding); i++)
            bAr[4 + descAr.length + i] = 0;
        System.arraycopy(textAr, 0, bAr, 4 + descAr.length
                + Helper.getStringDelimiterSize(encoding), textAr.length);
        return bAr;
    }

}
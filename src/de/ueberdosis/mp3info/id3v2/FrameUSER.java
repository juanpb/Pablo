package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Terms of use frame This frame contains a brief description of the terms of
 * use and ownership of the file. More detailed information concerning the legal
 * terms might be available through the "WCOP" frame. Newlines are allowed in
 * the text. There may only be one "USER" frame in a tag.
 * <p>
 * &lt;Header for 'Terms of use frame', ID: "USER"&gt; <br>
 * Text encoding $xx <br>
 * Language $xx xx xx <br>
 * The actual text <text string according to encoding> <br>
 * 
 * @author Florian Heer
 * @version $Id: FrameUSER.java,v 1.5 2005/03/05 20:25:01 heer Exp $
 */

public class FrameUSER extends ID3V2Frame {
    private byte encoding = 0;

    private String language = "";

    private String text = "";

    public String getLongName () {
        return "Terms of use frame";
    }

    public FrameUSER (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FrameUSER) {
            FrameUSER fr = (FrameUSER) frm;
            encoding = fr.encoding;
            language = fr.language;
            text = fr.text;
        }
    }

    public FrameUSER (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte ();
            language = new String (ds.getBytes (3));
            text = new String (ds.getBytes (ds.getBytesLeft ()));
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte[] getData () {
        byte[] langAr = language.getBytes ();
        byte[] textAr = Helper.getStringBytes (text, encoding);
        byte[] bAr = Helper.reallocateByteBuffer (4 + textAr.length);
        bAr[0] = encoding;
        System.arraycopy (langAr, 0, bAr, 1, 3);
        System.arraycopy (textAr, 0, bAr, 4, textAr.length);

        return bAr;
    }

    public String toString () {
        return getLongName () + "\nEncoding: " + encoding + "\nLanguage:  "
                + language + "\nText: " + text;
    }

}
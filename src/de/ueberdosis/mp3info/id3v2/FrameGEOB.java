package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.util.OutputCtr;

/**
 * General encapsulated object <br>
 * In this frame any type of file can be encapsulated. After the header, 'Frame
 * size' and 'Encoding' follows 'MIME type' represented as as a terminated
 * string encoded with ISO-8859-1. The filename is case sensitive and is encoded
 * as 'Encoding'. Then follows a content description as terminated string,
 * encoded as 'Encoding'. The last thing in the frame is the actual object. The
 * first two strings may be omitted, leaving only their terminations. There may
 * be more than one "GEOB" frame in each tag, but only one with the same content
 * descriptor.
 * <p>
 * &lt;Header for 'General encapsulated object', ID: "GEOB"&gt; <br>
 * Text encoding $xx <br>
 * MIME type &lt;text string&gt; $00 <br>
 * Filename &lt;text string according to encoding&gt; $00 (00) <br>
 * Content description $00 (00) <br>
 * Encapsulated object &lt;binary data&gt;
 * 
 * @author Florian Heer
 * @version $Id: FrameGEOB.java,v 1.6 2005/03/06 14:46:43 heer Exp $
 */

public class FrameGEOB extends ID3V2Frame {
    private byte encoding = 0;

    private String mimeType = "";

    private String fileName = "";

    private String contentDescription = "";

    private byte[] fileData = null;

    public String getLongName () {
        return "General encapsulated object frame";
    }

    public FrameGEOB (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FrameGEOB) {
            FrameGEOB fr = (FrameGEOB) frm;
            encoding = fr.encoding;
            mimeType = fr.mimeType;
            fileName = fr.fileName;
            contentDescription = fr.contentDescription;
            fileData = new byte[fr.fileData.length];
            System.arraycopy (fr.fileData, 0, fileData, 0, fr.fileData.length);
        }
    }

    public FrameGEOB (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte ();
            mimeType = ds.readString (ENCODING_BYTE_ISO88591);
            fileName = ds.readString (encoding);
            contentDescription = ds.readString (encoding);
            fileData = ds.getBytes (ds.getBytesLeft ());
        } catch (DatamismatchException ex) {
            OutputCtr.println (0, getLongName () + "\n" + ex);
        }
    }

    public String toString () {
        return getLongName () + "\n";
    }

    public byte[] getData () {
        int delimSize = Helper.getStringDelimiterSize (encoding);
        byte[] mimeAr = Helper
                .getStringBytes (mimeType, ENCODING_BYTE_ISO88591);
        byte[] fnAr = Helper.getStringBytes (fileName, encoding);
        byte[] descAr = Helper.getStringBytes (contentDescription, encoding);
        byte[] bAr = new byte[mimeAr.length + fnAr.length + descAr.length
                + fileData.length + 2 + (2 * delimSize)];
        int position = 0;
        bAr[position++] = encoding;
        System.arraycopy (mimeAr, 0, bAr, position, mimeAr.length);
        position += mimeAr.length;
        bAr[position++] = 0;
        System.arraycopy (fnAr, 0, bAr, position, fnAr.length);
        for (int i = 0; i < delimSize; i++)
            bAr[position++] = 0;
        System.arraycopy (descAr, 0, bAr, position, descAr.length);
        position += descAr.length;
        for (int i = 0; i < delimSize; i++)
            bAr[position++] = 0;
        System.arraycopy (fileData, 0, bAr, position, fileData.length);
        return bAr;
    }

}
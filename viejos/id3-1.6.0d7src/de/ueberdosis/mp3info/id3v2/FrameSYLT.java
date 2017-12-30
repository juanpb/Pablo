package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.id3v2.datatypes.TextData;
import de.ueberdosis.util.OutputCtr;

import java.util.Vector;

/**
 * Sychronised lyrics/text transcription
 *
 * @author Florian Heer
 * @version $Id: FrameSYLT.java,v 1.9 2005/03/06 00:17:00 heer Exp $
 */
// FIXME this frame has some serious implementation problems. 
public class FrameSYLT extends ID3V2Frame {
    private byte encoding = 0;

    // TODO evaluate
    private byte timeStampFormat = 0;

    private String language = "";

    // TODO evaluate
    private String description = "";

    // TODO evaluate
    private byte contentType = 0;

    private Vector texts = new Vector();

    private byte[] data = null;

    public String getLongName() {
        return "Sychronised lyrics/text transcription frame";
    }

    public FrameSYLT(ID3V2Frame frm) {
        super(frm);
        // FIXME!
        // Copying has to be performed!
    }

    public FrameSYLT(ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte();
            language = new String(ds.getBytes(3));
            timeStampFormat = ds.getByte();
            contentType = ds.getByte();
            byte[] bAr = ds.getBytesTo((byte) 0x00);
            if (bAr != null)
                description = new String(bAr);
            // Have to skip the terminator byte
            ds.getByte();

            data = ds.getBytes(ds.getBytesLeft());
            decodeData();
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    // FIXME the data are not encoding sensitive nor does the text
    // seem to be stored anywhere...
    private void decodeData() {
        int i = 0;
        int j = 0;
        TextData td;
        byte[] buffer, buffer2;
        byte b;
        boolean stop;
        while (i < data.length) {
            td = new TextData();
            buffer = Helper.reallocateByteBuffer(40, (byte[]) null);
            stop = false;
            j = 0;

            // Read the 0-terminated string
            while ((i + j) < data.length && !stop) {
                b = data[i + j];
                // text encoding not done yet!
                if (b != 0x00) {
                    if (j >= buffer.length)
                        buffer = Helper.reallocateByteBuffer(40, buffer);
                    if (buffer != null)
                        buffer[j] = b;
                } else
                    stop = true;
                j++;
            }

            i += j;

            buffer2 = new byte[j];
            System.arraycopy(buffer, 0, buffer2, 0, j);
            td.setTimeStamp(Helper.decodeBytesToInt(new byte[]{data[i],
                    data[i + 1], data[i + 2], data[i + 3]}));
            //texts.add (td);
            texts.addElement(td);
        }
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nlanguage: "
                + language + "\nEntries: " + texts.size();
    }


    // FIXME getData () needs a real implementation.
    public byte[] getData() {
        return null;
    }

}
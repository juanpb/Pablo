package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Position synchronisation frame This frame delivers information to the
 * listener of how far into the audio stream he picked up; in effect, it states
 * the time offset of the first frame in the stream. The frame layout is:
 * <p>
 * &lt;Head for 'Position synchronisation', ID: "POSS"&gt;<br>
 * Time stamp format $xx<br>
 * Position $xx (xx ...)
 * <p>
 * Where time stamp format is:
 * <p>
 * $01 Absolute time, 32 bit sized, using MPEG frames as unit<br>
 * $02 Absolute time, 32 bit sized, using milliseconds as unit
 * <p>
 * and position is where in the audio the listener starts to receive, i.e. the
 * beginning of the next frame. If this frame is used in the beginning of a file
 * 
 * @author Florian Heer
 * @version $Id: FramePOSS.java,v 1.5 2005/03/06 14:07:15 heer Exp $
 */

public class FramePOSS extends ID3V2Frame {
    private byte timeStampFormat = 0;

    private int position = 0;

    public String getLongName () {
        return "Position synchronisation frame";
    }

    public FramePOSS (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FramePOSS) {
            FramePOSS fr = (FramePOSS) frm;
            timeStampFormat = fr.timeStampFormat;
            position = fr.position;
        }
    }

    public FramePOSS (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            timeStampFormat = ds.getByte ();
            position = Helper.decodeBytesToInt (ds
                    .getBytes (ds.getBytesLeft ()));
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte [] getData () {
        // According to documentation, the size of the timestamp is 32 bit.
        byte [] bAr = new byte [5];
        bAr [0] = timeStampFormat;
        byte [] timeAr = Helper.intToByteArray(position, 4);
        System.arraycopy(timeAr, 0, bAr, 1, 4);
        return bAr;
    }

    public String toString () {
        return getLongName () + "\ntimeStampFormat: " + timeStampFormat
                + "\nPosition:  " + position;
    }

}
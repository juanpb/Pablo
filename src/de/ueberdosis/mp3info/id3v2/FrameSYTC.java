package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.id3v2.datatypes.TimeCode;
import de.ueberdosis.util.OutputCtr;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Synchronised tempo codes For a more accurate description of the tempo of a
 * musical piece this frame might be used. After the header follows one byte
 * describing which time stamp format should be used. Then follows one or more
 * tempo codes. Each tempo code consists of one tempo part and one time part.
 * The tempo is in BPM described with one or two bytes. If the first byte has
 * the value $FF, one more byte follows, which is added to the first giving a
 * range from 2 - 510 BPM, since $00 and $01 is reserved. $00 is used to
 * describe a beat-free time period, which is not the same as a music-free time
 * period. $01 is used to indicate one single beat-stroke followed by a
 * beat-free period.
 * <p>
 * The tempo descriptor is followed by a time stamp. Every time the tempo in the
 * music changes, a tempo descriptor may indicate this for the player. All tempo
 * descriptors should be sorted in chronological order. The first beat-stroke in
 * a time-period is at the same time as the beat description occurs. There may
 * only be one "SYTC" frame in each tag.
 * <p>
 * &lt;Header for 'Synchronised tempo codes', ID: "SYTC"&gt;<br>
 * Time stamp format $xx<br>
 * Tempo data &lt;binary data&gt;<br>
 * <p>
 * Where time stamp format is:
 * <p>
 * $01 Absolute time, 32 bit sized, using MPEG frames as unit<br> 
 * $02 Absolute time, 32 bit sized, using milliseconds as unit<br>
 *<p> 
 * Abolute time means that every stamp contains the time from the beginning of
 * the file.
 * 
 * @author Florian Heer
 * @version $Id: FrameSYTC.java,v 1.6 2005/03/06 00:06:51 heer Exp $
 */
public class FrameSYTC extends ID3V2Frame {

    private byte[] codes;

    private Vector tempoCodes = new Vector ();

    private int timeStampFormat = 0;

    public static final String[] timeFormats = { "N/A",
            "Absolute time, 32 bit sized, using MPEG frames as unit",
            "Absolute time, 32 bit sized, using milliseconds as unit", "N/A" };

    public String getLongName() {
        return "Synchronized tempo codes frame";
    }

    public FrameSYTC(ID3V2Frame frm) {
        super (frm);
        // FIXME!
        // Copying has to be performed.
    }

    public FrameSYTC(ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super (frm);

        // Decoding our information from the DataSource!
        try {
            timeStampFormat = (ds.getByte () & 0xff);

            // The value is the rest of the frame
            codes = ds.getBytes (ds.getBytesLeft ());
            // Decoding the values contained in the data
            decodeCodeData ();
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    /** Decodes the binary data to an internal, readable presentation of the data.
     * It fills the internal variable tempoCodes.
     */
    private void decodeCodeData() {
        int i = 0;
        TimeCode tc = null;
        byte[] bAr;
        while (i < codes.length) {
            tc = new TimeCode ();
            bAr = new byte[] { codes[i], codes[i + 1], codes[i + 2],
                    codes[i + 3] };

            tc.setTimeStamp(Helper.decodeBytesToInt (bAr));
            if (codes[i + 4] == 0xff) {
                tc.setBpm (255 + (codes[i + 5] & 0xff));
                i += 5;
            } else {
                tc.setBpm((codes[i + 4] & 0xff));
                i += 4;
            }
            //tempoCodes.add (tc);
            tempoCodes.addElement (tc);
        }
    }
    
    public String toString() {
        return getLongName () + "\nNumber of events : " + tempoCodes.size ();
    }

    public byte [] getData () {
        Enumeration enume = tempoCodes.elements();
        TimeCode tc = null;
        int size = 0;
        // Compute size of data. If the bpm-fiel is bigger than 255 it will
        // use 5 instead of 4 bytes.
        while (enume.hasMoreElements()) {
            tc = (TimeCode)enume.nextElement();
            size += tc.getBpm () > 255 ? 4 : 5;
        }
        byte [] bAr = new byte [size + 1];
        bAr [0] = (byte)timeStampFormat;
        int position = 1;
        enume = tempoCodes.elements();
        while (enume.hasMoreElements()) {
            byte [] currTC = Helper.timeCodeToSYTC(((TimeCode)enume.nextElement()));
            System.arraycopy(currTC, 0, bAr, position, currTC.length);
            position += currTC.length;
        }
        return bAr;
    }
}
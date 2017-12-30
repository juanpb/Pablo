package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

import java.util.Vector;

/**
 * Event timing codes frame<br>
 * This frame allows synchronisation with key events in
 * a song or sound. The header is:
 * <p/>
 * &lt;Header for 'Event timing codes', ID: "ETCO"&gt;<br>
 * Time stamp format $xx
 * <p/>
 * Where time stamp format is:<br>
 * $01  Absolute time, 32 bit sized, using MPEG [MPEG] frames as unit<br>
 * $02  Absolute time, 32 bit sized, using milliseconds as unit
 * <p/>
 * Abolute time means that every stamp contains the time from the beginning of
 * the file.
 * <p/>
 * Followed by a list of key events in the following format:
 * <p/>
 * Type of event $xx<br>
 * Time stamp $xx (xx ...)
 * <p/>
 * The 'Time stamp' is set to zero if directly at the beginning of the sound or
 * after the previous event. All events should be sorted in chronological order.
 * <p/>
 * FIXME getData is only a dummy implementation as I will have to analyze if I really did this Frame correctly :-)
 *
 * @author Florian heer
 * @version $Id: FrameETCO.java,v 1.6 2005/03/06 15:15:53 heer Exp $
 */
public class FrameETCO extends ID3V2Frame {
    private byte[] codes;

    private Vector timeCodes = new Vector();

    /**
     * TODO evaluate this flag
     */
    private int timeStampFormat = 0;

    public static final String[] timeFormats = {"N/A",
            "Absolute time, 32 bit sized, using MPEG frames as unit",
            "Absolute time, 32 bit sized, using milliseconds as unit", "N/A"};

    /**
     * Event definitions for i = 0x00 to i = 0x14
     */
    public static final String[] eventsFirstBlock = {
            "padding (has no meaning)", "end of initial silence",
            "intro start", "mainpart start", "outro start", "outro end",
            "verse start", "refrain start", "interlude start", "theme start",
            "variation start", "key change", "time change",
            "momentary unwanted noise (Snap, Crackle & Pop)",
            "sustained noise", "sustained noise end", "intro end",
            "mainpart end", "verse end", "refrain end", "theme end"};

    /**
     * Event definitions ofr i=0xE0 to i = 0xff
     */
    public static final String[] eventsSecondBlock = {
            "not predefined sync (0xe0)",
            "not predefined sync (0xe1)",
            "not predefined sync (0xe2)",
            "not predefined sync (0xe3)",
            "not predefined sync (0xe4)",
            "not predefined sync (0xe5)",
            "not predefined sync (0xe6)",
            "not predefined sync (0xe7)",
            "not predefined sync (0xe8)",
            "not predefined sync (0xe9)",
            "not predefined sync (0xea)",
            "not predefined sync (0xeb)",
            "not predefined sync (0xec)",
            "not predefined sync (0xed)",
            "not predefined sync (0xee)",
            "not predefined sync (0xef)",
            "reserved for future use (0xf0)",
            "reserved for future use (0xf1)",
            "reserved for future use (0xf2)",
            "reserved for future use (0xf3)",
            "reserved for future use (0xf4)",
            "reserved for future use (0xf5)",
            "reserved for future use (0xf6)",
            "reserved for future use (0xf7)",
            "reserved for future use (0xf8)",
            "reserved for future use (0xf9)",
            "reserved for future use (0xfa)",
            "reserved for future use (0xfb)",
            "reserved for future use (0xfc)",
            "audio end (start of silence)",
            "audio file ends",
            "one more byte of events follows (all the following bytes with the value $FF have the same function)"};

    public FrameETCO(ID3V2Frame frm) {
        super(frm);
        // FIXME!
        // Copying has to be performed!
    }

    public static String getEventName(int i) {
        if (i >= 0 && i < 21)
            return eventsFirstBlock[i];
        if (i >= 21 && i < 234)
            return "reserved for future use (" + Helper.byteToHexString(i)
                    + ")";
        if (i < 256)
            return eventsSecondBlock[i - 234];
        // if out of range...
        return "Undefinable Event (" + i + " < 0 || " + i + " > 255)";
    }

    public String getLongName() {
        return "Event timing codes frame";
    }

    public FrameETCO(ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super(frm);

        // Decoding our information from the DataSource!
        try {
            timeStampFormat = (ds.getByte() & 0xff);

            // The value is the rest of the frame
            codes = ds.getBytes(ds.getBytesLeft());
            // decoding the data
            decodeEventData();
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    private void decodeEventData() {
        int i = 0;
        Integer timeStamp;
        Integer event = null;
        byte[] bAr;
        EventTime et = null;
        while (i < codes.length) {
            et = new EventTime();
            bAr = new byte[]{codes[i], codes[i + 1], codes[i + 2],
                    codes[i + 3]};

            et.timeStamp = Helper.decodeBytesToInt(bAr);
            et.event = (codes[i + 4] & 0xff);

            i += 5;
            //timeCodes.add (et);
            timeCodes.addElement(et);
        }
    }

    /**
     * FIXME Dummy implementation to get this to compile
     */
    public byte[] getData() {
        return null;
    }

    public String toString() {
        return getLongName() + "\nNumber of events : " + timeCodes.size();
    }

    public class EventTime {
        public int timeStamp = 0;

        public int event = 0;
    }

}
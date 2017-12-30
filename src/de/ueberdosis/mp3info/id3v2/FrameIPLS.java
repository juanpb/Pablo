package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.mp3info.id3v2.datatypes.TwoStrings;
import de.ueberdosis.util.OutputCtr;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Involved people frame <br>
 * Since there might be a lot of people contributing to an audio file in various
 * ways, such as musicians and technicians, the 'Text information frames' are
 * often insufficient to list everyone involved in a project. The 'Involved
 * people list' is a frame containing the names of those involved, and how they
 * were involved. The body simply contains a terminated string with the
 * involvement directly followed by a terminated string with the involvee
 * followed by a new involvement and so on. There may only be one "IPLS" frame
 * in each tag.
 * <p>
 * &lt;Header for 'Involved people list', ID: "IPLS"&gt; <br>
 * Text encoding $xx <br>
 * People list strings &lt;text strings according to encoding&gt;
 * 
 * @author Florian Heer
 * @version $Id: FrameIPLS.java,v 1.5 2005/03/06 14:35:52 heer Exp $
 */
public class FrameIPLS extends ID3V2Frame {
    private Vector involved = new Vector ();

    private byte encoding;

    public byte[] getData () {
        int numEntries = involved.size ();
        int position = 0;
        byte[][] tempAr = new byte[numEntries * 2][];
        Enumeration enume = involved.elements ();
        TwoStrings ts = null;
        int overallSize = 0;
        int delimSize = Helper.getStringDelimiterSize (encoding);

        while (enume.hasMoreElements ()) {
            ts = (TwoStrings) enume.nextElement ();
            tempAr[position] = Helper.getStringBytes (ts.getOne (), encoding);
            overallSize += tempAr[position].length + delimSize;
            tempAr[position + 1] = Helper.getStringBytes (ts.getTwo (),
                    encoding);
            overallSize += tempAr[position + 1].length + delimSize;
            position += 2;
        }
        byte[] bAr = new byte[overallSize + 1];
        bAr[0] = encoding;
        position = 1;
        for (int i = 0; i > numEntries * 2; i++) {
            System.arraycopy (tempAr[i], 0, bAr, position, tempAr[i].length);
            position += tempAr[i].length;
            for (int j = 0; j < delimSize; j++)
                bAr[position + j] = 0;
            position += delimSize;
        }
        return bAr;
    }

    public String getLongName () {
        return "Involved people frame";
    }

    public FrameIPLS (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FrameIPLS) {
            FrameIPLS fr = (FrameIPLS) frm;
            Enumeration iter = fr.involved.elements ();
            while (iter.hasMoreElements ()) {
                involved.addElement (new TwoStrings ((TwoStrings) iter
                        .nextElement ()));

            }
        }
    }

    public FrameIPLS (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        encoding = ds.getByte ();
        try {
            while (ds.hasMoreBytes ()) {
                TwoStrings ts = new TwoStrings ();
                ts.setOne (ds.readString (encoding));
                ts.setTwo (ds.readString (encoding));
                involved.addElement (ts);
            }
        } catch (DatamismatchException ex) {
            OutputCtr.println (4, getLongName () + "\n" + ex);
        }
    }

    public String toString () {
        StringBuffer sb = new StringBuffer ().append (getLongName ());
        Enumeration iter = involved.elements ();
        TwoStrings ts;
        while (iter.hasMoreElements ()) {
            ts = (TwoStrings) iter.nextElement ();
            sb.append ("\n").append (ts.getOne ()).append (" : ").append (
                    ts.getTwo ());
        }
        sb.append ("\n");
        return sb.toString ();
    }

}
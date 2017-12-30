package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Play counter <br>
 * This is simply a counter of the number of times a file has been played. The
 * value is increased by one every time the file begins to play. There may only
 * be one "PCNT" frame in each tag. When the counter reaches all one's, one byte
 * is inserted in front of the counter thus making the counter eight bits
 * bigger. The counter must be at least 32-bits long to begin with.
 * <p/>
 * &lt;Header for 'Play counter', ID: "PCNT"&gt; <br>
 * Counter $xx xx xx xx (xx ...) <br>
 *
 * @author Florian Heer
 * @version $Id: FramePCNT.java,v 1.5 2005/03/06 14:35:24 heer Exp $
 */

public class FramePCNT extends ID3V2Frame {
    private int playCounter = 0;

    public String getLongName() {
        return "Play counter frame";
    }

    public FramePCNT(ID3V2Frame frm) {
        super(frm);
        if (frm instanceof FramePCNT) {
            playCounter = ((FramePCNT) frm).playCounter;
        }
    }

    public FramePCNT(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource!
        try {
            byte[] bAr = ds.getBytes(ds.getBytesLeft());
            if (bAr != null)
                playCounter = Helper.decodeBytesToInt(bAr);
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte[] getData() {
        return Helper.intToByteArray(playCounter);
    }

    public String toString() {
        return getLongName() + "\nPlayed: " + playCounter;
    }

}
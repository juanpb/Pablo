package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Equalisation Frame <br>
 * This is another subjective, alignment frame. It allows the user to predefine
 * an equalisation curve within the audio file. There may only be one "EQUA"
 * frame in each tag.
 * <p/>
 * &lt;Header of 'Equalisation', ID: "EQUA"&gt; <br>
 * Adjustment bits $xx
 * <p/>
 * The 'adjustment bits' field defines the number of bits used for
 * representation of the adjustment. This is normally $10 (16 bits) for MPEG 2
 * layer I, II and III and MPEG 2.5. This value may not be $00.
 * <p/>
 * This is followed by 2 bytes + ('adjustment bits' rounded up to the nearest
 * byte) for every equalisation band in the following format, giving a frequency
 * range of 0 - 32767Hz:
 * <p/>
 * Increment/decrement %x (MSB of the Frequency) Frequency (lower 15 bits)
 * Adjustment $xx (xx ...)
 * <p/>
 * The increment/decrement bit is 1 for increment and 0 for decrement. The
 * equalisation bands should be ordered increasingly with reference to
 * frequency. All frequencies don't have to be declared. The equalisation curve
 * in the reading software should be interpolated between the values in this
 * frame. Three equal adjustments for three subsequent frequencies. A frequency
 * should only be described once in the frame.
 * <p/>
 * FIXME getData is only a dummy implementation, I have to see if this Frame is
 * implemented correctly.
 *
 * @author Florian Heer
 * @version $Id: FrameEQUA.java,v 1.8 2005/03/06 15:15:53 heer Exp $
 */

public class FrameEQUA extends ID3V2Frame {
    private int adjustmentBits = 0;

    private byte[] data = null;

    private Vector adjustments = new Vector();

    public String getLongName() {
        return "Equalisation frame";
    }

    public FrameEQUA(ID3V2Frame frm) {
        super(frm);
        if (frm instanceof FrameEQUA) {
            FrameEQUA equa = (FrameEQUA) frm;
            this.adjustmentBits = equa.adjustmentBits;
            this.data = new byte[equa.data.length];
            System.arraycopy(equa.data, 0, this.data, 0, equa.data.length);

            Enumeration
            enum=equa.adjustments.elements();
            while (
            enum.hasMoreElements()){
                this.adjustments.addElement(((Adjustment)
                enum.nextElement())
                .clone());
            }
        }

        // FIXME!
        // Copying has to be performed.
    }

    public FrameEQUA(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource!
        try {
            adjustmentBits = (ds.getByte() & 0xff);
            data = ds.getBytes(ds.getBytesLeft());
            decodeData();
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, "Text frame can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    private void decodeData() {
        int i = 0;
        Adjustment adj;
        byte b = 0;
        byte[] bAr = null;
        while (i < data.length) {
            adj = new Adjustment();
            b = data[i];
            adj.increment = (b & 0x80) == 1;
            adj.frequency = (b & 0x7f);
            i++;
            bAr = new byte[adjustmentBits % 8 + 1];
            for(int j = 0; j <= (adjustmentBits % 8); j++)
                bAr[j] = data[i + j];
            i += (adjustmentBits % 8) + 1;
            adj.adjustment = Helper.decodeBytesToInt(bAr);
            //adjustments.add (adj);
            adjustments.addElement(adj);
        }
    }

    /**
     * FIXME dummy implementation to get this to compile
     */
    public byte[] getData() {
        return null;
    }

    public String toString() {
        return getLongName() + "\nEtries: " + adjustments.size();
    }

    public class Adjustment {
        public boolean increment = false;

        public int frequency = 0;

        public int adjustment = 0;

        public Object clone() {
            Adjustment adj = new Adjustment();
            adj.increment = this.increment;
            adj.frequency = this.frequency;
            adj.adjustment = this.adjustment;
            return adj;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Adjustment))
                return false;
            Adjustment adj = (Adjustment) o;
            return adj.increment == this.increment
                    || adj.frequency == this.frequency
                    || adj.adjustment == this.adjustment;
        }
    }

}
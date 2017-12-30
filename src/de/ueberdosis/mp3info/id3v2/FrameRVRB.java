package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Reverb frame <br>
 * Yet another subjective one. You may here adjust echoes of different kinds.
 * Reverb left/right is the delay between every bounce in ms. Reverb bounces
 * left/right is the number of bounces that should be made. $FF equals an
 * infinite number of bounces. Feedback is the amount of volume that should be
 * returned to the next echo bounce. $00 is 0%, $FF is 100%. If this value were
 * $7F, there would be 50% volume reduction on the first bounce, 50% of that on
 * the second and so on. Left to left means the sound from the left bounce to be
 * played in the left speaker, while left to right means sound from the left
 * bounce to be played in the right speaker.
 * <p>
 * 'Premix left to right' is the amount of left sound to be mixed in the right
 * before any reverb is applied, where $00 is 0% and $FF is 100%. 'Premix right
 * to left' does the same thing, but right to left. Setting both premix to $FF
 * would result in a mono output (if the reverb is applied symmetric). There may
 * only be one "RVRB" frame in each tag.
 * <p>
 * &lt;Header for 'Reverb', ID: "RVRB"&gt; <br>
 * Reverb left (ms) $xx xx <br>
 * Reverb right (ms) $xx xx <br>
 * Reverb bounces, left $xx <br>
 * Reverb bounces, right $xx <br>
 * Reverb feedback, left to left $xx <br>
 * Reverb feedback, left to right $xx <br>
 * Reverb feedback, right to right $xx <br>
 * Reverb feedback, right to left $xx <br>
 * Premix left to right $xx <br>
 * Premix right to left $xx
 * 
 * @author Florian Heer
 * @version $Id: FrameRVRB.java,v 1.6 2005/03/06 00:32:47 heer Exp $
 */

public class FrameRVRB extends ID3V2Frame {
    private int reverbLeft = 0;

    private int reverbRight = 0;

    private int bouncesLeft = 0;

    private int bouncesRight = 0;

    private int feedbackLeftLeft = 0;

    private int feedbackLeftRight = 0;

    private int feedbackRightRight = 0;

    private int feedbackRightLeft = 0;

    private int premixLeftRight = 0;

    private int premixRightLeft = 0;

    public String getLongName () {
        return "Reverb frame";
    }

    public FrameRVRB (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FrameRVRB) {
            FrameRVRB fr = (FrameRVRB) frm;
            reverbLeft = fr.reverbLeft;
            reverbRight = fr.reverbRight;
            bouncesLeft = fr.bouncesLeft;
            bouncesRight = fr.bouncesRight;
            feedbackLeftLeft = fr.feedbackLeftLeft;
            feedbackLeftRight = fr.feedbackLeftRight;
            feedbackRightRight = fr.feedbackRightRight;
            feedbackRightLeft = fr.feedbackRightLeft;
            premixLeftRight = fr.premixLeftRight;
            premixRightLeft = fr.premixRightLeft;
        }
    }

    public FrameRVRB (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            byte[] tAr = new byte[2];
            tAr[0] = ds.getByte ();
            tAr[1] = ds.getByte ();
            reverbLeft = Helper.decodeBytesToInt (tAr);
            tAr[0] = ds.getByte ();
            tAr[1] = ds.getByte ();
            reverbRight = Helper.decodeBytesToInt (tAr);
            bouncesLeft = (ds.getByte () & 0xff);
            bouncesRight = (ds.getByte () & 0xff);
            feedbackLeftLeft = (ds.getByte () & 0xff);
            feedbackLeftRight = (ds.getByte () & 0xff);
            feedbackRightRight = (ds.getByte () & 0xff);
            feedbackRightLeft = (ds.getByte () & 0xff);
            premixLeftRight = (ds.getByte () & 0xff);
            premixRightLeft = (ds.getByte () & 0xff);
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte[] getData () {
        byte[] bAr = new byte[12];
        byte[] tAr = Helper.intToByteArray (reverbLeft, 2);
        bAr[0] = tAr[0];
        bAr[1] = tAr[1];
        tAr = Helper.intToByteArray (reverbRight, 2);
        bAr[2] = tAr[0];
        bAr[3] = tAr[1];
        bAr[4] = (byte) bouncesLeft;
        bAr[5] = (byte) bouncesRight;
        bAr[6] = (byte) feedbackLeftLeft;
        bAr[7] = (byte) feedbackLeftRight;
        bAr[8] = (byte) feedbackRightRight;
        bAr[9] = (byte) feedbackRightLeft;
        bAr[10] = (byte) premixLeftRight;
        bAr[11] = (byte) premixRightLeft;
        return bAr;
    }

    public String toString () {
        return getLongName () + "\n";
    }

}
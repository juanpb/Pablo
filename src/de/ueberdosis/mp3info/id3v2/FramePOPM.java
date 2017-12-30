package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Popularimeter <br>
 * The purpose of this frame is to specify how good an audio file is. Many
 * interesting applications could be found to this frame such as a playlist that
 * features better audiofiles more often than others or it could be used to
 * profile a person's taste and find other 'good' files by comparing people's
 * profiles. The frame is very simple. It contains the email address to the
 * user, one rating byte and a four byte play counter, intended to be increased
 * with one for every time the file is played. The email is a terminated string.
 * The rating is 1-255 where 1 is worst and 255 is best. 0 is unknown. If no
 * personal counter is wanted it may be omitted. When the counter reaches all
 * one's, one byte is inserted in front of the counter thus making the counter
 * eight bits bigger in the same away as the play counter ("PCNT"). There may be
 * more than one "POPM" frame in each tag, but only one with the same email
 * address.
 * <p>
 * &lt;Header for 'Popularimeter', ID: "POPM"&gt; <br>
 * Email to user &lt;text string&gt; $00 <br>
 * Rating $xx <br>
 * Counter $xx xx xx xx (xx ...)
 * 
 * @author Florian Heer
 * @version $Id: FramePOPM.java,v 1.7 2005/03/06 14:07:15 heer Exp $
 */

public class FramePOPM extends ID3V2Frame {
    private String mailAdd = "";

    private int rating = 0;

    private int playCounter = 0;

    public String getLongName () {
        return "Popularimeter frame";
    }

    public FramePOPM (ID3V2Frame frame) {
        super (frame);
        if (frame instanceof FramePOPM) {
            FramePOPM frm = (FramePOPM) frame;
            mailAdd = frm.mailAdd;
            rating = frm.rating;
            playCounter = frm.playCounter;
        }
    }

    public FramePOPM (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            byte[] bAr = ds.getBytesTo ((byte) 0);
            if (bAr != null)
                mailAdd = new String (bAr);
            // Skip terminator byte
            ds.getByte ();

            rating = (ds.getByte () & 0xff);
            if (ds.hasMoreBytes ()) {
                bAr = ds.getBytes (ds.getBytesLeft ());
                if (bAr != null)
                    playCounter = Helper.decodeBytesToInt (bAr);
            }
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte [] getData () {
        byte [] mailAr = mailAdd.getBytes();
        byte [] countAr = Helper.intToByteArray(playCounter);
        byte [] bAr = new byte [mailAr.length + countAr.length + 2];
        System.arraycopy(mailAr, 0, bAr, 0, mailAr.length);
        bAr [mailAr.length] = 0;
        bAr [mailAr.length + 1] = (byte)rating;
        System.arraycopy(countAr, 0, bAr, mailAr.length+2, countAr.length);
        return bAr;
    }

    public String toString () {
        return getLongName () + "\nUser: " + mailAdd + "\nRating: " + rating
                + "\nPlay counter: " + playCounter;
    }

}
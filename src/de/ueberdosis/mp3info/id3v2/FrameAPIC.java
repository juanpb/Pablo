package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.util.OutputCtr;

/**
 * <b>Attached Picture frame </b> <br>
 * This frame contains a picture directly related to the audio file. Image
 * format is the MIME type and subtype for the image. In the event that the MIME
 * media type name is omitted, "image/" will be implied. The "image/png" or
 * "image/jpeg" picture format should be used when interoperability is wanted.
 * Description is a short description of the picture, represented as a
 * terminated textstring. The description has a maximum length of 64 characters,
 * but may be empty. There may be several pictures attached to one file, each in
 * their individual "APIC" frame, but only one with the same content descriptor.
 * There may only be one picture with the picture type declared as picture type
 * $01 and $02 respectively. There is the possibility to put only a link to the
 * image file by using the 'MIME type' "-->" and having a complete URL instead
 * of picture data. The use of linked files should however be used sparingly
 * since there is the risk of separation of files.
 * 
 * @author Florian Heer
 * @version $Id: FrameAPIC.java,v 1.10 2005/02/24 21:22:17 heer Exp $
 */

public class FrameAPIC extends ID3V2Frame {
    private byte encoding = 0;

    private String mimeType = "";

    private byte pictureType = 0;

    private String description = "";

    private byte[] pictureData = null;

    public static final String[] pictureTypes = { "Other",
            "32x32 pixels 'file icon' (PNG only)", "Other file icon",
            "Cover (front)", "Cover (back)", "Leaflet page",
            "Media (e.g. lable side of CD)",
            "Lead artist/lead performer/soloist", "Artist/performer",
            "Conductor", "Band/Orchestra", "Composer", "Lyricist/text writer",
            "Recording Location", "During recording", "During performance",
            "Movie/video screen capture", "A bright coloured fish",
            "Illustration", "Band/artist logotype", "Publisher/Studio logotype" };

    public String getLongName () {
        return "Attached Picture frame";
    }

    /**
     * @return Returns the description.
     */
    public String getDescription () {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription (String description) {
        this.description = description;
    }

    /**
     * @return Returns the encoding.
     */
    public byte getEncoding () {
        return encoding;
    }

    /**
     * @param encoding The encoding to set.
     */
    public void setEncoding (byte encoding) {
        this.encoding = encoding;
    }

    /**
     * @return Returns the mimeType.
     */
    public String getMimeType () {
        return mimeType;
    }

    /**
     * @param mimeType The mimeType to set.
     */
    public void setMimeType (String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return Returns the pictureData.
     */
    public byte[] getPictureData () {
        return pictureData;
    }

    /**
     * @param pictureData The pictureData to set.
     */
    public void setPictureData (byte[] pictureData) {
        this.pictureData = pictureData;
    }

    /**
     * @return Returns the pictureType.
     */
    public byte getPictureType () {
        return pictureType;
    }

    /**
     * @param pictureType The pictureType to set.
     */
    public void setPictureType (byte pictureType) {
        this.pictureType = pictureType;
    }

    public boolean containsData () {
        // There must be data in this! Either it's the picture itself or some
        // URL
        if (pictureData != null && pictureData.length > 0)
            return true;
        return false;
    }

    public int legalSince () {
        return 3;
    }

    public FrameAPIC (ID3V2Frame frame) {
        super (frame);
        if (frame instanceof FrameAPIC) {
            FrameAPIC frm = (FrameAPIC) frame;
            encoding = frm.encoding;
            mimeType = frm.mimeType;
            pictureType = frm.pictureType;
            description = frm.description;
            pictureData = new byte[frm.pictureData.length];
            System.arraycopy (frm.pictureData, 0, pictureData, 0,
                    frm.pictureData.length);
        }
    }

    public FrameAPIC (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte ();
            byte[] bAr = ds.getBytesTo ((byte) 0);
            if (bAr != null)
                mimeType = new String (bAr);
            // Skip terminator byte
            ds.getByte ();

            pictureType = ds.getByte ();
            try {
                description = ds.readString (encoding);
            } catch (DatamismatchException ex) {
                OutputCtr.println (1, ex);
            }

            pictureData = ds.getBytes (ds.getBytesLeft ());
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public String toString () {

        return getLongName () + "\nDescription: " + description
                + "\nPicture type: " + pictureTypes[pictureType]
                + "\nMIME Type: " + mimeType + "\n";
    }

    public byte[] getData () {
        byte[] mimeAr = mimeType.getBytes ();
        byte[] descAr = Helper.stringToByteArray (description, encoding);
        int stringDelimiterSize = Helper.getStringDelimiterSize (encoding);
        byte[] bAr = Helper.reallocateByteBuffer (3 + stringDelimiterSize
                + mimeAr.length + descAr.length + pictureData.length, null);
        bAr[0] = encoding;
        System.arraycopy (mimeAr, 0, bAr, 1, mimeAr.length);
        bAr[mimeAr.length + 1] = 0;
        bAr[mimeAr.length + 2] = pictureType;
        System.arraycopy (descAr, 0, bAr, mimeAr.length + 3, descAr.length);
        for (int i = 1; i <= stringDelimiterSize; i++)
            bAr[mimeAr.length + 3 + descAr.length + i] = 0;
        System.arraycopy (pictureData, 0, bAr, 3 + stringDelimiterSize
                + descAr.length + mimeAr.length, pictureData.length);
        return bAr;
    }

}
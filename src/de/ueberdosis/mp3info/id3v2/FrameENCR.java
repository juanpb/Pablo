package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

import java.io.UnsupportedEncodingException;

/**
 * Encryption method registration To identify with which method a frame has been
 * encrypted the encryption method must be registered in the tag with this
 * frame. The 'Owner identifier' is a null-terminated string with a URL
 * containing an email address, or a link to a location where an email address
 * can be found, that belongs to the organisation responsible for this specific
 * encryption method. Questions regarding the encryption method should be sent
 * to the indicated email address. The 'Method symbol' contains a value that is
 * associated with this method throughout the whole tag. Values below $80 are
 * reserved. The 'Method symbol' may optionally be followed by encryption
 * specific data. There may be several "ENCR" frames in a tag but only one
 * containing the same symbol and only one containing the same owner identifier.
 * The method must be used somewhere in the tag. See section 3.3.1, flag j for
 * more information.
 * <p>
 * &lt;Header for 'Encryption method registration', ID: "ENCR"&gt;<br>
 * Owner identifier &lt;text string&gt; $00<br>
 * Method symbol $xx<br>
 * Encryption data &lt;binary data&gt;
 * 
 * @author Florian Heer
 * @version $Id: FrameENCR.java,v 1.6 2005/03/04 16:51:18 heer Exp $
 */

public class FrameENCR extends ID3V2Frame {
    public byte [] getData () {
        byte [] ownerBytes;
        try {
        ownerBytes = ownerIdentifier.getBytes(ENCODING_STRING_ISO88591);
        } catch (UnsupportedEncodingException ex) {
            OutputCtr.println(4, ex);
            ownerBytes = ownerIdentifier.getBytes();
        }
        byte [] bAr = Helper.reallocateByteBuffer(ownerBytes.length 
                + 2 + encryptionData.length, null);
        System.arraycopy(ownerBytes, 0, bAr, 0, ownerBytes.length);
        bAr [ownerBytes.length] = 0;
        bAr [ownerBytes.length + 1] = methodSymbol;
        System.arraycopy(encryptionData, 0, bAr, ownerBytes.length + 2, encryptionData.length);
        return bAr;
    }
    
    private String ownerIdentifier = "";

    private byte methodSymbol = 0;

    private byte[] encryptionData = null;

    public String getLongName () {
        return "Encryption method registration frame";
    }

    public FrameENCR (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FrameENCR) {
            FrameENCR fr = (FrameENCR) frm;
            ownerIdentifier = fr.ownerIdentifier;
            methodSymbol = fr.methodSymbol;
            encryptionData = new byte[fr.encryptionData.length];
            System.arraycopy (fr.encryptionData, 0, encryptionData, 0,
                    fr.encryptionData.length);
        }
    }

    public FrameENCR (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            byte[] bAr = ds.getBytesTo ((byte) 0);
            if (bAr != null)
                ownerIdentifier = new String (bAr);
            // Have to skip the terminator byte of the owner identifier
            ds.getByte ();

            methodSymbol = ds.getByte ();
            encryptionData = ds.getBytes (ds.getBytesLeft ());
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, getLongName ()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    // FIXME
    public String toString () {
        return getLongName () + "\n Better toString () to follow!";
    }

}
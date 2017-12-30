package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Unique file identifier frame
 * <p/>
 * &lt;Header for 'Unique file identifier', ID: "UFID"&gt; <br>
 * Owner identifier &lt;text string&gt; $00 <br>
 * Identifier &lt;up to 64 bytes binary data&gt;
 *
 * @author Florian Heer
 * @version $Id: FrameUFID.java,v 1.5 2005/03/05 20:32:10 heer Exp $
 */

public class FrameUFID extends ID3V2Frame {

    private String ownerIdentifier = "";

    private byte[] identifier = null;

    public String getLongName() {
        return "Unique file identifier";
    }

    public FrameUFID(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource!
        try {
            StringBuffer sb = new StringBuffer();
            byte b = ds.getByte();
            // read in the 'Owner identifier'... 0 terminated string
            while (b != 0) {
                sb.append((char) b);
                b = ds.getByte();
            }
            ownerIdentifier = sb.toString();
            identifier = ds.getBytes(ds.getBytesLeft());
            if (identifier.length > 64)
                OutputCtr.println(1,
                        "Something's strange. FrameUFIDs identifier is "
                                + identifier.length + " bytes long...");
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, "FrameUFID can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte[] getData() {
        byte[] ownerAr = ownerIdentifier.getBytes();
        byte[] bAr = Helper.reallocateByteBuffer(ownerAr.length
                + identifier.length + 1);
        System.arraycopy(ownerAr, 0, bAr, 0, ownerAr.length);
        bAr[ownerAr.length] = 0;
        System.arraycopy(identifier, 0, bAr, ownerAr.length + 1,
                identifier.length);
        return bAr;
    }

    public String toString() {
        return getLongName() + " OI: " + ownerIdentifier + "\nident: "
                + Helper.arrayToHexString(identifier);
    }

}
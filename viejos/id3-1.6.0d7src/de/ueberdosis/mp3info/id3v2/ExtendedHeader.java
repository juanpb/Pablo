package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.mp3info.UndersizedException;
import de.ueberdosis.util.OutputCtr;

/**
 * Contains data about an extended header
 * See the ID3V2 documentation.<br>
 * <br>
 * Much to be done!
 *
 * @author Florian Heer
 * @version $Id: ExtendedHeader.java,v 1.7 2005/02/22 18:09:15 heer Exp $
 */

public class ExtendedHeader extends Header implements Defines {
    /**
     * is this tag an update to a tag earlier in this file?
     * TODO not evaluated yet
     */
    private boolean tagIsUpdate = false;
    /**
     * are data for CRC protection present?
     * TODO not evaluated yet
     */
    private boolean crcDataPresent = false;
    /**
     * is the tag restricted?
     * TODO not evaluated yet
     */
    private boolean tagRestrictions = false;

    /**
     * Creates an instance of ExtendedHeader.
     *
     * @param headBytes the binary representation of an extended header.
     *                  gets parsed.
     */
    public ExtendedHeader(byte[] headBytes) throws UndersizedException {
        super(headBytes);
        if (headBytes.length < ID3V2_X_HEADER_SIZE)
            throw new UndersizedException(headBytes.length, ID3V2_X_HEADER_SIZE);
        // The first 10 bytes are parsed, let's get to the rest.

        // FIXME! Probably gotta fetch more data!
        //
        // Size of the whole extended header. Can never be less than
        // 6 (for all the basic information) but can exceed that.
        // So the problem is to get more data.
        // As we probably do the procedure of decoding an ID3v2 tag this way:
        //   - Decode header, know the size, read all data from the current
        //     position up to (currentPosition + size)
        //   - find out we have an extended header, now let's call this with
        //     an array containing all tag-bytes
        // we already have all necessary information. Let's hope we never have
        // to read more data than the VM can handle :)

        long extendedSize = Helper.unsynchsafe
                (new byte[]{headBytes[10], headBytes[11],
                        headBytes[12], headBytes[13]});

        if (headBytes.length < (extendedSize + ID3V2_HEADER_SIZE))
            throw new UndersizedException("Extended decoding had too little data.",
                    headBytes.length,
                    (int) extendedSize + ID3V2_HEADER_SIZE);

        int numberOfFlagbytes = (headBytes[14] & 0xff);

        // FIXME! Gotta throw some severe Exception!
        if (numberOfFlagbytes > SUPPORTED_NUMBER_OF_EXTENDED_FLAG_BYTES)
            OutputCtr.println(1, "Possible data loss, can only understand "
                    + SUPPORTED_NUMBER_OF_EXTENDED_FLAG_BYTES
                    + " bytes of flags in an extended Header!");
        else {
            // Decoding the flags...
            tagIsUpdate = (headBytes[15] & 0x40) != 0;
            crcDataPresent = (headBytes[15] & 0x20) != 0;
            tagRestrictions = (headBytes[15] & 0x10) != 0;

            // FIXME!
            // Decoding of CRC-data and the tag-restrictions isn't done yet!

        }
    }
}

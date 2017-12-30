package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.mp3info.UndersizedException;

public class Header implements Defines {
    private String identifier;

    private int version;

    private int revision;

    private int flags;

    private boolean unsynchronisation = false;

    private boolean extendedHeader = false;

    private boolean experimentalIndicator = false;

    private boolean footerPresent = false;

    private boolean unknownFlagsExist = false;

    private long size;

    /** Creates a basic header */
    public Header() {
        identifier = "ID3";
        version = 3;
        revision = 0;
        flags = 0;
    }

    /** Copy constructor */
    public Header(Header oldHeader) {
        identifier = new String(oldHeader.identifier);
        version = oldHeader.version;
        revision = oldHeader.revision;
        flags = oldHeader.flags;
        unsynchronisation = oldHeader.unsynchronisation;
        extendedHeader = oldHeader.extendedHeader;
        experimentalIndicator = oldHeader.experimentalIndicator;
        footerPresent = oldHeader.footerPresent;
        unknownFlagsExist = oldHeader.unknownFlagsExist;
        size = oldHeader.size;

    }

    public Header(byte[] headBytes) throws UndersizedException {
        if (headBytes.length < ID3V2_HEADER_SIZE)
            throw new UndersizedException(headBytes.length, ID3V2_HEADER_SIZE);
        identifier = new String(headBytes, 0, 3);
        version = (headBytes[3] & 0xff);
        revision = (headBytes[4] & 0xff);
        flags = (headBytes[5] & 0xff);
        // Decoding flags
        unsynchronisation = (headBytes[5] & 0x80) != 0;
        extendedHeader = (headBytes[5] & 0x40) != 0;
        experimentalIndicator = (headBytes[5] & 0x20) != 0;
        footerPresent = (headBytes[5] & 0x10) != 0;
        unknownFlagsExist = (headBytes[5] & 0x0f) != 0;

        byte[] sizeBytes = new byte[] { headBytes[6], headBytes[7],
                headBytes[8], headBytes[9] };
        size = Helper.unsynchsafe(sizeBytes);
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getVersion() {
        return version;
    }

    public int getRevision() {
        return revision;
    }

    public int getFlags() {
        return flags;
    }

    public boolean hasUnsynchronisation() {
        return unsynchronisation;
    }

    public boolean hasExtendedHeader() {
        return extendedHeader;
    }

    public boolean isExperimental() {
        return experimentalIndicator;
    }

    public boolean hasFooter() {
        return footerPresent;
    }

    public boolean hasUnknownFlags() {
        return unknownFlagsExist;
    }

    /** @return size of the ID3V2-Tag without the size of the header */
    public long getSize() {
        return size;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("This ID3V2Tag seems to be:\n").append("Identifier: ")
                .append(identifier).append("\n").append("Version:    ").append(
                        version).append(".").append(revision).append("\n")
                .append("Flags:      ").append(Integer.toString(flags, 2))
                .append("\n").append("unsynch ").append(unsynchronisation)
                .append(", exthead ").append(extendedHeader).append(
                        ", exp'tal ").append(experimentalIndicator).append(
                        ", footer  ").append(footerPresent)
                .append(", unknown ").append(unknownFlagsExist).append("\n")

                .append("Size (without header or footer): ").append(size)
                .append("\n");
        return sb.toString();
    }
}
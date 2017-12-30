package de.ueberdosis.mp3info;

import de.ueberdosis.mp3info.id3v2.ID3V2Reader;
import de.ueberdosis.mp3info.id3v2.ID3V2Tag;
import de.ueberdosis.util.OutputCtr;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * ID 3 and Bitrate reader Milon Krejca <milon@iname.com>, Florian Heer
 * <heer@ueberdosis.de>, (c) 2000-2004
 *
 * @author Milon Krejca <milon@iname.com>, Florian Heer <heer@ueberdosis.de>,
 *         Feng ZHOU <zfff@users.sourceforge.net>
 * @version $Id: ID3Reader.java,v 1.26 2005/02/22 18:04:05 heer Exp $
 */

/*
 * 
 * ToDo: - Change the code that if the user enters a directory, it will be
 * correctly processed. (Rather not, that's up to frontends to do.) - Ensure
 * that repeated use of the suite makes use of caching. I.e. pseudo-static
 * information have to be kept
 */
public class ID3Reader implements Defines {

    /**
     * stores one extended Tag. Relict from the days I only had V1 tags to worry
     * about
     */
    protected ExtendedID3Tag xtag;

    /**
     * stores one possibly encountered V2 tag (null if none encountered).
     */
    protected static ID3V2Tag v2tag = null;

    /**
     * This reads an ID3V1Tag from the given File.
     */
    public static ID3Tag readTag(RandomAccessFile in) throws IOException {
        ID3Tag tag = null;

        if (in.length() > 128) {
            in.seek(in.length() - 128);
            byte[] tagBytes = new byte[128];
            in.read(tagBytes);
            tag = new ID3Tag(tagBytes);
            return tag;
        }
        return null;
    }

    /**
     * reads an extended ID3Tag from the given file. Apart from things like
     * artist and title, also bitrate and mpegID are read. <br>
     * This method just reads the first frame it can find. VBR-files definitely
     * result in wrong playing times, as the bitrate is VARIABLE. <br>
     * To get reliable information on VBR-files checkAllFrames () <b>must </b>
     * be called <u>afterwards </u>!
     */
    public static ExtendedID3Tag readExtendedTag(RandomAccessFile in)
            throws IOException {
        v2tag = null;

        long framePosition = 0;
        // I don't like files that are too small!
        if (in.length() < 128)
            return null;
        ExtendedID3Tag tag1 = new ExtendedID3Tag(readTag(in));
        ExtendedID3Tag tag2 = new ExtendedID3Tag(tag1);

        boolean good = false;
        byte[] frameHeader;

        // Offset for the framepointer, computed during analyzing an ID3V2
        // Tag and always added to the framePosition-pointer.
        long fpOffset = 0;
        long iD3v2Size = 0;

        // Trying to find the first header...
        while (!good && (framePosition < in.length() - 10)) {
            try {
                framePosition = findNextHeader(in, framePosition);

                frameHeader = getFrameHeader(framePosition, in);

                good = parseExtendedInto(frameHeader, tag1, framePosition);

                tag1.setSize(in.length());
                tag1.setPosition(framePosition);

            } catch (ID3V2Exception ex) {
                // So, we encountered a V2 tag.
                OutputCtr.println(6, ex);
                // OutputCtr.println ("Position:"+framePosition
                //                  + " Trying to find information...");
                // Read the header
                ID3V2Reader v2Reader = new ID3V2Reader(in, ex.getPosition());
                v2tag = v2Reader.getV2Tag();

                // Skip a certain amount of bytes according to the size of
                // the ID3v2 tag
                fpOffset = v2tag.getSize();
                iD3v2Size = fpOffset;
                OutputCtr.println(3, v2tag);

                OutputCtr.println(2, "Skipping " + fpOffset + " bytes.");
                framePosition = ex.getPosition() + fpOffset;
            } catch (ID3TagException ex) {
                OutputCtr.println(6, ex);
                framePosition = ex.getPosition() + 128;
            } catch (UndersizedException ue) {
                OutputCtr.println(0, ue);
            }
            framePosition++;
        }

        if (good) {
            tag1.setID3v2Size(iD3v2Size);
            return tag1;
        }
        return tag2;
    }

    /**
     * returns the 10 bytes that could be a frame header or an ID3V2-Tag Header
     *
     * @param pos position in the file
     * @param in  file to search in
     */
    private static byte[] getFrameHeader(long pos, RandomAccessFile in)
            throws IOException {
        byte[] frameHeader = new byte[ID3V2_HEADER_SIZE];
        in.seek(pos);
        in.read(frameHeader);
        return (frameHeader);
    }

    /**
     * tries to compute the framesize
     */
    private static int getFramesize(ExtendedID3Tag tag) {
        int size = 0;
        // Could the frame be legal?
        if (tag.getFrequencyI() > 1) {
            int padding = (tag.getPadding()) ? 1 : 0;
            switch (tag.getLayer()) {
                case 3:
                    size = (12 * tag.getBitrateI() * 1000 / tag.getFrequencyI() + padding) * 4;
                default:
                    size = 144 * tag.getBitrateI() * 1000 / tag.getFrequencyI()
                            + padding;
            }
        }
        return (size);
    }

    /**
     * Checks if the so called frameheader (minimum 4 bytes) starts with a sync
     *
     * @return true if it seems to be a valid header
     */
    private static boolean checkFrameHeader(byte[] frameHeader, long position)
            throws UndersizedException, ID3TagException, ID3V2Exception {
        if (frameHeader.length < MP3_FRAME_HEADER_SIZE)
            throw new UndersizedException(frameHeader.length,
                    MP3_FRAME_HEADER_SIZE);

        // Test if we encountered an ID3 or ID3v2 tag
        if (new String(frameHeader, 0, 3).equals("ID3")) {
            ID3V2Exception ex = new ID3V2Exception("Encountered an ID3v2Tag.");
            ex.setPosition(position);
            throw ex;
        }
        if (new String(frameHeader, 0, 3).equals("TAG")) {
            ID3TagException ex = new ID3TagException("Encountered an ID3Tag.");
            ex.setPosition(position);
            throw ex;
        }

        byte first = frameHeader[0];
        byte second = frameHeader[1];

        // Checking for sync
        return ((first == -1) && ((second >>> 4) >= 0x0e));
    }

    /**
     * Parses a given frame header ( minimum 4 bytes) into a given tag.
     *
     * @return if the given byte-array started with a sync, i.e. it contained a
     *         correct frame-header.
     */
    private static boolean parseExtendedInto(byte[] frameHeader,
                                             ExtendedID3Tag tag, long position) throws ID3V2Exception,
            ID3TagException, UndersizedException {

        if (checkFrameHeader(frameHeader, position)) {
            //Unnecessary, the first byte isn't used
            //byte first = frameHeader[0];
            byte second = frameHeader[1];
            byte third = frameHeader[2];
            byte fourth = frameHeader[3];

            tag.setMpegID((second & 0x30) >>> 4);
            tag.setLayer((second & 0x06) >>> 1);
            tag.setCrc((second & 0x01) == 0); // CRCProtection is on,
            // when bit is NOT set
            tag.setBitrate((third & 0xf0) >>> 4);
            tag.setFrequency((third & 0x0c) >>> 2);
            tag.setPadding((third & 0x02) >>> 1 == 1);
            tag.setChannelMode((fourth & 0xc0) >>> 6);
            tag.setCopyright((fourth & 0x08) != 0);
            tag.setOriginal((fourth & 0x04) != 0);
            tag.setEmphasis((fourth & 0x03));

            // Checks for legal entries
            return checkTag(tag);
        }
        return false;
    }

    /**
     * @return possibly the parsed tag (or null if none)
     */
    public ExtendedID3Tag getExtendedID3Tag() {
        return xtag;
    }

    /**
     * Gives you the last encountered ID3V2Tag. The tag gets nulled everytime a
     * method is called that could encounter a V2 tag (e.g. checkAllFrames,
     * readExtendedTag...) <br>
     * This is primarily for developers that call the methods defined in Version
     * 1.5. The information flow will change a bit soon.
     *
     * @return a tag or null
     */
    public static ID3V2Tag getV2Tag() {
        return v2tag;
    }

    /**
     * Creates an instance of ID3Reader that is bound to a file
     *
     * @param filename the file to examine
     */
    public ID3Reader(String filename) throws IOException {
        RandomAccessFile in = new RandomAccessFile(filename, "r");
        if (in.length() > 128) {
            xtag = readExtendedTag(in);

            if (checkVBR(in))
                checkAllFrames(in);
        }
        in.close();

    }

    /**
     * Checks a file for encoding with variable bitrate. As there still is no
     * support for VBR headers in this package (anyone?) the bitrates at some
     * (more or less random) are analyzed. If they seem to differ, it seems to
     * be VBR (there might have been some data that has been faultily taken as a
     * frameheader, if this method claims the data are VBR you cannot coount on
     * it 100%). The positions at which a possible frameheader is looked for are
     * defined in Defines.java. They are always relative to possibly encountered
     * V2-tags.
     *
     * @return true if the file is VBR. false, if this method only encountered
     *         frames with the same bitrates. This is <u>not </u> a definitive
     *         check, only a high possibility check!
     */
    public boolean checkVBR(RandomAccessFile raf) throws IOException {
        // an offset produced by diverse things...
        long fpOffset = xtag.getPosition();

        long currentPosition = 0;
        byte[] frameHeader;
        int bitrate = 0;
        ExtendedID3Tag tag;
        boolean retval = false;

        OutputCtr.println(2, "Entering checkVBR. Don't worry about all those"
                + " \'gibberish\' messages!\n");

        for(int i = 0; i < testPositions.length
                && fpOffset + testPositions[i] < raf.length(); i++) {
            try {
                currentPosition = findNextHeader(raf, fpOffset
                        + testPositions[i]);

                frameHeader = getFrameHeader(currentPosition, raf);
                tag = new ExtendedID3Tag();
                parseExtendedInto(frameHeader, tag, currentPosition);
                if (tag.getBitrateI() > 0 && tag.getFrequencyI() > 0) {
                    if (bitrate <= 0)
                        bitrate = tag.getBitrateI();
                    // bitrates of 0 or smaller denote a defective frame,
                    // nothing else.
                    if (checkTag(tag) && bitrate != tag.getBitrateI()) {
                        OutputCtr.println(2, "Got different bitrates! at "
                                + currentPosition + "|"
                                + Long.toHexString(currentPosition) + " ("
                                + bitrate + ", " + tag.getBitrateI() + ")");
                        OutputCtr.println(2, "The Tag at this point is:");
                        OutputCtr.println(2, tag);
                        retval = true;
                        break;
                    }
                }
            } catch (ID3V2Exception ex) {
                OutputCtr.println(6, ex);
                // OutputCtr.println ("Position: "+newPosition+
                //                     " Trying to analyze Header..:");
                //		    frameHeader = getFrameHeader (ex.getPosition (), raf);
                ID3V2Reader v2reader = new ID3V2Reader(raf, ex.getPosition());
                v2tag = v2reader.getV2Tag();
                OutputCtr.println(3, v2tag);
                fpOffset = v2tag.getSize();
                OutputCtr.println(2, "skipping " + fpOffset + " bytest.");
                fpOffset += xtag.getPosition();
                // We didn't check a frame at testPositions [i], but
                // we want to, so, reduce i!
                i--;
            } catch (ID3TagException ex) {
                OutputCtr.println(2, ex);
            } catch (UndersizedException ue) {
                OutputCtr.println(0, ue);
            }
        }
        OutputCtr.println(2, "Leaving checkVBR.");
        return retval;
    }

    /**
     * Reads all frames sequentially. It counts the frames and computes the
     * playing time of a file.
     * <p/>
     * <b>Caution: </b> this method is not supposed to be public. It is so only
     * at this time of development and may be altered in the next release!
     * <p/>
     * Since 1.6.0d2 this has to be called only if
     * <ul>
     * <li>a file is encoded in VBR and you want the real time (not one that
     * might be entered in an ID3v2 tag, thus having the real time even if the
     * file was not completely transferred.
     * <li>a file is encoded with a fixed bitrate but may contain an ID3v2 tag
     * </ul>
     * If you have a fixed bitrate file (@see checkVBR ()) and you are sure the
     * file contains (apart from an ID3v1 tag) only musical data (and no V2
     * tag), you do not need to call this time consuming method.
     *
     * @param in the file to be examined.
     */
    public void checkAllFrames(RandomAccessFile in) throws IOException {
        int frameCount = 0;
        long playTime = 0; // In Milliseconds
        long position = xtag.getPosition();
        long newPosition = position;
        int size;
        ExtendedID3Tag tag = xtag;
        byte[] frameHeader = null;
        int bitrate = 0;
        // Have to take the headersize off
        long end = in.length() - 11;

        boolean stop = false;
        boolean badFrame = false;

        OutputCtr.println(2, "Entering checkAllFrames.");

        while (!stop && !badFrame) {
            // An exception could have reset newPosition
            if (newPosition >= end) {
                stop = true;
            } else {
                size = getFramesize(tag);
                if (size > 1) {
                    frameCount++;
                    bitrate = tag.getBitrateI();
                    if (bitrate != 0) {
                        playTime += (size * 8) / bitrate;
                        xtag.updateBitrates(tag);
                    } else
                        OutputCtr.println(2, "bitrate was 0 at position "
                                + (position) + " !");

                    newPosition = position + size;
                } else
                    newPosition++; // Encountered a defective frame
            }
            // End denotes the number of bytes contained in this file.
            // Looks crappy, these two if's, don't they?
            if (newPosition >= end)
                stop = true;
            else {
                try {
                    newPosition = findNextHeader(in, newPosition);

                    frameHeader = getFrameHeader(newPosition, in);
                    tag = new ExtendedID3Tag(tag);
                    parseExtendedInto(frameHeader, tag, newPosition);

                } catch (ID3V2Exception ex) {
                    OutputCtr.println(6, ex);
                    // OutputCtr.println ("Position: "+newPosition+
                    //                     " Trying to analyze Header..:");
                    ID3V2Reader v2reader = new ID3V2Reader(in, ex
                            .getPosition());
                    // frameHeader = getFrameHeader (ex.getPosition (), in);

                    ID3V2Tag v2tag = v2reader.getV2Tag();
                    OutputCtr.println(2, v2tag);
                    long fpOffset = v2tag.getSize();
                    OutputCtr.println(2, "skipping " + fpOffset + " bytes at "
                            + ex.getPosition() + ".");
                    newPosition = ex.getPosition() + fpOffset;
                    OutputCtr.println(4, "newPosition = " + newPosition);
                } catch (ID3TagException ex) {
                    OutputCtr.println(2, ex);
                    OutputCtr.println(3, "Still in while...");
                    // I think it's best just to stop..
                    // newPosition = ex.getPosition () + 128;
                    stop = true;
                } catch (UndersizedException ue) {
                    OutputCtr.println(0, ue);
                } finally {
                    position = newPosition;
                }

            }
        }
        if (stop) {
            // We reached the stop, so the playing time and frame count are
            // known
            xtag.setKRuntime(playTime);
            xtag.setFramecount(frameCount);
        } else {
            OutputCtr.println(3, "Did not find a header at " + newPosition);
            OutputCtr.println(3, "Size is " + end + ".\nDifference therefore:"
                    + (end - newPosition));
            // If we end up less than 129 bytes before the end (remember,
            // an ID3Tag consists of the last 128 bytes), there must
            // have been an error
            if (newPosition <= end - 129) {
                // FIXME! There is a reason why I did this, but I can't remember
                //		newPosition = findNextHeader ();
                //xtag.setRuntime (0);
                //xtag.setFramecount (0);
                //if (frameHeader != null) {
                //    for (int i=0; i<frameHeader.length; i++) {
                //        OutputCtr.print (""+frameHeader[i]);
                //    }
                //}
            } else {
                xtag.setKRuntime(playTime);
                xtag.setFramecount(frameCount);
            }

            // For a better debugging: add a newline.
            OutputCtr.println(3, "");
        }
        OutputCtr.println(1, "FrameCount: " + frameCount);
        OutputCtr.println(1, "PlayTime: " + playTime);

    }

    /**
     * Reads each byte of the file until it encounters usable information.
     * Either a frame header is found, an ID3 tag or an ID3v2 tag
     *
     * @param raf           the file to scan
     * @param startPosition the position in the file where to start
     */
    private static long findNextHeader(RandomAccessFile raf, long startPosition)
            throws IOException, ID3V2Exception, ID3TagException,
            UndersizedException {
        // OutputCtr.println ("findNextHeader ( .. , "+startPosition+");");
        long currentPosition = startPosition;

        byte[] frameHeader = new byte[ID3V2_HEADER_SIZE];
        boolean good = false;
        int BUFFER_SIZE = 64 * 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        long bufStart = -BUFFER_SIZE;
        long len = raf.length();

        // we keep looking as long as we still have 10 bytes left
        // (remember, sizeof (id3v2 frameheader) = 10
        while (currentPosition < len - 10 && !good) {
            // buffer the file to speed up reading
            if (bufStart + BUFFER_SIZE <= currentPosition + ID3V2_HEADER_SIZE)
                try {
                    bufStart = currentPosition;
                    raf.seek(bufStart);
                    raf.read(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            System.arraycopy(buf, (int) (currentPosition - bufStart),
                    frameHeader, 0, ID3V2_HEADER_SIZE);
            //	    frameHeader = getFrameHeader (currentPosition, raf);
            good = checkFrameHeader(frameHeader, currentPosition);
            if (!good)
                currentPosition++;
            // OutputCtr.println (currentPosition);
        }
        if (currentPosition > startPosition)
            OutputCtr.println(3, "Skipped "
                    + (currentPosition - startPosition)
                    + " bytes of gibberish (" + startPosition + " - "
                    + currentPosition + " | "
                    + Long.toHexString(startPosition) + " - "
                    + Long.toHexString(currentPosition) + ")");
        return currentPosition;

    }

    /**
     * The user interface :-)
     */
    public static void main(String args[]) {
        OutputCtr.println(0, "Mp3 Id (" + VERSION + ") and bitrate reader.\n"
                + "Milon Krejca <milon@iname.com>, "
                + "Florian Heer <heer@ueberdosis.de>(c) 2001-2002");
        if (args.length != 1)
            OutputCtr.println(0, "Usage: ID3Reader <filename>");
        else {

            // Sets the level of output!
            OutputCtr.setLevel(0);
            try {
                ID3Reader x = new ID3Reader(args[0]);
                if (ID3Reader.getV2Tag() != null) {
                    OutputCtr.println(0, "\nV2 tag:");
                    OutputCtr.println(0,
                            new de.ueberdosis.mp3info.facades.Wamp(ID3Reader
                                    .getV2Tag()));
                }
                OutputCtr.println(0, x.getExtendedID3Tag());
            } catch (IOException e) {
                OutputCtr.println(0, e);
            }
        }
    }

    /**
     * Checks a tag (the bit-mapped information) for reliability
     * If any strange entries are encountered (like emphasis="reserved") the
     * tag is rejected.
     *
     * @return true if the tag seems to be valid
     */
    private static boolean checkTag(ExtendedID3Tag tag) {
        // To make the errors more understandable, we check each entry
        // separately.

        // We could also check, if the combination of MpegID, Layer
        // & Bitrate are legal...
        if (tag.getMpegID() < 0 || tag.getMpegID() > 3
                || tag.getMpegID() == 1) {
            OutputCtr.println(2, "MpegID was the illegal " + tag.getMpegID()
                    + "!");
            return false;
        }
        if (tag.getLayer() < 1 || tag.getLayer() > 3) {
            OutputCtr.println(2, "Layer was the illegal " + tag.getLayer()
                    + "!");
            return false;
        }
        if (tag.getBitrateI() < 8 || tag.getBitrateI() > 448) {
            OutputCtr.println(2, "Bitrate was the illegal "
                    + tag.getBitrateI() + "!");
            return false;
        }
        if (tag.getFrequencyI() < 8000 || tag.getFrequencyI() > 48000) {
            OutputCtr.println(2, "Frequency was the illegal "
                    + tag.getFrequencyI() + "!");
            return false;
        }
        if (tag.getChannelMode() < 0 || tag.getChannelMode() > 3) {
            OutputCtr.println(2, "Channelmode was the illegal "
                    + tag.getChannelMode() + "!");
            return false;
        }
        if (tag.getEmphasis() < 0 || tag.getEmphasis() > 3) {
            OutputCtr.println(2, "Emphasis was the illegal "
                    + tag.getEmphasis() + "!");
            return false;
        }

        // Hmm.. seems to be a valid tag...
        return true;

    }

}


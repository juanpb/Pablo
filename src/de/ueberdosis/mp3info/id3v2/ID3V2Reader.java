package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.mp3info.UndersizedException;
import de.ueberdosis.util.OutputCtr;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains all functionality to read and decode an ID3v2-Tag.
 * 
 * @author Florian Heer
 * @version $Id: ID3V2Reader.java,v 1.10 2005/02/24 21:24:25 heer Exp $
 */

public class ID3V2Reader implements Defines {
    private long headerPosition;

    private ID3V2Tag v2tag;

    /**
     * Constructs a reader. A reader is always connected to a certain file in
     * which an ID3v2-tag starts at the given position.
     * 
     * @param raf
     *            the file this reader is connected to.
     * @param position
     *            index where an ID3V2Header starts.
     */
    public ID3V2Reader(RandomAccessFile raf, long position) {
        headerPosition = position;
        v2tag = new ID3V2Tag ();
        try {
            raf.seek (position);
            byte[] headerBytes = new byte[ID3V2_HEADER_SIZE];
            raf.read (headerBytes);
            v2tag.setHeader (analyzeID3V2Header (headerBytes));

            // Setting the position to the start of the first frame
            position += ID3V2_HEADER_SIZE;

            ID3V2Frame frm;
            while (position < headerPosition + ID3V2_HEADER_SIZE
                    + v2tag.getHeader ().getSize ()) {
                raf.seek (position);
                headerBytes = new byte[ID3V2_FRAME_HEADER_SIZE];
                raf.read (headerBytes);
                if (headerBytes[0] == 0)
                    break;
                frm = analyzeID3V2Frame (headerBytes, getHeader ()
                        .getVersion (), getHeader ().getRevision ());
                frm.setPosition (position);
                if (frm.isValid ()) {
                    DataSource ds = new FileFrameDataSource (frm, raf);
                    v2tag.addFrame (FrameFactory.getFrame (frm, ds));
                }
                // ALWAYS add at least the Frame header size!
                // Is there a way to determine if we will only
                // encounter padding from now on?
                // Yes! :-)
                // if the next data are $00, the padding has started.
                if (frm.getSize () < 0) {
                    OutputCtr.println (1, "** Possible error in Frame:");
                    OutputCtr.print (1, frm);
                    OutputCtr.println (1, "Frame size = " + frm.getSize ());
                    break;
                }
                position += ID3V2_FRAME_HEADER_SIZE + frm.getSize ();
            }
        }
        // FIXME!
        catch (IOException ex) {
            ex.printStackTrace ();
        } catch (UndersizedException ex) {
            ex.printStackTrace ();
        } catch (NotAnID3V2FrameException ex) {
            ex.printStackTrace ();
        }
    }

    public Header getHeader() {
        return v2tag.getHeader ();
    }

    public ID3V2Tag getV2Tag() {
        return v2tag;
    }

    /**
     * Tries to get basic information on an ID3V2Tag
     * 
     * @param header
     *            10 bytes that (probably) denote a ID3V2 header
     * @return the V2Tag-header
     */

    // No checking done if the header really is a header.
    public static Header analyzeID3V2Header(byte[] header)
            throws UndersizedException {
        return new Header (header);
    }

    /**
     * @throws UndersizedException
     * @throws NotAnID3V2FrameException
     * @deprecated
     */
    public static ID3V2Frame analyzeID3V2Frame(byte[] frame)
            throws UndersizedException, NotAnID3V2FrameException {
        return new ID3V2FrameDefaultImpl (frame);
    }

    public static ID3V2Frame analyzeID3V2Frame(byte[] frame, int id3Version,
            int id3SubVersion) throws UndersizedException,
            NotAnID3V2FrameException {
        return new ID3V2FrameDefaultImpl (frame, id3Version, id3SubVersion);
    }

}
package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Configuration;
import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.util.OutputCtr;

import java.util.Enumeration;
import java.util.Vector;

//import java.util.Iterator;

/**
 * Contains the whole V2Tag.
 * As yet, only 1 header as well as all frames that are contained in this
 * one tag are included.
 *
 * @author Florian Heer
 * @version $Id: ID3V2Tag.java,v 1.11 2003/10/24 12:46:30 heer Exp $
 */
public class ID3V2Tag implements Defines {
    private long originalSize = 0;
    private Header header = null;
    private Vector frames = new Vector();

    public void setHeader(Header head) {
        header = head;
        originalSize = header.getSize() + ID3V2_HEADER_SIZE
                + (header.hasFooter() ? ID3V2_FOOTER_SIZE : 0);
    }

    public ID3V2Tag() {
        header = new Header();
    }

    /**
     * Deep copy constructor
     *
     * @param oldTag Tag to be copied
     */
    public ID3V2Tag(ID3V2Tag oldTag) {
        header = new Header(oldTag.getHeader());
        //Iterator oldFrames = oldTag.getFrames ().iterator ();
        Enumeration oldFrames = oldTag.getFrames().elements();
        ID3V2Frame frm;
        //while (oldFrames.hasNext ()) {
        //    frm = (ID3V2Frame)oldFrames.next ();
        while (oldFrames.hasMoreElements()) {
            frm = (ID3V2Frame) oldFrames.nextElement();
            frm = FrameFactory.getFrame(frm);
            //if (frm != null) frames.add (frm);
            if (frm != null) frames.addElement(frm);
        }
    }

    public void addFrame(ID3V2Frame frm) {
        frames.addElement(frm);
    }

    public void removeFrame(ID3V2Frame frm) {
        frames.removeElement(frm);
    }

    // Must be changed. i don't want the header as such, but its contained
    // information
    public Header getHeader() {
        return header;
    }

    /**
     * Delivers all contained Frames.
     *
     * @return all Frames that are included in this Tag.
     */
    public Vector getFrames() {
        return frames;
    }

    /**
     * Information about the size of the Tag in the file.
     * This is used to decide if the file has to be
     * rewritten (as the new data don't fit in the old tag)
     *
     * @return the size the tag had when it was read from file
     */
    public long getOriginalSize() {
        return originalSize;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(header).append("\nContained frames:\n\n");
        //Iterator iter = frames.iterator ();
        //while (iter.hasNext ())
        //    sb.append(iter.next ()).append("\n\n");
        Enumeration iter = frames.elements();
        while (iter.hasMoreElements())
            sb.append(iter.nextElement()).append("\n\n");
        return sb.toString();
    }

    /**
     * The size of the whole tag (effectively used).
     *
     * @return the size of the tag complete with header and footer
     */
    public long getEffectiveSize() {
        // Start by finding the sizes of all contained frames
        //Iterator iter = frames.iterator ();
        Enumeration iter = frames.elements();
        long sizes = 0;
        //while (iter.hasNext ()) {
        //    ID3V2Frame frm = (ID3V2Frame)iter.next ();
        while (iter.hasMoreElements()) {
            ID3V2Frame frm = (ID3V2Frame) iter.nextElement();
            sizes += frm.getSize();
            // Have to add the size of a header as getSize only delivers
            // the size of the contained data
            sizes += ID3V2_FRAME_HEADER_SIZE;
        }
        return sizes + ID3V2_HEADER_SIZE
                + (header.hasFooter() ? ID3V2_FOOTER_SIZE : 0);
    }

    public long getSize() {
        long size = header.getSize();
        if (size < 0)
            return getEffectiveSize();
        return size;
    }

    /**
     * A byte-array representation of the tag and all its contained frames
     *
     * @return byte array containing the tag.
     */

    // FIXME! Only the most basic functions are implemented.
    // No extended headers, no flags, no footer...
    public byte[] toByteArray() {
        byte[] tagArray;

        // Start by finding the effective Size
        long sizes = getEffectiveSize();

        // If our size does not exceed the size of the tag in the file
        // we do not need to resize it in the file.
        // Something that isn't even implemented yet... :-)
        if (sizes < header.getSize()) {
            tagArray = new byte[(int) header.getSize() + ID3V2_HEADER_SIZE];
        }
        // Now it is to decide how much we increase the size
        // Some recommendation of id3.org says:
        //  If you are editing an existing tag and run out of room (meaning
        //  you must rewrite the entire file) then double the amount of space
        //  reserved for the tag. For example, a file has a 4K tag filled with
        //  3K of data. User wants to add 2K of data, so you rewrite the file
        //  reserving 8K at the beginning; then save the 5K of data and
        //  leaving 3K of padding.
        //
        //  Obviously there should be an upper bound on doubling; a reasonable
        //  number would be 16K or 32K. For example, a file has a 25K tag but
        //  only 4K of padding left and the user wants to add 7K of new data.
        //  Rewrite the file and reserve 41K: save 28K of tag data and leave
        //  13K of padding.
        else {
            if (header.getSize() < 8192)
                tagArray = new byte[(int) getEffectiveSize() * 2];
            else
                tagArray = new byte[(int) getEffectiveSize() + 16384];
        }
        // I rely on Java initialising new arrays with 0's...

        // Building the data, starting with the header
        tagArray[0] = (byte) 'I';
        tagArray[1] = (byte) 'D';
        tagArray[2] = (byte) '3';
        //Version.. FIXME! is hardcoded to 3.0!
        tagArray[3] = 0x03;
        tagArray[4] = 0x00;
        // Flags.. FIXME! it's all set to the most basic.
        tagArray[5] = 0x00;
        // Size
        byte[] sizeBytes = Helper.synchsafe
                (tagArray.length - ID3V2_FRAME_HEADER_SIZE, 4);
        for(int i = 0; i < 4; i++)
            tagArray[6 + i] = sizeBytes[i];

        // The data. Starting at index 10
        int currentIndex = 10;
        //Iterator iter = frames.iterator ();
        //while (iter.hasNext ()) {
        //    ID3V2Frame frm = (ID3V2Frame)iter.next();
        Enumeration iter = frames.elements();
        while (iter.hasMoreElements()) {
            ID3V2Frame frm = (ID3V2Frame) iter.nextElement();

            // Only non-empty Frames get written if the Configuration doesn't say otherwise
            if (Configuration.keepAllFrames || frm.containsData()) {
                byte[] frameData = frm.toByteArray();
                OutputCtr.println(4, "Frame " + frm.getFrameID() + " size: " + frameData.length);
                System.arraycopy(frameData, 0, tagArray, currentIndex, frameData.length);
                currentIndex += frameData.length;
            }
        }
        return tagArray;
    }

}

package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.mp3info.UndersizedException;
import de.ueberdosis.mp3info.gui.Id3JPanel;
import de.ueberdosis.mp3info.gui.Id3Panel;
import de.ueberdosis.util.OutputCtr;

/**
 * Contains information about one ID3v2 frame.
 *
 * @version $Id: ID3V2Frame.java,v 1.9 2005/02/24 21:24:44 heer Exp $
 */
// Additional capabilities needed:
//    Changes must be tracked, maybe even with undo, but first only with a flag
//    getSize () must return the size of the contained data
//               (if neccessary, check if changes have been made)
// Should have the possibility to return a displaying Component and
//    an editing Component, switchable via canDisplay () & canEdit ()
public abstract class ID3V2Frame implements Defines {
    /**
     * ID of the Frame (like "UFID")
     */
    private String frameID = "";

    /**
     * size of the data contained in the frame
     */
    private long size = 0;

    /**
     * position of the frame-header in the current file
     */
    private long position = 0;

    // properties common to all frames
    private boolean tagAlterPreservation = false;

    private boolean fileAlterPreservation = false;

    private boolean readOnly = false;

    private boolean compression = false;

    private boolean encryption = false;

    private boolean groupingIdentity = false;

    private boolean unSynchronisation = false;

    private boolean dataLengthIndicator = false;

    /**
     * The information if a Frame has been altered. To be changed by subclasses
     */
    protected boolean dataChanged;

    /**
     * The information since when a Frame is legal The value denotes the minor
     * version of id3v2. A '3' means the Frame in question is legal since
     * version 2.3. To be changed by subclasses.
     */
    protected int legalSince = -1;

    /**
     * The information since when a Frame is illegal To be changed by
     * subclasses.
     */
    protected int deprecatedSince = -1;

    public ID3V2Frame(String id) {
        frameID = new String(id);
    }

    /**
     * Copy constructor
     */
    public ID3V2Frame(ID3V2Frame frm) {
        frameID = frm.frameID;
        size = frm.size;
        position = frm.position;
        tagAlterPreservation = frm.tagAlterPreservation;
        fileAlterPreservation = frm.fileAlterPreservation;
        readOnly = frm.readOnly;
        compression = frm.compression;
        encryption = frm.encryption;
        groupingIdentity = frm.groupingIdentity;
    }

    /**
     * decoding constructor. Is now deprecated as it only supported ID3V2.4.0.
     *
     * @param frameBytes ID3V2_FRAME_HEADER_SIZE bytes that contain vital information
     *                   about the Frame.
     * @deprecated
     */
    public ID3V2Frame(byte[] frameBytes) throws UndersizedException,
            NotAnID3V2FrameException {
        this(frameBytes, 4, 0);
    }

    /**
     * decoding constructor.
     *
     * @param frameBytes ID3V2_FRAME_HEADER_SIZE bytes that contain vital information
     *                   about the Frame.
     */
    public ID3V2Frame(byte[] frameBytes, int id3Version, int id3SubVersion)
            throws UndersizedException, NotAnID3V2FrameException {

        if (frameBytes.length < ID3V2_FRAME_HEADER_SIZE)
            throw new UndersizedException("Got only " + frameBytes.length
                    + " bytes for a frame header!", frameBytes.length,
                    ID3V2_FRAME_HEADER_SIZE);
        if (frameBytes[0] == 0)
            throw new NotAnID3V2FrameException("Padding encountered.");

        frameID = new String(frameBytes, 0, 4);
        byte[] sizeBytes = new byte[]{frameBytes[4], frameBytes[5],
                frameBytes[6], frameBytes[7]};
        OutputCtr.println(5, "SizeBytes: "
                + Helper.byteToHexString(sizeBytes[0]) + " "
                + Helper.byteToHexString(sizeBytes[1]) + " "
                + Helper.byteToHexString(sizeBytes[2]) + " "
                + Helper.byteToHexString(sizeBytes[3]));

        // If the version of the tag is 4 or bigger, the size-information is
        // stored synchsafe...
        if (id3Version >= 4)
            size = Helper.unsynchsafe(sizeBytes);
        else
            size = (sizeBytes[0] & 0xff) * 16777216 + (sizeBytes[1] & 0xff)
                    * 65536 + (sizeBytes[2] & 0xff) * 256
                    + (sizeBytes[3] & 0xff);

        OutputCtr.println(5, "And the size: " + size);

        OutputCtr.println(4, "Frame is " + size + " bytes long.");

        tagAlterPreservation = (frameBytes[8] & 0x40) != 0;
        fileAlterPreservation = (frameBytes[8] & 0x20) != 0;
        readOnly = (frameBytes[8] & 0x10) != 0;
        groupingIdentity = (frameBytes[9] & 0x40) != 0;
        compression = (frameBytes[9] & 0x08) != 0;
        encryption = (frameBytes[9] & 0x04) != 0;
        unSynchronisation = (frameBytes[9] & 0x02) != 0;
        dataLengthIndicator = (frameBytes[9] & 0x01) != 0;

        OutputCtr.println(6, "dataLengthIndicator: " + dataLengthIndicator);

    }

    /**
     * Information about what Frame this is.
     *
     * @return Four characters defining the Frame. E.g. "APIC" for the picture
     *         frame.
     */
    public String getFrameID() {
        return frameID;
    }

    /**
     * Information about the size of the contained data
     *
     * @return the size of the data contained in the frame. As yet (version
     *         1.6.0d5) no unsynchronisation is respected.
     */
    public long getSize() {
        if (dataChanged) {
            byte[] data = getData();
            if (data != null)
                return data.length;
            return 0;
        }
        return size;
    }

    public boolean getTagAlterPreservation() {
        return tagAlterPreservation;
    }

    public boolean getFileAlterPreservation() {
        return fileAlterPreservation;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isCompressed() {
        return compression;
    }

    public boolean isEncrypted() {
        return encryption;
    }

    public boolean hasGroupingIdentity() {
        return groupingIdentity;
    }

    public boolean getUnsynchronisation() {
        return unSynchronisation;
    }

    public boolean getDataLengthIndicator() {
        return dataLengthIndicator;
    }

    /**
     * Tells if the contained data are altered compared to those in the file.
     *
     * @return true if the frames data has been altered sinc reading it from
     *         file.
     */
    public boolean isAltered() {
        return dataChanged;
    }

    /**
     * Creates an informational String
     *
     * @return header data in human readable form.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ID3v2 Frame").append("\nID: ").append(frameID).append(
                "\nSize: ").append(size).append("\nTagAlterPreservation: ")
                .append(tagAlterPreservation).append(
                "\nFileAlterPreservation: ").append(
                fileAlterPreservation).append("\nReadOnly: ").append(
                readOnly).append("\nCompression: ").append(
                compression).append("\nEncryption: ").append(
                encryption).append("\nGroupingIdentity: ").append(
                groupingIdentity).append("\n");
        return sb.toString();
    }

    /**
     * Some programs like to fill unused spaces with lots of 0's, so we have to
     * test the frame for its validity. The easiest way is to check whether the
     * ID COULD be valid.
     *
     * @return true if the frame seems to be valid
     */
    public boolean isValid() {
        byte[] sbytes = getFrameID().getBytes();
        int retval = 0;
        for(int i = 0; i < sbytes.length; i++)
            retval += (sbytes[i] & 0xff);
        return (retval > 128); // If only 1 character is a non-Space...
    }

    /**
     * Denotes if actual data are in this frame. Some Frames have a minimum size
     * that is greater than the header although they don't contain any data. So
     * I can't generally determine if a Frame is empty just by asking for its
     * size. <br>
     * To be overwritten by subclasses.
     */
    public boolean containsData() {
        OutputCtr.println(5, getFrameID() + " Frame.containsData: "
                + (getSize() > 0) + "(Size: " + getSize() + ")");
        return (getSize() > 0);
    }

    /**
     * Sets the position of this frame in the file it is read from
     *
     * @param pos position in the file
     */
    void setPosition(long pos) {
        position = pos;
    }

    /**
     * Delivers the position of this frame within the file it is read from
     *
     * @return the position in the file
     */
    long getPosition() {
        return position;
    }

    /**
     * Generic method. All subclasses are to have a more sensible one!
     */
    public boolean equals(Object o) {
        if (!(o instanceof ID3V2Frame))
            return false;
        ID3V2Frame frm = (ID3V2Frame) o;
        return frameID.equals(frm.frameID) && size == frm.size
                // I don't think that should a a criterium...
                // && position = frm.position;
                && tagAlterPreservation == frm.tagAlterPreservation
                && fileAlterPreservation == frm.fileAlterPreservation
                && readOnly == frm.readOnly && compression == frm.compression
                && encryption == frm.encryption
                && groupingIdentity == frm.groupingIdentity;
    }

    /**
     * creates a byte array containing the whole Frame
     */

    // FIXME! All flags are currently just copied, none is checked for
    // sanity in the case of changed data.
    public byte[] toByteArray() {
        byte[] data = getData();

        // Building the header
        byte[] header = new byte[ID3V2_FRAME_HEADER_SIZE];
        for(int i = 0; i < 4; i++)
            header[i] = (byte) frameID.charAt(i);

        // Determining the size
        if (data != null) {
            byte[] sizeBytes = Helper.synchsafe(data.length, 4);
            for(int i = 0; i < 4; i++)
                header[i + 4] = sizeBytes[i];
        } else
            for(int i = 0; i < 4; i++)
                header[i + 4] = 0;

        header[8] = 0;
        header[9] = 0;
        if (tagAlterPreservation)
            header[8] += 0x40;
        if (fileAlterPreservation)
            header[8] += 0x20;
        if (readOnly)
            header[8] += 0x10;
        if (groupingIdentity)
            header[9] += 0x40;
        if (compression)
            header[9] += 0x08;
        if (encryption)
            header[9] += 0x04;
        if (unSynchronisation)
            header[9] += 0x02;
        if (dataLengthIndicator)
            header[9] += 0x01;

        byte[] wholeFrame = new byte[ID3V2_FRAME_HEADER_SIZE
                + (data == null ? 0 : data.length)];
        for(int i = 0; i < ID3V2_FRAME_HEADER_SIZE; i++)
            wholeFrame[i] = header[i];
        if (data != null) {
            for(int i = 0; i < data.length; i++) {
                wholeFrame[i + ID3V2_FRAME_HEADER_SIZE] = data[i];
            }
        }
        return wholeFrame;
    }

    /**
     * creates an byte array containing the data in the Frame. Dummy
     * implementation, to be overwritten by the actual Frames
     */
    public abstract byte[] getData();

    /**
     * Informs if the frame can create a component to display its contents To be
     * overwritten by subclasses
     */
    public boolean canDisplay() {
        return false;
    }

    /**
     * Informs if the frame can create a component to edit its contents To be
     * overwritten by subclasses
     */
    public boolean canEdit() {
        return false;
    }

    /**
     * Creates a JPanel to display or edit the contents of the frame. To be
     * overwritten by subclasses. The results of canDisplay / canEdit are only
     * binding for this method (ie. for JPanels). AWT-Panels may or may not be
     * implemented.
     *
     * @param edit       switches between display- (false) and edit-mode (true)
     * @param selfupdate switches if the created component takes care of updating its
     *                   data or not. Generally this means that the component carries
     *                   its own update-button. <br>
     *                   Of course, selfupdate is useless without edit set to true.
     * @return a JPanel that can be used to display or edit the contents
     */
    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        return null;
    }

    /**
     * Creates a (AWT-)Panel to display or edit the contents of the frame. To be
     * overwritten by subclasses. The results of canDisplay / canEdit are only
     * binding for the other method (ie. for JComponents). AWT-Panel may or may
     * not be implemented. <br>
     * This is only intended for future extensibility, maybe someone will write
     * extension/replacements for my Frame-implementations...
     *
     * @param edit       switches between display- (false) and edit-mode (true)
     * @param selfupdate switches if the created component takes care of updating its
     *                   data or not. Generally this means that the Component carries
     *                   its own update-button. <br>
     *                   Of course, selfupdate is useless without edit set to true.
     * @return a Panel that can be used to display or edit the contents
     */
    public Id3Panel createPanel(boolean edit, boolean selfupdate) {
        return null;
    }

    /**
     * Informs since which version of ID3V2 this Frame is legal
     * Only minor versions are supported (eg. 4 stands for ID3 v 2.4.0)
     *
     * @return a number between 0 and (currently) 4. -1 means the actual
     *         frame didn't overwrite the method, or there is no information
     *         about that.
     */
    public int legalSince() {
        return -1;
    }

    /**
     * Informs since which version of ID3V2 this Frame has become illegal
     * Only minor versions are supported.
     *
     * @return a number between -1 and (currently) 4 denoting the first
     *         version that does NOT support this frame anymore. -1 (in combination
     *         with a sane return of legalSince ()) means this frame is not
     *         deprecated.
     */
    public int deprecatedSince() {
        return -1;
    }
}
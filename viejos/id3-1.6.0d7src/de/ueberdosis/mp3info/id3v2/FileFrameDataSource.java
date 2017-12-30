package de.ueberdosis.mp3info.id3v2;

//import de.ueberdosis.mp3info.Defines;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.util.OutputCtr;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 * A DataSource for frames that are read from a file. Manages the data that is
 * contained in one frame only.
 *
 * @author Florian Heer
 * @version $Id: FileFrameDataSource.java,v 1.9 2005/02/22 18:06:53 heer Exp $
 */

public class FileFrameDataSource implements DataSource {
    /**
     * The source of the data
     */
    RandomAccessFile raf = null;

    /**
     * start position within the file
     */
    long startPosition = 0;

    /**
     * points to the next available byte
     */
    long internalPosition = 0;

    /**
     * size of the frame to be read
     */
    long size = 0;

    /**
     * Creates a DataSource that reads from a file.
     *
     * @param frm contains the basic information
     * @param in  the file to read from
     */
    public FileFrameDataSource(ID3V2Frame frm, RandomAccessFile in) {
        startPosition = frm.getPosition() + ID3V2_FRAME_HEADER_SIZE;
        size = frm.getSize();
        raf = in;
    }

    /**
     * delivers the next available byte
     *
     * @return next byte
     */
    public byte getByte() throws SeekPastEndException {
        return getBytes(1)[0];
    }

    /**
     * delivers the next available bytes
     *
     * @param number number of bytes to deliver
     * @return demanded bytes
     */
    public byte[] getBytes(long number) throws SeekPastEndException {
        byte[] bs = {0};
        if (internalPosition + number <= size) {
            bs = readBytes(number);
            internalPosition += number;
        } else
            throw new SeekPastEndException((internalPosition + number) + " >= "
                    + size);
        return bs;
    }

    /**
     * delivers the next bytes until the stop byte or the end is encountered.
     * The stop byte is not included in the return value.
     *
     * @return demanded bytes
     */
    public byte[] getBytesTo(byte stopByte) {
        OutputCtr.println(7, "StopByte: " + stopByte + " internal counter: "
                + internalPosition);
        byte[] bAr = Helper.reallocateByteBuffer(256, (byte[]) null);
        byte[] retval = null;
        int bytesRead = 0;
        boolean stop = false;
        byte b = 0;
        try {
            if (bAr != null) {
                OutputCtr.println(6, "initial bAr got..");
                while (hasMoreBytes() && !stop) {
                    b = getByte();
                    if (b != stopByte) {
                        bytesRead++;
                        if (bytesRead >= bAr.length) {
                            bAr = Helper.reallocateByteBuffer(256, bAr);
                            // Early return!
                            if (bAr == null) {
                                OutputCtr.print(5,
                                        " Reallocation of buffer failed");
                                return null;
                            }
                        }
                        bAr[bytesRead - 1] = b;
                    } else
                        stop = true;
                }
            } else
                return null;
            if (bytesRead > 0) {
                retval = new byte[bytesRead];
                System.arraycopy(bAr, 0, retval, 0, bytesRead);
                OutputCtr.println(7, "Resultarray is " + retval.length
                        + " bytes.");
            } else {
                OutputCtr.println(6, "No byte read.");
                retval = null;
            }
            // Correction of the internal counter. getByte always increases the
            // pointer, no matter if we used the read byte.
            internalPosition--;
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, ex);
        }
        OutputCtr.println(7, "Everything went fine...");
        OutputCtr.println(7, "internal counter: " + internalPosition);
        return retval;
    }

    /**
     * Sets the internal pointer to the start of the data
     */
    public void reset() {
        internalPosition = 0;
    }

    /**
     * informs whether there are more bytes than can be read
     *
     * @return true if there are bytes to follow
     */
    public boolean hasMoreBytes() {
        return (internalPosition < size);
    }

    /**
     * informs how many more bytes can be read
     *
     * @return number of bytes to be read
     */
    public long getBytesLeft() {
        return (size - internalPosition);
    }

    /**
     * Sets the internal pointer to a given position in the data
     *
     * @param position position to set the internal pointer to
     */
    public void seek(long position) throws SeekPastEndException {
        if (position < size)
            internalPosition = position;
        else
            throw new SeekPastEndException(position + " >= " + size);
    }

    public void seekRelative(long i) throws SeekPastEndException {
        seek(internalPosition + i);
    }

    /**
     * reads the requested number of bytes from the file
     *
     * @param number requested number of bytes
     * @return array containing the requested number of bytes
     */
    private byte[] readBytes(long number) throws SeekPastEndException {
        try {
            byte[] byteAr = new byte[(int) number];
            raf.seek(startPosition + internalPosition);
            raf.read(byteAr);
            return byteAr;
        } catch (IOException ex) {
            throw new SeekPastEndException(ex.getMessage());
        }
    }

    public String readString(byte encoding) throws DatamismatchException {
        // The number of 0s that determine a string end.
        int numberStopZeroes = 1;
        String retval = "";
        byte lastByte = 0;
        byte currentByte = 0;
        int bytesRead = 0;

        switch (encoding) {
            case ENCODING_BYTE_ISO88591:
                numberStopZeroes = 1;
                break;
            case ENCODING_BYTE_UTF16:
                numberStopZeroes = 2;
                break;
            case ENCODING_BYTE_UTF16BE:
                numberStopZeroes = 2;
                break;
            case ENCODING_BYTE_UTF8:
                numberStopZeroes = 1;
                break;
            default:
                throw new DatamismatchException("Requested encoding \"" + encoding
                        + "\" unknown.");
        }
        byte[] bAr = Helper.reallocateByteBuffer(256, null);

        try {
            if (numberStopZeroes == 1) {
                currentByte = getByte();
                while (currentByte != 0) {
                    if (bAr.length >= bytesRead)
                        bAr = Helper.reallocateByteBuffer(256, bAr);
                    bAr[bytesRead++] = currentByte;
                    currentByte = getByte();
                }
            } else if (numberStopZeroes == 2) {
                lastByte = 1;
                currentByte = getByte();
                while (currentByte != 0 && lastByte != 0) {
                    if (bAr.length >= bytesRead)
                        bAr = Helper.reallocateByteBuffer(256, bAr);
                    bAr[bytesRead++] = currentByte;
                    lastByte = currentByte;
                    currentByte = getByte();
                }
            }
        } catch (SeekPastEndException ex) {
            // This is handled gracefully. We assume the string is
            // finished now.
        }
        byte[] tempAr = Helper.reallocateByteBuffer(bytesRead
                - (numberStopZeroes - 1), null);
        System.arraycopy(bAr, 0, tempAr, 0, tempAr.length);
        try {
            retval = new String(tempAr, Helper.getStringEncoding(encoding));
        } catch (UnsupportedEncodingException ex) {
            OutputCtr.println(0, "*** Internal Error ***");
            OutputCtr.println(0, ex);
        }

        return retval;
    }
}
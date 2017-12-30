package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.mp3info.Defines;

/**
 * Definition for a source of data
 *
 * @author Florian Heer
 * @version $Id: DataSource.java,v 1.7 2005/03/06 14:57:53 heer Exp $
 */

public interface DataSource extends Defines {
    /**
     * delivers the next available byte
     *
     * @return next byte
     */
    public byte getByte() throws SeekPastEndException;

    /**
     * delivers the next available bytes
     *
     * @param number number of bytes to deliver
     * @return demanded bytes
     */
    public byte[] getBytes(long number) throws SeekPastEndException;

    /**
     * delivers the next bytes until a certain byte or the end is encountered.
     * The stop byte is not included in the return value.
     *
     * @param b at what byte to stop
     * @return demanded bytes
     */
    public byte[] getBytesTo(byte b);

    /**
     * Sets the internal pointer to the start of the data
     */
    public void reset();

    /**
     * informs whether there are more bytes than can be read
     *
     * @return true if there are bytes to follow
     */
    public boolean hasMoreBytes();

    /**
     * informs how many more bytes can be read
     *
     * @return number of bytes availabel
     */
    public long getBytesLeft();

    /**
     * Sets the internal pointer to a given position in the data
     *
     * @param position position to set the internal pointer to
     */
    public void seek(long position) throws SeekPastEndException;


    /**
     * Reads a string. The internal pointer will be set to the byte after
     * the zero(es) delimiting the String.
     *
     * @param encoding Determines the encoding the result is expected to be in.
     * @return the read string or an empty String
     */
    public String readString(byte encoding) throws DatamismatchException;

    /**
     * Moves the internal pointer
     *
     * @param i how much the pointer should be moved
     */
    public void seekRelative(long i) throws SeekPastEndException;
}

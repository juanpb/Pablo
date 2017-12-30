package de.ueberdosis.mp3info.id3v2;

/**
 * Just a marker Exception so you know what happened.
 * This gets thrown if an id3v2-frame tries to get more data than
 * are included in the frame (as described in the header
 *
 * @author Florian Heer
 * @version $Id: SeekPastEndException.java,v 1.3 2003/08/24 18:28:40 heer Exp $
 */
public class SeekPastEndException extends Exception {
    /**
     * default constructor. It is just a marker of Exception
     */
    public SeekPastEndException(String msg) {
        super(msg);
    }
}

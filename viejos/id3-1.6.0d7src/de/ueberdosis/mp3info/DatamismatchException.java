package de.ueberdosis.mp3info;

/**
 * Gets thrown if received data does not match expected data.
 *
 * @author Florian Heer
 * @version $Id: DatamismatchException.java,v 1.2 2005/02/20 20:27:15 heer Exp $
 */

public class DatamismatchException extends Exception {
    /**
     * Creates an instance of the exception
     *
     * @param message an explaning message
     */
    public DatamismatchException(String message) {
        super(message);
    }
}

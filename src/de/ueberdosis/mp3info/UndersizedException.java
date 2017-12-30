package de.ueberdosis.mp3info;

/** Exception to be used if more data is needed than we got. 
 * @author Florian Heer
 * @version $Id: UndersizedException.java,v 1.4 2003/10/24 12:46:29 heer Exp $
 */
public class UndersizedException extends Exception {
    /** The number of values (mostly bytes) that could be read */
    private int sizeGot=0;

    /** The number of values (mostly bytes) that were needed */
    private int sizeNeeded=0;

    /** Creates an instance of this Exception without additional information
     * about needed and available data.
     * @param message an explaining text
     * @deprecated
     */
    public UndersizedException (String message) { 
        super (message); 
    }

    /** Creates an instance of this Exception
     * @param message an explaining message
     * @param got the number of values that was available
     * @param needed the number of values that was needed
     */
    public UndersizedException (String message, int got, int needed) {
        super (message);
        sizeGot = got; sizeNeeded = needed;
    }

    /** Creates an instance of this Exception with a generic message 
     * @param got the number of values that was available
     * @param needed the number of values that was needed
     */
    public UndersizedException (int got, int needed) {
        super ("We expected the number to be at least "
               + needed
               + " but we got only "
               +got);
        sizeGot = got; sizeNeeded = needed;
    }

    /** @return the number of values that was available */
    public int getAvailableNumber () { return sizeGot; }

    /** @return the number of values that was needed */
    public int getNeededNumber () { return sizeNeeded; }
}

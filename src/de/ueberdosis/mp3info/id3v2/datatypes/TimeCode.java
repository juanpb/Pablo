/*
 * Created on 05.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ueberdosis.mp3info.id3v2.datatypes;

/**
 * @author Florian Heer &lt;heer@ueberdosis.de&gt;
 * @version $Id: TimeCode.java,v 1.2 2005/03/06 14:57:32 heer Exp $
 */
public class TimeCode {
    private int timeStamp = 0;

    private int bpm = 0;

    /**
     * @return Returns the bpm.
     */
    public int getBpm () {
        return bpm;
    }

    /**
     * @param bpm The bpm to set. The value must be between 2 and 510 (inclusive).
     * @return the current value, might not be what you set, if your bpm exceeded
     * the boundaries.
     */
    public int setBpm (int bpm) {
        if (bpm > 1 && bpm < 510)
            this.bpm = bpm;
        return bpm;
    }

    /**
     * @return Returns the timeStamp.
     */
    public int getTimeStamp () {
        return timeStamp;
    }

    /**
     * @param timeStamp The timeStamp to set.
     */
    public void setTimeStamp (int timeStamp) {
        this.timeStamp = timeStamp;
    }

}
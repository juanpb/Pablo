/*
 * Created on 06.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ueberdosis.mp3info.id3v2.datatypes;

/** Datatype used in FrameSYLT
 * @author Florian Heer &lt;heer@ueberdosis.de&gt;
 * @version $Id: TextData.java,v 1.1 2005/03/06 00:17:20 heer Exp $
 */
public class TextData {
    private int timeStamp = 0;
    private String text = "";

    /**
     * @return Returns the text.
     */
    public String getText () {
        return text;
    }
    /**
     * @param text The text to set.
     */
    public void setText (String text) {
        this.text = text;
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
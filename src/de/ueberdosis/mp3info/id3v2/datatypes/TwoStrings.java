/*
 * Created on 06.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ueberdosis.mp3info.id3v2.datatypes;

/**
 * @author Florian Heer &lt;heer@ueberdosis.de&gt;
 *
 */
public class TwoStrings {
    private String one = "";

    private String two = "";

    /**
     * @return Returns the one.
     */
    public String getOne () {
        return one;
    }

    /**
     * @param one The one to set.
     */
    public void setOne (String one) {
        this.one = one;
    }

    /**
     * @return Returns the two.
     */
    public String getTwo () {
        return two;
    }

    /**
     * @param two The two to set.
     */
    public void setTwo (String two) {
        this.two = two;
    }

    public TwoStrings () {
    };

    public TwoStrings (TwoStrings ts) {
        one = ts.one;
        two = ts.two;
    }

    public Object clone () {
        return new TwoStrings (this);
    }

}
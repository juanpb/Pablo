package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Base class for all URL frames (W000-WZZZ).
 * 
 * @author Florian Heer
 * @version $Id: FrameW.java,v 1.6 2005/03/05 20:25:00 heer Exp $
 */
public class FrameW extends ID3V2Frame {
    String url = "";

    public String getLongName () {
        return "Generic url frame";
    }

    /**
     * Copying constructor The information provided will be copied if the frame
     * is of an appropriate type. If it is not, an empty frame is created.
     * 
     * @param frm
     *            source of information
     */
    public FrameW (ID3V2Frame frm) {
        super (frm);
        if (frm instanceof FrameW) {
            FrameW fw = (FrameW) frm;
            url = fw.url;
        }
    }

    /**
     * Reading constructor Reads the information provided into a new frame
     * object.
     * 
     * @param frm
     *            basic information about the frame to create
     * @param ds
     *            where to read the data from.
     */
    public FrameW (ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {

            StringBuffer sb = new StringBuffer ();
            if (ds.hasMoreBytes ()) {
                byte b = ds.getByte ();
                // read in the text... 0 terminated string or to the end of the
                // available data, as described.
                while (b != 0) {
                    sb.append ((char) b);
                    if (ds.hasMoreBytes ())
                        b = ds.getByte ();
                    else
                        b = 0;
                }
                url = sb.toString ();
            } else
                url = "";

            // If the textstring is followed by a termination ($00) all the
            //following information should be ignored and not be displayed.

        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, "URL frame can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public String toString () {
        return getLongName () + "\nURL : " + url;
    }
    
    public byte [] getData () {
        return url.getBytes();
    }

}
/*
 * Created on 22.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.UndersizedException;

/**
 * @author Florian Heer &lt;heer@ueberdosis.de&gt;
 *
 */
public class ID3V2FrameDefaultImpl extends ID3V2Frame {
    byte [] dataArray = null;
    
    public ID3V2FrameDefaultImpl (String id) {
        super (id);
    }

    /** Copy constructor */
    public ID3V2FrameDefaultImpl (ID3V2Frame frm) {
        super (frm);
        dataArray = frm.getData ();
    }

    /**
     * decoding constructor. Is now deprecated as it only supported ID3V2.4.0.
     * 
     * @param frameBytes
     *            ID3V2_FRAME_HEADER_SIZE bytes that contain vital information
     *            about the Frame.
     * @deprecated
     */
    public ID3V2FrameDefaultImpl (byte[] frameBytes) throws UndersizedException,
            NotAnID3V2FrameException {
        super (frameBytes);
    }

    /**
     * decoding constructor.
     * 
     * @param frameBytes
     *            ID3V2_FRAME_HEADER_SIZE bytes that contain vital information
     *            about the Frame.
     */
    public ID3V2FrameDefaultImpl (byte[] frameBytes, int id3Version, int id3SubVersion)
            throws UndersizedException, NotAnID3V2FrameException {
        super (frameBytes, id3Version, id3SubVersion);
    }
    
    public ID3V2FrameDefaultImpl (ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
        super (frm);
        dataArray = Helper.reallocateByteBuffer((int)ds.getBytesLeft());
        int datasize = (int)ds.getBytesLeft();
        for (int i=0; i<datasize; i++)
            dataArray [i] = ds.getByte();
    }
    
    public byte [] getData () {
        return dataArray;
    }

}

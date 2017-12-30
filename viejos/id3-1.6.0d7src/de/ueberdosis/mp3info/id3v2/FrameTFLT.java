package de.ueberdosis.mp3info.id3v2;

/**
 * Filetype Frame.
 * The 'File type' frame indicates which type of audio this tag
 * defines. The following type and refinements are defined:
 * <p/>
 * MPG   MPEG Audio
 * /1 MPEG 1/2 layer I
 * /2 MPEG 1/2 layer II
 * /3 MPEG 1/2 layer III
 * /2.5 MPEG 2.5
 * /AAC Advanced audio compression
 * VQF Transform-domain Weighted Interleave Vector Quantization
 * PCM Pulse Code Modulated audio
 * <p/>
 * but other types may be used, not for these types though. This
 * is used in a similar way to the predefined types in the "TMED"
 * frame, but without parentheses. If this frame is not present
 * audio type is assumed to be "MPG".
 *
 * @author Florian Heer
 * @version $Id: FrameTFLT.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTFLT extends FrameT {
    public String getLongName() {
        return "Filetype frame";
    }

    public FrameTFLT(ID3V2Frame frm) {
        super(frm);
    }

    public FrameTFLT(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm, ds);
    }

    public String toString() {
        return getLongName() + " Encoding: " + encoding + "\nFiletype : "
                + text;
    }

}

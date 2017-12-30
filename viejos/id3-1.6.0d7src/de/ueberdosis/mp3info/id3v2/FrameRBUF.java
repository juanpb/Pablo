package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.util.OutputCtr;

/**
 * Recommended buffer size Sometimes the server from which a audio file is
 * streamed is aware of transmission or coding problems resulting in
 * interruptions in the audio stream. In these cases, the size of the buffer can
 * be recommended by the server using this frame. If the 'embedded info flag' is
 * true (1) then this indicates that an ID3 tag with the maximum size described
 * in 'Buffer size' may occur in the audiostream. In such case the tag should
 * reside between two MPEG frames, if the audio is MPEG encoded. If the position
 * of the next tag is known, 'offset to next tag' may be used. The offset is
 * calculated from the end of tag in which this frame resides to the first byte
 * of the header in the next. This field may be omitted. Embedded tags are
 * generally not recommended since this could render unpredictable behaviour
 * from present software/hardware.
 * <p/>
 * For applications like streaming audio it might be an idea to embed tags into
 * the audio stream though. If the clients connects to individual connections
 * like HTTP and there is a possibility to begin every transmission with a tag,
 * then this tag should include a 'recommended buffer size' frame. If the client
 * is connected to a arbitrary point in the stream, such as radio or multicast,
 * then the 'recommended buffer size' frame should be included in every tag.
 * Every tag that is picked up after the initial/first tag is to be considered
 * as an update of the previous one. E.g. if there is a "TIT2" frame in the
 * first received tag and one in the second tag, then the first should be
 * 'replaced' with the second.
 * <p/>
 * The 'Buffer size' should be kept to a minimum. There may only be one "RBUF"
 * frame in each tag.
 * <p/>
 * &lt;Header for 'Recommended buffer size', ID: "RBUF"&gt;<br>
 * Buffer size $xx xx xx<br>
 * Embedded info flag %0000000x<br>
 * Offset to next tag $xx xx xx xx
 *
 * @author Florian Heer
 * @version $Id: FrameRBUF.java,v 1.5 2005/03/06 14:07:15 heer Exp $
 */

public class FrameRBUF extends ID3V2Frame {
    private int buffer = 0;

    private boolean embeddedInfo = false;

    private int offsetToNextTag = 0;

    public String getLongName() {
        return "Recommended buffer frame";
    }

    public FrameRBUF(ID3V2Frame frm) {
        super(frm);
        if (frm instanceof FrameRBUF) {
            FrameRBUF fr = (FrameRBUF) frm;
            buffer = fr.buffer;
            embeddedInfo = fr.embeddedInfo;
            offsetToNextTag = fr.offsetToNextTag;
        }
    }

    public FrameRBUF(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource!
        try {
            buffer = Helper.decodeBytesToInt(ds.getBytes(4));
            embeddedInfo = ds.getByte() == 1;
            offsetToNextTag = Helper.decodeBytesToInt(ds.getBytes(4));
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName()
                    + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }

    public byte[] getData() {
        byte[] bAr = new byte[8];
        byte[] buffAr = Helper.intToByteArray(buffer, 3);
        byte[] offAr = Helper.intToByteArray(offsetToNextTag, 4);
        System.arraycopy(buffAr, 0, bAr, 0, 3);
        bAr[3] = (byte) (embeddedInfo ? 1 : 0);
        System.arraycopy(offAr, 0, bAr, 4, 4);
        return bAr;
    }

    public String toString() {
        return getLongName() + "\nbuffer: " + buffer + "\nEmbedded Info: "
                + embeddedInfo + "\nOffset to next tag: " + offsetToNextTag;
    }

}
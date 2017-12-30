package de.ueberdosis.mp3info;

import de.ueberdosis.mp3info.id3v2.ID3V2Tag;
import de.ueberdosis.util.OutputCtr;

/**
 * This class contains all interesting information about an mp3.
 *
 * @author Florian Heer <heer@ueberdosis.de>
 * @version $Id: ExtendedID3Tag.java,v 1.13 2005/02/22 18:00:57 heer Exp $
 */

public class ExtendedID3Tag extends ID3Tag {
    /**
     * contains the ID of the MPEG version that is used
     */
    private int mpegID;
    /**
     * contains the ID of the layer that is used
     */
    private int layer;
    /**
     * contains the bitrate in use (or the higher, if VBR is used)
     */
    private int bitrate;
    /**
     * contains either 0 (or the lower bitrate in use, if VBR is used)
     */
    private int bitrate2 = 0;
    /**
     * is the frame CRC protected?
     */
    private boolean crc;
    /**
     * the sample frequency used
     */
    private int freq;
    /**
     * is the frame padded?
     */
    private boolean padded;
    /**
     * the chanel mode in use
     */
    private int chanMode = 4;
    /**
     * is the material copyright protected?
     */
    private boolean copyright;
    /**
     * is the material the original?
     */
    private boolean original;
    /**
     * The emphasis used
     */
    private int emphasis = 4;
    /**
     * size of the frame
     */
    private long size = 0;
    /**
     * The position of this tag in the file.
     * Used to find the first FrameHeader later on.
     */
    private long position = 0;
    /**
     * Running time in milliseconds. Changed from seconds to milliseconds
     * since V1.5. Methods are still compatible.
     */
    private long runtime = 0;
    /**
     * the number of frames encountered
     */
    private int frameCount = 0;

    /**
     * size of the last encountered ID3v2tag
     * (only the last one is taken into account right now
     */
    private long iD3v2Size = 0;
    /**
     * The last ID3v2tag encountered
     */
    private ID3V2Tag v2tag = null;

    /**
     * String represenations of the bitrates
     */
    //        V1,L1   V1,L2      V1,L3      V2,L1      V2, L2 & L3

    public static final String[][] bitrates =
            {{"free", "free", "free", "free", "free"},
                    {"32", "32", "32", "32", "8"},
                    {"64", "48", "40", "48", "16"},
                    {"96", "56", "48", "56", "24"},
                    {"128", "64", "56", "64", "32"},
                    {"160", "80", "64", "80", "40"},
                    {"192", "96", "80", "96", "48"},
                    {"224", "112", "96", "112", "56"},
                    {"256", "128", "112", "128", "64"},
                    {"288", "160", "128", "144", "80"},
                    {"320", "192", "160", "160", "96"},
                    {"352", "224", "192", "176", "112"},
                    {"384", "256", "224", "192", "128"},
                    {"416", "320", "256", "224", "144"},
                    {"448", "384", "320", "256", "160"},
                    {"bad", "bad", "bad", "bad", "bad"}
            };

    /**
     * Int representations of the bitrates. -1 means free, -2 bad.
     * In any implementation, a bitrate of < 1 should result in a
     * new request to @see bitrates .
     */
    public static final int[][] bitratesI = {

            {-1, -1, -1, -1, -1},
            {32, 32, 32, 32, 8},
            {64, 48, 40, 48, 16},
            {96, 56, 48, 56, 24},
            {128, 64, 56, 64, 32},
            {160, 80, 64, 80, 40},
            {192, 96, 80, 96, 48},
            {224, 112, 96, 112, 56},
            {256, 128, 112, 128, 64},
            {288, 160, 128, 144, 80},
            {320, 192, 160, 160, 96},
            {352, 224, 192, 176, 112},
            {384, 256, 224, 192, 128},
            {416, 320, 256, 224, 144},
            {448, 384, 320, 256, 160},
            {-2, -2, -2, -2, -2}
    };


    // MPEG 1 MPEG2 MPEG2.5
    /**
     * String representation of the samplerates
     */
    public static final String frequencies[][] = {
            {"44100", "22050", "11025"},
            {"48000", "24000", "12000"},
            {"32000", "16000", "8000"},
            {"reserved", "reserved", "reserved"}
    };

    /**
     * Int representation of the samplerates.
     * Values lower than 1 are special and should result in a request
     * to frequencies-
     */
    public static final int frequenciesI[][] = {
            {44100, 22050, 11025},
            {48000, 24000, 12000},
            {32000, 16000, 8000},
            {-1, -1, -1}
    };


    /**
     * String representation of the channel modes
     */
    public static final String channelModes[] = {
            "Stereo", "Joint Stereo (Stereo)", "Dual Channel (Stereo)",
            "Single Channel (Mono)", "bad"
    };

    /**
     * Int representation of the channel modes.
     * 1 = mono, 2 = stereo, sound logical, doesn't it?
     * 0 is an error and should result in a request to @see channelModes .
     */
    public static final int channels[] = {
            2, 2, 2, 1, 0
    };

    /**
     * String representation of the possible emphasis-values
     */
    public static final String emps[] = {
            "none", "50/15ms", "reserved", "CCIT J.17", "bad"
    };


    public ExtendedID3Tag(ID3Tag tag) {
        super(tag);
    }

    /**
     * Just for internal use...
     */
    ExtendedID3Tag() {
    }

    /**
     * Sets the id of MPeg.
     * Watch out that this is NOT the same as the MPeg-Version.
     * MPeg-ID 3 equals "Mpeg 1".
     */
    public void setMpegID(int id) {
        mpegID = id;
    }

    /**
     * returns the MpegID.
     */
    public int getMpegID() {
        return mpegID;
    }

    /**
     * returns a String representation of the Mpeg-level.
     */
    public String getMpegIDS() {
        String retval = "error";
        switch (getMpegID()) {
            case 0:
                retval = "2.5";
                break;
            case 2:
                retval = "2";
                break;
            case 3:
                retval = "1";
                break;
            default:
                retval = "unknown";
                break;
        }
        return retval;
    }

    /**
     * Sets the layer of the mp3. watch out, this ID is not the same
     * as the real Layer. Layer 1 equals "Layer III".
     */
    public void setLayer(int l) {
        layer = l;
    }

    /**
     * @return the layer-ID @see setLayer
     */
    public int getLayer() {
        return layer;
    }

    /**
     * @return a String representation of the Layer
     */
    public String getLayerS() {
        String retval = "error";
        switch (getLayer()) {
            case 0:
                retval = "reserved";
                break;
            case 1:
                retval = "III";
                break;
            case 2:
                retval = "II";
                break;
            case 3:
                retval = "I";
                break;
            default:
                retval = "unknown";
                break;
        }
        return retval;
    }

    /**
     * @return the layer
     */
    public int getLayerI() {
        int retval = 0;
        switch (getLayer()) {
            case 1:
                retval = 3;
                break;
            case 2:
                retval = 2;
                break;
            case 3:
                retval = 1;
                break;
            default:
                retval = -1;
                break;
        }
        return retval;
    }

    /**
     * Sets the bitrate as an ID.
     * Watch out that this ID is NOT the real bitrate.
     * Only a pointer to a 2-dimensional array, dependend on MpegID and Layer
     */
    public void setBitrate(int b) {
        bitrate = b;
    }

    /**
     * Sets the lower bitrate as an ID.
     * Watch out that this ID is NOT the real bitrate.
     * Only a pointer to a 2-dimensional array, dependend on MpegID and Layer
     */
    public void setBitrate2(int b) {
        bitrate2 = b;
    }

    /**
     * gives you a String representation of the bitrate
     */
    public String getBitrateS() {
        String retval = "error";
        if (bitrate < bitrates.length) {
            if ((getMpegID() == 3) && (getLayer() == 3))
                retval = bitrates[bitrate][0];
            else if ((getMpegID() == 3) && (getLayer() == 2))
                retval = bitrates[bitrate][1];
            else if ((getMpegID() == 3) && (getLayer() == 1))
                retval = bitrates[bitrate][2];
            else if ((getMpegID() == 2) && (getLayer() == 3))
                retval = bitrates[bitrate][3];
            else if (getMpegID() == 2)
                retval = bitrates[bitrate][4];
        }
        return retval;
    }

    /**
     * @return the bitrate-ID
     */
    public int getBitrate() {
        return bitrate;
    }

    /**
     * @return the real higher bitrate
     *         or 0 if an error occured or this information
     *         is otherwise unavailable
     */
    public int getBitrateI() {
        int retval = 0;
        if (bitrate < bitratesI.length) {
            if ((getMpegID() == 3) && (getLayer() == 3))
                retval = bitratesI[bitrate][0];
            else if ((getMpegID() == 3) && (getLayer() == 2))
                retval = bitratesI[bitrate][1];
            else if ((getMpegID() == 3) && (getLayer() == 1))
                retval = bitratesI[bitrate][2];
            else if ((getMpegID() == 2) && (getLayer() == 3))
                retval = bitratesI[bitrate][3];
            else if (getMpegID() == 2)
                retval = bitratesI[bitrate][4];
        }
        return retval;
    }

    /**
     * @return the real lower bitrate
     *         or 0 if an error occured or this information
     *         is otherwise unavailable
     */
    public int getBitrate2I() {
        int retval = 0;
        if (bitrate2 < bitrates.length) {
            if ((getMpegID() == 3) && (getLayer() == 3))
                retval = bitratesI[bitrate2][0];
            else if ((getMpegID() == 3) && (getLayer() == 2))
                retval = bitratesI[bitrate2][1];
            else if ((getMpegID() == 3) && (getLayer() == 1))
                retval = bitratesI[bitrate2][2];
            else if ((getMpegID() == 2) && (getLayer() == 3))
                retval = bitratesI[bitrate2][3];
            else if (getMpegID() == 2)
                retval = bitratesI[bitrate2][4];
        }
        return retval;
    }

    /**
     * @return a String representation of the lower bitrate that is used in VBR
     */
    public String getBitrate2S() {
        String retval = "error";
        if (bitrate2 < bitrates.length) {
            if ((getMpegID() == 3) && (getLayer() == 3))
                retval = bitrates[bitrate2][0];
            else if ((getMpegID() == 3) && (getLayer() == 2))
                retval = bitrates[bitrate2][1];
            else if ((getMpegID() == 3) && (getLayer() == 1))
                retval = bitrates[bitrate2][2];
            else if ((getMpegID() == 2) && (getLayer() == 3))
                retval = bitrates[bitrate2][3];
            else if (getMpegID() == 2)
                retval = bitrates[bitrate2][4];
        }
        return retval;

    }


    /**
     * Sets if CRC-protection is on
     */
    public void setCrc(boolean state) {
        crc = state;
    }

    /**
     * @return true if CRC-Protection is on
     */
    public boolean getCrc() {
        return crc;
    }

    /**
     * Sets the samplerate as an ID.
     * This is not the same as the real samplerate
     */
    public void setFrequency(int fre) {
        freq = fre;
    }

    /**
     * @return the samplerate as an ID.
     *         This is not the same as the real samplerate
     */
    public int getFrequency() {
        return freq;
    }

    /**
     * @return a String representation of the samplerate.
     */
    public String getFrequencyS() {
        String retval = "error";
        if (freq < frequencies.length) {
            switch (getMpegID()) {
                case 3:
                    retval = frequencies[freq][0];
                    break;
                case 2:
                    retval = frequencies[freq][1];
                    break;
                case 0:
                    retval = frequencies[freq][2];
                    break;
            }
        }
        return retval;
    }

    /**
     * @return the real samplerate
     *         or 0 if this information is unavailable.
     */
    public int getFrequencyI() {
        int retval = 0;
        if (freq < frequencies.length) {
            switch (getMpegID()) {
                case 3:
                    retval = frequenciesI[freq][0];
                    break;
                case 2:
                    retval = frequenciesI[freq][1];
                    break;
                case 0:
                    retval = frequenciesI[freq][2];
                    break;
            }
        }
        return retval;
    }

    /**
     * Sets if padding of the frame that supplied the extended information is on
     */
    public void setPadding(boolean pad) {
        padded = pad;
    }

    /**
     * Gets if padding of the frame that supplied the extended information is on
     */
    public boolean getPadding() {
        return padded;
    }

    /**
     * Sets the channel Mode as an ID.
     */
    public void setChannelMode(int mode) {
        if ((mode >= 0) && (mode < 4)) chanMode = mode;
        else chanMode = 4;
    }

    /**
     * @return the channel Mode as an ID
     */
    public int getChannelMode() {
        return chanMode;
    }

    /**
     * @return a String representation of the channel mode
     */
    public String getChannelModeS() {
        return channelModes[chanMode];
    }

    /**
     * Sets if the material contained in this mp3 is copyrighted
     */
    public void setCopyright(boolean cp) {
        copyright = cp;
    }

    /**
     * @return true if the material contained in this mp3 is copyrighted
     */
    public boolean getCopyright() {
        return copyright;
    }

    /**
     * Sets if this mp3 is the original, or if it's only a copy
     */
    public void setOriginal(boolean or) {
        original = or;
    }

    /**
     * @return true if this mp3 is the original, or if it's only a copy
     */
    public boolean getOriginal() {
        return original;
    }

    /**
     * Sets the emphasis as an ID
     */
    public void setEmphasis(int em) {
        if ((em >= 0) && (em < 4)) emphasis = em;
        else emphasis = 4;
    }

    /**
     * @return the emphasis as an ID
     */
    public int getEmphasis() {
        return emphasis;
    }

    /**
     * @return a String representation of the emphasis used
     */
    public String getEmphasisS() {
        return emps[emphasis];
    }

    /**
     * @return the size of the examined file
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of the examined file
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Sets the running time of the examined file in seconds
     *
     * @deprecated as of mp3info V 1.6
     */
    public void setRuntime(int time) {
        // Error cases still use this
        if (time > 0) {
            OutputCtr.println(0, "Somebody set the runtime directly.\n"
                    + "This is deprecated!");
            runtime = (time * 1000);
        } else runtime = 0;
    }

    /**
     * Sets the running time of the examined file in milliseconds
     */
    public void setKRuntime(long time) {
        runtime = time;
    }

    /**
     * @return the running time in seconds.
     *         This value is either correctly computed while counting all frames
     *         or guessed by assuming that the whole file uses the same bitrate
     *         as this frame.
     */
    public int getRuntime() {
        if (runtime == 0) {
            // Preventing division by zero
            int bitrate = getBitrateI();
            if (bitrate != 0)
                return (int) (getSize() /
                        ((getBitrateI() * 1000.0) / 8.0));
            return 0;
        }
        // Remember, we now use milliseconds.
        return (int) (runtime / 1000);
    }

    /**
     * Sets the amount of frames contained in this file
     */
    public void setFramecount(int count) {
        frameCount = count;
    }

    /**
     * @return the frame count, or 0 if there has been an error counting
     */
    public int getFramecount() {
        return frameCount;
    }

    /**
     * creates a rather large String with all relevant information
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (!isValidTag()) {
            sb.append("This file has no tag.\n");
        }
        sb.append("Title   : ").append(getTitle())
                .append("\nArtist  : ").append(getArtist())
                .append("\nAlbum   : ").append(getAlbum())
                .append("\nYear    : ").append(getYear())
                .append("\nComment : ").append(getComment())
                .append("\nGenre   : ").append(genres[getGenre()])
                .append("\nTrack   : ").append(getTrack())
                .append("\nMpeg ").append(getMpegIDS())
                .append(" Layer ").append(getLayerS())
                .append("\nBitrate : ").append(getBitrateS())
                .append(" kbps\nBitrate2: ").append(getBitrate2S())
                .append(" kbps\nSampling-Rate: ")
                .append(getFrequencyS()).append("\nCRC-Protection: ");
        if (crc) sb.append("on");
        else sb.append("off");
        sb.append("\nChannel-Mode: ").append(getChannelModeS())
                .append("\nCopyrighted : ");

        if (getCopyright()) sb.append("on");
        else sb.append("off");

        sb.append("\nOriginal : ");
        if (getOriginal()) sb.append("on");
        else sb.append("off");

        sb.append("\nEmphasis: ").append(getEmphasisS());
        if (runtime == 0)
            sb.append("\nGuessed playing time: ");
        else
            sb.append("\nPlaying time: ");
        sb.append(getRuntime()).append(" seconds.");

        if (frameCount != 0)
            sb.append("\nFramecount: ").append(getFramecount());


        return sb.toString();

    }

    /**
     * @return one line with technical information
     */
    public String getTechnicalString() {
        StringBuffer sb = new StringBuffer()
                .append("\nMpeg ").append(getMpegIDS())
                .append(" Layer ").append(getLayerS())
                .append(" BR: ").append(getBitrateS()).append(" kbps SR: ")
                .append(getFrequencyS()).append(" CRC: ");
        if (crc) sb.append("on");
        else sb.append("off");
        sb.append(" ChMo: ").append(getChannelModeS())
                .append(" cpr: ");

        if (getCopyright()) sb.append("on");
        else sb.append("off");

        sb.append(" Orig: ");
        if (getOriginal()) sb.append("on");
        else sb.append("off");

        sb.append(" Emp: ").append(getEmphasisS());

        return sb.toString();

    }

    /**
     * Sets the position of this frame in the file
     */
    void setPosition(long framePosition) {
        position = framePosition;
    }

    /**
     * @return the position of this frame in the file
     */
    long getPosition() {
        return position;
    }

    /**
     * Sets the size of a possible V2 tag
     */
    void setID3v2Size(long size) {
        if (size >= 0) iD3v2Size = size;
    }

    /**
     * @return the size of a possible V2 tag
     */
    long getID3v2Size() {
        return iD3v2Size;
    }

    /**
     * Sets a V2 tag
     */
    void setID3V2Tag(ID3V2Tag tag) {
        v2tag = tag;
    }

    /**
     * @return a possible V2 tag or null
     */
    public ID3V2Tag getV2Tag() {
        return v2tag;
    }

    /**
     * compares the bitrates stored in the other tag and changes the
     * internal values, if the other higher BR is higher than this ones
     * higher BR or the other lower BR is higher than this ones lower BR
     */
    public void updateBitrates(ExtendedID3Tag tag) {
        int mybr1 = getBitrateI();
        int mybr2 = getBitrate2I();
        int tbr = tag.getBitrateI();
        int br = 0;

        if (tbr > mybr1) {
            if (mybr1 < mybr2 || mybr2 <= 0) {
                br = findBr(mybr2);
                bitrate2 = (br == -1) ? bitrate2 : br;
            }
            br = findBr(tbr);
            bitrate = (br == -1) ? bitrate2 : br;
        } else if (tbr < mybr2 || mybr2 <= 0) {
            br = findBr(tbr);
            bitrate2 = (br == -1) ? bitrate2 : br;
        }
    }

    private int findBr(int br) {
        int offset = 0;
        int retval = -1;

        // First we look for the right Mpeg & Layer-combination
        // I think MP3s shouldn't change these information in between ;)
        if ((getMpegID() == 3) && (getLayer() == 3))
            offset = 0;
        else if ((getMpegID() == 3) && (getLayer() == 2))
            offset = 1;
        else if ((getMpegID() == 3) && (getLayer() == 1))
            offset = 2;
        else if ((getMpegID() == 2) && (getLayer() == 3))
            offset = 3;
        else if (getMpegID() == 2)
            offset = 4;

        for(int i = 0; i < bitratesI.length && retval == -1; i++)
            if (bitratesI[i][offset] == br) retval = i;

        return retval;

    }

}

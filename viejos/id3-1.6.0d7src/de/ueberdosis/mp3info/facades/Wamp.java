package de.ueberdosis.mp3info.facades;

import de.ueberdosis.mp3info.ID3Tag;
import de.ueberdosis.mp3info.id3v2.*;

import java.util.Enumeration;

//import java.util.Iterator;

/**
 * This encapsulates the information you can access via WinAmps property page
 *
 * @author Florian Heer
 * @version $Id: Wamp.java,v 1.6 2005/02/22 18:05:30 heer Exp $
 */

public class Wamp {
    /**
     * The V2 tag that is edited
     */
    private ID3V2Tag tag = null;

    /**
     * The Track-frame that ist edited
     */
    private FrameTRCK trck = null;

    /**
     * The Encoder-frame that is edited
     */
    private FrameTENC tenc = null;

    /**
     * The URL-frame that is editedt
     */
    private FrameWXXX wxxx = null;

    /**
     * The Copyright-frame that is edited
     */
    private FrameTCOP tcop = null;

    private FrameTOPE tope = null;

    private FrameTCOM tcom = null;

    /**
     * The Comment-frame that is edited
     */
    private FrameCOMM comm = null;

    /**
     * The Year-frame that is edited
     */
    private FrameTYER tyer = null;

    private FrameTCON tcon = null;

    /**
     * The Album-frame that is edited
     */
    private FrameTALB talb = null;

    private FrameTPE1 tpe1 = null;

    /**
     * The Title-frame that is edited
     */
    private FrameTIT2 tit2 = null;

    /**
     * Creates a facade with an empty Tag
     */
    public Wamp() {
        tag = new ID3V2Tag();
        trck = new FrameTRCK();
        tenc = new FrameTENC();
        wxxx = new FrameWXXX();
        tcop = new FrameTCOP();
        tope = new FrameTOPE();
        tcom = new FrameTCOM();
        comm = new FrameCOMM();
        tyer = new FrameTYER();
        tcon = new FrameTCON();
        talb = new FrameTALB();
        tpe1 = new FrameTPE1();
        tit2 = new FrameTIT2();

        tag.addFrame(trck);
        tag.addFrame(tenc);
        tag.addFrame(wxxx);
        tag.addFrame(tcop);
        tag.addFrame(tope);
        tag.addFrame(tcom);
        tag.addFrame(comm);
        tag.addFrame(tyer);
        tag.addFrame(tcon);
        tag.addFrame(talb);
        tag.addFrame(tpe1);
        tag.addFrame(tit2);
    }

    /**
     * Copies information from a V1 tag to a V2 tag
     *
     * @param v1tag information to be copied
     */
    public Wamp(ID3Tag v1tag) {
        this();
        trck.setText("" + v1tag.getTrack());
        tyer.setText(v1tag.getYear());
        tcon.setText("(" + v1tag.getGenre() + ")" + v1tag.getGenreS());
        talb.setText(v1tag.getAlbum());
        tpe1.setText(v1tag.getArtist());
        tit2.setText(v1tag.getTitle());
        comm.setText(v1tag.getComment());
    }

    /**
     * Copies information from a V2 tag
     *
     * @param v2tag information to be copied
     */
    public Wamp(ID3V2Tag v2tag) {
        tag = new ID3V2Tag(v2tag);
        //Iterator iter = tag.getFrames().iterator ();
        //while (iter.hasNext ()) {
        //    ID3V2Frame frm = (ID3V2Frame)iter.next ();
        Enumeration iter = tag.getFrames().elements();
        while (iter.hasMoreElements()) {
            ID3V2Frame frm = (ID3V2Frame) iter.nextElement();
            if (frm.getFrameID().equals("TRCK"))
                trck = (FrameTRCK) frm;
            else if (frm.getFrameID().equals("TENC"))
                tenc = (FrameTENC) frm;
            else if (frm.getFrameID().equals("WXXX"))
                wxxx = (FrameWXXX) frm;
            else if (frm.getFrameID().equals("TCOP"))
                tcop = (FrameTCOP) frm;
            else if (frm.getFrameID().equals("TOPE"))
                tope = (FrameTOPE) frm;
            else if (frm.getFrameID().equals("TCOM"))
                tcom = (FrameTCOM) frm;
            else if (frm.getFrameID().equals("COMM"))
                comm = (FrameCOMM) frm;
            else if (frm.getFrameID().equals("TYER"))
                tyer = (FrameTYER) frm;
            else if (frm.getFrameID().equals("TCON"))
                tcon = (FrameTCON) frm;
            else if (frm.getFrameID().equals("TALB"))
                talb = (FrameTALB) frm;
            else if (frm.getFrameID().equals("TPE1"))
                tpe1 = (FrameTPE1) frm;
            else if (frm.getFrameID().equals("TIT2"))
                tit2 = (FrameTIT2) frm;
        }
    }

    /**
     * @return a valid V2 tag containing the current information
     */
    public ID3V2Tag getV2Tag() {
        return tag;
    }

    /**
     * @return a valid V1 tag containing the current information. Everything has
     *         been (if neccessary) truncated to fit, of course.
     */
    public ID3Tag getV1Tag() {
        ID3Tag v1tag = new ID3Tag();
        v1tag.setTrack(getTrack());
        v1tag.setYear(getYear());
        v1tag.setGenre(getGenre());
        v1tag.setAlbum(getAlbum());
        v1tag.setArtist(getArtist());
        v1tag.setTitle(getTitle());
        v1tag.setComment(getComment());
        return v1tag;
    }

    /**
     * @return Title of the mp3
     */
    public String getTitle() {
        if (tit2 != null)
            return tit2.getText();
        return "";
    }

    /**
     * Sets the title of the mp3
     */
    public void setTitle(String tit) {
        if (tit2 == null) {
            tit2 = new FrameTIT2();
            tag.addFrame(tit2);
        }
        tit2.setText(tit);
    }

    /**
     * @return Artist of the mp3
     */
    public String getArtist() {
        if (tpe1 != null)
            return tpe1.getText();
        return "";
    }

    /**
     * Sets the artist of the mp3
     */
    public void setArtist(String art) {
        if (tpe1 == null) {
            tpe1 = new FrameTPE1();
            tag.addFrame(tpe1);
        }
        tpe1.setText(art);
    }

    /**
     * @return Name of the album
     */
    public String getAlbum() {
        if (talb != null)
            return talb.getText();
        return "";
    }

    /**
     * Sets the name of the album
     */
    public void setAlbum(String alb) {
        if (talb == null) {
            talb = new FrameTALB();
            tag.addFrame(talb);
        }
        talb.setText(alb);
    }

    /**
     * @return release-year of the original song
     */
    public String getYear() {
        if (tyer != null)
            return tyer.getText();
        return "";
    }

    /**
     * Sets the release-year of the original song
     */
    public void setYear(String y) {
        if (tyer == null) {
            tyer = new FrameTYER();
            tag.addFrame(tyer);
        }
        tyer.setText(y);
    }

    /**
     * @return Comment included in the tag
     */
    public String getComment() {
        if (comm != null)
            return comm.getText();
        return "";
    }

    /**
     * Sets a comment to the tag
     */
    public void setComment(String c) {
        if (comm == null) {
            comm = new FrameCOMM();
            tag.addFrame(comm);
        }
        comm.setText(c);
    }

    /**
     * @return the genre of the song
     */
    public byte getGenre() {
        String genreS = getGenreS();
        byte genre = -1;
        if (!genreS.equals("")) {
            if (genreS.startsWith("("))
                genre = Byte.parseByte(genreS.substring(1, genreS
                        .indexOf(")")));
            else
                for(int i = 0; i < ID3Tag.genres.length; i++) {
                    if (ID3Tag.genres[i].equalsIgnoreCase(genreS)) {
                        genre = (byte) i;
                        break;
                    }
                }
        }
        return genre;
    }

    /**
     * @return String representation of the genre
     */
    public String getGenreS() {
        if (tcon != null)
            return tcon.getText();
        return "";
    }

    /**
     * Sets the genre of the song
     */
    public void setGenre(byte g) {
        if (g >= 0 && g < 126)
            setGenre("(" + g + ")" + ID3Tag.genres[g]);
        else
            setGenre("");
    }

    public void setGenre(int g) {
        setGenre((byte) g);
    }

    public void setGenre(String s) {
        if (tcon == null) {
            tcon = new FrameTCON();
            tag.addFrame(tcon);
        }
        tcon.setText(s);
    }

    /**
     * @return Number of the Track on the album
     */
    public byte getTrack() {
        if (trck != null)
            return (byte) Integer.parseInt(trck.getText());
        return 0;
    }

    /**
     * @return String representation of the number of the track
     */
    public String getTrackS() {
        if (trck != null)
            return trck.getText();
        return "0";
    }

    /**
     * sets the number of the track
     */
    public void setTrack(byte t) {
        setTrack((int) t);
    }

    /**
     * sets the number of the track
     */
    public void setTrack(int t) {
        setTrack("" + t);
    }

    /**
     * Sets the number of the track
     */
    public void setTrack(String t) {
        if (trck == null) {
            trck = new FrameTRCK();
            tag.addFrame(trck);
        }
        trck.setText(t);
    }

    /**
     * Sets the composer of the track
     */
    public void setComposer(String s) {
        if (tcom == null) {
            tcom = new FrameTCOM();
            tag.addFrame(tcom);
        }
        tcom.setText(s);
    }

    /**
     * @return the composer of the track
     */
    public String getComposer() {
        if (tcom != null)
            return tcom.getText();
        return "";
    }

    /**
     * Sets the original artist of this track
     */
    public void setOriginalArtist(String s) {
        if (tope == null) {
            tope = new FrameTOPE();
            tag.addFrame(tope);
        }
        tope.setText(s);
    }

    /**
     * @return the original artist of this track
     */
    public String getOriginalArtist() {
        if (tope != null)
            return tope.getText();
        return "";
    }

    /**
     * Sets the copyright owner of this track
     */
    public void setCopyright(String s) {
        if (tcop == null) {
            tcop = new FrameTCOP();
            tag.addFrame(tcop);
        }
        tcop.setText(s);
    }

    /**
     * return the copyright owner of this track
     */
    public String getCopyright() {
        if (tcop != null)
            return tcop.getText();
        return "";
    }

    /**
     * Sets the URL for this track
     */
    public void setURL(String s) {
        if (wxxx == null) {
            wxxx = new FrameWXXX();
            tag.addFrame(wxxx);
        }
        wxxx.setValue(s);
    }

    /**
     * @return the URL of this track
     */
    public String getURL() {
        if (wxxx != null)
            return wxxx.getValue();
        return "";
    }

    /**
     * Sets the encoder of this track
     */
    public void setEncodedBy(String s) {
        if (tenc == null) {
            tenc = new FrameTENC();
            tag.addFrame(tenc);
        }
        tenc.setText(s);
    }

    /**
     * @return the encoder of this track
     */
    public String getEncodedBy() {
        if (tenc != null)
            return tenc.getText();
        return "";
    }

    /**
     * Creates an info string
     *
     * @return a String containing the information edited here
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Title:        ").append(getTitle()).append(
                "\nArtist:       ").append(getArtist()).append(
                "\nAlbum:        ").append(getAlbum()).append(
                "\nYear:         ").append(getYear()).append(
                "\nGenre:        ").append(getGenreS()).append(
                "\nComment:      ").append(getComment()).append(
                "\nComposer:     ").append(getComposer()).append(
                "\nOrig. Artist: ").append(getOriginalArtist()).append(
                "\nCopyright:    ").append(getCopyright()).append(
                "\nURL:          ").append(getURL()).append(
                "\nEncoded by:   ").append(getEncodedBy()).append("\n");
        return sb.toString();
    }
}
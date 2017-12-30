package de.ueberdosis.mp3info;

import de.ueberdosis.util.OutputCtr;

import java.io.UnsupportedEncodingException;

/**
 * Contains the information a ID3 V1 tag has
 *
 * @author Florian Heer <heer@ueberdosis.de>
 * @version $Id: ID3Tag.java,v 1.10 2005/02/22 18:03:58 heer Exp $
 */

/*
 * Patch-List: - added isValidTag meaning this tag was not generated from an
 * invalid Byte-Array.
 */
public class ID3Tag implements Defines {
    /**
     * the title contained in the tag. Maximum is 30 characters
     */
    private String title; // Max 30

    /**
     * the artist contained in the tag. Maximum is 30 characters
     */
    private String artist; // max 30

    /**
     * the album contained in the tag. Maximum is 30 characters
     */
    private String album; // max 30

    /**
     * the year contained in the tag. Maximum is 4 characters
     */
    private String year; // max 4

    /**
     * the comment contained in the tag. Maximum is 29 characters
     */
    private String comment; // max 30

    /**
     * the genre contained in the tag. Is the index of genres
     */
    private byte genre; // 0-125

    /**
     * the track number contained in the tag. If it is 32 there is a high
     * possibility that the tag writing software just padded the comment field
     * with spaces instead of zeroes.
     */
    private byte track;

    /**
     * is this a valid tag?
     */
    private boolean validTag = false;

    /**
     * String representation of all curently used genres
     */
    public static final String[] genres = {"Blues", "Classic Rock", "Country",
            "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal",
            "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae",
            "Rock", "Techno", "Industrial", "Alternative", "Ska",
            "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
            "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
            "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
            "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
            "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
            "Gothic", "Darkwave", "Techno-Industrial", "Electronic",
            "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
            "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
            "Native American", "Cabaret", "New Wave", "Psychedelic", "Rave",
            "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
            "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
            "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
            "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
            "Avantgarde", "Gothic Rock", "Progressive Rock",
            "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
            "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
            "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
            "Booty Brass", "Primus", "Porn Groove", "Satire", "Slow Jam",
            "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
            "Rhytmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
            "Acapella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass",
            "Club-House", "Hardcore", "Terror", "Indie", "BritPop",
            "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap",
            "Heavy Metal", "Black Metal", "Crossover",
            "Contemporary Christian", "Christian Rock", "Merengue", "Salsa",
            "Thrash Metal", "Anime", "Jpop", "Synthpop", "unknown"};

    /**
     * Creates an instance basedon the data read from the end of an mp3-file.
     *
     * @param tagBytes is an Array of 128 bytes containing the proposed ID3Tag.
     */
    public ID3Tag(byte[] tagBytes) {
        this();
        try {
            if (((char) tagBytes[0] == 'T') && ((char) tagBytes[1] == 'A')
                    && ((char) tagBytes[2] == 'G')) {
                setTitle(new String(tagBytes, 3, 30,
                        Configuration.defaultEncoding));
                setArtist(new String(tagBytes, 33, 30,
                        Configuration.defaultEncoding));
                setAlbum(new String(tagBytes, 63, 30,
                        Configuration.defaultEncoding));
                setYear(new String(tagBytes, 93, 4,
                        Configuration.defaultEncoding));
                setComment(new String(tagBytes, 97, 28,
                        Configuration.defaultEncoding));
                setTrack(tagBytes[126]);
                setGenre(tagBytes[127]);

                validTag = true;
            } else
                validTag = false;
        } catch (UnsupportedEncodingException ex) {
            OutputCtr.println(0, "Error in the encoding!");
        }
    }

    /**
     * creates a new, empty V1 tag
     */
    public ID3Tag() {
        setTitle("");
        setArtist("");
        setAlbum("");
        setYear("");
        setComment("");
        setGenre((byte) 126);
        setTrack((byte) 0);
        validTag = true;
    }

    /**
     * Creates a copy of an existing tag
     */
    public ID3Tag(ID3Tag tag) {
        setTitle(tag.getTitle());
        setArtist(tag.getArtist());
        setAlbum(tag.getAlbum());
        setYear(tag.getYear());
        setComment(tag.getComment());
        setGenre(tag.getGenre());
        setTrack(tag.getTrack());
        validTag = true;
    }

    /**
     * creates a byte array from a string and fills the rest with zeroes.
     *
     * @param txt the text that should be included in the result
     * @param num the size of the resulting array.
     * @return the content of txt as a byte array padded with 0s
     */
    private static byte[] getPaddedByteArray(String txt, int num) {
        OutputCtr.println(2, "Padding \"" + txt + "\" to " + num);

        byte[] retval, t1;
        try {
            t1 = txt.trim().getBytes(Configuration.defaultEncoding); // Padding
            // should
            // always
            // be done
            // with 0x00, not whitespace
        } catch (UnsupportedEncodingException ex) {
            OutputCtr.println(0, "System doesn't understand \""
                    + Configuration.defaultEncoding + "\" ?");
            t1 = txt.trim().getBytes();
        }

        retval = new byte[num];
        // ***** SEHR h‰ﬂliche Bruteforce-Methode!
        if (txt.length() != 0) {
            if (t1.length < num) {
                for(int i = 0; i < t1.length; i++)
                    retval[i] = t1[i];
                for(int j = t1.length; j < num; j++)
                    retval[j] = 0;
            } else {
                for(int i = 0; i < num; i++)
                    retval[i] = t1[i];
            }
        } else {
            for(int i = 0; i < num; i++)
                retval[i] = 0;
        }
        return retval;
    }

    /**
     * Truncates a string
     *
     * @param txt the String to truncate
     * @param num the maximum number of characters contained in the result
     * @return the original String, if it was smaller than &lt;num&gt; chars or
     *         the first &lt;num&gt; characters of the original.
     */
    private String truncate(String txt, int num) {
        String retval;
        retval = (txt.length() > num) ? txt.substring(0, num - 1) : txt;
        return retval;
    }

    /**
     * @return Title of the mp3
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the mp3
     */
    public void setTitle(String tit) {
        title = truncate(tit.trim(), 30);
    }

    /**
     * @return Artist of the mp3
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist of the mp3
     */
    public void setArtist(String art) {
        artist = truncate(art.trim(), 30);
    }

    /**
     * @return Name of the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets the name of the album
     */
    public void setAlbum(String alb) {
        album = truncate(alb.trim(), 30);
    }

    /**
     * @return release-year of the original song
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the release-year of the original song
     */
    public void setYear(String y) {
        year = truncate(y.trim(), 4);
    }

    /**
     * @return Comment included in the tag
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets a comment to the tag
     */
    public void setComment(String c) {
        comment = truncate(c.trim(), 30);
    }

    /**
     * @return the genre of the song
     */
    public byte getGenre() {
        return genre;
    }

    /**
     * @return String representation of the genre
     */
    public String getGenreS() {
        if (genre >= genres.length)
            return UNKNOWN_GENRE;
        return genres[genre];
    }

    /**
     * Sets the genre of the song
     */
    public void setGenre(byte g) {
        genre = g;
    }

    /**
     * Sets the genre of the song
     */
    public void setGenre(int g) {
        setGenre((byte) g);
    }

    /**
     * @return Number of the on the album
     */
    public byte getTrack() {
        return track;
    }

    /**
     * @return String representation of the number of the track
     */
    public String getTrackS() {
        return new StringBuffer().append(track & 0xff).toString();
    }

    /**
     * Sets the number of the track
     */
    public void setTrack(byte t) {
        track = t;
    }

    /**
     * Sets the number of the track
     */
    public void setTrack(int t) {
        setTrack((byte) t);
    }

    /**
     * Gives information if this tag seems to be valid.
     *
     * @return false only if this tag was generated from a ByteArray which was
     *         not a valid tag.
     */
    public boolean isValidTag() {
        return validTag;
    }

    /**
     * creates an ID3-Tag that can directly be written to a file
     *
     * @return 128 bytes containing the information of this tag.
     */
    public byte[] toByteArray() {
        byte[] retval = new byte[128];
        byte[] t;
        int i = 0;
        retval[0] = (byte) 'T';
        retval[1] = (byte) 'A';
        retval[2] = (byte) 'G';
        t = getPaddedByteArray(getTitle(), 30);
        for(i = 0; i < 30; i++)
            retval[i + 3] = t[i];
        t = getPaddedByteArray(getArtist(), 30);
        for(i = 0; i < 30; i++)
            retval[i + 33] = t[i];
        t = getPaddedByteArray(getAlbum(), 30);
        for(i = 0; i < 30; i++)
            retval[i + 63] = t[i];
        t = getPaddedByteArray(getYear(), 4);
        for(i = 0; i < 4; i++)
            retval[i + 93] = t[i];
        t = getPaddedByteArray(getComment(), 29);
        for(i = 0; i < 29; i++)
            retval[i + 97] = t[i];
        retval[126] = getTrack();
        retval[127] = getGenre();
        return retval;
    }
}
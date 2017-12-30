package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Configuration;
import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.mp3info.gui.EncDescValJPanel;
import de.ueberdosis.mp3info.gui.Id3JPanel;
import de.ueberdosis.util.OutputCtr;

/**
 * Comments frame This frame is indended for any kind of full text information
 * that does not fit in any other frame. It consists of a frame header followed
 * by encoding, language and content descriptors and is ended with the actual
 * comment as a text string. Newline characters are allowed in the comment text
 * string. There may be more than one comment frame in each tag, but only one
 * with the same language and content descriptor.
 * <p>
 * <Header for 'Comment', ID: "COMM"><br> 
 * Text encoding $xx <br>
 * Language $xx xx xx <br>
 * Short content descrip. <text string according to encoding> $00 (00)<br> 
 * The actual text &lt;full text string according to encoding&gt;
 * 
 * @author Florian Heer
 * @version $Id: FrameCOMM.java,v 1.14 2005/02/24 21:25:21 heer Exp $
 */

public class FrameCOMM extends ID3V2Frame implements EncDescValFrame {
    private byte encoding = 0;

    private String language = "eng";

    private String description = "";

    private String text = "";

    private static final String caption1 = "Comment description";

    private static final String caption2 = "Comment";

    public String getLongName () {
        return "Comments frame";
    }

    public FrameCOMM () {
        super ("COMM");
    }

    public FrameCOMM (ID3V2Frame frame) {
        super (frame);
        if (frame instanceof FrameCOMM) {
            FrameCOMM frm = (FrameCOMM) frame;
            encoding = frm.encoding;
            language = frm.language;
            description = frm.description;
            text = frm.text;
        }
    }

public FrameCOMM (ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super (frm);
        // Decoding our information from the DataSource!
        try {
            encoding = ds.getByte ();
            // FIXME is a language string always ASCII?
            language = new String (ds.getBytes (3));
            description = ds.readString(encoding);
            text = ds.readString (encoding);
        } catch (SeekPastEndException ex) {
            OutputCtr.println (0, "Text frame can't be instantiated! SPEEx!");
            throw ex;
        } catch (DatamismatchException dmex) {
            OutputCtr.println (0, "Encountered an illegal encoding " + encoding);
        }

        // Setting language to a 'sane' something
        if (!Languages.checkLanguage (language))
            language = Configuration.defaultLanguage;
    }    

    public String toString () {
        return getLongName () + " Encoding: " + encoding + "\nlanguage: "
                + language + "\ndescription: " + description + "\ntext: "
                + text;
    }

    public boolean containsData () {
        return (description.length () > 0 || text.length () > 0);
    }

    public byte[] getData () {
        // The order is quite important. The Strings have to be converted
        // before their size in bytes is known.
        byte [] descAr = Helper.stringToByteArray(description, encoding);
        byte [] textAr = Helper.stringToByteArray(text, encoding);
        
        // The array is 4 bytes (encoding + 3 bytes language)
        // plus the size of the description-array
        // plus the size of the text-array
        // plus one terminator according to encoding (one or two zeroes)
        int stringDelimiterSize = Helper.getStringDelimiterSize(encoding);
        byte[] data = new byte[4 + descAr.length 
                               + stringDelimiterSize
                               + textAr.length];
        data[0] = encoding;
        data[1] = (byte) language.charAt (0);
        data[2] = (byte) language.charAt (1);
        data[3] = (byte) language.charAt (2);

        System.arraycopy (descAr, 0, data, 4, descAr.length);
        for (int i = 4 + descAr.length; 
             i < 4 + descAr.length + stringDelimiterSize;
             i++)
            data[i] = 0;
        System.arraycopy (textAr, 0, data, 4 + descAr.length + stringDelimiterSize, textAr.length);
        return data;
    }

    public boolean canEdit () {
        return true;
    }

    public boolean canDisplay () {
        return true;
    }

    public Id3JPanel createJPanel (boolean edit, boolean selfupdate) {
        return new EncDescValJPanel (edit, selfupdate, caption1, description,
                caption2, text, this);
    }

    public void setDescription (String txt) {
        if (!txt.equals (description))
            dataChanged = true;
        description = txt.trim ();
    }

    public void setValue (String txt) {
        setText (txt);
    }

    public String getValue () {
        return getText ();
    }

    /** Sets the content of the comment, not its description
     */
    public void setText (String t) {
        if (!t.equals (text)) {
            text = t.trim ();
            dataChanged = true;
        }
    }

    /** @return the comment, not its description */
    public String getText () {
        return text;
    }
}
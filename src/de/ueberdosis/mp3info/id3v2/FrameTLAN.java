package de.ueberdosis.mp3info.id3v2;

/** Language Frame.
 * The 'Language(s)' frame should contain the languages of the text or
 * lyrics spoken or sung in the audio. The language is represented with
 * three characters according to ISO-639-2. If more than one language
 * is used in the text their language codes should follow according to
 * their usage.
 *
 * @author Florian Heer
 * @version $Id: FrameTLAN.java,v 1.5 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTLAN extends FrameT {
    public String getLongName ()  { return "Language frame"; }

    public FrameTLAN (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTLAN (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nLanguage : "
	    + text;
    }

}

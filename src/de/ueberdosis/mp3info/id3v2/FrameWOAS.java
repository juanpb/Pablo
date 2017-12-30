package de.ueberdosis.mp3info.id3v2;

/** Oficial audio source url frame
 * The 'Official audio source webpage' frame is a URL pointing at the
 * official webpage for the source of the audio file, e.g. a movie.
 *
 * @author Florian Heer
 * @version $Id: FrameWOAS.java,v 1.4 2003/10/22 23:51:02 heer Exp $
 */
public class FrameWOAS extends FrameW {
    public String getLongName ()  { return "Official audio source URL frame"; }

    public FrameWOAS (ID3V2Frame frm) {
        super (frm);
    }

    public FrameWOAS (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + "\nURL : "
	    + url;
    }

}

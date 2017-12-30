package de.ueberdosis.mp3info.id3v2;


/** Title 1 Frame.
 * The 'Content group description' frame is used if the sound belongs to a
 * larger category of sounds/music. For example, classical music is often
 * sorted in different musical sections (e.g. "Piano Concerto",
 * "Weather - Hurricane").
 *
 * @author Florian Heer
 * @version $Id: FrameTIT1.java,v 1.6 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTIT1 extends FrameT {
    public String getLongName ()  { return "Title 1 frame"; }

    public FrameTIT1 (ID3V2Frame frm) {
        super (frm);
    }

    public FrameTIT1 (ID3V2Frame frm, DataSource ds)
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nTitle 1 : "
	    + text;
    }

}

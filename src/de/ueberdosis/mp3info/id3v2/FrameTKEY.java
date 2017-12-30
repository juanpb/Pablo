package de.ueberdosis.mp3info.id3v2;

/** Initial key Frame.
 * The 'Initial key' frame contains the musical key in which the sound 
 * starts. It is represented as a string with a maximum length of three 
 * characters. The ground keys are represented with "A","B","C","D","E", 
 * "F" and "G" and halfkeys represented with "b" and "#". Minor is 
 * represented as "m". Example "Cbm". Off key is represented with an "o" only.
 *
 * @author Florian Heer
 * @version $Id: FrameTKEY.java,v 1.4 2003/10/22 23:51:02 heer Exp $
 */
public class FrameTKEY extends FrameT {
    public String getLongName ()  { return "Initial key frame"; }

    public FrameTKEY (ID3V2Frame frm, DataSource ds) 
	throws SeekPastEndException {
	super (frm, ds);
    }

    public String toString () {
	return getLongName () + " Encoding: "+encoding+"\nInitial Key : " 
	    + text;
    }

}

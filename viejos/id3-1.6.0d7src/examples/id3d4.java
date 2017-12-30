package examples;

import de.ueberdosis.mp3info.*;
import de.ueberdosis.util.OutputCtr;
import de.ueberdosis.mp3info.id3v2.*;

import java.io.*;


/** Reads a whole directory and writes all information it encounters to a file
*/

public class id3d4 implements Defines {
    private static boolean frameClosed = false;

    public static void main (String args[]) {
        OutputCtr.println (0, "Mp3 Id ("+ VERSION + ") and bitrate reader.\n"+
                            "Milon Krejca <milon@iname.com>, " +
                            "Florian Heer <heer@ueberdosis.de>(c) 2001");
        if (args.length != 1)
            OutputCtr.println (0, "Usage: ID3Reader <filename>");
        else {
            try {
		// Sets the level of output!
		OutputCtr.setLevel (0);
		// Redirects the output to a file
		File o = new File ("id3d4-results.txt");
		FileOutputStream fos = new FileOutputStream (o);
		OutputCtr.setStream (new PrintStream (fos));
		File f = new File (args[0]);
		if (f.isDirectory ())
		    descendDirectory (f);
            }
            catch(Exception e) {
                OutputCtr.println(0, e);
            }
	    System.exit (0);
        }
    }

    private static void descendDirectory (File f) throws IOException {
	File [] far = f.listFiles (new FileFilter () {
		public boolean accept (File fi) {
		    if (fi.isDirectory ()) return true;
		    String sub = fi.getName().substring (fi.getName().length()-3);
		    if (sub.equalsIgnoreCase ("mp3")) return true;
		    return false;
		}});
	for (int i=0; i<far.length;i++) {
	    System.out.println ("Processing "+far[i].getAbsolutePath());
	    OutputCtr.println (0,"\n\n     -----   "+far[i].getAbsolutePath()+"\n");
	    if (far[i].isDirectory ())
		descendDirectory (far[i]);
	    else processFile (far[i]);
	}

    }

    private static void processFile (File f) throws IOException {
	ID3Reader x = new ID3Reader (f.getAbsolutePath());
	ExtendedID3Tag xtag = null;
	xtag = x.getExtendedID3Tag ();
	OutputCtr.println (0, xtag);
	ID3V2Tag v2tag = ID3Reader.getV2Tag ();
	if (v2tag != null)
	    OutputCtr.println (0, v2tag);
    }

}

package examples;

import de.ueberdosis.mp3info.*;
import de.ueberdosis.util.OutputCtr;
import de.ueberdosis.mp3info.id3v2.*;

import java.io.*;

/** Just maximum debug
*/

public class id3d5 implements Defines {
    private static boolean frameClosed = false;

    public static void main (String args[]) {
        OutputCtr.println (0, "Mp3 Id ("+ VERSION + ") and bitrate reader.\n"+
                            "Milon Krejca <milon@iname.com>, " +
                            "Florian Heer <heer@ueberdosis.de>(c) 2001");
        if (args.length != 1)
            OutputCtr.println (0, "Usage: ID3Reader <filename>");
        else {
	    ExtendedID3Tag xtag = null;
	    // Sets the level of output!
	    OutputCtr.setLevel (10);
            try {
		// Let ID3Reader do its thing...
                ID3Reader x = new ID3Reader (args[0]);
		xtag = x.getExtendedID3Tag ();
                OutputCtr.println (0, xtag);

		// If we have a V2 tag we can get into the nicer things...
		ID3V2Tag v2tag = ID3Reader.getV2Tag ();
		if (v2tag != null) {
		    OutputCtr.println (3, v2tag);
		}

            }
            catch(IOException e) {
                OutputCtr.println(0, e);
            }
	    System.exit (0);
        }
    }

}

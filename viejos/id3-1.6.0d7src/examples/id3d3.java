package examples;

import de.ueberdosis.mp3info.*;
import de.ueberdosis.util.OutputCtr;
import de.ueberdosis.mp3info.id3v2.*;
import de.ueberdosis.mp3info.facades.Wamp;

import java.io.*;

/** Reads a V1 tag from a file, copies the contents into a V2 tag and writes this back to the file.
*/

public class id3d3 implements Defines {
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
                ID3Reader x = new ID3Reader (args[0]);
		xtag = x.getExtendedID3Tag ();
                OutputCtr.println (0, xtag);
		OutputCtr.println (2, "Copying V1 tag to V2 tag..");
		Wamp wa = new Wamp (xtag);
		ID3V2Tag v2tag = wa.getV2Tag ();
		OutputCtr.println (3, v2tag);
		OutputCtr.println (2, "Writing tag to "+args[0]);
		ID3V2Writer.writeTag (new File (args[0]), v2tag);
            }
            catch(IOException e) {
                OutputCtr.println(0, e);
            }
	    System.exit (0);
        }
    }

}

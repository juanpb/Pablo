/*
 * ID3Writer.java
 *
 * This sourcecode is part of the mp3info package (http://www.ueberdosis.de/java/id3.html)
 * and free under the limitations of the LGPL.
 * Any code based on this source must bear a reference to the original implementation.
 *
 * Created on 11. November 2000, 19:51
 * @author Florian Heer <heer@ueberdosis.de>
 * @version $Id: ID3Writer.java,v 1.4 2004/11/28 21:20:44 heer Exp $
 */

package de.ueberdosis.mp3info;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * API for writing an ID3Tag to the specified file.
 *
 * @author heer
 * @version $Id: ID3Writer.java,v 1.4 2004/11/28 21:20:44 heer Exp $
 */
public class ID3Writer extends Object {

    /**
     * writes a new tag to the specified File.
     * If a tag already existited, it will be - adhering to standards -
     * replaced.
     */
    public static ID3Tag writeTag(RandomAccessFile in, ID3Tag tag)
            throws IOException {
        // Check if the file is more than 128 bytes in size. Else we get
        // a "IOException: negative seek offset"
        if (in.length() >= 128) {
            in.seek(in.length() - 128);
            byte[] tagBytes = new byte[128];
            in.read(tagBytes);
            ID3Tag newTag = new ID3Tag(tagBytes);
            if (newTag.isValidTag()) in.seek(in.length() - 128);
            else in.seek(in.length());
        } else in.seek(in.length());

        in.write(tag.toByteArray());

        return tag;
    }

}

package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.mp3info.UndersizedException;
import de.ueberdosis.util.OutputCtr;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The API for writing an ID3V2 tag to a file.
 */
// FIXME! See description.
public class ID3V2Writer implements Defines {

    /**
     * Writes a tag to a file.
     * If the new tag has a different size than the old one, or the file currently has no tag, copyWrite is called.
     * If the sizes (including padding) don't differ, the file is updated
     *
     * @param f   the file to write to
     * @param tag the tag to be written.
     */
    public static void writeTag(File f, ID3V2Tag tag) throws IOException {
        if (tag == null) {
            OutputCtr.println(0, "Can't write null-Tag!");
            return;
        }

        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        // First we need to make sure the file starts with a tag
        byte[] b = new byte[3];
        raf.seek(0);
        raf.read(b);
        if (new String(b).equalsIgnoreCase("ID3")) {
            try {
                // Okay, the file does seem to start with a v2 tag
                raf.seek(0);
                // Let's decode the tag
                b = new byte[ID3V2_HEADER_SIZE];
                raf.read(b);
                Header head = new Header(b);
                long originalSize = head.getSize() + ID3V2_HEADER_SIZE;

                if (tag.getEffectiveSize() <= originalSize) {
                    // Okay, I think we can write directly :-)
                    b = tag.toByteArray();
                    if (b.length <= originalSize) {
                        raf.seek(0);
                        raf.write(b);
                    } else {
                        // Can't write directly
                        copyWrite(raf, f, tag, originalSize);
                    }
                } else {
                    copyWrite(raf, f, tag, originalSize);
                }
            } catch (UndersizedException ex) {
                ex.printStackTrace();
            }
        } else {
            copyWrite(raf, f, tag, 0);
        }
        raf.close();
    }

    /**
     * Writes a new tag to a file
     * A temporary file is created, the tag gets written into it and the audio-part will be copied.
     * This code only works for v2 tags existing at the beginning of the file. If the original tag is
     * anywhere else, the code breaks the file.
     *
     * @param raf          the file to write to
     * @param f            the original file
     * @param tag          the tag to write
     * @param originalsize the size of the existing v2 tag.
     */
    private static void copyWrite(RandomAccessFile raf, File f,
                                  ID3V2Tag tag, long originalsize)
            throws IOException {
        // first we open a temporary file
        File directory = getParentFile(f);
        OutputCtr.println(3, "Path: " + directory);

        File tempFile = createTempFile("mp3info_", ".mp3", directory);
        RandomAccessFile raf2 = new RandomAccessFile(tempFile, "rw");
        raf2.seek(0);
        byte[] b = tag.toByteArray();
        OutputCtr.println(2, "tagsize: " + tag.getEffectiveSize() + " arraysize: " + b.length);
        raf2.write(b);
        raf.seek(originalsize);
        byte[] buffer = new byte[16384];
        int bytesRead = raf.read(buffer);
        while (bytesRead > -1) {
            raf2.write(buffer, 0, bytesRead);
            bytesRead = raf.read(buffer);
        }

        raf2.close();
        raf2 = null;
        raf.close();
        String name = f.getPath();
        f.delete();
        tempFile.renameTo(new File(name));

    }

    /**
     * Same as File.getParentFile() in JDK1.3.
     *
     * @requires the File parameter must be a File object constructed
     * with the canonical path.
     */
    private static File getParentFile(File f) {
        // slight changes to get around getParent bug on Mac
        String name = f.getParent();
        if (name == null) return null;
        try {
            return getCanonicalFile(new File(name));
        } catch (IOException ioe) {
            //if the exception occurs, simply return null
            return null;
        }
    }


    /**
     * Same as File.getCanonicalFile() in JDK1.3.
     */
    private static File getCanonicalFile(File f) throws IOException {
        return new File(f.getCanonicalPath());
    }


    /**
     * We don't have JDK1.3 createTempFile(), we'll just do it here....
     */
    private static File createTempFile(String prefix, String suffix,
                                       File directory) throws IOException {
        String newFileName = "";
        for(int i = 0; i < 1000; i++) {
            newFileName = prefix + i + suffix;
            File newFile = new File(directory, newFileName);
            if (newFile.exists()) continue;
            return newFile;
        }
        throw new IOException("Couldn't create file \""
                + directory
                + newFileName + "\"!");
    }

}

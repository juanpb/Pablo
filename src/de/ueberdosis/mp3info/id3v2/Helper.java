package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Configuration;
import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.mp3info.id3v2.datatypes.TimeCode;
import de.ueberdosis.util.OutputCtr;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

/** Provides some functionality that is necessary for decoding ID3v2 
 *
 * @author Florian Heer &lt;heer@ueberdosis.de&gt;
 * @version $Id: Helper.java,v 1.21 2005/03/06 14:09:15 heer Exp $
 */

public class Helper implements Defines {

    /**
     * creates a long from a number of synchsafe bytes. A synchsafe byte has its
     * highest bit zeroed. In accordance to the ID3V2-standard the byte-order is
     * MSB first.
     * 
     * @param b
     *            any number of bytes together denoting a number not bigger than
     *            can be stored in a long.
     * @return the integer contained in the byte-array (or an erroneous number
     *         if too many bytes were supplied).
     */
    public static long unsynchsafe (byte[] b) {
        long result = 0;
        // to clarify, a temp var
        long temp = 0;
        // to clarify, a position marker. This denotes the significance of the
        // currently processed byte.
        long pos = 0;
        for (long i = (b.length - 1); i >= 0; i--) {
            temp = (b[(int) i] & 0xff);
            result += (temp * (pow (2, pos * 7)));
            pos++;
        }
        return result;
    }

    /**
     * creates a number of synchsafe bytes from a long
     * 
     * @param value
     *            a value.. Sensibly one that fits into the given number of
     *            bytes
     * @param length
     *            number of bytes to create.
     * @return the array containing the snychsafed value
     */
    public static byte[] synchsafe (long value, int length) {
        byte[] b = new byte[length];
        int j = 0;
        for (int i = length - 1; i >= 0; i--) {
            b[i] = (byte) ((value >> (j * 7)) & 0x7f);
            j++;
        }
        return b;
    }

    /**
     * Probably bullshit, but I was quicker writing this POW function than
     * finding it in the JDK documentation
     */
    public static long pow (long number, long exp) {
        long result = 1;
        for (long i = 1; i <= exp; i++)
            result *= number;
        return result;
    }

    public static int decodeBytesToInt (byte[] bAr) {
        int retval = 0;
        int temp = 0;
        int pos = 0;
        for (int i = 0; i < bAr.length; i++) {
            temp = (bAr[i] & 0xff);
            pos = (bAr.length - 1) - i;
            retval += (temp * (pow (2, pos * 8)));
        }
        return retval;
    }

    /**
     * converts a byte array to a Hex string. With a line by Andreas Mandel at
     * sourceforge (thanks!).
     */
    public static String arrayToHexString (byte[] bAr) {
        StringBuffer sb = new StringBuffer ().append ("0x");
        for (int i = bAr.length; i > 0; i--)
            // the charAt-idea by Andreas Mandel
            sb.append ("0123456789ABCDEF".charAt (0xf & bAr[i - 1] >> 4))
                    .append ("0123456789ABCDEF".charAt (bAr[i - 1] & 0xF));

        return sb.toString ();
    }

    /** converts a byte to a Hex string */
    public static String byteToHexString (byte b) {
        return "0x" + "0123456789ABCDEF".charAt (0x0f & (b >> 4))
                + "0123456789ABCDEF".charAt (b & 0x0f);
    }

    /**
     * Converts an unsigned int-wrapped byte to a Hex String i.e. the int must
     * be between 0 and 255. if the int is out of range, an error string is
     * returned
     */
    public static String byteToHexString (int i) {
        if (i >= 0 && i <= 255)
            return byteToHexString (unsignedIntToByte (i));
        return "Can't convert " + i + " to byte!";
    }

    /**
     * converts a byte to an unsigned int
     * 
     * @deprecated
     */
    public static int byteToUnsignedInt (byte b) {
        return (b & 0xff);
    }

    /**
     * converts an unsigned int (between 0 and 255) to a byte any value out of
     * range will result in -128!!!
     */
    public static byte unsignedIntToByte (int i) {
        if (i >= 0 && i < 128)
            return (byte) i;
        if (i >= 128 && i < 255)
            return (byte) (i - 256);
        return (byte) -128;
    }

    /** Just a convenience method, works the same as reallocateByteBuffer (int, null);
     * @param size the size the resulting array should have.
     * @return an array of bytes of the size requested.
     */
    public static byte[] reallocateByteBuffer (int size) {
        return reallocateByteBuffer (size, null);
    }

    /**
     * creates a new byte array as buffer
     * 
     * @param size
     *            number of bytes the new buffer should be bigger than the old
     * @param oldBuffer
     *            old Buffer
     * @return new Buffer with size = size + oldBuffer.length <br>
     *         null if an error occured;
     */
    public static byte[] reallocateByteBuffer (int size, byte[] oldBuffer) {
        byte[] newBuffer = null;
        int newSize = 0;
        if (oldBuffer != null)
            newSize += oldBuffer.length;

        newSize += size;

        newBuffer = new byte[newSize];
        if (oldBuffer != null) {
            System.arraycopy (oldBuffer, 0, newBuffer, 0, oldBuffer.length);
        }
        return newBuffer;
    }

    /**
     * Decodes the encoding of strings according to id3v2-standard.
     * 
     * @param encByte
     *            byte-representation of the encoding
     * @return string-representation of the encoding.
     */
    public static String getStringEncoding (byte encByte) {
        String enc = "";
        switch (encByte) {
        case ENCODING_BYTE_ISO88591:
            enc = ENCODING_STRING_ISO88591;
            break;
        case ENCODING_BYTE_UTF16:
            enc = ENCODING_STRING_UTF16;
            break;
        case ENCODING_BYTE_UTF16BE:
            enc = ENCODING_STRING_UTF16BE;
            break;
        case ENCODING_BYTE_UTF8:
            enc = ENCODING_STRING_UTF8;
            break;
        }
        return enc;
    }

    /**
     * Validates an encoding
     * 
     * @param encoding
     *            a number from 0 to 3 (as of now)
     * @return true if the encoding is a valid one.
     */
    public static boolean isValidEncoding (byte encoding) {
        return getStringEncoding (encoding) != "";
    }

    /**
     * @param encoding
     *            string representation of the encoding
     * @return byte representation of the encoding, defaults to ISO-8859-1
     */
    public static byte getByteEncoding (String encoding) {
        if (ENCODING_STRING_ISO88591.equals (encoding))
            return ENCODING_BYTE_ISO88591;
        if (ENCODING_STRING_UTF16.equals (encoding))
            return ENCODING_BYTE_UTF16;
        if (ENCODING_STRING_UTF16BE.equals (encoding))
            return ENCODING_BYTE_UTF16BE;
        if (ENCODING_STRING_UTF8.equals (encoding))
            return ENCODING_BYTE_UTF8;

        return ENCODING_BYTE_ISO88591;
    }

    /**
     * Converts an array of bytes into a String, according to the encoding. If
     * the given encoding is invalid, it falls back to the default encoding as
     * set in Configuration. If even that fails, it tries the system default. If
     * the array is empty, a blank String is returned.
     * 
     * @param bAr
     *            contains the data
     * @param encoding
     *            one of the 4 encodings used in ID3V2 tags.
     * @return the data contained in bAr according to the encoding or an empty
     *         string.
     */
    public static String byteArrayToString (byte[] bAr, byte encoding) {
        String text = "";
        // if there are no data, a blank String is returned.
        if (bAr.length <= 0)
            return text;

        try {
            text = new String (bAr, Helper.getStringEncoding (encoding));
        } catch (UnsupportedEncodingException ex) {
            OutputCtr.println (1, "Unsupported Encoding "
                    + Helper.getStringEncoding (encoding) + " (" + encoding
                    + ") encountered, defaulting to"
                    + Configuration.defaultEncoding);
            try {
                text = new String (bAr, Configuration.defaultEncoding);
            } catch (UnsupportedEncodingException uex) {
                OutputCtr.println (1, "Unsupported default encoding "
                        + Configuration.defaultEncoding
                        + " encountered. Using system default.");
                text = new String (bAr);
            }
        }
        return text;
    }

    /**
     * Produces a byte array from a string, according to the encoding.
     * 
     * @param s
     *            the String to convert
     * @param encoding
     *            the encoding to use
     * @return either the given data as a byte array or an empty byte array.
     */
    public static byte[] stringToByteArray (String s, byte encoding) {
        try {
            return s.getBytes (Helper.getStringEncoding (encoding));
        } catch (UnsupportedEncodingException ex) {
            OutputCtr.println (0, "Unsupported Encoding " + encoding
                    + " encountered.");
            OutputCtr.println (0, ex);
        }
        return new byte[0];
    }

    /**
     * Gives you the number of zeroes a String needs as a terminator.
     * 
     * @param encoding
     *            the used encoding as allowed in ID3V2
     * @return the number of zeroes necessary to terminate a string or 1 if the
     *         encoding is unknown.
     */
    public static int getStringDelimiterSize (byte encoding) {
        switch (encoding) {
        case ENCODING_BYTE_ISO88591:
            return 1;
        case ENCODING_BYTE_UTF16:
            return 2;
        case ENCODING_BYTE_UTF16BE:
            return 2;
        case ENCODING_BYTE_UTF8:
            return 1;
        default:
            if (isValidEncoding (getByteEncoding (Configuration.defaultEncoding)))
                return getStringDelimiterSize (getByteEncoding (Configuration.defaultEncoding));

            OutputCtr.println (0, "Unsupported encoding " + encoding
                    + " encountered.");
        }
        return 1;
    }

    /** creates a byte array from a string, according to encoding.
     * If the encoding is unsupported, the default encoding is tried.
     * @param s the source data
     * @param encoding an encoding according to ID3v2 standards
     * @return a byte array containing the data contained in the source.
     */
    public static byte[] getStringBytes (String s, byte encoding) {
        byte[] bAr = null;
        try {
            bAr = s.getBytes (getStringEncoding (encoding));
        } catch (UnsupportedEncodingException ex) {
            bAr = s.getBytes ();
        }
        return bAr;
    }

    /** Converts a TimeCode (in datatypes) to a binary form.
     * Used for FrameSYTC.
     * @param tc the TimeCode to convert
     * @return a binary presentation of the TimeCode
     */
    public static byte[] timeCodeToSYTC (TimeCode tc) {
        byte[] bAr = tc.getBpm () > 255 ? reallocateByteBuffer (6)
                : reallocateByteBuffer (5);
        byte[] timeAr = intToByteArray (tc.getTimeStamp (), 4);
        System.arraycopy (timeAr, 0, bAr, 0, 4);
        if (tc.getBpm () > 255) {
            bAr[4] = -127;
            bAr[5] = (byte) (tc.getBpm () - 255);
        } else
            bAr[4] = (byte) (tc.getBpm ());
        return bAr;
    }

    /** Creates a byte array from an int.
     * This is inspired by <a href="http://www.koders.com/java/fid3066AB17DE92DD1BDA6F76DA68DA31331AA9EC5E.aspx">Koders.com</a>s Bytes.java.
     * @param i the integer to convert.
     * @param size the size the resulting array should have.
     * @return the encoded data.
     */
    public static byte[] intToByteArray (int i, int size) {
        byte[] bAr = new byte[size];
        for (int j = size; j > 0; j--) {
            bAr[j - 1] = (byte) i;
            i >>>= 8;
        }
        return bAr;
    }
    
    /** Creates a byte array from an int. The size of the resulting array
     * is minimal as to store the given integer.
     * @param i the integer to convert
     * @return a minimal sized byte array.
     */
    public static byte[] intToByteArray (int i) {
        Vector ve = new Vector ();
        byte b = 0;
        do {
            b = (byte)i;
            ve.add(new Byte(b));
            i >>>= 8;
        } while (i > 0);
        int size = ve.size();
        byte [] bAr = new byte [size];
        for (int j = size; j > 0; j--)
            bAr [j-1] = ((Byte)ve.elementAt(size-j)).byteValue();
        return bAr;
    }

}
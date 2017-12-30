package de.ueberdosis.mp3info;

/**
 * Contains various switches and values that can be changed by developers.
 *
 * @author Florian Heer
 * @version $Id: Configuration.java,v 1.6 2005/02/22 21:17:28 heer Exp $
 */

public class Configuration {

    /**
     * Width of a JLabel in the GUI-elements
     */
    public static int labelWidth = 110;
    /**
     * Height of a JLabel in the GUI-elements
     */
    public static int labelHeight = 21;
    /**
     * Width of a JTextField in the GUI-elements
     */
    public static int textWidth = 230;
    /**
     * Height of a JTextField in the GUI-elements
     */
    public static int textHeight = 21;
    /**
     * Width of a JButton ("OK") in the GUI-elements
     */
    public static int buttonWidth = 60;
    /**
     * Height of a JButton ("OK") in the GUI-elements
     */
    public static int buttonHeight = 25;
    /**
     * Width of a JTextArea in the GUI-elements
     */
    public static int textAreaWidth = 230;
    /**
     * Height of a JTextArea in the GUI-elements
     */
    public static int textAreaHeight = 66;

    /**
     * Switches the behaviour of the ID3V2Writer.
     * If this is set to false (the default), only Frames
     * that contain data are written by the ID3V2Writer
     */
    public static boolean keepAllFrames = false;

    /**
     * The default language for descriptions in Frames.
     * Used if unsane values were found and during instantiation of
     * Frames.
     * You can set it externally by changing the system property
     * "id3.default_language". It defaults to "ENG".
     */
    public static String defaultLanguage = System.getProperty("id3.default_language", "ENG");

    /**
     * The encoding used if not set otherwise
     * You can set it externally by changing the system property "id3.default_encoding" from ISO-8859-1
     * to anything else.
     */
    // FIXME! Why is it 8859-1 and not 8859-15? Any problems with the Euro-sign? Should be changed!
    public static String defaultEncoding = System.getProperty("id3.default_encoding", "ISO-8859-1");


}

package de.ueberdosis.util;

import de.ueberdosis.mp3info.ID3TagException;
import de.ueberdosis.mp3info.ID3V2Exception;

import java.io.PrintStream;

/** cheap output controlling. Decreases the debug output in production 
 * environment. 
 *
 * @author Florian Heer
 * @version $Id: OutputCtr.java,v 1.6 2003/10/24 12:49:48 heer Exp $
*/
public class OutputCtr {
    /** singleton in use.. */
    private static OutputCtr onlyInstance=null;
    /** level of output. */
    private static int level = 20;
    /** The output to use. Defaults to System.out */
    private static PrintStream ps = System.out;

    /** Creates an instance of the output controller with a level of 99 */
    private OutputCtr () {
	this (99);
    }

    /** Creates an instance of the output controller.
     * @param lvl maximum level of output. 0 - 5 is normal, whereas
     *    0 is real standard output, and 5 is VERY much debugging.
     *    more might be used, but never more than 100 :-)
     */
    private OutputCtr (int lvl) {
	level = lvl;
    }

    /** Outputs with a trailing newline
     * @param lvl the level assigned to this output
     * @param o what to output
     */
    public static void println (int lvl, Object o) {
	if (o != null) {
	    if (o instanceof Throwable) {
		if (!((o instanceof ID3V2Exception) 
		      || (o instanceof ID3TagException)))
		    ((Throwable)o).printStackTrace (ps);
	    }
	    else
		print (lvl, o.toString ()+"\n");
	}
	else print (lvl, "null\n");
    }

    /** Does output..
     * @param lvl the level assigned to this output
     * @param o what to output
     */
    public static void print (int lvl, Object o) {
	if (o != null) {
	    if (onlyInstance == null)
		onlyInstance = new OutputCtr ();
	    if (o instanceof Throwable)
		if (!((o instanceof ID3V2Exception) 
		      || (o instanceof ID3TagException)))
		    ((Throwable)o).printStackTrace (ps);

	    onlyInstance.output (lvl, o);
	}
	else onlyInstance.output (lvl, "null");
    }


    /** Sets the debug level */
    public static void setLevel (int lvl) {
	onlyInstance = new OutputCtr (lvl);
    }


    /** Changes the output from System.out 
     * @param s the new output to use
     */
    public static void setStream (PrintStream s) {
	if (onlyInstance == null)
	    onlyInstance = new OutputCtr ();
	onlyInstance.setPrintStream (s);
    }

    /** sets the output to use
     * @param s teh new output to use
     */
    private void setPrintStream (PrintStream s) {
	ps = s;
    }

    /** Writes something to the current output
     * @param lvl the level assigned to this output
     * @param o what to output
     */
    private void output (int lvl, Object o) {
	if (lvl <= level)
	    ps.print (o);
    }
}

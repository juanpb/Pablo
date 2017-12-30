package de.ueberdosis.mp3info.gui;

import java.awt.*;

/** Defines Panels that can be used for displaying or editing V2 frames
 * Currently unused as I have not created any non-swing Components for
 * the frames as yet. 
 *
 * @author Florian Heer
 * @version $Id: Id3Panel.java,v 1.2 2002/12/19 23:04:32 heer Exp $
 */


public abstract class Id3Panel extends Panel {
    /** To be called if the Component does not perform selfupdating */
    public abstract void updateData ();

}

package de.ueberdosis.mp3info.gui;

import de.ueberdosis.mp3info.gui.event.UpdateEvent;
import de.ueberdosis.mp3info.gui.event.UpdateListener;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

//import java.util.Iterator;

/**
 * Defines JPanels that can be used for displaying or editing V2 frames
 *
 * @author Florian Heer
 * @version $Id: Id3JPanel.java,v 1.7 2005/02/22 18:05:17 heer Exp $
 */

public abstract class Id3JPanel extends JPanel {
    /**
     * Contains all UpdateListeners
     */
    private Vector uListeners = null;

    /**
     * Creates a JPanel
     */
    public Id3JPanel() {
        super();
    }

    /**
     * Creates a JPanel
     *
     * @param isDoubleBuffered true if double buffering is to be used
     */
    public Id3JPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
     * Creates a JPanel
     *
     * @param layout the layout to use
     */
    public Id3JPanel(LayoutManager layout) {
        super(layout);
    }

    /**
     * Creates a JPanel
     *
     * @param isDoubleBuffered true if double buffering is to be used
     * @param layout           the layout to use
     */
    public Id3JPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
     * To be called if the Component does not perform selfupdating
     * Should copy the data of its edit field(s) to the edited frame
     * and call fireUpdateEvent to inform the listeners
     */
    public abstract void updateData();

    /**
     * Adds an UpdateListener
     *
     * @param ul the listener to add. It gets informed when either
     *           updateData () is called or (in case of selfupdating) the user has
     *           hit the OK button
     */
    public void addUpdateListener(UpdateListener ul) {
        if (uListeners == null) {
            uListeners = new Vector();
        }
        if (!uListeners.contains(ul))
            uListeners.addElement(ul);
    }

    /**
     * Removes a Listener
     *
     * @param ul the listener to remove.
     */
    public void removeUpdateListener(UpdateListener ul) {
        if (uListeners != null)
            uListeners.removeElement(ul);
    }

    /**
     * Informs all UpdateListeners that data have been updated
     *
     * @param ue information about the update
     */
    protected void fireUpdateEvent(UpdateEvent ue) {
        if (uListeners != null) {
            // Iterator iter = uListeners.iterator ();
            // while (iter.hasNext ())
            //    ((UpdateListener)iter.next ()).dataUpdated (ue);
            Enumeration iter = uListeners.elements();
            while (iter.hasMoreElements())
                ((UpdateListener) iter.nextElement()).dataUpdated(ue);

        }
    }
}

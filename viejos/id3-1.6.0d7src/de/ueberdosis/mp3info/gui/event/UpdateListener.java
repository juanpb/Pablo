package de.ueberdosis.mp3info.gui.event;

/**
 * Listener for data updates in Id3Jpanels
 *
 * @author Florian Heer
 * @version $Id: UpdateListener.java,v 1.2 2002/12/19 23:04:33 heer Exp $
 */

public interface UpdateListener {
    /**
     * Is called when data have been updated
     *
     * @param ue information about the update
     */
    public void dataUpdated(UpdateEvent ue);
}


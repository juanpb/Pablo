package de.ueberdosis.mp3info.gui.event;

import de.ueberdosis.mp3info.gui.Id3JPanel;

/**
 * Is created when an Id3JPanel updates data of an ID3V2Frame
 *
 * @author Florian Heer
 * @version $Id: UpdateEvent.java,v 1.2 2002/12/19 23:04:33 heer Exp $
 */

public class UpdateEvent extends java.util.EventObject {
    /**
     * Creates an instance of this event
     *
     * @param pnl the panel that created this event
     */
    public UpdateEvent(Id3JPanel pnl) {
        super(pnl);
    }

    /**
     * Creates a human readable presentation of this event
     */
    public String toString() {
        return "Data updated in " + getSource();
    }
}

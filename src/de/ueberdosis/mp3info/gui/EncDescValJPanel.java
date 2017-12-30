package de.ueberdosis.mp3info.gui;

import de.ueberdosis.mp3info.Configuration;
import de.ueberdosis.mp3info.gui.event.UpdateEvent;
import de.ueberdosis.mp3info.id3v2.EncDescValFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** This Panel contains display-/edit components for a number of ID3v2 Frames.
 * It works for every Frame that consists of a description and a value 
 * (and an encoding, but that is not up to the user to choose) like FrameWXXX
 *
 * Of course we need a callback-mechanism that is a little more advanced than 
 * that used in FrameText and it's JPanel.
 *
 * @author Florian Heer
 * @version $Id: EncDescValJPanel.java,v 1.4 2002/12/19 23:04:32 heer Exp $
 */
public class EncDescValJPanel extends Id3JPanel {
    /** Description and Value labels */
    JLabel lblDesc, lblVal;
    /** Description and Value fields */
    JTextField txtDesc, txtVal;
    /** The OK button used in selfupdate mode */
    JButton btn;

    /** The V2 frame to be displayed / edited */
    EncDescValFrame frm;

    /** Creates a panel.
     * @param edit switches between display and edit mode
     * @param selfupdate switches if the Panel has its own update-button
     * @param caption1 contains the caption for the first label
     * @param description is the data of the description
     * @param caption2 contains the caption for the second label
     * @param value the contents of the value.
     * @param frame the calling frame.
     */
    public EncDescValJPanel (boolean edit, boolean selfupdate, 
			     String caption1, String description,
			     String caption2, String value, 
			     EncDescValFrame frame) {
	// Creating the panel
	super (new FlowLayout (FlowLayout.LEFT));

	// Setting sizes
	int lWidth = Configuration.labelWidth;
	int lHeight = Configuration.labelHeight;
	Dimension lDim = new Dimension (lWidth,lHeight);
	int tWidth = Configuration.textWidth;
	int tHeight = Configuration.textHeight;
	Dimension tDim = new Dimension (tWidth,tHeight);
	int bWidth = Configuration.buttonWidth;
	int bHeight = Configuration.buttonHeight;
	Dimension bDim = new Dimension (bWidth,bHeight);


	frm = frame;
	// Examine the frame for its capabilities
	if (!frm.canEdit ()) edit = false;
	if (!edit) selfupdate = false;
	// Copy data from the frame
	lblDesc = new JLabel (caption1);
	lblVal = new JLabel (caption2);
	txtDesc = new JTextField (description);
	txtVal = new JTextField (value);

	// Computing the size of the panel
	int width = lWidth+tWidth+20;
	int height = 2*((lHeight > tHeight?lHeight:tHeight)+5);
	if (selfupdate) {
	    width += bWidth+10;
	    height += 5;
	}

	// Set sizes of the components
	lblDesc.setMinimumSize (lDim);
	lblDesc.setPreferredSize (lDim);
	txtDesc.setEditable (edit);
	txtDesc.setEnabled (edit);
	txtDesc.setMinimumSize (tDim);
	txtDesc.setPreferredSize (tDim);

	lblVal.setMinimumSize (lDim);
	lblVal.setPreferredSize (lDim);
	txtVal.setEditable (edit);
	txtVal.setEnabled (edit);
	txtVal.setMinimumSize (tDim);
	txtVal.setPreferredSize (tDim);

	// Set sizes of the Panel
	setMinimumSize (new Dimension (width, height));
	setPreferredSize (new Dimension (width, height));

	// .. and add the Components ..
	add (lblDesc);
	add (txtDesc);
	add (lblVal);
	add (txtVal);

	if (selfupdate) {
	    btn = new JButton ("OK");
	    btn.setMinimumSize (bDim);
	    btn.setPreferredSize (bDim);
	    btn.addActionListener 
		( new ActionListener () {
			public void actionPerformed (ActionEvent ae) {
			    updateData ();
			}
		    });
	    add (btn);
	}
    }

    /** Copies the possibly edited information to the V2 frame 
     * and informs all UpdateListeners
     */
    public void updateData () {
	// Copy values
	frm.setDescription (txtDesc.getText ());
	frm.setValue (txtVal.getText ());
	// Inform Listeners
	fireUpdateEvent (new UpdateEvent (this));
    }
}

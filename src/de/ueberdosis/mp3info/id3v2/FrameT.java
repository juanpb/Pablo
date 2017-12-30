package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Configuration;
import de.ueberdosis.mp3info.DatamismatchException;
import de.ueberdosis.mp3info.gui.Id3JPanel;
import de.ueberdosis.mp3info.gui.event.UpdateEvent;
import de.ueberdosis.util.OutputCtr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Base class for all text related frames.
 * 
 * @author Florian Heer
 * @version $Id: FrameT.java,v 1.8 2005/02/22 21:38:30 heer Exp $
 */

public class FrameT extends ID3V2Frame {
	byte encoding = 0;

	String text = "";

	public String getLongName() {
		return "Generic text frame";
	}

	public FrameT(String id) {
		super(id);
		encoding = Helper.getByteEncoding(Configuration.defaultEncoding);
	}

	public FrameT(ID3V2Frame frame) {
		super(frame);
		if (frame instanceof FrameT) {
			FrameT frm = (FrameT) frame;
			encoding = frm.encoding;
			text = frm.text;
		}
		else encoding = Helper.getByteEncoding (Configuration.defaultEncoding);
	}

	public FrameT(ID3V2Frame frm, DataSource ds) throws SeekPastEndException {
		super(frm);
		// Decoding our information from the DataSource!
		try {
			encoding = ds.getByte();
			// hack for texts not started with encoding byte (2.3 allows this)
			if (encoding > 3) {
				ds.seekRelative (-1);
				encoding = Helper.getByteEncoding (Configuration.defaultEncoding);
			}
			text = ds.readString(encoding);

			// If the textstring is followed by a termination ($00) all the
			//following information should be ignored and not be displayed.

		} catch (DatamismatchException ex) {
			OutputCtr.println(0, "Text frame can't be instantiated!");
			throw new SeekPastEndException (ex.getMessage());
		}
	}

	public String getText() {
		return new String(text);
	}

	public void setText(String txt) {
		text = new String(txt.trim());
	}

	public String toString() {
		return getLongName() + " Encoding: " + encoding + "\ntext: " + text;
	}

	public byte[] getData() {
		byte[] textBytes = Helper.stringToByteArray(text, encoding);
		byte[] data = new byte[textBytes.length + 1];
		data[0] = encoding;
		System.arraycopy(textBytes, 0, data, 1, textBytes.length);
		return data;
	}

	public boolean containsData() {
		return (text.trim().length() > 0);
	}

	/**
	 * @return the encoding used in this frame
	 */
	public byte getEncoding() {
		return encoding;
	}

	/** Changes the used encoding
	 * @param newEnc a byte-representation of the new encoding
	 * @throws DatamismatchException if the encoding to be set is not allowed 
	 * @see Helper isValidEncoding ()
	 */
	public void setEncoding(byte newEnc) throws DatamismatchException {
		if (Helper.isValidEncoding(newEnc))
			encoding = newEnc;
		else
			throw new DatamismatchException("Tried to set encoding to "
					+ newEnc + " which is out of range.");
	}

	protected Id3JPanel createJPanel(boolean edit, boolean selfupdate,
			String caption) {
		int lWidth = Configuration.labelWidth;
		int lHeight = Configuration.labelHeight;
		Dimension lDim = new Dimension(lWidth, lHeight);
		int tWidth = Configuration.textWidth;
		int tHeight = Configuration.textHeight;
		Dimension tDim = new Dimension(tWidth, tHeight);
		int bWidth = Configuration.buttonWidth;
		int bHeight = Configuration.buttonHeight;
		Dimension bDim = new Dimension(bWidth, bHeight);

		if (!canEdit())
			edit = false;
		if (!edit)
			selfupdate = false;
		JLabel lbl = new JLabel(caption);
		final JTextField txt = new JTextField(text);
		final Id3JPanel pnl = new Id3JPanel(new FlowLayout(FlowLayout.LEFT),
				true) {
			public void updateData() {
				text = txt.getText();
				dataChanged = true;
				fireUpdateEvent(new UpdateEvent(this));
			}
		};
		int width = lWidth + tWidth + 20;
		int height = (lHeight > tHeight ? lHeight : tHeight) + 6;
		if (selfupdate) {
			width += bWidth + 5;
			height = bHeight + 10;
		}

		txt.setEditable(edit);
		txt.setEnabled(edit);
		txt.setMinimumSize(tDim);
		txt.setPreferredSize(tDim);
		lbl.setMinimumSize(lDim);
		lbl.setPreferredSize(lDim);

		pnl.setMinimumSize(new Dimension(width, height));
		pnl.setPreferredSize(new Dimension(width, height));
		pnl.add(lbl);
		pnl.add(txt);
		if (selfupdate) {
			JButton btn = new JButton("OK");
			btn.setMinimumSize(bDim);
			btn.setPreferredSize(bDim);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					pnl.updateData();
				}
			});
			pnl.add(btn);
		}
		return pnl;
	}
}
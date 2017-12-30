package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Configuration;
import de.ueberdosis.mp3info.gui.Id3JPanel;
import de.ueberdosis.mp3info.gui.event.UpdateEvent;
import de.ueberdosis.util.OutputCtr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

/**
 * Ownership frame
 * The ownership frame might be used as a reminder of a made transaction or,
 * if signed, as proof. Note that the "USER" and "TOWN" frames are good to
 * use in conjunction with this one. The frame begins, after the frame ID,
 * size and encoding fields, with a 'price payed' field. The first three
 * characters of this field contains the currency used for the transaction,
 * encoded according to ISO-4217 alphabetic currency code. Concatenated to
 * this is the actual price payed, as a numerical string using "." as the
 * decimal separator. Next is an 8 character date string (YYYYMMDD) followed
 * by a string with the name of the seller as the last field in the frame.
 * There may only be one "OWNE" frame in a tag.
 * <p/>
 * <Header for 'Ownership frame', ID: "OWNE">
 * Text encoding $xx
 * Price payed <text string> $00
 * Date of purch. <text string>
 * Seller <text string according to encoding>
 *
 * @author Florian Heer
 * @version $Id: FrameOWNE.java,v 1.10 2005/02/20 23:58:31 heer Exp $
 */

// FIXME! Dates do not get checked.
//  And encoding is not cared for.
public class FrameOWNE extends ID3V2Frame {
    private byte encoding = 0;
    private String pricePayed = "";
    private String purchaseDate = "";
    private String seller = "";

    public FrameOWNE(ID3V2Frame frm) {
        super(frm);
        if (frm instanceof FrameOWNE) {
            FrameOWNE fr = (FrameOWNE) frm;
            encoding = fr.encoding;
            pricePayed = fr.pricePayed;
            purchaseDate = fr.purchaseDate;
            seller = fr.seller;
        }
    }

    public int legalSince() {
        return 3;
    }

    public int deprecatedSince() {
        return -1;
    }

    public String getLongName() {
        return "Ownership frame";
    }

    public FrameOWNE(ID3V2Frame frm, DataSource ds)
            throws SeekPastEndException {
        super(frm);
        // Decoding our information from the DataSource
        try {
            encoding = ds.getByte();
            byte[] bAr = ds.getBytesTo((byte) 0);
            if (bAr != null)
                pricePayed = new String(bAr);
            // Skip the terminating zero byte
            ds.getByte();

            purchaseDate = new String(ds.getBytes(8));
            bAr = ds.getBytes(ds.getBytesLeft());

            try {
                seller = new String(bAr, Configuration.defaultEncoding);
            } catch (UnsupportedEncodingException ex) {
                seller = new String(bAr);
            }
        } catch (SeekPastEndException ex) {
            OutputCtr.println(0, getLongName() + " can't be instantiated! SPEEx!");
            throw ex;
        }
    }


    public String toString() {
        return getLongName() + "\nEncoding: " + encoding
                + "\nPrice payed:  " + pricePayed
                + "\nDate of purchase: " + purchaseDate
                + "\nSeller: " + seller;
    }

    public byte[] getData() {
        byte[] bAr = new byte[pricePayed.length() + seller.length() + 10];
        bAr[0] = encoding;
        System.arraycopy(pricePayed.getBytes(), 0, bAr, 1, pricePayed.length());
        bAr[pricePayed.length() + 1] = (byte) 0;
        System.arraycopy(purchaseDate.getBytes(), 0, bAr, pricePayed.length() + 2, 8);
        System.arraycopy(seller.getBytes(), 0, bAr, pricePayed.length() + 10, seller.length());
        return bAr;
    }

    public boolean containsData() {
        return pricePayed.length() > 0
                || purchaseDate.length() > 0
                || seller.length() > 0;
    }

    public String getPricePayed() {
        return new String(pricePayed);
    }

    public void setPricePayed(String p) {
        pricePayed = new String(p);
    }

    public String getPurchaseDate() {
        return new String(purchaseDate);
    }

    public void setPurchaseDate(String p) {
        purchaseDate = new String(p);
    }

    public String getSeller() {
        return new String(seller);
    }

    public void setSeller(String s) {
        seller = new String(s);
    }

    /**
     * Can be edited
     */
    public boolean canEdit() {
        return true;
    }

    /**
     * Can be displayed
     */
    public boolean canDisplay() {
        return true;
    }

    public Id3JPanel createJPanel(boolean edit, boolean selfupdate) {
        int lWidth = Configuration.labelWidth;
        int lHeight = Configuration.labelHeight;
        Dimension lDim = new Dimension(lWidth, lHeight);
        int tWidth = Configuration.textWidth;
        int tHeight = Configuration.textHeight;
        Dimension tDim = new Dimension(tWidth, tHeight);
        int bWidth = Configuration.buttonWidth;
        int bHeight = Configuration.buttonHeight;
        Dimension bDim = new Dimension(bWidth, bHeight);


        if (!canEdit()) edit = false;
        if (!edit) selfupdate = false;
        JLabel lbl1 = new JLabel("Price payed:");
        JLabel lbl2 = new JLabel("Purchase date:");
        JLabel lbl3 = new JLabel("Seller:");

        final JTextField txt1 = new JTextField(pricePayed);
        final JTextField txt2 = new JTextField(purchaseDate);
        final JTextField txt3 = new JTextField(seller);
        final Id3JPanel pnl = new Id3JPanel(new FlowLayout(FlowLayout.LEFT), true) {
            public void updateData() {
                pricePayed = txt1.getText();
                purchaseDate = txt2.getText();
                seller = txt3.getText();
                dataChanged = true;
                fireUpdateEvent(new UpdateEvent(this));
            }
        };
        int width = lWidth + tWidth + 20;
        int height = 3 * (lHeight > tHeight ? lHeight : tHeight) + 6;
        if (selfupdate) {
            width += bWidth + 5;
        }

        txt1.setEditable(edit);
        txt1.setEnabled(edit);
        txt1.setMinimumSize(tDim);
        txt1.setPreferredSize(tDim);
        txt2.setEditable(edit);
        txt2.setEnabled(edit);
        txt2.setMinimumSize(tDim);
        txt2.setPreferredSize(tDim);
        txt3.setEditable(edit);
        txt3.setEnabled(edit);
        txt3.setMinimumSize(tDim);
        txt3.setPreferredSize(tDim);
        lbl1.setMinimumSize(lDim);
        lbl1.setPreferredSize(lDim);
        lbl2.setMinimumSize(lDim);
        lbl2.setPreferredSize(lDim);
        lbl3.setMinimumSize(lDim);
        lbl3.setPreferredSize(lDim);

        pnl.setMinimumSize(new Dimension(width, height));
        pnl.setPreferredSize(new Dimension(width, height));
        pnl.add(lbl1);
        pnl.add(txt1);
        if (selfupdate) {
            JButton btn = new JButton("OK");
            btn.setMinimumSize(bDim);
            btn.setPreferredSize(bDim);
            btn.addActionListener
                    (new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pnl.updateData();
                        }
                    });
            pnl.add(btn);
        }

        pnl.add(lbl2);
        pnl.add(txt2);
        pnl.add(lbl3);
        pnl.add(txt3);

        return pnl;
    }

}

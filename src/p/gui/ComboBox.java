package p.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:15:43 PM
 */
public class ComboBox extends JPanel implements ActionListener {
    private JLabel lbl = new JLabel();
    private JComboBox cmb = new JComboBox();

    public ComboBox() {
        this(null);
    }

    public ComboBox(String lblText, java.util.List<String> valores) {
        init(lblText);
        for (String v : valores) {
            cmb.addItem(v);
        }
    }

    public ComboBox(String lblText, Dimension d) {
        init(lblText);
        setSize(d);
    }

    public ComboBox(String lblText) {
        init(lblText);
    }

    private void init(String lblText) {
        setLayout(new GridBagLayout());
        Insets ins1 = new Insets(0, 0, 0, 0);

        GridBagConstraints bc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0);
        add(lbl, bc);
        bc.gridy = 1;
        bc.insets = new Insets(0,0,1,0);
        add(cmb, bc);

        if (lblText != null)
            lbl.setText(lblText);
    }


    public void setSize(Dimension d){
        super.setPreferredSize(d);
    }


    /**************************************************************************/
    /*          delegate methods        */
    /**************************************************************************/

    public int getItemCount() {
        return cmb.getItemCount();
    }

    public int getSelectedIndex() {
        return cmb.getSelectedIndex();
    }

    public void removeAllItems() {
        cmb.removeAllItems();
    }

    public void removeItemAt(int anIndex) {
        cmb.removeItemAt(anIndex);
    }

    public void setSelectedIndex(int anIndex) {
        cmb.setSelectedIndex(anIndex);
    }

    public void setEditable(boolean aFlag) {
        cmb.setEditable(aFlag);
    }

    public void setEnabled(boolean b) {
        cmb.setEnabled(b);
    }

    public ActionListener[] getActionListeners() {
        return cmb.getActionListeners();
    }

    public void addActionListener(ActionListener l) {
        cmb.addActionListener(l);
    }

    public void actionPerformed(ActionEvent e) {
        cmb.actionPerformed(e);
    }

    public ItemListener[] getItemListeners() {
        return cmb.getItemListeners();
    }

    public void removeActionListener(ActionListener l) {
        cmb.removeActionListener(l);
    }

    public void addItemListener(ItemListener aListener) {
        cmb.addItemListener(aListener);
    }

    public void removeItemListener(ItemListener aListener) {
        cmb.removeItemListener(aListener);
    }

    public void processKeyEvent(KeyEvent e) {
        cmb.processKeyEvent(e);
    }

    public Object getSelectedItem() {
        return cmb.getSelectedItem();
    }

    public Object[] getSelectedObjects() {
        return cmb.getSelectedObjects();
    }

    public Object getItemAt(int index) {
        return cmb.getItemAt(index);
    }

    public void addItems(java.util.List list) {
        for (Object o : list) {
            addItem(o);
        }

    }
    public void addItem(Object anObject) {
        cmb.addItem(anObject);
    }

    public void removeItem(Object anObject) {
        cmb.removeItem(anObject);
    }

    public void setSelectedItem(Object anObject) {
        cmb.setSelectedItem(anObject);
    }

    public void insertItemAt(Object anObject, int index) {
        cmb.insertItemAt(anObject, index);
    }

    public ComboBoxModel getModel() {
        return cmb.getModel();
    }

    public void setModel(ComboBoxModel aModel) {
        cmb.setModel(aModel);
    }
}

package p.gui;

import p.gui.dd.FileTransfer;
import p.gui.dd.FileTransferHandler;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:15:43 PM
 */
public class LabelTextField extends JPanel implements FileTransfer {
    private TextField txt;


    public LabelTextField() {
        this(null);
    }

    public LabelTextField(String lblText, Dimension d) {
        init(lblText);
        setSize(d);
    }

    public LabelTextField(String lblText) {
        super();
        init(lblText);
    }

    public LabelTextField(String lblText, String txtTexto) {
        super();
        init(lblText);
        txt.setText(txtTexto);
    }

    private void init(String lblText) {
        JLabel lbl = new JLabel();
        txt = new TextField();

        setLayout(new GridBagLayout());
        Insets ins1 = new Insets(0, 0, 0, 0);

        GridBagConstraints bc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0);
        add(lbl, bc);
        bc.gridy = 1;
        add(txt, bc);

        if (lblText != null)
            lbl.setText(lblText);

        //forwardeo el foco al txt
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                txt.requestFocus();
            }
        });


        //DD
        FileTransferHandler fth = new FileTransferHandler(this);
        txt.setTransferHandler(fth);
    }

    /**
     * Tooltip del textInput
     * @param fileImage
     */
    public void setImageTooltip(String fileImage){
        txt.setImageTooltip(fileImage);
    }

    public void setSize(Dimension d){
        super.setPreferredSize(d);
    }

    public void setText(String t){
        txt.setText(t);
    }
    public String getText(){
        return txt.getText();
    }


    public void addKeyListener(KeyListener l) {
        txt.addKeyListener(l);
    }


    public void removeKeyListener(KeyListener l) {
        txt.removeKeyListener(l);
    }

    public void addActionListener(ActionListener l) {
        txt.addActionListener(l);
    }

    public void removeActionListener(ActionListener l) {
        txt.removeActionListener(l);
    }


    public void addFocusListener(FocusListener l) {
        txt.addFocusListener(l);
    }


    public void removeFocusListener(FocusListener l) {
        txt.removeFocusListener(l);
    }

    public void addDocumentListener(DocumentListener l) {
        txt.getDocument().addDocumentListener(l);

    }

    public void clean() {
        txt.clean();
    }

    public void setEditable(boolean b) {
        txt.setEditable(b);
    }

    @Override
    public void setFont(Font font) {
        if (txt != null)
            txt.setFont(font);
    }

    @Override
    public void requestFocus() {
        txt.requestFocus();
    }

    @Override
    public void setToolTipText(String text) {
        txt.setToolTipText(text);
    }

    @Override
    public void setFile(File f) {
        if (f != null)
            txt.setText(f.getAbsolutePath());
    }

    @Override
    public void setFiles(List<File> files) {
        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            sb.append(f.getAbsolutePath()).append(";");
        }
        txt.setText(sb.toString());
    }


}

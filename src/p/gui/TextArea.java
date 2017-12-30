package p.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:15:43 PM
 */
public class TextArea extends JPanel {
    private JLabel lbl = new JLabel();
    private JTextArea_ txt = new JTextArea_();
    private JScrollPane scrP = new JScrollPane(txt,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public TextArea() {
        this(null);
    }

    public TextArea(String lblText, Dimension d) {
        init(lblText);
        setSize(d);
        setPreferredSize(d);
    }

    public TextArea(String lblText) {
        init(lblText);
    }

    public void setWrapStyleWord(boolean word) {
        txt.setWrapStyleWord(word);
    }


    public void setLineWrap(boolean wrap) {
        txt.setLineWrap(wrap);
    }

    private void init(String lblText) {
        setLayout(new GridBagLayout());
        setWrapStyleWord(true);
        setLineWrap(true);
        Insets ins1 = new Insets(0, 0, 0, 0);

        GridBagConstraints bc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0);
        add(lbl, bc);
        bc.gridy = 1;
        bc.weighty = 1;
        bc.fill = GridBagConstraints.BOTH;
        add(scrP, bc);

        if (lblText != null)
            lbl.setText(lblText);
    }



    public void clean(){
        setText("");
    }

    public void setText(String t){
        txt.setText(t);
    }
    public String getText(){
        return txt.getText().trim();
    }

    public java.util.List<String> getTextPorLinea() {
        String separador = "\n";
        java.util.List<String> res = new ArrayList<String>();
        String x = getText();

        while(x.trim().length() > 0){
            int hasta = x.indexOf(separador)  ;
            if (hasta < 0)
                hasta = x.length();
            String linea = x.substring(0, hasta);
            while (x.startsWith(separador)) {
                x = x.substring(1);
            }
            x = x.substring(hasta);
            if (!linea.isEmpty())
                res.add(linea);
        }

        return res;
    }
}

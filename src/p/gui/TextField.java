package p.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TextField extends JTextField {
    private ImageToolTip imageToolTip = null;

    public TextField(String text) {
        super(text);
        init();
    }

    public TextField() {
        super();
        init();
    }

    private void init() {
        new TextManager(this);
    }

    public void setImageTooltip(String fileImage) {
        if (fileImage == null)
            imageToolTip = null;
        else {
            ImageIcon ii = new ImageIcon(fileImage);
            int h = ii.getIconHeight();
            int w = ii.getIconWidth();
            double proporcionHW = (double) h / w;
            imageToolTip = new ImageToolTip(ii.getImage(), proporcionHW);
            setToolTipText("");
        }
    }

    @Override
    public JToolTip createToolTip() {
        //create the ImageToolTip by passing the
        //image to appear on it
        if (imageToolTip != null)
            return imageToolTip;
        else
            return super.createToolTip();
    }

    public void clean() {
        setText("");
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(200, 150);
        f.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent we) {
            System.exit(0);
          }
        });
        TextField txt1 = new TextField();
        f.add(txt1, BorderLayout.NORTH);
        TextField txt = new TextField();//getTC();
        f.add(txt, BorderLayout.CENTER);
        TextField txt2 = new TextField("p.gui.TextField South");
        f.add(txt2, BorderLayout.SOUTH);
        f.setVisible(true);
    }
}
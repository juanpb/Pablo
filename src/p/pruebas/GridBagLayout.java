package p.pruebas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: JP
 * Date: 24/08/2005
 * Time: 01:10:53
 */
public class GridBagLayout extends JFrame{
    public static void main(String[] args) {
        new GridBagLayout();
    }

    public GridBagLayout() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        JButton btn = new JButton("1");
        JButton btn2 = new JButton("2");
        JButton btn3 = new JButton("3");

        btn.setPreferredSize(new Dimension(60,20));
        btn2.setPreferredSize(new Dimension(60,20));
        btn3.setPreferredSize(new Dimension(60,20));
        setSize(200,190);

        Insets insets = new Insets(5, 5, 5, 5);
        this.getContentPane().add(btn,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                  GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                  insets, 0, 0));
        this.getContentPane().add(btn2,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                  GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                  insets, 0, 0));
        this.getContentPane().add(btn3,
            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.1,
                  GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                  insets, 0, 0));

        setVisible(true);

    }
}

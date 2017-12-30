package p.pruebas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: JP
 * Date: 29/07/2004
 * Time: 20:48:51
 */
public class Pruebas extends JFrame{


    public static void main(String[] args) throws ClassNotFoundException {
        new Pruebas();
    }

    public Pruebas() {
        getContentPane().setLayout(new BorderLayout());
        JButton s = new JButton("/\\");
        JButton b = new JButton("\\/");
        JButton jButton3 = new JButton("3");

        int x = 30;
        s.setMargin(new Insets(0, 0, 0, 0));
        b.setMargin(new Insets(0, 0, 0, 0));
        s.setPreferredSize(new Dimension(x,x));
        b.setPreferredSize(new Dimension(x,x));
        JPanel pnl = new JPanel();
        pnl.add(s,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
        pnl.add(b,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

        jButton3.setPreferredSize(new Dimension(100,100));
        getContentPane().add(jButton3,  BorderLayout.CENTER);
        getContentPane().add(pnl, BorderLayout.EAST);
        pack();
        setVisible(true);
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

}
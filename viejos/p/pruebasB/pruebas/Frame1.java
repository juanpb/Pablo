package p.pruebas;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Frame1 extends JFrame {
    private JButton jButton1 = new JButton();
    Process _proc = null;

    public Frame1() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{
        Frame1 frame1 = new Frame1();
        frame1.correr();
    }
    private void jbInit() throws Exception {
        jButton1.setBounds(new Rectangle(92, 85, 81, 34));
        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(jButton1, null);
        this.getContentPane().setLayout(null);
        this.setBounds(300,300,300,300);
        this.setVisible(true);

    }

    private void correr() throws Exception{
        _proc = Runtime.getRuntime().exec("notepad");
    }

    void jButton1_actionPerformed(ActionEvent e) {
        System.out.println("boton");
        _proc.destroy();
    }
}
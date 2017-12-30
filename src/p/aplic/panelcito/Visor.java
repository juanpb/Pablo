package p.aplic.panelcito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: JPB
 * Date: Mar 6, 2007
 * Time: 12:35:46 PM
 */
public class Visor extends JFrame {
//    p.gui.TextArea txt = new TextArea();
    JButton txt = new JButton("+++");
    public static void main(String[] args) {
        Visor v = new Visor();
        v.init();
    }

    private void init(){
        setLayout(new BorderLayout());
        add(txt, BorderLayout.CENTER);
        Dimension d = new Dimension(400, 200);
        setPreferredSize(d);
        pack();
        setVisible(true);
//        txt.addMouseMotionListener(new MouseMotionAdapter(){
//            public void mouseMoved(MouseEvent e) {
//                System.out.println("mouseMoved");
//            }
//        });
        txt.addMouseListener(new MouseAdapter(){
            public void mouseExited(MouseEvent e) {
                mouseExited2();
            }
        });

        txt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                System.out.println("e.toString() = " + e.toString());
                System.out.println("e.paramString() = " + e.paramString());
                System.out.println("e.getKeyChar() = " + e.getKeyChar());
                System.out.println("e.isActionKey() = " + e.isActionKey());
                System.out.println("e.getKeyCode() = " + e.getKeyCode());
                System.out.println("++++++++++++++++");
            }
        });
    }

    private void mouseExited2(){
        System.out.println("FL");
//        this.transferFocus();
    }
}

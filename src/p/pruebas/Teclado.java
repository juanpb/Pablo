package p.pruebas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: JPB
 * Date: 30/10/17
 * Time: 10:41
 */
public class Teclado extends JFrame {
    public static void main(String[] args) {
        Teclado teclado = new Teclado();
        teclado.init()    ;
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        JButton btn = new JButton("asd");
        getContentPane().add(btn, BorderLayout.CENTER);
        setSize(230,80);
        setVisible(true);
        btn.addKeyListener(new KeyAdapter(){
            public void keyTyped2(KeyEvent e) {
                System.out.println("e.paramString() = " + e.paramString());
                System.out.println("e.toString() = " + e.toString());
                System.out.println("e.getExtendedKeyCode() = " + e.getExtendedKeyCode());
                System.out.println("e.getKeyChar() = " + e.getKeyChar());
                System.out.println("e.getKeyCode() = " + e.getKeyCode());
            }
            public void keyPressed(KeyEvent e) {
                System.out.println("e.paramString() = " + e.paramString());
                System.out.println("e.toString() = " + e.toString());
                System.out.println("e.getExtendedKeyCode() = " + e.getExtendedKeyCode());
                System.out.println("e.getKeyChar() = " + e.getKeyChar());
                System.out.println("e.getKeyCode() = " + e.getKeyCode());
            }
        });
    }
}

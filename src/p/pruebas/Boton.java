package p.pruebas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: JP
 * Date: 30/07/2005
 * Time: 01:26:53
 */
public class Boton {
    private static Button b;
    private static JTextField tf;

    public static void main(String[] args) {

        new Boton();
//        Thread t = new Thread(new Runnable(){
//                    public void run() {
//                        new Boton();
//                        hacer();
//
//                    }
//              });
//        Thread t2 = new Thread(new Runnable(){
//                    public void run() {
//                        new Boton();
//                        hacer();
//                    }
//              });
//        t.start();
//        t2.start();
    }

    public Boton() {
        JFrame f = new JFrame();
        b = new Button();
        tf = new JTextField();
        f.getContentPane().setLayout(new BorderLayout(10,10));
        f.getContentPane().add(b, BorderLayout.NORTH);
        f.getContentPane().add(tf, BorderLayout.CENTER);
        dd();
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                hacer();
            }

        });
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    private static void dd() {
//        tf.setTransferHandler(new TransferHandler(){
//            public boolean canImport(TransferHandler.TransferSupport support)
//            {
////                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)
////                    || !support.isDrop())
//                    return false;
//            }
//        });
    }
    private static void hacer() {

    }
}

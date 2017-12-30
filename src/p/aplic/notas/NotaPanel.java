package p.aplic.notas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: JP
 * Date: 30/04/2005
 * Time: 02:16:18
 */
public class NotaPanel extends JFrame{
    JTextArea _txt = new JTextArea();
    JButton _btnCerrar = new JButton("x");
    private JPanel _pnlHeader;

    public NotaPanel() {
        init();
    }

    private void init(){
        _pnlHeader = new JPanel(new BorderLayout(3,3));
        _pnlHeader.add(_btnCerrar, BorderLayout.CENTER);

        setSizes();
        addListeners();

        JDialog win2 = new JDialog(this);
        JWindow win = new JWindow(this);
        win.getContentPane().setLayout(new BorderLayout(3,3));
        win.getContentPane().add(_pnlHeader, BorderLayout.SOUTH);
        win.getContentPane().add(_txt, BorderLayout.CENTER);
//        win.setSize(100,100);
        win.setBounds(600,600, 300, 300);
        win.setVisible(true);
    }

    private void addListeners() {
        _btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _txt.setText(e.paramString()+ "---");
            }
        });
    }

    private void cerrar() {
        System.exit(0);
    }

    private void setSizes() {
        _txt.setSize(140,140);
        _pnlHeader.setSize(50, 15);
        _btnCerrar.setSize(20, 10);
    }

    public static void main(String[] args) {
//        JPanel pnl = new JPanel();
        new NotaPanel();
    }
}

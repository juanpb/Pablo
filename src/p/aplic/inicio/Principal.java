package p.aplic.inicio;

import javax.swing.*;
import java.awt.*;

/**
 * User: JP
 * Date: 13/05/2005
 * Time: 11:30:20
 */
public class Principal extends JFrame{
    private JPanel pnlCentro = new JPanel();
    private JScrollPane scroll = new JScrollPane(pnlCentro);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                new Principal();
             }
        });
    }

    public Principal() {
        init();
    }

    private void init() {
        getContentPane().setLayout(new BorderLayout(5,5));
        pnlCentro.setLayout(new GridLayout(0, 1));
        ProgramaPanel pp = new ProgramaPanel(2000, "el exe/a.exe", 1);

        scroll.setPreferredSize(new Dimension(260 ,100));
        pnlCentro.add(pp);
        getContentPane().add(scroll, BorderLayout.CENTER);

        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

}

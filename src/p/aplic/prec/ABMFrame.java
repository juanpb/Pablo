package p.aplic.prec;

import org.apache.log4j.Logger;
import p.util.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * User: JPB
 * Date: Apr 16, 2008
 * Time: 7:29:24 PM
 */
public class ABMFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(ABMFrame.class);
    public ABMFrame() throws IOException {

        ABMPanel p = new ABMPanel();

        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);
        setPreferredSize(new Dimension(600, 600));
        setBounds(0,0,600, 600);
        GUI.centrar(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }



    public static void main(String[] args) throws IOException {

        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ABMFrame();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });

    }
}

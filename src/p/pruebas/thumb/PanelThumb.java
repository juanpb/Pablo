package p.pruebas.thumb;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 22, 2010
 * Time: 11:27:59 PM
 */
public class PanelThumb extends JPanel {
    private static final int COLS = 5;

    public PanelThumb(Component obs) {
        super(new GridLayout(0, COLS));
    }

    public void setFiles(List<String> files){
        super.removeAll();
        for(String f : files) {
            long ini = System.currentTimeMillis();
            add(new JButton(new Icono(f)));
            long fin = System.currentTimeMillis();
            long seg = (fin - ini);
            System.out.println("Demoró " + seg + " ms");            
        }
    }
}

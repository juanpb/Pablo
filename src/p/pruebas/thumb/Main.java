package p.pruebas.thumb;

import javax.swing.*;

/**
 * User: JPB
 * Date: Feb 22, 2010
 * Time: 11:17:56 PM
 */
public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                new PrincipalFrame();
             }
        });
            
    }
}

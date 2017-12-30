package p.aplic.verfotos;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;

/**
 * User: JP
 * Date: 07/05/2005
 * Time: 15:06:11
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    public static void main(final String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 try {
                     new Main(args);
                 } catch (IOException e) {
                     logger.error(e.getMessage(), e);
                 }
             }
        });
    }

    public Main(String[] args) throws IOException {
        if (args.length == 0){
            System.out.println("Hay que pasarle el config como parámetro.");
            return ;
        }
        Config config = new Config(args[0]);
        new Control(config);            
    }

}

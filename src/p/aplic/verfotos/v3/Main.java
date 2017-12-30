package p.aplic.verfotos.v3;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * User: JPB
 * Date: Aug 13, 2009
 * Time: 11:33:10 AM
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    private Config config;
    public Main(String configPath) throws IOException {
        config = new Config(configPath);
        init();
    }


    public static void main(final String[] args) throws IOException {
        if (args.length != 1){
            System.out.println("Falta pasar el config como parámetro");
            return ;
        }
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 try {
                     new Main(args[0]);
                 } catch (IOException e) {
                     logger.error(e.getMessage(), e);
                 } catch (Exception e) {
                     logger.error(e.getMessage(), e);
                 }
             }
        });
    }

    private void init() throws IOException {
        GraphicsDevice device = getDevice();
        ControlObj controlObj = new ControlObj();
        Visor3 visor = new Visor3(device, controlObj);

        Control control = new Control(visor, config, controlObj);
        control.empezar();

        System.exit(0);
    }

    public GraphicsDevice getDevice() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //todo ver monitor
        return  env.getDefaultScreenDevice();
    }
}

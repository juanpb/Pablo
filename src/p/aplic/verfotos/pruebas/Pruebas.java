package p.aplic.verfotos.pruebas;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Aug 11, 2009
 * Time: 9:44:23 AM
 */
public class Pruebas extends JFrame {
    String foto = "C:\\Documents and Settings\\JPB\\My Documents\\My Pictures\\t.bmp";
    public static void main(String[] args) {
        new Pruebas().prueba();
    }

    private void prueba(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        GraphicsDevice dev = devices[0];
        System.out.println("dev.isFullScreenSupported() = " + dev.isFullScreenSupported());
        int width = dev.getDisplayMode().getWidth();
        GraphicsConfiguration conf = dev.getDefaultConfiguration();



        DisplayMode newDisplayMode;
        setBackground(Color.black);

//        createBufferStrategy();
        
        setUndecorated(true);
        setIgnoreRepaint(true);


        try {
            dev.setFullScreenWindow(this);

        } finally {
            dev.setFullScreenWindow(null);
        }
        setVisible(true);        
    }

    public void mostrarDisplayModes(GraphicsDevice dev ){
        DisplayMode oldDisplayMode = dev.getDisplayMode();
        System.out.println("oldDisplayMode = " + oldDisplayMode);
        DisplayMode[] modes = dev.getDisplayModes();
        System.out.println("modes.length = " + modes.length);

        for(DisplayMode mode : modes) {
            System.out.println("mode.getWidth() = " + mode.getWidth());
            System.out.println("mode.getHeight() = " + mode.getHeight());
            System.out.println("mode.getBitDepth() = " + mode.getBitDepth());
            System.out.println("mode.getRefreshRate() = " + mode.getRefreshRate());
            System.out.println("mode.toString() = " + mode.toString());
            System.out.println("");
        }
    }

}

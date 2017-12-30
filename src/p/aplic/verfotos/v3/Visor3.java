package p.aplic.verfotos.v3;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferStrategy;


/**
 * User: JPB
 * Date: May 23, 2008
 * Time: 3:47:28 PM
 */
public class Visor3 extends Frame {
    private static final Logger logger = Logger.getLogger(Visor3.class);
    private ControlObj control;
    private GraphicsDevice device;

    public Visor3(GraphicsDevice device, ControlObj control) throws HeadlessException {
        this.control = control;
//        GraphicsConfiguration gc = device.getDefaultConfiguration();

//        mainFrame = new Frame(gc);
        setUndecorated(true);
        setIgnoreRepaint(true);

        device.setFullScreenWindow(this);
        this.device = device;
    }


    public void ver() {
        try {
            Rectangle bounds = getBounds();
            createBufferStrategy(1);
            BufferStrategy bufferStrategy = getBufferStrategy();
            Graphics g = bufferStrategy.getDrawGraphics();
            while (!control.isSalir()){
                if (!bufferStrategy.contentsLost() || control.isActualizar()) {
                    control.setActualizar(false);

                    //todo
                    g.drawImage(control.getFoto(),0 ,0,bounds.width, bounds.height, null);
                    bufferStrategy.show();
                    g.dispose();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            device.setFullScreenWindow(null);
        }
    }

}
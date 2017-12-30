package p.aplic.verfotos.v3;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

/**
 * User: JPB
 * Date: May 23, 2008
 * Time: 3:47:28 PM
 */
public class Visor4 extends Frame{
    private static final Logger logger = Logger.getLogger(Visor4.class);

    private GraphicsDevice device;

    public static void main(String[] args) {
        new Visor4(getDevice(), null);
    }
    static public GraphicsDevice getDevice() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //todo ver monitor
        return  env.getDefaultScreenDevice();
    }

    public Visor4(GraphicsDevice device,
                  KeyListener keyListener) throws HeadlessException {
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        setUndecorated(true);
        setIgnoreRepaint(true);
//        mainFrame.addKeyListener(keyListener);
        addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                System.out.println("666666666666666");
            }
        });
        device.setFullScreenWindow(this);
        this.device = device;
    }

    public void ver() {
        try {
            Rectangle bounds = getBounds();
            createBufferStrategy(1);
            BufferStrategy bufferStrategy = getBufferStrategy();
                    Graphics g = bufferStrategy.getDrawGraphics();
            while (true){
                    if (!bufferStrategy.contentsLost() ) {

                        //todo
                        g.drawRect(0 ,0,20, 20);
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
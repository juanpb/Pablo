package p.pruebas;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: JPB
 * Date: 5/23/11
 * Time: 8:28 AM
 */
public class Antibloqueo {
    private static final Logger logger = Logger.getLogger(Antibloqueo.class);
    private Robot robot = null;

    public static void main(String[] args) throws AWTException {
        new Antibloqueo();

    }

    public Antibloqueo() throws AWTException {
        iniciar();
    }

    private void iniciar() throws AWTException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice sd = ge.getDefaultScreenDevice();

        robot = new Robot();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run(){
                try {

                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    System.out.println(new Date());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        };
        long tiempoEnSeg = (14 * 60) + 10; //14.50

        long tiempoEnMs = tiempoEnSeg * 1000;
        timer.schedule(timerTask, 0, tiempoEnMs);
    }
}

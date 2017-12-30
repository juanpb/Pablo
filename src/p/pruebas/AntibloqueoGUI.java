package p.pruebas;

import org.apache.log4j.Logger;
import p.util.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: JPB
 * Date: 5/23/11
 * Time: 8:28 AM
 */
public class AntibloqueoGUI extends JFrame{
    private static final Logger logger = Logger.getLogger(AntibloqueoGUI.class);
    private Robot robot = null;
    private int x = 10;
    private int y = 10;

    private JButton _btn = new JButton("o");
    private JButton _btnInv = new JButton("Inv");

    public static void main(String[] args) throws AWTException {
        new AntibloqueoGUI();

    }

    public AntibloqueoGUI() throws AWTException {
        iniciar();
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_btnInv, BorderLayout.WEST);
        getContentPane().add(_btn, BorderLayout.CENTER);
        setSize(230, 80);
        _btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("The action have been performed");
            }
        });

        _btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("--**---> " + new Date());
            }



            public void mousePressed(MouseEvent e) {
                System.out.println("-----> " + new Date());
            }
        }
        );

        GUI.centrar(this);
        setVisible(true);
    }

    private void iniciar() throws AWTException {
        init();
        robot = new Robot();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run(){
                try {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_SHIFT);

                    robot.mouseMove(x, y);
                    if (x == 10)
                        x = 110;
                    else
                        x = 10;
                    System.out.println(new Date());
                    _btn.doClick();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        };
        long tiempoEnSeg = (8 * 60) + 10; //
//        long tiempoEnSeg = 4; //

        long tiempoEnMs = tiempoEnSeg * 1000;
        timer.schedule(timerTask, 0, tiempoEnMs);

    }
}

package p.pruebas;

import java.awt.*;

/**
 * User: JPB
 * Date: 5/23/11
 * Time: 8:28 AM
 */
public class Bloquear {
    private Robot robot = null;

    public static void main(String[] args) throws AWTException {
        Bloquear an = new Bloquear();
        an.iniciar();
    }


    private void iniciar() throws AWTException {
        robot = new Robot();
        System.out.println("Dale");
        try { Thread.sleep(3000); } catch        (Exception ex) {}

        robot.mouseMove(350,350);
try { Thread.sleep(1000); } catch (Exception ex) {}
        robot.mouseMove(350,400);
try { Thread.sleep(1000); } catch (Exception ex) {}
        robot.mouseMove(350,450);
try { Thread.sleep(1000); } catch (Exception ex) {}

        System.out.println("mouseMove");
        try { Thread.sleep(1000); } catch (Exception ex) {}


        System.out.println("fin");
    }
}

package p.aplic.verfotos;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Jun 19, 2008
 * Time: 2:19:19 PM
 */
public class Paint extends JComponent {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int anchoAMostrar;
    private int alturaAMostrar;
    private int posY;
    private int posX;
    private Image image;
    private boolean sePinto = true;
    private JFrame frame;

    public Paint() {
        frame = new JFrame("tit");
    }

    public void setParameter(Image imagen, int ancho, int alto, int x, int y){
        this.image = imagen;
        anchoAMostrar = ancho;
        alturaAMostrar = alto;
        posX = x;
        posY = y;
    }

    public synchronized void pintar(){
        frame.setVisible(true);
        /*(sePinto) */{
            sePinto = false;
            repaint();

            //espero hasta que se pinte
            while(!sePinto){
                try {
                    Toolkit.getDefaultToolkit().wait(100);
                } catch (InterruptedException ex) {
                    System.out.println("66666666");
                    return;
                }
            }
        }
    }

    public /*synchronized*/ void paint(Graphics graphics) {
        System.out.println("en paint............................");
        //si la imagen no ocupa toda la pantalla =>
        // primero pinto rectángulos de negro donde corresponda.
        double scW = screenSize.getWidth();
        double scH = screenSize.getHeight();
        if (anchoAMostrar < scW || alturaAMostrar < scH){

            graphics.setColor(Color.black);
            if (posY > 0){
                //pinto rectángulo arriba
                graphics.fillRect(0,0, (int) scW, posY);

                //pinto rectángulo abajo
                graphics.fillRect(0,posY + alturaAMostrar, (int) scW,
                        (int) (scH - posY - alturaAMostrar));
            }

            if (posX > 0){
                //pinto rectángulo a la izq
                graphics.fillRect(0,0, posX, (int) scH);

                //pinto rectángulo a la der
                graphics.fillRect(posX + anchoAMostrar, 0,
                        (int) (scW -  posX - anchoAMostrar), (int) scH);
            }

        }
        graphics.drawImage(image, posX, posY, anchoAMostrar, alturaAMostrar, null);
        graphics.dispose();
        sePinto = true;
    }

    public void setFoto(Image foto) {
        image = foto;
    }
}

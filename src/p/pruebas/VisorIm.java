package p.pruebas;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;
import p.util.GUI;
import p.util.UtilFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * User: JPB
 * Date: May 23, 2008
 * Time: 3:47:28 PM
 */
public class VisorIm extends JFrame {
    private static final Logger logger = Logger.getLogger(VisorIm.class);
    private int alturaAMostrar;
    private int anchoAMostrar;
    private int posX = 0;
    private int posY = 0;

    private int index = 0;
    private long tiempoPorFoto = 4000;
    private long fotosPorFoto = 100;
    private java.util.List<String> fotosAMostrar;
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Map<String, Image> cache = new HashMap<String, Image>();

    private Dimension screenSize = tk.getScreenSize();


    /**
     * Si tamPorc = 0 => la foto se ajusta a la pantalla.
     * Si tamPorc = 1 => la foto se muestra con su tamaño real.
     */
    private double tamPorc = 1.0;

    private Boolean sePinto = false;

    public VisorIm(String path) {
        cargarFotosAMostar(path);

        anchoAMostrar = (int) screenSize.getWidth();
        alturaAMostrar = (int) screenSize.getHeight();

        final VisorIm visorIm = this;
        addWindowListener(new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
              GUI.ocultarMouse(visorIm, false);
              System.exit(0);
            }
		});
		setSize(anchoAMostrar, alturaAMostrar);
        addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                System.exit(0);
            }
        });

        GUI.ocultarMouse(visorIm, true);

        // remove window frame
		setUndecorated(true);

        // switching to fullscreen mode
//		GraphicsEnvironment.getLocalGraphicsEnvironment().
//		    getDefaultScreenDevice().setFullScreenWindow(this);
        setVisible(true);
        java.util.Timer timerPorFoto = new java.util.Timer();
        TimerTask taskMostrarFoto = new TimerTask() {
            public void run() {
                //Modifico los tamaños solo si ya se pintó con el tamaño anterior
                synchronized(sePinto){
                    if (sePinto){
                        repaint();
                        tamPorc -= (1.0 / fotosPorFoto);
                        if (tamPorc < 0){
                            //cambio de foto y reinicio variables
                            index++;
                            if (index >= fotosAMostrar.size()) {
                                index = 0;
                            }
                            tamPorc = 1.0;//reinicio zoom
                        }
                        sePinto = false;
                    }
                }
            }
        };
        repaint();

        timerPorFoto.schedule(taskMostrarFoto,0, tiempoPorFoto / fotosPorFoto);
    }

    private void cargarFotosAMostar(String path){
        String[] ext = new String[]{"jpg", "JPG", "gif", "bmp", "png"};
        DirFileFilter f = new DirFileFilter(false, true, ext, false);
        File file = new File(path);
        fotosAMostrar = UtilFile.archivosAsString(file, f);                
    }

    public void paint(Graphics graphics) {
        synchronized(sePinto){
            if (index < 0){
                System.out.println("-0");
                return ;
            }

            calcularTamaño();

            //si la imagen no ocupa toda la pantalla =>
            // primero pinto la pantalla de negro.
            double scW = screenSize.getWidth();
            double scH = screenSize.getHeight();
            if (anchoAMostrar < scW || alturaAMostrar < scH){

                graphics.setColor(Color.black);
                if (posY > 0){
                    //pinto rectángula arriba
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
            graphics.drawImage(getImagen(), posX, posY, anchoAMostrar, alturaAMostrar, null);
            graphics.dispose();
            sePinto = true;
        }
    }

    private void calcularTamaño(){
        Image im = getImagen();
        int imW = im.getWidth(null);
        int imH = im.getHeight(null);

        double scW = screenSize.getWidth();
        double scH = screenSize.getHeight();
        double proporcion;

        //ajusto x altura
//        if (ajustarAltura)
//            proporcion = scH / imH;
//        else{
//            //ajusto x ancho
//            proporcion = scW / imW;
//        }

        double proporcionH = scH / imH;
        double proporcionW = scW / imW;


        //Calculo la proporción entre el tamaño de la pantalla y la foto
        //si la pantalla es más grande que la foto
        if (proporcionW > 1 && proporcionH > 1){
            //dejo el tamaño original de la foto
            proporcion = 1;
        }
        else{
            //Esta proporción es la que habría que multimplicar al tamaño de la
            //foto para que entre completa en la pantalla (aprovechando al
            // máximo la pantalla sin cortar la foto)
            proporcion = Math.min(proporcionW, proporcionH);
        }

        proporcion += (1 - proporcion) * tamPorc;

        //Si la proporción es = a 1 => la foto no se achica
        //Si la proporción es = a Math.min(proporcionW, proporcionH)
        //  => la foto se ajusta a la pantalla

        anchoAMostrar = (int) (imW * proporcion);
        alturaAMostrar = (int) (imH * proporcion);

        //si la imagen es más grande que la pantalla
        if (imW > scW || imH > scH){
            //y no se está aprovechando la altura ni el ancho
            if (anchoAMostrar < scW && alturaAMostrar < scH){
                //busco el menor de los ajustes que puedo hacer
                double ajusteW = scW / anchoAMostrar;
                double ajusteH = scH / alturaAMostrar;
                double ajuste = Math.min(ajusteH, ajusteW);
                anchoAMostrar *= ajuste;
                alturaAMostrar *= ajuste;
            }
        }

        //centrado
        posX = (int) ((scW - anchoAMostrar)/2);
        posY = (int) ((scH - alturaAMostrar)/2);
    }

    private Image getImagen() {
        String x = fotosAMostrar.get(index);
        Image image = cache.get(x);
        if (image == null){
            try {
                image = javax.imageio.ImageIO.read(new File(x));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            cache.put(x, image);
        }
        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                new VisorIm("D:\\P\\p2\\Fotos\\zoo-2006-02");
             }
        });
	}
}

package p.aplic.verfotos.v2;

import org.apache.log4j.Logger;
import p.aplic.verfotos.Config;
import p.aplic.verfotos.Offset;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.TimerTask;

/**
 * User: JPB
 * Date: May 23, 2008
 * Time: 3:47:28 PM
 */
public class Visor extends JFrame {
    private static final Logger logger = Logger.getLogger(Visor.class);
    private int cantFXFila = 8;
    private int cantFXCol = 8;
    private int anchoXF;
    private int alturaXF;
    private int indice = 0;
    private int indiceDir = 0;
    private java.util.List<File> fotosAMostrarDirActual;
    private Logger log = Logger.getLogger(Visor.class);
    private boolean mostrarYPausar = false;

    private Offset offset = new Offset();
    private Paint paint = new Paint();
    private boolean pausado = false;
    final private Object lock = new Object();
    private Config config;
    private List<String> dirsFotosAMostrar;


    private void init(String path) throws Exception{
        config = new Config(path);
        dirsFotosAMostrar = config.getDirectorios();
        cargarFotosDelDirectorioActual();
        Dimension ss = Paint.screenSize;
        anchoXF = (int) (ss.getWidth() / cantFXFila);
        alturaXF = (int) (ss.getHeight() / cantFXCol);
    }

    public Visor(String path) throws Exception {
        init(path);
        final Visor visorIm = this;
        addListeners();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension ss = tk.getScreenSize();
		setSize((int)ss.getWidth(), (int)ss.getHeight());

        GUI.ocultarMouse(visorIm, true);

        // remove window frame
		setUndecorated(true);
        cargarFotosDelDirectorioActual();

        setVisible(true);
        TimerTask taskMostrarFoto = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        taskMostrarFoto.run();
    }


    private void addListeners() {
        addWindowListener(new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
              System.exit(0);
            }
		});
        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                procesarTecla(e);
            }
        });
    }

    private void procesarTecla(KeyEvent e) {
        synchronized (lock) {
            final int keyCode = e.getKeyCode();
            log.debug("Procesando evento de teclado, keyCode =  " + keyCode);
            if (keyCode == KeyEvent.VK_ESCAPE ){
                log.debug("es escape. Termina el programa");
                System.exit(0);
            }
            else if (keyCode == KeyEvent.VK_P
                        ||keyCode == KeyEvent.VK_SPACE){
                log.debug("es pausa/reanudar");
                pausado = !pausado;
            }
            else if (keyCode == KeyEvent.VK_ADD){
                log.debug("es + zoom");
                mostrarYPausar = true;
                offset.setZoom(true);
            }
            else if (keyCode == KeyEvent.VK_SUBTRACT){
                log.debug("es - zoom");
                mostrarYPausar = true;
                offset.setZoom(false);
            }
            else if (keyCode == KeyEvent.VK_DIVIDE){
                log.debug("es / => se muestra tamaño real");
                mostrarYPausar = true;
                offset.reset();
//                basica.setPorcentaje(1);
            }

            else if (keyCode == KeyEvent.VK_MULTIPLY){
                log.debug("es / => se muestra tamaño real");
                mostrarYPausar = true;
                offset.reset();
//                basica.setPorcentaje(0);
            }
            else if (keyCode == KeyEvent.VK_B){
                log.debug("es B => se muestra info");
//                basica.setMostrarInfo(!basica.getMostrarInfo());
                if (pausado)
                    mostrarYPausar = true; //para que se repinte
            }

            else if (keyCode == KeyEvent.VK_UP){
                log.debug("es UP");
                mostrarYPausar = true;
                offset.setOffsetVertical(false);
            }
            else if (keyCode == KeyEvent.VK_DOWN){
                log.debug("es DOWN");
                mostrarYPausar = true;
                offset.setOffsetVertical(true);
            }
            else if (keyCode == KeyEvent.VK_LEFT){
                log.debug("es IZQ");
                mostrarYPausar = true;
                offset.setOffsetHorizontal(false);
            }
            else if (keyCode == KeyEvent.VK_RIGHT){
                log.debug("es DER");
                mostrarYPausar = true;
                offset.setOffsetHorizontal(true);
            }

//            else if (keyCode == KeyEvent.VK_PAGE_UP){
//                log.debug("es PAGE UP");
//                mostrarYPausar = true;
//                apuntarAProximaFoto(true);
//
//                try {
//                    cargarFoto();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//            else if (keyCode == KeyEvent.VK_PAGE_DOWN){
//                log.debug("es PAGE DOWN");
//                mostrarYPausar = true;
//                try {
//                    cargarFoto();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
        }
    }



    private void cargarFotosDelDirectorioActual()  {
        String[] ext = config.getExtensiones().toArray(new String[0]);
        log.debug("Se cargarán fotos con las extensiones: " + ext);
        DirFileFilter filtro = new DirFileFilter(false, true, ext, false);
        String dir = dirsFotosAMostrar.get(indiceDir);
        try {
            fotosAMostrarDirActual = UtilFile.archivos(dir, filtro, config.isRecursivo());
            log.debug("Se mostrarán las fotos del directorio: " + dir + ", que tiene " +
                    fotosAMostrarDirActual.size() + " fotos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    public void paint(Graphics graphics) {
        System.out.println("fotosAMostrarDirActual.size() = " + fotosAMostrarDirActual.size());
        System.out.println("indice = " + indice);
        if (indice >= fotosAMostrarDirActual.size())
            return ;
        log.debug("En paint");
        paint.pintarFondo(graphics);
        //basica.pintar(graphics);
        while(true){
            for(int f = 0; f < cantFXFila; f++) {
                for(int c = 0; c < cantFXCol; c++) {
                    paint.setFoto(getImagen());
                    indice++;
                    if (indice >= fotosAMostrarDirActual.size())
                        indice = 0;
                    paint.setPosicion(anchoXF * c, alturaXF * f, anchoXF, alturaXF);
                    paint.pintar(graphics);
                }
            }
        }
//        graphics.dispose();
    }


    private Image getImagen() {
        File file = fotosAMostrarDirActual.get(indice);

        log.debug("Se cargará nueva foto: " + file.getAbsolutePath());

        try {
            return javax.imageio.ImageIO.read(file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static void main(final String[] args) {
        if (args.length != 1){
            System.out.println("Falta pasar el config como parámetro");
            return ;
        }
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 try {
                     new Visor(args[0]);
                 } catch (IOException e) {
                     logger.error(e.getMessage(), e);
                 } catch (Exception e) {
                     logger.error(e.getMessage(), e);
                 }
             }
        });
	}

    public String getInfo() {
        File f = fotosAMostrarDirActual.get(indice);
        String tam = new BigDecimal(f.length() / 1024.0 / 1024.0).toString();
        tam = tam.substring(0, tam.indexOf(".") + 3) + "MB";
        return f.getAbsolutePath() + "   |   " + tam + "   |   " + (indice+1) + "/"+ fotosAMostrarDirActual.size();
    }
}
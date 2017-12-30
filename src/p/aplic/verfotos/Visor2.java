package p.aplic.verfotos;

import org.apache.log4j.Logger;
import p.aplic.verfotos.estrVisul.AjustarAAncho;
import p.aplic.verfotos.estrVisul.AjustarAPantalla;
import p.aplic.verfotos.estrVisul.Basica;
import p.pruebas.Antibloqueo;
import p.util.DirFileFilter;
import p.util.GUI;
import p.util.Util;
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
import java.util.Collections;
import java.util.TimerTask;

/**
 * User: JPB
 * Date: May 23, 2008
 * Time: 3:47:28 PM
 *
 * Funciona bien el tamaño real a pantalla 03/mar/2010
 */
public class Visor2 extends JFrame {
    private static final Logger logger = Logger.getLogger(Visor2.class);
    //todo
    /*
    _agregar parámetro "esperar_al_final" para que la foto se quede congelada cuando se está viendo completa
    _que no repinte si no cambia
    _eventos del mouse (ruedita)
    * */

    private int indice = 0;
    private int indiceDir = 0;
    private int nroFrameAMostar = 0;
    private java.util.List<String> dirsFotosAMostrar;
    private java.util.List<File> fotosAMostrarDirActual;


    private Offset offset = new Offset();
    private Boolean sePinto = true;
    private Basica basica;
    private Config config;
    private boolean pausado = false;
    private boolean mostrarYPausar = false;
    final private Object lock = new Object();

    private Logger log = Logger.getLogger(Visor2.class);
    private boolean yaSeMostroLaUltima = false;

    private void init(String path) throws Exception{
        config = new Config(path);
//        abrirConfig();
        System.out.println("config.getExtensiones() = " + config.getExtensiones());
        basica = getModoVisualizacion();
        basica.setOffset(offset);

        basica.setConfig(config);
        dirsFotosAMostrar = config.getDirectorios();
        cargarFotosDelDirectorioActual();
        cargarFoto();
        if (config.isUsarAntibloqueo())
            new Antibloqueo();
    }

    private void abrirConfig() {
        final ConfigEditor editor = new ConfigEditor(config);
        editor.setModal(true);
        GUI.centrar(editor);
        editor.setVisible(true);
        try {
            editor.setVisible(false);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage());
            e2.printStackTrace();
        }
    }

    public Visor2(String path) throws Exception {
        init(path);
        final Visor2 visorIm = this;
        addListeners();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension ss = tk.getScreenSize();
		setSize((int)ss.getWidth(), (int)ss.getHeight());

        GUI.ocultarMouse(visorIm, true);

        // remove window frame
		setUndecorated(true);

        long framesPorSegundo = config.getFramesPorSegundo();
        long tiempoPorFoto = config.getTiempoPorFoto();
        double framesPorFoto = (double) framesPorSegundo * (double) tiempoPorFoto/ 1000.0d;
        final double porcAEstirarPorFoto = 1.0d / framesPorFoto;

        log.debug("framesPorSegundo: " + framesPorSegundo);
        log.debug("tiempoPorFoto: " + tiempoPorFoto);
        log.debug("framesPorFoto: " + framesPorFoto);
        log.debug("porcAEstirarPorFoto: " + porcAEstirarPorFoto);
        
        log.debug("IntervaloDeTiempo x frame: " + basica.getIntervaloDeTiempo());

        setVisible(true);
        java.util.Timer timerPorFoto = new java.util.Timer();
        TimerTask taskMostrarFoto = new TimerTask() {
            public void run() {
                //Modifico los tamaños solo si ya se pintó con el tamaño anterior
                {
                    //log.debug("en run");
                    synchronized (lock) {
                        if (yaSeMostroLaUltima)
                            return;
                        if (sePinto && (!pausado || mostrarYPausar)){
                            log.debug("en run, se mostrará un nuevo frame");
                            if (!pausado)
                                try {
                                    cargarFoto();
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                }
//                                calcularParaDeRealAPantalla(porcAEstirarPorFoto);
                            //else =>  mostrarYPausar. En este caso ya se ajustó el
                            //tamaño de la fotos según el evento ocurrido.
                            sePinto = false;
                            repaint();
                            if (mostrarYPausar){
                                pausado = true;
                                mostrarYPausar = false;
                            }
                        }
                        else{
                            String msg = "en run, no se mostrará nueva imagen porque ";
                            if (!sePinto)
                                msg += "no se pintó la anterior";
                            else
                                msg += "está pausado";

                            log.debug(msg);
                        }
                    }
                }
            }
        };
        timerPorFoto.schedule(taskMostrarFoto,0, basica.getIntervaloDeTiempo());
    }

    private void calcularParaDeRealAPantalla(double porcAEstirarPorFoto) {
        double b = porcAEstirarPorFoto * nroFrameAMostar;
        double porc = 1.0d - b;//porcentaje va de 1 hasta 0
        if (porc < 0){
            //cambio de foto y reinicio variables
            try {
                cargarFoto();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            porc = 1.0d;//reinicio zoom
        }
        log.debug("Porcentaje de zoom a aplicar: " + porc);
        basica.setPorcentaje(porc);
        //     * Si porcentaje = 0 => la foto se ajusta a la pantalla.
        //     * Si porcentaje = 1 => la foto se muestra con su tamaño real.
        nroFrameAMostar++;
        log.debug("nroFrameAMostar = " + nroFrameAMostar);
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
              salir();
            }
		});
        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                procesarTecla(e);
            }
        });
    }

    private void bloquear() {
        Util.bloquearPC();
        salir();
    }
    private void salir() {
        System.exit(0);
    }

    private void procesarTecla(KeyEvent e) {
        synchronized (lock) {

            if (config.isBloquearPc()){
                System.out.println("procTec:  " + e.getKeyCode());
                System.out.println("e.getKeyChar() = " + e.getKeyChar());
                System.out.println("e.paramString() = " + e.paramString());
                System.out.println("e.toString() = " + e.toString());
                if (e.getKeyCode() != 16) //es el shift del "antibloqueo"
                    bloquear();
            }

            final int keyCode = e.getKeyCode();
            log.debug("Procesando evento de teclado, keyCode =  " + keyCode);
            if (keyCode == KeyEvent.VK_ESCAPE ){
                log.debug("es escape. Termina el programa");
                salir();
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
                basica.setPorcentaje(1);
            }

            else if (keyCode == KeyEvent.VK_MULTIPLY){
                log.debug("es / => se ajusta a pantalla");
                offset.reset();
                basica.setPorcentaje(0);
            }
            else if (keyCode == KeyEvent.VK_B){
                log.debug("es B => se muestra info");
                basica.setMostrarInfo(!basica.getMostrarInfo());
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

            else if (keyCode == KeyEvent.VK_PAGE_UP){
                log.debug("es PAGE UP");
                mostrarYPausar = true;
                apuntarAProximaFoto(true);

                try {
                    cargarFoto();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if (keyCode == KeyEvent.VK_PAGE_DOWN){
                log.debug("es PAGE DOWN");
                mostrarYPausar = true;
                try {
                    cargarFoto();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void cargarFoto() throws Exception {
        offset.reset();
        nroFrameAMostar = 0;
        basica.setFoto(getImagen());
        basica.setInfo(getInfo());
        apuntarAProximaFoto();
    }

    private void apuntarAProximaFoto() throws Exception {
        apuntarAProximaFoto(false);
    }

    private void apuntarAProximaFoto(boolean retroceder)  {
        if (yaSeMostroLaUltima)
            return ;
        
        log.debug("Se apunta a la próxima foto");
        if (retroceder){
            log.debug("se retrocede el índice de fotos");
            indice--;
            revisarCambioCarpeta();
            indice--;//retrocedo dos veces ya que siempre se apunta a la próxima
            revisarCambioCarpeta();            
        }else{
            indice++;
            if (indice >= fotosAMostrarDirActual.size()) {
                indiceDir++;
                log.debug("se cambia de carpeta");
                if (indiceDir >= dirsFotosAMostrar.size()){
                    if (config.isRepetir()){
                        indiceDir = 0;
                        log.debug("se repetirán las carpetas");
                    }
                    else{
                        log.debug("Ya se mostraron todas las carpetas y no se repite. Chau");
                        yaSeMostroLaUltima = true;
                        pausado = true;
                    }
                }
                cargarFotosDelDirectorioActual();
                indice = 0;
            }
        }
    }

    private void revisarCambioCarpeta() {
        if (indice < 0) {
            //apunto a la última del directorio anterior
            log.debug("se pasa el índice a la carpeta anterior");
            indiceDir--;
            if (indiceDir < 0){
                log.debug("se pasa de la primera carpeta a la última");
                //si estaba en el primer directorio => apunto al último
                indiceDir = dirsFotosAMostrar.size() - 1;
            }
            cargarFotosDelDirectorioActual();
            indice = fotosAMostrarDirActual.size() - 1;
        }
    }

    private void cargarFotosDelDirectorioActual()  {
        if (yaSeMostroLaUltima)
            return ;

        String[] ext = config.getExtensiones().toArray(new String[0]);
        log.debug("Se cargarán fotos con las extensiones: " + ext);
        DirFileFilter filtro = new DirFileFilter(false, true, ext, false);
        String dir = dirsFotosAMostrar.get(indiceDir);
        try {
            fotosAMostrarDirActual = UtilFile.archivos(dir, filtro, config.isRecursivo());
            if (config.isMostrarMezclado())
                Collections.shuffle(fotosAMostrarDirActual);

            log.debug("Se mostrarán las fotos del directorio: " + dir + ", que tiene " +
                    fotosAMostrarDirActual.size() + " fotos.");
            if (config.isPosicionDeInicioAleatoria()){
                indice = (int)(Math.random() * fotosAMostrarDirActual.size());
            }
            log.debug("Arranca desde la posición: " + indice);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private Basica getModoVisualizacion(){
        if (config.getModoVisualizacion() == ModoVisualizacion.AJUSTAR_A_ANCHO)
            return new AjustarAAncho();
        else if (config.getModoVisualizacion() == ModoVisualizacion.AJUSTAR_A_PANTALLA)
            return new AjustarAPantalla();
        else
            return new Basica();
    }

    public void paint(Graphics graphics) {
        log.debug("En paint");
        basica.calcularSize();
        basica.pintar(graphics);
        sePinto = true;
        setVisible(true);
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
                     new Visor2(args[0]);
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
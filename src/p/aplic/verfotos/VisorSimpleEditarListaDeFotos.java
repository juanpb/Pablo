package p.aplic.verfotos;

import org.apache.log4j.Logger;
import p.aplic.verfotos.estrVisul.Basica;
import p.util.GUI;
import p.util.Util;
import p.util.UtilFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Carga lista de fotos de un archivo (seteado en el config)
 * Si se apreta "delete" elimina el archivo de la lista
 * Al apretar Escape, imprime la lista de las fotos vistas (sin las borradas) y el nombre de la primera foto aún no vista
 *
 */
public class VisorSimpleEditarListaDeFotos extends JFrame {
    private static final Logger logger = Logger.getLogger(VisorSimpleEditarListaDeFotos.class);
    //todo
    /*
    _agregar parámetro "esperar_al_final" para que la foto se quede congelada cuando se está viendo completa
    _que no repinte si no cambia
    * */

    private int indiceFotos = 0; //es el índice de la próxima foto a mostrar
    private List<File> fotosAMostrar;

    private boolean pausado = false;

    private Offset offset = new Offset();
    private Basica basica;
    private Config config;
    final private Object lock = new Object();

    private Logger log = Logger.getLogger(VisorSimpleEditarListaDeFotos.class);
    private Timer timerSlideShow;
    private boolean primeraFotoMostrada = false;

    private void init(Config config) throws Exception{
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device;
        if (env.getScreenDevices().length > config.getNumeroDeMonitor())
            device = env.getScreenDevices()[config.getNumeroDeMonitor()];
        else
            device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(this);

        Dimension ss = device.getFullScreenWindow().getSize();

        setSize((int)ss.getWidth(), (int)ss.getHeight());

        //todo ocultar mouse
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame.
        getContentPane().setCursor(blankCursor);

        setVisible(true);

        basica = getModoVisualizacion();
        basica.setScreenSize(ss);
        basica.setOffset(offset);

        basica.setConfig(config);
        cargarFotos();

        basica.setPorcentaje(0.0d);

        mostrarFotoActual();
    }

    private void init(String path) throws Exception{
        config = new Config(path);
        init(config);
    }

    public VisorSimpleEditarListaDeFotos(String path) throws Exception {
        // remove window frame
		setUndecorated(true);
        addListeners();
        init(path);
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
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (config.isBloquearPc()){
                    System.out.println("Se bloquea xq perdió foco");
                    System.out.println("new Date() = " + new Date());
                    bloquear();
                }

//                requestFocus();
//                mostrarFotoActual();
            }
        });
        addMouseWheelListener(new MouseWheelListener(){
             public void mouseWheelMoved(MouseWheelEvent e) {
                 try {
                     procesarMouseWheel(e);
                 } catch (Exception e1) {
                     log.error(e1);
                 }
             }
        });
        addMouseListener(new MouseAdapter(){
             public void mouseClicked(MouseEvent e) {
                 procesarMouseClicked(e);
             }
        });
    }

    private void procesarMouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem itemSlideShow;
            itemSlideShow = new JMenuItem("Slide show");
            itemSlideShow.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                slideShow();
                }
            });
            popupMenu.add(itemSlideShow);
            popupMenu.show(this, e.getX(),  e.getY());
        }
    }

    private void abrirConfig() {
        final ConfigEditor editor = new ConfigEditor(config);
        editor.setModal(true);
        GUI.centrar(editor);
        editor.setVisible(true);
        try {
            init(config);
            editor.setVisible(false);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage());
            log.error(e2);
        }
        if (config.isArrancarSlideShow())
            slideShow();
    }

    private void slideShow() {
        if (timerSlideShow != null){//ya está corriendo => solo pongo pause en false
            pausado = false;
            return ;
        }

        timerSlideShow = new Timer();
        TimerTask taskMostrarFoto = new TimerTask() {
            public void run(){
                try {
                    if (!pausado){
                        if (primeraFotoMostrada)
                            apuntarAProximaFoto();
                        else
                            primeraFotoMostrada = true;
                        mostrarFotoActual();
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
        };
        timerSlideShow.schedule(taskMostrarFoto, 0, config.getTiempoPorFoto());
    }

    private void procesarMouseWheel(MouseWheelEvent e) throws Exception {
        boolean repintar = false;
        int wheelRotation = e.getWheelRotation();

        if (wheelRotation < 0){
            log.debug("es Mouse_arriba");   //muestro foto previa
            apuntarAProximaFoto(true);
            mostrarFotoActual();
        }
        else if (wheelRotation > 0){
            log.debug("es Mouse_abajo"); //muestro próxima foto
            apuntarAProximaFoto();
            mostrarFotoActual();
        }
    }

    private void bloquear() {
        Util.bloquearPC();
        salir();
    }
    private void salir() {
        System.exit(0);
    }
    private void grabarListaDeFotos() {
        List<String> out = new ArrayList<String>();
        File fotoAVer = fotosAMostrar.get(indiceFotos);
        String fotoAVerString = fotoAVer.getAbsolutePath();
        out.add("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        out.add("Visto hasta foto: " );
        out.add(fotoAVer.getAbsolutePath());
        out.add("");
        out.add("Lista de fotos que quedaron: ");
        for (int i = 0; i < indiceFotos; i++) {
            File foto = fotosAMostrar.get(i);
            out.add(foto.getAbsolutePath());

        }
        try {
            UtilFile.guardartArchivoPorLinea("C:\\Datos\\P\\java\\codigo\\Pablo\\config\\verFotos\\out.txt", out);
        } catch (IOException e) {
            e.printStackTrace();
            for (String anOut : out) {
                System.out.println(anOut);
            }
        }
        System.exit(0);
    }

    private void procesarTecla(KeyEvent e) {
        synchronized (lock) {
            final int keyCode = e.getKeyCode();

            //para que no se bloquee
            if (e.isShiftDown()
                    && e.isAltDown()
                    && e.isControlDown()
                    && keyCode == KeyEvent.VK_F12){
                salir();
            }


            log.debug("Procesando evento de teclado, keyCode =  " + keyCode);
            if (keyCode == KeyEvent.VK_ESCAPE ){
                log.debug("es escape. Termina el programa");
                grabarListaDeFotos();
                salir();
            }
            else if (keyCode == KeyEvent.VK_P){
                log.debug("es pausa/reanudar");
                pausado = !pausado;
            }
            else if (keyCode == KeyEvent.VK_ADD){
                log.debug("es + zoom");
                offset.setZoom(true);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_SUBTRACT){
                log.debug("es - zoom");
                offset.setZoom(false);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_DIVIDE){
                log.debug("es / => se muestra tamaño real");
                offset.reset();
                basica.setPorcentaje(1);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_MULTIPLY){
                log.debug("es / => se ajusta a pantalla");
                offset.reset();
                basica.setPorcentaje(0);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_B){
                log.debug("es B => se muestra info");
                basica.setMostrarInfo(!basica.getMostrarInfo());
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_UP){
                log.debug("es UP");
                offset.setOffsetVertical(false);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_DOWN){
                log.debug("es DOWN");
                offset.setOffsetVertical(true);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_LEFT){
                log.debug("es IZQ");
                offset.setOffsetHorizontal(false);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_RIGHT){
                log.debug("es DER");
                offset.setOffsetHorizontal(true);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_F12){
                log.debug("es F12");
                slideShow();
            }
            else if (keyCode == KeyEvent.VK_F11){
                log.debug("es F11");
                abrirConfig();
            }
            else if (keyCode == KeyEvent.VK_PAGE_UP){
                log.debug("es PAGE UP"); //retrocedo
                apuntarAProximaFoto(true);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_PAGE_DOWN || keyCode == KeyEvent.VK_SPACE ){
                log.debug("es PAGE DOWN");
                apuntarAProximaFoto();
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_END){
                indiceFotos = fotosAMostrar.size() -1;
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_HOME){
                indiceFotos = 0;
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_DELETE){
                System.out.println("Se borrará la foto nro: " + indiceFotos);
                fotosAMostrar.remove(indiceFotos);
                mostrarFotoActual();
            }
            else if (keyCode == KeyEvent.VK_DELETE){
                System.out.println("Se borrará la foto nro: " + indiceFotos);
                File file = fotosAMostrar.get(indiceFotos);
                fotosAMostrar.remove(indiceFotos);
                mostrarFotoActual();
            }
        }
    }



    private void mostrarFotoActual(){
        Image imagen = getImagen();
        basica.setFoto(imagen);
        basica.setInfo(getInfo());
        repaint();
    }

    private void apuntarAProximaFoto() {
        apuntarAProximaFoto(false);
    }

    private void apuntarAProximaFoto(boolean retroceder)  {
        offset.reset();
        log.debug("Se apunta a la próxima foto");
        if (retroceder){
            log.debug("se retrocede el índice de fotos");
            indiceFotos--;
           // indiceFotos--;//retrocedo dos veces ya que siempre se apunta a la próxima
            if (indiceFotos < 0){
                indiceFotos = 0;
                if (config.isMostrarMezclado())
                    Collections.shuffle(fotosAMostrar);
            }
        }else{
            indiceFotos++;
            if (indiceFotos >= fotosAMostrar.size()) {
                if (config.isRepetir()){
                    log.info("se repetirán las fotos");
                    indiceFotos = 0;
                    if (config.isMostrarMezclado())
                        Collections.shuffle(fotosAMostrar);
                }else{
                    log.debug("Ya se mostraron todas las fotos y no se repite.");
                    indiceFotos--;//retrocedo para que siga mostrando la última
                }
            }
        }
    }

    private void cargarFotos()  {
        log.info("Se cargarán fotos del archivo: " + config.getDirectorios());
        try {
            List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(config.getDirectorios().get(0), true);
            fotosAMostrar = new ArrayList<File>();
            for (String ar : archivoPorLinea) {
                File e = new File(ar);
                fotosAMostrar.add(e);
            }

            if (config.isMostrarMezclado())
                Collections.shuffle(fotosAMostrar);

            log.info("Arranca desde la posición: " + indiceFotos);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e.getMessage());
            log.error(e);
        }
    }

    private List<File> filtrar(List<File> fotos) {
        List<File> res = new ArrayList<File>();
//        System.out.println("No pasan: ");
        for(File foto : fotos) {
            if (pasaFiltro(foto))
                res.add(foto);
        }

        return res;
    }


    private Basica getModoVisualizacion(){
//        if (config.getModoVisualizacion() == ModoVisualizacion.AJUSTAR_A_ANCHO)
//            return new AjustarAAncho();
//        else if (config.getModoVisualizacion() == ModoVisualizacion.TAMANO_REAL_A_PANTALLA)
//            return new RealAPantalla(config, this);
//        else
            return new Basica();
    }

    public void paint(Graphics graphics) {
        log.debug("En paint");
        if (basica != null){
            basica.calcularSize();
            basica.pintar(graphics);
        }
    }

    private Image getImagen() {
        File file = fotosAMostrar.get(indiceFotos);
        log.debug("Se cargará nueva foto: " + indiceFotos + " - " + file.getAbsolutePath());

        try {
            return javax.imageio.ImageIO.read(file);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }

    private boolean pasaFiltro(File file){
        List<String> filtros = config.getFiltroFotos();
        if (filtros == null || filtros.size() == 0)
            return !file.isDirectory();
        if (!config.isTipoFiltroOr())
            return pasaFiltroAND(file);

        String name = file.getName().toLowerCase();
        for(String filtro : filtros) {
            if (name.contains(filtro.toLowerCase()))
                return !file.isDirectory();
        }
        return false;
    }
    private boolean pasaFiltroAND(File file){
        List<String> filtros = config.getFiltroFotos();
        if (filtros == null || filtros.size() == 0)
            return !file.isDirectory();

        String name = file.getName().toLowerCase();
        for(String filtro : filtros) {
            if (!name.contains(filtro.toLowerCase()))
                return false;
        }
        return true;
    }

    public static void main(final String[] args) {
        if (args.length != 1){
            System.out.println("Falta pasar el config como parámetro");
            return ;
        }
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 try {
                     new VisorSimpleEditarListaDeFotos(args[0]);
                 } catch (IOException e) {
                     logger.error(e.getMessage(), e);
                 } catch (Exception e) {
                     logger.error(e.getMessage(), e);
                 }
             }
        });
	}

    private  String getInfo() {
        File f = fotosAMostrar.get(indiceFotos);
        String tam = new BigDecimal(f.length() / 1024.0 / 1024.0).toString();
        tam = tam.substring(0, tam.indexOf(".") + 3) + "MB";
        return f.getAbsolutePath() + "   |   " + tam + "   |   " + (indiceFotos +1) + "/"+ fotosAMostrar.size();
    }
}
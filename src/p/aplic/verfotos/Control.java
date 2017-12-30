package p.aplic.verfotos;

import p.aplic.verfotos.estrVisul.RealAPantalla;
import p.util.DirFileFilter;
import p.util.GUI;
import p.util.UtilFile;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * User: JPB
 * Date: Jun 20, 2008
 * Time: 9:58:36 AM
 */
public class Control {
    private Config config;
    private boolean seguir = true;
    private JFrame frame = new JFrame();
    private RealAPantalla realAPantalla;

    public Control(Config config) {
        this.config = config;
        init();
    }

    private void init(){
        if (!validarConfig())
            throw new RuntimeException("Error");

        frame.setSize(100, 100);
//        addKeyListener(new KeyAdapter(){
//            public void keyTyped(KeyEvent e) {
//                System.exit(0);
//            }
//        });
        GUI.ocultarMouse(frame, true);

        // remove window frame
		frame.setUndecorated(true);
        realAPantalla = new RealAPantalla(config, frame);

        List<String> dirs = config.getDirectorios();
        int i = 0;
        while (seguir){
            String dir = dirs.get(i++);
            mostrarDir(dir);
            if (i >= dirs.size()){
                if (config.isRepetir())
                    i = 0;
                else
                    seguir = false;
            }
        }
    }

    private void mostrarDir(String dir){
        String[] ext = config.getExtensiones().toArray(new String[0]);

        DirFileFilter filtro = new DirFileFilter(false, true, ext, false);
        try {
            List<File> files = UtilFile.archivos(dir, filtro, config.isRecursivo());
            for (File f : files) {
                mostrar(f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrar(File f) {
        //todo: factory
        realAPantalla.mostrar();
    }


    //devuelve false si tiene errores
    private boolean validarConfig(){
        boolean sinError = true;
        if (config.getDirectorios().size() == 0){
            System.out.println("Error en config. Falta definir directorios");
            sinError = false;
        }
        if (config.getExtensiones() == null || config.getExtensiones().size() == 0){
            System.out.println("Error en config. Falta definir extensiones");
            sinError = false;
        }
        if (config.getFramesPorSegundo() == 0){
            System.out.println("Error en config. Falta definir Fotos_por_segundo");
            sinError = false;
        }
        if (config.getModoVisualizacion() == null){
            System.out.println("Error en config. Falta definir Modo_Visualizacion");
            sinError = false;
        }
        if (config.getTiempoPorFoto() == 0){
            System.out.println("Error en config. Falta definir Tiempo_Por_Foto");
            sinError = false;
        }
        return sinError;
    }
}

package p.aplic.verfotos.v3;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;
import p.util.UtilFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * User: JPB
 * Date: Aug 13, 2009
 * Time: 12:44:25 PM
 */
public class Control implements ChangeListener {
    private Logger log = Logger.getLogger(Control.class);

    private Visor3 visor;
    private Config config;
    private int indiceDir = 0;
    private int indice = 0;
    private List<File> fotosAMostrarDirActual;
    private ControlObj controlObj;

    public Control(Visor3 visor, Config config, ControlObj controlObj) {
        this.visor = visor;
        this.config = config;
        this.controlObj = controlObj;
        TecladoAdmin tecladoAdmin = new TecladoAdmin(controlObj);
        tecladoAdmin.addChangeListener(this);
        visor.addKeyListener(tecladoAdmin);
    }

    public void empezar(){
        cargarFotosDelDirectorioActual();
        cargarFoto();
        visor.ver();
        System.out.println("Control.empezar() -> fin");
    }

    private void cargarFoto()  {
//        offset.reset();
//        nroFrameAMostar = 0;
        controlObj.setFoto(getImagen());
//        basica.setInfo(getInfo());
    }

    private Image getImagen() {
        File file = fotosAMostrarDirActual.get(indice);

        log.debug("Se cargará nueva foto: " + file.getAbsolutePath());

        try {
            return javax.imageio.ImageIO.read(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private void apuntarAProximaFoto() {
        apuntarAProximaFoto(false);
    }

    private void apuntarAProximaFoto(boolean retroceder)  {
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
                if (indiceDir >= config.getDirectorios().size()){
                    if (config.isRepetir()){
                        indiceDir = 0;
                        log.debug("se repetirán las carpetas");
                    }
                    else{
                        log.debug("Ya se mostraron todas las carpetas y no se repite. Chau");
                        System.exit(0);
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
                indiceDir =  config.getDirectorios().size() - 1;
            }
            cargarFotosDelDirectorioActual();
            indice = fotosAMostrarDirActual.size() - 1;
        }
    }

    private void cargarFotosDelDirectorioActual()  {
        String[] ext = config.getExtensiones().toArray(new String[0]);
        log.debug("Se cargarán fotos con las extensiones: " + ext);
        DirFileFilter filtro = new DirFileFilter(false, true, ext, false);
        String dir = config.getDirectorios().get(indiceDir);
        try {
            fotosAMostrarDirActual = UtilFile.archivos(dir, filtro, config.isRecursivo());
            log.debug("Se mostrarán las fotos del directorio: " + dir + ", que tiene " +
                    fotosAMostrarDirActual.size() + " fotos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (controlObj.isMostrarProxima()){
            apuntarAProximaFoto();
            cargarFoto();
        }
        else if (controlObj.isMostrarPrevia()){
            apuntarAProximaFoto(false);
            cargarFoto();
        }
    }
}

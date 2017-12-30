package p.aplic.verfotos.v3;

import org.apache.log4j.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Aug 11, 2009
 * Time: 3:06:30 PM
 */
public class TecladoAdmin extends KeyAdapter {
    final private Object lock = new Object();
    private Logger log = Logger.getLogger(TecladoAdmin.class);
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private ControlObj control;

    public TecladoAdmin(ControlObj controlObj) {
        control = controlObj;
    }

    public void keyPressed(KeyEvent e) {
        procesarTecla(e);
    }

    private void procesarTecla(KeyEvent e) {
        System.out.println("pt");
        synchronized (lock) {
            final int keyCode = e.getKeyCode();
            log.debug("Procesando evento de teclado, keyCode =  " + keyCode);
            if (keyCode == KeyEvent.VK_ESCAPE ){
                log.debug("es escape. Termina el programa");
                control.setSalir(true);//todo System.exit(0);
            }
            else if (keyCode == KeyEvent.VK_P
                        ||keyCode == KeyEvent.VK_SPACE){
                log.debug("es pausa/reanudar");
                control.setPausar(!control.isPausar());
            }
            else if (keyCode == KeyEvent.VK_ADD){
                log.debug("es + zoom");
                control.setMostrarYPausar( true);
                control.getOffset().setZoom(true);
            }
//            else if (keyCode == KeyEvent.VK_SUBTRACT){
//                log.debug("es - zoom");
//                mostrarYPausar = true;
//                offset.setZoom(false);
//            }
//            else if (keyCode == KeyEvent.VK_DIVIDE){
//                log.debug("es / => se muestra tamaño real");
//                mostrarYPausar = true;
//                offset.reset();
//                basica.setPorcentaje(1);
//            }
//
//            else if (keyCode == KeyEvent.VK_MULTIPLY){
//                log.debug("es / => se muestra tamaño real");
//                mostrarYPausar = true;
//                offset.reset();
//                basica.setPorcentaje(0);
//            }
//            else if (keyCode == KeyEvent.VK_B){
//                log.debug("es B => se muestra info");
//                basica.setMostrarInfo(!basica.getMostrarInfo());
//                if (pausado)
//                    mostrarYPausar = true; //para que se repinte
//            }
//
//            else if (keyCode == KeyEvent.VK_UP){
//                log.debug("es UP");
//                mostrarYPausar = true;
//                offset.setOffsetVertical(false);
//            }
//            else if (keyCode == KeyEvent.VK_DOWN){
//                log.debug("es DOWN");
//                mostrarYPausar = true;
//                offset.setOffsetVertical(true);
//            }
//            else if (keyCode == KeyEvent.VK_LEFT){
//                log.debug("es IZQ");
//                mostrarYPausar = true;
//                offset.setOffsetHorizontal(false);
//            }
//            else if (keyCode == KeyEvent.VK_RIGHT){
//                log.debug("es DER");
//                mostrarYPausar = true;
//                offset.setOffsetHorizontal(true);
//            }
//
            else if (keyCode == KeyEvent.VK_PAGE_UP){
                log.debug("es PAGE UP");
                control.setMostrarProxima(true);
            }
            else if (keyCode == KeyEvent.VK_PAGE_DOWN){
                log.debug("es PAGE DOWN");
                control.setMostrarPrevia(true);
            }
            if (control.isActualizar())
                dispararChange();
        }
    }

    private  void dispararChange(){
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(e);
        }
    }

    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }

}

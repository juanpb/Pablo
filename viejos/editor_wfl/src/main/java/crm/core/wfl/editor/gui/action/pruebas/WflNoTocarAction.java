package crm.core.wfl.editor.gui.action.pruebas;

import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.cmp.WflState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflNoTocarAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static String title = "NO TOCAR";
    public static final int IZQ = 0;
    public static final int DER = 1;
    private WflContainerControl control = null;

    public WflNoTocarAction(WflContainerControl control) {
        this.control = control;
        init(title);
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, WflNoTocarAction.title);

        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, ks);
    }

    boolean parar = false;
    List<M> cs = new ArrayList<M>();

    public void actionPerformed(ActionEvent e) {
        if (parar){
            parar = !parar;
            for (int i = 0; i < cs.size(); i++) {
                M m = (M) cs.get(i);
                m.parar();
            }
            return ;
        }
        parar = !parar;
        int w = control.getContainer().getWidth();
        List<WflComponent> wc = control.getContainer().getWflComponents(WflState.class);
        int dir = 0;
        wc.addAll(control.getContainer().getWflComponents(WflFunction.class));
        for (int i = 0; i < wc.size(); i++) {
            WflComponent c = (WflComponent) wc.get(i);
            M m = new M(c, dir, w);
            cs.add(m);
            Thread t = new Thread(m);
            t.start();
            dir++;
            if (dir > 1)
                dir = 0;
        }
    }
}

class M implements Runnable{
    WflComponent c;
    int dir;
    int ancho;
    int dif = 26;
    int w;
    Point locOriginal;
    boolean seguir = true;
    public M(WflComponent c, int dir, int ancho){
        this.c = c;
        w = c.getWidth();
        locOriginal = c.getLocation();
        this.dir = dir;
        this.ancho = ancho;
        dif = (int) (dif * Math.random());
    }

    public void parar(){
        seguir = false;
        c.setPosition(locOriginal);
    }

    public void run() {

        while(seguir){
            long ini = System.currentTimeMillis();

            Point loc = c.getLocation();
            if (dir == WflNoTocarAction.DER){
                loc.translate(dif, 0);
                if (loc.x + w > ancho){
                    dir = WflNoTocarAction.IZQ;
                    loc.translate(-dif, 0);
                }
            }else{
                loc.translate(-dif, 0);
                if (loc.x < 0){
                    dir = WflNoTocarAction.DER;
                    loc.translate(dif, 0);
                }
            }
            c.setNewPosition(loc);
            do {
                Thread.yield();
            } while (System.currentTimeMillis()-ini< 67);
        }
    }
}

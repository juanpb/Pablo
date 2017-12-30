package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflMoveComponentAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private static final String name = "MOVE_COMPONENT";

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private WflContainerControl control = null;
    private int moveTo = -1;
    private int distance = 10;


    /**
     *
     * @param control
     * @param moveTo indica si es un alta, baja o modificación.
     *  Valores posibles: TYPE_CREATE, TYPE_EDIT, TYPE_REMOVE
     * @param showText
     */
    public WflMoveComponentAction(WflContainerControl control, int moveTo,
                                boolean showText) {
        super(name);
        this.control = control;
        this.moveTo = moveTo;
        init(showText);
    }

    private void init(boolean showText) {
        putValue(NAME, name);
        if (!showText)
            putValue(SHORT_DESCRIPTION, "?");
        KeyStroke ks = getKeyStroke();
        putValue(ACCELERATOR_KEY, ks);
    }

    /**
     * Devuelve el KeyStroke dependiendo del tipo de la acción.
     * @return el KeyStroke dependiendo del tipo de la acción.
     */
    private KeyStroke getKeyStroke() {
        if (moveTo == UP)
            return WflShortcuts.UP;
        else if (moveTo == DOWN)
            return WflShortcuts.DOWN;
        else if (moveTo == LEFT)
            return WflShortcuts.LEFT;
        else if (moveTo == RIGHT)
            return WflShortcuts.RIGHT;

        return null;
    }


    public void actionPerformed(ActionEvent e) {
        if (moveTo == UP)
            up();
        else if (moveTo == DOWN)
            down();
        else if (moveTo == LEFT)
            left();
        else if (moveTo == RIGHT)
            right();
   }

    private void move(int xx, int yy) {
        List sc = control.getSelectedComponents();
        for (int i = 0; i < sc.size(); i++) {
            WflComponent wc = (WflComponent) sc.get(i);
            int x = wc.getX();
            int y = wc.getY();
            wc.setNewPosition(x + xx, y + yy);
        }
    }
    private void right() {
        move(distance, 0);
    }

    private void left() {
        //Busco el componente seleccionado que esté más a la izquierda para
        // saber hasta dónde se permite mover los componentes seleccionados.
        int maxToMoveLeft = Integer.MAX_VALUE;

        List sc = control.getSelectedComponents();
        for(int i=0; i < sc.size(); i++){
            WflComponent wc = (WflComponent) sc.get(i);
            if(wc.getX() < maxToMoveLeft){
                maxToMoveLeft = wc.getX();
            }
        }
        int x = distance;
        if (x > maxToMoveLeft)
            x = maxToMoveLeft;

        move(-x, 0);
    }

    private void down() {
        move(0, distance);
    }

    private void up() {
        //Busco el componente seleccionado que esté más arriba para saber hasta
        // dónde se permite mover
        // los componentes seleccionados.
        int maxToMoveTop = Integer.MAX_VALUE;

        List sc = control.getSelectedComponents();
        for(int i=0; i < sc.size(); i++){
            WflComponent wc = (WflComponent) sc.get(i);
            if(wc.getY() + wc.getHeight() < maxToMoveTop)
                maxToMoveTop = wc.getY();
        }
        int y = distance;
        if (y > maxToMoveTop)
            y = maxToMoveTop;
        move(0, -y);
    }
}
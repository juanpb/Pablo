package crm.core.wfl.editor.gui.action;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflState;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflAddStateAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	public static final int NO_X_COORDINATE = -1;
	public static final int NO_Y_COORDINATE = -1;	
	
	private static final String name = "ADD_STATE";

    private WflContainerControl control = null;
    private int x = NO_X_COORDINATE;
    private int y = NO_Y_COORDINATE;


    public WflAddStateAction(WflContainerControl control) {
        this(control, true);
    }

    public WflAddStateAction(WflContainerControl control, String text) {
        super(name, WflState.createImageIcon());
        this.control = control;
        init(text);
    }

    public WflAddStateAction(WflContainerControl control, boolean showText) {
        super(name, WflState.createImageIcon());
        this.control = control;
        String n = showText ? "Agregar estado" : null;
        init(n);
    }

    private void init(String txt){

        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        putValue(NAME, txt);
        if (txt == null){
            putValue(SHORT_DESCRIPTION, "Agregar estado");
        }
        putValue(ACCELERATOR_KEY, WflShortcuts.ADD_STATE);
    }

    public void actionPerformed(ActionEvent e) {
    	if(this.x != NO_X_COORDINATE && this.y != NO_Y_COORDINATE)
    		control.addStateCentered(x, y);
    	else {
    		Point corner = control.getTopLeftCorner();
    		control.addState(corner.x, corner.y);
    	}
   }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
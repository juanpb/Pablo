package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;

import javax.swing.*;

import java.awt.Point;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflAddFunctionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "ADD_FUNCTION";

	public static final int NO_X_COORDINATE = -1;
	public static final int NO_Y_COORDINATE = -1;	

    private WflContainerControl control = null;
    private int x = NO_X_COORDINATE;
    private int y = NO_Y_COORDINATE;


    public WflAddFunctionAction(WflContainerControl control) {
        this(control, true);
    }

    public WflAddFunctionAction(WflContainerControl control, String text) {
        super(name, WflFunction.createImageIcon());
        this.control = control;
        init(text);
    }

    public WflAddFunctionAction(WflContainerControl control, boolean showText) {
        super(name, WflFunction.createImageIcon());
        this.control = control;
        String n = showText ? "Agregar función" : null;
        init(n);
    }

    private void init(String txt){
        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        putValue(NAME, txt);
        if (txt == null){
            putValue(SHORT_DESCRIPTION, "Agregar función");
        }
        putValue(ACCELERATOR_KEY, WflShortcuts.ADD_FUNCTION);
    }

    public void actionPerformed(ActionEvent e) {
    	if(this.x != NO_X_COORDINATE && this.y != NO_Y_COORDINATE)
            control.addFunctionCentered(x, y);
    	else {
    		Point corner = control.getTopLeftCorner();
    		control.addFunction(corner.x, corner.y);
    	}
        
   }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
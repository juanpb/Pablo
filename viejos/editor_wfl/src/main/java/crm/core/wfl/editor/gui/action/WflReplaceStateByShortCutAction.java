package crm.core.wfl.editor.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;
import crm.core.wfl.editor.gui.cmp.WflState;
import crm.core.wfl.editor.gui.cmp.WflStateShortCut;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;

/**
 * Agrega un short-cut a un estado.
 * @author cm
 */
public class WflReplaceStateByShortCutAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	public static final int NO_X_COORDINATE = -1;
	public static final int NO_Y_COORDINATE = -1;
	
    private static String title = "Agregar Atajo";
    private WflContainerControl control = null;
    private WflPosFunction posFunction;
    
    private int x = NO_X_COORDINATE;
    private int y = NO_Y_COORDINATE;    
    
    public WflReplaceStateByShortCutAction(WflContainerControl control,  WflPosFunction posFunction) {
        this(control, posFunction, true);
    }

    public WflReplaceStateByShortCutAction(WflContainerControl control, WflPosFunction posFunction, boolean showText) {
        this.control = control;
        this.posFunction = posFunction;
        String name = getTitle();
        String n = showText ? name : null;
        init(n);
    }

    private String getTitle() {
        return title;
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, title);
        putValue(SMALL_ICON, getIcon());
    }

    private Icon getIcon() {
        String ic = null;
        ic = WflConstants.ICON_SHORTCUT_ADD;
        return WflUtil.createImageIcon(ic);
    }

    public void actionPerformed(ActionEvent e) {
    	addShortCut(control, posFunction, x, y);
    }
    
    
    /**
     * Crea un shortcut de una posfuncion a un estado.
     * @param control Controlador.
     * @param posFunction Pos funcion que hay que reemplazar por el short cut.
     * @param x Ubicacion del shortcut
     * @param y Ubicacion del shortcut.
     */
    public static void addShortCut(WflContainerControl control, WflPosFunction posFunction, int x, int y) {
    	WflStateShortCut ssc = new WflStateShortCut(posFunction.getState());
    	WflPosFunction ps = new WflPosFunction(	posFunction.getFunction(), 
    											ssc,
    											(WflPosFunctionBObj)posFunction.getBObj());
    	control.getContainer().remove(posFunction);
    	if(x != NO_X_COORDINATE && y != NO_Y_COORDINATE)
    		control.addStateShortCut(ssc, posFunction.getX() +  x, posFunction.getY() + y);
    	else
    		control.addStateShortCut(ssc);
    	control.addPosFunction(ps);
        control.repaintContainer();
        WflUtility.setModified(true);
    }

    /**
     * Crea un shortcut de una posfuncion a un estado.
     * @param control Controlador.
     * @param posFunction Pos funcion que hay que reemplazar por el short cut.
     * @param x Ubicacion del shortcut
     * @param y Ubicacion del shortcut.
     */
    public static void addShortCut(WflContainerControl control, WflFunction function, WflState state, int x, int y) {
    	WflStateShortCut ssc = new WflStateShortCut(state);
    	WflPosFunction ps = new WflPosFunction(	function, ssc);

    	if(x != NO_X_COORDINATE && y != NO_Y_COORDINATE)
    		control.addStateShortCut(ssc, function.getX() +  x, function.getY() + y);
    	else
    		control.addStateShortCut(ssc);
    	control.addPosFunction(ps);
        control.repaintContainer();
        WflUtility.setModified(true);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }    
}
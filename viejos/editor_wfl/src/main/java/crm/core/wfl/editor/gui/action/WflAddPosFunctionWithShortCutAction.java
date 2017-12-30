package crm.core.wfl.editor.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;
import crm.core.wfl.editor.gui.cmp.WflState;
import crm.core.wfl.editor.gui.cmp.WflStateShortCut;
import crm.core.wfl.editor.gui.util.WflUtility;

/**
 * Agrega un short-cut a un estado.
 * @author cm
 */
public class WflAddPosFunctionWithShortCutAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
    private WflContainerControl control = null;
    private WflFunction function;
    private WflState state;    
    
    private static final int INITIAL_GAP = 30;
    private static final int GAP_INCREMENT = 10;
    private static int offset = INITIAL_GAP;
    
    public WflAddPosFunctionWithShortCutAction(	WflContainerControl control, 
    											WflFunction function, 
    											WflState state,
    											String text) {
        this.control = control;
        this.state = state;
        this.function = function;
        putValue(NAME, text);
    }


    public void actionPerformed(ActionEvent e) {
    	addShortCut(control, function, state);
    }
    
    /**
     * Crea un shortcut de una posfuncion a un estado.
     * @param control Controlador.
     * @param posFunction Pos funcion que hay que reemplazar por el short cut.
     * @param x Ubicacion del shortcut
     * @param y Ubicacion del shortcut.
     */
    public static void addShortCut(WflContainerControl control, WflFunction function, WflState state) {
    	WflStateShortCut ssc = new WflStateShortCut(state);
    	WflPosFunction ps = new WflPosFunction(	function, ssc);

   		control.addStateShortCut(	ssc, 
   									function.getX() +  function.getWidth() + offset, 
   									function.getY() +  function.getHeight() + offset);
    	control.addPosFunction(ps);
        control.repaintContainer();
        WflUtility.setModified(true);
        offset += GAP_INCREMENT;
    }

}
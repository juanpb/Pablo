package crm.core.wfl.editor.gui.mnu;

import java.awt.Component;

import javax.swing.JMenuItem;

import crm.core.wfl.editor.gui.action.WflReplaceStateByShortCutAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;

/**
 * Menu de pos funciones.
 */
public class WflPosFunctionMnu extends WflConnectorMnu {
	private static final long serialVersionUID = 1L;
	private JMenuItem itemAddStateShortCut = null;    
    private WflReplaceStateByShortCutAction addStateShortCutAction = null;
    
    /**
     * Crea un menu para post-funcion.
     * @param control Control.
     * @param posFunction Postfucion sobre la cual actuara el menu.
     * @param isEditable Si es o no editable.
     */
    public WflPosFunctionMnu(WflContainerControl control,
                             WflPosFunction posFunction, boolean isEditable) {
    	super(control, posFunction, isEditable);
    	addCustomItems();
    }

    private void addCustomItems() {
        itemAddStateShortCut = new JMenuItem(getAddStateShortCutAction());
        add(itemAddStateShortCut);
    }

    public void show(Component invoker, int x, int y) {
    	getAddStateShortCutAction().setLocation(x, y);
        super.show(invoker, x, y);
    }   
    
    private WflReplaceStateByShortCutAction getAddStateShortCutAction() {
    	if(addStateShortCutAction==null)
            addStateShortCutAction = new WflReplaceStateByShortCutAction(control, (WflPosFunction)this.connector);
    	return addStateShortCutAction;
    }
}

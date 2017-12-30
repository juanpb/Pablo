package crm.core.wfl.editor.gui.mnu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.gui.action.WflEditComponentAction;
import crm.core.wfl.editor.gui.action.WflGotoComponentAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflStateShortCut;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflStateShortCutMnu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem itemEdit = null;
    private JMenuItem itemGotoState = null;
    private WflAlignMnu itemAlign = null;

    public WflStateShortCutMnu(WflContainerControl control, WflStateShortCut state) {
        itemEdit = new JMenuItem(new WflEditComponentAction(control, state));
        itemAlign = new WflAlignMnu(control);
        itemGotoState = new JMenuItem(new WflGotoComponentAction(	control, 
        															state.getTargetState(), 
        															"Ir a " + state.getTargetState().getId() + 
        															" (" + 
        															((WflStateBObj)state.getTargetState().getBObj()).getName()
        															+ ")"));
        init();
    }

    private void init() {
    	add(itemGotoState);
        addSeparator();
        add(itemAlign);
        addSeparator();
        add(itemEdit);
    }
}

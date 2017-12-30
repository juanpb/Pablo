package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.gui.action.WflDeleteMarkAction;
import crm.core.wfl.editor.gui.action.WflEditMarkAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;

import javax.swing.*;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflMarkMnu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	private JMenuItem itemEdit = null;
    private JMenuItem itemRemove = null;
    private WflMarkBObj markBObj = null;

    public WflMarkMnu(WflContainerControl control, WflMarkBObj bo) {
        this.markBObj = bo;

        WflEditMarkAction editAction =
                new WflEditMarkAction(control, markBObj, true);
        WflDeleteMarkAction deleteAction =
                new WflDeleteMarkAction(control, markBObj, true);
        itemEdit = new JMenuItem(editAction);
        itemRemove = new JMenuItem(deleteAction);
        addItems();
    }

    private void addItems() {
        add(itemEdit);
        add(itemRemove);
    }

}

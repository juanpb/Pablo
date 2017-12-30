package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.gui.action.WflAddFunctionAction;
import crm.core.wfl.editor.gui.action.WflAddStateAction;
import crm.core.wfl.editor.gui.action.WflSelectAllAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflContainerMnu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
    private WflAddStateAction stateAction = null;
    private WflAddFunctionAction functionAction = null;
    private WflSelectAllAction selectAllAction = null;
    private WflAlignMnu mnuAlign;

    public WflContainerMnu(WflContainerControl control) {
        stateAction = new WflAddStateAction(control, true);
        functionAction = new WflAddFunctionAction(control, true);
        selectAllAction = new WflSelectAllAction(control, true);
        mnuAlign = new WflAlignMnu(control);
        init();
    }

    private void init() {
        addItems();
    }

    private void addItems() {
        JMenuItem itemAddState = new JMenuItem(stateAction);
        JMenuItem itemAddFunction = new JMenuItem(functionAction);
        JMenuItem itemSelectAll = new JMenuItem(selectAllAction);

        add(itemAddState);
        add(itemAddFunction);
        addSeparator();
        add(itemSelectAll);
        addSeparator();

        add (mnuAlign);
    }


    public void show(Component invoker, int x, int y) {
        stateAction.setLocation(x, y);
        functionAction.setLocation(x, y);
        super.show(invoker, x, y);
    }
}

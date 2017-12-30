package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflSelectAllAction extends AbstractAction {
    private static final String name = "SELECT_ALL";
    private static final long serialVersionUID = 1L;
    private WflContainerControl control = null;

    public WflSelectAllAction(WflContainerControl control) {
        this(control, true);
    }

    public WflSelectAllAction(WflContainerControl control, String text) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_SELECT_ALL));
        this.control = control;
        init(text);
    }

    public WflSelectAllAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_SELECT_ALL));
        this.control = control;
        String n = showText ? "Seleccionar todo" : null;
        init(n);
    }

    private void init(String txt){
        putValue(NAME, txt);

        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        if (txt == null)
            putValue(SHORT_DESCRIPTION, "Seleccionar todo");
        putValue(ACCELERATOR_KEY, WflShortcuts.SELECT_ALL);
    }

    public void actionPerformed(ActionEvent e) {
        control.selectAll();
   }

}
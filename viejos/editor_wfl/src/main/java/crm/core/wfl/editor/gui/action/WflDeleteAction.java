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
public class WflDeleteAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "DELETE";

    private WflContainerControl control = null;


    public WflDeleteAction(WflContainerControl control) {
        this(control, true);
    }

    public WflDeleteAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_DELETE));
        this.control = control;
        String name = showText ? "Borrar" : null;
        putValue(NAME, name);
        putValue(SHORT_DESCRIPTION, "Borrar");
        putValue(ACCELERATOR_KEY, WflShortcuts.DELETE);
    }

    public void actionPerformed(ActionEvent e) {
        control.removeSelected();
   }
}
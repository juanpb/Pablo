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
public class WflExitAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "EXIT";
    private static final String title = "Salir";

    private WflContainerControl control = null;


    public WflExitAction(WflContainerControl control) {
        this(control, true);
    }

    public WflExitAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_EXIT));
        this.control = control;
        String name = showText ? title : null;
        putValue(NAME, name);
        if (!showText)
            putValue(SHORT_DESCRIPTION, title);
        putValue(ACCELERATOR_KEY, WflShortcuts.EXIT);
    }

    public void actionPerformed(ActionEvent e) {
        control.exit();
   }
}
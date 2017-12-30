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
public class WflProcessDefinitionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "PROCESS_DEFINITION";

    private WflContainerControl control = null;


    public WflProcessDefinitionAction(WflContainerControl control) {
        this(control, true);
    }

    public WflProcessDefinitionAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_PROCESS_DEFINITION));
        this.control = control;
        String n = showText ? "Definición de proceso" : null;
        putValue(NAME, n);
        if (n == null)
            putValue(SHORT_DESCRIPTION, "Definición de proceso");
        putValue(ACCELERATOR_KEY, WflShortcuts.PROCESS_DEFINITION);
    }

    public void actionPerformed(ActionEvent e) {
        control.showProcessDefDlg();
   }
}
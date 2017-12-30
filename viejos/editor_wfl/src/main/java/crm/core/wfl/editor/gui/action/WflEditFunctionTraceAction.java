package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Edita el trace de una funcion (Seguimientos).
 */
public class WflEditFunctionTraceAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "EDIT_TRACE";
    private static final String title = "Editar Seguimientos";

    private WflContainerControl control = null;

    public WflEditFunctionTraceAction(WflContainerControl control, boolean showText) {

        super(WflEditFunctionTraceAction.name, WflUtil.createImageIcon(WflConstants.ICON_EDIT_TRACE));
        this.control = control;
        String name = showText ? WflEditFunctionTraceAction.title : null;
        putValue(NAME, name);

        putValue(ACCELERATOR_KEY, WflShortcuts.EDIT_TRACE);
        if (!showText)
            putValue(SHORT_DESCRIPTION, WflEditFunctionTraceAction.title);
    }

    public void actionPerformed(ActionEvent e) {
        control.showFunctionTraceEditor();
        control.getContainer().repaint();
    }
}

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
public class WflSaveAsFileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "SAVE_AS";
    private static final String desc = "Guardar como...";

    private WflContainerControl control = null;

    public WflSaveAsFileAction(WflContainerControl control) {
        this(control, true);
    }

    public WflSaveAsFileAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_SAVE_AS));
        this.control = control;
        String name = showText ? desc : null;
        init(name);
    }

    private void init(String txt){
        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        putValue(NAME, txt);
        if (txt == null){
            putValue(SHORT_DESCRIPTION, desc);
        }
        putValue(ACCELERATOR_KEY, WflShortcuts.SAVE_AS);
    }

    public void actionPerformed(ActionEvent e) {
        control.saveAs();
    }
}
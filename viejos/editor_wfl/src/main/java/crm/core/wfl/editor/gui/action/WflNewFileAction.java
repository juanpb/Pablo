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
public class WflNewFileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "NEW_FILE";
    private static final String desc = "Nuevo archivo";

    private WflContainerControl control = null;


    public WflNewFileAction(WflContainerControl control) {
        this(control, true);
    }

    public WflNewFileAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_NEW_FILE));
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
        putValue(ACCELERATOR_KEY, WflShortcuts.NEW_FILE);
    }

    public void actionPerformed(ActionEvent e) {
        control.newFile();
   }
}
package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflSaveFileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "SAVE";
    private static final String desc = "Guardar";

    private WflContainerControl control = null;

    public WflSaveFileAction(WflContainerControl control) {
        this(control, true);
    }

    public WflSaveFileAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_SAVE));
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
        putValue(ACCELERATOR_KEY, WflShortcuts.SAVE);
    }

    public void actionPerformed(ActionEvent e) {
        Frame cf = WflUtility.getCurrentFrame();
        cf.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        control.save();
        cf.setCursor(Cursor.getDefaultCursor());
    }
}
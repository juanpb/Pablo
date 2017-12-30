package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflCancelAddMarkAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "CANCEL_ADD_MARK";
    private static final String title = "a";
    private WflContainerControl control;

    public WflCancelAddMarkAction(WflContainerControl control, boolean showText) {
        super(name);
        this.control = control;
        String name = showText ? title : null;
        putValue(NAME, name);
        if (!showText)
            putValue(SHORT_DESCRIPTION, title);
        putValue(ACCELERATOR_KEY, WflShortcuts.RESTORE_CURSOR);
    }

    public void actionPerformed(ActionEvent e) {
        control.cancelAddMark();        
    }

    public String getName(){
        return name;
    }
}
package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflEditMarkAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "EDIT_MARK";
    private static final String title = "Editar";

    private WflContainerControl control = null;
    private WflMarkBObj mark = null;

    public WflEditMarkAction(WflContainerControl control,
                                  WflMarkBObj m, boolean showText) {
        super(name);
        this.mark = m;
        this.control = control;
        String n = showText ? title : null;
        init(n);
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, title);
//        putValue(ACCELERATOR_KEY, WflShortcuts.EDIT);
//        putValue(SMALL_ICON, WflUtil.createImageIcon(WflConstants.ICON_EDIT));
    }

    public void actionPerformed(ActionEvent e) {
        control.edit(mark);
   }

}
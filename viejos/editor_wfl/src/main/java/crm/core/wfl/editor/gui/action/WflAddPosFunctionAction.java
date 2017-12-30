package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflAddPosFunctionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "ADD_POSFUNCTION";

    private WflContainerControl control = null;

    public WflAddPosFunctionAction(WflContainerControl control) {
        this(control, true);
    }

    public WflAddPosFunctionAction(WflContainerControl control, String text) {
//        super(name, WflUtil.createImageIcon(WflConstants.ICON_POSFUNCTION));
        super(name, WflPosFunction.createImageIcon());
        this.control = control;
        init(text);
    }

    public WflAddPosFunctionAction(WflContainerControl control, boolean showText) {
        super(name, WflPosFunction.createImageIcon());
        this.control = control;
        String n = showText ? "Agregar posfunción" : null;
        init(n);
    }

    private void init(String txt){
        putValue(NAME, txt);

        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        if (txt == null)
            putValue(SHORT_DESCRIPTION, "Agregar posfunción");
        putValue(ACCELERATOR_KEY, WflShortcuts.ADD_POSFUNCTION);
    }

    public void actionPerformed(ActionEvent e) {
        control.addPosFunction();
   }

}
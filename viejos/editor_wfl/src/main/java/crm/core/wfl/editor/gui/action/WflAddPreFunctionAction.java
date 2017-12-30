package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflPreFunction;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflAddPreFunctionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "ADD_PREFUNCTION";

    private WflContainerControl control = null;

    public WflAddPreFunctionAction(WflContainerControl control) {
        this(control, true);
    }

    public WflAddPreFunctionAction(WflContainerControl control, String text) {
        super(name, WflPreFunction.createImageIcon());
        this.control = control;
        init(text);
    }

    public WflAddPreFunctionAction(WflContainerControl control, boolean showText) {
        super(name, WflPreFunction.createImageIcon());
        this.control = control;
        String n = showText ? "Agregar prefunción" : null;
        init(n);
    }

    private void init(String txt){
        putValue(NAME, txt);

        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        if (txt == null)
            putValue(SHORT_DESCRIPTION, "Agregar prefunción");
        putValue(ACCELERATOR_KEY, WflShortcuts.ADD_PREFUNCTION);
    }

    public void actionPerformed(ActionEvent e) {
        control.addPreFunction();
   }

}
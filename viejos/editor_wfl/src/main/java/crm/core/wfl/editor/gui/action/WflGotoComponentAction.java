package crm.core.wfl.editor.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;


/**
 * Marca una funcion como seleccionada y se asegura que quede visible en la pantalla.
 */
public class WflGotoComponentAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private WflContainerControl control = null;
    private WflComponent function = null;

    public WflGotoComponentAction(WflContainerControl control, WflComponent component, String text) {
        super(text);
        this.control = control;
        this.function = component;
        init(text);
    }

    private void init(String txt){
        putValue(NAME, txt);
    }

    public void actionPerformed(ActionEvent e) {
        control.deselectAll();
        function.setSelected(true);
        control.getContainer().viewComponent(function);
    }

}
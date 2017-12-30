package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflEditComponentAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "EDIT";
    private static final String title = "Editar";

    private WflContainerControl control = null;
    private WflComponent component = null;

    public WflEditComponentAction(WflContainerControl control) {
        this(control, true);
    }

    public WflEditComponentAction(WflContainerControl control, WflComponent c) {
        this(control, true);
        this.component = c;
    }

    public WflEditComponentAction(WflContainerControl control, String text) {
        super(name);
        this.control = control;
        init(text);
    }

    public WflEditComponentAction(WflContainerControl control, boolean showText) {
        super(name);
        this.control = control;

        String n = showText ? title : null;
        init(n);
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, title);
        putValue(ACCELERATOR_KEY, WflShortcuts.EDIT);
        putValue(SMALL_ICON, WflUtil.createImageIcon(WflConstants.ICON_EDIT));
    }

    public void actionPerformed(ActionEvent e) {
        if (component == null)
            control.editSelected();
        else
            control.edit(component);
   }

}
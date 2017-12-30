package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflViewXMLAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "VIEW_XML";
    private static final String title = "Ver XML";

    private WflContainerControl control = null;

    public WflViewXMLAction(WflContainerControl control, boolean showText) {

        super(WflViewXMLAction.name, WflUtil.createImageIcon(WflConstants.ICON_VIEW_XML));
        this.control = control;
        String name = showText ? WflViewXMLAction.title : null;
        putValue(NAME, name);

        putValue(ACCELERATOR_KEY, WflShortcuts.VIEW_XML);
        if (!showText)
            putValue(SHORT_DESCRIPTION, WflViewXMLAction.title);
    }

    public void actionPerformed(ActionEvent e) {
        control.showXML();
    }
}

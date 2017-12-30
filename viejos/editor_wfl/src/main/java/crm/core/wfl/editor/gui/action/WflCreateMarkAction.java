package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflCreateMarkAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static final String name = "CREATE_MARK";
    private static final String tit = "Crear hito";

    private WflContainerControl control = null;


    public WflCreateMarkAction(WflContainerControl control) {
        this(control, true);
    }

    public WflCreateMarkAction(WflContainerControl control, boolean showText) {
        super(name);
        ImageIcon ic = WflUtil.createImageIconCreateMark();
        putValue(SMALL_ICON, ic);
        this.control = control;
        String name = showText ? tit : null;
        putValue(NAME, name);
        if (!showText)
            putValue(SHORT_DESCRIPTION, tit);
    }

    public void actionPerformed(ActionEvent e) {
        control.createMark();
   }
}
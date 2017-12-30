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
public class WflDeleteMarkAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "DELETE_MARK";
    private static String tit = "Eliminar";

    private WflContainerControl control = null;
    private WflMarkBObj markBObj = null;
    private boolean showText = false;


    public WflDeleteMarkAction(WflContainerControl control) {
        this(control, true);
    }

    public WflDeleteMarkAction(WflContainerControl control, boolean showText) {
        this(control, null, showText);
    }

   public WflDeleteMarkAction(WflContainerControl control,
                                WflMarkBObj bo, boolean showText) {
        super(name);
//        super(name, WflUtil.createImageIcon(WflConstants.ICON_MARK_DELETE));
        this.control = control;
        this.markBObj = bo;
        this.showText = showText;
        init();
    }

    private void init() {
        String name = showText ? tit : null;
        putValue(NAME, name);
        if (!showText)
            putValue(SHORT_DESCRIPTION, tit);
    }

    public void actionPerformed(ActionEvent e) {
        if (markBObj == null){
            return ;
        }
        control.remove(markBObj);
   }

}
package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.bobj.WflMarkBObj;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflAddMarkAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "ADD_MARK";

    private WflContainerControl control = null;
    private WflMarkBObj markBObj = null;
    private boolean showText = false;
    private boolean showIcon = false;

   public WflAddMarkAction(WflContainerControl control, WflMarkBObj bo,
                                boolean showText, boolean showIcon) {
        super(name);
        this.control = control;
        this.markBObj = bo;
        this.showText = showText;
        this.showIcon = showIcon;
        init();
    }

    private void init() {
        WflMarkBObj bo = markBObj;
        String tit = "Agregar hito " + ((bo == null) ? "" : bo.getId());
        String name = (showText && bo != null) ? bo.getId() : null;
        putValue(NAME, name);
        if (showIcon){
            putValue(SMALL_ICON, WflUtil.createImageIcon(WflConstants.ICON_MARK));
        }
        putValue(SHORT_DESCRIPTION, tit);
    }

    public void actionPerformed(ActionEvent e) {
        if (markBObj == null){
            return ;
        }
        control.addMark(markBObj);
   }

    public void update() {
        init();
    }

    public WflMarkBObj getBObj() {
        return markBObj;
    }
}
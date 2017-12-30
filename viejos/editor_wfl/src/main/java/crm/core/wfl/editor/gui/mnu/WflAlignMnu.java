package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.gui.action.WflAlignAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;

import javax.swing.*;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflAlignMnu extends JMenu {
	private static final long serialVersionUID = 1L;
	private JMenu itemAlignHor = new JMenu("Horizontal");
    private JMenu itemAlignVer = new JMenu("Vertical");
    private JMenuItem itemTop = null;
    private JMenuItem itemBottom = null;
    private JMenuItem itemLeft = null;
    private JMenuItem itemRight = null;
    private JMenuItem itemHorCenter = null;
    private JMenuItem itemVerCenter = null;


    public WflAlignMnu(WflContainerControl control) {
        super("Alinear");
        WflAlignAction topAction = new WflAlignAction(control,WflAlignAction.TOP, true);
        WflAlignAction bottomAction = new WflAlignAction(control,WflAlignAction.BOTTOM, true);
        WflAlignAction verAction = new WflAlignAction(control,WflAlignAction.VER_CENTER, true);
        WflAlignAction horAction = new WflAlignAction(control,WflAlignAction.HOR_CENTER, true);
        WflAlignAction rightAction = new WflAlignAction(control,WflAlignAction.RIGHT, true);
        WflAlignAction leftAction = new WflAlignAction(control,WflAlignAction.LEFT, true);


        itemTop = new JMenuItem(topAction);
        itemBottom = new JMenuItem(bottomAction);
        itemLeft = new JMenuItem(leftAction);
        itemRight = new JMenuItem(rightAction);
        itemHorCenter = new JMenuItem(horAction);
        itemVerCenter = new JMenuItem(verAction);
        init();
    }

    private void init() {
        addItems();
    }

    private void addItems() {
        itemAlignHor.add(itemTop);
        itemAlignHor.add(itemHorCenter);
        itemAlignHor.add(itemBottom);
        itemAlignVer.add(itemLeft);
        itemAlignVer.add(itemVerCenter);
        itemAlignVer.add(itemRight);
        super.add(itemAlignHor);
        super.add(itemAlignVer);
    }

}

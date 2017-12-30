package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflState;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflAlignAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	public static final String TOP = "T";
    public static final String BOTTOM = "B";
    public static final String RIGHT = "R";
    public static final String LEFT = "L";
    public static final String HOR_CENTER = "H";
    public static final String VER_CENTER = "V";
    private String position = null;

    private static String title = null;

    private WflContainerControl control = null;

    public WflAlignAction(WflContainerControl control, String position) {
        this(control, position, true);
    }

    public WflAlignAction(WflContainerControl control, String position,
                         boolean showText) {
        this.position = position;
        this.control = control;
        String name = getTitle();
        title = "Alinear " + name.toLowerCase();
        String n = showText ? name : null;
        init(n);
    }

    private String getTitle() {
        if (TOP.equals(position))
            return "Arriba";
        else if (BOTTOM.equals(position))
            return "Abajo";
        else if (LEFT.equals(position))
            return "Izquierda";
        else if (RIGHT.equals(position))
            return "Derecha";
        else if (VER_CENTER.equals(position))
            return "Centro";
        else if (HOR_CENTER.equals(position))
            return "Centro";

        return null;
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, title);
        putValue(ACCELERATOR_KEY, getShortcut());
        putValue(SMALL_ICON, getIcon());
    }

    private Icon getIcon() {
        String ic = null;
        if (TOP.equals(position))
            ic = WflConstants.ICON_ALIGN_TOP;
        else if (BOTTOM.equals(position))
            ic = WflConstants.ICON_ALIGN_BOTTOM;
        else if (LEFT.equals(position))
            ic = WflConstants.ICON_ALIGN_LEFT;
        else if (RIGHT.equals(position))
            ic = WflConstants.ICON_ALIGN_RIGHT;
        else if (VER_CENTER.equals(position))
            ic = WflConstants.ICON_ALIGN_VERTICAL;
        else if (HOR_CENTER.equals(position))
            ic = WflConstants.ICON_ALIGN_HORIZONTAL;

        return WflUtil.createImageIcon(ic);
    }

    private KeyStroke getShortcut() {
        if (TOP.equals(position))
            return WflShortcuts.ALIGN_TOP;
        else if (BOTTOM.equals(position))
            return WflShortcuts.ALIGN_BOTTOM;
        else if (LEFT.equals(position))
            return WflShortcuts.ALIGN_LEFT;
        else if (RIGHT.equals(position))
            return WflShortcuts.ALIGN_RIGHT;
        else if (VER_CENTER.equals(position))
            return WflShortcuts.ALIGN_VERTICAL;
        else if (HOR_CENTER.equals(position))
            return WflShortcuts.ALIGN_HORIZONTAL;

        return null;
    }

    public void actionPerformed(ActionEvent e) {
        if (TOP.equals(position))
            top();
        else if (BOTTOM.equals(position))
            bottom();
        else if (LEFT.equals(position))
            left();
        else if (RIGHT.equals(position))
            right();
        else if (VER_CENTER.equals(position))
            vertical();
        else if (HOR_CENTER.equals(position))
            horizontal();
        else
            return ;

        WflUtility.setModified(true);
    }

    private void horizontal() {
        List<WflComponent> sc = control.getSelectedComponents(WflState.class);
        sc.addAll(control.getSelectedComponents(WflFunction.class));

        int maxY = Integer.MAX_VALUE;
        int minY = 0;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int y = c.getY();
            if (maxY > y)
                maxY = y;
            if (minY < y)
                minY = y;
        }
        int top = minY + (maxY - minY)/2;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int x = c.getX();
            c.setNewPosition(x, top);
        }
        control.repaintContainer();
    }

    private void vertical() {
        List<WflComponent> sc = control.getSelectedComponents(WflState.class);
        sc.addAll(control.getSelectedComponents(WflFunction.class));

        int minX = 0;
        int maxX = Integer.MAX_VALUE;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int x = c.getX();
            if (maxX > x)
                maxX = x;
            if (minX < x)
                minX = x;
        }
        int x = minX + (maxX - minX)/2;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int y = c.getY();
            c.setNewPosition(x, y);
        }
        control.repaintContainer();
    }

    private void right() {
        List<WflComponent> sc = control.getSelectedComponents(WflState.class);
        sc.addAll(control.getSelectedComponents(WflFunction.class));

        int maxX = 0;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int x = c.getX();
            if (maxX < x)
                maxX = x;
        }
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int y = c.getY();
            c.setNewPosition(maxX, y);
        }
        control.repaintContainer();
    }

    private void left() {
        List<WflComponent> sc = control.getSelectedComponents(WflState.class);
        sc.addAll(control.getSelectedComponents(WflFunction.class));

        int minX = Integer.MAX_VALUE;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int x = c.getX();
            if (minX > x)
                minX = x;
        }
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int y = c.getY();
            c.setNewPosition(minX, y);
        }
        control.repaintContainer();
    }

    private void bottom() {
        List<WflComponent> sc = control.getSelectedComponents(WflState.class);
        sc.addAll(control.getSelectedComponents(WflFunction.class));

        int top = 0;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int y = c.getY();
            if (top < y)
                top = y;
        }
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int x = c.getX();
            c.setNewPosition(x, top);
        }
        control.repaintContainer();
    }

    private void top() {
        List<WflComponent> sc = control.getSelectedComponents(WflState.class);
        sc.addAll(control.getSelectedComponents(WflFunction.class));

        int top = Integer.MAX_VALUE;
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int y = c.getY();
            if (top > y)
                top = y;
        }
        for (int i = 0; i < sc.size(); i++) {
            WflComponent c = (WflComponent) sc.get(i);
            int x = c.getX();
            c.setNewPosition(x, top);
        }
        control.repaintContainer();
    }

}
package crm.core.wfl.editor.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import crm.core.wfl.editor.gui.cmp.WflConnector;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;

/**
 * Accion para remover vertices de un conector.
 * @author cm
 *
 */
public class WflRemoveVerticeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	public static final int NO_X_COORDINATE = -1;
	public static final int NO_Y_COORDINATE = -1;
	
    private static String title = "Remover Vertice";
    private WflContainerControl control = null;
    private WflConnector connector;
    
    private int x = NO_X_COORDINATE;
    private int y = NO_Y_COORDINATE;    

    public WflRemoveVerticeAction(WflContainerControl control, WflConnector connector) {
        this(control, connector, true);
    }

    public WflRemoveVerticeAction(WflContainerControl control, WflConnector connector, boolean showText) {
        this.control = control;
        this.connector = connector;
        String name = getTitle();
        String n = showText ? name : null;
        init(n);
    }

    private String getTitle() {
        return title;
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, title);
        putValue(SMALL_ICON, getIcon());
    }

    private Icon getIcon() {
        String ic = null;
        ic = WflConstants.ICON_VERTICE_REMOVE;
        return WflUtil.createImageIcon(ic);
    }

    public void actionPerformed(ActionEvent e) {
       	connector.removeNearestVertice(connector.getX() + this.x, connector.getY() + this.y);
        control.repaintContainer();

        WflUtility.setModified(true);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
}
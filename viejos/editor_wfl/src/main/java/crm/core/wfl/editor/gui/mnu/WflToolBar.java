package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.gui.action.*;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflProcessDefinitionContainer;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Feb 10, 2006
 * Time: 10:52:15 AM
 */
public class WflToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	private WflProcessDefinitionContainer container;
    
    private JToggleButton zoomWindowsButton;

    public WflToolBar(WflProcessDefinitionContainer container) {
        this.container = container;
        init();
    }

    private void init() {
        super.setName("Paleta");
        addButtons();
    }

    private void addButtons() {
        WflContainerControl control = container.getControl();

        addButton(new WflAddStateAction(control, false));
        addButton(new WflAddFunctionAction(control, false));
        addButton(new WflAddPreFunctionAction(control, false));
        addButton(new WflAddPosFunctionAction(control, false));
        super.addSeparator();
        addButton(new WflProcessDefinitionAction(control, false));
        super.addSeparator();
        addButton(new WflParallelismAction(control,WflParallelismAction.TYPE_CREATE, false));
        addButton(new WflParallelismAction(control,WflParallelismAction.TYPE_EDIT, false));
        addButton(new WflParallelismAction(control,WflParallelismAction.TYPE_REMOVE, false));
        super.addSeparator();
        addButton(new WflDeleteAction(control, false));
        addButton(new WflEditComponentAction(control, false));
        addButton(new WflSelectAllAction(control, false));
        super.addSeparator();

        //Combo zoom
        JComboBox cb = new JComboBox();
        cb.addItem("25%");
        cb.addItem("50%");
        cb.addItem("75%");
        cb.addItem("100%");
        cb.addItem("125%");
        cb.addItem("150%");
        cb.setSelectedIndex(3);
        cb.setEditable(true);

        cb.setPrototypeDisplayValue("XXXXX");
        cb.setMaximumSize(cb.getMinimumSize());

        cb.setAction(new WflZoomAction(control, cb, true));
        super.add(cb);
        super.addSeparator();

        addButton(new WflAlignAction(control, WflAlignAction.TOP, false));
        addButton(new WflAlignAction(control, WflAlignAction.HOR_CENTER, false));
        addButton(new WflAlignAction(control, WflAlignAction.BOTTOM, false));
        addButton(new WflAlignAction(control, WflAlignAction.LEFT, false));
        addButton(new WflAlignAction(control, WflAlignAction.VER_CENTER, false));
        addButton(new WflAlignAction(control, WflAlignAction.RIGHT, false));
        super.addSeparator();

        addButton(new WflViewXMLAction(control, false));
        
        super.addSeparator();
        
        addButton(new WflEditFunctionTraceAction(control, false));        
        
        super.addSeparator();
        
        zoomWindowsButton = addToogleButton(new WflZoomWindowAction(control, false));
    }

    private JButton addButton(Action action) {
        JButton btn = new JButton(action);
        btn.setMargin(new Insets(0,0,0,0));
        super.add(btn);
        return btn;
    }

    private JToggleButton addToogleButton(Action action) {
        JToggleButton btn = new JToggleButton(action);
        btn.setMargin(new Insets(0,0,0,0));
        btn.setPreferredSize(new Dimension(26,26));
        btn.setMinimumSize(btn.getPreferredSize());
        btn.setMaximumSize(btn.getPreferredSize());
        super.add(btn);
        return btn;
    }
    
    /**
     * Retorna el boton de la ventana de zoom.
     * @return Boton de la ventana de zoom.
     */
    public void setZoomWindowsStatus(boolean windowsOpened) {
    	zoomWindowsButton.setSelected(windowsOpened);
    }
}

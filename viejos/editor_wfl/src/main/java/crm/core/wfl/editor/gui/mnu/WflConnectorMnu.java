package crm.core.wfl.editor.gui.mnu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import crm.core.wfl.editor.gui.action.WflAddVerticeAction;
import crm.core.wfl.editor.gui.action.WflParallelismAction;
import crm.core.wfl.editor.gui.action.WflRemoveVerticeAction;
import crm.core.wfl.editor.gui.cmp.WflConnector;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflConnectorMnu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	private JMenuItem itemEdit = new JMenuItem("Editar");
    private JMenuItem itemEditParallelism = null;
    private JMenuItem itemRemoveParallelism = null;
    private JMenuItem itemAddVertice = null;    
    private JMenuItem itemRemoveVertice = null;    
    protected WflConnector connector = null;
    protected WflContainerControl control;
    private boolean isEditable;
    private WflParallelismAction editParallelismAction = null;
    private WflParallelismAction removeParallelismAction = null;
    private WflAddVerticeAction addVerticeAction = null;
    private WflRemoveVerticeAction removeVerticeAction = null;    

    public WflConnectorMnu(WflContainerControl control,
                             WflConnector connector, boolean isEditable) {
        this.connector = connector;
        this.control = control;
        this.isEditable = isEditable;

        editParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_EDIT, true, connector);
        removeParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_REMOVE, true, connector);
        addVerticeAction = new WflAddVerticeAction(control, connector);
        removeVerticeAction = new WflRemoveVerticeAction(control, connector);        
        
        itemRemoveParallelism = new JMenuItem(removeParallelismAction);
        itemEditParallelism = new JMenuItem(editParallelismAction);
        itemAddVertice = new JMenuItem(addVerticeAction);
        itemRemoveVertice = new JMenuItem(removeVerticeAction);        

        init();
    }

    private void init() {
        addListeners();
        addItems();
    }

    protected void addItems() {
        if (isEditable){
            add(itemEdit);
            addSeparator();
        }
        add(itemEditParallelism);
        add(itemRemoveParallelism);
        add(itemAddVertice);        
        add(itemRemoveVertice);        
    }

    private void addListeners() {
        itemEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edit();
            }
        });
    }

    private void edit() {
        control.edit(connector);
    }

    public void setEnableEditParallelism(boolean enable) {
        itemEditParallelism.setEnabled(enable);
        itemRemoveParallelism.setEnabled(enable);
    }
    
    public void show(Component invoker, int x, int y) {
    	addVerticeAction.setLocation(x, y);
    	removeVerticeAction.setLocation(x, y);
        super.show(invoker, x, y);
    }

    /**
     * Inidca que puede o no remover vertices.
     * @param b Indicador true o false.
     */
	public void setEnableRemoveVertice(boolean b) {
		itemRemoveVertice.setEnabled(b);
	}
}

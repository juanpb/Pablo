package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.bobj.WflFunctionBObj;
import crm.core.wfl.editor.gui.action.WflEditComponentAction;
import crm.core.wfl.editor.gui.action.WflGotoComponentAction;
import crm.core.wfl.editor.gui.action.WflParallelismAction;
import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;
import crm.core.wfl.editor.gui.cmp.WflPreFunction;
import crm.core.wfl.editor.gui.cmp.WflState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflStateMnu extends JPopupMenu implements Comparator {
	private static final long serialVersionUID = 1L;
	private JMenuItem itemAddFunction = new JMenuItem("Agregar función");
    private JMenuItem itemAddFunctionState = new JMenuItem("Agregar función y estado");
    private JMenuItem itemAddParallelism = null;
    private WflParallelismAction createParallelismAction = null;
    private WflParallelismAction editParallelismAction = null;
    private WflParallelismAction removeParallelismAction = null;
    private JMenuItem itemEditParallelism = null;
    private JMenuItem itemRemoveParallelism = null;
    private JMenuItem itemEdit = null;
    private WflAlignMnu itemAlign = null;
    private WflState state = null;
    private WflContainerControl control;
    private JMenu relatedFunctionMenu;

    @SuppressWarnings("unchecked")
	public WflStateMnu(WflContainerControl control, WflState state) {
        this.state = state;
        this.control = control;
        
        createParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_CREATE, true, state);
        editParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_EDIT, true, state);
        removeParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_REMOVE, true, state);
        itemAddParallelism = new JMenuItem(createParallelismAction);
        itemRemoveParallelism = new JMenuItem(removeParallelismAction);
        itemEditParallelism = new JMenuItem(editParallelismAction);

        
        itemAddFunction.setIcon(WflFunction.createImageIcon());
        itemAddFunctionState.setIcon(WflState.createImageIcon());
        
        itemEdit = new JMenuItem(new WflEditComponentAction(control, state));
        itemAlign = new WflAlignMnu(control);

        relatedFunctionMenu = new JMenu("Funciones relacionadas");
        relatedFunctionMenu.setEnabled(false);
        List<WflComponent> related = state.getRelatedComponents();
        Collections.sort(related, this);
        Iterator i = related.iterator();
        while(i.hasNext()) {
        	Object o = i.next();
        	if(o instanceof WflPosFunction) {
        		WflPosFunction pf = (WflPosFunction)o;
        		JMenuItem posFunctionGoItem = new JMenuItem(
        				new WflGotoComponentAction(	control, 
        											pf.getFunction(), 
        											pf.getSource().getId() + 
        											" (" +
        											((WflFunctionBObj)pf.getFunction().getBObj()).getName() +
        											")"));
        		relatedFunctionMenu.add(posFunctionGoItem);
        		relatedFunctionMenu.setEnabled(true);
        	}
        }
        
        init();
    }

    private void init() {
        addListeners();
        addItems();
        setProperties();
    }

    private void setProperties() {
        //Si el estado ya tiene una posfunción => deshabilito los ítems para
        //agregar funciones.
        List rc = state.getRelatedComponents();
        for (int i = 0; i < rc.size(); i++) {
            WflComponent wc = (WflComponent) rc.get(i);
            if (wc instanceof WflPreFunction){
                itemAddFunction.setEnabled(false);
                itemAddFunctionState.setEnabled(false);
            }
        }
    }


    private void addItems() {
        add(itemAddFunction);
        add(itemAddFunctionState);
        addSeparator();
        add(relatedFunctionMenu);
        addSeparator();
        add(itemAddParallelism);
        add(itemEditParallelism);
        add(itemRemoveParallelism);
        addSeparator();
        add(itemAlign);
        addSeparator();
        add(itemEdit);
    }

    private void addListeners() {
        itemAddFunction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFunction();
            }
        });
        itemAddFunctionState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFunctionState();
            }
        });
    }

    private void addFunction() {
        control.addFunctionToState(state);
    }

    private void addFunctionState() {
        control.addFunctionAndStateToState(state);
    }

    public void setEnableAddParallelism(boolean enable) {
        itemAddParallelism.setEnabled(enable);
    }
    public void setEnableEditParallelism(boolean enable) {
        itemEditParallelism.setEnabled(enable);
        itemRemoveParallelism.setEnabled(enable);
    }

	public int compare(Object o1, Object o2) {
		if(o1 instanceof WflPosFunction && o2 instanceof WflPosFunction) {
			WflPosFunction pf1 = (WflPosFunction)o1;
			WflPosFunction pf2 = (WflPosFunction)o2;
			return pf1.getFunction().getId().compareTo(pf2.getFunction().getId());
		}
		return 0;
	}
}

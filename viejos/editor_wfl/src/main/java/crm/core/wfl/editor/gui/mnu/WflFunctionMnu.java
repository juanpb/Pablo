package crm.core.wfl.editor.gui.mnu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.gui.action.WflAddPosFunctionWithShortCutAction;
import crm.core.wfl.editor.gui.action.WflEditComponentAction;
import crm.core.wfl.editor.gui.action.WflParallelismAction;
import crm.core.wfl.editor.gui.action.WflTestTokenValueAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;
import crm.core.wfl.editor.gui.cmp.WflState;
import crm.core.wfl.editor.gui.cmp.WflStateShortCut;

/**
 * User: JPB
 * Date: Feb 2, 2006
 * Time: 10:47:29 AM
 */
public class WflFunctionMnu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	private JMenuItem itemAddState = new JMenuItem("Agregar estado");
    private JMenuItem itemEdit = null;
    private JMenuItem itemAlign = null;
    private JMenuItem itemEditParallelism = null;
    private JMenuItem itemRemoveParallelism = null;
    private JMenuItem itemTestTokenValue = null;

    private WflParallelismAction editParallelismAction = null;
    private WflParallelismAction removeParallelismAction = null;

    private WflFunction function = null;
    private WflContainerControl control;
    
    private JMenu addShortCutMenu;

    public WflFunctionMnu(WflContainerControl control, WflFunction function) {
        this.function = function;
        this.control = control;

        editParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_EDIT, true, function);
        removeParallelismAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_REMOVE, true, function);
        itemRemoveParallelism = new JMenuItem(removeParallelismAction);
        itemEditParallelism = new JMenuItem(editParallelismAction);
        itemEdit = new JMenuItem(new WflEditComponentAction(control, function));
        itemTestTokenValue = new JMenuItem(new WflTestTokenValueAction(control, function));
        itemAlign = new WflAlignMnu(control);
        
        // Creo un menu con los estados que actualmente tienen atajos.
        addShortCutMenu = new JMenu("Crear atajo a...");
        addShortCutMenu.setEnabled(false);
        List shortCuts = control.getContainer().getWflComponents(WflStateShortCut.class);
        Set<String> statesWithShortCuts = new TreeSet<String>();
        Iterator i = shortCuts.iterator();
        while(i.hasNext()) {
        	WflStateShortCut sc = (WflStateShortCut)i.next();
        	statesWithShortCuts.add(sc.getTargetState().getId());
        }
        
        // Una vez que obtuve los id's de los estados, armo los items del menu.
    	List relatedStates = function.getRelatedComponents(WflPosFunction.class);
        i = statesWithShortCuts.iterator();
        while(i.hasNext()) {
        	String stateId = (String)i.next();
        	WflState state = (WflState)control.getContainer().getWflComponent(stateId);
        	
        	// Antes de agregarlo, debo verificar que la funcion no tiene actualmente coneccion
        	// con el estado mencionado.
        	boolean found = false;
        	for(int j=0; j<relatedStates.size(); j++) {
        		WflPosFunction pf = (WflPosFunction)relatedStates.get(j);
        		WflStateBObj s = (WflStateBObj)pf.getState().getBObj();
        		if( s.getId().equals(stateId)) {
        			found = true;
        			break;
        		}
        	}
        	if(!found) {
        		String text = state.getId() + " (" + ((WflStateBObj)state.getBObj()).getName() + ")";
        		WflAddPosFunctionWithShortCutAction action = new WflAddPosFunctionWithShortCutAction(control, function, state, text);
				addShortCutMenu.add(new JMenuItem(action));
				addShortCutMenu.setEnabled(true);
        	}
        }
        
        init();
    }

    private void init() {
        addListeners();
        addItems();
    }

    private void addItems() {
        add(itemAddState);
        addSeparator();
        add(addShortCutMenu);
        addSeparator();
        add(itemEditParallelism);
        add(itemRemoveParallelism);
        addSeparator();
        add(itemAlign);
        addSeparator();
        add(itemEdit);
        addSeparator();
        add(itemTestTokenValue);
    }

    private void addListeners() {
        itemAddState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addState();
            }
        });
        itemAddState.setIcon(WflState.createImageIcon());
    }

    private void addState() {
        control.addStateToFunction(function);
    }

    public void setEnableEditParallelism(boolean enable) {
        itemEditParallelism.setEnabled(enable);
        itemRemoveParallelism.setEnabled(enable);
    }
}

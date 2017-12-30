package crm.core.wfl.editor.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflFunction;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;
import crm.core.wfl.editor.gui.dlg.WflTestTokenValueDlg;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;


/**
 * Accion para chequear por que post funcion saldra cuando tiene un determinado token value.
 */
public class WflTestTokenValueAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
    private WflContainerControl control = null;
    private WflFunction function = null;

    public WflTestTokenValueAction(WflContainerControl control, WflFunction function) {
        super("CHECK_TOKEN_VALUE", WflUtil.createImageIcon(WflConstants.ICON_TOKEN_TEST));
    	this.control = control;
    	this.function = function; 
        putValue(NAME, "Testear valor de punto");
    }

    public void actionPerformed(ActionEvent e) {
    	this.control.deselectAll();
    	
    	long value = 0;
    	
    	WflTestTokenValueDlg dlg = new WflTestTokenValueDlg();
    	dlg.setTokenValue(value);
    	dlg.setVisible(true);
    	
    	if(dlg.wasCancel())
    		return;
    	
    	value = dlg.getTokenValue();
    	
    	List<WflComponent> l = (List<WflComponent>)function.getRelatedComponents();
    	
    	for(WflComponent c : l) {
    		if(c instanceof WflPosFunction) {
    			WflPosFunction pf = (WflPosFunction)c;
    			WflPosFunctionBObj pfb = (WflPosFunctionBObj)pf.getBObj();
    			String maskType = pfb.getMaskType();
    			String maskValue = pfb.getMaskValue();
    			if(maskType==null || 
    			   maskType.trim().length()==0 || 
    			   matchMask(maskType, Long.parseLong(maskValue), value))
    				pf.setSelected(true);
    		}
    	}
    }
    
	/**
	 * Realiza el matcheo de un valor contra una mascara.
	 * ATENCION: Esta es una copia de la funcion que esta en WFL.
	 * Por ahora se copio hasta que el editor tenga una dependencia con WFL.
	 * @param pMaskType Tipo de mascara.
	 * @param pMask Mascara.
	 * @param pValue Valor a matchear.
	 * @return true si la mascara matchea.
	 */
    public static boolean matchMask(String pMaskType, long pMask, long pValue)
    {
        if(pMaskType.trim().length()==0)
            return true;
        
        if(pMaskType.trim().equals("D"))
            return true;
        
        long matching = pMask & pValue;
        
        if(pMaskType.trim().equals("+"))
            return matching!=0;
        
        if(pMaskType.trim().equals("-"))
            return matching==0;
        
        return false;
    }    
}
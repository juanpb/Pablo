package crm.core.wfl.editor.gui.cmp;

import javax.swing.JToolTip;

/**
 * Extension de tooltip para hacerlo multilinea.
 * @author cm
 *
 */
class WflMultiLineToolTip extends JToolTip {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor vacio.
	 */
	public WflMultiLineToolTip() {
		setUI(new WflMultiLineToolTipUI());
	}
}
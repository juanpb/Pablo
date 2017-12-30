package crm.core.wfl.editor.gui.dlg;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * Editor de combo box para editar celdas de tablas.
 * @author cm
 *
 */
class WflComboBoxCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	public WflComboBoxCellEditor(String[] items) {
        super(new JComboBox(items));
        super.setClickCountToStart(1);
    }
    
    public WflComboBoxCellEditor(JComboBox combo) {
        super(combo);
        super.setClickCountToStart(1);
    }

}

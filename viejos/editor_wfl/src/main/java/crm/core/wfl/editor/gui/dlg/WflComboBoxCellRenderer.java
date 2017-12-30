package crm.core.wfl.editor.gui.dlg;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Render para combo box en tablas.
 * @author cm
 */
class WflComboBoxCellRenderer extends JComboBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	public WflComboBoxCellRenderer(String[] items) {
        super(items);
        super.setFont(new Font("dialog", Font.PLAIN, 10));
    }

	public WflComboBoxCellRenderer(String[] items, ListCellRenderer aRenderer) {
        super(items);
        super.setRenderer(aRenderer);
        super.setFont(new Font("dialog", Font.PLAIN, 10));
    }

	public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        // Select the current value
        setSelectedItem(value);
        return this;
    }
}

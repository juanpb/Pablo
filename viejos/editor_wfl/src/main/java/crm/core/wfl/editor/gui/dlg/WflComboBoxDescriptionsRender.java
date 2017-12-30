package crm.core.wfl.editor.gui.dlg;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Render para mostrar descripciones en un combo.
 * @author cm
 *
 */
public class WflComboBoxDescriptionsRender extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	/**
	 * Descripciones.
	 */
	private String[] descriptions;
	
	/**
	 * Codigos.
	 */
	private String[] codes;

	/**
	 * Constructor donde se indican las descripciones.
	 * @param codes Codigos a usar para la busqueda.
	 * @param descriptions Descripciones a usar.
	 */
	public WflComboBoxDescriptionsRender(String[] codes, String[] descriptions) {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		this.descriptions = descriptions;
		this.codes = codes;
	}

	/**
	 * ({@inheritDoc}
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		index = -1;
		for(int i=0; i<codes.length; i++)
			if(codes[i].equals(value)) {
				index = i;
				break;
			}
		
		if(index>=0 && index<descriptions.length)
			setText(descriptions[index]);
		else
			setText("");
		return this;
	}
}
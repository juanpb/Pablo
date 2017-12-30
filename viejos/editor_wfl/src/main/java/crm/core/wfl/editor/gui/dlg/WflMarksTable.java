package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.bobj.WflStateMarkBObj;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: JPB
 * Date: Mar 1, 2006
 * Time: 12:11:02 PM
 */
public class WflMarksTable extends JTable{
	private static final long serialVersionUID = 1L;
	private List<WflStateMarkBObj> stateMarks;
    private WflMarksTableModel model = null;

    public WflMarksTable() {
        init();
    }

    public java.util.List<WflStateMarkBObj> getStateMarks() {
        return stateMarks;
    }

    /**
     *
     * @param stateMarks lista de WflStateMarkBObj
     */
    public void setStateMarks(List<WflStateMarkBObj> stateMarks){
        //guardo una copia
        this.stateMarks = new ArrayList<WflStateMarkBObj>( stateMarks.size());
        for (int i = 0; i < stateMarks.size(); i++) {
        	WflStateMarkBObj o = stateMarks.get(i);
            this.stateMarks.add(o);
        }
        model.setStateMarks(stateMarks);
    }

    public boolean isCellEditable(int rowIndex, int vColIndex) {
        return vColIndex == 3;
    }

    private void init() {
        model = new WflMarksTableModel();
        super.setModel(model);
        String[] values = new String[]{"Alta", "Baja", "Modificación"};

        //Asigno el combo a la columna de "Acción"
        TableColumn col = super.getColumnModel().getColumn(3);
        col.setCellEditor(new WflComboBoxCellEditor(values));
        col.setCellRenderer(new WflComboBoxCellRenderer(values));

        //Asigno checkbox a la columna de "Borrable"
        col = super.getColumnModel().getColumn(2);
        col.setCellEditor(new CheckBoxEditor());
        col.setCellRenderer(new CheckBoxRenderer());

        //Ancho de las columnas
        TableColumnModel cm = getColumnModel();     //270
        cm.getColumn(0).setPreferredWidth(60); //8 +=- 1 char id
        cm.getColumn(1).setPreferredWidth(60); // nombre
        cm.getColumn(2).setPreferredWidth(60); // borrable
        cm.getColumn(3).setPreferredWidth(135); // acción
    }

    public List<WflStateMarkBObj> getSelected() {
        List<WflStateMarkBObj> res = new ArrayList<WflStateMarkBObj>();
        int[] sr = getSelectedRows();
        for (int i = 0; i < sr.length; i++) {
            int j = sr[i];
            res.add(stateMarks.get(j));
        }
        return res;
    }

    public void removeSelected() {
        List s = getSelected();
        for (int i = 0; i < s.size(); i++) {
            WflStateMarkBObj m = (WflStateMarkBObj) s.get(i);
            stateMarks.remove(m);
        }
        model.setStateMarks(stateMarks);
    }
}

class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	public CheckBoxRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
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

        if (value instanceof Boolean){
            Boolean b = (Boolean)value;
            super.setSelected(b.booleanValue());
        }
        else
            super.setSelected(false);
        return this;
    }
}

class CheckBoxEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	public CheckBoxEditor() {
        super(new JCheckBox());
    }
}

class WflMarksTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	List data = new ArrayList();
    public WflMarksTableModel() {
        this(null);
    }

    public void setStateMarks(List ms){
        data= ms;
        fireTableDataChanged();
    }
    public WflMarksTableModel(List ms) {
        data = ms ;
    }

    public String getColumnName(int column) {
        if (column == 0)
            return "Id";
        else if (column == 1)
            return "Nombre";
        else if (column == 2)
            return "Borrable";
        else if (column == 3)
            return "Acción";

        return "?";
    }

    public int getRowCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int rowIndex, int c) {
        WflStateMarkBObj row = (WflStateMarkBObj)data.get(rowIndex);
        WflMarkBObj m = row.getMark();
        if (c == 0)
            return m.getId();
        else if (c == 1)
            return m.getName();
        else if (c == 2){
            if (m.isDeletable())
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        }
        else if (c == 3)
            return row.getActionDescription();

        return "?";
    }

    public void setValueAt(Object nuevo, int f, int c){
        WflStateMarkBObj m = (WflStateMarkBObj)data.get(f);
        if (c == 3){//la única columna editable
            String a = WflStateMarkBObj.getActionByDescription(nuevo.toString());

            WflMarkBObj ms = m.getMark();
            if (!ms.isDeletable()
                    && WflStateMarkBObj.BAJA.equals(a)){
                String msg = "El hito no es borrable.";
                WflUtil.showMessage(msg, "Hito");
                return ;
            }
            m.setAction(a);
        }

    }
}
package crm.core.wfl.editor.gui.dlg;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import crm.core.wfl.editor.bobj.WflObjectConstraintBObj;

/**
 * Tabla de edicion de las restricciones de objetos asociadas a un xml.
 */
public class WflObjectConstraintsTable extends JTable{
	private static final long serialVersionUID = 1L;
	private WflObjectConstraintsTableModel model = null;

    /**
     * Constructor vacio.
     */
    public WflObjectConstraintsTable() {
        init();
    }

    /**
     * Obtiene la lista de constraints.
     * @return Lista de constraints.
     */
    public java.util.List<WflObjectConstraintBObj> getConstraints() {
        return model.getConstraints();
    }

    /**
     * Setea la lista de constraints.
     * @param constraints Constraints.
     */
    public void setConstraints(java.util.List<WflObjectConstraintBObj> constraints){
        model.setConstraints(constraints);
    }

    public boolean isCellEditable(int rowIndex, int vColIndex) {
        return true;
    }

    private void init() {
        model = new WflObjectConstraintsTableModel();
        super.setModel(model);
        
        //Asigno checkbox a la columnas de isNull y Unique.
        TableColumn col = super.getColumnModel().getColumn(1);
        col.setCellEditor(new CheckBoxEditor());
        col.setCellRenderer(new CheckBoxRenderer());
        col = super.getColumnModel().getColumn(2);
        col.setCellEditor(new CheckBoxEditor());
        col.setCellRenderer(new CheckBoxRenderer());

        //Ancho de las columnas
        TableColumnModel cm = getColumnModel();     
        cm.getColumn(0).setPreferredWidth(50); 
        cm.getColumn(1).setPreferredWidth(10);
        cm.getColumn(2).setPreferredWidth(10);
        cm.getColumn(3).setPreferredWidth(50);
    }

    public List<WflObjectConstraintBObj> getSelected() {
        List<WflObjectConstraintBObj> res = new ArrayList<WflObjectConstraintBObj>();
        int[] sr = getSelectedRows();
        for (int i = 0; i < sr.length; i++) {
            int j = sr[i];
            res.add(model.getConstraints().get(j));
        }
        return res;
    }

    public void removeSelected() {
        List s = getSelected();
        for (int i = 0; i < s.size(); i++) {
        	model.getConstraints().remove(s.get(i));
        }
        model.fireTableDataChanged();
    }
}

class WflObjectConstraintsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	java.util.List<WflObjectConstraintBObj> data = new ArrayList<WflObjectConstraintBObj>();
    public WflObjectConstraintsTableModel() {
        this(null);
    }

    public void setConstraints(java.util.List<WflObjectConstraintBObj> ms){
        data= ms;
        fireTableDataChanged();
    }

    public java.util.List<WflObjectConstraintBObj> getConstraints(){
        return data;
    }
    
    public WflObjectConstraintsTableModel(java.util.List<WflObjectConstraintBObj> ms) {
        data = ms ;
    }

    public String getColumnName(int column) {
    	switch(column) {
    	case 0:
    		return "Etiqueta";
    	case 1:
    		return "No Nulo";    		
    	case 2:
    		return "Unico";    		
    	case 3:
    		return "Tipo Proceso";    		
    		
    	}
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
        WflObjectConstraintBObj row = (WflObjectConstraintBObj)data.get(rowIndex);

        switch(c) {
    	case 0:
    		return row.getLabel();
    	case 1:
    		return row.isNotNull();    		
    	case 2:
    		return row.isUnique();    		
    	case 3:
    		return row.getTargetProcessDefinitionId();    		
        }
        return "?";
    }

    public void setValueAt(Object nuevo, int f, int c){

    	WflObjectConstraintBObj row = (WflObjectConstraintBObj)data.get(f);

        switch(c) {
    	case 0:
    		row.setLabel((String)nuevo);
    		break;    		
    	case 1:
    		row.setNotNull((Boolean)nuevo);
    		break;    		
    	case 2:
    		row.setUnique((Boolean)nuevo);
    		break;    		
    	case 3:
    		row.setTargetProcessDefinitionId((String)nuevo);
    		break;    		
        }
    }
}
package crm.core.wfl.editor.gui.dlg;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import crm.core.wfl.editor.bobj.WflPropertyBObj;

/**
 * Tabla de edicion de las propiedades asociadas a un xml.
 */
public class WflPropertiesTable extends JTable{
	private static final long serialVersionUID = 1L;
	private java.util.List<WflPropertyBObj> properties;
    private WflPropertiesTableModel model = null;

    /**
     * Constructor vacio.
     */
    public WflPropertiesTable() {
        init();
    }

    /**
     * Obtiene la lista de propiedads.
     * @return Lista de propiedades.
     */
    public java.util.List<WflPropertyBObj> getProperties() {
        return properties;
    }

    /**
     * Setea la lista de propiedades.
     * @param propiedades Propiedades
     */
    public void setProperties(java.util.List<WflPropertyBObj> properties){
        //guardo una copia
        this.properties = new ArrayList<WflPropertyBObj>(properties);
        model.setProperties(properties);
    }

    public boolean isCellEditable(int rowIndex, int vColIndex) {
        return true;
    }

    private void init() {
        model = new WflPropertiesTableModel();
        super.setModel(model);
        
        //Ancho de las columnas
        TableColumnModel cm = getColumnModel();     
        cm.getColumn(0).setPreferredWidth(100); 
    }

    public List<WflPropertyBObj> getSelected() {
        List<WflPropertyBObj> res = new ArrayList<WflPropertyBObj>();
        int[] sr = getSelectedRows();
        for (int i = 0; i < sr.length; i++) {
            int j = sr[i];
            res.add(properties.get(j));
        }
        return res;
    }

    public void removeSelected() {
        List s = getSelected();
        for (int i = 0; i < s.size(); i++) {
        	properties.remove(s.get(i));
        }
        model.setProperties(properties);
    }
}

class WflPropertiesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	java.util.List data = new ArrayList();
    public WflPropertiesTableModel() {
        this(null);
    }

    public void setProperties(java.util.List ms){
        data= ms;
        fireTableDataChanged();
    }
    public WflPropertiesTableModel(java.util.List ms) {
        data = ms ;
    }

    public String getColumnName(int column) {
        if (column == 0)
            return "Nombre";
        return "?";
    }

    public int getRowCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    public int getColumnCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int c) {
        WflPropertyBObj row = (WflPropertyBObj)data.get(rowIndex);

        switch(c) {
        	case 0:
        		return row.getName();
        }
        return "?";
    }

    public void setValueAt(Object nuevo, int f, int c){

        WflPropertyBObj row = (WflPropertyBObj)data.get(f);

        switch(c) {
        	case 0:
        		row.setName((String)nuevo);
        		break;
        }
    }
}
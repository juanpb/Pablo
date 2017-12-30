package p.aplic.prec;

import org.apache.log4j.Logger;
import p.aplic.prec.domain.PrecioDO;
import p.aplic.prec.domain.ProductoDO;
import p.aplic.prec.domain.SuperDO;
import p.util.Fechas;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ABMTableModelHist extends DefaultTableModel {
    private static final Logger logger = Logger.getLogger(ABMTableModel.class);

    private List<SuperDO> supers;
    private Object[][] modelo;

    public ABMTableModelHist(ProductoDO prod){
        try {
            supers = Model.getSupers();
            modelo = new Object[supers.size()][];
            String[] x = new String[supers.size()];
            int i = 0;
            for (SuperDO s : supers) {
                x[i++] = s.getDescripcion();
            }
            cargarProds(prod);
            setColumnIdentifiers(x);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void cargarProds(ProductoDO prod){
        int i = 0;
        for (SuperDO s : supers) {
            List<PrecioDO> colum = Model.getPrecios(prod, s);
            modelo[i++] = listToObject(colum);
        }
    }

    private Object[] listToObject(List<PrecioDO> colum){
        Object[] res = new Object[colum.size()];

        int i = 0;
        for (PrecioDO p : colum) {
            res[i++] = getFechaParaPlanilla(p.getFecha()) + " " +
                    getImporteParaPlanilla(p.getPrecio());
        }

        return res;
    }

    private String getImporteParaPlanilla(double d){
        return Double.toString(d);
    }
    private String getFechaParaPlanilla(Date d){
        return "(" + Fechas.getDDMMAA("/", d.getTime(), true) + ")";
    }

    public int getRowCount(){
        if (modelo == null)
            return 0;

        int max = 0;
        for (Object[] col : modelo) {
            if (col != null && max < col.length)
                max = col.length;
        }
        return max;
    }

    public int getColumnCount(){
        return modelo.length;
    }

    public Object getValueAt(int f, int c){
        Object[] col = modelo[c];
        if (col.length <= f)
            return null;
        else
            return col[f];
    }

    public void setValueAt(Object nuevo, int f, int c){
        System.out.println("??????nuevo = " + nuevo);
    }
}

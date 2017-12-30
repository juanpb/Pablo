package p.aplic.prec;

import p.aplic.prec.domain.PrecioDO;
import p.aplic.prec.domain.ProductoDO;
import p.aplic.prec.domain.SuperDO;
import p.util.Fechas;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ABMTableModel extends DefaultTableModel {
    private List<PrecioDO> filas = new ArrayList();
    public static final int CANT_COL = 5;

    public static final int COLUMNA_SUPER = 0;
    public static final int COLUMNA_PRODUCTO = 1;
    public static final int COLUMNA_PRECIO = 2;
    public static final int COLUMNA_FECHA = 3;
    public static final int COLUMNA_PRECIO_ANTERIOR = 4;


    public ABMTableModel(){
        setColumnIdentifiers(new String[]{
                "Super", "Producto", "Precio", "Fecha", "Anterior"});
    }


    public boolean isCellEditable(int row, int column) {
        //solo se puede editar la columna de precios
        return column == COLUMNA_PRECIO;
    }

    public int getRowCount(){
        if (filas == null)
            return 0;
        else
            return filas.size();
    }

    public int getColumnCount(){
        return CANT_COL;
    }

    public Object getValueAt(int f, int c){
        if (f > getRowCount())
            return "f > getRowCount()";
        if (c > CANT_COL)
            return "c > this.CANT_COL";


        PrecioDO p = filas.get(f);

        switch (c)
        {
            case COLUMNA_FECHA:
                return Fechas.getAAAAMMDD("-", p.getFecha().getTime());
            case COLUMNA_PRECIO:
                return p.getPrecio();
            case COLUMNA_PRODUCTO:
                return p.getProducto().toString();
            case COLUMNA_SUPER:
                return p.getSuperM();
            case COLUMNA_PRECIO_ANTERIOR:
                return Model.getUltimoPrecioParaPlanilla(p.getProducto(), 
                        p.getSuperM());
            default:
                return "falló switch";
        }
    }

    public void setValueAt(Object nuevo, int f, int c){
        if (f > getRowCount()){
            System.out.println("setValueAt(...), f > getRowCount()");
            return;
        }
        if (c > CANT_COL){
            System.out.println("setValueAt(...), c > this.CANT_COL");
            return ;
        }

        PrecioDO p = filas.get(f);
        if (c == COLUMNA_FECHA){
            p.setFecha((Date) nuevo);
        }
        else if (c == COLUMNA_PRECIO){
            p.setPrecio(Double.parseDouble(nuevo.toString()));
        }
        else if (c == COLUMNA_PRODUCTO){
            p.setProducto((ProductoDO) nuevo);
        }
        else if (c == COLUMNA_SUPER){
            p.setSuperM((SuperDO) nuevo);
        }
        else
            System.out.println("setValue(...), no se implementó para esta columna");
    }


    public void clear(){
        filas.clear();
        fireTableDataChanged();
    }

    public void addFila(PrecioDO f){
        filas.add(f);
        fireTableDataChanged();
    }

    public void removeFila(int nro){
        filas.remove(nro);
        fireTableDataChanged();
    }

    public List<PrecioDO> getFilas(){
        return filas;
    }

    public PrecioDO getFila(int fila){
        return filas.get(fila);
    }



}

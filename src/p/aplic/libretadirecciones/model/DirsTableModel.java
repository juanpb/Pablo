package p.aplic.libretadirecciones.model;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.aplic.libretadirecciones.Util;
import p.aplic.libretadirecciones.bobj.Direccion;
import p.util.UtilString;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DirsTableModel extends DefaultTableModel{
    private static final Logger logger = Logger.getLogger(DirsTableModel.class);

    private List<Direccion> filas = null;
    private List<Direccion> filasTodas = null;

    public static final int CANT_COL = 8;
    private static int CANT_COL_VISIBLES = CANT_COL;

    public static final int COLUMNA_APELLIDO_NOMBRE = 0;
    public static final int COLUMNA_TELEFONO = 1;
    public static final int COLUMNA_MOVIL = 2;
    public static final int COLUMNA_CORREO = 3;
    public static final int COLUMNA_DIRECCION = 4;
    public static final int COLUMNA_NOTA = 5;
    public static final int COLUMNA_TEL_TRABAJO = 6;
    public static final int COLUMNA_CATEGORIA = 7;

    private boolean[] visibles = new boolean[CANT_COL];
    private String categoria = null;
    private boolean verHistoricos = false;

    public DirsTableModel() {
        for (int i = 0; i < visibles.length; i++) {
            visibles[i] = true;
        }
        visibles[COLUMNA_DIRECCION] = false;
        visibles[COLUMNA_NOTA] = false;
        visibles[COLUMNA_CATEGORIA] = false;
        CANT_COL_VISIBLES = CANT_COL - 3;

        try {
            filas = Model.getDirecciones();
            filasTodas = new ArrayList<Direccion>(filas.size());
            for (Direccion d : filas) {
                filasTodas.add(d);
            }

            //Títulos de las columnas
            //noinspection CollectionDeclaredAsConcreteClass,UseOfObsoleteCollectionType
            Vector colIds = new Vector();
            if (visibles[COLUMNA_APELLIDO_NOMBRE])
                colIds.add("Nombre");
            if (visibles[COLUMNA_TELEFONO])
                colIds.add("Casa");
            if (visibles[COLUMNA_MOVIL])
                colIds.add("Movil");
            if (visibles[COLUMNA_CORREO])
                colIds.add("Correo");
            if (visibles[COLUMNA_DIRECCION])
                colIds.add("Direccion");
            if (visibles[COLUMNA_NOTA])
                colIds.add("Nota");
            if (visibles[COLUMNA_TEL_TRABAJO])
                colIds.add("Trabajo");
            if (visibles[COLUMNA_CATEGORIA])
                colIds.add("Categoria");
            setColumnIdentifiers(colIds);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (JDOMException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void setColVisible(int col, boolean visible){
        visibles[col] = visible;
        if (visible)
            CANT_COL_VISIBLES = CANT_COL_VISIBLES++;
        else
            CANT_COL_VISIBLES = CANT_COL_VISIBLES--;
    }

    public int getRowCount(){
        if (filas == null)
            return 0;
        else
            return filas.size();
    }

    public int getColumnCount(){
        return CANT_COL_VISIBLES;
    }

    public Object getValueAt(int f, int c){
        if (f > getRowCount())
            return "f > getRowCount()";
        if (c > CANT_COL_VISIBLES)
            return "c > this.CANT_COL_VISIBLES";
        c = getColumnaVisible(c);

        Direccion dir = filas.get(f);
        if (c == COLUMNA_APELLIDO_NOMBRE){
            String ape = dir.getAppellido();
            String nom = dir.getNombre();
            if (ape == null || ape.equals("")){
                if (nom == null || nom.equals(""))
                    return "";
                else
                    return nom;
            }
            if (nom == null || nom.equals(""))
                return ape;
            else
                return ape +", " + nom;
        }
        if (c == COLUMNA_TELEFONO){
            return dir.getTelCasa();
        }
        if (c == COLUMNA_MOVIL){
            return dir.getMovil();
        }
        if (c == COLUMNA_CORREO){
            return dir.getCorreo();
        }
        if (c == COLUMNA_DIRECCION){
            return dir.getDirec();
        }
        if (c == COLUMNA_NOTA){
            return dir.getNotas();
        }
        if (c == COLUMNA_TEL_TRABAJO){
            return dir.getTelTrabajo();
        }
        if (c == COLUMNA_CATEGORIA){
            return UtilString.getValoresSeparadosPorComa(dir.getCategorias());
        }

        return "epa";
    }

    private int getColumnaVisible(int c) {
        int vis = -1;
        for (int i = 0; i <= CANT_COL; i++) {
            if (visibles[i]){
                vis++;
            }
            if(vis == c)
                return i;
        }

        return 0;
    }

    public Direccion getDir(int i) {
        return filas.get(i);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void actualizar(Direccion vieja, Direccion nueva) throws IOException {
        grabar(vieja, nueva);
        fireTableDataChanged();
    }

    public void actualizar() throws Exception {
        filtrarPorCategoria(categoria);
    }

    private void grabar(Direccion vieja, Direccion nueva) throws IOException {
        Model.modificar(vieja, nueva);
    }

    public int getPosicion(Direccion v){
        for (int i = 0; i < filas.size(); i++) {
            Direccion d = filas.get(i);
            if (d.equals(v))
                return i;
        }
        return -1;
    }

    public void verHistoricos(boolean b) {
        verHistoricos = b;
        try {
            filasTodas = Model.getDirecciones();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        filas =  Util.filtrar(categoria, verHistoricos, filasTodas);
        fireTableDataChanged();
    }

    public void filtrarPorCategoria(String nc) {
        categoria = nc;
        try {
            filasTodas = Model.getDirecciones();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        filas =  Util.filtrar(categoria, verHistoricos, filasTodas);
        fireTableDataChanged();
    }

    public List<Direccion> getDireccionesVisibles(){
        return filas;
    }
}

package p.dm;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Oct 2, 2008
 * Time: 10:24:28 AM
 */
public class DataSet {
    public enum Clase {
        STRING, BOOLEAN, ENUM, NUMERICO;
    }

    private List<Clase> clases;
    private List<String> nombres;
    private List<String> otrasLineas;
    private List<Registro> registros;

    public DataSet() {
        clases = new ArrayList<Clase>();
        nombres = new ArrayList<String>();
        registros = new ArrayList<Registro>();
        otrasLineas = new ArrayList<String>();
    }

    public void add(Registro element) {
        registros.add(element);
    }

    public void add(int index, Registro element) {
        registros.add(index, element);
    }

    public Registro set(int index, Registro element) {
        return registros.set(index, element);
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    public List<String> getNombres() {
        return nombres;
    }

    public void setNombres(List<String> nombres) {
        this.nombres = nombres;
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }

    public List<String> getOtrasLineas() {
        return otrasLineas;
    }

    public void setOtrasLineas(List<String> otrasLineas) {
        this.otrasLineas = otrasLineas;
    }

    public boolean addOtraLinea(String s) {
        return otrasLineas.add(s);
    }
}

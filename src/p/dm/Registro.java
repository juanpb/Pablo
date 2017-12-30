package p.dm;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Oct 2, 2008
 * Time: 10:27:10 AM
 */
public class Registro {
    private List<Object> atributos;

    public Registro() {
        this(new ArrayList<Object>());
    }

    public Registro(List<Object> atributos) {
        this.atributos = atributos;
    }

    public boolean add(Object o) {
        return atributos.add(o);
    }

    public Object get(int index) {
        return atributos.get(index);
    }

    public void add(int index, Object element) {
        atributos.add(index, element);
    }

    public Object set(int index, Object element) {
        return atributos.set(index, element);
    }

    public List<Object> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Object> atributos) {
        this.atributos = atributos;
    }

    public String toStringEntrecomillado() {
        String res = "";
        for(Object atr : getAtributos()) {
            if (atr != null)
                res += "'" + atr.toString() + "'";
            else
                res += "null";

            res += ", ";
        }
        if (res.endsWith(", "))
            res = res.substring(0, res.length() - 2);

        return res;
    }
    
    public String toString(String separador) {
        String res = "";
        for(Object atr : getAtributos()) {
            if (atr != null)
                res += atr.toString();
            else
                res += "null";

            res += separador;
        }
        if (res.endsWith(separador))
            res = res.substring(0, res.length() - separador.length());

        return res;
    }
}

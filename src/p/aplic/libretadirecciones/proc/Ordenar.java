package p.aplic.libretadirecciones.proc;

import org.jdom.JDOMException;
import p.aplic.libretadirecciones.bobj.Direccion;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: JP
 * Date: 03/05/2005
 * Time: 01:52:06
 */
public class Ordenar {

    public static void main(String[] args) throws IOException, JDOMException {
        String a = "auto";
        String b = "barcoauto";
        String c = "casabarcoauto";

        System.out.println("a.compareTo(b) = " + a.compareTo(b));
        System.out.println("c.compareTo(b) = " + c.compareTo(b));
//        List dirs = Model.getDirecciones();
//        Collections.sort(dirs, new ElCompa());
//        String salida = "C:\\Documents and Settings\\JP\\Escritorio\\agenda\\ordenado.xml";
//        Model.guardar(dirs, salida);
    }

    public static void ordenar(List dirs){
        Collections.sort(dirs, new ElCompa());
    }
}

class ElCompa implements Comparator{
    public int compare(Object o1, Object o2) {
        Direccion d1 = (Direccion)o1;
        Direccion d2 = (Direccion)o2;

        String n1 = getNombre(d1).toLowerCase();
        String n2 = getNombre(d2).toLowerCase();


        int res = n1.compareTo(n2);
        return res;
    }

    private String getNombre(Direccion dir) {
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
            return ape + ", " + nom;
    }

}
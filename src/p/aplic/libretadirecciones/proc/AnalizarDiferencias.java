package p.aplic.libretadirecciones.proc;

import org.jdom.JDOMException;
import p.aplic.libretadirecciones.bobj.Direccion;
import p.aplic.libretadirecciones.model.Model;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Dos direcciones son iguales si coinciden el nombre y el apellido
 * User: JPB
 * Date: May 3, 2005
 * Time: 3:41:31 PM
 */
public class AnalizarDiferencias {
    private List _soloD1 = new Vector();
    private List _soloD2 = new Vector();
    private Map  _difs = new HashMap();

    public AnalizarDiferencias(List pDirs1, List pDirs2){
        List dirs1 = copiar(pDirs1);
        List dirs2 = copiar(pDirs2);

        while (dirs1.size() > 0) {
            Direccion d1 = (Direccion) dirs1.get(0);
            analizar(d1, dirs2);
            dirs1.remove(0);
        }
        for (int i = 0; i < dirs2.size(); i++) {
            Direccion d = (Direccion) dirs2.get(i);
            _soloD2.add(d);
        }
    }

    private void analizar(Direccion d1, List dirs2) {
        Direccion d2 = buscar(d1, dirs2);
        if (d2 == null){
            _soloD1.add(d1);
        }
        else{
            dirs2.remove(d2);
            if (!d2.equals(d1))
                _difs.put(d1, d2);
        }
    }

    private Direccion buscar(Direccion d1, List dirs2) {
        for (int i = 0; i < dirs2.size(); i++) {
            Direccion d = (Direccion) dirs2.get(i);
            if (d.mismaPersona(d1))
                return d;
        }
        return null;
    }

    private List copiar(List pDirs1) {
        List res = new Vector(pDirs1.size());
        for (int i = 0; i < pDirs1.size(); i++) {
            Direccion d = (Direccion) pDirs1.get(i);
            res.add(d);
        }
        return res;
    }

    public List getSoloD1() {
        return _soloD1;
    }

    public List getSoloD2() {
        return _soloD2;
    }
    public Map getDifs() {
        return _difs;
    }


    public static void main(String[] args) throws IOException, JDOMException {
        String xml1 = "C:\\IntelliJ-IDEA-4.0\\Pablo\\src\\p\\aplic\\ordenado.xml";
        String xml2 = "C:\\IntelliJ-IDEA-4.0\\Pablo\\src\\p\\aplic\\ordenado2.xml";
        List dirs1 = Model.getDirecciones(new File(xml1));
        List dirs2 = Model.getDirecciones(new File(xml2));

        AnalizarDiferencias ad = new AnalizarDiferencias(dirs1, dirs2);

        List soloD1 = ad.getSoloD1();
        System.out.println("Solo en d1:");
        mostrar(soloD1);

        System.out.println("");
        System.out.println("Solo en d2:");
        List soloD2 = ad.getSoloD2();
        mostrar(soloD2);

        System.out.println("");
        System.out.println("Diferencias:");
        Map difs = ad.getDifs();
        Iterator it = difs.keySet().iterator();
        while (it.hasNext()) {
            Direccion d = (Direccion) it.next();
            Object d2 = difs.get(d);
            System.out.println("////////////");
            System.out.println("d1 = " + d);
            System.out.println("d2 = " + d2);
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        }

    }

    private static void mostrar(List soloD2) {
        for (int i = 0; i < soloD2.size(); i++) {
            Direccion d = (Direccion) soloD2.get(i);
            System.out.println("d.getNombre() = " + d.toString());
            System.out.println("------");
        }
    }
}

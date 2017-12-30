package p.aplic.libretadirecciones;

import p.aplic.libretadirecciones.bobj.Direccion;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: May 10, 2005
 * Time: 5:53:58 PM
 */
public class Util {

    public static List<Direccion> filtrar(String categoria,
                                          boolean incluirHistoricos,
                                          List<Direccion> dir){
        List<String> cs = new ArrayList<String>();
        cs.add(categoria);
        return filtrar(cs, incluirHistoricos,  dir);
    }

    public static List<Direccion> filtrar(List<String> categorias,
                                          boolean incluirHistoricos,
                                          List<Direccion> dir){
        List<Direccion> res = filtrarPorHistóricos(incluirHistoricos, dir);
        res = filtrarPorCategorias(categorias, res);
        return res;
    }

    private static  List<Direccion> filtrarPorHistóricos(
            boolean incluirHistoricos, List<Direccion> dir){
        if (incluirHistoricos)
            return dir;

        List<Direccion> res = new ArrayList<Direccion>();

        for (Direccion d : dir) {
            if (!d.isHistorico())
                res.add(d);
        }

        return res;
    }

    private static List<Direccion> filtrarPorCategoria(String cat,
                                                      List<Direccion> dir){
        if (cat == null || cat.equals("Todas"))
            return dir;
        List<Direccion> res = new ArrayList<Direccion>();

        for (Direccion d : dir) {
            List<String> cates = d.getCategorias();
            if (cates != null){
                if (cates.contains(cat))
                    res.add(d);
            }
        }

        return res;
    }

    private static List<Direccion> filtrarPorCategorias(List<String> cat,
                                                        List<Direccion> dir){
        if (cat == null || cat.contains("Todas"))
            return dir;
        List<Direccion> res = new ArrayList<Direccion>();

        for(String c : cat) {
            res.addAll(filtrarPorCategoria(c, dir));
        }

        return res;
    }
}

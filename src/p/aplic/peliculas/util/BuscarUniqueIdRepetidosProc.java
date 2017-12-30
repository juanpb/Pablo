package p.aplic.peliculas.util;

import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: JPB
 * Date: 12/12/11
 * Time: 8:45 AM
 */
public class BuscarUniqueIdRepetidosProc {
    public BuscarUniqueIdRepetidosProc(String[] args) {
                String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = "D:\\P\\java\\codigo\\Pablo\\config\\pelis\\películas.xml";

        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath);
        } catch (Exception e) {
            Log.error(e);
            return ;
        }
        List<Integer> repet = new ArrayList<Integer>();
        Set<Integer> set = new HashSet<Integer>();
        for (Pelicula p : pelis.getPeliculas()) {
            Integer uniqueId = p.getUniqueId();
            if (set.contains(uniqueId)){
                repet.add(uniqueId);
            }
            else{
                set.add(uniqueId);
            }
        }
        if (repet.size() == 0)
            System.out.println("Sin repetidos");
        else{
            System.out.println("repet = " + repet);
        }

        //busco hueco
        for(int i = 1; i < 1312; i++) {
            if (!set.contains(i))
                System.out.println("hueco: " + i);
        }
    }

    public static void main(String[] args) {
        args = new String[1];
        args[0] = "D:\\P\\java\\codigo\\Pablo\\config\\pelis\\config.ini";
        new BuscarUniqueIdRepetidosProc(args);
    }
}

package p.aplic.peliculas.util;

import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Administrador
 * Date: 26/03/2007
 * Time: 21:32:22
 */
public class EliminarPosterRepetidos {
    public EliminarPosterRepetidos(String[] args) {
                String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");


        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath);
        } catch (Exception e) {
            Log.error(e);
            return ;
        }
        for (Pelicula p : pelis.getPeliculas()) {
            List<String> posters = p.getPosters();
            Set<String> set = new HashSet<String>();
            set.addAll(posters);
            p.setPosters(new ArrayList<String>());
            for (String aSet : set) {
                p.getPosters().add(aSet);
            }
        }
        Util.grabar(xmlPath, pelis);
    }

    public static void main(String[] args) {
        args = new String[1];
        args[0] = "F:\\Java\\ProyectosJava\\Pablo\\src\\p\\aplic\\peliculas\\config.ini";
        new EliminarPosterRepetidos(args);
    }
}

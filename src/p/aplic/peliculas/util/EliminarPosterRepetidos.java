package p.aplic.peliculas.util;

import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;

import java.io.IOException;
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
    public EliminarPosterRepetidos(String[] args) throws IOException, JDOMException {
                String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");
        List<Tag> tags = Util.getTags(System.getProperty("tags"));

        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath, tags);
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

    public static void main(String[] args) throws IOException, JDOMException {
        args = new String[1];
        args[0] = "F:\\Java\\ProyectosJava\\Pablo\\src\\p\\aplic\\peliculas\\config.ini";
        new EliminarPosterRepetidos(args);
    }
}

package p.aplic.peliculas.util;

import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A partir de un directorio con fotos (posters) devuelve lista con nombre de plis
 */
public class FotosAPelis {
    private  static String fotosPath = "D:\\P\\_env\\_\\_\\Pelis2";
    private Peliculas pelis;

    public static void main(String[] args) throws IOException, JDOMException {
        new FotosAPelis(fotosPath);
    }

    private FotosAPelis(String dir) throws IOException, JDOMException {

        String configPath = "D:\\P\\_env\\config\\pelis\\config.ini";
        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");
        List<Tag> tags = Util.getTags(System.getProperty("tags"));

        List<String> noEncontrado = new ArrayList<String>();
        List<Pelicula> noHD = new ArrayList<Pelicula>();
        List<Pelicula> encontrado = new ArrayList<Pelicula>();

        try {
            pelis = Util.getPeliculas(xmlPath, tags);
        } catch (Exception e) {
            Log.error(e);
            return ;
        }

        File fileDir = new File(fotosPath);
        final File[] files = fileDir.listFiles();
        for (File f : files) {
            final String name = f.getName();
            final Pelicula p = Util.getPeliculaPorFoto(name, pelis);
            if (p == null)
                noEncontrado.add(name);
            else if (p.isHD())
                encontrado.add(p);
            else
                noHD.add(p);
        }

        System.out.println("no encontrado:");
        for (String s : noEncontrado) {
            System.out.println(s);
        }

        System.out.println("");
        System.out.println("No hd:");
        imprimir(noHD);

        System.out.println("");
        System.out.println("Bingo:");
        imprimir(encontrado);

    }

    private void imprimir(List<Pelicula> list) {
        for (Pelicula s : list) {
            System.out.println(s.getNombre() + " (" + s.getAnnus() + ")");
        }
    }

}

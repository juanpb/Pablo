package p.aplic.peliculas.util;

import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 18, 2008
 * Time: 11:49:46 AM
 */
public class ExistePoster {
    private String dirRaizFotos;
    private int contExisten = 0;
    private int contNoExisten = 0;

    public static void main(String[] args) throws IOException {
        new ExistePoster(args);
    }

    public ExistePoster(String[] args) throws IOException {
        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");
        dirRaizFotos = System.getProperty("dirRaizFotosCompleto");
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
            buscarEImprimir(posters);
        }
        System.out.println("contExisten = " + contExisten);
        System.out.println("contNoExisten = " + contNoExisten);
        
    }

    private void buscarEImprimir(List<String> ps){
        for (String p : ps) {
            File file = new File(dirRaizFotos, p);
            if (file.exists()){
                contExisten++;
            }
            else{
                System.out.println(p);
                contNoExisten++;
            }
        }
    }
}

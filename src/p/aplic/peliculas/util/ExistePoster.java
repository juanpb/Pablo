package p.aplic.peliculas.util;

import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;

import java.io.File;
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

    public static void main(String[] args) {
        new ExistePoster(args);
    }

    public ExistePoster(String[] args) {
        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");
        dirRaizFotos = System.getProperty("dirRaizFotosCompleto");

        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath);
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

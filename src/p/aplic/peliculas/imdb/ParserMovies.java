package p.aplic.peliculas.imdb;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;
import p.aplic.peliculas.util.Util;
import p.util.UtilFile;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;

/**
 * User: JPB
 * Date: Apr 15, 2009
 * Time: 10:22:31 AM
 */
public class ParserMovies {
    private static final Logger logger = Logger.getLogger(ParserMovies.class);

    //    private static final String filePath = "D:\\P\\_env\\config\\pelis\\movies.list";
    private static final String SEPARADOR_CAMPOS = "|";
    private static final String IGNORAR_HASTA = "===========";
    private static final String IGNORAR_DESDE = "\"#1 College Sports Show, The\" (2004)\t\t\t2004-????";
    private static List<String> lineasDesconocidas = new ArrayList<String>();

    public static void main(String[] args) throws JDOMException, IOException {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        crearLocalMovieList();
    }

    public static void crearLocalMovieList() throws JDOMException, IOException {
        List<Tag> tags = Util.getTags(System.getProperty("tags"));
        Peliculas misPelis = Util.getPeliculas(System.getProperty("xmlPath"), tags);
        Set<Peli> pelis = getMovies();
        List<Peli> salida = new ArrayList<Peli>();

        int id = 1;

        for(Pelicula p : misPelis.getPeliculas()) {
            Peli pe = Peli.get(pelis, p);
            if (pe != null){
                pe.setId(id+"");
                p.setImdbId(id+"");
                salida.add(pe);
                id++;
            }
            else
                System.out.println("No encontrado: " + p.getNombre());
        }
        Util.grabar(System.getProperty("xmlPath"), misPelis);
        grabar(salida, System.getProperty("localMoviesListPath"));
    }

    private static void grabar(List<Peli> salida, String path) throws IOException {

        List<String> aGrabar = new ArrayList<String>(salida.size());
        for(Peli p : salida) {
            String txt = p.getId() + SEPARADOR_CAMPOS + p.getNombre() +
                    SEPARADOR_CAMPOS + p.getAño()+
                    SEPARADOR_CAMPOS + p.getLinea();
            aGrabar.add(txt);
        }
        UtilFile.guardartArchivoPorLinea(new File(path), aGrabar, true);
    }

    public static Set<Peli> getMovies(){
        String filePath = System.getProperty("moviesListPath");

        LineNumberReader lnr = null;
        Set<Peli> res = new HashSet<Peli>();
        try{
            lnr = new LineNumberReader(new FileReader(filePath));
            boolean seguir = true;

            //ignoro las primeras líneas
            while(seguir){
                String linea = lnr.readLine();
                if (linea.equals(IGNORAR_HASTA))
                    seguir = false;
            }
            seguir = true;
            while(seguir){
                String linea = lnr.readLine();
                if (linea.equals(IGNORAR_DESDE))
                    seguir = false;
                else{
                    Peli e = parsear(linea);
                    if (e != null){
                        res.add(e);
                    }
                }
            }

            return res;
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally{
            if (lnr != null)
                try {
                    lnr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    public static Set<Peli> getLocalMovies(){
        String filePath = System.getProperty("localMoviesListPath");

        Set<Peli> res = new HashSet<Peli>();
        try{
            List<String> lst = UtilFile.getArchivoPorLinea(filePath);
            for(String s : lst) {
                Peli p = parsearMLP(s);
                if (p != null)
                    res.add(p);
            }
            return res;
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    //formato de linea = imdbId|nombre|año|linea original
    //ej: 735|The Reader|2008|Reader, The (2008)					2008
    private static Peli parsearMLP(String linea) {
        StringTokenizer st = new StringTokenizer(linea, "|");
        try {
            String id = st.nextToken();
            String n = st.nextToken();
            String an = st.nextToken();
            String or = st.nextToken();
            return new Peli(n, an, or, id);
        }
        catch (Exception ex) {
            return null;
        }
    }


    private static Peli parsear(String linea) {
        if (linea == null ||linea.trim().equals(""))
            return null;
        int d = linea.indexOf("(");
        int h = linea.indexOf(")");
        if (d < 2){//ej: (15)abc (1976)
            d = linea.indexOf("(", d+1);
            h = linea.indexOf(")", h+1);
            if (d < 2){
                lineasDesconocidas.add(linea);
                return null;
            }
        }

        try {

            if (h < d) //ej: 1)abc (1999)
                h = linea.indexOf(")", h+1);

            String n = linea.substring(0, d-1);
            String a = linea.substring(d+1, d+5);
            if (!"????".equals(a)){
                try {
                    Integer.parseInt(a);
                }
                catch (Exception ex) {
                    try {
    //ej: abc (fgh) (1994)
                        d = linea.indexOf("(", h+1);
                        n = linea.substring(0, d-1);
                        a = linea.substring(d+1, d+5);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            return new Peli(n, a, linea);
        } catch (Exception e) {
            lineasDesconocidas.add(linea);
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}

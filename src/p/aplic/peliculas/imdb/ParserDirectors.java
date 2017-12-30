package p.aplic.peliculas.imdb;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.util.Constantes;
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
public class ParserDirectors {
    private static final Logger logger = Logger.getLogger(ParserDirectors.class);

    private static final String SEPARADOR_CAMPOS = "|";
    public static final String IGNORAR_HASTA = "----\t\t\t------";
    public static final String IGNORAR_DESDE = "-----------------------------------------------------------------------------";
    private static int contNoRef = 0;
    private static int contRef = 0;
    private static String ANO_DESCONOCIDO = "????";

    public static void main(String[] args) throws JDOMException, IOException {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
//        crearLocalDirectorsList();
        Set<Persona> localList = getLocalList();
        Set<Peli> localMovies = ParserMovies.getLocalMovies();
        int contCD=0;
        int contSD=0;
        for(Peli p : localMovies) {
            boolean conDir = false;
            for(Persona d : localList) {
                for(Peli pd : d.getPelis()) {
                    conDir |= p.getId().equals(pd.getId());
                }
            }
            if (!conDir){
                contSD++;
                System.out.println("Sin dir = " + p.getNombre());
            }
            else
                contCD++;
        }
        System.out.println("contSD = " + contSD);
        System.out.println("contCD = " + contCD);
    }

    public static void crearLocalDirectorsList() throws JDOMException, IOException {
        Set<Peli> localMovies = ParserMovies.getLocalMovies();
        Set<Persona> dirs = getDirectorsReferenciados(localMovies);

        List<Persona> salida = new ArrayList<Persona>();

        for(Persona d : dirs) {
            List<Peli> pelisRef = new ArrayList<Peli>();
            List<Peli> pelisNoRef = new ArrayList<Peli>();
            for(Peli p : d.getPelis()) {
                String s = getLocalId(p, localMovies);
                if (s!= null){
                    p.setId(s);
                    pelisRef.add(p);
                }
                else
                    pelisNoRef.add(p);
            }
//            pelisRef.addAll(pelisNoRef); //las junto
            d.setPelis(pelisRef);
            salida.add(d);
        }
        grabar(salida, System.getProperty("localDirectorsListPath"));
    }


    private static Set<Persona> unirPelis(Set<Persona> d){
        Map<String, Persona> m = new HashMap<String, Persona>();
        Set<Persona> res = new HashSet<Persona>();
        Iterator<Persona> it = d.iterator();
        while (it.hasNext()) {
            Persona o = it.next();
            Persona dir = m.get(o.getNombre());
            if (dir != null){
                dir.addPelis(o.getPelis());
            }
            else{
                m.put(o.getNombre(), o);
                res.add(o);
            }
        }
        return res;
    }

    public static Set<Persona> getLocalList() throws JDOMException, IOException {
        String filePath = System.getProperty("localDirectorsListPath");

        Set<Persona> res = new HashSet<Persona>();
        try{
            List<String> lst = UtilFile.getArchivoPorLinea(filePath);
            for(String s : lst) {
                Persona d = parsearDL(s);
                if (d != null)
                    res.add(d);
            }

            return unirPelis(res);
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    //formato de linea = nombreDir|imdbId|nombrePel|añoPel
    //ej: Curtiz, Michael|252|Casablanca|1942
    private static Persona parsearDL(String linea) {
        StringTokenizer st = new StringTokenizer(linea, "|");
        try {
            String nd = st.nextToken();
            String id = st.nextToken();
            String np = st.nextToken();
            String an = st.nextToken();
            Peli p = new Peli(np, an, null, id);
            List<Peli> ps = new ArrayList<Peli>();
            ps.add(p);
            return new Persona(nd, ps);
        }
        catch (Exception ex) {
            return null;
        }
    }

    //formato: dir|peliId|peliNombre|año (por línea)
    private static void grabar(List<Persona> salida, String path) throws IOException {

        List<String> aGrabar = new ArrayList<String>(salida.size());
        for(Persona d : salida) {
            String n = d.getNombre();
            for(Peli p : d.getPelis()) {
                String txt = n + SEPARADOR_CAMPOS + p.getId() +
                    SEPARADOR_CAMPOS + p.getNombre()+
                    SEPARADOR_CAMPOS + p.getAño();
                aGrabar.add(txt);
            }

        }
        UtilFile.guardartArchivoPorLinea(new File(path), aGrabar, true);
    }

    /**
     *
     * @param pelisLocal  si es null => devuelve todos
     * @return
     */
    public static Set<Persona> getDirectorsReferenciados(Set<Peli> pelisLocal){
        String filePath = System.getProperty("directorsListPath");

        LineNumberReader lnr = null;
        Set<Persona> res = new HashSet<Persona>();
        try{
            lnr = new LineNumberReader(new FileReader(filePath));
            boolean seguir = true;

            //ignoro las primeras líneas
            while(seguir){
                String linea = lnr.readLine();
                if (linea != null && linea.equals(IGNORAR_HASTA))
                    seguir = false;
            }
            seguir = true;
            Persona d=null;
            while(seguir){
                String linea = lnr.readLine();
                if (linea.equals(IGNORAR_DESDE))
                    seguir = false;             
                else{
                    while(linea.startsWith(Constantes.TAB_CHARACTER+"")){
                        Peli m = getMovie(linea);
                        if (m != null)
                            d.addPeli(m);
                        linea = lnr.readLine();
                    }
                    if (d != null){
                        if (pelisLocal == null)
                            res.add(d);
                        else{
                            //agrego al dir solo si tiene una peli en el set
                            if (esReferenciado(d, pelisLocal)){
                                contRef++;
                                res.add(d);
                            }
                            else
                                contNoRef++;
                            d = null;
                        }
                    }
                    String n = getNombre(linea);
                    if (n != null){
                        d = new Persona();
                        d.setNombre(n);
                        //en la misma línea hay nombre de pel
                        linea = linea.substring(n.length()).trim();
                        Peli m = getMovie(linea);
                        if (m != null)
                            d.addPeli(m);
                    }
                }
            }
            if (pelisLocal != null){
                System.out.println("contNoRef = " + contNoRef);
                System.out.println("contRef = " + contRef);
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

    private static String getLocalId(Peli pd, Set<Peli> pelisLocal) {
        for(Peli p : pelisLocal) {
            if (p.equals(pd))
                return p.getId();
        }
        return null;
    }

    private static boolean esReferenciado(Persona d, Set<Peli> pelisLocal) {
        for(Peli p : pelisLocal) {
            List<Peli> pelis = d.getPelis();
            if (pelis == null){
                System.out.println("Dire. sin pelis: '" + d.getNombre() + "'");
                return false;
            }
            for(Peli pd : pelis) {
                if (p.equals(pd))
                    return true;
            }
        }
        return false;
    }

    public static String getNombre(String linea) {
        try {
            return linea.substring(0, linea.indexOf(Constantes.TAB_CHARACTER));
        } catch (Exception e) {
            return null;
        }
    }

    public static Peli getMovie(String linea) {
        linea = linea.replaceAll(Constantes.TAB_CHARACTER+"", "");
        int d = linea.indexOf("(");
        try {
            String s = linea.substring(d + 1, d + 5);
            if (!ANO_DESCONOCIDO.equals(s))
                new Long(s);
        }
        catch (Exception ex) {
            //nuevo intento
            d = linea.indexOf("(", d+1);
            try {
                new Long(linea.substring(d+1, d+5));
            }
            catch (Exception a) {
                return null;
            }
        }
        String n = linea.substring(0, d).trim();
        String a = linea.substring(d+1, d+5);
        return new Peli(n, a);
    }
}
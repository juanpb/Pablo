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
public class ParserActor {
    private static final Logger logger = Logger.getLogger(ParserActor.class);
    private static final String SEPARADOR_CAMPOS = "|";
    public static final String IGNORAR_HASTA = "----\t\t\t------";
    public static final String IGNORAR_DESDE = "Þrastarson, Ingólfur Karl\tPerlur og svín (1997)  [Starfsmaður í vinnusal Brauðvers]  <151>";
    private static int contNoRef = 0;
    private static int contRef = 0;
    private static String ANO_DESCONOCIDO = "????";
    private static final int CANT = 1000;
    private static String linea;

    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);

        try {
            crearLocalList();
        } catch (Exception e) {
            System.out.println("Última línea leída: '" + linea + "'");
            throw e;
        }
    }
    

    public static void crearLocalList() throws JDOMException, IOException {
        Set<Peli> localMovies = ParserMovies.getLocalMovies();
        Set<Actor> dirs = getReferenciados(localMovies);

        List<Actor> salida = new ArrayList<Actor>();

        for(Actor d : dirs) {
            List<Actuacion> pelisRef = new ArrayList<Actuacion>();
            for(Actuacion p : d.getPelis()) {
                String s = getLocalId(p.getPeli(), localMovies);
                if (s!= null){
                    p.getPeli().setId(s);
                    pelisRef.add(p);
                }
            }
            d.setPelis(pelisRef);
            salida.add(d);
        }
        grabar(salida, System.getProperty("localActorsListPath"));
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

    //formato: act|peliId|peliNombre|papel|puesto (por línea)
    private static void grabar(List<Actor> salida, String path) throws IOException {

        List<String> aGrabar = new ArrayList<String>(salida.size());
        for(Actor d : salida) {
            String n = d.getNombre();
            for(Actuacion a : d.getPelis()) {
                Peli p = a.getPeli();
                String txt = n + SEPARADOR_CAMPOS + p.getId() +
                    SEPARADOR_CAMPOS + p.getNombre()+
                    SEPARADOR_CAMPOS + a.getPapel()+
                    SEPARADOR_CAMPOS + a.getPuesto();
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
    public static Set<Actor> getReferenciados(Set<Peli> pelisLocal){
        String filePath = System.getProperty("actorsListPath");

        LineNumberReader lnr = null;
        Set<Actor> res = new HashSet<Actor>();

        try{
            int cont=0;
            System.out.println("Parseando archivo: " + (filePath));
            lnr = new LineNumberReader(new FileReader(filePath));
            boolean seguir = true;//i==0; //solo el primer archivo tiene cabezera

            //ignoro las primeras líneas
            while(seguir){
            String linea = lnr.readLine();
                if (linea != null && linea.equals(IGNORAR_HASTA))
                    seguir = false;
            }
            seguir = true;
            Actor d=null;
            while(seguir){
                linea = lnr.readLine();
                if (linea != null){
                    if (linea.equals(IGNORAR_DESDE)|| !lnr.ready())
                        seguir = false;
                    else{
                        while(linea!=null && linea.startsWith(Constantes.TAB_CHARACTER+"")){
                            Actuacion m = getActuacion(linea);
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
                                    //ver puesto
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
                            d = new Actor();
                            cont++;
                            if (cont % CANT == 0)
                                System.out.println("Cant act parseada = " + cont);
                            d.setNombre(n);
                            //en la misma línea hay nombre de pel
                            String linea2 = linea.substring(n.length()).trim();
                            Actuacion m = getActuacion(linea2);
                            if (m != null)
                                d.addPeli(m);
                        }
                    }
                }
                else if (!lnr.ready())
                    seguir = false;
            }
            if (pelisLocal != null){
                System.out.println("contNoRef = " + contNoRef);
                System.out.println("contRef = " + contRef);
            }
            lnr.close();
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

    private static boolean esReferenciado(Actor d, Set<Peli> pelisLocal) {
        for(Peli p : pelisLocal) {
            List<Actuacion> pelis = d.getPelis();
            if (pelis == null){
                System.out.println("Act. sin pelis: '" + d.getNombre() + "'");
                return false;
            }
            for(Actuacion pd : pelis) {
                Peli p2 = pd.getPeli();
                if (p.equals(p2))
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

    public static Actuacion getActuacion(String linea) {
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

        int dPap = linea.indexOf("[", d+5);
        int hPap = linea.indexOf("]", dPap+1);
        String papel = null;
        try {
            papel = linea.substring(dPap + 1, hPap);
        }
        catch (Exception ex) {   //puede no tener papel
        }
        int puesto = 0;
        try {
            d = linea.indexOf("<", d + 6);
            int h = linea.indexOf(">", d + 1);
            puesto = Integer.parseInt(linea.substring(d+1, h));
            
        } catch (Exception ignored) {
        }

        return new Actuacion(new Peli(n, a), puesto, papel);
    }
}
package p.aplic.peliculas.util;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: Administrador
 * Date: 29/03/2007
 * Time: 18:56:15
 */
public class Exportar {
    private static final Logger logger = Logger.getLogger(Exportar.class);

    public static void main(String[] args) throws IOException, JDOMException {
//        String configPath = "F:\\Java\\ProyectosJava\\Pablo\\src\\p\\aplic\\peliculas\\config.ini";
//        p.util.Util.loadProperties(configPath);
//        String xmlPath = System.getProperty("xmlPath");
        String xmlPath = "D:\\P\\_env\\películas.xml";
        Peliculas peliculas = Util.getPeliculas(xmlPath);
        List<String> noms = getNombrePeliculas("DVD", peliculas.getPeliculas());
        System.out.println("DVD");
        for (String nom : noms) {
            System.out.println(nom);
        }
        System.out.println("");
        noms = getNombrePeliculas("DIVX", peliculas.getPeliculas());
        System.out.println("DIVX");
        for (String nom : noms) {
            System.out.println(nom);
        }
    }


    /**
     *
     * Devuelve todos los nombres de películas cuyo id contenga el recibido
     * como parámetro.
     */
    public static List<String> getNombrePeliculas(String id,
                                                  List<Pelicula> pelis){
        List<String> res = new ArrayList<String>();
        for (Pelicula peli : pelis) {

            String id1 = peli.getId();
            if (id1 == null){
//                System.out.println("Id null, " + peli.getNombre());
            }
            else if (id1.contains(id))
                res.add(peli.getNombre() + " (" + peli.getAnnus() +")");
        }

        return res;
    }

    public static StringBuffer toString(List<Pelicula> ps, ExportarConfig conf){
        StringBuffer res = new StringBuffer();

        for (Pelicula p : ps) {
            if (conf.isMostrarNombre())
                res.append(p.getNombre());
            if (conf.isMostrarAnnus())
                res.append(" (" + p.getAnnus() + ")");
            if (conf.isMostrarId() && p.getId() != null)
                res.append(" [" + p.getId() + "]");
            String vsc = "";
            if (conf.isMostrarComentarios() && p.getComentario() != null)
                vsc += ", " + p.getComentario();
            if (conf.isMostrarCritica() && p.getCritica() != null)
                vsc += ", " + p.getCritica();
            if (conf.isMostrarDatosTecnicos() && p.getDatosTecnicos() != null)
                vsc += ", " + p.getDatosTecnicos();
            if (conf.isMostrarDuracion() && p.getDuracion() != null)
                vsc += ", " + p.getDuracion();
            if (conf.isMostrarIMDB() && p.getImdbPuntaje() != null)
                vsc += ", " + p.getImdbPuntaje();
            if (conf.isMostrarPais() && p.getPais() != null)
                vsc += ", " + p.getPais();
            if (conf.isMostrarPosters() && p.getPosters() != null)
                vsc += ", " + p.getPosters();
            if (conf.isMostrarProblemas() && p.getProblemas() != null)
                vsc += ", " + p.getProblemas();

//            if (!"".equals(vsc))
//            res.append(" " + vsc.substring(2));//saco la primera coma
            res.append(vsc);
            res.append("\n");
        }

        return res;
    }
    
    public static List<Pelicula> getPeliculas(Pelicula.Estado... est){
        return getPeliculas(null, est);
    }
    
    public static List<Pelicula> getPeliculas(Object[] ordX, Pelicula.Estado... est){
        String[] ord = toString (ordX);
        return getPeliculas(ord, est);
    }

    private static String[] toString(Object[] ordX){
        String[] res = new String[ordX.length];
        int i = 0;
        for(Object o : ordX) {
            res[i++] = o.toString();
        }
        return res;
    }

    public static List<Pelicula> getPeliculas(String[] ordX, Pelicula.Estado... est){
        List<Pelicula> res = new ArrayList<Pelicula>();
        String xmlPath = System.getProperty("xmlPath");
        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath);

            for (Pelicula p : pelis.getPeliculas()) {
                Pelicula.Estado e = p.getEstado();
                if (está(e, est)){
                    res.add(p);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (ordX == null)
            return ordenarPorId(res);
        else
            return Util.ordenar(res, ordX);
    }

    private static List<Pelicula> ordenarPorId(List<Pelicula> aExportar) {
        Collections.sort(aExportar, new ComparatorPorId());
        return aExportar;
    }

    private static boolean está(Pelicula.Estado e, Pelicula.Estado... est){
        for (Pelicula.Estado estado : est) {
            if (estado.equals(e))
                return true;
        }
        return false;
    }
}

class ComparatorPorId implements Comparator<Pelicula> {

    public int compare(Pelicula p1, Pelicula p2) {
        String id1 = p1.getId();
        String id2 = p2.getId();
        if (id1 == null)
            return -1;
        else if (id2 == null)
            return 1;

        //primero DIVX y luego DVD
        if (!esDivx(id1) && !esDVD(id1))
            return -1;
        else if (!esDivx(id2) && !esDVD(id2))
            return 1;
        else if (esDivx(id1) && esDVD(id2))
            return -1;
        else if (esDivx(id2) && esDVD(id1))
            return 1;
        return compararNumero(id1, id2);

    }

    private int compararNumero(String id1, String id2) {
        id1 = id1.substring(4).trim();
        id2 = id2.substring(4).trim();
        if (id1.indexOf(" - ") > 0)
            id1 = id1.substring(0, id1.indexOf(" - "));
        if (id2.indexOf(" - ") > 0)
            id2 = id2.substring(0, id2.indexOf(" - "));
        Integer n1 = new Integer(id1);
        Integer n2 = new Integer(id2);
        return n1.compareTo(n2);
    }

    private boolean esDivx(String id) {
        return id.startsWith("DIVX");
    }
    private boolean esDVD(String id) {
        return id.startsWith("DVD");
    }
}
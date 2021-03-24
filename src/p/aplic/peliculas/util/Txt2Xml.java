package p.aplic.peliculas.util;

import org.apache.log4j.Logger;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;
import p.util.Constantes;
import p.util.UtilFile;
import p.util.xml.XMLUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: Administrador
 * Date: 11/02/2007
 * Time: 13:28:48
 */
public class Txt2Xml {
    private static final Logger logger = Logger.getLogger(Txt2Xml.class);

    private static String dir =
            "F:\\_videos_\\_info_\\tmp";
//            "F:\\Java\\ProyectosJava\\Pablo\\src\\p\\aplic\\peliculas";
//            "C:\\Documents and Settings\\Administrador\\Escritorio\\Películas";
    private static final String archSalida = dir + "\\vistos.xml";
    private static final String archEntrada = dir + "\\vistos.txt";
    private static final Pelicula.Estado ESTADO = Pelicula.Estado.SIN_VER;


    public static void main(String[] args) throws IOException {
        new Txt2Xml().hacer();
    }
    private void hacer() throws IOException {
        List<String> apl = UtilFile.getArchivoPorLinea(new File(archEntrada));

        //Separo cada película(un conjunto de líneas) en peliEnLineas y la guardo
        //en listaPelis
        List<List<String>> listaPelis = new ArrayList<List<String>>();
        List<String> peliEnLineas = new ArrayList<String>();
        listaPelis.add(peliEnLineas);
        boolean buscandoNuevaPeli = false;
        for (Object anApl : apl) {
            String linea = (String) anApl;
            int idt = linea.indexOf(Constantes.TAB_CHARACTER);
            String col1;
            if (idt > -1)
                col1 = linea.substring(0, idt).trim();
            else
                col1 = linea;
            if (buscandoNuevaPeli) {

                if (!col1.equals("")) {
                    peliEnLineas = new ArrayList<String>();
                    listaPelis.add(peliEnLineas);
                    peliEnLineas.add(linea);
                    buscandoNuevaPeli = false;
                }
            } else {
                peliEnLineas.add(linea);
                if (col1.equals("")) {
                    buscandoNuevaPeli = true;
                }
            }
        }

        //Armo un objeto Pelicula por cada peliEnLineas
        List<Pelicula> peliculas = new ArrayList<Pelicula>();
        for (List<String> listaPeli : listaPelis) {
            peliEnLineas = listaPeli;
            Pelicula pel = getPelicula(peliEnLineas);
            peliculas.add(pel);
        }
        asignarTiempo(peliculas);
        guardar(peliculas);
    }

    private void asignarTiempo(List<Pelicula> peliculas) {
        Peliculas pelis;
        try {
            String xmlPath = "F:\\_videos_\\_info_\\películas.xml";
            List<Tag> tags = null; //Util.getTags(System.getProperty("tags"));

            pelis = Util.getPeliculas(xmlPath, tags);

            for (Pelicula pelicula : peliculas) {
                buscarYModificarTiempo(pelicula, pelis);
            }

            Util.grabar(xmlPath, pelis);
        } catch (Exception e) {
            Log.error(e);
        }

    }

    private void buscarYModificarTiempo(Pelicula pelicula, Peliculas pelis) {
        for (Pelicula pBuena : pelis.getPeliculas()) {
            if (pBuena.getNombre().equals(pelicula.getNombre())){
                modificarTiempo(pelicula, pBuena);
            }
        }
    }

    private void modificarTiempo(Pelicula pelicula, Pelicula pBuena) {
        if (pBuena.getDuracion() == null || pBuena.getDuracion().equals("")){
            String duracion = pelicula.getDuracion();
            System.out.println("pelicula.getNombre() = " + pelicula.getNombre());
            System.out.println("pBuena.getNombre() = " + pBuena.getNombre());
            System.out.println("duracion = " + duracion);
            pBuena.setDuracion(duracion);
            System.out.println("");
        }
    }

    private void guardar(List<Pelicula> peliculas) throws IOException {

        File f = new File(archSalida);
        if (f.exists())
            f.delete();
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        String x =  "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + Constantes.NUEVA_LINEA;
        bos.write(x.getBytes());
        x =  "<R>" + Constantes.NUEVA_LINEA;
        bos.write(x.getBytes());
        for (Pelicula p : peliculas) {
            String tag = pel2Tag(p);
            bos.write(tag.getBytes());
        }
        x = "</R>" + Constantes.NUEVA_LINEA;
        bos.write(x.getBytes());
        bos.close();
    }

    private String pel2Tag(Pelicula p) {
        String raiz = "F:\\_videos_\\_info_\\fotos";
        String s1 = p.getNombre();
        s1 = s1.replaceAll(":", ",");
        s1 = s1.replaceAll("/", "-");
        File f = new File(raiz, s1 + ".jpg");
        List<String> posters = new ArrayList<String>(2);
        if (f.exists())
            posters.add(f.getName());
        else{
            System.out.println("Sin poster para " + f.getName());
        }
        f = new File(raiz, p.getNombre() + "_2.jpg");
        if (f.exists())
            posters.add(f.getName());

        f = new File(raiz, p.getNombre() + "_3.jpg");
        if (f.exists())
            posters.add(f.getName());

        f = new File(raiz, p.getNombre() + "_4.jpg");
        if (f.exists())
            posters.add(f.getName());

        p.setPosters(posters);


        String res = Constantes.TAB + "<Pelicula>" + Constantes.NUEVA_LINEA;
        String x = XMLUtil.escapeString(p.getNombre()).toString();
        res += Constantes.TAB + Constantes.TAB + "<Nombre>" + x + "</Nombre>" + Constantes.NUEVA_LINEA;

		x = p.getAka();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Aka>" + x + "</Aka>" + Constantes.NUEVA_LINEA;
        }

		x = p.getImdbId();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<ImdbId>" + x + "</ImdbId>" + Constantes.NUEVA_LINEA;
        }

		x = p.getImdbPuntaje();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Imdb-puntaje>" + x + "</Imdb-puntaje>" + Constantes.NUEVA_LINEA;
        }
        x = p.getCritica();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Critica>" + x +
                    "</Critica>" + Constantes.NUEVA_LINEA;
        }

        x =  p.getComentario();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Comentario>" + x +
                    "</Comentario>" + Constantes.NUEVA_LINEA;
        }

        x =  p.getProblemas();
        if (x != null){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Problemas>" + x +
                    "</Problemas>" + Constantes.NUEVA_LINEA;
        }

        x = p.getAnnus();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Annus>" + x +
                    "</Annus>" + Constantes.NUEVA_LINEA;
        }

        x = p.getPais();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Pais>" + x +
                    "</Pais>" + Constantes.NUEVA_LINEA;
        }

        x = p.getId();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Id>" + x + "</Id>" +
                    Constantes.NUEVA_LINEA;
        }

        x = p.getDuracion();
        if (x != null && !x.trim().equals("")){
            x = XMLUtil.escapeString(x).toString();
            res += Constantes.TAB + Constantes.TAB + "<Duracion>" + x + "</Duracion>" +
                    Constantes.NUEVA_LINEA;
        }

        x = XMLUtil.escapeString(p.getEstado().toString()).toString();
        res += Constantes.TAB + Constantes.TAB + "<Estado>" + x +
                "</Estado>" + Constantes.NUEVA_LINEA;


        for (String poster : posters) {
            String s = poster;
            s = XMLUtil.escapeString(s).toString();
            res += Constantes.TAB + Constantes.TAB + "<Poster>" + s +
                    "</Poster>" + Constantes.NUEVA_LINEA;
        }

        res += Constantes.TAB + "</Pelicula>" + Constantes.NUEVA_LINEA;

        return res;
    }

    private Pelicula getPelicula(List peliEnLineas) {
        Pelicula res = new Pelicula();
        String s = (String)peliEnLineas.get(0);
        try{
            //hasta el primer tab tengo el nombre/año/país/duración.
            //Ej: Million Dollar Baby (2004) - 0,57 + 1,15
            // y en los siguientes tab la crítica
            StringTokenizer st = new StringTokenizer(s, Constantes.TAB_CHARACTER+"");

            String critica = null;
            String imdb = null;
            String id = null;
            String comentario = null;

            agregarNombrePaisAnnusDuracion(res, st.nextToken().trim());
            critica = buscarYAgregarCritica(st, critica);

            if (peliEnLineas.size() > 1){
                s = (String)peliEnLineas.get(1);
                st = new StringTokenizer(s, Constantes.TAB_CHARACTER+"");
                if (st.hasMoreTokens())
                    imdb = st.nextToken().trim();
                critica = buscarYAgregarCritica(st, critica);
            }

            if (peliEnLineas.size() > 2){
                s = (String)peliEnLineas.get(2);
                st = new StringTokenizer(s, Constantes.TAB_CHARACTER+"", true);
                if (st.hasMoreTokens())
                    id = st.nextToken().trim();
                critica = buscarYAgregarCritica(st, critica);
            }

            if (peliEnLineas.size() > 3){
                s = (String)peliEnLineas.get(3);
                st = new StringTokenizer(s, Constantes.TAB_CHARACTER+"", true);

                if (st.hasMoreTokens())
                    comentario = st.nextToken().trim();

                critica = buscarYAgregarCritica(st, critica);
            }

            for (int i = 4; i < peliEnLineas.size(); i++) {
                s = (String) peliEnLineas.get(i);

                st = new StringTokenizer(s, Constantes.TAB_CHARACTER+"", true);
                if (st.hasMoreTokens()){
                    s = st.nextToken().trim();
                    if (!s.equals("")){
                        if (comentario == null)
                            comentario = s;
                        else
                            comentario += Constantes.NUEVA_LINEA + s;

                    }
                }
                critica = buscarYAgregarCritica(st, critica);
            }

            res.setCritica(critica);
            res.setId(id);
            res.setImdbPuntaje(imdb);
            res.setComentario(comentario);
            res.setEstado(ESTADO);
//            buscarYAgregarPelicula(res);
        }catch(RuntimeException e){
            System.out.println("Problemas en " + s);
        }
        return res;
    }

    /*private void buscarYAgregarPelicula(Pelicula res) {
        String fotosPath = "F:\\_videos_\\_info_\\tmp\\Libro1_archivos\\";
        List<String> posters = res.getPosters();
        String nombre = res.getNombre() + ".jpg";
        nombre = AsignarFotosProc.parchear(nombre);
        File f = new File(fotosPath, nombre);
        if (!f.exists()){
            boolean preguntar = true;
            while (preguntar){
                String msg = "'Cancelar' -> ignora " + Constantes.NUEVA_LINEA +
                        "vacío + 'Aceptar' para reintentar" +
                          Constantes.NUEVA_LINEA +
                        "o nueva dir + 'Aceptar'";
                nombre = JOptionPane.showInputDialog(msg, nombre);
                if (nombre == null) //se apretó cancelar
                    preguntar = false;
                else if (!nombre.trim().equals("")){
                    //si se ingresó algo pruebo con la nueva dir
                    f = new File (fotosPath, nombre);
                }
                //else si se dejó en blanco vuelvo a probar con la dir
                //original
                preguntar = preguntar && !f.exists();
            }
        }

        if (nombre != null){
            posters.add(nombre);
            nombre = nombre.replaceAll(".jpg", "_2.jpg");
            f = new File(fotosPath, nombre);
            System.out.println("f.getAbsolutePath() = " + f.getAbsolutePath());
            if (f.exists()){
                posters.add(nombre);

//                f = new File(fotosPath, nombre + "_3.jpg");
//                if (f.exists()){
//                    posters.add(nombre + "_3.jpg");
//                }
            }
        }
    }
     */

    //Ej: s = Million Dollar Baby (2004) - 0,57 + 1,15
    private void agregarNombrePaisAnnusDuracion(Pelicula pel, String s) {
        String x = s;
        try{
            int i = s.lastIndexOf(" - ");
            if (i > -1){
                String duracion = s.substring(i + 3);
                pel.setDuracion(duracion);
                s = s.substring(0, i);
            }
            else{
                System.out.println("Sin duración: " + s);
            }

            i = s.lastIndexOf("[");
            if (i > -1){
                String pais = s.substring(i + 1, s.lastIndexOf("]"));
                pel.setPais(pais);
                s = s.substring(0, i);
            }

            i = s.lastIndexOf("(");
            if (i > -1){
                String an = s.substring(i + 1, s.lastIndexOf(")"));
                pel.setAnnus(an);
                s = s.substring(0, i).trim();
            }
            pel.setNombre(s);
        }catch(RuntimeException e){
            System.out.println("Problemas con la línea: " + x);
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private String buscarYAgregarCritica(StringTokenizer st,
                                         String critica){
        boolean seguir = true;
        while (st.hasMoreTokens() && seguir){
            String s = st.nextToken().trim();
            if (!s.equals(""))  {
                if (critica == null)
                    critica = s;
                else
                    critica += Constantes.NUEVA_LINEA + s;
                seguir = false;
            }
        }
        return critica;
    }
//    private String buscarYAgregarCritica(String linea,
//                                         String critica){
//        StringTokenizer st = new StringTokenizer(linea, Constantes.TAB_CHARACTER+"");
//        return buscarYAgregarCritica(st, critica);
//    }
}

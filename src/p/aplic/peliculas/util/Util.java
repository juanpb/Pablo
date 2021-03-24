package p.aplic.peliculas.util;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.PeliculasFrame;
import p.aplic.peliculas.Tag;
import p.util.SVN;
import p.util.UtilFile;
import p.util.xml.XMLWriter;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * User: Administrador
 * Date: 03/02/2007
 * Time: 13:01:50
 */
public class Util {
    private static final Logger logger = Logger.getLogger(Util.class);

    public static Locale locale = new Locale("ES", "AR");

    public static void main(String[] args) throws IOException, JDOMException {
        long ini = System.currentTimeMillis();

        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];

        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");
        List<Tag> tags = Util.getTags(System.getProperty("tags"));
        Peliculas peliculas = Util.getPeliculas(xmlPath, tags);
        List<Pelicula> peliculasOrd = peliculas.getPeliculas();
        generarHTML(peliculas);

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Listo. Demoró " + seg + " segundos");        
    }

    static public void trimTodo(String xmlPath, List<Tag> tags) throws IOException, JDOMException {
        Peliculas peliculas = getPeliculas(xmlPath, tags);
        for(Pelicula pel : peliculas.getPeliculas()) {
            pel.setAka(trim(pel.getAka()));
            pel.setAnnus(trim(pel.getAnnus()));
            pel.setComentario(trim(pel.getComentario()));
            pel.setProblemas(trim(pel.getProblemas()));
            pel.setCritica(trim(pel.getCritica()));
            pel.setDatosTecnicos(trim(pel.getDatosTecnicos()));
            pel.setDuracion(trim(pel.getDuracion()));
            pel.setId(trim(pel.getId()));
            pel.setImdbId(trim(pel.getImdbId()));
            pel.setImdbPuntaje(trim(pel.getImdbPuntaje()));
            pel.setNombre(trim(pel.getNombre()));
            pel.setPais(trim(pel.getPais()));
        }
        grabar(xmlPath, peliculas);
    }

    static private  String trim(String x) {
        if (x != null)
            x = x.trim();
        return x ;
    }

    /*
    ordena por año y nombre,
     */
    //static public List<Pelicula> ordenarPorAño(List<Pelicula> lista, boolean primeroMasNuevo) {
     //   Object[] ordenarPor = {PeliculasFrame.IT_OR_AÑO, PeliculasFrame.IT_OR_NOMBRE};
      //  Collections.sort(lista, new Compa(ordenarPor));
       // return lista;
    //}

    static public List<Pelicula> ordenar(List<Pelicula> lista, Object[] ordenarPor) {
        Collections.sort(lista, new Compa(ordenarPor));
        return lista;
    }

    static public List<Pelicula> ordenar(List<Pelicula> lista, String[] ordenarPor) {
        Collections.sort(lista, new Compa(ordenarPor));
        return lista;
    }

    public static Peliculas getPeliculas(String xmlPath, List<Tag> tags)
            throws IOException, JDOMException {
        return leerXML(new File(xmlPath), tags);
    }

    /*
    static public void asignarUniqueId(String xmlPath) throws IOException, JDOMException {
        Peliculas peliculas = getPeliculas(xmlPath, tags);
        for (int i = 0; i < peliculas.getPeliculas().size(); i++) {
            Pelicula p = peliculas.getPeliculas().get(i);
            p.setUniqueId(i+1);
        }
        grabar(xmlPath, peliculas);
    }
    */
    /*
    static public void asignarContadorSinVer(String xmlPath) throws IOException, JDOMException {
        Peliculas peliculas = getPeliculas(xmlPath, tags);
        List<Pelicula> peliculasSV = peliculas.getPeliculas(Pelicula.Estado.SIN_VER);
        for (int i = 0; i < peliculasSV.size(); i++) {
            Pelicula p = peliculasSV.get(i);
            p.setContador(i); 
        }
        grabar(xmlPath, peliculas);
    } */

    /*
    static public void asignarContadorVistos(String xmlPath) throws IOException, JDOMException {
        Peliculas peliculas = getPeliculas(xmlPath, tags);
        int cont = 0;
        for (int i = 0; i < peliculas.getPeliculas().size(); i++) {
            Pelicula p = peliculas.getPeliculas().get(i);
            if (p.getEstado().equals(Pelicula.Estado.VISTO)){
                cont++;
                p.setContador(cont);
            }
            else
                p.setContador(0);
        }
        grabar(xmlPath, peliculas);
    }  */

    static public void printNombres(String xmlPath, List<Tag> tags) throws IOException, JDOMException {
        Peliculas peliculas = getPeliculas(xmlPath, tags);
        for (int i = 0; i < peliculas.getPeliculas().size(); i++) {
            Pelicula p = peliculas.getPeliculas().get(i);
            System.out.println(p.getNombre());
        }
    }

    /*
    static public void cargarTags(Peliculas peliculas, String tagFile) throws IOException, JDOMException {
        List<String> apl = UtilFile.getArchivoPorLinea(tagFile);
        List<Tag> tags = new ArrayList<Tag>();
        peliculas.setTags(tags);
        for (String s : apl) {
            StringTokenizer st = new StringTokenizer(s, "=");
            Tag tag = new Tag(Integer.parseInt(st.nextElement().toString()), st.nextToken());
            tags.add(tag);
        }
    } */

    static public List<Tag> getTags(String tagFile) throws IOException {
        List<String> apl = UtilFile.getArchivoPorLinea(tagFile);
        List<Tag> tags = new ArrayList<Tag>();
        for (String s : apl) {
            StringTokenizer st = new StringTokenizer(s, "=");
            Tag tag = new Tag(st.nextElement().toString(), st.nextToken());
            tags.add(tag);
        }
        return tags;
    }

    static private Peliculas leerXML(File f, List<Tag> tags) throws IOException, JDOMException {
        if (f.exists()){
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(f);
            return parsear(doc, tags);
        }
        else{
            return new Peliculas();
        }
    }

    static private Peliculas parsear(Document doc, List<Tag> tags) {
        Peliculas res = new Peliculas();
        Element r = doc.getRootElement();

        Element contDVDTag = r.getChild("ContadorDVD");
        Element contUniqueID = r.getChild("UniqueID");
        Element contVistos = r.getChild("ContadorVistos");
        Element contSinVer = r.getChild("ContadorSinVer");
        res.setContadorDVD(new Integer(contDVDTag.getText()));
        res.setContadorUniqueID(new Integer(contUniqueID.getText()));
        res.setContadorVistos(new Integer(contVistos.getText()));
        res.setContadorSinVer(new Integer(contSinVer.getText()));

        Element ts = r.getChild("Timestamp");
        if (ts != null)
            res.setTimestamp(Long.parseLong(ts.getText()));
//        Element contIMDBId = r.getChild("ContadorIMDBId");
//        try {
//            res.setContadorIMDBId(new Integer(contIMDBId.getText()));
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
        List pls = r.getChildren("Pelicula");

        for (Object pl : pls) {
            Element pX = (Element) pl;
            Pelicula p = crearPelicula(pX, tags);
            res.addPelicula(p);
        }

        return res;
    }

    /**
     * Parsea un documento y devuelve la primera (y debería ser la única) película.
     */
    static public Pelicula parsearPelicula(String docum, List<Tag> tags) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        if (docum != null)
            docum = docum.trim();
        Document doc = builder.build(new InputSource(new StringReader(docum)));
        Element r = doc.getRootElement();
        return crearPelicula(r.getChild("Pelicula"), tags);
    }

    private static Pelicula crearPelicula(Element pelX, List<Tag> tags) {
        Pelicula res = new Pelicula();
        String value = pelX.getAttributeValue("uniqueId");
        if (value != null && !value.equals("null") )
            res.setUniqueId(Integer.valueOf(value));
        value = pelX.getAttributeValue("contador");
        if (value != null && !value.equals("null") )
            res.setContador(Integer.valueOf(value));
        
        Element el = pelX.getChild("Nombre");
        if (el != null){
            res.setNombre(el.getText());
        }
        else{
            String s = "Mal el xml. A una película le falta el nombre.";
            throw new RuntimeException(s);
        }

        el = pelX.getChild("Aka");
        if (el != null){
            res.setAka(el.getText());
        }

        el = pelX.getChild("ImdbId");
        if (el != null){
            res.setImdbId(el.getText());
        }

        el = pelX.getChild("Annus");
        if (el != null){
            res.setAnnus(el.getText());
        }

        el = pelX.getChild("Pais");
        if (el != null){
            res.setPais(el.getText());
        }
        el = pelX.getChild("Imdb-puntaje");
        if (el != null){
            res.setImdbPuntaje(el.getText());
        }
        el = pelX.getChild("Estado");
        if (el != null){
            res.setEstado(Pelicula.Estado.parse(el.getText()));
        }
        else{
            String s = "Mal el xml. A una película (" +
                    res.getNombre() + ") le falta el estado.";
            throw new RuntimeException(s);
        }

        el = pelX.getChild("Critica");
        if (el != null){
            res.setCritica(el.getText());
        }

        el = pelX.getChild("Id");
        if (el != null){
            res.setId(el.getText());
        }

        el = pelX.getChild("DatosTecnicos");
        if (el != null){
            res.setDatosTecnicos(el.getText());
        }

        el = pelX.getChild("Duracion");
        if (el != null){
            res.setDuracion(el.getText());
        }

        el = pelX.getChild("Comentario");
        if (el != null){
            res.setComentario(el.getText());
        }
        el = pelX.getChild("Problemas");
        if (el != null){
            res.setProblemas(el.getText());
        }

        List postersX = pelX.getChildren("Poster");
        if (postersX != null){
            for (Object aPostersX : postersX) {
                Element posX = (Element) aPostersX;
                res.addPoster(posX.getText());
            }
        }

        List tagsId = pelX.getChildren("Tag");
        if (tagsId != null){
            for (Object elem : tagsId) {
                Element tagX = (Element) elem;
                res.addTag(Tag.crearTag(tagX.getText(), tags));
            }
        }

        return res;
    }

    private static boolean verificarTimestamp(String xmlPath, Peliculas pelis){
        long tsArchivo = getTimestamp(xmlPath);
        if (tsArchivo == 0) //debería pasar solo la primera vez, cuando no hay nada definido en el xml
            return true;
        else{
            return tsArchivo == pelis.getTimestamp();
        }
    }

    private static long getTimestamp(String xmlPath) {
        try {
            File f = new File(xmlPath);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(f);
            Element r = doc.getRootElement();
            Element ele = r.getChild("Timestamp");
            if (ele == null)
                return 0;
            return new Long(ele.getText());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void grabar(final String xmlPath, final Peliculas pelis) {
        grabar(xmlPath, pelis, false);
    }

    // graba y hace commit del xml
    public static void grabar(final String xmlPath, final Peliculas pelis,
                              final boolean hacerCommit) {

        boolean b = verificarTimestamp(xmlPath, pelis);
        if (!b)
            throw new RuntimeException("No se graban los cambios por problemas en el timestamp");
        pelis.setTimestamp(new Date().getTime());

        try{
            pelis.ordenarPorUniqueID();
            Document docSal = peliculas2XML(pelis);
            grabar(docSal, xmlPath);

            //en paralelo, hago commit
            new Thread(new Runnable(){
                public void run() {
                    if (hacerCommit){
                        try {
                            SVN.commit(xmlPath);
                        } catch (Exception e) {
                            Log.error(e);
                        }
                    }
                }
            }).start();

            //en paralelo, genero los html
            new Thread(new Runnable(){
                public void run() {
                    try {
                        generarHTML(pelis, true);
                    } catch (IOException e) {
                        Log.error(e);
                    } catch (JDOMException e) {
                        Log.error(e);
                    }
                }
            }).start();

        }catch(Exception e){
            Log.error(e);
        }
    }

    private static void generarHTML(Peliculas pls, boolean soloSinVer) throws JDOMException, IOException {
        List<Pelicula.Estado> estados = Pelicula.getEstados();
        String htmlRoot = System.getProperty("htmlRoot");
        if (htmlRoot == null){
            System.out.println("No se definió el htmlRoot");
            return ;
        }
        else if (!htmlRoot.endsWith("\\"))
            htmlRoot += "\\";


        String nombre = System.getProperty("nombre");
        int cantMaxPelisPorHTML = Integer.MAX_VALUE;
        try{
            cantMaxPelisPorHTML = Integer.parseInt(System.getProperty("cantMaxPelisPorHTML"));
        }catch(Exception e){}

        if (nombre == null)
            nombre = "";
        else
            nombre += "_";

        //Si no existe htmlRoot no genero los html de salida

        File f = new File(htmlRoot);
        if (f.exists()){
            for (Pelicula.Estado estado : estados) {
                if (soloSinVer && estado.equals(Pelicula.Estado.SIN_VER)){
                    List<Pelicula> peliculas = pls.getPeliculas(estado);
                    ordenarPorContador(peliculas);
                    if (estado.equals(Pelicula.Estado.SIN_VER)){
                        //no incluyo las que están solo en divx
                        peliculas = excluirSoloDIVX(peliculas);
                    }

                    if (peliculas.size() == 0){
                        //genero html vacío
                        String archSalida = htmlRoot + nombre + estado + ".html";
                        HTML.generarHTML(peliculas, archSalida);
                    }
                    else{
                        //por cada estado, genero html que no superen la cantidad de pelis especificada
                        for (int i=0; i * cantMaxPelisPorHTML < peliculas.size(); i++){
                            String cont = "";
                            if (i > 0)
                                cont = "_" + i;
                            String archSalida = htmlRoot + nombre + estado + cont + ".html";
                            List<Pelicula> peliculasParte;
                            if (cantMaxPelisPorHTML >= peliculas.size())
                                peliculasParte = peliculas;
                            else{
                                peliculasParte = new ArrayList<Pelicula>(cantMaxPelisPorHTML);
                                for(int j = i * cantMaxPelisPorHTML;
                                    j <i * cantMaxPelisPorHTML + cantMaxPelisPorHTML && peliculas.size() > j
                                        ; j++) {
                                    peliculasParte.add(peliculas.get(j));
                                }
                            }
                            HTML.generarHTML(peliculasParte, archSalida);
                        }
                    }
                }
            }
            if (!soloSinVer){
                //genera HD
                String archSalidaHD = htmlRoot + nombre + "HD.html";//todo
                List<Pelicula> peliculasHD = new ArrayList<Pelicula>();

                //genera HD
                archSalidaHD = htmlRoot + nombre + "HD_ordenado.html";//todo
                List<Pelicula> pelis = pls.getPeliculas();
                List<Pelicula> pelisOrd = new ArrayList();
                pelisOrd.addAll(pelis);
                peliculasHD.clear();
                ordenarAñoNombre(pelisOrd);
                for (Pelicula p : pelisOrd) {
                    if (p.isHD())
                        peliculasHD.add(p);
                }
                HTML.generarHTML(peliculasHD, archSalidaHD);

                //genera varios html con CANT pelis HD desordenadas
                int CANT = 200;
                int cantPelisTotal = peliculasHD.size();
                boolean seguir = true;
                int cont = 0;
                Collections.shuffle(peliculasHD);
                while( seguir)        {
                    archSalidaHD = htmlRoot + nombre + "HD_mezcla_" + ++cont + ".html";//todo

                    int hasta = CANT * cont;

                    if (hasta >= cantPelisTotal){
                        seguir = false;
                        hasta = cantPelisTotal -1;
                    }
                    List<Pelicula> pelisMezcla = peliculasHD.subList(CANT * (cont-1), hasta);

                    HTML.generarHTML(pelisMezcla, archSalidaHD);
                }
            }
        }else{
            System.out.println("No existe el directorio de salida para los html: '" + htmlRoot +"'");
        }

        //creo un html con los estados VISTO y SIN_VER (que estén en DVD),
        //ordenado por año (> a <) y nombre
//        List<Pelicula> peliculas = pls.getPeliculas(Pelicula.Estado.SIN_VER);
//        List<Pelicula> peliculasV = pls.getPeliculas(Pelicula.Estado.VISTO);
//        peliculas.addAll(peliculasV);
//        peliculas = excluirSoloDIVX(peliculasV);
//        ordenarAñoNombre(peliculas);
//        String archSalida = htmlRoot + "//dvd.html";
//        HTML.generarHTML(peliculas, archSalida);
    }

    public static String pelicula2XML(Pelicula p){
        Element raiz = new Element("Raíz");
        Document res = new Document(raiz);
        Element pelTag = pel2Element(p);
        raiz.addContent(pelTag);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            XMLWriter.docAOutputStream(res, os);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return os.toString();
    }

    private static Document peliculas2XML(Peliculas peliculas){
        Element raiz = new Element("Raíz");
        Document res = new Document(raiz);

        List<Pelicula> pelis = peliculas.getPeliculas();

        //Contadores
        raiz.addContent(getContadorDVDElement(peliculas));
        raiz.addContent(getUniqueIDElement(peliculas));
        raiz.addContent(getContadorVistosElement(peliculas));
        raiz.addContent(getContadorSinVerElement(peliculas));
        Element el = getContadorIMDBIdElement(peliculas);
        if (el != null)
            raiz.addContent(el);

        raiz.addContent(getTimestampElement(peliculas));

        List<Element> elements = new ArrayList<Element>();
        for (Pelicula pel : pelis) {
            Element pelTag = pel2Element(pel);
            elements.add(pelTag);
        }
        raiz.addContent(elements);
        return res;
    }

    private static Element getTimestampElement(Peliculas peliculas) {
        Element el = new Element("Timestamp");
        try {
            el.setText(peliculas.getTimestamp()+"");
        } catch (Exception e) {
            return null;
        }
        return el;
    }

    private static Element getContadorIMDBIdElement(Peliculas peliculas) {
        Element el = new Element("ContadorIMDBId");
        try {
            el.setText(peliculas.getContadorIMDBId().toString());
        } catch (Exception e) {
            return null;
        }
        return el;
    }

    private static Element getContadorVistosElement(Peliculas peliculas) {
        Element el = new Element("ContadorVistos");
        el.setText(peliculas.getContadorVistos().toString());
        return el;
    }

    private static Element getContadorSinVerElement(Peliculas peliculas) {
        Element el = new Element("ContadorSinVer");
        el.setText(peliculas.getContadorSinVer().toString());
        return el;
    }

    private static Element getUniqueIDElement(Peliculas peliculas) {
        Element el = new Element("UniqueID");
        el.setText(peliculas.getContadorUniqueID().toString());
        return el;
    }

    private static Element getContadorDVDElement(Peliculas peliculas) {
        Element el = new Element("ContadorDVD");
        el.setText(peliculas.getContadorDVD().toString());
        return el;
    }

    private static Element pel2Element(Pelicula p) {
        Element pel = new Element("Pelicula");
        pel.setAttribute("uniqueId", p.getUniqueId()+"");
        pel.setAttribute("contador", p.getContador()+"");
        String str = p.getNombre();
        Element el = new Element("Nombre");
        el.setText(str);
        pel.addContent(el);


        str = p.getAka();
        if (str != null && str.length() >0){
            el = new Element("Aka");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getImdbId();
        if (str != null && str.length() >0){
            el = new Element("ImdbId");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getId();
        if (str != null && str.length() >0){
            el = new Element("Id");
            el.setText(str);
            pel.addContent(el);
        }
        
        str = p.getAnnus();
        if (str != null && str.length() >0){
            el = new Element("Annus");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getPais();
        if (str != null && str.length() >0){
            el = new Element("Pais");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getImdbPuntaje();
        if (str != null && str.length() >0){
            el = new Element("Imdb-puntaje");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getEstado().toString();
        if (str != null && str.length() >0){
            el = new Element("Estado");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getCritica();
        if (str != null && str.length() >0){
            el = new Element("Critica");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getComentario();
        if (str != null && str.length() >0){
            el = new Element("Comentario");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getProblemas();
        if (str != null){
            el = new Element("Problemas");
            el.setText(str);
            pel.addContent(el);
        }

        str = p.getDatosTecnicos();
        if (str != null && str.length() >0){
            el = new Element("DatosTecnicos");
            el.setText(str);
            pel.addContent(el);
        }
        
        str = p.getDuracion();
        if (str != null && str.length() >0){
            el = new Element("Duracion");
            el.setText(str);
            pel.addContent(el);
        }

        List<String> list = p.getPosters();
        for (String s : list) {
            el = new Element("Poster");
            el.setText(s);
            pel.addContent(el);
        }
        List<Tag> tags = p.getTags();
        for (Tag s : tags) {
            el = new Element("Tag");
            el.setText(s.getId()+"");
            pel.addContent(el);
        }
        return pel;
    }

    private static void grabar(Document docSal, String salida) throws Exception {
        File f = new File(salida);
        if (!f.exists()){
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
        XMLWriter.docAOutputStream(docSal, f);
    }

    /**
     * Genera un archivo HTML por cada estado (o varios, si la cantidad de pelis es mayor a
     * 'cantMaxPelisPorHTML'), en el directorio %htmlRoot%.
     */
    public static void  generarHTML(Peliculas pls ) throws IOException, JDOMException {
        generarHTML(pls, false);
    }

    private static void ordenarPorContador(List<Pelicula> peliculas) {
        Collections.sort(peliculas, new Comparator<Pelicula>(){
            public int compare(Pelicula p1, Pelicula p2){
                int a1 = p1.getContador();
                int a2 = p2.getContador();
                //si devuelvo negativo => va primero p1
                //quiero que vaya primero los de año >
                return a2 - a1;

            }
        });
    }
    private static void ordenarAñoNombre(List<Pelicula> peliculas) {
        Collections.sort(peliculas, new Comparator<Pelicula>(){
            public int compare(Pelicula p1, Pelicula p2){
                int a1 = getInt(p1.getAnnus());
                int a2 = getInt(p2.getAnnus());
                //si devuelvo negativo => va primero p1
                //quiero que vaya primero los de año >
                int res = a2 - a1;
                if (res != 0)
                    return res;
                else
                    return p1.getNombre().compareTo(p2.getNombre());
            }
        });
    }

    private static int getInt(String x){
        if (x == null)
            return 0;
        else{
            try {
                if (x.length() > 4)
                    x = x.substring(0,4);
                return Integer.parseInt(x);
            }
            catch (Exception ex) {
                System.out.println("problemas con el año = '" + x +"'" );
                return 0;
            }
        }
    }

    //Devuelve una lista, igual a la recibida como parámetro, pero excluyendo las que
    //solo están en DIVX
    private static List<Pelicula> excluirSoloDIVX(List<Pelicula> peliculas) {
        List<Pelicula> peliculasDVD = new ArrayList<Pelicula>();
        for (Pelicula p:peliculas){
            String id = p.getId();
            if (id != null && (id.contains("DVD")|| id.toLowerCase().contains("hd"))){
                peliculasDVD.add(p);
            }else{
//                System.out.println(" No se incluyó "+ p.getNombre() );
            }
        }
        return peliculasDVD;
    }

    private static List<Pelicula> invertir(List<Pelicula> peliculas) {
        int cant = peliculas.size();
        List<Pelicula> res = new ArrayList<Pelicula>(cant);
        for (int i = 0; i < cant; i++) {
            Pelicula p = peliculas.get(peliculas.size() - 1 - i);
            res.add(p);
        }
        return res;
    }

    public static Pelicula getPeliculaPorFoto(String name, Peliculas ps) {
        final List<Pelicula> peliculas = ps.getPeliculas();
        for (Pelicula p : peliculas) {
            if(p.getPosters().contains(name))
                return p;
        }
        return null;
    }

    private static class Compa implements Comparator {
        private Object[] ordenarPor;

        public Compa(Object[] ordenarPor) {
            this.ordenarPor = ordenarPor;
        }

        private  int comparar(String c1, String c2) {
            int res = 0;
            if (c1 != null && c2 != null)
                res =  c1.toLowerCase(locale).compareTo(c2.toLowerCase(locale));
            else
                if (c1 != null)
                    res = -1;
                else  if(c2 != null)
                    res = 1;

            return res;
        }

        public int compare(Object p1, Object p2) {
            if (ordenarPor == null)
                return 0;

            int res = 0;

            for(int i = 0; i < ordenarPor.length ; i++) {
                String c1 = getElemAComparar((Pelicula)p1, i);
                String c2 = getElemAComparar((Pelicula)p2, i);
                if (ordenarPor[i].equals(PeliculasFrame.IT_OR_ID ) &&
                        c1 != null && c2 != null &&
                        ((c1.startsWith("DIVX ") && c2.startsWith("DIVX ")
                            ||(c1.startsWith("DVD ") && c2.startsWith("DVD "))))){
                    int a = getInt(c1);
                    int b = getInt(c2);
                    res = a - b;
                }
                else
                    res = comparar(c1,c2);
                if (res != 0){
                    if (ordenarPor[i].equals(PeliculasFrame.IT_OR_AÑO ) )
                        res = res * -1; //primero van las más nuevas
                    return res;
                }
            }

            return res;
        }

        private int getInt(String s){
            int i = s.indexOf(" ");
            s = s.substring(i).trim();
            if (s.indexOf(" ")> 0)
                s = s.substring(0, s.indexOf(" "));
            return Integer.parseInt(s);
        }
        private String getElemAComparar(Pelicula p, int ind){
            String ord = ordenarPor[ind].toString();
            if (ord.equals(PeliculasFrame.IT_OR_NOMBRE))
                return p.getNombre();
            else if (ord.equals(PeliculasFrame.IT_OR_ID))
                return p.getId();
            else if (ord.equals(PeliculasFrame.IT_OR_ESTADO))
                return p.getEstado().toString();
            else if (ord.equals(PeliculasFrame.IT_OR_AÑO)) {
                String a = p.getAnnus();
                if (a != null)
                    return a;
                else
                    return "0";
            }
            return null;
        }
    }
}
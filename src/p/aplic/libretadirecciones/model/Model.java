package p.aplic.libretadirecciones.model;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import p.aplic.libretadirecciones.bobj.Direccion;
import p.aplic.libretadirecciones.proc.Ordenar;
import p.util.Fechas;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Model {
    private static String logFile = "renombrar.logFile";
    private static String xmlFile = null;
//    private static String xmlDirs = "";
//    private static String xmlDirs2 =
//            "C:\\Documents and Settings\\JP\\Escritorio\\agenda\\ordenado.xml";
//    private static String xmlDirs3 =
//            "";
    private static final Logger logger = Logger.getLogger(Model.class);

    private static List direcciones = null;
    private static File archivo = null;

    static public List<Direccion> getDirecciones()
            throws IOException, JDOMException {
        if (direcciones != null)
            return direcciones;
        if (xmlFile == null){
            throw new RuntimeException("No se definió el xml con los datos");
        }

        archivo = new File(xmlFile);
        if (!archivo.exists()){

            String message = "No existe el xml con los datos: " + xmlFile;
            JOptionPane.showMessageDialog(null, message);
            throw new RuntimeException(message);
        }

        direcciones = getDirecciones(archivo);
        return direcciones;
    }

    static public List<Direccion> getDirecciones(File f)
            throws IOException, JDOMException {
        List dirs;
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(f);
        dirs = getDirecciones(doc);
        return dirs;
    }

    static private List<Direccion> getDirecciones(Document doc) {
        Element r = doc.getRootElement();
        List<Direccion> res = new ArrayList<Direccion>(r.getContentSize());
        List pers = r.getChildren("Persona");
        Direccion dir;
        for (Object per1 : pers) {
            Element per = (Element) per1;
            dir = new Direccion();
            copiar(per, dir);
            res.add(dir);
        }

        return res;
    }

    static private void copiar(Element per, Direccion dir) {
        Element el = per.getChild("Apellido");
        if (el != null){
            dir.setApellido(el.getText());
        }

        el = per.getChild("Nombre");
        if (el != null){
            dir.setNombre(el.getText());
        }

        el = per.getChild("Trabajo");
        if (el != null){
            dir.setTelTrabajo(el.getText());
        }

        el = per.getChild("Casa");
        if (el != null){
            dir.setTelCasa(el.getText());
        }

        el = per.getChild("Correo");
        if (el != null){
            dir.setCorreo(el.getText());
        }

        List children = per.getChildren("Categoria");
        if (children != null){
            for(Object o : children) {
                dir.addCategoria(((Element)o).getText());
            }
        }

        el = per.getChild("Direccion");
        if (el != null){
            dir.setDirec(el.getText());
        }

        el = per.getChild("Movil");
        if (el != null){
            dir.setMovil(el.getText());
        }

        el = per.getChild("Notas");
        if (el != null){
            dir.setNotas(el.getText());
        }
        el = per.getChild("Historico");
        if (el != null){
            dir.setHistorico(el.getText().toLowerCase().startsWith("s"));
        }
        else
            dir.setHistorico(false);
    }

    private static void guardar() throws IOException {
        File f = archivo;
        String n = f.getAbsolutePath();
        guardar(direcciones, n);
    }

    private static void guardar(List dirs, String salida) throws IOException {

       hacerBackup(salida);
        try{
            Element raiz = new Element("Raiz");
            Document docSal = new Document(raiz);

            Ordenar.ordenar(dirs);
            direcciones = dirs;

            List pers = new ArrayList(dirs.size());
            for (Object dir1 : dirs) {
                Direccion dir = (Direccion) dir1;
                Element per = dir2Element(dir);
                pers.add(per);
            }

            raiz.addContent(pers);

            grabar(docSal, salida);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    //lo único que hace es renombrar el archivo
    private static void hacerBackup(String salida) {
        File f = new File(salida);
        String nuevo = salida + "_" + Fechas.getAAAADDMMHHMMSS("");
        boolean b = f.renameTo(new File(nuevo));
        if (!b)
            throw new RuntimeException("Falló buckup de archivo. Ver Model.hacerBackup()");
    }

    //HACER BACKUP ANTES DE LLAMAR A ESTE MÉTODO
    private static void grabar(Document docSal, String salida) throws Exception {
        XMLOutputter out = new XMLOutputter();
        Format format = out.getFormat();
        format.setEncoding("ISO-8859-1");
        format.setIndent("    ");
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(salida);
            out.setFormat(format);
            out.output(docSal, fos);
        } finally{
            if (fos != null)
                fos.close();
        }

    }

    private static Element dir2Element(Direccion dir) {
        Element per = new Element("Persona");

        String str = dir.getAppellido();
        if (str != null && !str.equals("")){
            Element el = new Element("Apellido");
            el.setText(str);
            per.addContent(el);
        }

        List<String> cates = dir.getCategorias();
        if (cates != null){
            for(String c : cates) {
                Element el = new Element("Categoria");
                el.setText(c);
                per.addContent(el);
            }
        }

        str = dir.getCorreo();
        if (str != null && !str.equals("")){
            Element el = new Element("Correo");
            el.setText(str);
            per.addContent(el);
        }

        str = dir.getDirec();
        if (str != null && !str.equals("")){
            Element el = new Element("Direccion");
            el.setText(str);
            per.addContent(el);
        }

        str = dir.getMovil();
        if (str != null && !str.equals("")){
            Element el = new Element("Movil");
            el.setText(str);
            per.addContent(el);
        }

        str = dir.getNombre();
        if (str != null && !str.equals("")){
            Element el = new Element("Nombre");
            el.setText(str);
            per.addContent(el);
        }

        str = dir.getNotas();
        if (str != null && !str.equals("")){
            Element el = new Element("Notas");
            el.setText(str);
            per.addContent(el);
        }

        str = dir.getTelCasa();
        if (str != null && !str.equals("")){
            Element el = new Element("Casa");
            el.setText(str);
            per.addContent(el);
        }

        str = dir.getTelTrabajo();
        if (str != null && !str.equals("")){
            Element el = new Element("Trabajo");
            el.setText(str);
            per.addContent(el);
        }


        if (dir.isHistorico()){
            Element el = new Element("Historico");
            el.setText("Sí");
            per.addContent(el);
        }
        return per;
    }

    public static List<String> getCategorias(){
        List<String> res = new Vector<String>();

        res.add("Trabajo");
        res.add("Médicos Matías");
        res.add("Médicos JP");
        res.add("Particular");
        res.add("Auto");
        res.add("Exactas");
        res.add("Comidas");
        res.add("Varios");
        res.add("Negocios");
        res.add("Mar del Plata");


        return res;
    }

    public static void agregarDireccion(Direccion d) throws IOException {
        String s = "Dirección nueva:\n    " + d.toString();
        log(s);
        direcciones.add(d);
        guardar();
    }

    public static void borrar(Direccion d) throws IOException {
        String s = "Dirección borrada:\n    " + d.toString();
        log(s);
        direcciones.remove(d);
        guardar();
    }

    public static void modificar(Direccion vieja, Direccion nueva) throws IOException {
        String s = "Dirección modificada:\n    " +
                "Vieja: " + vieja.toString() + "\n    " +
                "Nueva: " + nueva.toString();
        log(s);

        vieja.cargar(nueva);
        guardar();
    }


    private static void log(String s) {
//        try {
//            StringBuilder sb = new StringBuilder(Fechas.getDDMMAAAAHHMMSS());
//            sb.append(" " + s + Constantes.NUEVA_LINEA);
//            UtilFile.logEnUserDir(logFile, sb);
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//            JOptionPane.showMessageDialog(null, e);
//        }
    }

    public static void setXMLFile(String xml) {
        xmlFile = xml;
    }
}

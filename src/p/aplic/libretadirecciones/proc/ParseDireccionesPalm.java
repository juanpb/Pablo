package p.aplic.libretadirecciones.proc;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import p.util.UtilFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * User: JP
 * Date: 30/04/2005
 * Time: 12:36:50
 */
public class ParseDireccionesPalm {
    static String[] _tits;
    private static final Logger logger = Logger.getLogger(ParseDireccionesPalm.class);


    public static void main(String[] args) throws IOException {
        String LS = System.getProperty("line.separator");

        String n = "C:\\Documents and Settings\\JP\\Escritorio\\dir3.txt";
        File f = new File(n);
//        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            Element raiz = new Element("Raíz");
            Document doc = new Document(raiz);

//            fis = new FileInputStream(f);
            List lineas = UtilFile.getArchivoPorLinea(f);
            _tits = parseCabezera((String) lineas.get(0));
            Element e = nodoTitulos();
            raiz.addContent(e);

            for (int i = 1; i < lineas.size(); i++) {
                String s = (String) lineas.get(i);
                while (!s.endsWith("\"")){
                    i++;
                    s += LS + (String) lineas.get(i);
                }

                Element el = getFila(s);
                raiz.addContent(el);
            }

            XMLOutputter out = new XMLOutputter();
            Format format = out.getFormat();
            format.setEncoding("ISO-8859-1");
            String s = "C:\\Documents and Settings\\JP\\Escritorio\\sal.xml";
            fos = new FileOutputStream(s);
            out.output(doc, fos);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally{
//            if (fis != null)
//                fis.close();
            if (fos != null)
                fos.close();
        }

    }

    private static Element nodoTitulos() {
        Element res = new Element("Títulos");

        for (int i = 0; i < _tits.length; i++) {
            String tit = _tits[i];
            Element h = new Element("Tit_" + Integer.toString(i + 1));
            h.setText(tit);
            res.addContent(h);
        }

        return res;
    }

    private static Element getFila(String s) {
        Element us = new Element("Persona");
        int indCab = 0;
        if (!s.startsWith("\""))
            System.out.println("[No empieza con comillas] s = " + s);
        if (!s.endsWith("\""))
            System.out.println("[No ternmina con comillas] s = " + s);
        String buscar = "\",\"";
        s = s.substring(1, s.length() - 1) + buscar;
        int ind = s.indexOf(buscar);
        while (ind >= 0) {
            String o = s.substring(0, ind);
            s = s.substring(ind + buscar.length());
            ind = s.indexOf(buscar);

            System.out.println(_tits[indCab] + " = '" + o + "'");

            String tit = sacarComillas(_tits[indCab]);
            indCab++;
            if (o.length() == 0 )
                continue;

            Element e = new Element("Atributo");
            e.setAttribute("nombre", tit);
            e.setText(o);
            us.addContent(e);

        }
        System.out.println("");
        return us;
    }

    private static String sacarComillas(String tit) {
        if (tit.startsWith("\""))
            tit = tit.substring(1);
        if (tit.endsWith("\""))
            tit = tit.substring(0, tit.length() - 1);
        return tit;
    }

    private static String[] parseCabezera(String cab) {
        StringTokenizer st = new StringTokenizer(cab, ",", false);
        List vec = new Vector();
        while(st.hasMoreTokens()){
            String s = sacarComillas(st.nextToken());
            vec.add(s);
        }
        String[] res = new String[vec.size()];
        for (int i = 0; i < vec.size(); i++) {
            java.lang.String s = (java.lang.String) vec.get(i);
            res[i] = s;
        }
        return res;
    }
}

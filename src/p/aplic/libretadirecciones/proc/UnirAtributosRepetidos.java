package p.aplic.libretadirecciones.proc;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


public class UnirAtributosRepetidos {
    private static final Logger logger = Logger.getLogger(UnirAtributosRepetidos.class);
    public static void main(String[] args) throws IOException, JDOMException {
        UnirAtributosRepetidos u = new UnirAtributosRepetidos();
        u.hacer();
    }

    private void hacer() throws IOException, JDOMException {
        String n = "C:\\P\\Proy. ag\\todo.xml";
        File f = new File(n);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(f);

        FileOutputStream fos = null;
        try {
            Element rootEnt = doc.getRootElement();
            List psEnt = rootEnt.getChildren("Persona");

            Element raiz = new Element("Raíz");
            Document docSal = new Document(raiz);

            for (int i = 0; i < psEnt.size(); i++) {
                Element pEnt = (Element) psEnt.get(i);
                Element pSal = unir(pEnt);
                raiz.addContent(pSal);
            }

            XMLOutputter out = new XMLOutputter();
            Format format = out.getFormat();
            format.setEncoding("ISO-8859-1");
            String s = "C:\\P\\Proy. ag\\salUnida.xml";
            fos = new FileOutputStream(s);
            out.setFormat(format);
            out.output(docSal, fos);

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

    private Element unir(Element pEnt) {
        Element res = new Element(pEnt.getName());
        Hashtable ht = new Hashtable();
        List atrsEnt = pEnt.getChildren("Atributo");
        for (int i = 0; i < atrsEnt.size(); i++) {
            Element atEnt = (Element) atrsEnt.get(i);
            String nom = atEnt.getAttributeValue("nombre");
            String val = atEnt.getText();
            String o = (String)ht.get(nom);
            if (o == null)
                o = val;
            else
                o += ";" + val;
            ht.put(nom, o);

        }

        Element at = null;
        Enumeration en = ht.keys();
        while (en.hasMoreElements()) {
            String s = (String) en.nextElement();
            String v = (String)ht.get(s);
            at = new Element("Atributo");
            at.setAttribute("nombre", s);
            at.setText(v);
            res.addContent(at);
        }
        return res;
    }
}

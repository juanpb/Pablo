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
import java.util.HashMap;
import java.util.List;

/**
 * User: JP
 * Date: 03/05/2005
 * Time: 01:31:27
 */
public class CambiosDeTag {
    private static final Logger logger = Logger.getLogger(CambiosDeTag.class);
    public static void main(String[] args) throws IOException, JDOMException {
        CambiosDeTag u = new CambiosDeTag();
        u.hacer();
    }

    private void hacer() throws IOException, JDOMException {
        String n = "C:\\P\\Proy. ag\\todo.xml";
        File f = new File(n);
        if (!f.exists())
            f = new File("C:\\Documents and Settings\\JP\\Escritorio\\agenda\\todo.xml");
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
                Element pSal = cambiar(pEnt);
                raiz.addContent(pSal);
            }

            XMLOutputter out = new XMLOutputter();
            Format format = out.getFormat();
            format.setEncoding("ISO-8859-1");
            String s = "C:\\Documents and Settings\\JP\\Escritorio\\agenda\\\\salNF.xml";
            fos = new FileOutputStream(s);
            out.setFormat(format);
            out.output(docSal, fos);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally{
            if (fos != null)
                fos.close();
        }


    }

    private Element cambiar(Element pEnt) {
        Element res = new Element(pEnt.getName());
        List atrsEnt = pEnt.getChildren("Atributo");
        for (int i = 0; i < atrsEnt.size(); i++) {
            Element atEnt = (Element) atrsEnt.get(i);
            HashMap map = new HashMap();
            Element nuevoEl = null;
            String  nombre = null;
            String nomAt = atEnt.getAttributeValue("nombre");
            String valor = atEnt.getText();
            if (nomAt.equals("Apellido"))
                nombre = "Apellido";
            else if (nomAt.equals("Nombre"))
                nombre = "Nombre";
            else if (nomAt.equals("Trabajo"))
                nombre = "Trabajo";
            else if (nomAt.equals("Casa"))
                nombre = "Casa";
            else if (nomAt.equals("Correo"))
                nombre = "Correo";
            else if (nomAt.equals("Categoría"))
                nombre = "Categoría";
            else if (nomAt.equals("Direcciones"))
                nombre = "Direcciones";
            else if (nomAt.equals("Móvil"))
                nombre = "Móvil";
            else if (nomAt.equals("Notas"))
                nombre = "Notas";
            else
                map.put(nomAt, valor);

            if (nombre == null){
                System.out.println("problemas..........................");
            }
            if (nombre != null){
                nuevoEl = new Element(nombre);
                nuevoEl.setText(valor);
                res.addContent(nuevoEl);
            }
        }


        return res;
    }
}

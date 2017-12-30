package p.util.xml;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;

import java.io.*;

/**
 * User: JPB
 * Date: Jan 31, 2006
 * Time: 9:52:18 AM
 */
public class XMLWriter {

    /**
     * Genera la salida de un xml tabulado.
     *
     * @param doc   xml a grabar
     * @param file  destino del xml. Si existe se sobreescribe
     * @throws IOException
     */
    static public void output(Document doc, File file)  throws IOException {
        StringBuffer sb = XMLUtil.xml2String(doc);
        output(sb, file);
    }

    private static void output(StringBuffer sb, File file) throws IOException {
        BufferedOutputStream bos = null;
        try{
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] bytes = sb.toString().getBytes();
            bos.write(bytes);
        }finally{
            if (bos != null)
                bos.flush();
            if (bos != null) {
                bos.close();
            }
        }
    }

    public static void generarXMLTabulado(File entrada, File salida) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        org.jdom.Document doc = builder.build(entrada);
        FileOutputStream fos = new FileOutputStream(salida);
        docAOutputStream(doc, fos);
    }

    public static void docAOutputStream(org.jdom.Document docSal, String fileSalida) throws IOException {
        docAOutputStream(docSal, new FileOutputStream(fileSalida));
    }
    public static void docAOutputStream(org.jdom.Document docSal, File fileSalida) throws IOException {
        docAOutputStream(docSal, new FileOutputStream(fileSalida));
    }

    public static void docAOutputStream(org.jdom.Document docSal, OutputStream os) throws IOException {

        XMLOutputter out = new XMLOutputter();
        Format format = out.getFormat();
        format.setEncoding("ISO-8859-1");
        format.setIndent("    ");
        out.setFormat(format);
        out.output(docSal, os);
    }

    public static void main(String[] args) throws JDOMException, IOException {
        String a = "D:\\P\\java\\codigo\\Pablo\\config\\MNU_DESKTOP_001.xml";
        String sal = "D:\\P\\java\\codigo\\Pablo\\config\\MNU_DESKTOP_001_niu.xml";
        File e = new File(a);
        File s = new File(sal);
        XMLWriter x = new XMLWriter();
        x.generarXMLTabulado(e,s);
    }

}

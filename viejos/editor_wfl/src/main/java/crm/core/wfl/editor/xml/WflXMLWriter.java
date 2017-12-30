package crm.core.wfl.editor.xml;

import org.w3c.dom.*;

import java.io.*;

/**
 * User: JPB
 * Date: Jan 31, 2006
 * Time: 9:52:18 AM
 */
public class WflXMLWriter {

    /**
     * Genera la salida de un xml tabulado.
     *
     * @param doc   xml a grabar
     * @param file  destino del xml. Si existe se sobreescribe
     * @throws IOException
     */
    static public void output(Document doc, File file)  throws IOException {
        StringBuffer sb = WflXMLUtil.xml2String(doc);
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
                bos.close();
        }
    }
}

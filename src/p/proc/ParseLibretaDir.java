package p.proc;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * User: JP
 * Date: 30/04/2005
 * Time: 15:24:31
 */
public class ParseLibretaDir {
    private static final Logger logger = Logger.getLogger(ParseLibretaDir.class);

    public static void main(String[] args) throws Exception {

        String s = "C:\\Documents and Settings\\JP\\Escritorio\\ld.csv";
        String sal = "C:\\Documents and Settings\\JP\\Escritorio\\ld.txt";
        File f = new File(s);

        FileOutputStream fos = new FileOutputStream(sal);
        try{
            List lineas = UtilFile.getArchivoPorLinea(f);
            for (int i = 0; i < lineas.size(); i++) {
                String o = (String) lineas.get(i);
                int ii = o.indexOf(",");
                String no = o.substring(0, ii);
                o = o.substring(ii + 1);
                ii = o.indexOf(",");
                String ap = o.substring(0, ii);
                o = o.substring(ii + 1);

                System.out.println("o = " + o);
                String ss = "<Atributo nombre=\"Apellidos\">" + ap + "</Atributo> " + Constantes.NUEVA_LINEA;
                ss += "<Atributo nombre=\"Nombre\">" + no + "</Atributo> " + Constantes.NUEVA_LINEA;
                ss += "<Atributo nombre=\"Correo\">" + o + "</Atributo> " + Constantes.NUEVA_LINEA + Constantes.NUEVA_LINEA;
                fos.write(ss.getBytes());
            }
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally{
            if (fos != null)
                fos.close();
        }

    }
}

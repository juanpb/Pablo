package p.proc;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;
import p.util.UtilFile;

import java.io.File;
import java.util.List;

/**
 * User: Administrador
 * Date: 10/11/2006
 * Time: 19:50:07
 */
public class RenombrarPor {
    private static final Logger logger = Logger.getLogger(RenombrarPor.class);
    private String raiz = "";
    private String extension = ".ras";
    private int cont = 0;

    public static void main(String[] args) throws Exception {
        new RenombrarPor();
    }
    public RenombrarPor() throws Exception {
        DirFileFilter f = new DirFileFilter(false, extension);
        List list = UtilFile.archivos(new File(raiz), f);
        for (int i = 0; i < list.size(); i++) {
            File o = (File) list.get(i);
            aplicarRegla(o);
        }
        System.out.println("Listo. Se renombraron " + cont + " archivos");
    }

    private void aplicarRegla(File f) {
        agregar0SiTieneTam1(f);
    }

    private void agregar0SiTieneTam1(File f) {
        try {
            String n = f.getName();
            String s = n.substring(0, n.indexOf(extension));
            if (s.length() == 1){
                String nuevo = "0"+s+extension;
                f.renameTo(new File(f.getParent(), nuevo));
                cont++;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}

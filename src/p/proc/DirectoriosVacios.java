package p.proc;

import p.util.UtilFile;

import java.io.File;
import java.util.List;

/**
 * User: Administrador
 * Date: 10/03/2007
 * Time: 16:55:21
 */
public class DirectoriosVacios {

    private static String dirRaiz = "M:";

    public static void main(String[] args) throws Exception {
        listar(dirRaiz);
    }
    public static void listar(String dirRaiz) throws Exception {
        System.out.println("Lista de directorios vacíos;");
        List<String> dirs = UtilFile.directorios(dirRaiz);

        for (String dir : dirs) {
            File file = new File(dir);
            if (file.exists() && file.list() != null && file.list().length == 0)
                System.out.println("    " + dir);
        }
        System.out.println("Fin");
    }
}

package p.aplic.peliculas.util;

import p.util.UtilFile;

import java.io.File;
import java.util.List;

/**
 * User: Administrador
 * Date: 25/02/2007
 * Time: 17:56:00
 */
public class RenombrarFotosProc {
    private static String fotosARenombrar = "C:\\Documents and Settings\\Administrador\\Escritorio\\Películas\\Películas_archivos";
    private static String nombres = "C:\\Documents and Settings\\Administrador\\Escritorio\\Películas\\jpges.txt";

    public static void main(String[] args) throws Exception {
        hacer();
    }

    private static void hacer() throws Exception {
        List list = UtilFile.archivos(new File(fotosARenombrar));
        List<String> jpgs = UtilFile.getArchivoPorLinea(nombres);
        int cont = 0;
        int cont2 = 277;
        for (Object aList : list) {
            File o = (File) aList;
            o.renameTo(new File(o.getParent(), agregarCeros(cont2) + "_"+  jpgs.get(cont)));
            cont++;
            cont2++;
            if (cont == jpgs.size()){
                System.out.println("Se acabaron los jpg");
                break;
            }
        }
    }

    private static String agregarCeros(int cont) {
        if (cont < 10)
            return "00" + cont;
        if (cont < 100)
            return "0" + cont;
        return cont+"";
    }
}

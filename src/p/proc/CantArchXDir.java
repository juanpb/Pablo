package p.proc;

import p.util.DirFileFilter;
import p.util.Util;
import p.util.UtilFile;

import java.io.File;
import java.util.*;

/**
 * User: Administrador
 * Date: 26/08/2007
 * Time: 15:44:27
 */
public class CantArchXDir {

    public static void main(String[] args) throws Exception {
        Map<String, Integer> map = contar("F:\\Fotos");
        Set<String> dirs = map.keySet();
        ArrayList<String> list = new ArrayList<String>(dirs);
        Object[] ord = Util.ordenar(list);
        for (Object d : ord) {
            System.out.print(d + " = ");
            Integer cant = map.get(d);
            System.out.println(cant);
        }
    }

    public static Map<String, Integer> contar(String directorio) throws Exception {
        Map<String, Integer> res = new HashMap<String, Integer>();
        List<String> dirs = UtilFile.directorios(directorio);
        for (String dir : dirs) {
            int x = archivosXDir(dir);
            res.put(dir, x);
        }
        return res;
    }

    private static int archivosXDir(String dir) {
        DirFileFilter filter = new DirFileFilter(false, true, "jpg", false);
        File f = new File(dir);

        return f.listFiles(filter).length;
    }
}

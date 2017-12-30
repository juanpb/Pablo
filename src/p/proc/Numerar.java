package p.proc;

import p.util.UtilFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 6, 2009
 * Time: 12:43:27 PM
 *
 * Agrega ceros para archivos con contador.  
 */
public class Numerar {
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Hay que pasar como parámetro el nombre del directorio");
            return ;
        }
        ponerCeros(args[0], true);
    }

    public static void ponerCeros(String dir, boolean recursivo){
        List<String> dirs = new ArrayList<String>();

        if (recursivo)
            dirs.addAll(UtilFile.directorios(dir));
        else
            dirs.add(dir);
        for (String s : dirs) {
            int cmdd = getCantidadMayorDeDigitos(s);
            ponerCeros(s, cmdd);
        }

    }
    public static void ponerCeros(String dir){
        int cmdd = getCantidadMayorDeDigitos(dir);
        ponerCeros(dir, cmdd);
    }


    public static void ponerCeros(String dir, int tamFinal){
        File f = new File(dir);
        File[] files = f.listFiles();
        int cantArchModificados = 0;
        for(File file : files) {
            int t = tam(file);
            if (t > 0){
                boolean cambio = false;
                String n = file.getName();
                while (t < tamFinal){
                    cambio = true;
                    n = "0" + n;
                    t = tam(n);
                }
                if (cambio) {
                    File dest = new File(file.getParent(), n);
                    boolean seModificó = file.renameTo(dest);
                    if (seModificó)
                        cantArchModificados++;
                }
            }
        }
        System.out.println("Se modificaron " + cantArchModificados + " archivo/s");
    }

    /**
     * Devuleve la cantidad de dígitos máxima de los nombres de los
     * archivos que son números.
     */
    public static int getCantidadMayorDeDigitos(String dir){
//        dir += "\\";
        File f = new File(dir);
        File[] files = f.listFiles();
        if (files == null){//puede pasar si dir tiene acentos...
            System.out.println("Problemas con el dir: " + dir);
            System.out.println("Puede ser por acentos, etc. Dir = " + dir);
            return 0;
        }
        int res = 0;
        for(File file : files) {
            int t = tam(file);
            res = Math.max(res, t);
        }
        return res;
    }

    /**
     * Devuleve la cantidad de dígitos del nombre del archivo o 0 si no es un número.
     */
    private static int tam(String s){
        int ext = s.lastIndexOf(".");
        if (ext > 0)
            s = s.substring(0, ext);
        try {
            Integer.parseInt(s);
            return s.length();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static int tam(File f){
        if (f.isDirectory())
            return 0;
        String s = f.getName();
        return tam(s);
    }
}

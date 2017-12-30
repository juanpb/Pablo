package p.proc;

import p.util.DirFileFilter;
import p.util.UtilFile;

import java.io.File;
import java.util.List;

/**
 * User: Administrador
 * Date: 16/02/2008
 * Time: 10:17:04
 */
public class ExisteSub {

    public static void main(String[] args) throws Exception {
        if(args.length != 2){
            System.out.println("Modo de uso: java p.proc.ExisteSub dir extSub");
            System.exit(-1);
        }
        int contBuenos=0;
        int contMalos=0;
        List<File> avis = getAvis(args[0]);
        String extSub = args[1];
        if (!extSub.startsWith("."))
            extSub = "." + extSub;
        for (File avi : avis) {
            if (existeSubParaAvi(avi, extSub)){
                contBuenos++;
            }
            else{
                contMalos++;
                System.out.println("No existe sub para " + avi.getAbsolutePath());
            }
        }
        System.out.println("contBuenos = " + contBuenos);
        System.out.println("contMalos = " + contMalos);
    }

    public static List<File> getAvis(String dir) throws Exception {
        DirFileFilter filtro = new DirFileFilter(false, ".avi");
        return UtilFile.archivos(new File(dir), filtro, false);
    }

    public static boolean existeSubParaAvi(File avi, String extSub){
        String name = avi.getName();
        String n = name.substring(0, name.length()-4) + extSub;
        File f = new File(avi.getParent(), n);
        return f.exists();
    }
}

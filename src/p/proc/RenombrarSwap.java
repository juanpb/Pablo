package p.proc;

import java.io.File;


public class RenombrarSwap {

    public static void main(String[] args) {
        String dir = null;
//        if (args.length > 0){
//            dir = args[0];
//            System.out.println("dir = " + dir);
//        }
//        else{
//            System.out.println("Falta parámetro de entrada");
//            System.exit(-1);
//        }
        String raiz = "F:\\tem\\_\\Dolina\\copiadas\\2004-01\\";
        File raizF = new File(raiz);
        for (int i = 1; i < 10; i++) {
            dir = raiz + "0" + i;
            renombrarDir(dir);
        }
        for (int i = 10; i < 23; i++) {
            dir = raiz + i;
            renombrarDir(dir);
        }

    }

    private static void renombrarDir(String dir) {
        File dirF = new File(dir);
        if (!dirF.exists()){
            System.out.println("No existe el directorio " + dir);
            return ;
        }
        if (dirF.isDirectory()){
            renombrarDir(dirF);
        }
        else{
            System.out.println("No es un directorio");
            return ;
        }
    }

    private static void renombrarDir(File dirF) {
        File[] files = dirF.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            renombrar(f);
        }
    }

    /**
     * Cambia el nombre del archivo que debe tener la forma 000_111.ext a
     * 111_000.ext
     *
     * @param f
     */
    private static void renombrar(File f) {
        String name = f.getName();
        int i = name.lastIndexOf(".");
        String ext = "";
        if (i > 0){
            ext = name.substring(i + 1);
            name = name.substring(0, i);
        }
        i = name.indexOf("_");
        if (i < 0){
            System.out.println("No se renombra" + name);
            return ;
        }
        String prin = name.substring(0, i);
        String fin = name.substring(i + 1 );
        String nuevo = fin + "_" + prin + "." + ext;
        File nuevoFile = new File(f.getParent(), nuevo);
        System.out.println("Renombrado = " + nuevoFile);
        f.renameTo(nuevoFile);
    }
}

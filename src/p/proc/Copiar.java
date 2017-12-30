package p.proc;

import p.util.UtilFile;

import java.io.File;
import java.util.List;


public class Copiar {

    final static private String origen = "D:\\P\\m\\Música\\";
//    final static private String origen = "M:\\Música\\";
    final static private String destino = "D:\\P\\_env\\config\\borrar\\";

    /**
     * Copia los directorios que están en 'archInfo' (uno por línea, el valor de la línea
     * se concatena con 'origen') a destino.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            System.out.println("uso: java Copiar archInfo");
            System.exit(-1);
        }
        System.out.println("");
        Copiar c = new Copiar();
        String archInfo = args[0];
        c.hacer(archInfo);
        System.out.println("Listo");
    }

    private void hacer(String archInfo) throws Exception {
        File f = new File(archInfo);
        if (!f.exists()){
            System.out.println("No existe el archivo " + f.getAbsolutePath());
            return ;
        }
        List<String> lineas = UtilFile.getArchivoPorLinea(f);

        for(String subcarpetaOrigen : lineas) {
            if (origen == null || origen.trim().equals(""))
                continue;
            copiar(origen + subcarpetaOrigen.trim(), destino + subcarpetaOrigen);
        }
    }


    private void copiar(String origen, String destino) throws Exception {
        System.out.println("origen = " + origen);
        System.out.println("destino = " + destino);
        System.out.println("");

        File fOr = new File (origen);
        if (!fOr.exists()){
            System.out.println("No existe el archivo " + fOr.getAbsolutePath());
            return ;
        }
        File[] files = fOr.listFiles();
        for(File file : files) {
            if (file.isDirectory()) {
                String n = file.getName();
                String nuevoDes = destino + File.separator + n;
                copiar(file.getAbsolutePath(), nuevoDes);
            } else {
                String nn = destino + File.separator + file.getName();
                UtilFile.copiar(file, new File(nn));
            }
        }
    }

}
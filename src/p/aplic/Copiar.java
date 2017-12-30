package p.aplic;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class Copiar {

    private static int archBienCopiados;
    private static int archMalCopiados;

    public static void main(String[] args) throws IOException {
        if (args.length != 1){
            System.out.println("Modo de uso: java p.aplic.Copiar archIni");
            System.exit(-1);
        }
        Copiar.copiar(args[0]);
    }

    public static void copiar(String archIni) throws IOException {
        archBienCopiados = 0;
        archMalCopiados = 0;

        List<String> strings = UtilFile.getArchivoPorLinea(archIni);
        for (String s : strings) {
            parsearYCopiar(s);
        }
        System.out.println("Archivos bien copiados: " + archBienCopiados);
        System.out.println("Archivos mal copiados: " + archMalCopiados);
    }

    private static void parsearYCopiar(String linea) {
        StringTokenizer st = new StringTokenizer(linea, ",", false);
        String archOrigen = st.nextToken();
        while(st.hasMoreTokens()){
            String dirDest = st.nextToken().trim();
            if (dirDest.equals(""))
                continue;
            try {
                copiar(archOrigen, dirDest);
                archBienCopiados++;
            } catch (Exception e) {
                archMalCopiados++;
                System.out.println("Problemas al copiar el archivo " +
                    archOrigen + " al directorio " + dirDest);
                System.out.println(e.getMessage());
            }
        }
    }

    private static void copiar(String archOrigen, String dirDest)
            throws Exception {
        File orig = new File(archOrigen);
        File file = new File(dirDest, orig.getName());
        System.out.println("Copiando " + archOrigen + " al dirctorio "
                + dirDest + "...");
        UtilFile.copiar(orig, file);
    }
}

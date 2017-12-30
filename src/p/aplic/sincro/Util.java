package p.aplic.sincro;

import java.io.File;
import java.io.IOException;

/**
 * User: JPB
 * Date: Dec 1, 2008
 * Time: 5:27:14 AM
 */
public class Util {
    public static void main(String[] args) throws IOException {
        System.out.println("getRaizPD = " + getRaizPD());
    }

    public static String getRaizPD(){
        String[] candidatos = {"D", "H", "E", "F", "G", "I", "J"};

        for(String candidato : candidatos) {
            File f = new File(candidato + ":\\", "sinc");
            if (f.exists())
                return candidato + ":\\";
        }
        return null;
    }

    public static boolean sePuedeCopiar(String origen, String dirDestino){
        return sePuedeCopiar(new File(origen), new File(dirDestino));
    }

    public static boolean sePuedeCopiar(File origen, File dirDestino){
        String n = origen.getName();
        File d = new File(dirDestino, n);
        return ! d.exists();
    }



}

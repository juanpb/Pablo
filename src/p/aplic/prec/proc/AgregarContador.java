package p.aplic.prec.proc;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Apr 28, 2008
 * Time: 2:33:39 PM
 */
public class AgregarContador {
    public static void main(String[] args) throws IOException {
        hacer();
    }

    private static void hacer() throws IOException {
        String p = "D:\\P\\_env\\prc\\productos.txt";
        List<String> lineas = UtilFile.getArchivoPorLinea(p);
        List<String> salida = new ArrayList<String>();
        int cont=0;
        for (String l : lineas) {
            String n = cont++ + ";" + l + ";v";
            salida.add(n);
        }
        File f = new File(p + ".sal");
        UtilFile.guardartArchivoPorLinea(f,salida);
    }
}

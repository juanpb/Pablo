package p.proc.pplan;

import p.util.UtilFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 19/04/13
 * Time: 20:13
 */
public class EliminarCabDet {

    private final static String dirOrigen = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\pplan in";
    private final static String archSalida= "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\pplan in\\pp_unidos";

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        sacarCD(dirOrigen, archSalida);



        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    public static void sacarCD (String dirOrigen, String archSalida) throws IOException {
        File fileOrigen = new File(dirOrigen);
        final File[] files = fileOrigen.listFiles();
        for (File f : files) {
            final List<String> archivoPorLineaSinCD = getArchivoPorLineaSinCD(f);
            UtilFile.guardartArchivoPorLinea(new File(dirOrigen, f.getName() + "_sinCD"), archivoPorLineaSinCD);
        }


    }


    public static List<String> getArchivoPorLineaSinCD(File f)
            throws IOException{
        LineNumberReader lnr = null;
        int cont = 0;
        try{
            List<String> res = new ArrayList<String>();
            lnr = new LineNumberReader(new FileReader(f));
            lnr.readLine();//ignoro primera línea
            String linea = lnr.readLine();
            while(lnr.ready()){
                res.add(linea);
                linea = lnr.readLine();
                cont++;
            }
            return res;
        }
        finally{
            if (lnr != null)
                lnr.close();
        }
    }


}

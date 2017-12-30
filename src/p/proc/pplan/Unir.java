package p.proc.pplan;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * User: JPB
 * Date: 19/04/13
 * Time: 11:27
 */
public class Unir {

 //   private final static String dirOrigen = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\files_Divi\\separado";
  //  private final static String archSalida= "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\files_Divi\\separado\\imed_unidos";

    private final static String dirOrigen = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\pplan in\\sinCD";
    private final static String archSalida= "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\pplan in\\pp_unidos";

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        unir(dirOrigen, archSalida);



        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    public static void unir (String dirOrigen, String archSalida) throws IOException {
        File fileOrigen = new File(dirOrigen);
        final File[] filesAUnir = fileOrigen.listFiles();
        final List<File> files = Arrays.asList(filesAUnir);

        UtilFile.unir(files, new File(archSalida));
    }
}

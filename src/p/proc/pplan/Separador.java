package p.proc.pplan;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 18/04/13
 * Time: 23:51
 */
public class Separador {
    private static final int N1 = 72234;
    private static final int N2 = 72236;
    private static int contImedPorArchivos = 0;
    private static int contArchivosPP = 0;
    private static String dirSal = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\files_Divi\\separado";
    private static String dirEnt = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\files_Divi\\todos descomprimidos";


    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        final File file = new File(dirEnt);
        final File[] list = file.listFiles();
        System.out.println("Buscando para separar, list.length = " + list.length);
        for (File file1 : list) {
            if (file1.isFile())
                try {
                    separar(file1);
                } catch (Exception ex) {
                    System.out.println("Error al separar " + file1.getAbsolutePath());
                    ex.printStackTrace();
                }

        }

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");
        System.out.println("Cant. de arch para PP: " + contArchivosPP);


    }

    public static void separar(String archivo) throws IOException {
     separar(new File(archivo));
    }

    public static void separar(File archivo) throws IOException {
        final List<String> apl = UtilFile.getArchivoPorLinea(archivo);
        final List<String> sal = new ArrayList<String>();

        for (String s : apl) {
            int n = getImsi(s);
            if (n == N1 || n == N2){
                sal.add(s);
                contImedPorArchivos++;
            }
        }
        if (contImedPorArchivos > 0){
            String nuevo = archivo.getName()+ "_SEP";
            UtilFile.guardartArchivoPorLinea(new File(dirSal, nuevo) , sal);
            System.out.println("cant líneas en sep: (" + archivo + "_SEP)" + contImedPorArchivos);
            contArchivosPP++;
            contImedPorArchivos = 0;
        }
    }

    private static int getImsi(String s) {
        if (s.length() < 30){
            System.out.println("Linea < 30: " + s);
            return 0;
        }
        final String i = s.substring(25, 30);
        try {
            return Integer.parseInt(i);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}

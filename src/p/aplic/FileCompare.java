package p.aplic;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: Administrador
 * Date: 13/05/2006
 * Time: 15:41:52
 */
public class FileCompare {
//    private static String arch1 = "M:\\B5CA1096.bin";
//    private static String arch2 = "H:\\B5CA1096.bin";
    private static final Logger logger = Logger.getLogger(FileCompare.class);

    private static String dir = "F:\\_videos_\\Películas\\Sin ver\\_____grabadas\\Hiroshima mon amour (1959) [JAP-FRA]";
    private static String arch1 = dir + "\\" + "Hiroshima.Mon.Amour.1959.DVDRip.DivX.srt";
    private static String arch2 = dir + "\\" + "Hiroshima.Mon.Amour.1959.DVDRip.DivX.srtVP";

    public static void main(String[] args) {
        FileCompare fc = new FileCompare();
        try {
            fc.comparar(arch1, arch2);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ;
        }
        System.out.println("Son iguales");
    }

    private boolean comparar(String f1, String f2) throws IOException {
        boolean res = true;
        FileInputStream fos1 = new FileInputStream(f1);
        FileInputStream fos2 = new FileInputStream(f2);

        int tam1 = fos1.available();
        int tam2 = fos2.available();
        System.out.println("available = " + tam2);
        if (tam1 == tam2||true){
            byte[] b1 = new byte[tam1];
            byte[] b2 = new byte[tam2];
            fos1.read(b1);
            fos2.read(b2);

            for (int i = 0; i < b2.length; i++) {
                byte a = b1[i];
                byte b = b2[i];
                boolean bo = (a ==b);
                if(!bo){
                    System.out.println("Distintos");
                     res = false;
                    System.out.println("a = " + a);
                    System.out.println("b = " + b);
                }
            }
        }
        else{
            System.out.println("Tienen distinto tamaño, " + tam1 +" y " + tam2);
            return false;
        }
        return res;
    }
}

package p.aplic.peliculas.imdb;

import p.util.UtilFile;

import java.io.IOException;

/**
 * User: JPB
 * Date: Apr 20, 2009
 * Time: 12:53:55 PM
 */
public class Partir {
    private static String path = "D:\\P\\_env\\config\\pelis\\actors.list";

    public static void main(String[] args) throws IOException {
        UtilFile.partir(path, 10);
//        List<String> ls = UtilFile.getArchivoPorLinea(path);
//        int d = 0;
//        int tam = 500000;
//        int i = 0;
//        while ( d < ls.size()){
//            System.out.println("Guardando '" + (path+i) + "', d-h=" + d + "-" + d+tam);
//            UtilFile.guardartArchivoPorLinea(new File(path+i), ls, false, d, d+tam);
//            i++;
//            d += tam;
//        }

//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path) );
//
//        System.out.println("bis.available() = " + bis.available());
//        byte[] bytes = new byte[100];
//        bis.read(bytes);
//        System.out.println("bis.available()-100 = " + bis.available());
//        bis.read();
//        System.out.println("bis.available() -1= " + bis.available());

    }

}

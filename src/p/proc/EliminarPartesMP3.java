package p.proc;

import p.mp3.UtilMP3;
import p.util.UtilFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: JPB
 * Date: 10/04/15
 * Time: 11:29
 */
public class EliminarPartesMP3 {
    private static String archEntrada;
    private static String desde;
    private static String hasta;

    //TOTAL = 29.979.169 - ID3v1 is 128 bytes long  = 29979041 bytes || 125:29 = 7529 seg ==>>
    private static final int BYTE_POR_SEGUNDO = 3750; //aprox 29979041 / 7529;

//    public static void main(String[] args) throws IOException {
//        File x = new File("D:\\P\\videos\\youtube", "05-07_orig.mp3");
//        File fileSalida = new File("D:\\P\\videos\\youtube", "mp3");
//        fileSalida.createNewFile();
//        FileOutputStream fos = new FileOutputStream(fileSalida);
//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(x) );
//
//        byte[] bytes = new byte[2560];
//        bis.read(bytes);
//        fos.write(bytes);
//
//        //grabo desde el 'hasta' hasta el final del archivo
//
//        fos.close();
//        bis.close();
//    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3){
            System.out.println("Modo de uso: java EliminarPartesMP3 archEntrada desde hasta");
            return ;
        }
        archEntrada = args[0];
        desde = args[1];
        hasta = args[2];

        System.out.println("archEntrada = " + archEntrada);
        System.out.println("desde = " + desde);
        System.out.println("hasta = " + hasta);
        sacar();
    }

    private static void sacar() throws IOException {
        int d = tiempoAByte(desde);
        int h = tiempoAByte(hasta);
        File original = new File(archEntrada);
        File nuevo = new File(archEntrada + "_tmp");

        if (d > 0){//busco el prox sync
            int d2 = UtilMP3.buscarMP3Sync(original, d);
            if (d2 > 0)
                d = d2;
        }

        int i = UtilMP3.buscarMP3Sync(original, h);
        if (i > 0){
            h = i;

            //todo sacar
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(original) );
            bis.skip(h);

                int by = bis.read();
                int by2 = bis.read();
                int by3 = bis.read();
                int by4 = bis.read();
              bis.close();
                //FF    FB  10  C4
            if (by == 255 && by2 == 251 && by3 == 16 && by4 == 196)
                System.out.println("bien skip");
            else
                System.out.println("mal skip");
        }

        UtilFile.sacar(original, nuevo, d, h);

        boolean b = original.renameTo(new File(archEntrada + "_orig"));
        if (b)
            nuevo.renameTo(new File(archEntrada));
        else
            System.out.println("No se pudo renombrar los archivos");
    }

    private static int tiempoAByte(String hhmmss) {
        hhmmss = hhmmss.trim();
        int i = hhmmss.indexOf(":");
        int hh = Integer.parseInt(hhmmss.substring(0, i));

        hhmmss = hhmmss.substring(i+1);
        i = hhmmss.indexOf(":");
        int mm = Integer.parseInt(hhmmss.substring(0, i));

        int ss = Integer.parseInt(hhmmss.substring(i + 1));

        return (ss + mm * 60 + hh *60 * 60) * BYTE_POR_SEGUNDO;
    }

}

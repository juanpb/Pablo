package p.proc;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * User: JPB
 * Date: 10/04/15
 * Time: 11:29
 */
public class PartirYMoverArchivosEnOrden {
    private static String archEntrada;
    private static String dirSalida;
    private static File dirTmp;
    private static int cant;

    public static void main(String[] args) throws IOException {
        if (args.length != 4){
            System.out.println("Modo de uso: java PartirYMoverArchivosEnOrden archEntrada dirSalida dirTmp cant");
            return ;
        }
        archEntrada = args[0];
        dirSalida = args[1];
        dirTmp = new File(args[2]);
        cant = Integer.parseInt(args[3]);

        System.out.println("archEntrada = " + archEntrada);
        System.out.println("dirSalida = " + dirSalida);
        System.out.println("dirTmp = " + dirTmp);
        System.out.println("cant = " + cant);
        partir();
        mover();
    }

    private static void partir() throws IOException {
        UtilFile.partirMP3(new File(archEntrada), cant, dirTmp);
    }

    private static void mover() {
        File dirSalidaFile = new File(dirSalida);
        dirSalidaFile.mkdirs();
        File[] files = dirTmp.listFiles();
//        log(files);
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return f1.compareTo(f2);
            }
        });

        for (File file : files) {
            System.out.print("Moviendo '" + file.getAbsolutePath() + "' a '" + dirSalida + "' ... ");
            boolean b = file.renameTo(new File(dirSalida, file.getName()));
            if (b)
                System.out.println("Listo");
            else
                System.out.println("Falló");
        }
    }

    private static void log(File[] files) {
        for (File file : files) {
            System.out.println(file);
        }
    }
}

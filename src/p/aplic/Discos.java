package p.aplic;

import p.util.Constantes;
import p.util.DirFileFilter;
import p.util.UtilFile;

import java.io.File;
import java.io.FileOutputStream;

/**
 * User: Administrador
 * Date: 11/02/2008
 * Time: 19:26:11
 */
public class Discos {

    public static void main2(String[] args) {
        if (args.length != 2){
            System.out.println("Modo de uso: " +
                    "java p.aplic.Discos archSalida raizDiscosEntrada");
            System.exit(-1);
        }

        //creo el archivo con los discos
        File f = new File (args[0]);
        try {
            f.createNewFile();
            ListaCompleta lc = new ListaCompleta();
            lc.discos(f, args[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        long ini = System.currentTimeMillis();

        StringBuffer sb = new StringBuffer();
        //creo el archivo con los discos
//        String dirRaiz = "m:\\Música\\";
        String dirRaiz = "m:\\Música\\";
        File f = new File (dirRaiz);
        File fSal = new File (dirRaiz, "dct.txt");
        try {
            DirFileFilter filter = new DirFileFilter(true);
            File[] arts = f.listFiles(filter);
            for(File art : arts) {
                File[] discos = art.listFiles(filter);
                agregar(art.getName(), discos, sb);
            }

            FileOutputStream fos = new FileOutputStream(fSal);
            fos.write(sb.toString().getBytes());
            fos.close();
            System.out.println("Se grabó la salida en: " + fSal.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");
        
    }

    private static void agregar(String artista, File[] discos, StringBuffer sb){
        for(File disco : discos) {
            long tamEnBytes = UtilFile.getSize(disco);
            sb.append(artista).append(Constantes.TAB_CHARACTER).
                    append(disco.getName()).append(Constantes.TAB_CHARACTER).
                    append((tamEnBytes/(1024*1024))).append(Constantes.NUEVA_LINEA);
        }
    }

}

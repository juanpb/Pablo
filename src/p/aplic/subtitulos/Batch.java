package p.aplic.subtitulos;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: Juan
 * Date: 12/04/15
 * Time: 17:36
 */
public class Batch {
    private String dirEntrada = "J:\\videos - Series\\The Big.Bang Theory (2007 - )\\02\\3";

    public static void main(String[] args) throws IOException {
        Batch b = new Batch();
        b.correr();
    }

    private void correr() throws IOException {
        File file = new File(dirEntrada);
        File[] files = file.listFiles();
        for (File f : files) {
            modificar(f);
        }
    }

    private void modificar(File f) throws IOException {
        List<Subtitulo> subs = Util.getSubtitulos(f);
        Util.offset(0, 0, 1, 0, subs, true);
        grabar(f, subs);
    }


    private void grabar(File fileOriginal, List<Subtitulo> subs) throws IOException {
        String nombreArchOrig = fileOriginal.getAbsolutePath();
        fileOriginal.renameTo(new File(nombreArchOrig + ".orig"));//backup
        File fileSal = new File(nombreArchOrig);

        Util.guardarFormatoSRT(fileSal, subs);

    }
}


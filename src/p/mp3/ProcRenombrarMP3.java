package p.mp3;

import java.io.File;

/**
 * Date: 22/09/14
 * Proceso que renombra los mp3 de un directorio. El resultado es ART - AAAA - ALB - Tema.mp3
 * La información la saca del ID3 tag
 */
public class ProcRenombrarMP3 {
    private static final String dirEntrada = "D:\\P\\Música\\_mp4";
//    private static final String dirSalidaOK = "D:\\P\\Música\\_mp4\\2";

    public static void main(String[] args) throws Exception {
        File fileEntrada = new File(dirEntrada);
//        File fileSalida = new File(dirSalidaOK);

        int contOk = 0;
        int contError = 0;

        File[] files = fileEntrada.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith("mp3")){
                MP3File mp3 = new MP3File(file);
                //ART - AAAA - ALB - Tema
                String art = mp3.getArtista();
                String annus = mp3.getAnnus();
                String album = mp3.getAlbum();
                String titulo = mp3.getTitulo();
                if (art != null && album != null && annus != null && titulo != null &&
                        !art.isEmpty() && !album.isEmpty() && !annus.isEmpty() && !titulo.isEmpty()){
                    String nuevoNombre = art + " - " + annus + " - " + album + " - " + titulo + ".mp3";
                    boolean b = file.renameTo(new File(file.getParent(), nuevoNombre));
                    if (!b){
                        System.out.println("Falló. '" + nuevoNombre + "'");
                        contError++;
                    }
                    else
                        contOk++;
                }
                else{
                    System.out.println("Faltan datos id3 en: " + file.getName());
                    contError++;
                }
            }
        }
        System.out.println("contError = " + contError);
        System.out.println("contOk = " + contOk);
    }
}

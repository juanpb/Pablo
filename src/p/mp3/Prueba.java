package p.mp3;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: JP
 * Date: 28/11/2004
 * Time: 13:44:27
 */
//http://www.onjava.com/pub/a/onjava/2004/08/11/javasound-mp3.html?page=last
public class Prueba {
    static String dir = "D:\\P\\Música\\_mp4\\-\\";
    static String arch = dir + "Adriana Calcanhotto - 2000 - Público - Clandestino.mp3";

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File archivo = new File("D:\\P\\videos\\youtube\\prueba.bin");
        int i = UtilMP3.buscarMP3Sync(archivo, 0);
        System.out.println("i = " + i);
//        crearArchPrueba();
//        File file = new File(arch);
//
//        AudioFileFormat baseFileFormat =
//            AudioSystem.getAudioFileFormat(file);
//        Map properties = baseFileFormat.properties();   ->>>>  J2SE 1.5,
//        AudioInputStream in = AudioSystem.getAudioInputStream(file);
//        AudioInputStream din = null;
//        AudioFormat baseFormat = in.getFormat();
//        AudioFormat decodedFormat =
//                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
//                        baseFormat.getSampleRate(),
//                        16,
//                        baseFormat.getChannels(),
//                        baseFormat.getChannels() * 2,
//                        baseFormat.getSampleRate(),
//                        false);
//        din = AudioSystem.getAudioInputStream(decodedFormat, in);
//        // Play now.
//        rawplay(decodedFormat, din);
//        in.close();
    }


    private static void rawplay(AudioFormat targetFormat,
                         AudioInputStream din)
            throws IOException, LineUnavailableException {
        System.out.println("1");
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        System.out.println("2");
        if (line != null) {
            // Start
            line.start();
            int nBytesRead = 0;
            int cont = 0;
            while (nBytesRead != -1) {
                System.out.println(cont++);
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1)
                    line.write(data, 0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private static SourceDataLine getLine(AudioFormat audioFormat)
            throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info =
                new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    public static void crearArchPrueba() throws IOException {
        File archivo = new File("D:\\P\\videos\\youtube\\prueba.bin");

        archivo.createNewFile();
        FileOutputStream fos = new FileOutputStream(archivo);

        for (int i = 0; i < 10; i++) {
            fos.write(i);
        }
            fos.write(255);
            fos.write(251);
            fos.write(16);
            fos.write(196);

        for (int i = 20; i < 30; i++) {
            fos.write(i);
        }
        fos.close();
    }
}

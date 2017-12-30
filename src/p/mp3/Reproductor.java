package p.mp3;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * User: JP
 * Date: 28/11/2004
 * Time: 13:44:27
 */
//http://www.onjava.com/pub/a/onjava/2004/08/11/javasound-mp3.html?page=last
public class Reproductor {
    static String arch = "D:\\BITSDATA\\DATA04\\LAN01\\SOUND01.AIF";


    public static void play(String file)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        play(new File(file));
    }

    public static void play(File file)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        AudioInputStream in = AudioSystem.getAudioInputStream(file);

        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat =
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false);
        AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);

        // Play now.
        rawplay(decodedFormat, din);
        in.close();
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
        SourceDataLine res;
        DataLine.Info info =
                new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}

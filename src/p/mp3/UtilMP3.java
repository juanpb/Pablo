package p.mp3;

import de.ueberdosis.mp3info.UndersizedException;
import de.ueberdosis.mp3info.id3v2.ID3V2Writer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class UtilMP3 {

    public static void borrarID3v3(String d)
            throws IOException, UndersizedException {
        borrarID3v3(new File(d));
    }


    public static void borrarID3v3(File d)
            throws IOException, UndersizedException {
        if (d.isFile()){
            ID3V2Writer.borrarTag(d);
        }
        else{
            File[] files = d.listFiles();
            for(File file : files) {
                borrarID3v3(file);
            }
        }
    }


    public static int buscarMP3Sync(File entrada, int desde) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(entrada) );
        bis.skip(desde);
        int res = desde;

        boolean seguir = true;

        if(bis.available() > 3){
            int by = bis.read();
            int by2 = bis.read();
            int by3 = bis.read();
            int by4 = bis.read();
            res +=4;
            while(true){
                    //FF    FB  10  C4
                if (by == 255 && by2 == 251 && by3 == 16 && by4 == 196){
                    return res-4;
                }else{
                    if ( bis.available() > 0){
                        by = by2;
                        by2 = by3;
                        by3 = by4;
                        by4 = bis.read();
                        res++;
                    }
                    else {
                        return -1;
                    }
                }
            }
        }
        return -1;
    }
}

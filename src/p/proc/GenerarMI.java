/**
 * Created by IntelliJ IDEA.
 * User: JPB
 * Date: Jul 7, 2004
 * Time: 12:26:51 PM
 * To change this template use Options | File Templates.
 */
package p.proc;

import org.apache.log4j.Logger;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class GenerarMI{
    private static final Logger logger = Logger.getLogger(GenerarMI.class);

    public static void main(String[] args){
        try
        {
            unir();
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    private static void z(){
        String w = "http://www.mansioningles.com/Sonidosvocab/Vocab";

        for (int i = 1; i < 63; i++)
        {
            for (int j = 1; j < 7; j++)
            {
                String a = w + i + "_" + j + ".wma";
                System.out.println(a);
            }
        }
    }

    private static void unir() throws IOException
    {
        String dirEnt = "F:\\tem\\inglés\\vocabulario\\__ing";
        String dirSal = dirEnt + "\\" + "sal";
        String w = "Vocab";
        for (int i = 10; i < 63; i++)
        {
            List lista = new Vector();
            File sal = new File(dirSal, "unido_" + i + ".mp3");
            System.out.println("sal.getAbsolutePath() = " + sal.getAbsolutePath());
            for (int j = 1; j < 7; j++)
            {
                String n = w + i + "_" + j + ".mp3";
                File f = new File(dirEnt, n);
                if (f.exists())
                    lista.add(f);
                else
                    break;
            }
            if (lista.size() > 0){
                UtilFile.unir(lista, sal);
            }
        }
    }
}

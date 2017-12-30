package p.aplic;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.DirFileFilter;
import p.util.UtilFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: JPB
 * Date: Feb 14, 2005
 * Time: 4:08:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CDATxt implements Constantes {
    private static final Logger logger = Logger.getLogger(CDATxt.class);

    public static void main(String[] args) {
        new CDATxt(args);
    }

    public CDATxt(String[] args){
        StringBuffer sal = new StringBuffer();
        String dirCD;

        if (args.length != 2){
            String msg = "usar: java p.apli.CDATxt [unidad de cd] [arch. sal.]";
            JOptionPane.showMessageDialog(new JPanel(), msg);
            System.exit(-1);
        }
        dirCD = args[0];
        if (dirCD.length() == 1)
            dirCD += ":";
        String archSal = args[1];

        File raiz = new File(dirCD);
        DirFileFilter fil = new DirFileFilter(true);
        File[] artis = raiz.listFiles(fil);
        for (int i = 0; i < artis.length; i++) {
            File arti = artis[i];
            sal.append(TAB + arti.getName() + NUEVA_LINEA);
            File[] dis = arti.listFiles(fil);
            for (int j = 0; j < dis.length; j++) {
                File di = dis[j];
                sal.append(di.getName() + NUEVA_LINEA);
            }
        }
        try {
            UtilFile.agregarAlFinal(sal.toString(), new File(archSal));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

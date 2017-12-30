package p.aplic;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.Hora;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DemonioIProxy extends TimerTask implements Constantes{
    private static final Logger logger = Logger.getLogger(DemonioIProxy.class);

    private static boolean _version1 = false;
    private static String _exe = "G:\\Archivos de programa\\AnalogX\\Proxy\\proxy.exe"; //ABAJO
//    static String _exe = "C:\\Archivos de programa\\Outlook Express\\msimn.exe"; //ARRIBA
    private static long _delay = Hora.MS_EN_MIN * 1;
    private static long _repetir = Hora.MS_EN_MIN * 5;
    private static Process _process;
    private static String ARCHIVO_LOG = "Y:\\iProxyLog.txt";

    public DemonioIProxy()
    {
    }
    public static void main(String[] args) throws Exception
    {


        DemonioIProxy tt = new DemonioIProxy();
        Timer t = new Timer(false);

        //empieza en 3 minutos y se repite cada 15
        System.out.println("t.schedule(tt, _delay, _repetir)");
        if (_version1){
            t.schedule(tt, _delay, _repetir);
        }else{
            t.schedule(tt, 0);
        }
    }

    /* interfaz TimerTask*/


    public void run() {
        System.out.println("run(), versión1 = " + _version1 + " - " + new Date());
        try{
            _process = Runtime.getRuntime().exec(_exe);
            while (true){
                long ini = System.currentTimeMillis();
                long fin = System.currentTimeMillis();
                fin = fin -ini;

                if (fin < 500){
                    log("duró: " + fin + " ms.");
                    System.exit(0);
                }
                log("murió: " + new Date());
                Thread.sleep(Hora.MS_EN_SEG * 1);
                _process = Runtime.getRuntime().exec(_exe);

            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private void log(String c){
        try{
            File file = new File(ARCHIVO_LOG);
            if (!file.exists())
                file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), true);
            c += NUEVA_LINEA;
            fos.write(c.getBytes());
            fos.close();
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}

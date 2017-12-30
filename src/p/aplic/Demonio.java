package p.aplic;

import org.apache.log4j.Logger;
import p.util.Hora;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Demonio extends TimerTask{
    private static final Logger logger = Logger.getLogger(Demonio.class);
    static boolean _version1 = false;
    static String _exe = "G:\\Archivos de programa\\Outlook Express\\msimn.exe"; //ABAJO
//    static String _exe = "C:\\Archivos de programa\\Outlook Express\\msimn.exe"; //ARRIBA
    static long _delay = Hora.MS_EN_MIN * 1;
    static long _repetir = Hora.MS_EN_MIN * 5;
    private static Process _process;
    static String ARCHIVO_SALIR = "C:\\comp\\salir.txt";
    static String ARCHIVO_SALIR_2 =
            "E:\\Documents and Settings\\Pablo\\Escritorio\\salir.txt";

    public Demonio()
    {
    }
    public static void main(String[] args) throws Exception
    {
        if (args.length == 1){
            String x = args[0];
            if (x.startsWith("v1"))
                _version1 = true;
            else if (x.startsWith("v2"))
                _version1 = false;
        }

        if (args.length > 1){
            String x = args[0];
            long lon = Long.parseLong(x);
            _delay = lon * Hora.MS_EN_MIN;

            x = args[1];
            lon = Long.parseLong(x);
            _repetir = lon * Hora.MS_EN_MIN;
        }

        Demonio tt = new Demonio();
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

            if (_version1 == false){
                while (true){
                    long ini = System.currentTimeMillis();
                    int x = _process.waitFor();
                    long fin = System.currentTimeMillis();
                    fin = fin -ini;
                    System.out.println("duró: " + fin + " ms.");
                    if (fin < 500)
                        System.exit(0);

                    nosVamos();
                    System.out.println("run: " + x);
                    Thread.sleep(Hora.MS_EN_SEG * 20);
                    _process = Runtime.getRuntime().exec(_exe);

                }
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }


    private void nosVamos() throws Exception{
        File file = new File(ARCHIVO_SALIR);
        if (file.exists()){
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            while(lnr.ready()){
                String linea = lnr.readLine();
                if (linea != null && !linea.equals("")){
                    lnr.close();
                    System.exit(0);
                }
            }
            lnr.close();
        }
        file = new File(ARCHIVO_SALIR_2);
        if (file.exists()){
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            while(lnr.ready()){
                String linea = lnr.readLine();
                if (linea != null && !linea.equals("")){
                    lnr.close();
                    System.exit(0);
                }
            }
            lnr.close();
        }
    }
}

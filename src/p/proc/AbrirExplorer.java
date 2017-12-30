package p.proc;

import org.apache.log4j.Logger;
import p.util.Hora;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: JPB
 * Date: Dec 15, 2004
 * Time: 12:27:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbrirExplorer {
    private static final Logger logger = Logger.getLogger(AbrirExplorer.class);
    private static String _www = "http://oink.me.uk";
    private static long _esperarAntesDeCerrar = 20 * 1000;
    private static String _exe = "C:\\Archivos de programa\\Internet Explorer\\iexplore.exe";
//    static String _exe = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
    private long _delay = 0;
    private long _horas = 12;

    public static String getExe() {
        return _exe;
    }

    public static String getWww() {
        return _www;
    }

    public static long getEsperarAntesDeCerrar() {
        return _esperarAntesDeCerrar;
    }

    public static void main(String[] args) {
        //delay en ms
        AbrirExplorer ae = new AbrirExplorer();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-delay")){
                String tem = arg.substring(6).trim();
                ae._delay = Long.parseLong(tem);
            }else if (arg.startsWith("-horas")){
                String tem = arg.substring(6).trim();
                ae._horas = Long.parseLong(tem);
            }
        }
        ae.empezar();
    }

    void empezar(){
        Timer timer = new Timer(false);

        long period = _horas * Hora.MS_EN_HORA;
        timer.schedule(new Tarea(), _delay, period);
    }
}


class Tarea extends TimerTask{
    private static final Logger logger = Logger.getLogger(Tarea.class);

    public void run() {
        String exe = AbrirExplorer.getExe() + " "  + AbrirExplorer.getWww();
        try {
            Process proc = Runtime.getRuntime().exec(exe);
            Thread.sleep(AbrirExplorer.getEsperarAntesDeCerrar());
            if (proc != null)
                proc.destroy();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
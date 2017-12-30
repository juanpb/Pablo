package p.aplic;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.Hora;
import p.util.Util;
import p.util.UtilString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Date;

public class IniciarDemonios implements Constantes{
    private static final Logger logger = Logger.getLogger(IniciarDemonios.class);

    private static String ARCHIVO_LOG = "C:\\comp\\demonios.log";

//    public static void main(String[] args)
//    {
//        String str = "C:\\Pablo\\java\\proyectos\\ID.ini";
//        DemonitoMoverArchivos a =
//                new DemonitoMoverArchivos(new File(str));
//        a.run();
//    }
    public static void main(String[] args) throws Exception{
        crearLog();
//        String archAIniciar = "F:\\Mis documentos\\Exe\\Mis programas\\Iniciar.txt"; //args[0];
        String archAIniciar = args[0];
        if (args.length > 1){
            File fileIni;
            String tem = args[1];
            int x = tem.indexOf("/");
            int y = tem.indexOf("\\");
            //Si el segundo parámetro no tiene path se toma el mismo
            //que el primer parámetro.
            if ((x + y) == 0){
                int z = archAIniciar.lastIndexOf("\\");
                String path = archAIniciar.substring(0, z);
                fileIni = new File(path, tem);
            }
            else
                fileIni = new File(tem);

            String mover = System.getProperty("MOVER");
            if (mover != null && mover.trim().equalsIgnoreCase("s"))
                new DemonitoMoverArchivos(fileIni).start();
        }
        log("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        log("Inicio: " + new java.util.Date().toString());
        log("Archivo a parsear: " + archAIniciar);
        File file = new File(archAIniciar);
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        while(lnr.ready()){
            String linea = lnr.readLine();
            ejecutar(linea);
        }
        lnr.close();

    }

    public static void log(String s) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ARCHIVO_LOG, true);
            s += NUEVA_LINEA;
            fos.write(s.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private static void ejecutar(String linea) throws Exception{
        if (linea.startsWith("//"))
            return;
        int i = linea.indexOf("-");
        int seg;
        String exe = "";
        if (i > 0){
            seg = Integer.parseInt(linea.substring(0, i));
            exe = linea.substring(i + 1).trim();
            log("Se abrirá " + exe + " en " + seg + " segundos. ");
        }
        else{
            seg = Integer.parseInt(linea.trim());
            log("Demora de " + seg + " segundos. ");
        }
        Thread.sleep(seg * 1000);

        if (!exe.equals("")){
            if (exe.startsWith("UV-")) {
                //se ejecuta una única vez
                exe = UtilString.reemplazarUnaVez(exe, "UV-", "");
                log ("(((Se ejecutará por única vez: " + exe + ")))");
                try {
                    Runtime.getRuntime().exec(exe);
                } catch (Exception ex) {
                    log(ex.getMessage());
                    ex.printStackTrace();
                }
            }
            else
                new Demonito(exe).start();
        }

    }


    private static void crearLog() throws Exception{
        File file = new File(ARCHIVO_LOG);
        if (!file.exists())
            file.createNewFile();
    }
}

class Demonito extends Thread{
    private static Process _process;
    private String _exe;
    private long ESPERAR_AL_CERRAR = Hora.MS_EN_SEG * 50;
    private static final Logger logger = Logger.getLogger(Demonito.class);

    public Demonito(String exe) {
        _exe = exe;
    }

    public void run() {
        try{
            _process = Runtime.getRuntime().exec(_exe);
            boolean salir = false;
            while (!salir){
                long ini = System.currentTimeMillis();
                _process.waitFor();
                long fin = System.currentTimeMillis();
                fin = fin -ini;
                if (fin < 3000){
                    //puede ser que ya esté corriendo
                    salir = true;
                }

                String msg = (new Date().toString()) + _exe + " duró: " +
                                (fin/1000) + " seg." + Constantes.NUEVA_LINEA;
                if (salir)
                    msg += "    (No se volverá a abrir)";
                else
                    msg += "    (Se volverá a abrir en "+ (ESPERAR_AL_CERRAR/1000) + " seg.)";
                IniciarDemonios.log(msg);
                Thread.sleep(ESPERAR_AL_CERRAR);
                _process = Runtime.getRuntime().exec(_exe);

            }
        }catch(Exception e){
            IniciarDemonios.log(e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

}

class DemonitoMoverArchivos extends Thread{
    private long    MINUTOS_ENTRE_CICLOS_DEFAULT = (long)(60) * Hora.MS_EN_MIN;
    private String  _dirOrig;
    private String  _dirDest;
    private long    _minEntreCiclos = MINUTOS_ENTRE_CICLOS_DEFAULT;
    private static final Logger logger = Logger.getLogger(DemonitoMoverArchivos.class);



    public DemonitoMoverArchivos(File archiviIni) {
        Util.loadProperties(archiviIni.getAbsolutePath());
        _dirOrig = System.getProperty("DIRECTORIO_ORIGEN");
        _dirDest = System.getProperty("DIRECTORIO_DESTINO");
        String minTem = System.getProperty("MINUTOS_ENTRE_CICLOS");
        if (minTem != null && !minTem.trim().equals("")){
            long tem = Long.parseLong(minTem);
            _minEntreCiclos = tem * Hora.MS_EN_MIN;
        }
    }

    public void run() {
        IniciarDemonios.log("Se inició 'Mover archivos");
        IniciarDemonios.log("   Dir. origen = " + _dirOrig);
        IniciarDemonios.log("   Dir. destino = " + _dirDest);
        IniciarDemonios.log("   Min. entre ciclos = " + (_minEntreCiclos / Hora.MS_EN_MIN));


        boolean salir = false;
        File origen = new File(_dirOrig);
        File dest = new File(_dirDest);
        while (!salir){
//            IniciarDemonios.log("Nuevo ciclo " + new Date().toString());
            File[] lista = origen.listFiles();
            if (lista != null){
                for (File aMover : lista) {
                    try {
                        if (aMover.isFile()) {
                            String nom = aMover.getName();
                            File des = new File(dest, nom);
                            aMover.renameTo(des);
                        }
                    } catch (Exception e) {
                        IniciarDemonios.log(e.getMessage());
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            try{
                Thread.sleep(_minEntreCiclos);
            }catch (Exception ex){
                IniciarDemonios.log(ex.getMessage());
                    ex.printStackTrace();
            }
        }
        IniciarDemonios.log("-------fin mover demonio----");
    }
}
package p.pruebas;

import p.util.*;

import java.util.*;
import java.io.*;


public class T {

    static Vector _horarios = null;
    static String ARCHIVO_TIEMPOS = "C:\\comp\\tiempos.txt";
    static String ARCHIVO_ULT_ACCESO = "C:\\comp\\ultAcceso.txt";
    static String ARCHIVO_CONT = "C:\\comp\\cont.txt";
    static String ARCHIVO_LOG = "C:\\comp\\log.txt";
    static String EXE = "spider";
    static int    _cont;
    static int    MAX_CONT = 8;
    static String NUEVA_LINEA = "\n";

    public T() {
    }


    public static void main(String[] args) throws Exception{
        try{
            Process proc = Runtime.getRuntime().exec(EXE);
            init();
            Object o = _horarios.get(_cont);
            int mins = Integer.parseInt((String)o);
            Thread.sleep(mins * Hora.MS_EN_MIN);
            matar(proc);
        }catch(Exception e){
            manejarExcepcion(e);
        }
        System.exit(0);
    }

    static void init() throws Exception{
        guardarLog("Iniciado: " + new Date());
        levantarTiempos();
        levantarContador();
        if (_cont < MAX_CONT) //si llegó al máx. => no aumento más
            guardarContador(_cont + 1);
    }

    static void levantarTiempos() throws Exception {
        _horarios = new Vector();
        File file = new File(ARCHIVO_TIEMPOS);
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        while(lnr.ready()){
            String linea = lnr.readLine();
            if (linea != null && !linea.equals(""))
                _horarios.add(linea);
        }
        lnr.close();
    }

    static void levantarContador() throws Exception {
        File fileUA = new File(ARCHIVO_ULT_ACCESO);
        File fileCont = new File(ARCHIVO_CONT);

        //si no existe el archivo con el último acceso => lo creo
        if (!fileUA.exists()){
            fileUA.createNewFile();
            FileWriter fw = new FileWriter(fileUA);
            fw.write(Long.toString(System.currentTimeMillis()));
            fw.close();
            _cont = 0;
            return;
        }

        //Si pararon más de 3 horas desde el último acceso => empieza de nuevo
        LineNumberReader lnr = new LineNumberReader(new FileReader(fileUA));
        String linea = lnr.readLine();
        lnr.close();

        long ms = Long.parseLong(linea);
        long ahora = System.currentTimeMillis();

        if (!fileCont.exists()){
            fileCont.createNewFile();
            guardarContador(0);
            _cont = 0;
        }

        //actualizo archivo de último acceso
        FileWriter fw = new FileWriter(fileUA);
        fw.write(Long.toString(System.currentTimeMillis()));
        fw.close();

        //si pasaron 3 horas...
        if ((ahora - ms) > (Hora.MS_EN_HORA * 3) ){
            _cont = 0;
            return;
        }

        //...si no, levanto el contador
        lnr = new LineNumberReader(new FileReader(fileCont));
        linea = lnr.readLine();
        _cont = Integer.parseInt(linea);
        lnr.close();
    }

    static void guardarContador(int c) throws Exception{
        File file = new File(ARCHIVO_CONT);
        FileWriter fw = new FileWriter(file);
        fw.write(Integer.toString(c));
        fw.close();
    }

    static void manejarExcepcion(Exception e) throws Exception{
        e.printStackTrace();
        PrintStream ps = new PrintStream(
                new FileOutputStream(new File(ARCHIVO_LOG).getAbsolutePath(), true));
        e.printStackTrace(ps);
        ps.close();
        guardarLog(e.getMessage());
    }

    static void matar(Process p)throws Exception{
        guardarLog("matado  : " + new Date() + NUEVA_LINEA);
        p.destroy();
    }

    static void guardarLog(String c) throws Exception{
        File file = new File(ARCHIVO_LOG);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), true);
        c += NUEVA_LINEA;
        fos.write(c.getBytes());
        fos.close();
    }
}
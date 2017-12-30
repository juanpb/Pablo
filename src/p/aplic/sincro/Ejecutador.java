package p.aplic.sincro;

import org.apache.log4j.Logger;
import p.util.Fechas;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: Dec 2, 2008
 * Time: 6:30:15 AM
 */
public class Ejecutador {
    private static final Logger logger = Logger.getLogger(Ejecutador.class);

    private static String directorioBackup;
    private static Map<String, String> archUltCopia;
    private static String archCopias;

    public static boolean ejecutar(p.aplic.sincro.Accion a, Log log) throws Exception {
        boolean res = false;
        String e = a.getAccion();
        if ("c".equals(e)){
            res = sePuedeCopiar(a.getParam3());
            if (res)
                res = copiar(a, log);
            else{
                log.addNoCopiado("Versión desactualizada: " + a.getParam1());
            }
        }
        else if ("m".equals(e)){
            res = sePuedeCopiar(a.getParam3());
            if (res)
                res = mover(a.getParam2(), a.getParam1(), log);
            else{
                log.addNoMovido("Versión desactualizada: " + a.getParam1());
            }
        }
        else if ("mc".equals(e)){
            res = sePuedeCopiar(a.getParam3());
            if (res)
                res = moverContenido(a.getParam2(), a.getParam1(), log);
            else{
                log.addContenidoNoMovido("Versión desactualizada: " + a.getParam1());
            }
        }
        else if ("a".equals(e))
            res = abrir(a, log);

        return res;
    }


    private static boolean abrir(p.aplic.sincro.Accion a, Log log){
        boolean res = true;
        String exe = a.getParam1();
        String s = a.getParam2();
        File or = new File(s);
        if (or.exists()){
            boolean abrir = false;
            if (or.isDirectory()){
                File[] files = or.listFiles();
                //Abro el directorio solo si tiene algo adentro
                for(File f : files) {
                    abrir |= f.length() > 0 || f.isDirectory();
                }
                if (!abrir){
                    log.addNoAbierto("El dir '" + s + "' está vacío.");
                    res = false;
                }
            }else{
                //Abro el archivo solo si su tamaño es > 0
                abrir = (or.length() > 0);
                if (!abrir){
                    log.addNoAbierto("El arch '" + s + "' porque está vacío.");
                    res = false;
                }
            }
            if (abrir){
                exe +=  " " + or.getAbsolutePath();
                try {
                    Runtime.getRuntime().exec(exe);
                    log.addAbierto("Se ejecutó: '" + exe + "'");
                    res = true;
                } catch (IOException e) {
                    res = false;
                    log.addNoAbierto("Falló la  Ejecución de '" + exe + "'. " +
                            "Mensaje: " + e.getMessage());
                    logger.error(e.getMessage(), e);
                }
            }
        }
        else{
            log.addNoAbierto("'" + s + "' porque no se encuentra.");
            res = false;
        }

        return res;
    }

    private static boolean sePuedeCopiar(String arch) throws Exception {
        if (arch == null)
            return true;
        File or = new File(arch);
        return !or.exists();
    }

    private static boolean copiar(p.aplic.sincro.Accion a, Log log) throws Exception {
        String dest = a.getParam2();
        String n = a.getParam1();
        File or = new File(n);
        boolean res = true;
        if (or.exists()){
            File destF = new File(dest, or.getName());
            if (or.isFile()){
                if (!noCambioDesdeLaUltimaCopia(or)) {
                    if (destF.exists())
                        backup(destF);

                    UtilFile.copiar(or, destF);
                    log.addCopiado( or.getAbsolutePath() + " -> " + destF.getAbsolutePath());
                    res = true;
                    agregarAArchivoUltCopia(or);
                }
                else{
                    log.addNoCopiado("'" + or.getAbsolutePath()
                            + "' porque no se modificó desde la última vez que se copió.");
                    res = false;
                }
            }
            else if (or.isDirectory()){
                UtilFile.copiarDir(or, destF);
                res = true;
                log.addCopiado(" '" + or.getAbsolutePath() + "' -> '" + destF.getAbsolutePath() + "'");
            }
        }
        else{
            log.addNoCopiado("'" + or.getAbsolutePath() + "' no se encuentra");
            res = false;
        }
        return res;
    }

    private static boolean noCambioDesdeLaUltimaCopia(File archivo) throws Exception {
        if (archCopias != null ) {
            String s = archUltCopia.get(archivo.getAbsolutePath());
            if (s != null){
                return s.equals(archivo.lastModified()+"");
            }
        }
        return false;
    }

    private static boolean moverContenido(String destino, String archOrigen, Log log) throws Exception {
        //prec: archOrigen es un directorio
        boolean res = true;
        File or = new File(archOrigen);
        if (or.exists()){
            if (or.isFile()){
                res = false;
                log.addError("mc se usa para mover directorios. " + or.getAbsolutePath() +
                    " es un archivo.");
            }
            else if (or.isDirectory()){
                File[] files = or.listFiles();
                if (files.length == 0){
                    log.addNoMovido("'" + archOrigen + "' porque está vacío");
                    res = false;
                }
                for(File s : files) {
                    res &= mover(destino, s.getAbsolutePath(), log);
                }
            }
        }
        else{
            log.addNoMovido("'" + or.getAbsolutePath() + "' no existe el origen");
            res = false;
        }
        return res;
    }


    private static boolean mover(String destino, String origen, Log log) throws Exception {
        File or = new File(origen);
        boolean res = true;
        if (or.exists()){
            if (or.isFile()){
                File destFile = new File(destino, or.getName());
                if (destFile.exists())
                    backup(destFile);

                UtilFile.copiar(or, destFile);
                agregarAArchivoUltCopia(destFile);
                log.addMovido("'" + or.getAbsolutePath() + " -> " + destFile.getAbsolutePath());
                res = true;
            }
            else if (or.isDirectory()){
                File archOrigen = new File(origen);
                UtilFile.moverContenidoDeDir(archOrigen, new File(destino, archOrigen.getName()));
                log.addMovido("'" + origen + "' -> '" + destino + "'");
                res = true;
            }

            boolean borrado = UtilFile.deleteAll(or);
            if (!borrado)
                log.addError("No se pudo borrar: '" + or.getAbsolutePath() + "'");
        }
        else{
            log.addNoMovido("'" + origen + "' no se encuentra.");
            res = false;
        }

        return res;
    }

    private static void backup(File f) throws Exception {
        if (directorioBackup == null){
            System.out.println("No se definió el dir de backup");
            throw new RuntimeException("No se definió el dir de backup");
        }
        String sufijo = Fechas.getAAAAMMDDHHMMSS("_");
        int punto = f.getName().lastIndexOf(".");
        String n = f.getName().substring(0, punto) + "_" +
                sufijo + f.getName().substring(punto);
        File d = new File(directorioBackup, n);
        UtilFile.copiar(f,d);
    }

    public static void setDirectorioBackup(String db) {
        directorioBackup = db;
    }

    public static void agregarAArchivoUltCopia(File f) {
        if (archCopias != null){
            long lm = f.lastModified();
            archUltCopia.put(f.getAbsolutePath(),
                    lm + "|" + new Date(lm));//piso el valor viejo

            List<String> nuevos = new ArrayList<String>();
            for(String s : archUltCopia.keySet()) {
                nuevos.add(s + "=" + archUltCopia.get(s));
            }
            try {
                UtilFile.guardartArchivoPorLinea(new File(archCopias), nuevos, true);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public static void setArchivoCopias(String ac) {
        archCopias = ac;
        archUltCopia = new HashMap<String, String>();
        if (ac == null)
            return ;
        try {
            List<String> stringList = UtilFile.getArchivoPorLinea(ac);
            for(String s : stringList) {
                int i = s.indexOf("=");
                if (i > 0){
                    String arch = s.substring(0, i).trim();
                    String fecha = s.substring(1+i).trim();
                    int j = fecha.indexOf("|");
                    if (j >0)
                        fecha = fecha.substring(0, j).trim();
                    archUltCopia.put(arch,  fecha);
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }
}

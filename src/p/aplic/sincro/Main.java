package p.aplic.sincro;

import p.util.UtilFile;
import p.util.UtilString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: JPB
 * Date: Dec 1, 2008
 * Time: 10:29:12 AM
 */
public class Main {
    private static String directorioBackup;
    private static String archivoCopias;
    private static Map<String, String> vars = new HashMap<String, String>();

    //todo: disp. proc. java; SVN
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            System.out.println("Falta archivo de entrada");
            System.exit(0);
        }
        List<p.aplic.sincro.Accion> list = parsear(args[0]);
        Log log = ejecutar(list);
        mostrar(log);
        System.out.println("Listo");
    }

    private static void mostrar(Log log) {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        log.mostrar();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static Log ejecutar(List<p.aplic.sincro.Accion> s) throws Exception {
        Ejecutador.setDirectorioBackup(directorioBackup);
        Ejecutador.setArchivoCopias(archivoCopias);
        Log log = new Log();
        log.addInfo("Dir. bachup: " + directorioBackup);
        log.addInfo("Archivo Copias: " + archivoCopias);

        for(Accion a : s) {
            Ejecutador.ejecutar(a, log);
        }
        return log;
    }

    private static void parsearParametro(String s){
        if (s.toLowerCase().startsWith("-b")){
            int d = s.indexOf("\"");
            int h = s.indexOf("\"", d+1);
            directorioBackup = reemplazarClaves(s.substring(d+1, h));
        }
        else if (s.toLowerCase().startsWith("-x")){
            int d = s.indexOf("\"");
            int h = s.indexOf("\"", d+1);
            archivoCopias = reemplazarClaves(s.substring(d+1, h));
        }
        else{
            if (s.trim().length() > 0 && !s.startsWith("-") && !s.startsWith("/")) {
                System.out.println("Parámetro no reconocido. Línea = '" + s + "'");
            }
        }
    }

    private static List<Accion> parsear(String arg) throws IOException {
        List<String> ent = UtilFile.getArchivoPorLinea(arg);

        List<Accion> res = new ArrayList<Accion>();

        for(String s : ent) {
            Accion a = Accion.crearAccion(s);
            if (a != null)
                res.add(a);
            else if (s.toLowerCase().startsWith("set")){
                //ej: set clave=valor
                //saco set y espacios
                s = s.substring(3).trim();
                int d = s.indexOf("=");
                String c = s.substring(0, d).trim();
                String v = reemplazarClaves(s.substring(d+1).trim());
                vars.put(c, v);
            }
            else
                parsearParametro(s);
        }
        reemplazarClaves(res);
        return res;
    }


    private static String reemplazarClaves(String entrada ){
        if (entrada != null && !entrada.trim().equals("")){
            for(String s : vars.keySet()) {
                String k = "%" + s + "%";
                if (entrada.contains(k)){
                    String replacement = vars.get(s);
                    entrada = UtilString.reemplazarTodo(entrada, k, replacement);
                }
            }
        }
        return entrada;
    }

    private static void reemplazarClaves(List<Accion> accs){
        for(Accion a : accs) {
            a.setParam1(reemplazarClaves(a.getParam1()) );
            a.setParam2(reemplazarClaves(a.getParam2()) );
            a.setParam3(reemplazarClaves(a.getParam3()) );
        }
    }
}

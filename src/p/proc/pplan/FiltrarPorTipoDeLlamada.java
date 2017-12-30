package p.proc.pplan;

import p.util.Util;
import p.util.UtilFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 22/04/13
 * Time: 13:53
 */
public class FiltrarPorTipoDeLlamada {

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        if (args.length != 1 ){
            System.out.println("Modo de uso: java Filtrar archIni");
            System.exit(-1);
        }
        Util.loadProperties(args[0]);
        final String archEntrada = System.getProperty("ARCHIVO_ENTRADA");
        System.out.println(archEntrada);
        final List<String> arch = UtilFile.getArchivoPorLinea(archEntrada);
        List<String> sal;

        String archSalida = archEntrada + "_" ;
        System.out.println("Archivos de salida: " + archSalida + "[TIPO]");

        sal = filtrarPorTipo(Reg.TIPO_SALIENTE, arch);
        UtilFile.guardartArchivoPorLinea(archSalida + Reg.TIPO_SALIENTE, sal);

        sal = filtrarPorTipo(Reg.TIPO_TRANSFERENCIA, arch);
        UtilFile.guardartArchivoPorLinea(archSalida + Reg.TIPO_TRANSFERENCIA, sal);

        sal = filtrarPorTipo(Reg.TIPO_ENTRANTE, arch);
        UtilFile.guardartArchivoPorLinea(archSalida + Reg.TIPO_ENTRANTE, sal);


        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }


    public static List<String> filtrarPorTipo(String tipo, List<String> imed){
        List<String> res = new ArrayList<String>();

        for (String s : imed) {
            if (s.substring(RegPP.TIPO_DESDE, RegPP.TIPO_HASTA).equals(tipo))
            res.add(s);
        }
        return res;
    }

}

package p.proc.pplan;

import p.util.Util;
import p.util.UtilFile;
import p.util.UtilList;
import p.util.UtilString;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: JPB
 * Date: 22/04/13
 * Time: 13:53
 */
public class FiltrarEnPosicion {

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        if (args.length != 1 ){
            System.out.println("Modo de uso: java FiltrarEnPosicion archIni");
            System.exit(-1);
        }
        Util.loadProperties(args[0]);
        final String archEntrada = System.getProperty("ARCHIVO_ENTRADA");
        System.out.println(archEntrada);

        String d = System.getProperty("FEP_DESDE");
        String h = System.getProperty("FEP_HASTA");
        String v = System.getProperty("FEP_VALOR");

        int desde = 0, hasta = 0;
        try {
            desde = Integer.parseInt(d);
            hasta = Integer.parseInt(h);
        } catch (Exception ex) {
            System.out.println("Mal los valores desde y hasta: " + d + "," + h);
            System.exit(-1);
        }


        final List<String> arch = UtilFile.getArchivoPorLinea(archEntrada);
        List<String> sal;

        String archSalida = archEntrada + "_" + UtilString.reemplazarTodo(v, "*", "_");
        System.out.println("Archivos de salida: " + archSalida );
        final File fileSal = new File(archSalida);
        if (fileSal.exists()){
            System.out.println("Ya existe el archvo de salida. No se hace nada");
            return ;
        }

        sal = UtilList.filtrarPorValor(arch, desde, hasta, v);
        UtilFile.guardartArchivoPorLinea(fileSal, sal);


        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }




}

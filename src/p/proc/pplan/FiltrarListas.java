package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Util;
import p.util.UtilFile;
import p.util.UtilList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 22/04/13
 * Time: 13:53
 *
 * Dadas dos listas, elimina de la primera todos los registros que estén en la segunda y genera un archivo de salida
 * [ARCHIVO_ENTRADA_1]_eliminado = {[ARCHIVO_ENTRADA_1]} {[ARCHIVO_ENTRADA_2]}
 */
public class FiltrarListas implements Aplicacion{
    private static final Logger logger = Logger.getLogger(FiltrarListas.class);
    private static final String NOMBRE = "Filtrar listas";

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        if (args.length != 1 ){
            System.out.println("Modo de uso: java Filtrar archIni");
            System.exit(-1);
        }
        Util.loadProperties(args[0]);
        final String archEntrada1 = System.getProperty("ARCHIVO_ENTRADA_1");
        System.out.println("archEntrada1=" + archEntrada1);
        final String archEntrada2 = System.getProperty("ARCHIVO_ENTRADA_2");
        System.out.println("archEntrada2=" + archEntrada2);
        final int validarHasta = Integer.parseInt(System.getProperty("FL_VALIDAR_HASTA")) ;
        System.out.println("validarHasta=" + validarHasta);

        filtrar(archEntrada1,  archEntrada2, validarHasta);

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");
    }

    private static String filtrar(String archEntrada1, String archEntrada2, int validarHasta){
        long ini = System.currentTimeMillis();


        try {
            final List<String> arch1 = UtilFile.getArchivoPorLinea(archEntrada1);
            final List<String> arch2 = UtilFile.getArchivoPorLinea(archEntrada2);
            List<String> sal;

            UtilList.eliminar(archEntrada1, archEntrada2, validarHasta, false);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "Error al filtrar: " + e.getMessage();
        }

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        return ("Demoró " + seg + " segundos. Se grabó en el archivo: " + archEntrada1 + "_eliminado");
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<String> vp = new ArrayList<String>();
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo 1");
        res.add(p1);

        final Param p2 = new Param("Archivo 2");
        res.add(p2);
        final Param p3 = new Param("Validar hasta", "Valida hasta esta posición. Si se pone 0 valida toda la línea");
        res.add(p3);


        return res;
    }

    @Override
    public String ejecutar(List<String> params) {
        final String s = params.get(2);
        int i = Integer.parseInt(s);
        return filtrar(params.get(0),params.get(1), i);
    }
}

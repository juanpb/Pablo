package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 */
public class Partir implements Aplicacion {
    private static final Logger logger = Logger.getLogger(Partir.class);

    private static final String NOMBRE = "Partir";

    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();

        String archEntrada = params.get(0);
        int cantMax     = Integer. parseInt(params.get(1));

        final List<String> archivoPorLinea;
        final List<String> archivoPorLineaSalida = new ArrayList<String>();

        try {
            archivoPorLinea = UtilFile.getArchivoPorLinea(archEntrada);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al leer archivo de entrada: ").append(e.getMessage());
            return res.toString();
        }
        int cont = 1;
        List<String> sal = null;

        try {
            for (String s : archivoPorLinea) {
                archivoPorLineaSalida.add(s);
                if (archivoPorLineaSalida.size() == cantMax){
//                    String archSalida = "IMED-" + llenar(cont++) + ".20130506235854.TODOXX_60096816_PPLAN";
                    String archSalida = archEntrada + "_P_" + (cont++);
                    UtilFile.guardartArchivoPorLinea(archSalida, archivoPorLineaSalida);
                    archivoPorLineaSalida.clear();
                }
            }
            if (!archivoPorLineaSalida.isEmpty()){
                File archSalida = new File (archEntrada + "_P_" + (cont++));
                UtilFile.guardartArchivoPorLinea(archSalida, archivoPorLineaSalida);
                archivoPorLineaSalida.clear();
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al generar el archivo de salida: ").append(e.getMessage());
            return res.toString();
        }


        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Arch. de entrada: " + archEntrada).append(Constantes.NUEVA_LINEA);
        res.append("Demoró " + ms + " ms");

        return res.toString();
    }

    private String llenar (int x){
        String sal = x+"";
        while(sal.length() < 4)
            sal = "0" + sal;

        return sal;
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo");
        res.add(p1);
        final Param p2 = new Param("Cant Máx", "Cantidad máx. de líneas de los archivos de salida");
        res.add(p2);

        return res;
    }

}

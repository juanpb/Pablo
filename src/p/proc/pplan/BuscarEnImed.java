package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.Util;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 Dada una línea de pplan, y un directorio con imeds, devuelve el nombre del imed que lo contiene
 */
public class BuscarEnImed implements Aplicacion {
    private static final Logger logger = Logger.getLogger(BuscarEnImed.class);
    private static final String NOMBRE = "BuscarEnImed";

    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();

        StringBuilder res = new StringBuilder();

        File archDirEntrada = new File(params.get(0));
        File archBuscando = null;
        String lineaPP      = params.get(1);

        final List<String> archivoPorLinea;
        int nroLinea = -1;

        try {
            if (archDirEntrada.isFile()){
                archBuscando = archDirEntrada;
                nroLinea = buscar(archDirEntrada, lineaPP);
            }
            else{
                final File[] files = archDirEntrada.listFiles();
                for (File file : files) {
                    try {
                        if (file.isFile()){
                            if (file.length() > 0){
                                nroLinea = buscar(file, lineaPP);
                            }
                        }
                        if (nroLinea >= 0){
                            archBuscando = file;
                            break;
                        }
                    } catch (RuntimeException e) {
                        System.out.println("Problemas en el archivo: ");
                        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            res.append("Problemas: " + e.getMessage());
            logger.error(e.getMessage(), e);
        }


        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Dir. de entrada: " + params.get(0)).append(Constantes.NUEVA_LINEA);
        final boolean b = nroLinea > -1;
        res.append("Encontrado = " + b).append(Constantes.NUEVA_LINEA);
        if(b){
            res.append("Archivo encontrado: " + archBuscando.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            res.append("Línea: " + nroLinea).append(Constantes.NUEVA_LINEA);
            Util.pegarEnElPortapapeles(archBuscando.getAbsolutePath());
        }
        res.append("Demoró " + ms + " ms");

        return res.toString();
    }

    //Devuelve -1 si no lo encuentra o el nro de línea
    private int buscar(File archImed, String lineaPP) throws IOException {
        boolean res = false;
        final List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(archImed);
        final Date horaLlamada = RegPP.getHoraLlamada(lineaPP);
        final String tipoLlamada = RegPP.getTipoLlamada(lineaPP);
        final String nroOrigen = RegPP.getNroOrigen(lineaPP);

        int cont = 1;
        for (String s : archivoPorLinea) {
            Date horaLlamadaI = RegImed.getHoraLlamada(s);
            String tipoLlamadaI = RegImed.getTipoLlamada(s);
            String nroOrigenI = RegImed.getNroOrigen(s);
            if (horaLlamada.equals(horaLlamadaI))
                if (tipoLlamada.equals(tipoLlamadaI))
                    if (nroOrigen.endsWith(nroOrigenI.trim())){
                        return cont;
                    }
            cont++;
        }
        return -1;
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Dir. o arch. Imed");
        res.add(p1);
        final Param p2 = new Param("Línea PPLAN", "línea de PP a buscar");
        res.add(p2);

        return res;
    }

}

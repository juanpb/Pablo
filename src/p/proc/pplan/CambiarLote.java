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
public class CambiarLote implements Aplicacion {
    private static final Logger logger = Logger.getLogger(CambiarLote.class);

    private static final String NOMBRE = "Cambiar NroLote";

    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();

        String archEntrada = params.get(0);
        String nroLote     = params.get(1);
        String tipo        = params.get(2);

        final List<String> archivoPorLinea;
        try {
            archivoPorLinea = UtilFile.getArchivoPorLinea(archEntrada);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al leer archivo de entrada: ").append(e.getMessage());
            return res.toString();
        }

        List<String> sal = null;
        String archSalida = archEntrada + "_" + nroLote;
        final File file = new File(archSalida);
        if (file.exists()){
            res.append("Ya existe el archivo de salida: " + archSalida);
            res.append("No se hace nada");
            return res.toString();
        }

        if (tipo.equals(Reg.TIPO_IMED)){
            sal = Tranformar.cambiarNroLoteImed(nroLote, archivoPorLinea);
        }
        else if (tipo.equals(Reg.TIPO_PP)){
            sal = Tranformar.cambiarNroLotePP(nroLote, archivoPorLinea);
        }
        else{
            System.out.println("No se indico el tipo de archivo. Valores posible: P,I. Valor recibido: " + tipo);
            System.exit(-1);
        }

        try {
            UtilFile.guardartArchivoPorLinea(archSalida, sal);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al generar el archivo de salida: ").append(e.getMessage());
            return res.toString();
        }


        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Arch. de entrada: " + archEntrada).append(Constantes.NUEVA_LINEA);
        res.append("Se cambió con el nro de lote: " + nroLote).append(Constantes.NUEVA_LINEA);
        res.append("Salida: " + archSalida).append(Constantes.NUEVA_LINEA);
        res.append("Demoró " + ms + " ms");

        return res.toString();
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<String> vp = new ArrayList<String>();
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo");
        res.add(p1);
        final Param p2 = new Param("Nro de lote");
        res.add(p2);

        vp.add(Reg.TIPO_IMED);
        vp.add(Reg.TIPO_PP);
        final Param p3 = new Param(vp, "Tipo");
        res.add(p3);


        return res;
    }

}

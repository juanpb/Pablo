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
 * Date: 22/04/13
 * Time: 13:53
 *
 * Dadas dos listas, genera 3 nuevas, una con las iguales (según criterio) y las otras 2 con las distintas.
 */
public class BuscarIgualesListas implements Aplicacion{
    private static final Logger logger = Logger.getLogger(BuscarIgualesListas.class);
    private static final String NOMBRE = "Buscar Iguales Listas";
    private static final String COMPARAR_NRO_DESTINO = "Nro Destino";
    private static final String COMPARAR_TODO = "Todo";

    private static String filtrar(String archEntrada1, String archEntrada2, String tipo, String compararPor){
        long ini = System.currentTimeMillis();

        File archSalIguales = new File(archEntrada1 + "_iguales");
        File archSalIzq = new File(archEntrada1 + "_izq");
        File archSalDer = new File(archEntrada1 + "_der");
        if (archSalIguales.exists())
            throw new RuntimeException("Existe archSal: " + archSalIguales + ". No se hace nada");
        if (archSalIzq.exists())
            throw new RuntimeException("Existe archSal: " + archSalIzq + ". No se hace nada");
        if (archSalDer.exists())
            throw new RuntimeException("Existe archSal: " + archSalDer + ". No se hace nada");


        try {
            final List<String> archIzq = UtilFile.getArchivoPorLinea(archEntrada1);
            final List<String> archDer = UtilFile.getArchivoPorLinea(archEntrada2);
            List<String> salIg = new ArrayList<String>();
            List<String> salDiIzq = new ArrayList<String>();
            List<String> salDiDer = new ArrayList<String>();

            final int cantMin = Math.min(archIzq.size(), archDer.size());
            for (int i = 0; i < cantMin; i++) {
                String izq = archIzq.get(i);
                String der = archDer.get(i);
                if (sonIguales(izq, der, tipo, compararPor)){
                    salIg.add(izq);
                }else{
                    salDiIzq.add(izq);
                    salDiDer.add(der);
                }
            }

            //agrego los que sobraron a la izq
            for (int i = cantMin; i < archIzq.size(); i++) {
                String izq = archIzq.get(i);
                salIg.add(izq);
            }

            //agrego los que sobraron a la der
            for (int i = cantMin; i < archDer.size(); i++) {
                String d = archDer.get(i);
                salIg.add(d);
            }

            //grabo salida
            UtilFile.guardartArchivoPorLinea(archSalIguales, salIg);
            UtilFile.guardartArchivoPorLinea(archSalIzq, salDiIzq);
            UtilFile.guardartArchivoPorLinea(archSalDer, salDiDer);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "Error al filtrar: " + e.getMessage();
        }


        long fin = System.currentTimeMillis();
        long ms = (fin - ini);///1000;
        String s = "Demoró " + ms + " ms. " + Constantes.NUEVA_LINEA +
                "Se grabó en los archivos: " + Constantes.NUEVA_LINEA +
                        archSalIguales + Constantes.NUEVA_LINEA +
                        archSalIzq + Constantes.NUEVA_LINEA +
                        archSalDer + Constantes.NUEVA_LINEA;
        return s;
    }

    private static boolean sonIguales(String izq, String der, String tipo, String compararPor) {
        if (COMPARAR_TODO.equals(compararPor))
            return izq.equals(der);
        else if (COMPARAR_NRO_DESTINO.equals(compararPor)){
            if (Reg.TIPO_PP.equals(tipo)){
                izq = izq.substring(RegPP.NUMERO_DEST_DESDE, RegPP.NUMERO_DEST_HASTA);
                der = der.substring(RegPP.NUMERO_DEST_DESDE, RegPP.NUMERO_DEST_HASTA);
                return izq.equals(der);
            }
        }

        throw new RuntimeException("Tipo/compararPor no implementado, tipo: " + tipo + ", compararPor: " + compararPor);
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<String> vp = new ArrayList<String>();
        List<String> vp2 = new ArrayList<String>();
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo 1");
        res.add(p1);

        final Param p2 = new Param("Archivo 2");
        res.add(p2);

//        vp.add(Reg.TIPO_IMED);
        vp.add(Reg.TIPO_PP);
        final Param p3 = new Param(vp, "Tipo");
        res.add(p3);

        vp2.add(COMPARAR_NRO_DESTINO);
        vp2.add(COMPARAR_TODO);
        final Param p4 = new Param(vp2, "Comparar por");

        res.add(p4);


        return res;
    }

    @Override
    public String ejecutar(List<String> params) {
        return filtrar(params.get(0),params.get(1), params.get(2), params.get(3));
    }
}

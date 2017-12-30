package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 * Busca concidencias entre un imed y un pplan
 */
public class Mapear implements Aplicacion{
    private static final Logger logger = Logger.getLogger(Mapear.class);
    private List<String> imedCoinc = new ArrayList<String>();
    private List<String> ppCoinc = new ArrayList<String>();
    private List<String> imedNoCoinc = new ArrayList<String>();
    private List<String> ppNoCoinc = new ArrayList<String>();
//    private List<String> ppCoincMal = new ArrayList<String>();
//    private List<String> imedCoincMal = new ArrayList<String>();
//    private static List<String> msgCoincMal = new ArrayList<String>();

    private static final String posicionesPP = "2, 12, 14, 28, 29, 36, 40, 41, 53, 59, 65, 73, 77, 82, 87, 94, 101, 102, 103, 107, 113, 119, 129, 130, 133, 138, 140, 142, 146, 150";
    private static final String posicionesImed = "3,11,19,25,40,41,49,57,65,71,74,75,103,118,119,147,153,159,166,173,180,190,192,212,232,233,234,235,237,239,240,241,243,246,253,260,261,262,263,282,286,290,294,298,299,300,301,302";


    /*
    Se asume que los archivos están ordenados
    de menor a mayor, por fecha, luego nroOrigen, luego por tipo, nroDestino, duración Aire (chargeable+connection)
    */

    private String mapear(String archivoImed, String archivoPP) throws IOException {
        long ini = System.currentTimeMillis();

        imedCoinc.clear();
        ppCoinc.clear();
        imedNoCoinc.clear();
        ppNoCoinc.clear();
//        ppCoincMal.clear();
//        imedCoincMal.clear();
//        msgCoincMal.clear();

        final List<String> imed = UtilFile.getArchivoPorLinea(archivoImed);
        final List<String> pp = UtilFile.getArchivoPorLinea(archivoPP);
        int indImed = 0;
        int indPP = 0;
        RegImed ri = new RegImed(imed.get(indImed++));
        RegPP rp = new RegPP(pp.get(indPP++));
        boolean seguir = true;
        while (seguir){

            if (coinciden(ri, rp)){
                imedCoinc.add(ri.getLinea());
                ppCoinc.add(rp.getLinea());
                indImed++;
                indPP++;
//                if (!coincidenTodosLosCampos(ri, rp)){
//                    imedCoincMal.add(ri.getLinea());
//                    ppCoincMal.add(rp.getLinea());
//                }
            }
            else if (ri.compareTo(rp) < 0){
                //Avanza registro imed
                indImed++;
                imedNoCoinc.add(ri.getLinea()) ;
            }
            else{       //no pueden ser iguales porque no 'coinciden'
                //Avanza registro PP
                indPP++;
                ppNoCoinc.add(rp.getLinea()) ;
            }

            if (indImed >= imed.size()){
                seguir = false;
                //guardo el registro actual
                //si no coincidía...
                if (!coinciden(ri, rp)){
                    ppNoCoinc.add(rp.getLinea());
                    indPP++;
                }
                //guardo el resto de los registros.
                for (int i = indPP; i < pp.size(); i++)
                    ppNoCoinc.add(pp.get(i));
            }
            else
                ri = new RegImed (imed.get((indImed)));//si no avanzó el índice => queda igual

            if (indPP >= pp.size()){
                seguir = false;
                //guardo el registro actual
                //si no coincidía...
                if (!coinciden(ri, rp)){
                    imedNoCoinc.add(ri.getLinea());
                    indImed++;
                }

                //guardo el resto de los registros.
                for (int i = indImed; i < imed.size(); i++)
                    imedNoCoinc.add(imed.get(i));
            }
            else
                rp = new RegPP (pp.get((indPP)));
        }

        guardar((new File(archivoImed).getParent()));

        long fin = System.currentTimeMillis();
        long ms = (fin - ini);
        return "Demoró " + ms + " segundos";

    }

    private void guardar(String dirSalida) throws IOException {
        guardarRegistros(dirSalida);
//        guardarRegistrosCSV(dirSalida);
    }


    private void guardarRegistros(String dirSalida) throws IOException {
        File dir = new File(dirSalida);
        UtilFile.guardartArchivoPorLinea(new File(dir, "imedCoinc"), imedCoinc);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoinc"), ppCoinc);

        UtilFile.guardartArchivoPorLinea(new File(dir, "imedNoCoinc"), imedNoCoinc);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppNoCoinc"), ppNoCoinc);

//        UtilFile.guardartArchivoPorLinea(new File(dir, "imedCoincMal"), imedCoincMal);
//        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoincMal"), ppCoincMal);
//        UtilFile.guardartArchivoPorLinea(new File(dir, "msgCoincMal"), msgCoincMal);
    }


    private void guardarRegistrosCSV(String dirSalida) throws IOException {
        File dir = new File(dirSalida);
        final List<String> cabeceraImed = PasarAExcel.getCabeceraImed();
        final List<String> cabeceraPP = PasarAExcel.getCabeceraPP();
        UtilFile.guardartArchivoPorLinea(new File(dir, "imedCoinc.csv"), cabeceraImed);
        UtilFile.guardartArchivoPorLinea(new File(dir, "imedCoinc.csv"), CSV(imedCoinc, posicionesImed));
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoinc.csv"), cabeceraPP);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoinc.csv"), CSV(ppCoinc, posicionesPP));

        UtilFile.guardartArchivoPorLinea(new File(dir, "imedNoCoinc.csv"), cabeceraImed);
        UtilFile.guardartArchivoPorLinea(new File(dir, "imedNoCoinc.csv"), CSV(imedNoCoinc, posicionesImed));
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppNoCoinc.csv"), cabeceraPP);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppNoCoinc.csv"), CSV(ppNoCoinc, posicionesPP));

//        UtilFile.guardartArchivoPorLinea(new File(dir, "imedCoincMal.csv"), cabeceraImed);
//        UtilFile.guardartArchivoPorLinea(new File(dir, "imedCoincMal.csv"), CSV(imedCoincMal, posicionesImed));
//        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoincMal.csv"), cabeceraPP);
//        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoincMal.csv"), CSV(ppCoincMal, posicionesPP));
    }

    private List<String> CSV(List<String> lineas, String posicionesSinParsear) {
        List<String> res = new ArrayList<String>(lineas.size());

        final List<Integer> posiciones = parsearPosiciones(posicionesSinParsear);

        for (String s : lineas) {
            String s1 = agregarComillasYPYC(posiciones, s);
            res.add(s1);
        }
        return res;
    }

    private List<Integer> parsearPosiciones(String posiciones){
        StringTokenizer st = new StringTokenizer(posiciones, ",");
        List<Integer> indices = new ArrayList<Integer>();
        while (st.hasMoreElements()){
            String s = st.nextToken();
            Integer i = Integer.parseInt(s.trim());
            indices.add(i);
        }
        return indices;
    }

    //agrega comillas dobles, comillas simples al principio y ;
    static private String agregarComillasYPYC(List<Integer> indices, String entrada) {
        StringBuilder sb = new StringBuilder();
        int posDesde = 0;
        for (final Integer posHasta : indices) {
            final String c = entrada.substring(posDesde, posHasta);
            posDesde = posHasta;
            sb.append("\"'").append(c).append("\";");
        }
        return sb.toString();
    }

    /*
    Coindicen si tienen la misma fecha, hora, nroOrigen y tipo
     */
    public static boolean coinciden(RegImed ri, RegPP rp){
        if (ri.getFecha().equals(rp.getFecha()))
            if (ri.getNumOrigen().equals(rp.getNumOrigen()))
                if (ri.getTipo().equals(rp.getTipo())) {
                    final String ndI = ri.getNumDestino();
                    final String ndR = rp.getNumDestino();
                    return ndI.endsWith(ndR); //comparo sin sufijo
                }

        return false;
    }

    /**
     * Devuelve true si coindicen algunos campos conocidos (expluyendo los que se validan en 'coinciden'), estos son:
     * duraciónAire  = ChargeableDuration+ ConnectionTime
     * duraciónTierra = ChargeableDuration
     */
    public static boolean coincidenTodosLosCamposXXX(RegImed ri, RegPP rp){
        boolean res = true;
        String msj = "";

        String v1 = ri.getNumDestino();
        String v2 = rp.getNumDestino();
        String v1SP = sacarPrefijos(v1);

        if(!v1SP.endsWith(v2)){
            msj += "mal nro destino, " + v1 + " != " + v2 + "|";
            res = false;
        }

        final Long dCH = ri.getDuracion1();
        final Long dC = ri.getDuracion2();
        final Long dA = rp.getDuracion1();
        final Long dT = rp.getDuracion2();

        if ((dCH + dC) != dA){
            msj += "mal duración1, dCH+dC: " + (dCH+ dC) + " != " + dA + "(dCH: "+ dCH + ", dC: " + dC + ")|";
            res = false;
        }

        if (!dT.equals(dCH)){
            msj += "mal duración2, dt: " + dT + " != " + dCH + "|";
            res = false;
        }
//
//        if (!res )
//            msgCoincMal.add(msj);

        return res;
    }

    private static String sacarPrefijos(String num){
        //todo mal implementado
        if (num.startsWith("00")){
            num = num.substring(2);
        }
        if (num.startsWith("0"))
            num = num.substring(1);

        return num;
    }

    @Override
    public String getName() {
        return "Mapear";
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo IMED");
        res.add(p1);
        final Param p2 = new Param("Archivo PP");
        res.add(p2);

        return res;
    }

    @Override
    public String ejecutar(List<String> params) {
        try {
            return mapear(params.get(0), params.get(1));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}

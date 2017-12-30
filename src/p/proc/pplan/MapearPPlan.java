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
 * * Busca concidencias entre dos pplan
 */
public class MapearPPlan implements Aplicacion{
    private static final Logger logger = Logger.getLogger(MapearPPlan.class);
    private List<String> ppCoincIzq = new ArrayList<String>();
    private List<String> ppCoincDer = new ArrayList<String>();
    private List<String> ppNoCoincIzq = new ArrayList<String>();
    private List<String> ppNoCoincDer = new ArrayList<String>();

    private static final String posicionesPP = "2, 12, 14, 28, 29, 36, 40, 41, 53, 59, 65, 73, 77, 82, 87, 94, 101, 102, 103, 107, 113, 119, 129, 130, 133, 138, 140, 142, 146, 150";
    private String NOMBRE = "Mapear PPlan";

    /*
   Se asume que los archivos están ordenados por fecha, hora, nroOrigen, tipo, nroDestino
    */

    private String mapear(String archivoPPIzq, String archivoPPDer) throws IOException {
        long ini = System.currentTimeMillis();

        ppCoincDer.clear();
        ppCoincIzq.clear();
        ppNoCoincIzq.clear();
        ppNoCoincDer.clear();

        final List<String> listaIzq = UtilFile.getArchivoPorLinea(archivoPPIzq);
        final List<String> listaDer = UtilFile.getArchivoPorLinea(archivoPPDer);
        int indIzq = 0;
        int indDer = 0;
        RegPP regIzq = new RegPP(listaIzq.get(indIzq++));
        RegPP regDer = new RegPP(listaDer.get(indDer++));
        boolean seguir = true;
        while (seguir){

            if (coinciden(regIzq, regDer)){
                ppCoincIzq.add(regIzq.getLinea());
                ppCoincDer.add(regDer.getLinea());
                indIzq++;
                indDer++;
            }
            else if (regIzq.compareTo(regDer) < 0){ //no pueden ser iguales porque no 'coinciden'
                //Avanza registro izq
                indIzq++;
                ppNoCoincIzq.add(regIzq.getLinea()) ;
            }
            else{
                //Avanza registro der
                indDer++;
                ppNoCoincDer.add(regDer.getLinea()) ;
            }

            if (indIzq >= listaIzq.size()){
                seguir = false;
                //guardo el registro actual
                //si no coincidía...
                if (!coinciden(regIzq, regDer)){
                    ppNoCoincDer.add(regDer.getLinea());
                    indDer++;
                }
                //guardo el resto de los registros.
                for (int i = indDer; i < listaDer.size(); i++)
                    ppNoCoincDer.add(listaDer.get(i));
            }
            else
                regIzq = new RegPP (listaIzq.get((indIzq)));//si no avanzó el índice => queda igual

            if (indDer >= listaDer.size()){
                seguir = false;
                //guardo el registro actual
                //si no coincidía...
                if (!coinciden(regIzq, regDer)){
                    ppNoCoincIzq.add(regIzq.getLinea());
                    indIzq++;
                }

                //guardo el resto de los registros.
                for (int i = indIzq; i < listaIzq.size(); i++)
                    ppNoCoincIzq.add(listaIzq.get(i));
            }
            else
                regDer = new RegPP (listaDer.get((indDer)));
        }

        guardar((new File(archivoPPIzq).getParent()));

        long fin = System.currentTimeMillis();
        long ms = (fin - ini);
        return "Demoró " + ms + " ms";

    }

    private void guardar(String dirSalida) throws IOException {
        guardarRegistros(dirSalida);
    }


    private void guardarRegistros(String dirSalida) throws IOException {
        File dir = new File(dirSalida);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoincIzq"), ppCoincIzq);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppCoincDer"), ppCoincDer);

        UtilFile.guardartArchivoPorLinea(new File(dir, "ppNoCoincIzq"), ppNoCoincIzq);
        UtilFile.guardartArchivoPorLinea(new File(dir, "ppNoCoincDer"), ppNoCoincDer);
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
    public static boolean coinciden(Reg ri, Reg rp){
        if (ri.getFecha().equals(rp.getFecha()))
            if (ri.getNumOrigen().equals(rp.getNumOrigen()))
                if (ri.getTipo().equals(rp.getTipo()))
                    if (sacarPrefijos(ri.getNumDestino()) .equals(rp.getNumDestino()))
                        return true;

        return false;
    }

    private static String sacarPrefijos(String num){
        if (num.startsWith("00")){
            num = num.substring(2);
        }
        if (num.startsWith("0"))
            num = num.substring(1);

        return num;
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo PP izq");
        res.add(p1);
        final Param p2 = new Param("Archivo PP der");
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

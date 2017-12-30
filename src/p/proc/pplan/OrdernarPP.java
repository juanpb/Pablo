package p.proc.pplan;

import p.util.UtilFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: JPB
 * Date: 18/04/13
 * Time: 14:29
 */
public class OrdernarPP {

    private static final String nombreArch = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\pplan in\\pp_unidos";
    private static final String nombreArchSalida = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\pplan in\\pp_unidos_ord";

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();

        final List<String> apl = UtilFile.getArchivoPorLinea(nombreArch);
        ordenar(apl);
        UtilFile.guardartArchivoPorLinea(nombreArchSalida, apl);


        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    public static void ordenar(List dirs){
        Collections.sort(dirs, new ElCompaPP());
    }
}

class ElCompaPP implements Comparator {

    /*
    Compara de menor a mayor, por fecha, luego nroOrigen, luego por tipo (este campo lo ordeno al revés x el tema de anomalías),
    nroDestino, duración Aire (chargeable+connection)
     */
    public int compare(Object o1, Object o2) {
        String d1 = (String)o1;
        String d2 = (String)o2;

        Long v1 = getValorFecha(d1);
        Long v2 = getValorFecha(d2);


        int res = v1.compareTo(v2);//Comparo por fecha
        if (res == 0){
            v1 = getValorNumOrigen(d1);
            v2 = getValorNumOrigen(d2);
            res = v1.compareTo(v2); //Comparo por nroOrigen
            if (res == 0){
                String s1 = getValorTipo(d1);
                String s2 = getValorTipo(d2);
                //comparo al revés para que sea de mayor a menor (T,S,E)
                res = s2.compareTo(s1); //Comparo por tipoLLamada
                if (res == 0){
                    v1 = getValorNumDestino(d1);
                    v2 = getValorNumDestino(d2);
                    res = v1.compareTo(v2); //Comparo por nroDestino
                    if (res == 0){
                        v1 = getValorDuracAire(d1);
                        v2 = getValorDuracAire(d2);
                        res = v1.compareTo(v2); //Comparo por dur. aire
                        if (res == 0){
                            System.out.println("Repetidos:");
                            System.out.println(d1);
                            System.out.println(d2);
                        }
                    }
                }
            }
        }
        return res;
    }

    private Long getValorFecha(String s) {
        String x = s.substring(RegPP.FECHA_DESDE, RegPP.FECHA_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private Long getValorNumDestino(String s) {
        String x = s.substring(RegPP.NUMERO_DEST_DESDE, RegPP.NUMERO_DEST_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private Long getValorNumOrigen(String s) {
        String x = s.substring(RegPP.NUMERO_ORIGEN_DESDE, RegPP.NUMERO_ORIGEN_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private String getValorTipo(String s) {
        return s.substring(RegImed.TIPO_DESDE, RegImed.TIPO_HASTA);
    }

    private Long getValorDuracAire(String s) {
        String x = s.substring(RegPP.DURA_A_DESDE, RegPP.DURA_A_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private Long getValorDuracT(String s) {
        String x = s.substring(RegPP.DURA_T_DESDE, RegPP.DURA_T_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }
}

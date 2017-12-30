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
public class OrdernarImed {

    private static final String archAOrdenar = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\files_Divi\\imed_unidos";
    private static final String archOrdenado = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\files_Divi\\imed_unidos_ordenado";

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        final List<String> apl = UtilFile.getArchivoPorLinea(archAOrdenar);
        ordenar(apl);
        UtilFile.guardartArchivoPorLinea(archOrdenado, apl);


        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    public static void ordenar(List<String> dirs){
        Collections.sort(dirs, new ElCompaImed());
    }
}

class ElCompaImed implements Comparator {

    /*
    Compara de menor a mayor, por fecha, luego nroOrigen, luego por tipo (este campo lo ordeno al revés x el tema de anomalías)
    , nroDestino, duración Aire (chargeable+connection)
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
                        v1 = RegImed.getValorDuracionAireEnSegundos(d1);
                        v2 = RegImed.getValorDuracionAireEnSegundos(d2);
                        res = v1.compareTo(v2);     //Comparo por duración aire ('ChargeableDuration'+ 'ConnectionTime')
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
        String x = s.substring(RegImed.FECHA_DESDE, RegImed.FECHA_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private Long getValorNumOrigen(String s) {
        String x = s.substring(RegImed.NUMERO_ORIGEN_DESDE, RegImed.NUMERO_ORIGEN_HASTA);
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private Long getValorNumDestino(String s) {
        String x = s.substring(RegImed.NUMERO_DEST_DESDE, RegImed.NUMERO_DEST_HASTA);
        try {
            //le saco el prefijo para que quede ordenado igual que el de PPLAN
            final String nd = sacarPrefijos(x.trim());
            return Long.parseLong(nd);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    private static String sacarPrefijos(String num){
        if (num.startsWith("00")){
            num = num.substring(2);
        }
        if (num.startsWith("0"))
            num = num.substring(1);

        return num;
    }

    private String getValorTipo(String s) {
        return s.substring(RegImed.TIPO_DESDE, RegImed.TIPO_HASTA);
    }

//    private Long getValorDuracAire(String s) {
//        String x = s.substring(RegImed.CHARG_DUR_DESDE, RegImed.CHARG_DUR_HASTA);
//        try {
//            return Long.parseLong(x.trim());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return 0L;
//        }
//    }



}

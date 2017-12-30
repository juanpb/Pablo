package p.test;

import p.util.Constantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Administrador
 * Date: 27/01/2008
 * Time: 16:09:20
 */
public class MemAdmin {

     /**
     * Límite de objetos vivos que se pueden borrar
     * que se usa con el método de memoria = UMBRAL
     */
    private static int umbral;

     /**
     * Tabla de objetos vivos. No incluye los que están en 'aLiberar'
     * Por cada id guarda la cantidad de objetos asociados.
     */
    private static Map<String, Long> tabla = new HashMap<String, Long>();

    private static Map<String, List<String>> idsPorRegion =
            new HashMap<String, List<String>>();

    /**
     * Lista de ids de objetos que ya se pueden liberar
     */
    private static Map<String, Long> tablaALiberar = new HashMap<String, Long> ();

    /**
     * Contador de objetos creados. Incluye los vivos, los a liberar y los
     * liberados.
     */
    private static long contNews = 0;
    private static boolean usarUmbral = false;

    public static int getUmbral() {
        return umbral;
    }

    public static void setUmbral(int umbral) {
        MemAdmin.umbral = umbral;
    }

    public static void usarUmbral(boolean b){
        usarUmbral = b;
    }

    public static void newInstance(String id){
        contNews++;
        Long o = tabla.get(id);
        if (o == null)
            o = 0L;
        tabla.put(id, ++o);
    }

    public static void liberar(String... s) {
        StringBuffer sb = new StringBuffer();
        for (String id : s) {
            Long cant = tabla.remove(id);
            sb.append(id + " = " + cant + ", ");

            if (usarUmbral && cant != null){
                 if (tablaALiberar.containsKey(id))
                    cant += tablaALiberar.get(id); //le sumo los ya liberados
                tablaALiberar.put(id, cant);

                //si se pasó el umbral => limpio
                long totalALib = getCantObjALiberar();
                if (totalALib >= umbral){
                    tablaALiberar.clear();
                }
            }
        }
//        System.out.println("Liberados = " + sb);
    }

    private static long getCantObjALiberar() {
        long cont = 0;
        for (Object v : tablaALiberar.values()) {
            cont += (Long) v;
        }
        return cont;
    }

    public static void mostrar(){
        mostrar(null);
    }
//    public static void mostrar(String msg){
//        if (msg != null)
//            System.out.println(msg);
//        System.out.println("News total= " + contNews);
//        int cont = 0;
//        for (String o : tabla.keySet()) {
//            System.out.println(o + " = " + tabla.get(o));
//            cont += tabla.get(o);
//        }
//        System.out.println("cont vivos= " + cont);
//        System.out.println("---------------------------------------------------");
//    }
    public static void mostrar(String msg){
        System.out.print(contNews + "" + Constantes.TAB_CHARACTER);
        int cont = 0;
        for (String o : tabla.keySet()) {
            cont += tabla.get(o);
//            System.out.println(o + " = " + tabla.get(o));
        }
        for (String o : tablaALiberar.keySet()) {
            cont += tablaALiberar.get(o);
//            System.out.println("en aLiberar: " + o + " = " + tabla.get(o));
        }
        System.out.println("" + cont );
    }

}

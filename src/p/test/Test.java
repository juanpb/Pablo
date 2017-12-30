package p.test;

import p.util.Constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: JPB
 * Date: Nov 19, 2007
 * Time: 3:18:59 PM
 */
@SuppressWarnings({"ProtectedField", "UnnecessaryBoxing", "ClassReferencesSubclass", "CollectionDeclaredAsConcreteClass"})
public class Test {
    public enum Metodo {REGIONES, UMBRAL, OPTIMO}



    private static Metodo metodoMemoria;

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

    public int getUmbral() {
        return umbral;
    }

    public void setUmbral(int umbral) {
        this.umbral = umbral;
    }

    public static Metodo getMetodoMemoria() {
        return metodoMemoria;
    }

    public static void setMetodoMemoria(Metodo m) {
        metodoMemoria = m;
    }

    protected static void liberarRegion (String id){
        tabla.remove(id);
    }
    protected static void liberar (String id){
        id = getId(id);
        switch (metodoMemoria){
            case REGIONES:{
                //tabla.remove(id);
                break;
            }
            case UMBRAL:{
                long cont = tabla.remove(id); //cant. de obj. vivos a liberar
                if (tablaALiberar.containsKey(id))
                    cont += tablaALiberar.get(id); //le sumo los ya liberados
                tablaALiberar.put(id, cont);
                long cant = getCantObjALiberar();
                if (cant >= umbral){
                    tablaALiberar.clear();
                }
                break;
            }
            case OPTIMO:{
                tabla.remove(id);
                break;
            }
        }
    }

    /**
     * Cantidad de objetos que no se pueden liberar.
     * @return
     */
    private static long getCantObjVivos() {
        long cont = 0;
        for (Object v : tabla.values()) {
            cont += (Long) v;
        }
        return cont;
    }

    private static long getCantObjALiberar() {
        long cont = 0;
        for (Object v : tablaALiberar.values()) {
            cont += (Long) v;
        }
        return cont;
    }

    private static void incrementar(String id) {
        id = getId(id);
        contNews++;
        Long o = tabla.get(id);
        if (o == null)
            o = 0L;
        tabla.put(id, new Long(++o));
    }

    /************************************************************************/
    /*      fin news        /*
    /************************************************************************/
    protected void asociar(Object x, String id){
        incrementar(id);
    }

    protected Long getLong(long x){
        return new Long(x);
    }
    protected Long getLong(long x, String id){
        incrementar(id);
        return new Long(x);
    }

    protected static ArrayList<Long> getArrayListDeLong(int n, String id){
        incrementar(id);
        return new ArrayList<Long>(n);
    }
    protected static Integer getInteger(int x, String id){
        incrementar(id);
        return new Integer(x);
    }

    protected RefObj getRefObj(String id){
        incrementar(id);
        return new RefObj();
    }
    /************************************************************************/
    /*      fin news        /*
    /************************************************************************/
    public static void limpiar(){
        tabla.clear();
    }
    
    public static long getCantNews(String...ids){
        long total = 0;
        for (String id : ids) {
            total += tabla.get(id);
        }
        return total;
    }

    public static long getContNews(){
        return contNews;
    }

    private static String getId(String id){
        if (metodoMemoria == Metodo.REGIONES)
            return getRegion(id);
        else
            return id;
    }

    private static String getRegion(String id){
        for (String reg : idsPorRegion.keySet()) {
            List<String> ids = idsPorRegion.get(reg);
            if (ids.contains(id))
                return reg;
        }
        return null;
    }

    protected void registar(String region, List<String>ids){
        idsPorRegion.put(region, ids);
    }

    public static void mostrarCantNews(){
        long total = 0;
        for (Object o : tabla.keySet()) {
            String id = (String) o;
            long o1 = tabla.get(id);
            total += o1;
            System.out.println(id + " = " + o1);
        }
        System.out.println("total = " + total);
        System.out.println("");
    }

    public static void mostrarEstadisticas(){
        System.out.println("" + getContNews() + "" + Constantes.TAB_CHARACTER  +
                            getCantObjVivos() + "" + Constantes.TAB_CHARACTER +
                            "" + getCantObjALiberar());
    }
}

package p.util;

import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class UtilString{

    /*
    Llena a la izq con el valor de relleno hasta el tamaño tam.
    No trunca si es más largo.
     */
    public static String rellenarCon(String orig, String relleno, int tam){
        while (orig.length() < tam) {
            orig = relleno + orig;
        }
        return orig;
    }

    public static List<String> getTextAsList(String s){
        List<String> res = new ArrayList<String>();
        int i;
        while ((i = s.indexOf(Constantes.NUEVA_LINEA)) > -1) {
            String e = s.substring(0, i);
            res.add(e);
            s = s.substring(i+1);
        }
        if (s.length() > 0)
            res.add(s);

        return res;
    }

    public static List<String> getTextAsListV1(String s){
        List<String> res = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(s, Constantes.NUEVA_LINEA, true);
        while(st.hasMoreElements()){
            String e = st.nextToken();
            res.add(e);
        }
        return res;
    }

    public static String list2String(List<String> s, int desdeLaLinea){
        StringBuffer res = new StringBuffer();

        for(int i = desdeLaLinea; i < s.size(); i++) {
            res.append(s.get(i)).append(Constantes.NUEVA_LINEA);
        }
        
        return res.toString();
    }

    public static String getValoresSeparadosPorComa(List<String> s){
        if (s == null)
            return "";
        String res = "";
        String sep = ", ";

        for(String x : s) {
            res += x + sep;
        }
        if (res.endsWith(sep)){
            res = res.substring(0, res.length() - sep.length());
        }
        return res;
    }

    /**
     * Elimina 'cant' caracteres luego de encontrar 'queda'.
     * Si no encuentra la cadena => devuelve el string original.
     * Ejemplo: eliminarLuegoDe(unoDosTres, 2, Dos) => unoDoses
     */
    public static String eliminarLuegoDe(String str, int cant, String queda){
        int j = str.indexOf(queda);
        if (j < 0)
            return str;

        j+= queda.length();
        String pre = str.substring(0, j);
        if (j + cant > str.length())
            return pre;

        String pos = str.substring(j + cant);

        return pre + pos;
    }

    public static String reemplazarTodo(String str, String viejo, String nuevo){
        if (viejo == null || viejo.equals(""))
            return str;
        int d;
        String res = "";
        String pos = str;
        while ((d = pos.indexOf(viejo)) > -1){
            String pre = pos.substring(0, d);
            pos = pos.substring(d + viejo.length());
            res += pre + nuevo;
        }
        return res + pos;
    }

    public static String reemplazarUnaVez(String str, String viejo, String nuevo){
        int d = str.indexOf(viejo);
        if (d < 0)
            return str;

        String pre = str.substring(0, d);
        String pos = str.substring(d + viejo.length());
        str = pre + nuevo + pos;
        return str;
    }

    /**
     * Devuelve el String en minúscula salvo el comienzo de cada palabra
     */
    public static String mayusMinus(String str){
        String res = "";
        boolean pasarAMayuscula = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)){
                if (pasarAMayuscula)
                    res += Character.toUpperCase(c);
                else
                    res += Character.toLowerCase(c);
                pasarAMayuscula = false;
            }
            else {
                res += c;
                pasarAMayuscula = !(c == '\'' && i > 0);
            }
        }
        return res;
    }

    /**
     * Devuelve el String en minúscula salvo el comienzo de cada palabra
     */
    public static String minusculaSalvoPrimeraLetra(String str){
        StringBuilder res = new StringBuilder("");
        String[] split = str.split(Pattern.quote("."));
        for (String s : split) {
            s = s.trim();
            boolean pasarAMayuscula = true;
            String tmp = "";
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (pasarAMayuscula && Character.isLetter(c)){
                    pasarAMayuscula = false;
                    tmp += Character.toUpperCase(c);
                }
                else if (Character.isLetter(c))
                    tmp += Character.toLowerCase(c);
                else {
                    tmp += c;
                }
            }
            res.append(tmp).append(". ");
//            if (s.length() > 0){
//                String min = s.toLowerCase();
//                if (min.length() > 0)
//                    min = min.substring(1);
//                String may = (s.charAt(0) + "").toUpperCase();
//                res.append(may).append(min).append(". ");
//            }
        }
        //saco el último espacio
        res.deleteCharAt(res.length()-1);

        //si la entrada no termina con . => lo saco
        boolean b = str.trim().endsWith(".");
        if (!b)
            res.deleteCharAt(res.length()-1);
        return res.toString().trim();
    }

    /**
     * Elimina 'cant' caracteres desde el final sin contar la extensión
     */
    public static String eliminarSufijo(String str, int cant){
        String res;
        int x = str.lastIndexOf(".");
        String ext = "";
        if (x > 0)
            ext = str.substring(x);
        else
            x = str.length();
        int h = x - cant;
        if (h < 0)
            return ext;
        res = str.substring(0, h) + ext;
        return res;
    }

    /**
     * Si no encuentra 'hasta' no hace nada.
     * Ejemplo:
     *     str = 'alejandro dolina - Ajedrez.mp3'
     *     hasta = 'dolina'
     *     devuelve = ' - Ajedrez.mp3'
     */
    public static String eliminarDesdeLaIzquierda(String str, String hasta){
        int d = str.indexOf(hasta);
        if (d > -1){
            str = str.substring(d + hasta.length());
        }
        return str;
    }

    public static String eliminarCantDesdeLaIzquierda(String str, int cant){

        if (str.length() > cant){
            str = str.substring(cant);
        }
        return str;
    }

    /**
     * Respeta la extensión.
     * Ejemplo:
     *     str = 'alejandro dolina - Ajedrez.mp3'
     *     hasta = 'dolina'
     *     devuelve = 'alejandro.mp3'
     */
    public static String eliminarDesdeLaDerecha(String str, String hasta){
        int d = str.lastIndexOf(".");
        String ext = "";
        if (d > 0 && d +4 == str.length()) //funciona si la extensión es de tres letras
            ext = str.substring(d);
        else
            d = str.length();
        String nom = str.substring(0, d);
        int h = nom.lastIndexOf(hasta);
        if (h > 0){
            nom = nom.substring(0, h);
        }
        return nom.trim() + ext;
    }

        /**
     * Respeta la extensión.
     * Ejemplo:
     *     str = 'alejandro dolina - Ajedrez.mp3'
     *     cant = '2'
     *     devuelve = 'alejandro dolina - Ajedr.mp3'
     */
    public static String eliminarCantDesdeLaDerecha(String str, int cant){
        int d = str.lastIndexOf(".");
        String ext = "";
        if (d > 0)
            ext = str.substring(d);
        else
            d = str.length();
        String nom = str.substring(0, d);
        if (nom.length() > cant)
            nom = nom.substring(0, nom.length() - cant);

        return nom.trim() + ext;
    }

    public static String insertarDespuesDe(String str, String nuevo, String cond){
        int d = str.indexOf(cond);
        if (d < 0)
            return str;

        String pre = str.substring(0, d);
        String pos = str.substring(d + cond.length());
        str = pre + cond + nuevo + pos;
        return str;
    }
    public static String insertarAntesDe(String str, String nuevo, String cond){
        int d = str.indexOf(cond);
        if (d < 0)
            return str;

        String pre = str.substring(0, d);
        String pos = str.substring(d );
        str = pre +  nuevo + pos;
        return str;
    }

    /**
     * Inserta al final del string, pero antes de la extensión si
     * antesDeLaExtension = true
     *
     * @param str
     * @param nuevo
     * @return
     */
    public static String insertarAlFinal(
            String str, String nuevo, boolean antesDeLaExtension){
        String res;

        if (antesDeLaExtension){
            int d = str.lastIndexOf(".");
            String ext = "";
            if (d > 0)
                ext = str.substring(d);
            else
                d = str.length();
            String nom = str.substring(0, d);
            res = nom.trim() + nuevo + ext;
        }
        else
            res = str + nuevo;
        return res;
    }


    /**
     * Devuelve el string recibido, pero agregándole espacios al final hasta
     * que el tamaño de la cadena sea 'tam'. (NO TRUNCA)
     */
    public static String llenar(String txt, int tam){
        String espacios = "";
        for (int i = txt.length() ; i < tam ; i++){
            espacios = espacios + " ";
        }
        return txt + espacios;
    }


    public static String llenarXIzq(String txt, int tam, String nuevoValor){
        String izq = "";
        for (int i = txt.length() ; i < tam ; i++){
            izq += nuevoValor;
        }
        return izq + txt;
    }

    /**
    * Devuelve el string recibido, pero agregándole espacios al final hasta
    * que el tamaño de la cadena sea 'tam' o truncando en caso de que
     * truncar=true
    */
    public static String llenar(String txt, int tam, boolean truncar){
        String res = llenar(txt, tam);
        if (truncar && res.length() > tam)
            res = res.substring(0, tam);
        return res;
    }

    @Test
    public void llenarTest(){
        String txt = "abcde";
        String conEspST = llenar(txt, 7, false);
        String conEspT  = llenar(txt, 7, true);
        String trunF = llenar(txt, 3, false);
        String trunT = llenar(txt, 3, true);
        assertEquals(txt + "  ", conEspST);
        assertEquals(txt + "  ", conEspT);
        assertEquals(txt, trunF);
        assertEquals("abc", trunT);
    }

    public static List<String> sacarRpepetidos(List<String> list) {
        List<String> res = new ArrayList<String>();
        Set<String> set = new HashSet<String>();
        for(String s : list) {
            boolean b = set.add(s);
            if (b)
                res.add(s);
        }
        return res;
    }

    public static String[] separarAsArray(String txt, String separador) {
        StringTokenizer st = new StringTokenizer(txt, separador);
        List<String> list = new ArrayList<String>();
        while(st.hasMoreElements()){
            String s = st.nextToken().trim();
            list.add(s);
        }
        String[] res = new String[list.size()];
        int i = 0;
        for(String s : list) {
            res[i++] = s;
        }
        return res;
    }

    public static int cantidadApariciones(String txt, String cadenaABuscar) {
        int res = 0;

        while (txt.indexOf(cadenaABuscar) > -1) {
            res++;
            txt = txt.substring(txt.indexOf(cadenaABuscar)+cadenaABuscar.length());
        }

        return res;
    }

    private static void mostrar(long heapSize) {
        System.out.println(heapSize + " bytes");
        System.out.println((heapSize/1024) + " KB");
        System.out.println(((heapSize/1024)/1024) + " MB");
    }
    public static void main(String[] args) {
        String x = "LOS DELITOS INFORMÁTICOS. AAA BBB . CASA. ¡NAda!";
//        String x = "¡NAda!";
        String s = minusculaSalvoPrimeraLetra(x);
        System.out.println(x);
        System.out.println(s);

//        // Get current size of heap in bytes
//        long heapSize = Runtime.getRuntime().totalMemory();
//        System.out.println("heapSize:");
//        mostrar(heapSize);
//        System.out.println("");
//        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
//        // Any attempt will result in an OutOfMemoryException.
//        long heapMaxSize = Runtime.getRuntime().maxMemory();
//        System.out.println("heapMaxSize:");
//        mostrar(heapMaxSize);
//        System.out.println("");
//
//        // Get amount of free memory within the heap in bytes. This size will increase
//        // after garbage collection and decrease as new objects are created.
//        long heapFreeSize = Runtime.getRuntime().freeMemory();
//        System.out.println("heapFreeSize:");
//        mostrar(heapFreeSize);
        }

}

package p.proc.qc2alm;

import p.util.UtilFile;

import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: 30/01/17
 * Time: 14:32
 */
public class Limpiar {
    private static String archEntrada = "D:\\RootBuild\\ALM\\pruebas.csv";
    private static List<String> salida1 = new ArrayList<String>();
    private static List<String> salida2 = new ArrayList<String>();

    private static String msg1 = "Sin acentos";
    private static String msg2 = "Con acentos";

    private static Map<String, String> aCambiar = new HashMap<String, String>();

    static
    {
        aCambiar.put("Á", "A");
        aCambiar.put("É", "E");
        aCambiar.put("Í", "I");
        aCambiar.put("Ó", "O");
        aCambiar.put("Ú", "U");

        aCambiar.put("á", "a");
        aCambiar.put("é", "e");
        aCambiar.put("í", "i");
        aCambiar.put("ó", "o");
        aCambiar.put("ú", "u");
    }

    public static void main2(String[] args) throws IOException {
        List<String> entrada = UtilFile.getArchivoPorLinea(archEntrada);
        verificar(entrada);
    }
    public static void main(String[] args) throws IOException {
        List<String> entrada = UtilFile.getArchivoPorLinea(archEntrada);
        limpiar(entrada);

        System.out.println(msg1 + ": " + salida1.size());
        for (String s : salida1) {
            System.out.println(s);
        }
        System.out.println("");
        System.out.println(msg2 + ": " + salida2.size());
        for (String s : salida2) {
            System.out.println(s);
        }
    }

    private static String limpiar(String entrada) {
        Set<String> keys = aCambiar.keySet();

        for (String key : keys) {
            entrada = entrada.replace(key, aCambiar.get(key));
        }
        return entrada;
    }

    private static void verificar(List<String> entrada) {
        for (String s : entrada) {
            String[] split = s.split(",");
            if (split.length != 4)
                System.out.println(s);
        }
    }
    private static void limpiar(List<String> entrada) {
        for (String s : entrada) {
            String cambiado = limpiar(s);
            if (cambiado.equals(s)){
                salida1.add(cambiado);
            }else{
                salida2.add(cambiado);
            }
        }
    }

    private static boolean tieneENIE(String s) {
        return s.contains("ñ") || s.contains("Ñ");
    }
}

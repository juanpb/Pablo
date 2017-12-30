package p.pruebas;

import p.util.LineNumberReaderItf;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: May 5, 2009
 * Time: 7:53:31 AM
 */
public class ContadorSimbolos implements LineNumberReaderItf, Comparator<String> {

    private String file = "D:\\P\\_env\\borrar\\Another.Year.2010.LIMITED.720p.BluRay.DTS.x264-HDxT (1)_Track1.srt";
    private String[] simbolos = {"Ã", "Â", "³", "©", "±", "Î", "™", "‘", "ï", "»"};

    private Map<String, Integer> map = new HashMap<String, Integer>();

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();
        new ContadorSimbolos().hacer();
        long fin = System.currentTimeMillis();
        long seg = (fin - ini);
        System.out.println("Demoró " + seg + " ms");
    }

    public void hacer() throws IOException {
        UtilFile.readFile(new File(file), this);
    }

    public void newLine(String s) {
        for(String simbolo : simbolos) {
            if (s.contains(simbolo))
                System.out.println(simbolo + " = " + s);
        }

        for(int i = 0; i <s.length(); i++) {
            String x = s.substring(i, i+1);
            Integer cont = map.get(x);
            if (cont == null)
                cont = 0;
            cont++;
            map.put(x, cont);
        }
    }



    public void terminó() {
        List<String> ks = ordenar();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        imprimir(ks);
        System.out.println("Fin");
    }

    private List<String> ordenar() {
        Set<String> keys = map.keySet();
        List<String> ks = p.util.Util.getList(keys);
        Collections.sort(ks, this);
        return ks;
    }

    private void imprimir(List<String> ks) {
        int total =0;
        for(String k : ks) {
            Integer cont = map.get(k);
            System.out.println(k + " = " + cont);
            total += cont;
        }
        System.out.println("total = " + total);
    }

    /**
     * Devuelve primero en la lista las palabras que aparecen más veces.
     * Si aparecen igual cantidad de veces => orden alfabético.
     */
    public int compare(String s1, String s2) {
        Integer cont1 = map.get(s1);
        Integer cont2 = map.get(s2);
        int i = cont2 - cont1;
        if (i == 0)
            return s1.compareTo(s2);
        else
            return i;
    }
}

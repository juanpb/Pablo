package p.proc;

import p.util.LineNumberReaderItf;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: 19/04/12
 * Time: 09:01
 */
public class ContadorCaracteres implements LineNumberReaderItf, Comparator<Character> {
    private boolean imprimirSoloSímbolosSinContador = false;
    
    private String file = "D:\\P\\_env\\The.Sting.1973.1080p.HDDVD.x264-FSiHD_Track5.srt";

    private Map<Character, Integer> map = new HashMap<Character, Integer>();

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();
        new ContadorCaracteres().hacer();
        long fin = System.currentTimeMillis();
        long seg = (fin - ini);
        System.out.println("Demoró " + seg + " ms");
    }

    public void hacer() throws IOException {
        UtilFile.readFile(new File(file), this);
    }

    public void newLine(String s) {
        String copia = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Integer cont = map.get(c);
            if (cont == null)
                cont = 0;
            cont++;
            map.put(c, cont);
            copia += c;
        }
    }
 

    public void terminó() {
        List<Character> ks = ordenar();
        imprimir(ks);
        System.out.println("Fin");
    }

    private List<Character> ordenar() {
        Set<Character> keys = map.keySet();
        List<Character> ks = p.util.Util.getList(keys);
        Collections.sort(ks, this);
        return ks;
    }

    private void imprimir(List<Character> ks) {
        int total =0;
        String salida;
        for(Character k : ks) {
            salida = k + "";
            int cont = map.get(k);
            if (!imprimirSoloSímbolosSinContador)
                salida += " = " + cont;

            System.out.println(salida);
            total += cont;
        }

        System.out.println("total = " + total);
    }

    /**
     * Devuelve primero en la lista las palabras que aparecen más veces.
     * Si aparecen igual cantidad de veces => orden alfabético.
     */
    public int compare(Character s1, Character s2) {
        Integer cont1 = map.get(s1);
        Integer cont2 = map.get(s2);
        int i = cont2 - cont1;
        if (i == 0)
            return s1.compareTo(s2);
        else
            return i;
    }
}

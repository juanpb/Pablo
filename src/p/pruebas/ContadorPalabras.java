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
public class ContadorPalabras implements LineNumberReaderItf, Comparator<String> {

    private boolean imprimirSoloPalabras = true;
    private String file = "D:\\tmp\\a.txt";
    private String[] simbolos = {"\"", "'", "\\?", "¿", "¡", "!", "º", "", "$"
    , "%", "\\(", "\\)", ",", "\\.", ";", ":", "”", "“", "‘", "’", "·", "\\[", "\\]"};

    private Map<String, Integer> map = new HashMap<String, Integer>();

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();
        new ContadorPalabras().hacer();        
        long fin = System.currentTimeMillis();
        long seg = (fin - ini);
        System.out.println("Demoró " + seg + " ms");
    }

    public List<String> getPalabras(List<String> orig){
        List<String> res = new ArrayList<String>();
        for(String s : orig) {
            newLine(s);
        }
        return ordenar();
    }

    public void hacer() throws IOException {
        UtilFile.readFile(new File(file), this);
    }

    public void newLine(String s) {
        StringTokenizer st = new StringTokenizer(s, " ");
        while (st.hasMoreElements()) {
            String x = sacarSimbolos(st.nextToken());
            x = x.toLowerCase();
            if (!isNumber(x)){
                Integer cont = map.get(x);
                if (cont == null)
                    cont = 0;
                cont++;
                map.put(x, cont);
            }
        }
    }

    private boolean isNumber(String x) {
        try {
            Long.parseLong(x);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String sacarSimbolos(String x) {
        for(String s : simbolos) {
            x = x.replaceAll(s, "");
        }
        return x;
    }

    public void terminó() {
        List<String> ks = ordenar();
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
            if (!imprimirSoloPalabras)
                k += " = " + cont;
            System.out.println(k);
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

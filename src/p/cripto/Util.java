package p.cripto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * User: JPB
 * Date: 03/01/13
 * Time: 14:25
 */
public class Util {

    public static final List<Character> ordenProbaCastellano = new ArrayList<Character>(
            Arrays.asList('A', 'E', 'O', 'S', 'N', 'R', 'I', 'L', 'D', 'T', 'U', 'C', 'M',
                    'P', 'B', 'Y', 'V', 'H', 'Q', 'F', 'J', 'Z', 'X', 'K','W') );
        /*ordenProbaCastellano.add(' ');
        ordenProbaCastellano.add('A');
        ordenProbaCastellano.add('E');
        ordenProbaCastellano.add('O');
        ordenProbaCastellano.add('S');
        ordenProbaCastellano.add('N');
        ordenProbaCastellano.add('R');
        ordenProbaCastellano.add('I');
        ordenProbaCastellano.add('L');
        ordenProbaCastellano.add('D');
        ordenProbaCastellano.add('T');
        ordenProbaCastellano.add('U');
        ordenProbaCastellano.add('C');
        ordenProbaCastellano.add('M');
        ordenProbaCastellano.add('P');
        ordenProbaCastellano.add('B');
        ordenProbaCastellano.add('Y');
        ordenProbaCastellano.add('V');
        ordenProbaCastellano.add('H');
        ordenProbaCastellano.add('Q');
        ordenProbaCastellano.add('F');
        ordenProbaCastellano.add('J');
        ordenProbaCastellano.add('Z');
        ordenProbaCastellano.add('X');
        ordenProbaCastellano.add('K');
        ordenProbaCastellano.add('W');

    };                          */

    public static void main(String[] args) throws NoSuchAlgorithmException {
        long ini = System.currentTimeMillis();

        String x = "25123";

        for (int i = 0; i < 50000000; i++) {
            String w = ""+i;
//            System.out.println(Util.sha256(w));
            Util.sha256(w);
            if (i % 1000000==0){
                System.out.println(i);
            }
        }

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");
    }

    public static Map<Character, Integer> getFrecuenciasCaracteres(byte[] texto){
        return getFrecuenciasCaracteres(new String(texto));
    }

    public static Map<Character, Integer> getFrecuenciasCaracteres(String texto){
        Map<Character, Integer> res = new HashMap<Character, Integer>();

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            Integer cont = res.get(c);
            if (cont == null)
                cont = 0;
            cont++;
            res.put(c, cont);

        }

        return res;
    }
    /*
    Devuelve primero en la lista los char que aparecen más veces.
     * Si aparecen igual cantidad de veces => orden alfabético.
    */
    public static List<Character> ordenar(final Map<Character, Integer> map){
        Set<Character> keys = map.keySet();
        List<Character> ks = p.util.Util.getList(keys);
        Collections.sort(ks, new Comparator<Character>() {
            @Override
            public int compare(Character s1, Character s2) {
                Integer cont1 = map.get(s1);
                Integer cont2 = map.get(s2);
                int i = cont2 - cont1;
                if (i == 0)
                    return s1.compareTo(s2);
                else
                    return i;
            }
        });
        return ks;

    }


    private static String sha256String(String txt) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] sha = sha256.digest(txt.getBytes());


        StringBuilder sb = new StringBuilder();
        for(int i=0; i< sha.length ;i++) {
            sb.append(Integer.toString((sha[i] & 0xff) + 0x100, 16).substring(1));
        }
        String generatedPassword = sb.toString();
        return generatedPassword;
    }

    private static byte[] sha256(String txt) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] passBytes = txt.getBytes();
        return  sha256.digest(passBytes);
    }
}

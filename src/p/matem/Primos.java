package p.matem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: May 15, 2009
 * Time: 9:44:56 AM
 */
public class Primos {
    private static List<Long> primos;

    public static void main(String[] args) throws IOException {

        long ini = System.currentTimeMillis();
        List<Long> p = getNPrimos(20);
        long acum = 1;
        for(Long aLong : p) {
            acum *= aLong;
            System.out.println("Es primo " + (acum+1) + ": " + esPrimo(acum+1));
        }
//        System.out.println(p);
//
//        p = getNPrimos(15);
//        System.out.println(p);
//
//        p = getNPrimos(8);
//        System.out.println(p);
//
//        long fin = System.currentTimeMillis();
//        long seg = (fin - ini);
//        System.out.println("Demoró " + seg + " ms");
    }

    /**
     * @return el i-ésimo primo empezando por 0
     */
    public static List<Long> getNPrimos(int cant){
        if (primos == null){
            primos = new ArrayList<Long>();
            primos.add(2L);
        }

        if (primos.size() >= cant){
            return primos.subList(0, cant);
        }

        long cont = primos.size();
        for(long i = 3; cont <cant; i++, i++) {
            if (!Util.esDivisible(primos, i)){
                primos.add(i);
                cont++;
            }
        }
        return primos;
    }

    /**
     * @return los primos menores a numMax (incluido)
     */
    public static List<Long> getPrimosMenorosA(int numMax){
        List<Long> res = new ArrayList<Long>();
        if (numMax >=2)
            res.add(2L);
        for(long i = 3; i <numMax; i++, i++) {
            if (!Util.esDivisible(res, i))
                res.add(i);
        }
        return res;
    }

    /**
     * @return el i-ésimo primo empezando por 0
     */
    public static long getIEsimo(int i){
        List<Long> nPrimos = getNPrimos(i+1);
        return nPrimos.get(i);
    }

    public static boolean esPrimo(long n){
        if (n==2)
            return true;

        if (n==0 || n==1 || n % 2 == 0)
            return false;
        for(long i = 3; i*i < n; i++) { //criba Eratóstenes
            if (n % i == 0)
                return false;
        }
        return true;
    }
}

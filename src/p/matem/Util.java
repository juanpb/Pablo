package p.matem;

import java.util.List;

/**
 * User: JPB
 * Date: May 15, 2009
 * Time: 9:54:00 AM
 */
public class Util {

    public static long factorial(int n){
        if (n == 0)
            return 1;
        long res = 1;
        for(int i = 1; i <= n; i++) {
            res *=i;
        }
        return res;
    }

    public static boolean esDivisible(List<Long> d, long n){
        for(Long dd : d) {
            if (n % dd == 0)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 20; i++) {
            System.out.println("Factorial ( " + i + ") = " + factorial(i));
        }
    }
}

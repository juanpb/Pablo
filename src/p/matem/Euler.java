package p.matem;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * User: JPB
 * Date: May 15, 2009
 * Time: 9:44:29 AM
 */
public class Euler {
    private static int CANT_ITER = 130;

    public static void main(String[] args) {
//        MathContext mc = new MathContext(512);
//        BigDecimal a = new BigDecimal(1, mc);
//        BigDecimal b = new BigDecimal(3, mc);
//
//        a = a.divide(b, mc);
//        System.out.println("b = " + a);
//        String s = "33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333";
//        System.out.println("s.length() = " + s.length());
        probar();
    }

                           /*

Well do you, don't you want me to make you
I'm coming down fast, but don't let me break you
Tell me, tell me, tell me your answer
You may be a lover but you ain't no dancer

                           * */

    private static void probar(){
        MathContext mc = MathContext.DECIMAL128;
        double s = 0;
        double p = 1;
        BigDecimal bdP = new BigDecimal(1);


        for(int i = 1; i <= CANT_ITER; i++) {
            double a = 1.0/(i*i);
            s += a;

            long iEsimo = Primos.getIEsimo(i - 1);
            iEsimo *= iEsimo;
            double a1 = 1.0 / iEsimo;
            double a2 = 1.0 - a1;
            double a3 = 1.0/ a2;
            p *= a3;

            BigDecimal bd1 = new BigDecimal(1);
            BigDecimal bd2 = new BigDecimal(1);
            BigDecimal bd3 = new BigDecimal(1);
            BigDecimal bIE = new BigDecimal(iEsimo);
            BigDecimal ba1 = bd1.divide(bIE, mc);
            BigDecimal ba2 = bd2.subtract(ba1);

            bd3 = bd3.divide(ba2, mc);
            bdP = bdP.multiply(bd3, mc);

            BigDecimal m = new BigDecimal(p);
            System.out.println(m.subtract(bdP));

        }
    }
}

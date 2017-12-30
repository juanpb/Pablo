package p.test;

/**
 * User: JPB
 * Date: Jan 24, 2008
 * Time: 1:39:05 PM
 */
@SuppressWarnings({"ALL"})
public class Ejemplo1 {

    public static void main(String[] args) {
        int CANT = 2000;
        Ejemplo1 e = new Ejemplo1();
        for (int i = 0; i < CANT; i++) {
            e.m0(1);
        }
    }

    void m0(int mc) {
        RefObj h = new RefObj();
        Object[] a = m1 (mc);
        Object[] e = m2 (5 * mc, h);
    }

    Object[] m1(int k) {
        int i;
        RefObj l = new RefObj();
        Object[] b = new Object[k];
        for(i = 0; i < k; i++ ) {
            b[i] = m2(i,l);
        }
        Object[] c = new Integer[666];
        return b;
    }

    Object[] m2(int n, RefObj s) {
        int j;
        Object c,d;
        Object[] f = new Object[n];
        for(j = 0; j < n; j++) {
            if(j % 3 == 0) {
                c = new Integer[ j*2 +  1];
            }
            else {
                c = new Integer( 100 );
            }
            d = new Integer[1];
            s.ref = d;
            f[j] = c;
        }
        return f;
    }
}

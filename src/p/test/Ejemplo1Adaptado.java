package p.test;

/**
 * User: JPB
 * Date: Jan 24, 2008
 * Time: 1:39:05 PM
 */
@SuppressWarnings({"ALL"})
public class Ejemplo1Adaptado {

    public static void main(String[] args) {
        Ejemplo1Adaptado x = new Ejemplo1Adaptado();
        for (int i = 0; i < 100; i++) {
//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println("n = " + i+ "\n");
            x.m0(i);
        }
    }

    void m0(int mc) {
        MemAdmin.newInstance("m0_1");
        RefObj h = new RefObj();

        Object[] a = m1 (mc);
        MemAdmin.liberar("m1_3", "m2_3", "m2_6", "m2_8");

        Object[] e = m2 (5 * mc, h);
        MemAdmin.liberar("m0_1","m2_3", "m2_6", "m2_8", "m2_9");

        MemAdmin.mostrar("fin de m0");
    }

    Object[] m1(int k) {
        int i;
        MemAdmin.newInstance("m1_2");
        RefObj l = new RefObj();

        MemAdmin.newInstance("m1_3");
        Object[] b = new Object[k];

        for(i = 0; i < k; i++ ) {
            b[i] = m2(i,l);
            MemAdmin.liberar("m2_9");
        }
        MemAdmin.liberar("m1_2", "m2_9");

        MemAdmin.newInstance("m1_6");
        Object[] c = new Integer[666];
        MemAdmin.liberar("m1_6");

        MemAdmin.mostrar("fin de m1");
        return b;
    }

    Object[] m2(int n, RefObj s) {
        int j;
        Object c,d;

        MemAdmin.newInstance("m2_3");
        Object[] f = new Object[n];

        for(j = 0; j < n; j++) {
            if(j % 3 == 0) {
                MemAdmin.newInstance("m2_6");
                c = new Integer[ j*2 +  1];
            }
            else {
                MemAdmin.newInstance("m2_8");
                c = new Integer( 100 );
            }
            MemAdmin.newInstance("m2_9");
            d = new Integer[1];

            s.ref = d;
            f[j] = c;
        }
        MemAdmin.mostrar("fin de m2");
        return f;
    }
}

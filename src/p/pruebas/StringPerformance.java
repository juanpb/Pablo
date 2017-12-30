package p.pruebas;

/**
 * User: JPB
 * Date: Feb 4, 2011
 * Time: 12:38:57 AM
 */
public class StringPerformance {
    private static final int CONT = 1000;

    public static void main(String[] args) {
        probar();
    }

    public static void probar() {
        String x = "AbCs";
        String str = "";
        StringBuffer sbi = new StringBuffer();
        StringBuilder sbu = new StringBuilder();

        long ini = System.currentTimeMillis();
        for(int i = 0; i < CONT; i++) {
            str += x;
        }
        long fin = System.currentTimeMillis();
        long ms = (fin - ini);
        System.out.println("con str Demoró " + ms + " ms");

        ini = System.currentTimeMillis();
        for(int i = 0; i < CONT; i++) {
            sbi.append(x);
        }
        fin = System.currentTimeMillis();
        ms = (fin - ini);
        System.out.println("con sbui Demoró " + ms + " ms");

        ini = System.currentTimeMillis();
        for(int i = 0; i < CONT; i++) {
            sbu.append(x);
        }
        fin = System.currentTimeMillis();
        ms = (fin - ini);
        System.out.println("con sbuf Demoró " + ms + " ms");

    }
}

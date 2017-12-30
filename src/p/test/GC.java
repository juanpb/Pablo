package p.test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Administrador
 * Date: 08/12/2007
 * Time: 13:33:29
 */
public class GC {
    static int CICLOS = 1000000;
    static List list = new ArrayList(CICLOS);
    public static void main(String[] args) {
        for (int i = 0; i <CICLOS; i++) {
            m1();
        }
    }

    private static void m1() {
        list.add(new Integer(17)) ;
    }
}

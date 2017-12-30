package p.optim;

import java.util.*;

public class LlamadasAFunc {
    int CICLOS = 100000;
    Vector vec;
    public LlamadasAFunc() {

        long ini = System.currentTimeMillis();
        for (int i = 0; i < CICLOS;i++ ) {
            vec = new Vector();
            aux();
        }
        long fin = System.currentTimeMillis();
        long d = fin - ini;
        System.out.println("tiempo: " + d);

    }

    private void aux(){
        long j = 9;
        long i = j;
        if (i < 500)
            j = j * 10;
        else
            j = j - 10;
        Vector x = vec;

        for (int ih = 0; ih < 50;ih++ ) {
            x.add(new Integer(ih));
        }
        long z = 0;
        for (int ii = 0; ii < x.size();ii++ ) {
            z += ((Integer)x.elementAt(ii)).intValue();
        }
        j = z +99;

    }
    public static void main(String[] args) {
        LlamadasAFunc llamadasAFunc1 = new LlamadasAFunc();
    }
}
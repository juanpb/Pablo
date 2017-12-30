package tleng.gramatica;

import java.io.*;
import java.util.*;

public class Prueba {

    public static void main(String[] args) throws Exception{

        GLC g = new GLC(" {abz} {SB} S S:Sa; S:B; B:b; B:z;");
        g.imprimirGramatica();
        LRUno lr = new LRUno(g);
        boolean b = lr.tieneConflicto();
        System.out.println("conflicto ?? " + b);
        String cad = "ab";
        b = lr.pertenece(cad);
        System.out.println("pertenece?? " + b);
        /*
        Produccion p  = new Produccion(GLC.SPRIMA + ":S;", 0);
        Item it = new Item(p, GLC.PESOS);
        Vector v = new Vector();
        v.add(it);
        Vector v2 = lr.clausura(v);
        System.out.println("asd");*/
    }

}
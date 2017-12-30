package p.pruebas.combinaciones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: JPB
 * Date: 4/19/11
 * Time: 7:39 AM
 */
public class Recursivo {
/*
todas las combinaciones:
        recursivo: caso base 1, devuelve una lista con un solo elem
                        otro: genera TLC sin el primer elemento y agrega este elemento en todas las posiciones.
        iterativo: factorial para saber cant. de combinaciones
            ej: cant de elem. 4, fact = 24, cada elem. va a estar 24/4 veces en cada posición.

            combin: n! / m! (n-m)!
* */

    public static void main(String[] args) {
        Recursivo r = new Recursivo();
        Collection<String> col = new ArrayList<String>();
        for(int i = 0; i < 6; i++) {
            long ini = System.currentTimeMillis();
            List<List<String>> combinaciones = r.getCombinaciones(col);
            long fin = System.currentTimeMillis();
            long seg = (fin - ini)/1;
            System.out.println("Demoró " + seg + " ms. Cant. de elem de entrada: " + i);
            System.out.println("Cant. elem de salida: " + combinaciones.size());
            System.out.println(combinaciones);
            System.out.println("_____________________________________________");
            col.add(""+i);
        }
    }
//    public static void main(String[] args) {
//        Collection<String> col = new ArrayList<String>(args.length);
//        for(String s : args) {
//            col.add(s);
//        }
//        Recursivo r = new Recursivo();
//        List<List<String>> combinaciones = r.getCombinaciones(col);
//        System.out.println(combinaciones);
//    }

    public <E> List<List<E>> getCombinaciones(Collection<E> elems){
        return getCombinaciones(new ArrayList<E>(elems));
    }

    private  <E> List<List<E>> getCombinaciones(List<E> elems){
        if (casoBase(elems)){
            List<List<E>> lists = new ArrayList<List<E>>(1);
            lists.add(elems);
            return lists;
        }
        else{
            E e = elems.remove(0);
            List<List<E>> combinaciones = getCombinaciones(elems);

            return agregarEnListas(combinaciones, e);
        }
    }

    private <E> List<List<E>> agregarEnListas(List<List<E>> elems, E aAgregar){
        List<List<E>> res = new ArrayList<List<E>>();
        for(List<E> lista : elems) {
            //a cada elemento de la lista, le agrego 'aAgregar' en todas
            //las posiciones, desde la 0, hasta la n - 1
            List<List<E>> nuevaLis = agregarEnLista(lista, aAgregar);
            res.addAll(nuevaLis);
        }

        return res;
    }

    /**
     * A partir de una lista de n elementos, devuelvo una lista de n + 1
     * listas. En la 1ra de estas listas, el elemento aAgregar está en la posición 0,
     * en la 2da en la pos. 1, etc
     */
    private <E> List<List<E>> agregarEnLista(List<E> lista, E aAgregar){
        List<List<E>> res = new ArrayList<List<E>>();
        for(int posi = 0; posi < lista.size()+1; posi++) {
            List<E> nuevaLista = new ArrayList<E>(lista.size());
            nuevaLista.addAll(lista);
            nuevaLista.add(posi, aAgregar);
            res.add(nuevaLista);
        }

        return res;
    }

    private  <E> boolean casoBase(List<E> elems){
        return elems.size() < 2;
    }

}

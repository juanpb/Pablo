package p.proc;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 11, 2008
 * Time: 1:19:18 PM
 */
public class Factorizar {

    public static void main(String[] args) {
        Factorizar f = new Factorizar();
        List<List<Integer>> list = f.paso1();
        list = f.sacarRepetidos(list);
        f.sumarOrdenarMostrar(list);

        for(int i=3; i<10; i++){
            list = f.agregarNro(list);
            list = f.sacarRepetidos(list);
            System.out.println("----------------------------------------------");
            System.out.println("Tamaño: " + i);
            f.sumarOrdenarMostrar(list);
        }
    }


    private void sumarOrdenarMostrar(List<List<Integer>> x){
        List[] xSuma = new ArrayList[362881];
        for (List<Integer> list : x) {
            int suma = sumar(list);
            List list1 = xSuma[suma];
            if (list1 == null){
                list1 = new ArrayList();
                xSuma[suma] = list1;
            }
            list1.add(list);
        }
        for (int i = 0; i < xSuma.length; i++) {
            List a = xSuma[i];
            if (a != null){
                System.out.println(i + ": " + list2String(a));
            }
        }
    }

    private String list2String(List<List<Integer>> a){
        StringBuffer sb = new StringBuffer();
        for (List<Integer> x : a) {
            for (Integer i : x) {
                sb.append(i);
            }
            sb.append("|");
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    private int sumar(List<Integer> a){
        int res = 0;
        for (Integer i : a) {
            res += i;
        }
        return res;
    }

    private List<List<Integer>> agregarNro(List<List<Integer>> x){
        List<List<Integer>> res = new ArrayList();
        for (List<Integer> r : x) {
            res.addAll(agregarN(r));
        }
        return res;
    }

    private List<List<Integer>> agregarN(List<Integer> x){
        List<List<Integer>> res = new ArrayList();

        for (int i = 1; i < 10; i++) {
            if (!x.contains(i)){
                List<Integer> n = new ArrayList();
                n.addAll(x);
                n.add(i);
                res.add(n);
            }
        }

        return res;
    }

    public List<List<Integer>> sacarRepetidos(List<List<Integer>> x){
        List<List<Integer>> res = new ArrayList();
        for (List<Integer> r : x) {
            if (!estaEn(r, res))
                res.add(r);
        }
        return res;
    }

    private boolean estaEn(List<Integer> x, List<List<Integer>> lista){
        for (List<Integer> xx : lista) {
            if (sonIguales(x, xx))
                return true;
        }
        return false;
    }

    private boolean sonIguales(List<Integer> x, List<Integer> x2){
        if (x.size() != x2.size())
            return false;
        else{
            return x2.containsAll(x);
        }
    }

    public void mostrar(List<List<Integer>> x){
        System.out.println("x.size() = " + x.size());
        for (List<Integer> list : x) {
            System.out.println(list);
        }
    }

    public List<List<Integer>> paso1(){
        List<List<Integer>> res = new ArrayList();

        for (int i = 1; i < 9; i++) {
            for (int j = i+1; j < 10; j++) {
                List<Integer> c = new ArrayList<Integer>(2);
                c.add(i);
                c.add(j);
                res.add(c);
            }
        }

        return res;
    }
}

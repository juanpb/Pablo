package p.dm.trans;

import p.dm.Registro;

import java.util.*;

/**
 * User: JPB
 * Date: Oct 6, 2008
 * Time: 8:40:36 AM
 */
public class Imprimir {

    public  static void imprimirCantXRango(List<Rango> rangos, List<Integer> ordenados) {
        int ind;
        ind = 0;
        int contXRango = 0;
        Rango r = rangos.get(ind);
        List<Integer> cants = new ArrayList<Integer>();
        for(Integer o : ordenados) {
            if (r.enRango(o))
                contXRango++;
            else{
                cants.add(contXRango);
                contXRango = 0;
                ind++;
                if (ind < rangos.size())
                    r = rangos.get(ind);
            }
        }
        //meto el último
        cants.add(contXRango);

        int prom = ordenados.size() / rangos.size();
        System.out.println("promedio: " + prom);
        ind = 0;
        int dist = 0;
        for(Rango r2 : rangos) {
            Integer cant = cants.get(ind++);
            int d = prom - cant;
            dist += Math.sqrt(d * d);
            System.out.println(r2.toString() + cant);
        }
        System.out.println("dist = " + dist);
    }

    public static void calcularEImprimirCantXValor(List<Registro> regs, int atrib){
        List<Integer> valores = new ArrayList<Integer>();
        Map<Integer, Integer> cantXV = new HashMap<Integer, Integer>();
        for(Registro reg : regs) {
            Object o = reg.getAtributos().get(atrib);
            Integer ii = cantXV.get(new Integer(o.toString()));
            if (ii == null){
                ii = 0;
                valores.add(new Integer(o.toString()));
            }
            ii++;
            cantXV.put(new Integer(o.toString()), ii);
         }
        Collections.sort(valores);
        for(Integer v : valores) {
            System.out.println(v + " = " + cantXV.get(v));
        }
    }

}
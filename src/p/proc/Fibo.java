package p.proc;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Jan 25, 2008
 * Time: 12:02:23 PM
 */
public class Fibo {
    public static void main(String[] args) {
        Fibo f= new Fibo();
        List<Long> x = f.getSerie(50);
        List<Double> a = f.calcularAureo(x);
        a = f.distancia(a);
        f.mostrar(a);
    }

    private double getAureoPulenta(){
        return (1+Math.sqrt(5))/2;
    }

    private void mostrar(List s){
        for (Object value : s) {
            System.out.println(value );
        }
        System.out.println("");
    }

    public List<Double> distancia(List<Double> f){
        List<Double> res = new ArrayList<Double>(f.size());
        double pulenta = getAureoPulenta();
        for (Double aF : f) {
            double x = aF - pulenta;
            res.add(x);
        }
        return res;
    }

    public List<Double> calcularAureo(List<Long> f){
        List<Double> res = new ArrayList<Double>(f.size());
        if (f.size() < 2)
            return res;
        for(int i=1; i<f.size()-1;i++){
            long n = f.get(i);
            long d = f.get(i-1);
            double a = (double)n / d;
            res.add(a);
        }
        return res;
    }
    public List<Long> getSerie(int cant){
        List<Long> res = new ArrayList<Long>(cant);
        if (cant < 2)
            return res;
        res.add(1L);
        res.add(1L);
        for(int i=0; i<cant-2;i++){
            Long n = res.get(i) + res.get(i+1);
            res.add(n);
        }
        return res;
    }
}

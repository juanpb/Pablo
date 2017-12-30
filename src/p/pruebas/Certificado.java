package p.pruebas;

import java.util.ArrayList;
import java.util.List;

class A {

   public static void main(String[] args) {
       List<Long> x = new ArrayList<Long>(3);

       x.add(new Long(1));
       x.add(new Long(2));
       x.add(new Long(3));

       mostrar(x);

       x.remove(new Long(2));
       mostrar(x);
   }

    public static void mostrar(List<Long> a){
        for (Long b : a) {
            System.out.println("b = " + b);
        }
    }
}
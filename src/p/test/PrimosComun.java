package p.test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Administrador
 * Date: 10/11/2007
 * Time: 19:15:41
 */
@SuppressWarnings({"UnnecessaryBoxing"})
public class PrimosComun{

    public static void main(String[] args) {
        main2(100);
    }


    public static void main2(int n) {
        PrimosComun p = new PrimosComun();
        p.calcularYMostrarPrimerosNPrimos(n);
    }

//    private static void mostrar(List<Long> primos) {
//        for (Long primo : primos) {
//            System.out.print(primo + ", ");
//        }
//    }

    public void calcularYMostrarPrimerosNPrimos(Integer n){
        List<Long> primos = primerosNPrimos(n);
//        mostrar(primos);
    }

    private List<Long> primerosNPrimos(Integer n){
        List<Long> primos = new ArrayList<Long>();
        Integer cantEncontrados = new Integer(0);
        Long indice = new Long(2);
        while (cantEncontrados < n)
        {
            if (esPrimo(indice)){
                primos.add(indice);
                cantEncontrados = new Integer(cantEncontrados + 1);
//                if ((cantEncontrados % 10)==0)
                    System.out.println("cantEncontrados = " + cantEncontrados);
            }
            indice = new Long(indice + 1);
        }
        return primos;
    }


    private boolean esPrimo(Long testPrime)
    {
        if(testPrime == 1)
            return false;
        Long test = new Long(2);
    	while ( test < testPrime )
    	{
    		if ( testPrime % test == 0 )
    			return false;
    		test = new Long(test + 1);
    	}
        return true;
    }

}

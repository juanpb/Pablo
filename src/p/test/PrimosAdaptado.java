package p.test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Administrador
 * Date: 10/11/2007
 * Time: 19:15:41
 */
@SuppressWarnings({"UnnecessaryBoxing"})
public class PrimosAdaptado extends Test {

    public static void main(String[] args) {
        PrimosAdaptado p = new PrimosAdaptado();
        setMetodoMemoria(Metodo.UMBRAL);
        p.setUmbral(100);
        for (int i = 1; i<50;i++){
            p.calcularYMostrarPrimerosNPrimos(i);
        }
        mostrarMemoria("main");
    }

    private static void mostrarMemoria(String x) {
//        System.out.print(x + ": ");
        if (getMetodoMemoria() == Metodo.REGIONES)
            liberarRegion(x);
        mostrarEstadisticas();
    }

    private static void mostrar(List<Long> primos) {
//        for (Long primo : primos) {
//            System.out.print(primo + ", ");
//        }
//        System.out.println("");
        mostrarMemoria("mostrar");
    }

    public void  calcularYMostrarPrimerosNPrimos(Integer n){
        List list = new ArrayList();
        list.add("primos");                  
        list.add("candidatoNoPrimo");
        registar("calcularYMostrarPrimerosNPrimos", list);
        List<Long> primos = primerosNPrimos(n);
        mostrar(primos);
        liberar("primos");
        mostrarMemoria("calcularYMostrarPrimerosNPrimos");
    }

    private List<Long> primerosNPrimos(Integer n){
        List list = new ArrayList();
        list.add("cantEncontrados");
        list.add("candidatoNoPrimo");
        registar("primerosNPrimos", list);

        List<Long> primos = getArrayListDeLong(n, "primos");
        Integer cantEncontrados = getInteger(0,"cantEncontrados");
        Long candidato = getLong(2);
        while (cantEncontrados < n)
        {
            if (esPrimo(candidato)){
                asociar(candidato, "primos");
                primos.add(candidato);
                int cantEncontradosCopy = cantEncontrados;
                liberar("cantEncontrados");
                cantEncontrados = getInteger(cantEncontradosCopy + 1,"cantEncontrados");
            }else{
                asociar(candidato, "candidatoNoPrimo");
                liberar("candidatoNoPrimo");
            }
            candidato = getLong(candidato + 1 );
        }
        if (candidato > 4){ // si <= 4 => no se usó candidatoNoPrimo         
            asociar(candidato, "candidatoNoPrimo");
            liberar("candidatoNoPrimo");
        }
        liberar("cantEncontrados");
        mostrarMemoria("primerosNPrimos");
        return primos;
    }


    private boolean esPrimo(Long testPrime)
    {
        List list = new ArrayList();
        list.add("test");
        registar("esPrimo", list);

        if(testPrime == 1)
            return false;
        Long test = getLong(2, "test");
    	while ( test < testPrime )
    	{
            long testCopy = test;
            liberar("test");
            if ( testPrime % testCopy == 0 )
    			return false;
    		test = getLong(testCopy + 1, "test");
        }
        liberar("test");
        mostrarMemoria("esPrimo");
        return true;
    }

}

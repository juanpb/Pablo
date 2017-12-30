package p.test;


import java.util.List;

/**
 * User: Administrador
 * Date: 10/11/2007
 * Time: 19:15:41
 */
@SuppressWarnings({"UnnecessaryBoxing"})
public class Primos extends Test {

    public static void main(String[] args) {
//        int[] args2 = new int[]{10,20,30,40,50,60,70,80,90,100,300,600,1000,2000,4000,8000,16000};
//        int[] args2 = new int[]{10,20,30,40,50,60,70,80,90,100};
//        int[] args2 = new int[]{29,71,113,173,229,281,349,409,463,541,1987,
//                4409,7919,17389,37813,81799,176081};
//        for (int n : args2) {
//            main2(n);
//            mostrarCapturadosEnCalcularYMostrarPrimerosNPrimos(n);
//            mostrarTamañoDeRegiones(n);
//            limpiar();
//        }
        main2(40);
    }

    private static void mostrarTamañoDeRegiones(int n){
        long z = getCantNews("primerosNPrimos.1", "primerosNPrimos.3", "primerosNPrimos.8");
        System.out.println("CS en región A para n = " + n + ": " + z);
        long total = z;
        z = getCantNews("primerosNPrimos.2", "primerosNPrimos.7");
        System.out.println("CS en región B para n = " + n + ": " + z);
        total +=z;
        z = getCantNews("esPrimo.3", "esPrimo.7");
        total +=z;
        System.out.println("CS en región C para n = " + n + ": " + z);
        System.out.println("Total: " + total);
        System.out.println("");
    }

    private static void mostrarCapturadosEnCalcularYMostrarPrimerosNPrimos(int n){
        long z = getCantNews("primerosNPrimos.3", "primerosNPrimos.8");
        System.out.println("Capturados para n = " + n + ": " + z);
    }
    private static void mostrarCapturadosEnPrimerosNPrimos(int n){
        long z = getCantNews("primerosNPrimos.1", "primerosNPrimos.2",
                "primerosNPrimos.7");
        System.out.println("Capturados para n = " + n + ": " + z);
    }

    public static void main2(int n) {
        Primos p = new Primos();
        List<Long> primos = p.primerosNPrimos(n);
//        System.out.println("último prima para n ="+ n + ": " + primos.get(n - 1));
//        mostrarCantNews();
//        limpiar();
    }

    private static void mostrar(List<Long> primos) {
        for (Long primo : primos) {
            System.out.print(primo + ", ");
        }
    }

    public void calcularYMostrarPrimerosNPrimos(Integer n){
        List<Long> primos = primerosNPrimos(n);
        mostrar(primos);
    }

    private List<Long> primerosNPrimos(Integer n){
        List<Long> primos = getArrayListDeLong(n, "primerosNPrimos.1");
        Integer cantEncontrados = getInteger(0,"primerosNPrimos.2");
        Long indice = getLong(2, "primerosNPrimos.3");
        while (cantEncontrados < n)
        {
            //reseteo new de esPrimo para que se quede con el último
//            limpiar("esPrimo.3");
//            limpiar("esPrimo.7");
            if (esPrimo(indice)){
                primos.add(indice);
                cantEncontrados = getInteger(cantEncontrados + 1,"primerosNPrimos.7");
            }
            indice = getLong(indice + 1, "primerosNPrimos.8" );
        }
        return primos;
    }


    private boolean esPrimo(Long testPrime)
    {
        if(testPrime == 1)
            return false;
        Long test = getLong(2, "esPrimo.3");
    	while ( test < testPrime )
    	{
    		if ( testPrime % test == 0 )
    			return false;
    		test = getLong(test + 1, "esPrimo.7");
    	}
        return true;
    }

}

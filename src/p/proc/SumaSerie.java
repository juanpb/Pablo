package p.proc;

import p.util.UtilFile;

/**
 * Date: 25/12/2008
 * Time: 13:10:37
 */
public class SumaSerie {

    private final static double nueve37 = 9d/37d;
    private final static double diOc37 = 18d/37d;
    private final static int sumarHasta = 100;

    public static void main(String[] args) throws Exception {
        UtilFile.moverContenidoDeDir("C:\\_grabar\\Copia de a", "C:\\_grabar-ver");

//        System.out.println("nueve37 = " + nueve37);
//        System.out.println("diOc37 = " + diOc37);
//        sumar();
    }

    private static void sumar() {
        double acum = nueve37;
        double i = 1;
        for (; i < sumarHasta; i++) {
            double aport = (Math.pow(nueve37 * 2, i)) * nueve37;
            acum += aport;
            //System.out.println("aport = " + aport);
        }
        System.out.println("acum con i = " + i + " =>" + acum);

        for (; i < sumarHasta*2; i++) {
            double aport = (Math.pow(nueve37 * 2, i)) * nueve37;
            acum += aport;
            //System.out.println("aport = " + aport);
        }
        System.out.println("acum con i = " + i + " =>" + acum);

        for (; i < sumarHasta*20; i++) {
            double aport = (Math.pow(nueve37 * 2, i)) * nueve37;
            acum += aport;
            //System.out.println("aport = " + aport);
        }
        System.out.println("acum con i = " + i + " =>" + acum);
    }
}

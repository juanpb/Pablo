package p.dm.trans;

import p.dm.DataSet;
import p.dm.Parser;
import p.dm.Registro;
import p.util.Fechas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: JPB
 * Date: Oct 6, 2008
 * Time: 8:40:36 AM
 */
public class CalcularRangos {

    private static String entrada = "C:\\Documents and Settings\\Administrador\\Escritorio\\TP AA\\cmc.data";
    private static int cantRangos = 10;
    private static int columna = 0;

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();
        DataSet set = Parser.parsear(entrada);
        System.out.println("cant. reg. = " + set.getRegistros().size());
//        List<Rango> rangos = calcular(set.getRegistros(), columna, cantRangos);
//        testearRangos(set.getRegistros(), rangos, columna);
        testearRangos(set.getRegistros(), getRangos(), columna);
//        Imprimir.calcularEImprimirCantXValor(set.getRegistros(), columna);
        long fin = System.currentTimeMillis();
        long ms = (fin - ini);
        System.out.println("Demoró " + Fechas.getTiempo(ms));
    }



    //imprime cant de reg. x rango
    private static void testearRangos(List<Registro> registros, List<Rango> rangos, int columna){
        List<Integer> ords = ordenar(registros, columna);
        Imprimir.imprimirCantXRango(rangos, ords);
    }


    //rangos para probar...
    public  static List<Rango> getRangos() {
        List<Rango> rangos = new ArrayList<Rango>();
//para cant de hijos
//        rangos.add(new Rango(0, 1) );
//        rangos.add(new Rango(2, 2) );
//        rangos.add(new Rango(3, 3) );
//        rangos.add(new Rango(4, 5) );
//        rangos.add(new Rango(6, 16) );

        //edad 2 rangos
//        rangos.add(new Rango(16, 31) );
//        rangos.add(new Rango(32, 49) );

        //edad 3 rangos
//        rangos.add(new Rango(16, 27) );
//        rangos.add(new Rango(28, 36) );
//        rangos.add(new Rango(37, 49) );

        //edad 4 rangos
//        rangos.add(new Rango(16, 25) );
//        rangos.add(new Rango(26, 31) );
//        rangos.add(new Rango(32, 38) );
//        rangos.add(new Rango(39, 49) );

        //edad 5 rangos
//        rangos.add(new Rango(16, 24) );
//        rangos.add(new Rango(25, 29) );
//        rangos.add(new Rango(30, 34) );
//        rangos.add(new Rango(35, 40) );
//        rangos.add(new Rango(41, 49) );

        //edad 6 rangos
//        rangos.add(new Rango(16, 23) );
//        rangos.add(new Rango(24, 27) );
//        rangos.add(new Rango(28, 31) );
//        rangos.add(new Rango(32, 35) );
//        rangos.add(new Rango(36, 42) );
//        rangos.add(new Rango(43, 49) );

        //edad 7 rangos
//        rangos.add(new Rango(16, 23) );
//        rangos.add(new Rango(24, 26) );
//        rangos.add(new Rango(27, 29) );
//        rangos.add(new Rango(30, 33) );
//        rangos.add(new Rango(34, 37) );
//        rangos.add(new Rango(38, 43) );
//        rangos.add(new Rango(44, 49) );

        //edad 8 rangos
//        rangos.add(new Rango(16, 22) );
//        rangos.add(new Rango(23, 25) );
//        rangos.add(new Rango(26, 28) );
//        rangos.add(new Rango(29, 31) );
//        rangos.add(new Rango(32, 34) );
//        rangos.add(new Rango(35, 38) );
//        rangos.add(new Rango(39, 43) );
//        rangos.add(new Rango(44, 49) );

        //edad 9 rangos
//        rangos.add(new Rango(16, 22) );
//        rangos.add(new Rango(23, 25) );
//        rangos.add(new Rango(26, 27) );
//        rangos.add(new Rango(28, 30) );
//        rangos.add(new Rango(31, 33) );
//        rangos.add(new Rango(34, 36) );
//        rangos.add(new Rango(37, 40) );
//        rangos.add(new Rango(41, 44) );
//        rangos.add(new Rango(45, 49) );

        //edad 10 rangos
        rangos.add(new Rango(16, 21) );
        rangos.add(new Rango(22, 24) );
        rangos.add(new Rango(25, 26) );
        rangos.add(new Rango(27, 29) );
        rangos.add(new Rango(30, 32) );
        rangos.add(new Rango(33, 34) );
        rangos.add(new Rango(35, 37) );
        rangos.add(new Rango(38, 41) );
        rangos.add(new Rango(42, 44) );
        rangos.add(new Rango(45, 49) );

        return rangos;
    }

    private static List<Rango> calcular(List<Registro> regs, int atrib, int cantRangos){
        int cantXRango = regs.size() / cantRangos;
        int desde=0;
        int hasta;
        int cont = 0;

        List<Rango> rangos = new ArrayList<Rango>(cantRangos);
        int ind = 0;

        List<Integer> ordenados = ordenar(regs, atrib);

        int cant = ordenados.size();
        for(int i = 0; i < cant; i++) {
            Integer v = ordenados.get(i);
            if (cont == 0)
                desde = v;
            cont++;
            if (cont == cantXRango){
                cont = 0;
                hasta = v;
                Rango r = new Rango(desde, hasta, null);
                rangos.add(r);
                ind++;

                //calculo nueva cant x rango
                for(int j = i+1; j < cant; j++) {
                    v = ordenados.get(j);
                    if (hasta != v){
                        //me fijo cuántos quedan
                        int faltan = cant - j;
                        cantXRango = faltan / (cantRangos-ind);
                        i = j-1;
                        break;
                    }
                }
            }
        }
        return rangos;
    }

    private static List<Integer> ordenar(List<Registro> regs, int atrib) {
        List<Integer> res = new ArrayList<Integer>();
        for(Registro reg : regs) {
            Object o = reg.getAtributos().get(atrib);
            res.add(new Integer(o.toString()));
        }

        Collections.sort(res);
        return res;
    }


}

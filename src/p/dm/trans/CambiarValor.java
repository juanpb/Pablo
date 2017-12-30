package p.dm.trans;

import p.dm.DataSet;
import p.dm.Parser;
import p.util.Fechas;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Date: 11/10/2008
 * Time: 17:01:17
 */
public class CambiarValor {
    private static String n = "cmc_data_fijado_rango_de_hijos5.arff";
    private static String entrada = "C:\\Documents and Settings\\Administrador\\Escritorio\\TP AA\\" + n;
//    private static int cantRangos = 5;
    private static int columna = 0;

    public static void main(String[] args) throws IOException {
                long ini = System.currentTimeMillis();
        DataSet set = Parser.parsear(entrada);
        List<Rango> rangos = CalcularRangos.getRangos();
        AplicarRango.aplicar(set.getRegistros(), rangos, columna);

        //agrego una línea con los nuevos rangos
        String x = "Columna " + (columna+1) + ", valores: {" + Rango.getClasesCVS(rangos) + "}";
        set.addOtraLinea(x);
        String s = grabar(set);
        long fin = System.currentTimeMillis();
        long ms = (fin - ini);
        System.out.println("Archivo salida = " + s);
        System.out.println("Demoró " + Fechas.getTiempo(ms));
    }

    private static String grabar(DataSet set) throws IOException {
        return Parser.grabar(set, new File(entrada));
    }

}

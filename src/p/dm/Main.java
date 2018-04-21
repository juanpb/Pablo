package p.dm;

import p.util.Constantes;
import p.util.Util;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    private static String entrada = "D:\\P\\_env\\DB\\partida\\a.txt0";
    private static String archSalida = "D:\\P\\_env\\DB\\partida\\a_txt0.sql";

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();
        DataSet set = Parser.parsear(entrada);
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró en parsear " + seg + " segundos");
        salidaSQL(set);

    }

    private static void salida(DataSet ds){
        Filtros.sacarComillas(ds);
        Filtros.trim(ds);
        StringBuffer sal = GeneradorSalida.salida(ds, Constantes.TAB_CHARACTER+"");
        Util.pegarEnElPortapapeles(sal.toString());
        JOptionPane.showMessageDialog(null, "Se pegó la salida en el portapapeles");
    }


    private static void salidaSQL(DataSet ds) throws IOException {
        long ini = System.currentTimeMillis();
        Filtros.sacarComillas(ds);
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró en sacar com " + seg + " segundos");
        ini = fin;
        Filtros.trim(ds);
        fin = System.currentTimeMillis();
        seg = (fin - ini)/1000;
        System.out.println("Demoró en trim " + seg + " segundos");

        ini = fin;
        StringBuffer sal = GeneradorSalida.salidaSQL(ds);
        fin = System.currentTimeMillis();
        seg = (fin - ini)/1000;
        System.out.println("Demoró en generar sal " + seg + " segundos");

        ini = fin;
        FileOutputStream os = new FileOutputStream(archSalida);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        bos.write(sal.toString().getBytes());
        fin = System.currentTimeMillis();
        seg = (fin - ini)/1000;
        System.out.println("Demoró en generar grabar " + seg + " segundos");

        JOptionPane.showMessageDialog(null, "Se generó la salida: " + archSalida);
    }

    private static void analizar(DataSet set){
        List<Registro> regs = set.getRegistros();
        int dist = 0;

        int cant = regs.get(0).getAtributos().size();
        for(Registro r : regs) {
            int cant2 = r.getAtributos().size();
            if (cant != cant2){
                dist++;
            }
        }
        System.out.println("cant = " + cant);
        System.out.println("dist = " + dist);
    }


    
}

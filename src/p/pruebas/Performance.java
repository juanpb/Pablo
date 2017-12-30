package p.pruebas;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * User: JPB
 * Date: 20/05/17
 * Time: 12:08
 */
public class Performance {
    private  static long anterior;
    private  static long inicial;
    private static DecimalFormat decimalFormat;

    public static void main(String[] args) {
        prueba();
    }

    private static void prueba() {
        anterior = System.currentTimeMillis();
        inicial = System.currentTimeMillis();
        BigDecimal contadorCiclos = new BigDecimal(0);
        BigDecimal uno = new BigDecimal(1);
        BigDecimal dos = new BigDecimal(2);
        int potencia = 1;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String pattern = "#,##0.###";
        decimalFormat = new DecimalFormat(pattern, symbols);
        
        while (true) {
            contadorCiclos = contadorCiclos.add(uno);
            BigDecimal valorPotencia = dos.pow(potencia);
            if (contadorCiclos.compareTo(valorPotencia) > 0){
                potencia++;
                log(potencia, valorPotencia);
            }
        }
    }

    private static void log(int potencia, BigDecimal valor) {
        String hora = "";
        String horaTotal = "";
        long actual = System.currentTimeMillis();
        long ms = (actual - anterior);
        long msTotal = (actual - inicial);

        if (ms < 1000){
            hora +="0." + ms;// + " ms.";
        }else {
            long seg = ms / 1000;
            String min = "";
            if (seg > 60){
                min = (seg /60) + ":";
                seg = seg % 60;
            }
            hora += min + seg + "." + ms % 1000 ;
        }

        if (msTotal < 1000){
            horaTotal +="0." + msTotal;
        }else {
            long seg = msTotal / 1000;
            String min = "";
            if (seg > 60){
                min = (seg /60) + ":";
                seg = seg % 60;
            }
            horaTotal += min + seg + "." + msTotal % 1000 ;
        }

//        System.out.println(potencia - 1 + "->" + potencia + "("+ valor + "): " + hora);
        System.out.println(decimalFormat.format(potencia) + "("+ decimalFormat.format(valor) + "): " + hora + " - Total: " + horaTotal);
        anterior = actual;
    }
}

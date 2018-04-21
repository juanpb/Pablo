package p.proc;

import p.util.Constantes;
import p.util.Util;
import p.util.UtilFile;

import java.io.IOException;

/**
 * User: p_benedetti
 * Date: 15/03/18
 * Time: 11:39
 */
public class Word2Excel {
    private static final String NUEVA_FILA = "---NL---";
    private static final String NUEVA_COLUMNA = "--NC--";

    private static final String fileIn = "D:\\Seguridad\\tmp\\in.txt";
    private static final String fileOut = "D:\\Seguridad\\tmp\\out.txt";

    public static void main(String[] args) throws IOException {
        Word2Excel x = new Word2Excel();
        x.daleGas();
    }

    private void daleGas() throws IOException {
        String contenidoAsString = UtilFile.getContenidoAsString(fileIn);
        String[] filas = contenidoAsString.split(NUEVA_FILA);
        StringBuilder salida = new StringBuilder();
        for (String fila : filas) {
            if (!fila.trim().equals("")){
                String[] split = fila.split(NUEVA_COLUMNA);
                agregarFila(salida, split);
                salida.append(Constantes.NUEVA_LINEA);
            }
        }

        System.out.println("Salimos:");
        System.out.println(salida);
        Util.pegarEnElPortapapeles(salida.toString());
    }

    private void agregarFila(StringBuilder salida, String[] split) {
        for (String s : split) {
            salida.append("'" + s.trim());
            salida.append(Constantes.TAB_CHARACTER);
        }

    }


}

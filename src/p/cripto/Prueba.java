package p.cripto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: JPB
 * Date: 03/01/13
 * Time: 14:39
 */
public class Prueba {

    public static void main(String[] args) throws IOException {
        String f = "D:\\P\\capacitaciones\\Criptografía\\prácticas\\1\\c1.csu";

        FileInputStream fis = new FileInputStream(f);
        int tam = fis.available();
        byte[] datos = new byte[tam];
        fis.read(datos);
        Map<Character,Integer> frecuenciasCaracteres = Util.getFrecuenciasCaracteres(datos);
        System.out.println(frecuenciasCaracteres);
        List<Character> ord = Util.ordenar(frecuenciasCaracteres);
        StringBuilder sb = new StringBuilder();
        for (Character c : ord) {
            Integer cant = frecuenciasCaracteres.get(c);
            sb.append(c).append("=").append(cant).append(", ");
        }
        System.out.println(sb);
        System.out.println(ord);

        int indice = 0;
        String salida = new String(datos);

        List<Character> ordenProbaCastellano = Util.ordenProbaCastellano;
        for (Character cif : ord) {
            //los símbolos quedan cómo están
            if (Character.isLetter(cif)){
                Character plano = ordenProbaCastellano.get(indice);
                indice++;
                salida = salida.replaceAll(cif+"", plano+"");
            }
        }

        System.out.println("Entrada: ");
        System.out.println(new String(datos));
        System.out.println("salida: ");
        System.out.println(salida);
    }
}

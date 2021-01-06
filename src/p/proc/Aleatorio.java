package p.proc;

import java.io.*;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.Random;

/**
 * Created by Juan on 28/5/2020.
 */
public class Aleatorio {
    private static long cant = 10000000;
    private static String salida = "C:\\Datos\\P\\varios\\comprobantes + docs\\a cifr\\sal";

    public static void main(String[] args) throws IOException {
//        BitSet bs = generar();
//        grabar(bs);
        generarNS();
    }

    private static BitSet generar() {
        BitSet bs = new BitSet();
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];

        for (int i = 0; i <cant; i++) {
            bs.set(i, random.nextBoolean());
        }

        return bs;
    }

    private static void generarNS() throws IOException {
        double x[] = new double[(int)cant];

        for (int i = 0; i <cant; i++) {
            x[i] = Math.random();
        }

        ObjectOutputStream oas = new ObjectOutputStream(new FileOutputStream(salida));
        oas.writeObject(x);
        oas.close();
    }

    private static void grabar(BitSet bs) throws IOException {
        ObjectOutputStream oas = new ObjectOutputStream(new FileOutputStream(salida));
        oas.writeObject(bs.toByteArray());
        oas.close();
    }
}

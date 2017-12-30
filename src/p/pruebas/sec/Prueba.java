package p.pruebas.sec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * User: JPB
 * Date: 9/7/11
 * Time: 1:38 PM
 */
public class Prueba {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new Prueba();
    }

    public Prueba() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("DES");
        SecretKey desKey = keygen.generateKey();
        System.out.println("desKey.getFormat() = " + desKey.getFormat());
        System.out.println("desKey.getAlgorithm() = " + desKey.getAlgorithm());
        System.out.println("desKey.getClass() = " + desKey.getClass());
        System.out.println("desKey.getEncoded() = " + desKey.getEncoded());
    }
}

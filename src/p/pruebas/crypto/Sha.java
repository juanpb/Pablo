package p.pruebas.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: JPB
 * Date: 27/03/17
 * Time: 17:04
 */
public class Sha {

    public static String toSha1(String xxx) throws NoSuchAlgorithmException {

        MessageDigest sha = MessageDigest.getInstance("SHA"); // Inicializa SHA-1
        byte[] digest = sha.digest(xxx.getBytes());
        return new String(digest);
    }

    public static void main(String [] args) throws NoSuchAlgorithmException {
        if (args.length != !)
            System.out.println("Pasar el string para calcular el sha");
        else{
            System.out.println("SHA1: " + toSha1(args[0]));
        }
    }

    public static void main2(String [] args)
    {
        long ini = System.currentTimeMillis();
        //declarar funciones resumen
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5"); // Inicializa MD5
            MessageDigest sha = MessageDigest.getInstance("SHA"); // Inicializa SHA-1

            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < 5000000; i++) {
                String x = i + "";
//                byte[] digest = sha.digest(x.getBytes());
                byte[] digest = md5.digest(x.getBytes());
                String d = new String(digest);
                String viejo = map.put(d, x);
                if (viejo != null){
                    System.out.println("viejo = " + viejo);
                    System.out.println("d = " + d);
                }
            }

        }
        //declarar funciones resumen
        catch(java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("listo");

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1;
        System.out.println("Demoró " + seg + " ms");

    }

}
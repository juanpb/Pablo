package p.pruebas.crypto;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * User: JPB
 * Date: 27/03/17
 * Time: 17:04
 */
public class Sha {

    public static void main(String [] args)
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
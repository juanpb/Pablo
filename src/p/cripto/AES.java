package p.cripto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

/**
 * User: JPB
 * Date: 09/08/17
 * Time: 09:57
 */
public class AES {

    // Definición del tipo de algoritmo a utilizar (AES, DES, RSA)
    private final static String alg = "AES";
    private final static String algDES = "DES";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "AES/CBC/PKCS5Padding";
    private final static String cIECB = "AES/ECB/NoPadding";
    private final static String cIDES = "DES/ECB/NoPadding";
    private final static String cIDES2 = "DES/ECB/PKCS5Padding";

    public static byte[] encryptDES(String key, byte[] cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cIDES2);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algDES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(cleartext);
        return encrypted;
    }

    public static String encryptDES(String key, String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cIDES);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algDES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return new String(encodeBase64(encrypted));
    }

    public static String decryptDES(String key, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(cIDES);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algDES);
        byte[] enc = decodeBase64(encrypted);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(enc);
        return new String(decrypted);
    }

    public static String encryptECB(String key, String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cIECB);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return new String(encodeBase64(encrypted));
    }

    public static String decryptECB(String key, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(cIECB);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);
        byte[] enc = decodeBase64(encrypted);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(enc);
        return new String(decrypted);
    }


    public static String encrypt(String key, String iv, String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return new String(encodeBase64(encrypted));
    }

    /**
     * Función de tipo String que recibe una llave (key), un vector de inicialización (iv)
     * y el texto que se desea descifrar
     * @param key la llave en tipo String a utilizar
     * @param iv el vector de inicialización a utilizar
     * @param encrypted el texto cifrado en modo String
     * @return el texto desencriptado en modo String
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException
     */
    public static String decrypt(String key, String iv, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        byte[] enc = decodeBase64(encrypted);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(enc);
        return new String(decrypted);
    }

    public static void main2(String[] args) throws Exception {
        String key  = "92AE31A79FEEB2A3"; //llave
        String key2 = "12AE31A79FEEB2A3"; //llave
        String keyDES  = "01234567"; //llave
        String keyDES2 = "01234567"; //llave
        String iv = "9123456789ABCDEF"; // vector de inicialización
        String cleartext = "hola1234hola1234hola1234hola1234hola1234hola1234hola1234hola1234";
        System.out.println("cleartext.getBytes().length = " + cleartext.getBytes().length);
        System.out.println("Texto encriptado: "+ encrypt(key, iv,cleartext));
        System.out.println("Texto desencriptado: "+ decrypt(key, iv, encrypt(key, iv, cleartext)));
        System.out.println("Texto desencriptado: "+ decrypt(key, iv, encrypt(key, iv,cleartext)));
        System.out.println("ECB:");
        System.out.println("Texto encriptado: " + encryptDES(keyDES, cleartext));
        System.out.println("Texto desencriptado: " + decryptDES(keyDES2, encryptECB(key, cleartext)));
    }

    public static void main(String[] args) throws Exception {
        String keyDES  = "01234567"; //llave
        FileInputStream fis = new FileInputStream("D:\\Seguridad\\Organigrama.bmp");
        int available = fis.available();
        byte[] plano = new byte[available];
        fis.read(plano);
        byte[] cifrado = encryptDES(keyDES, plano);
        FileOutputStream fos = new FileOutputStream("D:\\Seguridad\\Organigrama2.bmp");
        fos.write(plano, 0, 60);
        fos.write(cifrado, 60, cifrado.length-60);
    }
}

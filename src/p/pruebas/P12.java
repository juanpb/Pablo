package p.pruebas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * User: P_BENEDETTI
 * Date: 25/4/2019
 * Time: 10:45
 */
public class P12 {
    private static final int CONT_MAX = 99999;
    private static String p12File = "D:\\P\\_env\\miFirma.p12";
    public static void main(String[] args) throws Exception {

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        long ini = System.currentTimeMillis();
        buscarPass(p12);

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");
        /*p12.load(new FileInputStream(p12File), "password".toCharArray());
        Enumeration<String> e = p12.aliases();
        while (e.hasMoreElements()) {
            String alias = e.nextElement();
            X509Certificate c = (X509Certificate) p12.getCertificate(alias);
            Principal subject = c.getSubjectDN();
            String subjectArray[] = subject.toString().split(",");
            for (String s : subjectArray) {
                String[] str = s.trim().split("=");
                String key = str[0];
                String value = str[1];
                System.out.println(key + " - " + value);
            }
        }    */
    }

    private static void buscarPass(KeyStore p12)  {
        for (int i = 0; i < CONT_MAX; i++) {
            try {
                p12.load(new FileInputStream(p12File), "password".toCharArray());
                ver(p12);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    private static void ver(KeyStore p12) throws KeyStoreException {
        Enumeration<String> e = p12.aliases();
        while (e.hasMoreElements()) {
            String alias = e.nextElement();
            X509Certificate c = (X509Certificate) p12.getCertificate(alias);
            Principal subject = c.getSubjectDN();
            String subjectArray[] = subject.toString().split(",");
            for (String s : subjectArray) {
                String[] str = s.trim().split("=");
                String key = str[0];
                String value = str[1];
                System.out.println(key + " - " + value);
            }
        }
    }
}

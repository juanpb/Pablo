package p.pruebas;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: JPB
 * Date: 5/19/11
 * Time: 8:07 AM
 */
public class BajarHTML {
    private static Logger log = Logger.getLogger(BajarHTML.class.getName());

    private static final List<String> ipOtros = new ArrayList<String>();
    private static final List<String> excluir = new ArrayList<String>();

    private static final Set<String> ipExcluir = new HashSet<String>();

    private static void cargarExclusiones(){
        excluir.add("<title>Apache Tomcat");
        excluir.add("<title>Welcome to JBoss");
        excluir.add("na para probar la instalaci");
        excluir.add("<title>Sun Java System Application </title>");
        excluir.add("Portal de autoservicio de Virtual Machine Manager");
    }

    public static void main(String[] args) {
        long ini = System.currentTimeMillis();
        String url = "http://yahoo.com";
//        List<String> ipAProbar = new ArrayList<String>();
//        ipAProbar.add("10.204.130.18");
//        ipAProbar.add("10.204.130.19");
//        ipAProbar.add("10.204.130.140");
//        ipAProbar.add("10.204.130.141");
//        ipAProbar.add("10.204.130.167");
//        ipAProbar.add("10.204.130.168");
//        ipAProbar.add("10.204.130.169");
//        ipAProbar.add("10.204.130.170");
        ipExcluir.add("10.204.130.245");
//        for(String ip : ipAProbar) {

        //Listo:
        //"10.203."
        //...
        //10.209.
        for(int z=223; z<256; z++) {
            String b2 = "10." + z;
            for(int j=0;j < 158; j++) {
                if (z == 223 && j == 0)
                    j = 65;
                String b3 = b2 + "." + j;//corre desde 158   10.233.155.200
                for(int i=0; i < 256; i++){
                    String b4 = b3 + "." + i;
                    if (i % 100 == 0){
                        System.out.println(b4);
                    }

                    if (!ipExcluir.contains(b4)) {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(b4, 8080));
                        String s = dameHTML(url, proxy);
                        if (s != null){
                            System.out.println("Algo en ip = " + b4);
                            System.out.println(s);
                            System.out.println("__________________________________________");
                        }
                    }
                }
            }
        }


        System.out.println("Ip otros: ");
        for(String x : ipOtros) {
            System.out.println("    " + x);
        }
        System.out.println("___________________________");


        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");
    }

//    public static void main2(String[] args) {
//        String url = "http://yahoo.com";
//
//
////        for(int j=130;j < 140; j++) {
//        for(int j=130;j < 137; j++) { //hasta 10.204.130.200
//                String dest = "10.204." + j;
//                for(int i=0; i < 256; i++){
//                    String host = dest + "." + i;
//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, 8080));
//                    String s = dameHTML(url, proxy);
//                    if (s != null){
//                        System.out.println("FUNCIONA: " + host);
//                    }
//                    if (i % 100 == 0){
//                        System.out.println(host);
//                    }
//                }
//        }
//    }

      /**
     * Devuelve el HTML de una página web
     * @param direccionUrl Página web a descargar
     * @return string con el código HTML de la página
     */
    public static String dameHTML(String direccionUrl, Proxy proxy) {
        try {
            URL dir = new URL(direccionUrl);
            URLConnection yc = dir.openConnection(proxy);

            // Aquí agregamos unas cabeceras a la petición para "saltar" la seguridad de algunos sitios
            // así piensan que somos un navegador.
            yc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.0.10) Gecko/2009042316 Firefox/3.0.10");
            yc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            yc.addRequestProperty("Accept-Language", "es-es,es;q=0.8,en-us;q=0.5,en;q=0.3");
            yc.setConnectTimeout(3000);

            //Creamos el objeto con el que vamos a leer
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));


            String inputLine;
            String retorno = "";
            while ((inputLine = in.readLine()) != null) {
                for(String s : excluir) {
                    if (inputLine.contains(s)){
                        ipOtros.add(proxy.address().toString());
                        return null;
                    }
                }

                retorno += inputLine + "\n";
            }


            in.close();

            return retorno;
        } catch (Exception ex) {
            //Dejamos constancia en el log si hubo alguna excepción

            //log.error("Error al leer HTML de " + direccionUrl, ex);
        }
        //Si llegamos aquí hubo una excepción y devolvemos null
        return null;
    }
}

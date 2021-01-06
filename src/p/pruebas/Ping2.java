package p.pruebas;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.Fechas;
import p.util.UtilFile;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Sep 8, 2010
 * Time: 6:27:26 PM
 */
public class Ping2 {
    private static final Logger logger = Logger.getLogger(Ping2.class);
    private static final boolean REINICIAR = false;
    private static final int TIEMPO_PARA_REINICIAR_EN_MIN = 20;

//    Best IP Addresses to Ping Host 	IP Address
//    google-public-dns-a.google.com. 	8.8.8.8
//    google-public-dns-b.google.com 	8.8.4.4
//    ns1.telstra.net 	139.130.4.5

    public static void main(String[] args) throws IOException {
        String file = "D:\\OneDrive - Telefonica\\Seguridad\\proyectos\\WAF\\lista.txt";
        List<String> apl = UtilFile.getArchivoPorLinea(file);
        System.out.println("Nombre" + Constantes.TAB_CHARACTER + "IP" + Constantes.TAB_CHARACTER + "Se conecta");
        for (String s : apl) {
            probar(s);
        }

    }

    private static void controlarConexion(){
        List<String> historial = new ArrayList<String>();
        logger.debug("Empieza verificación");
        boolean isInternetReachable = isInternetReachable();
        long ini = System.currentTimeMillis();
        String estado = isInternetReachable? "Conectado":"Sin conexión";

        logger.debug("Estado de la conexión: " + estado);

        boolean estadoAnterior = isInternetReachable;
        int cont = 0;

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                //si estaba desconectado verifico más rápido
                if (estadoAnterior)
                    Thread.sleep(10000);
                else
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }

            isInternetReachable = isInternetReachable();
            if (isInternetReachable != estadoAnterior){
                long fin = System.currentTimeMillis();
                String msj =  estadoAnterior ? "Conectado":"Sin conexión";
                msj += " [" + Fechas.getDDMMAAAAHHMMSS(ini) + " - " + Fechas.getDDMMAAAAHHMMSS(fin) +  "]";
                msj += " (" + duracion(ini, fin) +  ")";
                logger.info(msj);
                historial.add(msj);
                ini = fin;
                estadoAnterior = isInternetReachable;
                imprimir(historial);
            }
            cont++;
            if (cont % 5000 == 0){
                logger.debug("cont = " + cont + ", estado: " + (estadoAnterior ? "Conectado":"Sin conexión"));
            }

            //noinspection PointlessBooleanExpression
            if (!isInternetReachable && REINICIAR){
                long ahora = System.currentTimeMillis();
                int minutosSinConexion= (int)((ahora - ini)/1000)/60;
                if (minutosSinConexion >= TIEMPO_PARA_REINICIAR_EN_MIN ){
                    String msj = "Se reiniciará la PC en 5 minutos. Cancelar con 'shutdown /a'" + new Date();
                    mostrarAlerta(msj);
                    logger.error(msj);
                    esperar3Minutos();
                    //última oportunidad
                    if (!isInternetReachable())
                        reiniciarPCEn2Min();
                    else
                        logger.info("Zafamos del reinicio");
                }
            }
        }
    }


    private static void esperar3Minutos() {
        try {
            Thread.sleep(3 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void reiniciarPCEn2Min() {
        Runtime r=Runtime.getRuntime();
        try {
            r.exec("shutdown -r -f -t 120"); //-r reinicia, -f fuerza, -t tiempo en segundos
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }


    private static void mostrarAlerta(String msj) {
        JDialog dlg = new JDialog((JFrame)null, msj);
        dlg.setSize(400, 200);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setVisible(true);
    }

    private static String duracion(long ini, long fin) {
        long dur = (fin - ini) / 1000;       // en seg
        String min = "0";
        String hor = "0";
        String seg = trim(dur % 60);
        long resto = (dur - dur % 60) / 60; // en min
        if (resto > 0){
            min = trim(resto % 60);
            hor = trim(resto / 60);
        }
        return hor + ":" + min + ":" + seg + "";
    }

    private static String trim(long x) {
        if (x < 10)
            return "0" + x;
        else
            return "" + x;
    }

    private static void imprimir(List<String> historial) {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        for(String s : historial) {
            if (s.contains("Conectado"))
                logger.info(s.substring(s.indexOf("Conectado")));
        }
        for(String s : historial) {
            if (s.contains("Sin con"))
                logger.info(s.substring(s.indexOf("Sin con")));
        }
        logger.info("------------------------------------------------");
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }


    public static void main2(String[] args) throws IOException {
        long min = Long.MAX_VALUE;
        long max = 0;
        long cont = 0;
        //noinspection InfiniteLoopStatement
        while (true){
            long ini = System.currentTimeMillis();
            boolean b = isInternetReachable();
            long fin = System.currentTimeMillis();
            long ms = (fin - ini);
            if (ms < min){
                min = ms;
                System.out.println("Min: " + min);
            }
            if (ms > max){
                max = ms;
                System.out.println("Max: " + max);
            }
            cont++;
            if(cont % 100 == 0){
                System.out.println("cont = " + cont);
            }
        }
    }

    public static boolean isInternetReachable()
    {
        HttpURLConnection urlConnect = null;
        try {
            //make a URL to a known source
            URL url = new URL("200-081-036-043.wireless.movistar.net.ar");

            //open a connection to that source
            urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
//            logger.error(e.getMessage());
            return false;
        }
        catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
        finally {
            urlConnect.disconnect();
        }
        return true;
    }

    private static void probar(String name) throws IOException {
        InetAddress in;
        String nameOriginal = name;
        //Definimos la ip de la cual haremos el ping
//        in = InetAddress.getByName("yahoo.com.ar");
        try {


            String hostAddress = null;
            try {
                in = InetAddress.getByName(name);
                hostAddress = in.getHostAddress();
            } catch (Exception e) {
                //si falla prueba agregándelo o sacándole el prejo www
                if (name.toLowerCase().startsWith("www."))
                    name = name.substring(4);
                else
                    name = "www." + name;
                in = InetAddress.getByName(name);
                hostAddress = in.getHostAddress();
            }

            System.out.println(in.getHostName() + Constantes.TAB_CHARACTER + "'" + hostAddress +
                    Constantes.TAB_CHARACTER + hostAddress);
                  /*
            if(in.isReachable(1000)){
                System.out.println("OK");}
            else{
                System.out.println("Time out");
            }      */
        } catch (UnknownHostException e) {
            /*
            System.out.println(nameOriginal + Constantes.TAB_CHARACTER + "NA" +
                    Constantes.TAB_CHARACTER + "NA" );*/
            System.out.println(nameOriginal + Constantes.TAB_CHARACTER + "NA"  );
        }
    }

    private static void probar2(String name) throws IOException {
        InetAddress in;
        //Definimos la ip de la cual haremos el ping
//        in = InetAddress.getByName("yahoo.com.ar");
        in = InetAddress.getByName(name);
//        in = InetAddress.getByName("10.204.137.193");
        //Definimos un duracion en el cual ha de responder
        System.out.println("Name: " + in.getHostName());
        System.out.println("Addr: " + in.getHostAddress());

        if(in.isReachable(10000)){
            System.out.println("Responde OK");}
        else{
            System.out.println("No responde: Time out");
        }
    }

//    private void probar(String ip) throws IOException {
//        byte[] ip1 = getIP(ip);
//        InetAddress in;
//        //Definimos la ip de la cual haremos el ping
////        in = InetAddress.getByName("yahoo.com.ar");
//        in = InetAddress.getByAddress(ip1);
////        in = InetAddress.getByName("10.204.137.193");
//        //Definimos un duracion en el cual ha de responder
//        System.out.println("Name: " + in.getHostName());
//        System.out.println("Addr: " + in.getHostAddress());
//
//        if(in.isReachable(10000)){
//            System.out.println("Responde OK");}
//        else{
//            System.out.println("No responde: Time out");
//        }
//    }

    //dado ip= 10.1.2.3 devuelve byte[] res = [10, 1, 2, 3]
    private byte[] getIP(String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".", false);
        byte[] res = new byte[4];
        res[0] = Byte.parseByte(st.nextToken());
        res[1] = Byte.parseByte(st.nextToken());
        res[2] = Byte.parseByte(st.nextToken());
        res[3] = Byte.parseByte(st.nextToken());
        return res;
    }

    public void prueba2() {
        //10.204.139.0 al 4

//        String dest = "10.204.137.";
//        String dest = "10.204.138.";
//        String dest = "10.204.139.";
        String etat = "isNotAlive";
        try{
            Runtime runtime = Runtime.getRuntime();
            Process pr;
            InputStream in;// = pr.getInputStream();
            BufferedReader din;// = new BufferedReader(new InputStreamReader(in));

            for(int j=137;j < 140; j++) {
                String dest = "10.204." + j;
                for(int i=0; i < 256; i++){
                    String host = dest + "." + i;
                    boolean seguir = true;
                    pr = runtime.exec("ping " + host);
                    in = pr.getInputStream();
                    din = new BufferedReader(new InputStreamReader(in));
                    while (seguir){
                        if (din.ready()){
                            String x = din.readLine();
                            if (!x.equals("")){
                                if (!x.startsWith("Request timed out.")
                                        && !x.contains("Packets: Sent = 4, Received = 0, Lost = 4 (100% loss)")
                                        && !x.contains("Approximate round trip times in milli-seconds")
                                        && !x.contains("Pinging ")
                                        && !x.contains("Minimum = 0ms, Maximum =  0ms, Average =  0ms") )
                                    System.out.println("din.readLine() = " + x);
                                if (x.contains("Minimum ="))
                                    seguir = false;
                            }
                        }

                        Thread.sleep(100);
                    }
                }
            }


        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}


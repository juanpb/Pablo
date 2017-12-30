package p.web;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class BajaPaginas {

    private static final Logger logger = Logger.getLogger(BajaPaginas.class);

    public static void main(String [] arg) throws Exception{

//        File file = new File("C:\\Pablo\\subd\\yo.txt");
//        boolean b  = file.mkdirs();
//        System.out.println("listo: " + b);
//        String pUrl = "http://www.google.com";
//        int desde = pUrl.indexOf(".");
//        int hasta = pUrl.indexOf(".", desde + 1);
//        String nombreHtml = pUrl.substring(desde + 1, hasta);
//        System.out.println("web: " + nombreHtml);
        BajaPaginas.bajar("http://oink.me.uk/browse.php?page=1", "C:\\");
    }

    public static void mains(String [] arg){
        try{
            String urlS = "http://www.clarin.com/diario/hoy/index_diario.html";
            //String urlS = "http://www.google.com/images/hp2.gif";
//            Socket socket = new Socket(url, 80);
//            InputStream is = socket.getInputStream();
//            DataInputStream dis = new DataInputStream(is);
//            int ava = dis.available();
//            byte[] bytes = new byte[ava];
//            dis.read(bytes);
//            String str = new String(bytes);
            URL url = new URL(urlS);
            URLConnection urlc = url.openConnection();
            String x = urlc.getContentEncoding();
            System.out.println(x);
            InputStream is = urlc.getInputStream();
//            DataInputStream dis = new DataInputStream(is);
            BufferedInputStream bis = new BufferedInputStream(is);
            File file = new File("C:\\xxx.html");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            while (bis.available() > 0){
                int a = bis.available();
                System.out.println(a);
                byte[] b = new byte[a];
                bis.read(b);
                fos.write(b);
            }
            bis.close();
            fos.close();
            System.out.println("listo");
//            int ava = dis.available();
//            while (true){
//                String xx = dis.readLine();
//                System.out.println(xx);
//            }

            //System.out.println(ava);
//            byte[] bytes = new byte[1000000];
//            dis.read(bytes);
//            x = new String(bytes);
//            System.out.println(x);
//            System.out.println("99999999999999999999999999999999999999999");

        }catch(Exception e ){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Guarda en el directorio indicado la página 'pUrl' con las fotos
     */
    public static void bajar(String pUrl, String pGuardarEnDirectorio)
            throws Exception{
        bajar(pUrl, pGuardarEnDirectorio, false);
    }

    public static void bajar(String pUrl, String pGuardarEnDirectorio,
                        boolean pSinFotos) throws Exception{
        Vector fotos = new Vector();

        File directorio = new File(pGuardarEnDirectorio);
        directorio.mkdirs();
        int desde = pUrl.indexOf(".");
        int hasta = pUrl.indexOf(".", desde + 1);
        String nombreHtml = pUrl.substring(desde + 1, hasta);

        String tem;
        if (pGuardarEnDirectorio.endsWith("\\"))
            tem = pGuardarEnDirectorio + nombreHtml;
        else
            tem = pGuardarEnDirectorio + "\\" + nombreHtml;
        File fileHmtl = new File(tem + ".html");
        fileHmtl.createNewFile();

        URL url = new URL(pUrl);
        URLConnection urlc = url.openConnection();
        String pass = "***";
        String c = "uid=***; pass=" + pass ;
        urlc.setRequestProperty("Cookie", c);
        InputStream is = urlc.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        FileOutputStream fos = new FileOutputStream(fileHmtl);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] b = new byte[2048];
        while (bis.read(b, 0, 2048) > 0){
            /*
            if (!pSinFotos){
                String str = new String(b);
                int d = str.indexOf("<img");
                while (d > 0){
                    int desd, hast;
                    int d2 = str.indexOf("\"", d);
                    int d3 = str.indexOf("'", d);
                    if (d3 < 0 || d2 > 0 && d2 < d3)
                        desd = d2 + 1;
                    else
                        desd = d3 + 1;

                    d2 = str.indexOf("\"", desd);
                    d3 = str.indexOf("'", desd);
                    if (d3 < 0 || d2 > 0 && d2 < d3)
                        hast = d2;
                    else
                        hast = d3;

                    String img = str.substring(desd, hast);
                    fotos.add(img);
                    d = str.indexOf("<img", hast);
                }
            }*/
            fos.write(b);
        }
        bis.close();
        fos.close();
        System.out.println("listo");

    }

    public static void bajar(String pUrl, String pGuardarEnDirectorio,
                        int pCantNiveles){

    }
}
package p.aplic;

import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

public class Iniciar implements Constantes{

    private static String ARCHIVO_LOG = "miPc.log";

    public static void main(String[] args) throws Exception{
        hacerLog();
//        String archAIniciar = "J:\\Java\\ProyectosJava\\Pablo\\bats\\Iniciar.txt"; //args[0];
        if (args.length > 0){
            String archAIniciar = args[0];
            System.out.println("Archivo a parsear: " + archAIniciar);
            File file = new File(archAIniciar);
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            while(lnr.ready()){
                String linea = lnr.readLine();
                ejecutar(linea);
            }
            lnr.close();
        }
//        borrar();
        System.exit(0);
    }

    private static void ejecutar(String linea) throws Exception{
        if (linea.startsWith("//") || linea.length() == 0)
            return;
        int i = linea.indexOf("-");
        int seg;
        String exe = "";
        if (i > 0){
            seg = Integer.parseInt(linea.substring(0, i));
            exe = linea.substring(i + 1).trim();
            System.out.println("Se abrirá " + exe + " en " + seg + " segundos. ");
        }
        else{
            seg = Integer.parseInt(linea.trim());
            System.out.println("Demora de " + seg + " segundos. ");
        }
        Thread.sleep(seg * 1000);
//        i = exe.indexOf("PPP:");
//        if (i > 0){
//            String param = exe.substring(i + 4);
//            String[] params = new String[1];
//            params[0] = param;
//            Runtime.getRuntime().exec(exe, params);
//        }
//        else
        try{
            if (!exe.equals(""))
                Runtime.getRuntime().exec(exe);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

//    private static void borrar(){
//        File f = new File("C:\\Documents and Settings\\Pablo\\Favoritos\\From ICQ");
//        f.delete();
//    }

    private static void hacerLog() throws Exception{
        StringBuilder sb = new StringBuilder(new java.util.Date().toString());
        sb.append(NUEVA_LINEA);
        UtilFile.logEnUserDir(ARCHIVO_LOG, sb);
    }
}
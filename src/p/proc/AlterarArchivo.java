package p.proc;

import java.io.*;

/**
 * User: Administrador
 * Date: 16/12/2007
 * Time: 17:23:54
 */
public class AlterarArchivo {
    public enum Metodos { INVERTIR}

    public static void main(String[] args) throws IOException {
        String ent = "D:\\P\\_env\\_MP3_\\google.earth.pro.v4.0.xxxx.generic.patch.exe.inv";
        String sal = "D:\\P\\_env\\_MP3_\\google.earth.pro.v4.0.xxxx.generic.patch.exe2";
        alterar(ent, sal, Metodos.INVERTIR);
    }

    public static void alterar(String archEntrada, String salida, Metodos metodo)
            throws IOException {
        switch (metodo){
            case INVERTIR:
                invertir(archEntrada, salida);
                break;
        }

    }

    private static void invertir(String archEntrada, String salida)
            throws IOException {
        FileInputStream ent = new FileInputStream(new File(archEntrada));
        BufferedInputStream bis = new BufferedInputStream(ent);
        File sal = new File(salida);
        FileOutputStream out = new FileOutputStream(sal);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        byte[] b = new byte[bis.available()];
        bis.read(b);
        for (int i = 0; i < b.length; i++) {
            bos.write(b[b.length-1-i]);
        }
        bos.flush();
        bos.close();
    }
}

package p.pruebas;

import java.io.*;

public class RenombrarRecursivo {
    static int z = 0;
    String _raiz = "C:\\My Intranet\\";

    public RenombrarRecursivo() {
        init();
    }
    public static void main(String[] args) {
        RenombrarRecursivo renombrarRecursivo1 = new RenombrarRecursivo();
    }

    void init(){
        File f = new File(_raiz);
        File[] fs = f.listFiles();
        for (int i = 0; i < fs.length; i++) {
            hacer(fs[i]);
        }
    }

    void hacer(File f){
        try {
            String agregar = f.getName();
            System.out.println("agregar: " + agregar);
            File[] fs = f.listFiles();
            //busco ./images
            for (int i = 0; i < fs.length; i++) {
                f = fs[i];
                if (f.isDirectory())
                    break;
            }


            fs = f.listFiles();
            for (int i = 0; i < fs.length; i++) {
                File f2 = fs[i];
                renombrar(f2, agregar);
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void renombrar(File f, String agregar){
        try{
            p.util.DirFileFilter filtro = new p.util.DirFileFilter(false, ".jpg");
            File[] fs = f.listFiles(filtro);
            for (int i = 0; i < fs.length; i++) {
                f = fs[i];
                String v = f.getName();
                File niuF = new File(f.getParent(), agregar + "_" + v);
                boolean b = f.renameTo(niuF);
                if (!b)
                    System.out.println("falló en " + niuF.getAbsoluteFile());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
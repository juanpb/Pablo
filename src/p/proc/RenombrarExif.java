/**
 * Created by IntelliJ IDEA.
 * User: JPB
 * Date: Jul 16, 2004
 * Time: 4:04:10 PM
 * To change this template use Options | File Templates.
 */
package p.proc;

import p.fotos.Exif;
import p.util.DirFileFilter;

import java.io.File;

public class RenombrarExif{

    public static void main(String[] args){
        String dir = "C:\\Pablo\\fo\\fotos\\Copy of 100NIKON";
        new RenombrarExif(dir);
    }

    public RenombrarExif(String file){
        this(new File(file));
    }
    public RenombrarExif(File f){
        if (!f.exists()){
            String msg = "No existe el archivo " + f.getAbsolutePath();
            System.out.println(msg);
            throw new RuntimeException(msg);
        }

        if (f.isFile()){
            renombar(f);
        }
        else{
            DirFileFilter filtro = new DirFileFilter(false, ".jpg");
            File[] files = f.listFiles(filtro);
            for (int i = 0; i < files.length; i++) {
                File a = files[i].getAbsoluteFile();
                renombar(a);
            }
        }
    }

    private void renombar(File f) {
        System.out.println("Renombrando " + f.getName());
        Exif e = new Exif(f);
        String exif = "[ " + e.toString() + " ]";
        String n = f.getName();
        String nuevo = exif + n;
        f.renameTo(new File(f.getParent(), nuevo));
    }
}

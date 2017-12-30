package p.aplic;

import p.util.Constantes;

import java.io.*;
import java.util.Properties;

public class Partir
{

    public Partir()
    {}

    //tamDeCadaParticion en KB
    public void partir(File file, int tamDeCadaParticion) throws Exception
    {
        FileInputStream fis = new FileInputStream(file);
        byte[] leido = new byte[tamDeCadaParticion ];
        String salida = file.getAbsolutePath();

        long ciclos = file.length() / tamDeCadaParticion;
        int i = 0;
        for (; i < ciclos; i++)
        {
            FileOutputStream fos = new FileOutputStream(salida + i);
            fis.read(leido);
            fos.write(leido);
            fos.close();
        }
        if (fis.available() > 0){
            FileOutputStream fos = new FileOutputStream(salida + i);
            leido = new byte[fis.available()];
            fis.read(leido);
            fos.write(leido);
            fos.close();
        }
        fis.close();
        System.out.println("Listo");
    }

    public static void partirPorLineas(File file, int cantLineasXArchivo) throws Exception
    {
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        String salida = file.getAbsolutePath();

        int i = 0;
        while(lnr.ready()){
            FileOutputStream fos = new FileOutputStream(salida + i++);
            for(int j = 0; j < cantLineasXArchivo && lnr.ready(); j++) {
                String ln = lnr.readLine() + Constantes.NUEVA_LINEA;
                fos.write(ln.getBytes());
            }
            fos.close();
        }
        lnr.close();
        System.out.println("Listo");
    }
   

    public void unir(String fileName) throws Exception{
        File fileSal = new File(fileName);
        int cont = 0;
        File fileEnt = new File(fileName + cont++);
        FileOutputStream fos = new FileOutputStream(fileSal);
        while(fileEnt.exists()){
            FileInputStream fis = new FileInputStream(fileEnt);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] leido = new byte[bis.available()];
            fis.read(leido);
            fis.close();
            fos.write(leido);
            fileEnt = new File(fileName + cont++);
        }
        fos.close();
    }

    public static void main(String[] args) throws Exception{

        String f = "D:\\P\\_env\\DB\\a.txt";
        LineNumberReader lnr = new LineNumberReader(new FileReader(f));
        System.out.println("lnr.getLineNumber() = " + lnr.getLineNumber());

        Partir.partirPorLineas(new File(f), 199000);
//            while(lnr.ready()){
//                String linea = lnr.readLine();
//                res.add(linea);
//            }

//        UtilFile.getArchivoPorLinea(f);
//        FileInputStream fis = new FileInputStream(f);
//
//        String salida = "D:\\P\\_env\\DB\\a2.txt";
//
//        byte[] leido = new byte[1024];
//
//        {
//            FileOutputStream fos = new FileOutputStream(salida);
//            fis.read(leido);
//            fos.write(leido);
//            fos.close();
//            fis.close();
//        }
//
//        fis.close();
        System.out.println("Listo");

    }

    public static void main2(String[] args) throws Exception
    {
        if (args.length != 1){
            System.out.println("Forma de uso: java ...Partir [arch_de_prop]");
            System.out.println("El archivo de prop. debe tener: ");
            System.out.println("    partir=[S/N]");
            System.out.println("    unir=[S/N]");
            System.out.println("    (Uno de los dos anteriores se puede omitir)");
            System.out.println("    nombre=[del archivo con path incluido]");
            System.out.println("    tam=[tamaño de cada una de las particiones, en KB]");
            return;
        }
        String fileEnt = args[0];
        FileInputStream fis = new FileInputStream(fileEnt);

        Partir partir = new Partir();

        Properties prop = new Properties();
        prop.load(fis);

        String nombre = prop.getProperty("nombre");
        String unir = prop.getProperty("unir");
        if (unir != null && unir.equalsIgnoreCase("s")){
            partir.unir(nombre);
            System.out.println("Se ha unido el archivo: " + nombre);
            return;
        }

        String artir = prop.getProperty("partir");
        if (artir != null && artir.equalsIgnoreCase("s")){
            String tem = prop.getProperty("tam");
            int tam = Integer.parseInt(tem);
            File f = new File(nombre);
            if (!f.exists()){
                System.out.println("No se encuentra el archivo: " + f.getCanonicalPath());
                return;
            }
            partir.partir(f, tam * 1024);
            System.out.println("Se ha partido el archivo: " + nombre);
            return;
        }

//        File f = new File(nombre);
//        Partir partir1 = new Partir(f, 1400 * 1024);
//        Partir par = new Partir();
//        par.unir(nombre);
    }
}
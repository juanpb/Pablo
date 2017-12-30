package p.mp3;

import p.util.DirFileFilter;
import p.util.Util;
import p.util.UtilFile;

import java.io.File;


public class Cortar {

//    static long _tamFrame = 156;
//    static long _tamFrame = 417;
    static long _tamParte = 300;

//    Si empieza con FF FB (EN HEXA)= 417  [PARA 128 kbps]

//    FrameSize = 144 * BitRate / (SampleRate + Padding)


//    17.11 =       39687
    //1031 seg =    39687 frames
    //1 seg = 38 frames
    //5 min = 300 seg = 11400 frames

    private static long _masMenos = 0; //3 * 38 * _tamFrame; // seg * fps * _tamFrame
    private static String _dirSal = "C:\\";
    private static String _entrada = null;
    private static boolean _crearDirectorios = false;
    private int _contParaDirectorio = 0;

    public static void main(String[] args) {

        parsearArchEntr(args);
        Cortar c = new Cortar();
        c.batch();
        System.out.println("Listo");

    }

    private static void parsearArchEntr(String[] args){
        if (args.length == 0){
            System.out.println("Hay que pasarle como parámetro el archivo ini");
            System.exit(-1);
        }
        String ini = args[0];

        Util.loadProperties(ini);
        String x = System.getProperty("DIR_SALIDA");
        if (x != null && !x.trim().equals(""))
            _dirSal = x.trim();
        System.out.println("Directorio de salida = " + _dirSal);

        x = System.getProperty("ENTRADA");
        if (x == null){
            System.out.println("Falta directorio de origen");
            return;
        }
        else
            _entrada = x.trim();
        System.out.println("Entrada = " + _entrada);

//        x = System.getProperty("TAM_FRAME");
//        if (x != null && !x.trim().equals(""))
//            _tamFrame = Long.parseLong(x.trim());
//
//        System.out.println("tamaño del frame = " + _tamFrame);

        x = System.getProperty("TAM_PARTE");
        if (x != null && !x.trim().equals(""))
            _tamParte = Long.parseLong(x.trim());
        System.out.println("tamaño de cada parte = " + _tamParte);

        x = System.getProperty("MAS_MENOS");
        if (x != null && !x.trim().equals(""))
            _masMenos = Long.parseLong(x.trim()) * 38; // * _tamFrame; // seg * fps * _tamFrame
        System.out.println("_masMenos = " + _masMenos);

        x = System.getProperty("CREAR_CARPETAS");
        _crearDirectorios = x != null && x.trim().equalsIgnoreCase("s");
        System.out.println("_crearDirectorios = " + _crearDirectorios);

    }

    public void batch(){
        File f = new File(_entrada);
        if (!f.exists()){
            String msg = "No existe el archivo " + f.getAbsolutePath();
            System.out.println(msg);
            throw new RuntimeException(msg);
        }

        if (f.isFile()){
            cortar(f);
        }
        else{
            DirFileFilter filtro = new DirFileFilter(false, ".mp3");
            File[] files = f.listFiles(filtro);
            _contParaDirectorio = 0;
            for (int i = 0; i < files.length; i++) {
                _contParaDirectorio++;
                File a = files[i].getAbsoluteFile();
                cortar(a);
            }
        }
    }

    public void cortar(File arch){
        System.out.println("cortando " + arch);

        try {
            FrameMP3 fm = new FrameMP3(arch);
            long tamFrame = fm.frameLenght();
            System.out.println("tamaño del Frame = " + tamFrame);
            long tam = _tamParte * 38 * tamFrame; // seg * fps * _tamFrame
            long masMenos = _masMenos * tamFrame;
            long desde = 0;
            long hasta = tam + masMenos;
            String name = arch.getName();
            String ext = "";
            int n = name.lastIndexOf(".");
            if (n > 0){
                ext = name.substring(n + 1);
                name = name.substring(0, n);
            }
            long archTam = arch.length();
            int cont = 0;
            while (hasta < archTam){
                String sep = "_";
                if (cont < 10) //tiene sentido hasta 99 partes
                    sep += "0";
                String nom = name + sep + cont;
                cont++;
                if (ext.length() > 0)
                    nom += "." + ext;
                File dir = new File(_dirSal);
                if (_crearDirectorios){
                    String subdir = "" + _contParaDirectorio;
                    if (_contParaDirectorio < 10)
                        subdir = "0" + subdir;
                    dir = new File(dir, subdir);
                }
                File fSal = new File(dir, nom);

                UtilFile.copiar(arch, fSal, desde, hasta);
                desde = hasta - (masMenos * 2);
                hasta = desde + tam + (masMenos * 2);
            }

            if (desde < archTam){
                String sep = "_";
                if (cont < 10) //tiene sentido hasta 99 partes
                    sep += "0";
                String nom = name + sep + cont;
                if (ext.length() > 0)
                    nom += "." + ext;
                File dir = new File(_dirSal);
                if (_crearDirectorios){
                    String subdir = "" + _contParaDirectorio;
                    if (_contParaDirectorio < 10)
                        subdir = "0" + subdir;
                    dir = new File(dir, subdir);
                }

                File fSal = new File( dir, nom);

                UtilFile.copiar(arch, fSal, desde, archTam);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package p.proc.pplan;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: JPB
 * Date: 15/07/13
 * Time: 14:39
 */
public class BuscarDupXTransf {
    private static String dir = "D:\\RootBuild\\_pplan\\paralelo\\v3\\";
//    private static String dir2 = "D:\\RootBuild\\_pplan\\paralelo\\v2\\imed\\respaldo";
    private static String dir2 = "D:\\RootBuild\\_pplan\\paralelo\\data\\imeds\\5-11-May";
    private static final String archEntrada = dir + "IMED-0077.20130506235959.TODOXX_60096816_PPLAN";
    private static final String archSalidaAc = dir + "dup_acc.txt";
    private static final String archSalidaDup = dir + "dup.txt";
    private static final String archSalidaDupDeMas = dir + "dupDeMas.txt";

    private static  long contadorReg = 0;
    private static  long contadorRegDup = 0;
    private static  long contadorRegBorradosDeMas = 0;

    public static void main(String[] args) throws Exception {
        long ini = System.currentTimeMillis();

        buscarDupMapContemplados2(new File(archEntrada));

        System.out.println("Cantidad de registros analizados: " + contadorReg);
        System.out.println("Cantidad de registros BorradosDeMas: " + contadorRegBorradosDeMas);
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    public static void mainDir(String[] args) throws Exception {
        long ini = System.currentTimeMillis();
        final List<File> archivos = UtilFile.archivos(new File(dir));
        int tam1 = archivos.size();
//        final List<File> archivos2 = UtilFile.archivos(new File(dir2));
//        archivos.addAll(archivos2);

        final int size = archivos.size();
        int cont = 0;
        for (File archivo : archivos) {
            cont++;
            buscarDupMapContemplados2(archivo);
            if (cont % 10000 == 0)
                System.out.println(cont + " de " + size);
        }
        System.out.println("Cantidad de archivos analizados: " + size);
        System.out.println("Cantidad de registros analizados: " + contadorReg);
        System.out.println("Cantidad de registros duplicados: " + contadorRegDup);
        System.out.println("Cantidad de registros BorradosDeMas: " + contadorRegBorradosDeMas);
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    public static void buscar() throws IOException {
        List<String> salida = new ArrayList<String>();
        List<String> salidaAc = new ArrayList<String>();

        List<String> salidaBorradoDeMas = new ArrayList<String>();
        final List<String> apl = UtilFile.getArchivoPorLinea(archEntrada);
        Set<String> cdrDup = getCDRSecDup(apl);


        for (String s : apl) {
            final String cdrSec = RegImed.getCDRSec(s);
            final String cdrType = RegImed.getCDRType(s);
            final String redirNum = RegImed.getRedirNum(s);
            final String tipo = RegImed.getTipo(s);

            final boolean esDup = cdrDup.contains(cdrSec);
            final boolean esCF = cdrType.equals("CF ");
            final boolean tieneRedNum = !redirNum.trim().equals("");

            if(esDup || esCF || tieneRedNum){
                String agregar = esDup?"1":"0";
                agregar += esCF?"1":"0";
                agregar += tieneRedNum?"1":"0";
                if (tipo.equals(RegImed.TIPO_ENTRANTE) && tieneRedNum && esCF){
                    salidaAc.add(s);
                    if (!esDup)
                        salidaBorradoDeMas.add(s);
                }
                //
                String nuevo = agregar + s.substring(3);
                salida.add(nuevo);
            }
        }
//        UtilFile.guardartArchivoPorLinea(archSalidaAc, salidaAc);
//        UtilFile.guardartArchivoPorLinea(archSalidaDup, salida);
        UtilFile.guardartArchivoPorLinea(archSalidaDupDeMas, salidaBorradoDeMas);
    }

    public static void buscarDupMapContemplados(File archivo) throws IOException {

        List<String> salidaBorradoDeMas = new ArrayList<String>();
        List<String> salidaBorradoDeMenos = new ArrayList<String>();
        final List<String> apl = UtilFile.getArchivoPorLinea(archivo);
        Set<String> cdrDup = getCDRSecDup(apl);


        for (String s : apl) {
            final String cdrSec = RegImed.getCDRSec(s);
            final String cdrType = RegImed.getCDRType(s);
            final String redirNum = RegImed.getRedirNum(s);
            final String tipo = RegImed.getTipo(s);

            final boolean esDup = cdrDup.contains(cdrSec);
            final boolean esCF = cdrType.equals("CF ");
            final boolean tieneRedNum = !redirNum.trim().equals("");

            if(esDup || esCF || tieneRedNum){
                if (!esDup)
                    salidaBorradoDeMas.add(s);
                else if (!esCF || !tieneRedNum)
                    salidaBorradoDeMenos.add(s);
                //
            }
        }
//        UtilFile.guardartArchivoPorLinea(archSalidaAc, salidaAc);
//        UtilFile.guardartArchivoPorLinea(archSalidaDup, salida);
//        UtilFile.guardartArchivoPorLinea(archSalidaDupDeMas, salidaBorradoDeMas);
    }


    public static void buscarDupMapContemplados2(File archivo) throws IOException {
        final List<String> apl = UtilFile.getArchivoPorLinea(archivo);
        contadorReg += apl.size();
        List<String> dupDA = getDupDefinicionActual(apl);
        contadorRegDup += dupDA.size();

        //por cada uno de los que se van a eliminar, busco la tranferencia
        for (String s : dupDA) {
            String aBuscar = s.substring(0,40) + "T";
            boolean encontrado = false;
            for (String x : apl) {
                if (x.startsWith(aBuscar)){
                    if (encontrado){
                        System.out.println("Duplicado"); //si aparece más de una vez, revisar definición
                        System.out.println("File: " + archivo.getAbsolutePath());
                        System.out.println(aBuscar);
                    }
                    encontrado = true;
                }
            }
            if (!encontrado){
                contadorRegBorradosDeMas++;
                System.out.println("no encontrado"); //si aparece más de una vez, revisar definición
                System.out.println("File: " + archivo.getAbsolutePath());
                System.out.println(aBuscar);
            }
        }
    }

    private static Set<String> getCDRSecDup(List<String> apl) {
        Set<String> todos = new HashSet<String>();
        Set<String> res = new HashSet<String>();
        for (String s : apl) {
            final String cdrSec = RegImed.getCDRSec(s);
            final boolean nuevo = todos.add(cdrSec);
            if(!nuevo)
                res.add(cdrSec);
        }

        return res;
    }


    private static List<String> getDupDefinicionActual(List<String> apl) {
        List<String> res = new ArrayList<String>();
        for (String s : apl) {
            final String cdrType = RegImed.getCDRType(s);
            final String redirNum = RegImed.getRedirNum(s);
            final String tipo = RegImed.getTipo(s);

            final boolean esCF = cdrType.equals("CF ");
            final boolean tieneRedNum = !redirNum.trim().equals("");

            if("E".equals(tipo) && esCF && tieneRedNum){
                res.add(s);
            }

            if (esCF && !tieneRedNum)
                System.out.println("P1");
            if (!esCF && tieneRedNum)
                System.out.println("P2");
        }
        return res;
    }
}

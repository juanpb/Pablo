package p.proc.pplan;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 02/05/13
 * Time: 10:46
 */
public class ModificarNroOrigen {

    private final static String archOrigenImed = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\mapeados\\imedCoinc";
    private final static String archSalidaImed= "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\mapeados\\imedCoincNroCamb";

    private final static String archOrigenPP = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\mapeados\\\\ppCoinc";
    private final static String archSalidaPP= "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\mapeados\\\\ppCoincNroCamb";
    private static final int LONG_NRO_IMED = 28;
    private static final int LONG_NRO_PP = 10;

    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();
        System.out.println("archOrigenImed = " + archOrigenImed);
        System.out.println("archSalidaImed = " + archSalidaImed);
        System.out.println("archOrigenPP = " + archOrigenPP);
        System.out.println("archSalidaPP = " + archSalidaPP);

        final File fileSalImed = new File(archSalidaImed);
        final File fileSalPP = new File(archSalidaPP);
        if (fileSalImed.exists()){
            System.out.println("Existe el archivo de salida Imed. No se hace nada");
            return ;
        }
        if (fileSalPP.exists()){
            System.out.println("Existe el archivo de salida PP. No se hace nada");
            return ;
        }

        final List<String> imedEnt = UtilFile.getArchivoPorLinea(archOrigenImed);
        List<String> imedSal = cambiarNroImed(imedEnt);
//        imedSal = Tranformar.cambiarNroLoteImed("0001", imedSal);
        UtilFile.guardartArchivoPorLinea(archSalidaImed, imedSal);

        //PP
        final List<String> ppEnt = UtilFile.getArchivoPorLinea(archOrigenPP);
        List<String> ppSal = cambiarNroPP(ppEnt);
        ppSal = Tranformar.cambiarNroLotePP("00001", ppSal);
        UtilFile.guardartArchivoPorLinea(archSalidaPP, ppSal);


        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

    }

    private static List<String>  cambiarNroImed(List<String> imedEnt) {
        return cambiarNroDesde(imedEnt, RegImed.NUMERO_ORIGEN_DESDE, LONG_NRO_IMED);
    }
    private static List<String>  cambiarNroPP(List<String> ent) {
        return cambiarNroDesde(ent, RegPP.NUMERO_ORIGEN_DESDE, LONG_NRO_PP);
    }

    private static List<String> cambiarNroDesde(List<String>imedEnt, int posNro, int longNro) {
        List<String> res = new ArrayList<String>(imedEnt.size());

        for (String s : imedEnt) {
            if (s.length() < posNro + longNro ){
                System.out.println("No se modifica la linea: " + s);
            }
            else {
                String nn = getNuevoNro(s.substring(posNro, posNro + longNro), longNro);
                s = s.substring(0, posNro) +  nn + s.substring( posNro + longNro);
            }
            res.add(s);
        }
        return res;
    }


    public static String getNuevoNro(String nro, int longNro) {
        String res = nro.trim();
        res = res.substring(res.length() - 4 );
        int i = Integer.parseInt(res);
        String nn;
        if (i == 9999)
            nn = "0000";
        else
            nn =  (i+1) + "";

        while (nn.length() < 4){
            nn = "0" + nn;
        }
        nn = nro.substring(0, nro.trim().length() - 4 ) + nn ;
        for (int j = 0; j < longNro; j++) {
            if (nro.substring(j, j+1).equals("0"))
                nn = "0" + nn;
            else
                break;
        }
        return pad(nn, longNro);
    }

    private static String pad(String nn, int longitud) {
        while (nn.length() < longitud) {
            nn += " ";
        }
        return nn;
    }
}

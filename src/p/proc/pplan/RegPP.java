package p.proc.pplan;

import org.apache.log4j.Logger;
import p.util.Fechas;
import p.util.UtilFile;
import p.util.UtilString;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: JPB
 * Date: 20/04/13
 * Time: 23:53
 */
public class RegPP extends Reg {
    private static final Logger logger = Logger.getLogger(RegPP.class);
    protected static final int NUMERO_ORIGEN_DESDE = 2;
    protected static final int NUMERO_ORIGEN_HASTA = 12;
    protected static final int FECHA_DESDE = 14;
    protected static final int FECHA_HASTA = 28;
    protected static final int TIPO_DESDE = 40;
    protected static final int TIPO_HASTA = 41;
    protected static final int NUMERO_DEST_DESDE = 41;
    protected static final int NUMERO_DEST_HASTA = 53;
    protected static final int DURA_A_DESDE = 53;
    protected static final int DURA_A_HASTA = 59;
    protected static final int DURA_T_DESDE = 59;
    protected static final int DURA_T_HASTA = 65;
    protected static final int LOTE_DESDE = 133;
    protected static final int LOTE_HASTA = 138;



    public RegPP(String linea) {
        super(linea);
    }



    @Override
    public int getFechaDesde() {
        return FECHA_DESDE;
    }

    @Override
    public int getFechaHasta() {
        return FECHA_HASTA;
    }

    @Override
    public int getNumeroOrigDesde() {
        return NUMERO_ORIGEN_DESDE;
    }

    @Override
    public int getNumeroOrigHasta() {
        return NUMERO_ORIGEN_HASTA;
    }

    @Override
    public int getNumeroDestDesde() {
        return NUMERO_DEST_DESDE;
    }

    @Override
    public int getNumeroDestHasta() {
        return NUMERO_DEST_HASTA;
    }

    @Override
    public int getTipoDesde() {
        return TIPO_DESDE;
    }

    @Override
    public int getTipoHasta() {
        return TIPO_HASTA;
    }

    @Override
    public int getDuraADesde() {
        return DURA_A_DESDE;
    }

    @Override
    public int getDuraAHasta() {
        return DURA_A_HASTA;
    }

    @Override
    public int getDuraTDesde() {
        return DURA_T_DESDE;
    }

    @Override
    public int getDuraTHasta() {
        return DURA_T_HASTA;
    }


    public static long getDuracionAire(String s) {
        s = s.substring(DURA_A_DESDE, DURA_A_HASTA);
        return Long.parseLong(s);
    }

    public static long getDuracionTierra(String s) {
        s = s.substring(DURA_T_DESDE, DURA_T_HASTA);
        return Long.parseLong(s);
    }

    public static Date getHoraLlamada(String s) {
        s = s.substring(FECHA_DESDE, FECHA_HASTA);
        return Fechas.getDateAAAAMMDDHHMMSS(s);
    }

    public static String getTipoLlamada(String s) {
        return s.substring(TIPO_DESDE, TIPO_HASTA);
    }

    public static String getNroOrigen(String s) {
        return s.substring(NUMERO_ORIGEN_DESDE, NUMERO_ORIGEN_HASTA);
    }

    public static void main(String[] args) {
        String x = "D:\\RootBuild\\_pplan\\paralelo\\v2\\rechazos\\nuev\\";
        String loteOriginal = "06.41701.20130511180337.PG";

        String nuevoNroLote = loteOriginal.substring(3, 8);
        String fechaGen = loteOriginal.substring(9, 23);

        String h = RegPP.getHeader("06", nuevoNroLote, fechaGen);
        System.out.println(h);

        final List<String> apl;
        try {
            apl = UtilFile.getArchivoPorLinea(new File(x, loteOriginal));
            final List<String> strings = sacarHeaderYFooter(apl);
            final String footer = getFooter("06", nuevoNroLote, fechaGen, strings);
            System.out.println(footer);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }



    }

    public static List<String> sacarHeaderYFooter(File f) throws IOException {
        return sacarHeaderYFooter(UtilFile.getArchivoPorLinea(f));
    }
    public static List<String> sacarHeaderYFooter(List<String> archivoPorLinea) {
        final List<String> res = new ArrayList<String>();
        for(int i = 1; i< archivoPorLinea.size() -1; i++){ //saco header y footer
            res.add(archivoPorLinea.get(i));
        }
        return res;
    }


    public static String getHeader(String prestadora, String nuevoNroLote, String fechaGen) {
        if (fechaGen.length() != 14){
            throw new RuntimeException("mal tamaño de fecha: " + fechaGen);
        }
        StringBuilder res = new StringBuilder();
        res.append("01");
        res.append(UtilString.llenar(prestadora, 2));
        res.append(UtilString.llenar(nuevoNroLote, 5));
        res.append(fechaGen);
        return UtilString.llenar(res.toString(), 150);
    }

    public static String getFooter(String prestadora, String nuevoNroLote, String fechaGen, List<String> registros) {
        if (fechaGen.length() != 14){
            throw new RuntimeException("mal tamaño de fecha: " + fechaGen);
        }
        StringBuilder res = new StringBuilder();
        res.append("99");
        res.append(UtilString.llenar(prestadora, 2));
        res.append(UtilString.llenar(nuevoNroLote, 5));
        res.append(fechaGen);

        final int cantReg = registros.size() + 2;
        res.append(UtilString.llenarXIzq(cantReg+"", 10, "0"));

        final long totalAire = sumarAire(registros);
        res.append(UtilString.llenarXIzq(totalAire+"", 10, "0"));

        final long totalTierra = sumarTierra(registros);
        res.append(UtilString.llenarXIzq(totalTierra+"", 10, "0"));

        return UtilString.llenar(res.toString(), 150);
    }

    private static long sumarAire(List<String> registros) {
        long res = 0;

        for (String r : registros) {
            final long da = getDuracionAire(r);
            res +=da;
        }

        return res;
    }

    private static long sumarTierra(List<String> registros) {
        long res = 0;

        for (String r : registros) {
            final long dt = getDuracionTierra(r);
            res +=dt;
        }

        return res;
    }
}

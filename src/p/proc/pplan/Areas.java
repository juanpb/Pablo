package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 */
public class Areas implements Aplicacion {
    private static final Logger logger = Logger.getLogger(Areas.class);
    private static String areasPath = "D:\\RootBuild\\_pplan\\datos\\areas.txt";
    private static String codClZonaPath = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\mapeados\\8\\in\\cod_clave_zona.txt";
    private static String imed = "D:\\RootBuild\\_pplan\\ejemplos\\15abr\\mapeados\\8\\in\\IMED-0401.20130313120043._U_F6_PPLAN_401";
//    private static String imed = "D:\\RootBuild\\_pplan\\paralelo\\data\\receptor\\pplan_in\\imed\\IMED-1499.20130506235854.TODOXX_60096816_PPLAN";

    private static Map<String, String> areas = new HashMap<String, String>(34452);
    private static List<String> codClaveZona = new ArrayList<String>();

    private static final String NOMBRE = "Cambiar Lote";
    private static int cont = 0;

    public static void main(String[] args) throws IOException {
        File salida = new File(imed + "_AD_2");
        if (salida.exists()){
            System.out.println("Existe archivo de salida, no se hace nada.");
            System.out.println("salida.getAbsolutePath() = " + salida.getAbsolutePath());
            return ;
        }

        cargarCT();
        final List<String> list = ejecutar(imed);
        UtilFile.guardartArchivoPorLinea(salida, list);
        System.out.println("contador encontrados: " + cont);
        System.out.println("LIsto");
    }

    private static void cargarCT() throws IOException {

        final List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(codClZonaPath);
        for (String s : archivoPorLinea) {
            codClaveZona.add(s.trim());
        }


        final List<String> archivoPorLinea2 = UtilFile.getArchivoPorLinea(areasPath);
        for (String s : archivoPorLinea2) {
            StringTokenizer st = new StringTokenizer(s, ",", false);
            String ori = st.nextToken();
            String des = st.nextToken();
            String val = st.nextToken();
            final String put = areas.put(ori, val);
            if(put != null){
                System.out.println("CLAVE REPETIDA: ");
                System.out.println("ori = " + ori);
                System.out.println("val = " + val);
                throw new RuntimeException("CLAVE REPETIDA");
            }
        }
    }

    public static List<String> ejecutar(String imedPath) throws IOException {
        final List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(imedPath);
        List<String> areasDestino = new ArrayList<String>();
        int cont = 0;
        for (String s : archivoPorLinea) {
            final RegImed imed2 = new RegImed(s);
            areasDestino.add("'"+pad(getAreaDestino(imed2, codClaveZona.get(cont))));
//            areasDestino.add("'"+pad(getAreaDestino(imed2, codClaveZona.get(0))));
            cont++;
        }

        return areasDestino;
    }


    private static String pad(String x) {
        if (x == null)
            return "     ";
        while (x.length() < 5)
            x += " ";

        return x;
    }

    private static String getAreaDestino(RegImed imed, String codClaveZona) {

        // getAditInfo1 = 264 a 268 |  getAditInfo2 = 269-273
        final String aditInfo1 = imed.getAditInfo1();
        final String aditInfo2 = imed.getAditInfo2();



        if (Reg.TIPO_TRANSFERENCIA.equals(imed.getTipo())){
            cont++;
        //Se completará de la siguiente manera: con las posiciones 254 a 260 (getDestinationArea) del IMED se debe ir a
        //buscar a la tabla TA_AREAS (tabla de siscel), buscando por el campo COD_AREA,
            //--lo siguiente no está implementado
        // si no encuentra el dato, buscar
        // en el campo COD_ALM, una vez identificado el registro (ya sea con el COD_AREA o el COD_ALM), se obtendrá el
        // valor del campo COD_DDN el cual se utilizará para completar ÁREA DESTINO, eliminando previamente
        // el 0 de la izquierda.
            String da = imed.getDestinationArea();
            String ddn = areas.get(da.trim());
            if (ddn == null){
                System.out.println("DDN null: " + imed.linea);
                System.out.println("da = " + da);
                throw new RuntimeException("Falta implementar asda");
            } else if (ddn.startsWith("0"))
                ddn = ddn.substring(1);
            return ddn;
        }

        //Para sentido entrante y saliente, si la clave zona es TXM, al área destino colocar el valor reconocido
        //para el área origen (posiciones 78 a 82 de PPLAN)
        if ("TXM".equals(codClaveZona))
            return getAreaOrigen(imed);

        //Si no:
        //Sentido Saliente: Posiciones 269 a 273 del imed. Si viene nulo colocar el área origen.
        //Sentido Entrante: Posiciones 269 a 273 del imed

        if (Reg.TIPO_SALIENTE.equals(imed.getTipo())){
            //Posiciones 269 a 273 del imed. Si viene nulo colocar el área origen (Sentido Saliente: Posiciones 264 a 268 del imed ).
            if (aditInfo2 == null || aditInfo2.trim().equals(""))
                return getAreaOrigen(imed);
            else
                return aditInfo2;
        }

        if (Reg.TIPO_ENTRANTE.equals(imed.getTipo())){
            //Sentido Entrante: Posiciones 269 a 273 del imed
            return aditInfo2;
        }
        else
            throw new RuntimeException("Falta el tipo en línea: " + imed.linea);
    }

    private static String getAreaOrigen(RegImed imed) {
        /**
         * Sentido Saliente: Posiciones 264 a 268 del imed
         Sentido Entrante: Posiciones 269 a 273 del imed
         Sentido Transferencia: Posiciones 264 a 268 del imed

         */
        final String aditInfo1 = imed.getAditInfo1();   //264 a 268
        final String aditInfo2 = imed.getAditInfo2();   // 269-273

        if (Reg.TIPO_SALIENTE.equals(imed.getTipo())){
            return aditInfo1;
        } else if (Reg.TIPO_ENTRANTE.equals(imed.getTipo())){
            return aditInfo2;
        } else if (Reg.TIPO_TRANSFERENCIA.equals(imed.getTipo())){
            return aditInfo1;
        }
        throw new RuntimeException("no definido 879");
    }


    private static String getAreaDestinoV1(RegImed imed) {

        // getAditInfo1 = 264 a 268 |  getAditInfo2 = 269-273
        final String aditInfo1 = imed.getAditInfo1();
        final String aditInfo2 = imed.getAditInfo2();

        if (Reg.TIPO_SALIENTE.equals(imed.getTipo())){
            //Posiciones 269 a 273 del imed. Si viene nulo colocar el área origen (Sentido Saliente: Posiciones 264 a 268 del imed ).
            if (aditInfo2 == null || aditInfo2.trim().equals(""))
                return aditInfo1;
            else
                return aditInfo2;
        } else if (Reg.TIPO_ENTRANTE.equals(imed.getTipo())){
            //Sentido Entrante: Posiciones 269 a 273 del imed
            return aditInfo2;
        } else if (Reg.TIPO_TRANSFERENCIA.equals(imed.getTipo())){
            //Se completará de la siguiente manera: con las posiciones 254 a 260 del IMED se debe ir a buscar a la tabla
            // TA_AREAS (tabla de siscel), buscando por el campo COD_AREA y se obtendrá el valor del campo COD_DDN el cual
            // se utilizará para completar este campo, eliminando previamente el 0 de la izquierda
            String da = imed.getDestinationArea();
            String ddn = areas.get(da.trim());
            if (ddn == null){
                System.out.println("DDN null: " + imed.linea);
            }else if (ddn.startsWith("0"))
                ddn = ddn.substring(1);
            return ddn;
        }
        else
            throw new RuntimeException("Falta el tipo en línea: " + imed.linea);
    }

    private static String getAreaDestinoV2(RegImed imed) {

        // getAditInfo1 = 264 a 268 |  getAditInfo2 = 269-273
        final String aditInfo1 = imed.getAditInfo1();
        final String aditInfo2 = imed.getAditInfo2();


        //Se completará de la siguiente manera: con las posiciones 254 a 260 del IMED se debe ir a buscar a la tabla
        // TA_AREAS (tabla de siscel), buscando por el campo COD_AREA y se obtendrá el valor del campo COD_DDN el cual
        // se utilizará para completar este campo, eliminando previamente el 0 de la izquierda
        String da = imed.getDestinationArea();
        String ddn = areas.get(da.trim());
        if (ddn == null){
            System.out.println("DDN null: " + imed.linea);
        }else if (ddn.startsWith("0"))
            ddn = ddn.substring(1);
        return ddn;
    }

    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();

        String archEntrada = params.get(0);
        String nroLote     = params.get(1);
        String tipo        = params.get(2);

        final List<String> archivoPorLinea;
        try {
            archivoPorLinea = UtilFile.getArchivoPorLinea(archEntrada);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al leer archivo de entrada: ").append(e.getMessage());
            return res.toString();
        }

        List<String> sal = null;
        String archSalida = archEntrada + "_" + nroLote;
        final File file = new File(archSalida);
        if (file.exists()){
            res.append("Ya existe el archivo de salida: " + archSalida);
            res.append("No se hace nada");
            return res.toString();
        }

        if (tipo.equals(Reg.TIPO_IMED)){
            sal = Tranformar.cambiarNroLoteImed(nroLote, archivoPorLinea);
        }
        else if (tipo.equals(Reg.TIPO_PP)){
            sal = Tranformar.cambiarNroLotePP(nroLote, archivoPorLinea);
        }
        else{
            System.out.println("No se indico el tipo de archivo. Valores posible: P,I. Valor recibido: " + tipo);
            System.exit(-1);
        }

        try {
            UtilFile.guardartArchivoPorLinea(archSalida, sal);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al generar el archivo de salida: ").append(e.getMessage());
            return res.toString();
        }


        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Arch. de entrada: " + archEntrada).append(Constantes.NUEVA_LINEA);
        res.append("Se cambió con el nro de lote: " + nroLote).append(Constantes.NUEVA_LINEA);
        res.append("Salida: " + archSalida).append(Constantes.NUEVA_LINEA);
        res.append("Demoró " + ms + " ms");

        return res.toString();
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<String> vp = new ArrayList<String>();
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Archivo");
        res.add(p1);
        final Param p2 = new Param("Nro de lote");
        res.add(p2);

        vp.add(Reg.TIPO_IMED);
        vp.add(Reg.TIPO_PP);
        final Param p3 = new Param(vp, "Tipo");
        res.add(p3);


        return res;
    }

}

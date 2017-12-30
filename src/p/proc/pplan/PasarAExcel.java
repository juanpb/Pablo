package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 */
public class PasarAExcel implements Aplicacion {
    private static final Logger logger = Logger.getLogger(PasarAExcel.class);

    private static final String posicionesPP    = "2, 12, 14, 28, 29, 36, 40, 41, 53, 59, 65, 73, 77, 82, 87, 94, 101, 102, 103, 107, 113, 119, 129, 130, 133, 138, 140, 142, 146, 150";
    private static final String posicionesPPOut = "2, 12, 14, 28, 29, 36, 40, 41, 53, 59, 65, 73, 77, 82, 87, 94, 101, 102, 103, 107, 113, 119, 129, 130, 133, 138, 140, 142, 144, 146, 148, 150";
    private static final String posicionesImed  = "3,11,19,25,40,41,49,57,65,71,74,75,103,118,119,147,153,159,166,173,180,190,192,212,232,233,234,235,237,239,240,241,243,246,253,260,261,262,263,268,273,282,286,290,294,298,299,300,301,302";

    private static final String NOMBRE = "Pasar a excel";


    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();

        String archEntrada = params.get(0);
        String tipo        = params.get(1);
        String posiciones = null;
        List<String> cabecera = null;
        if (tipo.equals(Reg.TIPO_IMED)){
            posiciones = posicionesImed;
            cabecera = getCabeceraImed();
        }
        else if (tipo.equals(Reg.TIPO_PP)){
            posiciones = posicionesPP;
            cabecera = getCabeceraPP();
        }
        else if (tipo.equals(Reg.TIPO_PP_OUT)){
            posiciones = posicionesPPOut;
            cabecera = getCabeceraPPOut();
        }
        else{
            System.out.println("No se indico el tipo de archivo. Valores posible: P,I. Valor recibido: " + tipo);
            System.exit(-1);
        }

        PasarAExcel m = new PasarAExcel();
        final List<String> archivoPorLinea;
        try {
            archivoPorLinea = UtilFile.getArchivoPorLinea(archEntrada);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al leer archivo de entrada: ").append(e.getMessage());
            return res.toString();
        }

        final List<String> csv = m.CSV(archivoPorLinea, posiciones);
        File  archSalida = new File(archEntrada + ".csv");
        int cont = 0;
        while (archSalida.exists()){
            archSalida = new File(archEntrada + cont++ + ".csv");
        }

        if (archSalida.exists()){
            res.append("Existe el archivo de salida: " + archSalida.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            res.append("No se hace nada.");
        }
        else{

            try {
                UtilFile.guardartArchivoPorLinea(archSalida, cabecera);
                UtilFile.guardartArchivoPorLinea(archSalida, csv);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                res.append("Error al generar el archivo de salida: ").append(e.getMessage());
                return res.toString();
            }


            long fin = System.currentTimeMillis();
            long ms = (fin - ini);


            res.append("Arch. de entrada: " + archEntrada).append(Constantes.NUEVA_LINEA);
            res.append("Se pasó a excel el archivo: " + archEntrada).append(Constantes.NUEVA_LINEA);
            res.append("Salida: " + archSalida).append(Constantes.NUEVA_LINEA);
            res.append("Demoró " + ms + " ms");
        }
        return res.toString();
    }

    private List<String> getCabeceraPPOut() {
        List<String> res = new ArrayList<String>();
        res.add("'1';'2';'3';'4';'5';'6';'7';'8';'9';'10';'11';'12';'13';'14';'15';'16';'17';'18';'19';'20';'21';'22';'23';'24';'25';'26';'27';'28';'29';'30';'31';'32'");
        res.add("'1-2';'3-12';'13-14';'15-28';'29-29';'30-36';'37-40';'41-41';'42-53';'54-59';'60-65';'66-73';'74-77';'78-82';'83-87';'88-94';'95-101';'102-102';'103-103';'104-107';'108-113';'114-119';'120-129';'130-130';'131-133';'134-138';'139-140';'141-142';'143-144';'145-146';'147-148';'149-150'");
        res.add("'Tipo de Registro';'Número de ANI';'Código de Llamada';'Fecha y Hora Llamada';'Flag de Llamada';'Código de DDI';'Código de DDN';'Tipo de Llamada';'Número Destino';'Duración Aire';'Duración Tierra';'Número de Serie';'Código de Clave Zona';'Área Origen';'Área Destino';'Ruta de Entrada';'Ruta de Salida';'Operador Origen Siscel';'Indicador Llamada WIN';'Prefijo de Ubicación Movil';'Duración Aire Real';'Duración Red Real';'Número IMSI';'Sistema Comercial';'Operador Origen Siscel';'Número de Lote Original';'Operador Destino Siscel';'Operador Destino Siscel';'Operador Origen Movics';'Operador Origen Movics';'Operador Destino Movics';'Operador Destino Movics'");

        return res;
    }

    public static List<String> getCabeceraPP() {
        List<String> res = new ArrayList<String>();
        res.add("'1';'2';'3';'4';'5';'6';'7';'8';'9';'10';'11';'12';'13';'14';'15';'16';'17';'18';'19';'20';'21';'22';'23';'24';'25';'26';'27';'28';'29';'30'");
        res.add("'1-2';'3-12';'13-14';'15-28';'29';'30-36';'37-40';'41';'42- 53';'54- 59';'60- 65';'66- 73';'74- 77';'78- 82';'83- 87';'88- 94';'95- 101';'102- ';'103- ';'104- 107';'108- 113';'114- 119';'120- 129';'130- ';'131- 133';'134- 138';'139- 140';'141- 142';'143- 146';'147- 150'");
        res.add("'TipoRegistro';'NumeroMin';'CodigoLlamada';'FechaHoraLlamada';'FlagLlamada';'CodigoDdi';'CodigoDdn';'TipoLlamada';'NumeroDestino';'DuracionAire';'DuracionTierra';'NumeroSerie';'CodigoClaveZona';'AreaOrigen';'AreaDestino';'RutaSalida';'RutaEntrada';'NumeroPortadoFlag';'IndLlamadaWin';'PrefijoUbicacionMovil';'DurAireReal';'DurRedReal';'NroImsi';'SistComercial';'Espacios';'NumeroLoteOriginal';'CodigoError1';'CodigoError2';'CodigoOperOrigen';'CodigoOperDestino'");

        return res;
    }


    public static List<String> getCabeceraImed() {
        List<String> res = new ArrayList<String>();
        res.add("'1';'2';'3';'4';'5';'6';'7';'8';'9';'10';'11';'12';'13';'14';'15';'16';'17';'18';'19';'20';'21';'22';'23';'24';'25';'26';'27';'28';'29';'30';'31';'32';'33';'34';'35';'36';'37';'38';'39';'40';'41';'42';'43';'44';'45';'46';'47';'48';'49';'50';'51'");
        res.add("'1-3';'4-11';'12-19';'20-25';'26-40';'41-41';'42-49';'50-57';'58-65';'66-71';'72-74';'75';'76-103';'104-118';'119';'120-147';'148-153';'154-159';'160-166';'167-173';'174-180';'181-190';'191-192';'193-212';'213-232';'233';'234';'235';'236-237';'238-239';'240';'241';'242-243';'244-246';'247-253';'254-260';'261';'262';'263';'264-268';'269-273';'274-285';'283-286';'287-290';'291-294';'295-298';'299';'300';'301';'302';'303-?'");
        res.add("'Switch';'CDR_SequenceNumber';'DateOfCall';'CallStartTime';'MIN';'CallDirection';'CDRFileSequenceNumber';'CallIdentificationNumber';'RelatedCallNumber';'NetworkCallReference';'CDRType';'TypeOfSubscriber';'BillingNumber ¿ANI?';'ESN';'TypeOfCall';'DestinationNumber';'ChargeableDuration';'ConnectionTime';'IncomingRoute';'OutgoingRoute';'CellIdentification';'TAC';'TypeOfService';'RedirectedNumber';'MobileStationRoamingNumber';'NumberingPlan';'TypeOfNumber';'RoamerPortIndicator';'BillingSystem';'VPNIndicator';'TypeOfCallingSubscriber';'CarrierSelectionIdent';'PrefixDialled';'CarrierIdentification';'OriginArea';'DestinationArea';'BPartyAnswerFlag';'ModuleOfOrigin';'TypeTechnology';'AdditionalInformation1';'AdditionalInformation2';'AdditionalInformation3';'OperadoraOrigenSiscel';'OperadoraDestinoSiscel';'OperadoraOrigenMovics';'OperadoraDestinoMovics';'PortNumber';'BillingNumOrigen';'BillingNumDestino';'SisTas';'Filler'");

        return res;
    }


    private List<String> CSV(List<String> lineas, String posicionesSinParsear) {
        List<String> res = new ArrayList<String>(lineas.size());

        final List<Integer> posiciones = parsearPosiciones(posicionesSinParsear);

        for (String s : lineas) {
            String s1 = agregarComillasYPYC(posiciones, s);
            res.add(s1);
        }
        return res;
    }

    private List<Integer> parsearPosiciones(String posiciones){
        StringTokenizer st = new StringTokenizer(posiciones, ",");
        List<Integer> indices = new ArrayList<Integer>();
        while (st.hasMoreElements()){
            String s = st.nextToken();
            Integer i = Integer.parseInt(s.trim());
            indices.add(i);
        }
        return indices;
    }

    //agrega comillas dobles, comillas simples al principio y ;
    static private String agregarComillasYPYC(List<Integer> indices, String entrada) {
        if (entrada.trim().equals(""))
            return "";
        StringBuilder sb = new StringBuilder();
        int posDesde = 0;
        for (final Integer posHasta : indices) {
            final String c = entrada.substring(posDesde, posHasta);
            posDesde = posHasta;
            sb.append("\"'").append(c).append("\";");
        }
        return sb.toString();
    }

    /*
    Coindicen si tienen la misma fecha, hora, nroOrigen y tipo
     */
    public static boolean coinciden(RegImed ri, RegPP rp){
        if (ri.getFecha().equals(rp.getFecha()))
            if (ri.getNumOrigen().equals(rp.getNumOrigen()))
                if (ri.getTipo().equals(rp.getTipo()))
                    return true;

        return false;
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

        vp.add(Reg.TIPO_IMED);
        vp.add(Reg.TIPO_PP);
        vp.add(Reg.TIPO_PP_OUT);
        final Param p2 = new Param(vp, "Tipo");
        res.add(p2);


        return res;
    }

}

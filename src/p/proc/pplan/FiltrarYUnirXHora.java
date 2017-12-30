package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.Fechas;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 */
public class FiltrarYUnirXHora implements Aplicacion {
    private static final Logger logger = Logger.getLogger(FiltrarYUnirXHora.class);

    private static final String NOMBRE = "Filtrar y unir x hora";

    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        try {

            File archDirEntrada = new File (params.get(0));
            String archSalida     = params.get(1);
            String d        = params.get(2);
            String h        = params.get(3);
            String tipo        = params.get(4);
            boolean guardarFueraDeRango = new Boolean(params.get(5));

            if (archSalida == null || archSalida.length() == 0){
                if (archDirEntrada.isDirectory()){
                    archSalida = archDirEntrada.getAbsolutePath() + "\\" + "sal_(" + d + "-" + h + ")";
                }
                else{
                    archSalida = archDirEntrada.getAbsolutePath() + "_(" + d + "-" + h + ")";
                }
            }

            File fileFR = new File(archSalida + "_FR");
            if (guardarFueraDeRango){
                int cont = 0;
                while (fileFR.exists()) {
                    fileFR = new File(archSalida + "_FR_" + cont);
                    cont++;
                }
            }


            Date fd = Fechas.getDateAAAAMMDDHHMMSS("2013" + d);
            Date fh = Fechas.getDateAAAAMMDDHHMMSS("2013" + h);

            final List<String> archivoPorLineaSalida;
            final List<String> archivoPorLineaSalidaFueraRango = new ArrayList<String>();

            int contArchFiltr = 0;
            int contArchNoFiltr = 0;
            if (archDirEntrada.isFile())
                archivoPorLineaSalida = filtrar(archDirEntrada, fd, fh, tipo, archivoPorLineaSalidaFueraRango);
            else{
                final File[] files = archDirEntrada.listFiles();
                archivoPorLineaSalida = new ArrayList<String>();
                for (File file : files) {
                    if (file.isFile()){
                        if (file.length() > 0){
                            archivoPorLineaSalida.addAll(filtrar(file, fd, fh, tipo, archivoPorLineaSalidaFueraRango));
                            contArchFiltr++;
                        }
                        else
                            contArchNoFiltr++;
                    }

                    //libero memoria
                    if (guardarFueraDeRango)
                        UtilFile.guardartArchivoPorLinea(fileFR, archivoPorLineaSalidaFueraRango, false);
                    archivoPorLineaSalidaFueraRango.clear();
                }
            }
            if (tipo.equals(Reg.TIPO_IMED))
                OrdernarImed.ordenar(archivoPorLineaSalida);
            else
                OrdernarPP.ordenar(archivoPorLineaSalida);

            final File file = new File(archSalida);
            if (file.exists()){
                res.append("Ya existe el archivo de salida: " + archSalida);
                res.append("No se hace nada");
                return res.toString();
            }

            UtilFile.guardartArchivoPorLinea(archSalida, archivoPorLineaSalida);

            long fin = System.currentTimeMillis();
            long ms = (fin - ini);


            res.append("Arch./dir de entrada: " + archDirEntrada.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            res.append("Se filtró y ordenó por las fechas: " + d + " y " + h).append(Constantes.NUEVA_LINEA);
            res.append("Salida: " + archSalida).append(Constantes.NUEVA_LINEA);
            if (guardarFueraDeRango){
                res.append("Salida FR: " + fileFR.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            }
            res.append("contArchFiltr: " + contArchFiltr).append(Constantes.NUEVA_LINEA);
            res.append("contArchNoFiltr (vacios): " + contArchNoFiltr).append(Constantes.NUEVA_LINEA);
            res.append("Demoró " + ms + " ms");

            return res.toString();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            res.append("Error al generar el archivo de salida: ").append(e.getMessage());
            return res.toString();
        }
    }

    private List<String> filtrar(File entrada, Date d, Date h, String tipo, List<String> archivoPorLineaSalidaFueraRango) throws IOException {
        try {
            List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(entrada);
            if (tipo.equals(Reg.TIPO_PP)){
                archivoPorLinea = RegPP.sacarHeaderYFooter(archivoPorLinea);
            }
            return filtrar(archivoPorLinea, d, h, tipo, archivoPorLineaSalidaFueraRango);
        } catch (RuntimeException e) {
            System.out.println("entrada.getAbsolutePath() = " + entrada.getAbsolutePath());
            throw e;
        }
    }

    private List<String> filtrar(List<String> entrada, Date d, Date h, String tipo, List<String> archivoPorLineaSalidaFueraRango){
        if(tipo.equals(Reg.TIPO_PP))
            return filtrarPP(entrada, d, h, archivoPorLineaSalidaFueraRango);
        else
            return filtrarImed(entrada, d, h, archivoPorLineaSalidaFueraRango);

    }
    private List<String> filtrarImed(List<String> entrada, Date d, Date h, List<String> archivoPorLineaSalidaFueraRango){
        List<String> res = new ArrayList<String>();

        for (String s : entrada) {
            try {
                Date hi = RegImed.getHoraLlamada(s);
                if (enFecha(hi, d, h))
                    res.add(s);
                else
                    archivoPorLineaSalidaFueraRango.add(s);
            } catch (RuntimeException e) {
                System.out.println(s);
                throw e;
            }
        }
        return res;
    }

    //Devuelve true sii desde <= f <= hasta
    private boolean enFecha(Date f, Date desde, Date hasta) {
//        System.out.println("f = " + f);
//        System.out.println("desde = " + desde);
//        System.out.println("hasta = " + hasta);
//        System.out.println("f.before(desde) = " + f.before(desde));
//        System.out.println("f.after(hasta) = " + f.after(hasta));
//        System.out.println("");
        return (!f.before(desde)) && (!f.after(hasta));
    }

    private List<String> filtrarPP(List<String> entrada, Date d, Date h, List<String> archivoPorLineaSalidaFueraRango){
        List<String> res = new ArrayList<String>();


            for (String s : entrada) {
                try {
                    Date hi = RegPP.getHoraLlamada(s);
                    if (enFecha(hi, d, h))
                        res.add(s);
                    else
                        archivoPorLineaSalidaFueraRango.add(s);
                } catch (Exception e) {
                    System.out.println(s);
                    logger.error(e.getMessage(), e);
                }
            }

        return res;
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Directorio/Archivo");
        res.add(p1);
        final Param p2 = new Param("Archivo salida");
        res.add(p2);

        final Param p3 = new Param("FD 2013MMDDHHMMSS", "fecha desde incluida");
        res.add(p3);


        final Param p4 = new Param("FH 2013MMDDHHMMSS", "fecha hasta incluida");
        res.add(p4);


        List<String> vp = new ArrayList<String>();
        vp.add(Reg.TIPO_PP);
        vp.add(Reg.TIPO_IMED);

        final Param p5 = new Param(vp, "Tipo");
        res.add(p5);

        final Param p6 = new Param("Fuera de rango a archivo", true);
        p6.setTooltip("seleccionar para generar archivo con los fuera de rango");
        res.add(p6);

        return res;
    }

}

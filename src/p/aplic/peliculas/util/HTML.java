package p.aplic.peliculas.util;

import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.util.Constantes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * User: Administrador
 * Date: 17/03/2007
 * Time: 17:58:21
 */
public class HTML {
    private static String raizFotos = System.getProperty("dirRaizFotos");;


    public static void generarHTML(List<Pelicula> peliculas, String archSalida)
            throws IOException, JDOMException {
        generarHTML(peliculas, archSalida, new ExportarConfig(true));
    }

    public static StringBuffer getContenidoHTML(List<Pelicula> peliculas, ExportarConfig conf){
        StringBuffer sb = new StringBuffer();
        agregarCabecera(sb);
        int cont = 0;
        if (peliculas.size() == 0){
            sb.append("        <h2> Vacío </h2>");
        }
        else {
            for (Pelicula p : peliculas) {
                agregarPelicula(p, sb, "    ", cont, conf);
                sb.append(Constantes.NUEVA_LINEA);
                cont++;
            }
        }
        agregarPie(sb);
        return sb;
    }

    public static void generarHTML(List<Pelicula> peliculas, String archSalida, ExportarConfig conf)
            throws IOException, JDOMException {

        StringBuffer sb = getContenidoHTML(peliculas, conf);
        grabar(sb, archSalida);

        //parche ubuntu
        //cambio <IMG src='" + raizFotos + "\\"
        // por  <IMG src='" + raizFotos + "/" --linux--
        /*String win = "<IMG src='" + raizFotos + "\\\\";
        String lin = "<IMG src='" + raizFotos + "/";
        String sbLin = sb.toString().replaceAll(win, lin);
        archSalida = archSalida.replaceAll("\\.html", "_ub\\.html");
        grabar(new StringBuffer(sbLin), archSalida);
        */
    }

    private static void grabar(StringBuffer sb, String archSalida) throws IOException {

        File f = new File(archSalida);
        if (f.exists())
            f.delete();
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(sb.toString().getBytes());
        bos.close();
    }

    private static void agregarCabecera(StringBuffer sb) {
        sb.append("<HTML>" + Constantes.NUEVA_LINEA);
        sb.append("    <BODY bgcolor='#C0C0C0'>" + Constantes.NUEVA_LINEA);
    }

    private static void agregarPie(StringBuffer sb) {
        sb.append("    </<BODY>" + Constantes.NUEVA_LINEA);
        sb.append("</HTML>" + Constantes.NUEVA_LINEA);
    }

    private static StringBuffer armarTablaCritica(Pelicula p, String tabInicial, ExportarConfig conf){
        StringBuffer res = new StringBuffer();

        res.append(tabInicial + "<TABLE width='100%'>"+ Constantes.NUEVA_LINEA);
        String critica = p.getCritica();
        if (conf.isMostrarCritica() && critica != null){
            critica = reemplazarEnterPorBR(critica);
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top'>" +
                    critica +          "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);
        }
        res.append(tabInicial + "</TABLE>");

        return res;
    }

    private static StringBuffer armarTablaDetalles(Pelicula p, String tabInicial, ExportarConfig conf){
        StringBuffer res = new StringBuffer();

//      | Nombre [ - problemas ]    |                         |
//      | Año                       |                         |
//      | Imdb                      |                         |
//      | Duración                  |                         |
//      | Id                        |     <Crítica>           |
//      | País                      |                         |
        res.append(tabInicial + "<TABLE align='top'>"+ Constantes.NUEVA_LINEA);

        //año
        if (conf.isMostrarAnnus() && p.getAnnus() != null){
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' nowrap='true'>" +
                    p.getAnnus() +          "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);

        }

        //IMDB
        if (conf.isMostrarIMDB() && p.getImdbPuntaje() != null){
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' nowrap='true'>" +
                    p.getImdbPuntaje() +          "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);

        }
        //Duración
        if (conf.isMostrarDuracion() && p.getDuracion() != null){
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' nowrap='true'>" +
                    p.getDuracion() +          "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);

        }

        //Id
        if (conf.isMostrarId() && p.getId() != null){
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' nowrap='true'>" +
                    p.getId() +             "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);
        }

        //País
        if (conf.isMostrarPais() && p.getPais() != null){
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' nowrap='true'>" +
                    p.getPais() +           "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);
        }

        res.append(tabInicial + "</TABLE>");
        return res;
    }

    private static StringBuffer armarTablaDT(Pelicula p, String tabInicial, ExportarConfig conf){
        StringBuffer res = new StringBuffer();

    //      | Comen
    //      | Datos Tec
        res.append(tabInicial + "<TABLE align='top'>"+ Constantes.NUEVA_LINEA);

        //comentario
        if (conf.isMostrarComentarios() && p.getComentario() != null){
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' >" +
                    p.getComentario() +     "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);
        }

        //Datos Técnicos
        String datosTecnicos = p.getDatosTecnicos();
        if (conf.isMostrarDatosTecnicos() && datosTecnicos != null){
            String pr = p.getProblemas();
            if(pr != null)
                datosTecnicos += "<BR/>" + pr                       
                        ;

            datosTecnicos = reemplazarEnterPorBR(datosTecnicos);
            res.append(tabInicial + "    <TR align='left'>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "        <TD valign='top' >" +
                    datosTecnicos +     "</TD>" + Constantes.NUEVA_LINEA);
            res.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);
        }
        res.append(tabInicial + "</TABLE>");
        return res;
    }

    private static void agregarPelicula(Pelicula p, StringBuffer sb, String
            tabInicial, int cont, ExportarConfig conf) {
    //      --------------------------------------------
    //      | Nombre [probl] |  <<    >>               |
    //      |-------------------------------------------
    //      | Año            |                         |
    //      | Imdb           |                         |
    //      | Id             |     Posters             |
    //      | País           |                         |
    //      | Comen          |                         |
    //      | Duración       |                         |
    //      --------------------------------------------
    //      |  Crítica              |      Datos Tec.  |
    //      |                       |                  |
    //      -------------------------------------------

        //todo ¿agregar tags?
        String tabDet = "            ";
        StringBuffer tablaDetalles = armarTablaDetalles(p, tabInicial + tabDet, conf);
        StringBuffer tablaDatosTec = armarTablaDT(p, tabInicial + tabDet, conf);
        StringBuffer tablaCritica = armarTablaCritica(p, tabInicial + tabDet, conf);
        StringBuffer tablaPosters = armarTablaPosters(p, tabInicial, conf);
        StringBuffer tablaNombreYLinks = armarTablaNombreYLink(p, tabInicial, cont, conf);

        sb.append(tablaNombreYLinks + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "<Table width='100%'>" + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "    <TR valign='top'>" + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        <TD align><h6>" + Constantes.NUEVA_LINEA);
        sb.append(                          tablaDetalles + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        </h6></TD>" + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        <TD>" + Constantes.NUEVA_LINEA);
        sb.append(                          tablaPosters +Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        </TD>"+ Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "    </TR>" + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "</Table>" + Constantes.NUEVA_LINEA);

        sb.append(tabInicial + "<Table >" + Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        <TD>" + Constantes.NUEVA_LINEA);
        sb.append(                          tablaCritica +Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        </TD>"+ Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        <TD BORDER='5'>" + Constantes.NUEVA_LINEA);
        sb.append(                          tablaDatosTec +Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "        </TD>"+ Constantes.NUEVA_LINEA);
        sb.append(tabInicial + "</Table>" + Constantes.NUEVA_LINEA);

        sb.append(tabInicial + "<HR/>" + Constantes.NUEVA_LINEA);
    }

    private static StringBuffer armarTablaNombreYLink(Pelicula p, String tabInicial,
                                                      int cont, ExportarConfig conf) {
        StringBuffer res = new StringBuffer();
        res.append(tabInicial + "<TABLE width='800'>"+ Constantes.NUEVA_LINEA);
        res.append(tabInicial + "    <TR>"+ Constantes.NUEVA_LINEA);
        if (conf.isMostrarNombre()){
            res.append(tabInicial + "        <TD width='40%'>"+ p.getNombre());
            String pr = p.getProblemas();
            if (pr != null)
                res.append(" --- problemas ---");                        
            res.append("</TD> "+ Constantes.NUEVA_LINEA);
        }
        res.append(tabInicial + "        <TD ALIGN='right'> <A name='PRE_" + cont + "' HREF='#PRE_" + (cont-1) + "'>Anterior</A>" +  "</TD> "+ Constantes.NUEVA_LINEA);
        res.append(tabInicial + "        <TD ALIGN='left'> <A name='POS_" + cont + "' HREF='#POS_" + (cont+1) + "'>Siguiente</A>" +  "</TD> "+ Constantes.NUEVA_LINEA);

        res.append(tabInicial + "    </TR>"+ Constantes.NUEVA_LINEA);
        res.append(tabInicial + "</TABLE>");
        return res;
    }

    private static StringBuffer armarTablaPosters(Pelicula p, String tabInicial, ExportarConfig conf){
        StringBuffer res = new StringBuffer();
        res.append(tabInicial + "<TABLE>"+ Constantes.NUEVA_LINEA);
        res.append(tabInicial + "    <TR>"+ Constantes.NUEVA_LINEA);

        List<String> list = p.getPosters();
        if (conf.isMostrarPosters() && list != null){
            for (String poster : list) {
                res.append(tabInicial + "        <TD valign='top' > <IMG src='" +
                        raizFotos + "\\" + poster + "'/><BR/>" + Constantes.NUEVA_LINEA);
            }
        }

        res.append(tabInicial + "    </TR>"+ Constantes.NUEVA_LINEA);
        res.append(tabInicial + "</TABLE>");
        return res;
    }


    @SuppressWarnings({"UnusedDeclaration"})
    /*private void agregarPelicula2(Pelicula p, StringBuffer sb) {

        sb.append("    <TABLE width='100%'>"+ Constantes.NUEVA_LINEA);

        //primera fila
        sb.append("        <TR align='left'><TD valign='top' nowrap='true'>" +
                p.getNombre() + "</TD></TR>" + Constantes.NUEVA_LINEA);

        //segunda fila
        sb.append("        <TR align='left'><TD valign='top'>"+ Constantes.NUEVA_LINEA);
        sb.append("             <TABLE>"+ Constantes.NUEVA_LINEA);
        if (p.getAnnus() != null)
            sb.append("                 <TR align='left'><TD valign='top' nowrap='true' >"+
                p.getAnnus() + "</TD></TR>"+ Constantes.NUEVA_LINEA);

        if(p.getImdbPuntaje() != null)
            sb.append("<TR align='left'><TD valign='top' nowrap='true'>" +
                    p.getImdbPuntaje() + "</TD></TR>"+ Constantes.NUEVA_LINEA);
        if(p.getId() != null)
            sb.append("<TR align='left'><TD valign='top' nowrap='true'>" +
                    p.getId() + "</TD></TR>"+ Constantes.NUEVA_LINEA);

        if (p.getPais() != null)
            sb.append("<TR align='left'><TD valign='top' nowrap='true'>País:" +
                    p.getPais() + "</TD></TR>"+ Constantes.NUEVA_LINEA);

        if (p.getComentario() != null)
            sb.append("<TR align='left'><TD valign='top' nowrap='true'>" +
                    p.getComentario() + "</TD></TR>"+ Constantes.NUEVA_LINEA);
        if (p.getDatosTecnicos() != null)
            sb.append("<TR align='left'><TD valign='top' nowrap='true'>" +
                    p.getDatosTecnicos() + "</TD></TR>"+ Constantes.NUEVA_LINEA);

        sb.append("             </TABLE></TD>"+ Constantes.NUEVA_LINEA);

       List list = p.getPosters();
        if (list != null){
            for (Object aList : list) {
                String s = (String) aList;
                sb.append("             <TD valign='top' > <IMG src='" +
                        raizFotos + "\\" + s + "'/><BR/>" + Constantes.NUEVA_LINEA);
            }
        }


        String critica = p.getCritica();
        if (critica != null)
            sb.append("        <TD valign='top'>" +
                    reemplazarEnterPorBR(critica) + "</TD>" + Constantes.NUEVA_LINEA);
        sb.append("        </TR>");
        sb.append("    </TABLE>");
        sb.append("    <HR/>");

    }  */

    /**
     * Se reemplazon los 'enter' por su equivalente en html: <BR/>
     * @param critica
     * @return
     */
    private static String reemplazarEnterPorBR(String critica) {
        if (critica == null)
            return null;
        return critica.replaceAll(Constantes.NUEVA_LINEA, "<BR/>");
    }

}

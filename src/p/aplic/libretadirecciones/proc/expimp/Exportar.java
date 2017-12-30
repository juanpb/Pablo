package p.aplic.libretadirecciones.proc.expimp;

import org.jdom.JDOMException;
import p.aplic.libretadirecciones.bobj.Direccion;
import p.aplic.libretadirecciones.model.Model;
import p.util.Constantes;
import p.util.UtilString;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Exportar implements Constantes{

    public Exportar() {
    }

    public static void main(String[] args) throws IOException, JDOMException {
        List d = Model.getDirecciones();
        String sal = "C:\\dirs.txt";
        separadoPorComaParaImprimir(new File(sal), d);
        System.out.println("Listo");
        System.exit(0);
    }

    public static void comoHTML(String archSal, List dirs) throws IOException {
        comoHTML(new File(archSal), dirs);
    }

    public static void comoHTML(File archSal, List<Direccion> dirs) throws IOException {
        if (archSal.exists()){
            String msg = "El archivo " + archSal.getAbsolutePath() +
                    " ya existe. ¿Desea sobreescribir?";
            int res = JOptionPane.showOptionDialog(null, msg, "",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, null, null);
            if (res != JOptionPane.YES_OPTION){
                System.out.println("No se sobreescribe");
                return ;
            }
        }

        String txt = "<html>\n" +
                "  \t<head>\n" +
                "  \t\t<title>Direcciones</title>\n" +
                "  \t</head>\n" +
                "\t<body>\n" +
                "\t\t<table border='1'> \n" +
                "\t\t\t<tr bgcolor='#9acd32'>\n" +
                "\t\t\t\t<th align='center'>Nombre</th>\n" +
                "\t\t\t\t<th align='center'>Casa</th>\n" +
                "\t\t\t\t<th align='center'>Móvil</th>\n" +
                "\t\t\t\t<th align='center'>Trabajo</th>\n" +
                "\t\t\t\t<th align='center'>Correo</th>\n" +
                "\t\t\t\t<th align='center'>Dirección</th>\n" +
                "\t\t\t\t<th align='center'>Notas</th>\n" +
                "\t\t\t\t<th align='center'>Categoría</th>\n" +
                "\t\t\t</tr>\n";
        for (Direccion dir : dirs) {
            txt += "\t\t\t<tr>";
            String n = dir.getNombre() == null ? "" : dir.getNombre();
            n += " ";
            n += dir.getAppellido() == null ? "" : dir.getAppellido();

            txt += "\t\t\t\t<th align='left' nowrap = 'true' >" + n + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getTelCasa() == null ? "" : dir.getTelCasa()) + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getTelTrabajo() == null ? "" : dir.getTelTrabajo()) + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getMovil() == null ? "" : dir.getMovil()) + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getCorreo() == null ? "" : dir.getCorreo()) + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getDirec() == null ? "" : dir.getDirec()) + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getNotas() == null ? "" : dir.getNotas()) + "</th>\n" +
                    "\t\t\t\t<th align='left' nowrap = 'true' >" + (dir.getCategorias() == null ? "" : UtilString.getValoresSeparadosPorComa(dir.getCategorias()) ) + "</th>\n";
            txt += "\t\t\t</tr>\n";
        }
        txt += "\t</body>\n";
        txt += "\t</html>\n";

        FileOutputStream fos = new FileOutputStream(archSal);
        grabar(fos, txt);
    }

    public static void separadoPorTab(String archSal, List dirs) throws IOException {
        separadoPorTab(new File(archSal), dirs);
    }

    public static void separadoPorTab(File archSal, List dirs) throws IOException {
        if (archSal.exists()){
            String msg = "El archivo " + archSal.getAbsolutePath() +
                    " ya existe. ¿Desea sobreescribir?";
            int res = JOptionPane.showOptionDialog(null, msg, "",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, null, null);
            if (res != JOptionPane.YES_OPTION){
                System.out.println("No se sobreescribe");
                return ;
            }
        }
        FileOutputStream fos = new FileOutputStream(archSal);
        String separador  = TAB_CHARACTER + "";
        String aGrabar = generarSalida(separador, dirs);

        grabar(fos, aGrabar);
    }


    public static void separadoPorComa(String archSal, List dirs) throws IOException {
        separadoPorComa(new File(archSal), dirs);
    }

    public static void separadoPorComa(File archSal, List dirs) throws IOException {
        if (archSal.exists()){
            String msg = "El archivo " + archSal.getAbsolutePath() +
                    " ya existe. ¿Desea sobreescribir?";
            int res = JOptionPane.showOptionDialog(null, msg, "",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, null, null);
            if (res != JOptionPane.YES_OPTION){
                System.out.println("No se sobreescribe");
                return ;
            }
        }
        FileOutputStream fos = new FileOutputStream(archSal);
        String separador  = ", ";
        String aGrabar = generarSalida(separador, dirs, true);

        grabar(fos, aGrabar);
    }

    public static void separadoPorComaParaImprimir(String archSal, List dirs)
            throws IOException {
        separadoPorComaParaImprimir(new File(archSal), dirs);
    }
    public static void separadoPorComaParaImprimir(File archSal, List dirs)
            throws IOException {
        if (archSal.exists()){
            String msg = "El archivo " + archSal.getAbsolutePath() +
                    " ya existe. ¿Desea sobreescribir?";
            int res = JOptionPane.showOptionDialog(null, msg, "",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, null, null);
            if (res != JOptionPane.YES_OPTION){
                System.out.println("No se sobreescribe");
                return ;
            }
        }
        FileOutputStream fos = new FileOutputStream(archSal);
        String separador  = ", ";
        String aGrabar = generarSalidaParaImprimir(separador, dirs);

        grabar(fos, aGrabar);
    }


    private static void grabar(FileOutputStream fos, String aGrabar)
            throws IOException {
        try {
            fos.write(aGrabar.getBytes());
        } catch (IOException ex) {
            throw new IOException();
        }
        finally{
            if (fos != null)
                fos.close();
        }
    }



    private static String generarSalida(String separador, List dirs) {
        return generarSalida(separador,  dirs,  false);
    }

    private static String generarSalida(String separador, List<Direccion> dirs,
                                        boolean incluirSeparadorSiempre) {
        String res = "";
        res += "Nombre" + separador +
                "Apellido" + separador +
                "Casa"  + separador +
                "Trabajo"  + separador +
                "Móvil"  + separador +
                "Correo"  + separador +
                "Dirección"  + separador +
                "Notas"  + separador +
                "Categorías" + NUEVA_LINEA;

        for (Direccion d : dirs) {
            String str = d.getNombre();
            String comillas = "";//\"";
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas +separador;
            }
            str = d.getAppellido();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }

            str = d.getTelCasa();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }
            str = d.getTelTrabajo();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }
            str = d.getMovil();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }
            str = d.getCorreo();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }

            str = d.getDirec();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }

            str = d.getNotas();
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }

            str = UtilString.getValoresSeparadosPorComa(d.getCategorias());
            if (incluirSeparadorSiempre || (str != null && !str.equals(""))) {
                if (str == null)
                    str = "";
                res += comillas + str + comillas + separador;
            }
            if (res.endsWith(separador)) {
                res = res.substring(0, res.length() - separador.length());
            }
            res += NUEVA_LINEA;
        }
        return res;
    }

    private static String generarSalidaParaImprimir(String separador,
                                                    List<Direccion> dirs) {
        String res = "";
        res += "Apellido" + separador +
                "Nombre" + separador +
                "Casa"  + separador +
                "Trabajo"  + separador +
                "Móvil"  + separador +
//                "Correo"  + separador +
                "Dirección"  + separador +
                "Notas"  + separador + NUEVA_LINEA;

        for (Direccion d : dirs) {

            String str = d.getAppellido();
            if (str != null && !str.equals(""))
                res += str + separador;

            str = d.getNombre();
            if (str != null && !str.equals(""))
                res += str + separador;

            str = d.getTelCasa();
            if (str != null && !str.equals(""))
                res += str + separador;

            str = d.getTelTrabajo();
            if (str != null && !str.equals(""))
                res += "Trabajo: " + str + separador;

            str = d.getMovil();
            if (str != null && !str.equals(""))
                res += str + separador;

//            str = d.getCorreo();
//            if (str != null && !str.equals(""))
//                res += str + separador;

            str = d.getNotas();
            if (str != null && !str.equals(""))
                res += str + separador;

            str = d.getDirec();
            if (str != null && !str.equals(""))
                res += str + separador;

            if (res.endsWith(separador))
                res = res.substring(0, res.length() - separador.length());
            res += NUEVA_LINEA;
        }
        return res;
    }
}

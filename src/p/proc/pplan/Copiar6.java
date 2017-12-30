package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 dado:
 dir raiz,
 dir salida
 arch con lista de dirs o archivos a copiar.
 */
public class Copiar6 implements Aplicacion {
    private static final Logger logger = Logger.getLogger(Copiar6.class);

    private static final String NOMBRE = "Copiar";
    private static final String DIR_NUEVAS = "K:\\películas HD";
    private static final String DIR_VIEJAS = "J:\\películas HD";

    public static void main(String[] args) {
        String dirSalida = "D:\\Películas";
        String archivo = "F:\\tem\\p.txt";
        ejecutarPelis(dirSalida, archivo);
    }

    public static String ejecutarPelis(String dirRaizSalida, String archivo)  {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        int cont = 0;

        final List<String> archivoPorLinea;
        final List<String> falloCopia = new ArrayList<String>();
        try {
            archivoPorLinea = UtilFile.getArchivoPorLinea(archivo);
            int total = archivoPorLinea.size();

            String dirRaiz2;
            for (String f : archivoPorLinea) {

                if (!f.equals("")){
                    if (f.endsWith("10)") ||f.endsWith("11)") ||f.endsWith("12)") ||
                        f.endsWith("13)") ||f.endsWith("14)") ||f.endsWith("15)"))
                        dirRaiz2 = DIR_NUEVAS;
                    else
                        dirRaiz2 = DIR_VIEJAS;

                    System.out.println("Copiando " + (cont +1) + " / " + total + "( " + f + " )");
                    final File aCopiar = new File(dirRaiz2, f);
                    final File destino = new File(dirRaizSalida, f);
                    try {
                        if (aCopiar.isDirectory())
                            UtilFile.copiarDir(aCopiar, destino);
                        else
                            UtilFile.copiar(aCopiar, destino);
                    } catch (Exception e) {
                        falloCopia.add(f + " (" + aCopiar.getAbsolutePath() + ")");
                        cont--;
                        logger.error(e.getMessage(), e);
                    }

                    cont++;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            res.append("Error: ").append(e.getMessage());
            return res.toString();
        }
        if (falloCopia.size() > 0){
            System.out.println("Fallaron: ");
            for (String s : falloCopia) {
                System.out.println(s);
            }
        }
        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Cant. de copiados: " + cont).append(Constantes.NUEVA_LINEA);
        res.append("Demoró " + ms + " ms");

        return res.toString();
    }

    public String ejecutar(String dirRaiz, String dirRaizSalida, String archivo)  {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        int cont = 0;

        final List<String> archivoPorLinea;
        try {
            archivoPorLinea = UtilFile.getArchivoPorLinea(archivo);


            for (String f : archivoPorLinea) {
                if (!f.equals("")){
                    final File aCopiar = new File(dirRaiz, f);
                    final File destino = new File(dirRaizSalida, f);
                    if (aCopiar.isDirectory())
                        UtilFile.copiarDir(aCopiar, destino);
                    else
                        UtilFile.copiar(aCopiar, destino);

                    cont++;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            res.append("Error: ").append(e.getMessage());
            return res.toString();
        }
        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Cant. de copiados: " + cont).append(Constantes.NUEVA_LINEA);
        res.append("Demoró " + ms + " ms");

        return res.toString();
    }

    public String ejecutar(List<String> params)  {

        String dirRaiz = params.get(0);
        String dirRaizSalida     = params.get(1);
        String archivo        = params.get(2);

        return ejecutar(dirRaiz, dirRaizSalida, archivo);
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Dir raíz");
        res.add(p1);
        final Param p2 = new Param("Dir raíz destino");
        res.add(p2);

        final Param p3 = new Param("Archivo con lista a copiar");
        res.add(p3);

        return res;
    }

}

package p.aplic.peliculas.util;

import p.util.DirFileFilter;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 18/02/15
 * Time: 14:11
 */
public class MoverPelis {
    private List<Rango> rangos = null;
    private boolean mover = false;

    public static void main(String[] args) {
        new MoverPelis(args);
    }

    private MoverPelis(String[] args) {
        if (args.length < 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }
        if (args.length > 1){
            if (args[1].equals("MOVER")){
                mover = true;
            }
        }
        String configPath = args[0];
        p.util.Util.loadProperties(configPath);

        crearRangos();
        revisarRangos();

    }

    private void revisarRangos() {
        for (Rango rango : rangos) {
            revisarRango(rango);
        }
    }

    private void revisarRango(Rango rango) {
        int contOk = 0;
        String raiz = rango.getRaiz();
        File raizF = new File(raiz);
        File[] pelis = raizF.listFiles(new DirFileFilter(true));
        for (File peli : pelis) {
            if (!estaEnRango(peli, rango)){
                System.out.println("Fuera de rango, Peli: '" + peli.getName() + "', rango actual: " + rango.getRaiz());
                if (mover){
                    String raizDest = getRaiz(getAnnus(peli.getName()));
                }
            }
            else
                contOk++;
        }
        System.out.println("Cant. de pelis en rango en '" + raiz + "': " + contOk);
    }

    private String getRaiz(int annus) {
        for (Rango rango : rangos) {
            if (rango.pertenece(annus))
                return rango.getRaiz();
        }
        throw new RuntimeException("no se encontró la raiz para el año '" + annus + "'" );
    }

    private boolean estaEnRango(File peli, Rango rango) {
        String name = peli.getName();
        int annus = getAnnus(name);
        return rango.getDesde() <=  annus && annus <= rango.getHasta();
    }

    private int getAnnus(String name) {
        try {
            //ej: name = nombre de la peli (1977)
            return Integer.parseInt(name.substring(name.length() - 5, name.length() - 1));
        } catch (NumberFormatException e) {
            System.out.println("Problemas con name = " + name);
            return 0;
        }
    }

    private void crearRangos(){
        int cont = 1;
        boolean listo = false;
        rangos = new ArrayList<Rango>();
        while (!listo){
            String dirRaizCarpetaHD = System.getProperty("dirRaizCarpetaHD" + cont);

            if (dirRaizCarpetaHD == null)  {
                listo = true;
            }
            else {
                //formato (0-2009)L:\\películas\\
                int j = dirRaizCarpetaHD.indexOf(")");
                String años = dirRaizCarpetaHD.substring(1, j);
                int d = Integer.parseInt(años.substring(0, años.indexOf("-")));
                int h = Integer.parseInt(años.substring(años.indexOf("-")+1));
                String raiz = dirRaizCarpetaHD.substring(j+1);
                Rango rango = new Rango(d, h, raiz);
                rangos.add(rango);
            }
            cont++;
        }
    }
}

class Rango{
    private int desde;
    private int hasta;
    private String raiz;

    Rango(int desde, int hasta, String raiz) {
        this.desde = desde;
        this.hasta = hasta;
        this.raiz = raiz;
    }

    public boolean pertenece (int annus){
        return desde <= annus && annus <= hasta;
    }

    public int getDesde() {
        return desde;
    }

    public int getHasta() {
        return hasta;
    }

    public String getRaiz() {
        return raiz;
    }
}
package p.proc.pplan;

import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 */
public class MoverCero implements Aplicacion {


    private static final String NOMBRE = "Mover Cero";

    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();

        String dirEntrada = params.get(0);
        String dirSalida     = params.get(1);
        int mov = 0;

        File f = new File(dirEntrada);
        final File[] files = f.listFiles();
        for (File file : files) {
            if (file.isFile() && file.length() == 0){
                file.renameTo(new File(dirSalida, file.getName()));
                mov++;
            }
        }

        long fin = System.currentTimeMillis();
        long ms = (fin - ini);


        res.append("Dir Entrada: " + dirEntrada).append(Constantes.NUEVA_LINEA);
        res.append("Dir salida: " + dirSalida).append(Constantes.NUEVA_LINEA);
        res.append("Cant. movidos: " + mov).append(Constantes.NUEVA_LINEA);
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

        final Param p1 = new Param("Dir Entrada");
        res.add(p1);
        final Param p2 = new Param("Dir Salida");
        res.add(p2);

        return res;
    }

}

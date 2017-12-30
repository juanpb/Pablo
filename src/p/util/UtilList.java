package p.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 30/04/13
 * Time: 10:04
 */
public class UtilList {

    /**
     * Elimina de la primera lista, todos los elementos de la segunda lista.
     * Considera que son iguales 2 strings si son iguales hasta la posición 'validarHasta'
     * @param archAModificar
     * @param archAEliminar
     * @param validarHasta posición hasta la cual se validará si son iguales dos String. si está en 0 valida toda la
     *                     línea.
     * @param eliminarRepetidos si está en false elimina a lo sumo un elemento de la listaAModificar. En true elimina
     *                          todos.
     */
    public static void eliminar(String archAModificar, String archAEliminar, int validarHasta, boolean eliminarRepetidos) throws IOException {
        eliminar(new File(archAModificar), new File(archAEliminar), validarHasta, eliminarRepetidos);
    }


    /**
     * Elimina de la primera lista, todos los elementos de la segunda lista.
     * Considera que son iguales 2 strings si son iguales hasta la posición 'validarHasta'
     * @param archAModificar
     * @param archAEliminar
     * @param validarHasta posición hasta la cual se validará si son iguales dos String. si está en 0 valida toda la
     *                     línea.
     * @param eliminarRepetidos si está en false elimina a lo sumo un elemento de la listaAModificar. En true elimina
     *                          todos.
     */
    public static void eliminar(File archAModificar, File archAEliminar, int validarHasta, boolean eliminarRepetidos) throws IOException {
        final List<String> arch1 = UtilFile.getArchivoPorLinea(archAModificar);
        final List<String> arch2 = UtilFile.getArchivoPorLinea(archAEliminar);

        int tamOrig = arch1.size();
        int tamAEliminar = arch2.size();

        eliminar(arch1, arch2, validarHasta, eliminarRepetidos);


        String archSalida = archAModificar.getAbsolutePath() + "_eliminado" ;
        System.out.println("Archivos de salida: " + archSalida);


        if (tamOrig - tamAEliminar != arch1.size()){
            System.out.println("No se eliminaron todos los registros del arch2");
        }

        final int cantElim = tamOrig - arch1.size();
        System.out.println("Cantidad de registros eliminados: " + cantElim);
        if(cantElim == 0){
            System.out.println("no se graba nada");
        }
        else {

            final File f = new File(archSalida);
            if (f.exists()){
                throw new RuntimeException ("Ya existe el archivo de Salida: " + f.getAbsolutePath() + ". No se graba nada.");
            }
            else
                UtilFile.guardartArchivoPorLinea(f,  arch1, false);
        }
    }


    /**
     * Elimina de la primera lista, todos los elementos de la segunda lista.
     * Considera que son iguales 2 strings si son iguales hasta la posición 'validarHasta'
     * @param listaAModificar
     * @param listaAEliminar
     * @param validarHasta posición hasta la cual se validará si son iguales dos String. si está en 0 valida toda la
     *                     línea.
     * @param eliminarRepetidos si está en false elimina a lo sumo un elemento de la listaAModificar. En true elimina
     *                          todos.
     */
    public static void eliminar(List<String> listaAModificar, List<String> listaAEliminar, int validarHasta, boolean eliminarRepetidos) {

        if (validarHasta == 0){
            if (eliminarRepetidos)
                listaAModificar.removeAll(listaAEliminar);
            else{
                //eliino de a uno
                for (String s : listaAEliminar) {
                    final int i = listaAModificar.indexOf(s);
                    if (i >= 0)
                        listaAModificar.remove(i);
                }
            }
        }
        else{

            for (String s : listaAEliminar) {
                final String s2 = s.substring(0, validarHasta);
                for (String orig : listaAModificar) {

                    if (orig.startsWith(s2)){
                        listaAModificar.remove(orig);
                        if (!eliminarRepetidos)
                            break;
                    }
                }
            }
        }
    }

    /**
     * Devuelve una lista con los elementos de la lista recibida que cumplan con el filtro en la posición indicada.
     *
     * @param lineas
     * @param desde
     * @param hasta
     * @param valor
     * @return
     */
    public static List<String> filtrarPorValor(List<String> lineas, int desde, int hasta, String valor){
        List<String> res = new ArrayList<String>();

        for (String s : lineas) {
            if (s.length() >= hasta)
                if (s.substring(desde, hasta).equals(valor))
                    res.add(s);
        }
        return res;
    }

    /**
     * Devuelve una lista con los elementos de la lista recibida que no cumplan con el filtro en la posición indicada.
     *
     * @param lineas
     * @param desde
     * @param hasta
     * @param valor
     * @return
     */
    public static List<String> excluirPorValor(List<String> lineas, int desde, int hasta, String valor){
        List<String> res = new ArrayList<String>();

        for (String s : lineas) {
            if (s.length() >= hasta
                && s.substring(desde, hasta).equals(valor))
                ;
            else
                res.add(s);
        }
        return res;
    }
}

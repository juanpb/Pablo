package p.aplic.peliculas;

import java.util.EventListener;

/**
 * User: Administrador
 * Date: 03/02/2007
 * Time: 15:30:09
 */
public interface PeliculasListener  extends EventListener {

    void agregar(Pelicula p);
    void modificar(Pelicula p);
    void borrar(Pelicula p);
}

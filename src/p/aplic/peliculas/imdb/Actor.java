package p.aplic.peliculas.imdb;

import p.aplic.peliculas.imdb.loaders.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Apr 17, 2009
 * Time: 12:56:04 PM
 */
public class Actor {
    private String nombre;
    private List<Actuacion> pelis;

    public Actor() {
    }

    public Actor(String nombre, List<Actuacion> pelis) {
        setNombre(nombre);
        this.pelis = pelis;
    }

    public void addPelis(List<Actuacion> p) {
        if (pelis == null)
             pelis = new ArrayList<Actuacion>();

        pelis.addAll(p);
    }

    public void addPeli(Actuacion p) {
        if (pelis == null)
             pelis = new ArrayList<Actuacion>();

        pelis.add(p);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Util.truncar(nombre, 200);
    }

    public List<Actuacion> getPelis() {
        return pelis;
    }

    public void setPelis(List<Actuacion> pelis) {
        this.pelis = pelis;
    }
}
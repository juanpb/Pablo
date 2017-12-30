package p.aplic.peliculas.imdb;

import p.aplic.peliculas.imdb.loaders.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Apr 17, 2009
 * Time: 12:56:04 PM
 */
public class Persona {
    private String nombre;
    private List<Peli> pelis;

    public Persona() {

    }
    public Persona(String nombre, List<Peli> pelis) {
        setNombre(nombre);
        this.pelis = pelis;
    }

    public void addPelis(List<Peli> p) {
        if (pelis == null)
             pelis = new ArrayList<Peli>();

        pelis.addAll(p);
    }

    public void addPeli(Peli p) {
        if (pelis == null)
             pelis = new ArrayList<Peli>();

        pelis.add(p);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Util.truncar(nombre, 200);
    }

    public List<Peli> getPelis() {
        return pelis;
    }

    public void setPelis(List<Peli> pelis) {
        this.pelis = pelis;
    }
}

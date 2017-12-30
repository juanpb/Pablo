package p.aplic.peliculas.imdb;

import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.imdb.loaders.Util;

import java.util.Set;

/**
 * User: JPB
 * Date: Apr 15, 2009
 * Time: 10:23:58 AM
 */
public class Peli {
    private String nombre;
    private String a�o;
    private String linea;
    private String id;

    public Peli(String nombre, String a�o, String linea) {
        this(nombre, a�o,  linea,  null);
    }
    public Peli(String nombre, String a�o, String linea, String id) {
        setNombre(nombre);
        setA�o(a�o);
        this.linea = linea;
        setId(id);
    }

    public Peli(String n, String a) {
        this(n, a, null);
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre.endsWith(", A"))
            nombre = "A " + nombre.substring(0, nombre.length()-3);
        if (nombre.endsWith(", An"))
            nombre = "An " + nombre.substring(0, nombre.length()-4);
        if (nombre.endsWith(", Das"))
            nombre = "Das " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", Der"))
            nombre = "Der " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", Die"))
            nombre = "Die " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", El"))
            nombre = "El " + nombre.substring(0, nombre.length()-4);
        if (nombre.endsWith(", Il"))
            nombre = "Il " + nombre.substring(0, nombre.length()-4);
        if (nombre.endsWith(", L'"))
            nombre = "L'" + nombre.substring(0, nombre.length()-4);
        if (nombre.endsWith(", La"))
            nombre = "La " + nombre.substring(0, nombre.length()-4);
        if (nombre.endsWith(", Las"))
            nombre = "Las " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", Le"))
            nombre = "Le " + nombre.substring(0, nombre.length()-4);
        if (nombre.endsWith(", Les"))
            nombre = "Les " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", Los"))
            nombre = "Los " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", The"))
            nombre = "The " + nombre.substring(0, nombre.length()-5);
        if (nombre.endsWith(", O"))
            nombre = "O " + nombre.substring(0, nombre.length()-3);
        if (nombre.endsWith(", Un"))
            nombre = "Un " + nombre.substring(0, nombre.length()-4);

        this.nombre = Util.truncar(nombre, 200);;
    }

    public String getA�o() {
        return a�o;
    }

    public void setA�o(String a�o) {
        if (a�o.length()>4)
            a�o = a�o.substring(0,4);
        this.a�o = a�o;
    }

    @SuppressWarnings({"NonFinalFieldReferenceInEquals"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peli peli = (Peli) o;

        if (a�o != null ? !a�o.equals(peli.a�o) : peli.a�o != null)
            return false;
        return !(nombre != null ? !nombre.equals(peli.nombre) : peli.nombre != null);

    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getId() {
        return id;
    }
    public int getIdAsInt() {
        try {
            return Integer.parseInt(getId());
        }
        catch (Exception ex) {
            return 0;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings({"NonFinalFieldReferencedInHashCode"})
    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.hashCode() : 0;
        result = 31 * result + (a�o != null ? a�o.hashCode() : 0);
        return result;
    }

    public static boolean contiene(Set<Peli> pelis, Pelicula p) {
        for(Peli p2 : pelis) {
            if (sonLaMisma(p2, p))
                return true;
        }
        return false;
    }

    private static boolean sonLaMisma(Peli p2, Pelicula p) {
        try {
            String nomMio = p.getNombre();
            return nomMio.equalsIgnoreCase(p2.getNombre()) && p.getAnnus().equals(p2.getA�o());
        } catch (Exception e) {
            System.out.println("Problemas al comparar: ");
            System.out.println("p.getNombre() = " + p.getNombre());
            System.out.println("p.getA�o() = " + p.getAnnus());
            System.out.println("p2.getNombre() = " + p2.getNombre());
            System.out.println("p2.getA�o() = " + p2.getA�o());
            return false;
        }
    }

    public static Peli get(Set<Peli> pelis, Pelicula p) {
        for(Peli p2 : pelis) {
            if (sonLaMisma(p2, p))
                return p2;
        }
        return null;
    }

    public void setId(int id) {
        this.id = id + "";
    }
}

package p.aplic.peliculas;

import java.util.List;

/**
 * User:
 * Date: 21/11/2019
 * Time: 10:07
 */
public class Tag {
    private String id;
    private String nombre;

    public Tag(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!id.equals(tag.id)) return false;
        if (nombre != null ? !nombre.equals(tag.nombre) : tag.nombre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }

    public static boolean estaEnLista(String nombre, List<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getNombre().equals(nombre))
                return true;
        }
        return false;
    }

    public static Tag crearTag(String id, List<Tag> tags) {
        String nombre = getNombre(id, tags);
        if (nombre == null)
            throw new RuntimeException("No existe el tagId = " + id);
        return new Tag(id, nombre);
    }
    public static String getNombre(String id, List<Tag> tags) {
        for (Tag tag : tags) {
            if ((tag.getId()).equals(id))
                return tag.getNombre();
        }
        return null;
    }

    public static String getId(String n, List<Tag> tags) {
        for (Tag tag : tags) {
            if ((tag.getNombre() ).equals(n))
                return tag.getId();
        }
        return null;
    }
}

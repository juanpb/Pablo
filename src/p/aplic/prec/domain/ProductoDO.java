package p.aplic.prec.domain;

/**
 * User: JPB
 * Date: Apr 28, 2008
 * Time: 10:33:15 AM
 */
public class ProductoDO {
    final private int id;
    private String descripcion;
    private boolean imprimir=true;


    public ProductoDO(int id) {
        this.id = id;
    }

    public ProductoDO(int id, String descripcion, boolean imprimir) {
        this.id = id;
        this.descripcion = descripcion;
        this.imprimir = imprimir;
    }


    public int getId() {        return id;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public String toString() {
        return getDescripcion();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProductoDO that = (ProductoDO) o;

        return id == that.id;

    }

    public int hashCode() {
        return id;
    }
}

package p.aplic.prec.domain;

/**
 * User: JPB
 * Date: Apr 28, 2008
 * Time: 10:33:05 AM
 */
public class SuperDO {
    final private int id;
    private String descripcion;
    private boolean imprimir;
    private int porcentajeDescuento = 0;

    public SuperDO(int id) {
        this(id, null);
    }

    public SuperDO(int id, String descripcion) {
        this(id, descripcion, true);
    }
    public SuperDO(int id, String descripcion, boolean imprimir) {
        this.id = id;
        this.descripcion = descripcion;
        this.imprimir = imprimir;
    }


    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String toString() {
        return getDescripcion();
    }


    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SuperDO that = (SuperDO) o;

        return id == that.id;

    }

    public int hashCode() {
        return id;
    }

    public int getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(int porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}

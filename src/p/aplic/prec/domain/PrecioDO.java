package p.aplic.prec.domain;

import java.util.Date;

/**
 * User: JPB
 * Date: Apr 28, 2008
 * Time: 10:33:27 AM
 */
public class PrecioDO {
    private ProductoDO producto;
    private Date fecha;
    private double precio;
    private  SuperDO superM;


    public PrecioDO() {
        
    }
    public PrecioDO(ProductoDO producto, double precio, SuperDO superM, Date fecha) {
        this.producto = producto;
        this.precio = precio;
        this.superM = superM;
        this.fecha = fecha;        
    }


    public ProductoDO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDO producto) {
        this.producto = producto;
    }

    public double getPrecio() {
        return precio;
    }

    public double getPrecioConDesc() {
        final int porc = superM.getPorcentajeDescuento();
        if (porc != 0){
            return precio * (100-porc) * 0.01;
        }
        else
            return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public SuperDO getSuperM() {
        return superM;
    }

    public void setSuperM(SuperDO superM) {
        this.superM = superM;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

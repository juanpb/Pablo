package p.proc.i26;

/**
 * User: JPB
 * Date: 24/05/16
 * Time: 14:07
 */
public class Registro {
    private String MaterialID;
    private String warehouseID;
    private String resourceID;
    private String resourceType;
    private String orderID;
    private String orderActionID;

    public Registro(String material, String almacen, String nroSerie) {
        setMaterialID(material);
        setWarehouseID(almacen);
        setResourceID(nroSerie);
    }


    public String getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(String materialID) {
        MaterialID = materialID;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderActionID() {
        return orderActionID;
    }

    public void setOrderActionID(String orderActionID) {
        this.orderActionID = orderActionID;
    }

    public static Registro parsear(String linea) {
        String[] split = linea.split(";");
        //Material;Centro;Almacén;Número de serie
        //return new Registro(split[0], split[2], split[3]);

        //Almacén;Material;Número de serie
//        return new Registro(split[1], split[0], split[2]);

        //material ;Número de serie; almacen
        String mat =  split[0];
        String nro =  split[1];
        String alm =  split[2];


        return new Registro(mat, alm, nro);
    }

    public String toString() {
        return "Registro{" +
                "MaterialID='" + MaterialID + '\'' +
                ", warehouseID='" + warehouseID + '\'' +
                ", resourceID='" + resourceID + '\'' +
                '}';
    }

    public String generarSalida() {
        StringBuilder res = new StringBuilder();
        // salida.add("warehouseID;MaterialID;orderID;orderActionID;resourceType;resourceID");
        res.append(getWarehouseID()).append(";");
        res.append(getMaterialID()).append(";");
        res.append(getOrderID()).append(";");
        res.append(getOrderActionID()).append(";");
        res.append(getResourceType()).append(";");
        res.append(getResourceID());
        return res.toString();
    }
}

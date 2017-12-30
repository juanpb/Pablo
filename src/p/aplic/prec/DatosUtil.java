package p.aplic.prec;

import p.aplic.prec.domain.PrecioDO;
import p.aplic.prec.domain.ProductoDO;
import p.aplic.prec.domain.SuperDO;
import p.util.Constantes;
import p.util.Fechas;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * User: JPB
 * Date: Apr 30, 2008
 * Time: 3:12:00 PM
 */
public class DatosUtil {
    private List<PrecioDO> precios;

    public DatosUtil(List<PrecioDO> precios) {
        this.precios = precios;
    }


    public String getUltimoPrecioParaPlanilla(ProductoDO prod, SuperDO sup){
        PrecioDO up = getUltimoPrecio(prod, sup);
        if (up == null)
            return "";
        else{
            String f = getFechaParaPlanilla(up.getFecha());
            return f + " " + getImporteParaPlanilla(up.getPrecio());
        }
    }

    public String getUltimoPrecioParaPlanillaConDesc(ProductoDO prod, SuperDO sup){
        PrecioDO up = getUltimoPrecio(prod, sup);
        if (up == null)
            return "";
        else{
            return getImporteParaPlanilla(up.getPrecioConDesc());
        }
    }

    private String getImporteParaPlanilla(double d){
        final DecimalFormat df = new DecimalFormat("###.##");
        return df.format(d);
    }
    
    private String getFechaParaPlanilla(Date d){
        return "(" + Fechas.getDDMMAA("/", d.getTime(), true) + ")";
    }

    private PrecioDO getUltimoPrecio(ProductoDO prod, SuperDO sup){
        List<PrecioDO> list = getPrecios(prod, sup);
        int cant = list.size();
        if (cant > 0){
            return list.get(0);
        }
        else
            return null;
    }

    //Devuelve lista de precios ordenados por fecha. El primero es el más nuevo
    public List<PrecioDO> getPrecios(ProductoDO prod, SuperDO sup){
        List<PrecioDO> res = new ArrayList<PrecioDO>();

        for (PrecioDO p : precios) {
            if (p.getProducto().equals(prod)
                    && p.getSuperM().equals(sup))
                res.add(p);
        }

        //Ordeno por fecha
         //noinspection EqualsAndHashcode
        Collections.sort(res, new Comparator<PrecioDO>(){
            public int compare(PrecioDO o1, PrecioDO o2){
                return o2.getFecha().compareTo(o1.getFecha());
            }

            public boolean equals(Object obj){
                return obj instanceof PrecioDO;
            }
        });


        return res;
    }

    /**
     * Devuelve un String con csv
     *
     * |    | sup1 | sup 2| sup 3|
     * |Pr1 | (f)$ | (f)$ |(f)$
     * @return
     */
    public String getPlanilla() throws IOException {
        StringBuffer res = new StringBuffer();

        char t = Constantes.TAB_CHARACTER;
        String n = Constantes.NUEVA_LINEA;

        List<SuperDO> supers = Model.getSupers();
        res.append(t);
        for (SuperDO s : supers) {
            if (s.isImprimir()){
                res.append(s.getDescripcion()).append(t);
                if(s.getPorcentajeDescuento() != 0)
                    res.append(s.getPorcentajeDescuento()).append(t);
            }
        }
        res.append(n);

        List<ProductoDO> prods = Model.getProductos();
        //para cada prod, busco precios x cada super
        for (ProductoDO p : prods) {
            if (p.isImprimir()){
                res.append(p.getDescripcion()).append(t);
                for (SuperDO s : supers) {
                    if (s.isImprimir()){
                        String up = getUltimoPrecioParaPlanilla(p, s);
                        res.append(up).append(t);
                        if(s.getPorcentajeDescuento() != 0)
                            res.append(getUltimoPrecioParaPlanillaConDesc(p,s)).append(t);
                    }
                }
                res.append(n);
            }
        }

        return res.toString();
    }
}

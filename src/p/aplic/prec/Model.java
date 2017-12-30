package p.aplic.prec;

import org.apache.log4j.Logger;
import p.aplic.prec.domain.PrecioDO;
import p.aplic.prec.domain.ProductoDO;
import p.aplic.prec.domain.SuperDO;
import p.util.Fechas;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: Apr 28, 2008
 * Time: 10:22:42 AM
 */
public class Model {
    private static final Logger logger = Logger.getLogger(Model.class);

    private static String prodsPath = System.getProperty("archivoProductos");
    private static String supersPath = System.getProperty("archivoSuper");
    private static String dataPath = System.getProperty("archivoData");
    private static String salidaPath = System.getProperty("planillaPath");

    private static Map<String,SuperDO> cacheSuper;
    private static Map<String,ProductoDO> cacheProductos;
    private static DatosUtil util;

    public static  String getUltimoPrecioParaPlanilla(ProductoDO prod,
                                                      SuperDO sup) {
        //todo: queda desactualizado
        if (util == null){
            try {
                util = new DatosUtil(getPrecios());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        return util.getUltimoPrecioParaPlanilla(prod, sup);
    }

    public static  List<PrecioDO> getPrecios(ProductoDO prod, SuperDO sup) {
        //todo: queda desactualizado
        if (util == null){
            try {
                util = new DatosUtil(getPrecios());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        return util.getPrecios(prod, sup);
    }

    public static List<PrecioDO> getPrecios() throws IOException {
        List<PrecioDO> res;
        List<String> list = getLista(dataPath);
        res = string2Precios(list);
        return res;
    }

    public static String generarSalida() throws IOException {
        DatosUtil du = new DatosUtil(getPrecios());
        String txt = du.getPlanilla();
        UtilFile.crearArchivo(txt, salidaPath);
        return salidaPath;
    }
    
    public static void modificarProducto(ProductoDO p) throws IOException {
        cacheProductos.put(Integer.toString(p.getId()), p);

        List<ProductoDO> ps = new ArrayList<ProductoDO>();
        int idMax = getIdProductoNuevo();
        for (int i = 0; i < idMax; i++) {
            ProductoDO pr = cacheProductos.get(Integer.toString(i));
            if (pr != null)
                ps.add(pr);
        }
        grabarProductos(ps);
    }

    private static void grabarProductos(List<ProductoDO> prods)
            throws IOException {
        List<String> datos = new ArrayList<String>();
        for (ProductoDO p : prods) {
            datos.add(prod2String(p));
        }
        UtilFile.guardartArchivoPorLinea(new File(prodsPath), datos, true);
    }

    public static void agregarProducto(ProductoDO prod) throws IOException {
        String x = prod2String(prod);
        UtilFile.agregarAlFinal(x, new File(prodsPath));
        llenarCacheProductos(prod);
    }

    public static void agregarPrecios(List<PrecioDO> list) throws IOException {
        File f = new File(dataPath);
        List<String> lista = precios2String(list);
        UtilFile.guardartArchivoPorLinea(f, lista);
    }

    private static List<String> precios2String(List<PrecioDO> list){
        List<String> res = new ArrayList<String>();
        for (PrecioDO p : list) {
            String x = p.getSuperM().getId() + ";" +
                    Fechas.getDDMMAA("-", p.getFecha().getTime())
                     + ";" + p.getProducto().getId() + ";" + p.getPrecio();
            res.add(x);
        }
        return res;
    }

    private static SuperDO getSuper(String superId){
        return cacheSuper.get(superId);
    }

    /**
     * Se fija cuál es el último id usado y devuelve el último más uno.
     * @return
     */
    public static int getIdProductoNuevo(){
        Collection<ProductoDO> ps = cacheProductos.values();
        int maxId = -1;
        for (ProductoDO p : ps) {
            int tmp = p.getId();
            if (tmp > maxId)
                maxId = tmp;
        }
        return maxId+1;
    }

    private static ProductoDO getProducto(String prodId){
        return cacheProductos.get(prodId);
    }

    private static List<PrecioDO> string2Precios(List<String> list){
        List<PrecioDO> res = new ArrayList<PrecioDO>();
        for (String p : list) {
            PrecioDO pr = new PrecioDO();
            StringTokenizer st = new StringTokenizer(p, ";");
            pr.setSuperM(getSuper(st.nextToken()));
            String d = st.nextToken();
            Date dateDD_mm_aa = Fechas.getDateDD_MM_AA(d);
            pr.setFecha(dateDD_mm_aa);
            pr.setProducto(getProducto(st.nextToken()));
            pr.setPrecio(Double.parseDouble(st.nextToken()));

            res.add(pr);
        }
        return res;
    }

    public static List<ProductoDO> getProductos() throws IOException {
        List<String> list = getLista(prodsPath);
        List<ProductoDO> productos = toProductos(list);

        ordenar(productos);
        llenarCacheProductos(productos);
        return productos;
    }

    private static void ordenar(List<ProductoDO> productos) {
        //noinspection EqualsAndHashcode
        Collections.sort(productos, new Comparator<ProductoDO>(){
            public int compare(ProductoDO o1, ProductoDO o2){
                String d1 = o1.getDescripcion().toLowerCase();
                String d2 = o2.getDescripcion().toLowerCase();
                return d1.compareTo(d2);
            }

            public boolean equals(Object obj){
                return obj instanceof ProductoDO;
            }
        });
    }

    public static List<SuperDO> getSupers() throws IOException {
        List<String> list = getLista(supersPath);
        List<SuperDO> supers = toSuper(list);
        llenarCacheSupers(supers);
        return supers;
    }

    private static void llenarCacheProductos(ProductoDO p){
        List<ProductoDO> a = new ArrayList<ProductoDO>();
        a.add(p);
        llenarCacheProductos(a);
    }
    private static void llenarCacheProductos(List<ProductoDO> prods){
        cacheProductos = new HashMap<String, ProductoDO>(prods.size());
        for (ProductoDO s : prods) {
            cacheProductos.put(Integer.toString(s.getId()), s);
        }
    }

    private static void llenarCacheSupers(List<SuperDO> supers){
        cacheSuper = new HashMap<String, SuperDO>(supers.size());
        for (SuperDO s : supers) {
            cacheSuper.put(Integer.toString(s.getId()), s);
        }
    }

    private static List<String> getLista(String path) throws IOException {
        return UtilFile.getArchivoPorLinea(path, "//");
    }

    private static List<SuperDO> toSuper(List<String> list){
        List<SuperDO> res = new ArrayList<SuperDO>();
        for (String s : list) {
            if (!s.startsWith("/") && !s.startsWith("-") && !s.trim().equals("")){
                StringTokenizer st = new StringTokenizer(s, ";");
                int id = Integer.parseInt(st.nextToken());
                String desc = st.nextToken();
                String b2 = st.nextToken();
                boolean imp = string2Boolean(b2);;
                SuperDO su = new SuperDO(id, desc, imp);
                if (st.hasMoreTokens()){
                    b2 = st.nextToken();
                    final int porc = Integer.parseInt(b2);
                    su.setPorcentajeDescuento(porc);
                }

                res.add(su);
            }
        }
        return res;
    }

    private static List<ProductoDO> toProductos(List<String> list){
        List<ProductoDO> res = new ArrayList<ProductoDO>();
        for (String s : list) {
            if (s != null && !s.startsWith("//") && !s.trim().equals("")){

                StringTokenizer st = new StringTokenizer(s, ";");
                int id = Integer.parseInt(st.nextToken());
                String desc = st.nextToken();
                String b2 = st.nextToken();
                boolean b = string2Boolean(b2);
                ProductoDO su = new ProductoDO(id, desc, b);
                res.add(su);
            }
        }
        return res;
    }

    private static boolean string2Boolean(String b2) {
        return (b2.equalsIgnoreCase("v")) || (b2.equalsIgnoreCase("t"));
    }

    private static String prod2String(ProductoDO p){
        String res = p.getId() + ";" + p.getDescripcion() + ";";
        if (p.isImprimir())
            res += "v";
        else
            res += "f";
      
        return res;
    }
}
